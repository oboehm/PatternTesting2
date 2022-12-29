/*
 * $Id: RunParallelAspect.aj,v 1.5 2016/12/18 21:56:49 oboehm Exp $
 *
 * Copyright (c) 2008 by Oliver Boehm
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
 * (c)reated 13.01.2009 by oliver (ob@oasd.de)
 */
package patterntesting.concurrent;

import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.slf4j.*;

import patterntesting.annotation.concurrent.RunParallel;
import patterntesting.runtime.util.JoinPointHelper;

/**
 * This aspect together with the @RunParallel annotation allows you to
 * run a method parallel in different threads.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 0.9
 */
@SuppressAjWarnings({"adviceDidNotMatch"})
public aspect RunParallelAspect percflow(parallelMethods()) {

    private static Logger log = LoggerFactory.getLogger(RunParallelAspect.class);

    static {
        log.debug("{} is successful initialized.", RunParallelAspect.class);
    }
    
    /** Here one of the results of a parallel run is stored. */
    public Object result;

    /** The parallel runs. */
    public RunParallel parallelRuns;

    /**
     * Parallel methods.
     */
    pointcut parallelMethods() :
        execution(@RunParallel * *..*.*(..))
        || execution(@RunParallel *..*.new(..));

    /**
     * Each method marked as <code>@RunParallel</code> is started in its own
     * thread and the result of it is stored in <code>result</code>.
     *
     * @param t the <code>@RunParallel</code> annotation with the information,
     *        how many parallel threads should be started.
     * @return the object from one of the thread. The different parallel
     *         executions should be either of type "void" or should return
     *         for each call the same result. Otherwise the result is not
     *         predictable.
     *
     * @{link "http://dev.eclipse.org/mhonarc/lists/aspectj-users/msg09446.html"}
     * @{link "http://dev.eclipse.org/newslists/news.eclipse.technology.ajdt/msg01872.html"}
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    Object around(RunParallel t) : parallelMethods() && @annotation(t) {
        parallelRuns = t;
        Runnable work = new Runnable() {
            public void run() {
                if (log.isTraceEnabled()) {
                    log.trace("> "
                            + JoinPointHelper.getAsShortString(thisJoinPoint)
                            + " started...");
                }
                Object x = proceed(parallelRuns);
                if (log.isTraceEnabled()) {
                    log.trace("< "
                            + JoinPointHelper.getAsShortString(thisJoinPoint)
                            + " = " + x);
                }
                result = x;
            }
        };
        runParallel(work, t);
        return result;
    }

    /**
     * This method starts the given Runnable in several threads.
     * @param work
     * @param runs contains the information how many thread should be created.
     */
    private void runParallel(Runnable work, RunParallel runs) {
        int n = runs.value();
        Thread[] threads = new Thread[n];
        for(int i = 0; i < n; i++) {
            threads[i] = new Thread(work, "t"+i);
            log.debug("starting " + threads[i] + "...");
            threads[i].start();
        }
        for(int i = 0; i < n; i++) {
            waitFor(threads[i]);
        }
        if (result == null) {
            log.debug("All {} threads are finished with no result.", n);
        } else {
            log.debug("All {} threads are finished with result = {}.", n, result);
        }
    }

    private void waitFor(Thread t) {
        try {
            t.join();
        } catch (InterruptedException ie) {
            log.warn("can't no longer wait for " + t, ie);
        }
    }

}
