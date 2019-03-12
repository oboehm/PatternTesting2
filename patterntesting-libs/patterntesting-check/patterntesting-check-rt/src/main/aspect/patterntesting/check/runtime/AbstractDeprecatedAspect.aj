/**
 * $Id: AbstractDeprecatedAspect.aj,v 1.6 2016/12/18 21:59:31 oboehm Exp $
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
 * (c)reated 17.02.2009 by oliver (ob@oasd.de)
 */
package patterntesting.check.runtime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.apache.logging.log4j.*;

import patterntesting.runtime.util.*;

/**
 * If you want you can use this abstract aspect to mark your own deprected
 * code. But normally you should rely on DeprecatedAspect.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.6 $
 * @see DeprecatedAspect
 * @since 17.02.2009
 */
public abstract aspect AbstractDeprecatedAspect {

    /**
     * To get the aspect specific logger.
     * 
     * @return the logger
     */
    protected abstract Logger getLog();
    
    /**
     * Gets the marker like "@Deprecated" or "@Zombie".
     *
     * @return the marker
     */
    protected abstract String getMarker();
    
    /**
     * This method should throw the cause for a foiling joinpoint.
     *
     * @param jp the joinpoint
     */
    protected abstract void throwCauseFor(final JoinPoint jp);

    /**
     * Specify what the deprected code is that should be subject to the
     * pattern test.
     * Normally these are methods, attributes or classes which are marked
     * with the @Deprecated Annotation.
     *
     * @see DeprecatedAspect
     */
    public abstract pointcut deprecatedCode();

    /**
     * We will print here a warning for a call of a deprecated method or class.
     * If assertions are enabled we throw a DeprecatedCodeException.
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    before() : deprecatedCode() {
        if (Assertions.ENABLED) {
            getLog().error(JoinPointHelper.getAsShortString(thisJoinPoint)
                    + " is marked as '" + getMarker()
                    + "' and will fail!");
            throwCauseFor(thisJoinPoint);
        } else {
            getLog().warn(thisJoinPoint + " is marked as '" + getMarker() + "'!");
        }
    }

}
