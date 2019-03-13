/**
 * $Id: AbstractTestExceptionAspect.aj,v 1.4 2016/12/18 21:57:35 oboehm Exp $
 *
 * Copyright (c) 2009 by Oliver Boehm
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * (c)reated 08.03.2009 by oliver (ob@aosd.de)
 */

package patterntesting.exception;

import org.apache.logging.log4j.*;
import org.aspectj.lang.annotation.SuppressAjWarnings;

import patterntesting.runtime.util.Assertions;

/**
 * This aspect controls if an checked exception is thrown for methods which
 * have it in their throws statement. You can control via the
 * ExceptionFactory and ExceptionFactoryMBean (i.e. by JMX) if an exception
 * is thrown or not. This is helpful for testing purposes if you want to see
 * how your (web) application reacts on a thrown exception.
 * <br/>
 * Asserts must be enabled otherwise no additional exceptions will be thrown.
 * This is for security reason. In production mode assertions are normally
 * disabled - only for testing assertions are enabled (normally).
 * <br/>
 * You can control the thrown exceptions via the ExceptionFactory which is
 * registered as MBean. You will see the ExceptionFactory not before the first
 * method marked as @TestException has finished (because it is realized as
 * after advice). So you can't hit the first @TestException method with the
 * JConsole because you don't see the ExceptionFactory. If you want to see it
 * before call <tt>ExceptionFactory.getInstance()</tt> (with the creation of
 * the instance it is also registered as MBean).
 *
 * @see ExceptionFactory
 * @see ExceptionFactoryMBean
 *
 * @author <a href="ob@aosd.de">oliver</a>
 * @since 0.9
 */
public abstract aspect AbstractTestExceptionAspect {

	private static final ExceptionFactory exceptionFactory = ExceptionFactory
			.getInstance();

    /**
     * To get the aspect specific logger.
     * @return the logger
     */
    public abstract Logger getLog();

	/**
	 * Specify which methods (or constructors) can throw an checked exception
	 * and should be controlled via ExceptionFactory.
	 * <br/>
	 * Ex: <code>execution(@TestException * *..*.*(..) throws Exception+)</code>
	 */
	public abstract pointcut applicationCode();

	/**
	 * If asserts are enabled this aspect will throw this exception which is
	 * valid for the weaved method. This is useful for testing if you want to
	 * see how your application reacts on this.
	 */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    after() returning : applicationCode()
            && !within(TestExceptionAspect) {
    	if (!Assertions.ENABLED) {
    		return;
    	}
        if (getLog().isTraceEnabled()) {
            getLog().trace("will throw exception for " + thisJoinPoint);
        }
        exceptionFactory.provokeFor(thisJoinPoint);
    }

}
