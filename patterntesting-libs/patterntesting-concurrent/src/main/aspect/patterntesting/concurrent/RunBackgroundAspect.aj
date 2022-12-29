/**
 * $Id: RunBackgroundAspect.aj,v 1.8 2016/12/18 21:56:49 oboehm Exp $
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
 * (c)reated 19.01.2009 by oliver (ob@oasd.de)
 */
package patterntesting.concurrent;

import java.util.*;

import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.slf4j.*;

import patterntesting.annotation.concurrent.RunBackground;
import patterntesting.runtime.util.*;

/**
 * This aspect together with the @RunBackground annotation allows you to
 * run a method in the backbround (as separate thread).
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 0.9
 */
public final aspect RunBackgroundAspect {

    private static final Logger log = LoggerFactory.getLogger(RunBackgroundAspect.class);
    private static int bgNumber = 0;
    private static Collection<Thread> activeJobs = new ArrayList<Thread>();
    
    static {
        log.debug("{} is successful initialized.", RunBackgroundAspect.class);
    }
    
    pointcut backgroundMethods() :
        execution(@RunBackground * *..*.*(..))
        || execution(@RunBackground *..*.new(..));

    /**
     * Runs a method in the background.
     *
     * @return always null
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    Object around() : backgroundMethods() {
        if (!Environment.areThreadsAllowed()) {
            if (log.isDebugEnabled()) {
                log.debug(JoinPointHelper.getAsShortString(thisJoinPoint)
                        + " is NOT started in background (threading disabled)");
            }
            return proceed();
        }
        Runnable work = new Runnable() {
            public void run() {
                if (log.isTraceEnabled()) {
                    log.trace(JoinPointHelper.getAsShortString(thisJoinPoint)
                            + " started...");
                }
                proceed();
                if (log.isTraceEnabled()) {
                    log.trace(JoinPointHelper.getAsShortString(thisJoinPoint)
                            + " finished.");
                }
            }
        };
        try {
            runBackground(work);
        } catch (IllegalStateException ise) {
            log.warn("can't start "
                    + JoinPointHelper.getAsShortString(thisJoinPoint)
                    + " in background"
                    + " (reason: inside JEE server normally it is not allowed to start a thread)",
                    ise);
        } catch (RuntimeException re) {
            log.warn("can't start "
                    + JoinPointHelper.getAsShortString(thisJoinPoint)
                    + " in background", re);
            throw re;
        }
        return null;
    }

    private void runBackground(Runnable work) {
        Thread t = new Thread(work, "bg-" + bgNumber++);
        t.start();
        synchronized (activeJobs) {
            cleanActiveJobs();
            activeJobs.add(t);
        }
    }

    /**
     * Gets a collection of the active background jobs.
     *
     * @return the active jobs
     * @since 1.3.1
     */
    public static Collection<Thread> getActiveJobs() {
        synchronized (activeJobs) {
            cleanActiveJobs();
            return activeJobs;
        }
    }

    /**
     * To avoid a list with to many dead threads we clean it here.
     */
    private static void cleanActiveJobs() {
        Collection<Thread> deadJobs = new ArrayList<Thread>();
        for (Thread t : activeJobs) {
            if (!t.isAlive()) {
                deadJobs.add(t);
            }
        }
        activeJobs.removeAll(deadJobs);
    }

}
