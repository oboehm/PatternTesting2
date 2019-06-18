/*
 * $Id: ParallelProxyRunner.java,v 1.4 2016/12/18 21:56:49 oboehm Exp $
 *
 * Copyright (c) 2011 by Oliver Boehm
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express orimplied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * (c)reated 18.12.2011 by oliver (ob@oasd.de)
 */

package patterntesting.concurrent.junit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import patterntesting.annotation.check.runtime.MayReturnNull;
import patterntesting.concurrent.junit.internal.RecordingRunNotifier;
import patterntesting.runtime.junit.ProxyRunner;
import patterntesting.runtime.util.Environment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * This is the parallel version of the {@link ProxyRunner}.
 * Till 1.2.10-YEARS it was placed in the experimental package.
 *
 * @author oliver
 * @since 1.2 (18.12.2011)
 */
public class ParallelProxyRunner extends ProxyRunner {

    private static final Logger LOG = LogManager.getLogger(ParallelProxyRunner.class);
    private final Map<FrameworkMethod, Result> results = new HashMap<FrameworkMethod, Result>();
    private final Executor executor = Executors.newCachedThreadPool();

    /**
     * Class for the result and the used FutureTask.
     * Also it looks like the Result class in ParallelRunner a
     * {@link RecordingRunNotifier} is stored in here (and not
     * a Statement).
     */
    private static class Result {
        /**
         * @param method the method
         */
        Result(final FrameworkMethod method) {
            this.method = method;
        }
        /** The called method. */
        FrameworkMethod method;
        /** The (future) result. */
        FutureTask<RecordingRunNotifier> future;
    }

    /**
     * Instantiates a new parallel proxy runner.
     *
     * @param testClass the test class
     * @throws InitializationError the initialization error
     */
    public ParallelProxyRunner(final Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    /**
     * Returns a {@link Statement}: Call {@link #runChild(FrameworkMethod, RunNotifier)}
     * on each object returned by {@link #getChildren()} (subject to any imposed
     * filter and sort).
     *
     * @param notifier the notifier
     * @return the statement
     * @see org.junit.runners.ParentRunner#childrenInvoker(RunNotifier)
     */
    @Override
    protected Statement childrenInvoker(final RunNotifier notifier) {
        if (!Environment.areThreadsAllowed()) {
            return super.childrenInvoker(notifier);
        }
        return new Statement() {
            @Override
            public void evaluate() {
                runChildren(notifier);
            }
        };
    }

    /**
     * The tests are started parallel and recorded at the beginning. Later in
     * runChild(..) only the recorded result will be returned.
     *
     * @param notifier the RunNotifier
     */
    private void runChildren(final RunNotifier notifier) {
        List<FrameworkMethod> methods = getChildren();
        LOG.trace("Running {} methods in parallel...", methods.size());
        this.recordResults(methods);
        for (final FrameworkMethod each : methods) {
            Runnable r = (new Runnable() {
                public void run() {
                    runChild(each, notifier);
                }
            });
            Thread t = new Thread(r, each.getName());
            t.start();
            LOG.trace("{} started.", t);
        }
    }

    /**
     * Replays the recorded test notifications. The tests itself were started
     * by the method <code>recordResults(List)</code>.
     * In case of a failing test this test is started normal (not parallel) to
     * the delegated runner class the chance to do its initialization stuff
     * without being disturbed from other tests running in parallel.
     *
     * @param method the method
     * @param notifier the notifier
     * @see org.junit.runners.ParentRunner#runChild(Object, RunNotifier)
     */
    @Override
    protected void runChild(final FrameworkMethod method, final RunNotifier notifier) {
        if (this.shouldBeIgnored(method)) {
            Description description = describeChild(method);
            notifier.fireTestIgnored(description);
            return;
        }
        if (Environment.areThreadsAllowed()) {
            Result result = results.get(method);
//            Description description = describeChild(method);
//            notifier.fireTestStarted(description);
            try {
                RecordingRunNotifier recorder = result.future.get();
                // in case of failure we restart original test as fallback
                if (recorder.failureRecorded()) {
                    LOG.info("parallel call of " + method.getMethod()
                            + " failed - retry with normal call...");
                    super.runChild(method, notifier);
                } else {
                    recorder.replay(notifier);
                }
            } catch (InterruptedException ie) {
                LOG.info(method.getMethod() + " was interrupted - retrying...", ie);
                super.runChild(method, notifier);
            } catch (ExecutionException ee) {
                LOG.info("recording of " + method.getMethod() + " failed - calling it direct...", ee);
                super.runChild(method, notifier);
            }
        } else {
            super.runChild(method, notifier);
        }
    }



    /////   concurrency section   /////////////////////////////////////////////

    /**
     * Here we will start the tests in parallel and record the results.
     *
     * @param testMethods the test methods
     */
    private void recordResults(final List<FrameworkMethod> testMethods) {
        if (LOG.isTraceEnabled()) {
            LOG.trace(testMethods.size() + " test methods found: "
                    + testMethods);
        }
        for (FrameworkMethod method : testMethods) {
            Result result = new Result(method);
            results.put(method, result);
            triggerTest(result);
        }
    }

    /**
     * Here we trigger the test only and store the result (a Statement) in a
     * "Future" object.
     *
     * @param result this contains the JUnit method
     */
    @MayReturnNull
    private void triggerTest(final Result result) {
        Callable<RecordingRunNotifier> callable = new Callable<RecordingRunNotifier>() {
            public RecordingRunNotifier call() {
                return invokeTest(result.method);
            }
        };
        result.future = new FutureTask<RecordingRunNotifier>(callable);
        executor.execute(result.future);
    }

    /**
     * Here we start the original {@link #runChild(FrameworkMethod, RunNotifier)}
     * method of the wrapped JUnit runner.
     *
     * @param method the test method
     * @return the RecordingRunNotifier
     */
    private RecordingRunNotifier invokeTest(final FrameworkMethod method) {
        RecordingRunNotifier notifier = new RecordingRunNotifier();
        super.runChild(method, notifier);
        return notifier;
    }

}

