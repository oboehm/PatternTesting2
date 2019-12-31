/*
 * $Id: ParallelRunner.java,v 1.26 2016/12/18 21:56:49 oboehm Exp $
 *
 * Copyright (c) 2010 by Oliver Boehm
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
 * (c)reated 16.03.2010 by oliver (ob@oasd.de)
 */

package patterntesting.concurrent.junit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Test.None;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;
import patterntesting.annotation.check.runtime.MayReturnNull;
import patterntesting.runtime.junit.SmokeRunner;
import patterntesting.runtime.junit.internal.JUnitHelper;
import patterntesting.runtime.junit.internal.ProfiledStatement;
import patterntesting.runtime.util.Environment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Implements the JUnit 4 standard test case class model, as defined by the
 * annotations in the org.junit package. It will run the test methods in
 * parallel.
 * <p>
 * It also supports JUnit 3 test cases now. I.e. you can add
 * {@code @RunWith(ParallelRunner.class)} also in front of a JUnit3 TestCase.
 * </p>
 *
 * @author oliver
 * @since 1.0 (16.03.2010)
 */
public final class ParallelRunner extends SmokeRunner {

    private static final Logger LOG = LogManager.getLogger(ParallelRunner.class);
    private final Map<FrameworkMethod, Result> results = new HashMap<FrameworkMethod, Result>();
    private final Executor executor = Executors.newCachedThreadPool();

    /**
     * Class for the result and the used FutureTask.
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
        FutureTask<Statement> future;
    }

    /**
     * Creates a ParallelRunner to run klass methods in parallel.
     *
     * @param klass the test class to run
     * @throws InitializationError if the test class is malformed
     */
    public ParallelRunner(final Class<?> klass) throws InitializationError {
        super(klass);
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
        if (Environment.areThreadsAllowed()) {
            LOG.trace("Methods of {} will be started parallel...", this.getTestClass().getJavaClass());
            return new Statement() {
                @Override
                public void evaluate() {
                    runChildren(notifier);
                }
            };
        }
        return super.childrenInvoker(notifier);
    }

    /**
     * The tests are started parallel and recorded at the beginning. Later in
     * runChild(..) only the recorded result will be returned.
     *
     * @param notifier the RunNotifier
     */
    private void runChildren(final RunNotifier notifier) {
        this.recordResults();
        for (final FrameworkMethod each : getChildren()) {
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
     * We want to see to log the statement with our logger.
     *
     * @param stmt the stmt to be logged
     * @see SmokeRunner#logStatement(Statement)
     * @see SmokeRunner#runChild(FrameworkMethod, RunNotifier)
     */
    @Override
    protected void logStatement(final Statement stmt) {
        LOG.info("{}", stmt);
    }

    /**
     * We will return here the statement recorded earlier.
     * (see also BlockJUnit4ClassRunner#methodBlock(FrameworkMethod))
     *
     * @param method the test method to be called
     * @return the (recorded) result of this test method
     */
    @SuppressWarnings("squid:S2142")
    @Override
    protected Statement methodBlock(final FrameworkMethod method) {
        if (Environment.areThreadsAllowed()) {
            Result result = results.get(method);
            if ((result == null) || (result.future == null)) {
                return new RecordedStatement(this.getTestClass(), method,
                        new AssertionError(method.getName()
                                + " not found in recorded results"));
            }
            try {
                return result.future.get();
            } catch (InterruptedException e) {
                LOG.info(method.getName() + " was interrupted:", e);
                return new RecordedStatement(this.getTestClass(), method, e);
            } catch (ExecutionException e) {
                LOG.info("Cannot execute " + method.getName() + ":", e);
                return new RecordedStatement(this.getTestClass(), method, e);
            }
        }
        return super.methodBlock(method);
    }

    /**
     * We will append the sign for parallel ("||") at the end of the
     * description as indication that the tests will run in parallel.
     *
     * @return name with "||" appended
     * @see org.junit.runners.ParentRunner#getDescription()
     */
    @Override
    protected String getName() {
        if (Environment.areThreadsAllowed()) {
            String name = super.getName();
            return name + "||";
        }
        return super.getName();
    }



    /////   JUnit3 support section   //////////////////////////////////////////

    /**
     * Gets the framework method.
     *
     * @param testClass the test class
     * @param name the name
     * @return the framework method
     */
    @MayReturnNull
    static FrameworkMethod getFrameworkMethod(final TestClass testClass,
            final String name) {
        return JUnitHelper.getFrameworkMethod(testClass.getJavaClass(), name);
    }

    private void invoke(final TestClass testClass, final String methodName,
            final Object target, final RecordedStatement statement) {
        FrameworkMethod method = getFrameworkMethod(testClass, methodName);
        if (method != null) {
            invoke(method, target, statement);
        }
    }



    /////   concurrency section   /////////////////////////////////////////////

    /**
     * Here we will start the tests in parallel and record the results.
     */
    private void recordResults() {
        List<FrameworkMethod> testMethods = this.getChildren();
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
        Callable<Statement> callable = new Callable<Statement>() {
            public Statement call() {
                return invokeTest(result.method);
            }
        };
        result.future = new FutureTask<Statement>(callable);
        executor.execute(result.future);
    }

    /**
     * Before we can start the given test method we must invoke all setUp
     * methods. This will be done here. After the test method was executed
     * we have to call the tearDown methods. This will be also done here.
     *
     * @param method the test method
     * @return the RecordedStatement
     */
    private Statement invokeTest(final FrameworkMethod method) {
        RecordedStatement statement = new RecordedStatement(
                this.getTestClass(), method);
        if (this.shouldBeIgnored(method)) {
            return statement;
        }
        try {
            Object target = this.getTestClass().getOnlyConstructor().newInstance();
            statement.startTimer();
            invokeBefores(target, statement);
            statement.startTestTimer();
            if (statement.success()) {
                Test annotation = method.getAnnotation(Test.class);
                if (annotation == null) {
                    this.invoke(method, target, statement);
                } else {
                    this.invoke(method, target, annotation, statement);
                }
            }
            statement.startAftersTimer();
            invokeAfters(target, statement);
            statement.endTimer();
            return statement;
        } catch (InstantiationException e) {
            LOG.warn("Cannot instantiate {}:", this.getTestClass(), e);
            return new RecordedStatement(this.getTestClass(), method, e);
        } catch (IllegalAccessException e) {
            LOG.warn("Cannot access {}:", method.getName(), e);
            return new RecordedStatement(this.getTestClass(), method, e);
        } catch (InvocationTargetException e) {
            LOG.trace("{} could not be successful invoked:", method, e);
            LOG.warn("{} failed:", method.getName(), e.getTargetException());
            return new RecordedStatement(this.getTestClass(), method, e.getTargetException());
        }
    }

    /**
     * Here we invoke all setUp() methods.
     * @param target the instantiated JUnit test
     * @param statement the recorded statement
     */
    protected void invokeBefores(final Object target,
            final RecordedStatement statement) {
        TestClass testClass = this.getTestClass();
        if (ProfiledStatement.isTestCaseClass(testClass)) {
            invoke(testClass, "setUp", target, statement);
        } else {
            List<FrameworkMethod> befores = testClass.getAnnotatedMethods(Before.class);
            invoke(befores, target, statement);
        }
    }

    /**
     * Here we invoke all tearDown() methods.
     * @param target the instantiated JUnit test
     * @param stmt the recorded statement
     */
    protected void invokeAfters(final Object target, final RecordedStatement stmt) {
        TestClass testClass = this.getTestClass();
        if (ProfiledStatement.isTestCaseClass(testClass)) {
            invoke(testClass, "tearDown", target, stmt);
        } else {
            List<FrameworkMethod> befores = testClass.getAnnotatedMethods(After.class);
            invoke(befores, target, stmt);
        }
    }

    private void invoke(final List<FrameworkMethod> frameworkMethod,
            final Object target, final RecordedStatement stmt) {
        for (FrameworkMethod before : frameworkMethod) {
            invoke(before, target, stmt);
            if (stmt.failed()) {
                return;
            }
        }
    }

    private void invoke(final FrameworkMethod frameworkMethod,
            final Object target, final RecordedStatement stmt) {
        Method method = frameworkMethod.getMethod();
        try {
            method.setAccessible(true);
            method.invoke(target);
        } catch (IllegalAccessException ex) {
            LOG.trace("Cannot access {}:", method, ex);
            LOG.debug("{} will record AssertionError for {} because {}.", stmt, method, ex);
            stmt.setThrown(getAssertionErrorFor(method, ex));
        } catch (InvocationTargetException ex) {
            LOG.trace("Cannot invoke {} with {}:", method, target, ex);
            LOG.debug("{} will record AssertionError for {} and {} because {}.", stmt, method, target, ex);
            Throwable t = ex.getTargetException();
            if (t != null) {
                stmt.setThrown(t);
            } else {
                stmt.setThrown(getAssertionErrorFor(method, ex));
            }
        }
    }

    /**
     * If the test was annotated with {@code expected=RuntimeException.class}
     * we must look in the calculated statement if this expected exception was
     * thrown. If yes the statement should be marked as "success".
     *
     * @param method the FrameworkMethod
     * @param target the instantiated test
     * @param test the Test annotation with a possibly expected value
     * @param stmt the recorded statement
     */
    private void invoke(final FrameworkMethod method, final Object target,
            final Test test, final RecordedStatement stmt) {
        invoke(method, target, stmt);
        Class<? extends Throwable> expected = test.expected();
        if ((expected != null) && (expected != None.class)) {
            stmt.setExpected(expected);
        }
    }

    private AssertionError getAssertionErrorFor(final Method method, final Throwable t) {
        String detailedMessage = "invoke of " + this.getTestClass() + "."
                + method + "() failed\n" + t;
        return new AssertionError(detailedMessage);
    }

}

