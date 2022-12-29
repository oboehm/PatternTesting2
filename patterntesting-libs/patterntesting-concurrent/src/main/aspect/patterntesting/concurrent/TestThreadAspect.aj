/**
 * $Id: TestThreadAspect.aj,v 1.2 2016/12/18 21:56:49 oboehm Exp $
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
 * (c)reated 13.10.2008 by oliver (ob@oasd.de)
 */
package patterntesting.concurrent;

import org.slf4j.*;

import patterntesting.annotation.concurrent.TestThread;

/**
 * This is the concrete part of AbstractTestThreadAspect. It defines the
 * pointcut "applicationCode".
 *
 * @see AbstractTestThreadAspect
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 13.10.2008
 * @version $Revision: 1.2 $
 */
public aspect TestThreadAspect extends AbstractTestThreadAspect {

    private static final Logger log = LoggerFactory.getLogger(TestThreadAspect.class);
    
    /**
     * To return the aspect specific logger to the super aspect.
     * @return the aspect specific logger
     */
    public final Logger getLog() {
        return log;
    }

    /**
     * All methods marked with @TestThread (and below) should be instrumented
     * for thread testing.
     * But this pointcut instruments nearly each line of code. So it is now
     * limited to methods in these classes which are marked with @TestThread.
     * If you want the more general approach you must extend yourself the
     * AbstractTestThreadAspect (you can copy this aspect and remove the
     * within statement).
     */
	public pointcut applicationCode() :
	    within(@TestThread *) &&
	    cflowbelow(execution(@TestThread * *..*.*(..)));
	
}
