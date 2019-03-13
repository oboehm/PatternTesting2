/**
 * $Id: TestExceptionAspect.aj,v 1.3 2016/12/18 21:57:35 oboehm Exp $
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
 * (c)reated 03.03.2009 by oliver (ob@oasd.de)
 */
package patterntesting.exception;

import org.apache.logging.log4j.*;

import patterntesting.annotation.exception.TestException;
import patterntesting.runtime.util.Assertions;

/**
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 03.03.2009
 * @version $Revision: 1.3 $
 */
public aspect TestExceptionAspect extends AbstractTestExceptionAspect {

    private static final Logger log = LogManager.getLogger(TestExceptionAspect.class);

    /**
     * To return the aspect specific logger to the super aspect.
     * @return the aspect specific logger
     */
    @Override
    public final Logger getLog() {
        return log;
    }

    static {
        if (Assertions.ENABLED) {
            log.debug("TestExceptionAspect is active");
        } else {
            log.debug("TestExceptionAspect is not active - call 'java -ea' (SunVM) to activate it");
        }
    }

	/**
	 * All methods which are marked with @TestException should
	 * be able to be controlled via JMX. They can be advised to throw an
	 * exception or not (for testing).
	 *
	 * @see ExceptionFactory
	 * @see ExceptionFactoryMBean
	 */
    @Override
    public pointcut applicationCode() :
        execution(@TestException * *..*.*(..) throws Exception+)
        || execution(* (@TestException *..*).*(..) throws Exception+)
        ;

}
