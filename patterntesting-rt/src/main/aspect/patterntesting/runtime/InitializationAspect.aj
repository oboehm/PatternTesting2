/**
 * $Id: InitializationAspect.aj,v 1.4 2016/12/18 20:19:39 oboehm Exp $
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
 * (c)reated 23.11.2008 by oliver (ob@oasd.de)
 */
package patterntesting.runtime;

import java.lang.reflect.Field;
import java.util.Collection;

import org.slf4j.LoggerFactory;
import org.slf4j.*;
import org.aspectj.lang.annotation.SuppressAjWarnings;

import patterntesting.runtime.annotation.GuardInitialization;
import patterntesting.runtime.util.*;

/**
 * If your static initialization fails and you get a NoClassDefFoundError
 * use ths anntoation. Together with the GuardInitialization annotation it will
 * skip the errornous parts of the initialisation and log the errors.
 * <br/>
 * But there is another task for this aspect (together with the
 * GuardInitialization annotation): to avoid the run-on initialization bug
 * (see {@link "http://www.ibm.com/developerwords/java/library/j-diag0416.html"})
 * the attributes should be guarded. They should be all initialized after a
 * constructor has been called.
 *
 * @see GuardInitialization
 * @{link "http://www.ibm.com/developerwords/java/library/j-diag0416.html"}
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 0.8
 */
public aspect InitializationAspect {

	private static final Logger log = LoggerFactory.getLogger(InitializationAspect.class);

	static {
	    if (log.isDebugEnabled()) {
	        if (Assertions.ENABLED) {
	            log.debug("@GuardInitialization constructors are guarded");
	        } else {
	            log.debug("@GuardInitialization constructors will not be guarded");
	        }
	    }
	}

	/**
	 * All classes with the GuardInitialization annotation should be guarded.
	 */
	pointcut guardedClass() :
		staticinitialization(@GuardInitialization *);

	/**
	 * If there's an error during the static initialization log it to
	 * ease the trouble shooting.
	 */
    @SuppressAjWarnings({"adviceDidNotMatch"})
	void around() : guardedClass() {
		try {
			proceed();
		} catch (Throwable t) {
			log.error(thisJoinPointStaticPart.getSignature().getDeclaringType()
					+ " can't be initialized", t);
		}
	}

	/**
     * All classes with the GuardInitialization annotation should be guarded.
     */
	pointcut guardedConstructors() :
	    execution(@GuardInitialization *..*.new(..))
	    || (execution(*..*.new(..)) && @within(GuardInitialization))
	    ;

	/**
	 * Guard all marked constructors. But only if assertions are enabled.
	 *
	 * @param obj the guarded object
	 */
    @SuppressAjWarnings({"adviceDidNotMatch"})
	after(Object obj) : guardedConstructors() && this(obj) {
	    if (Assertions.ENABLED) {
            Collection<Field> fields = ReflectionHelper
                    .getUninitializedNonStaticFields(obj);
            if (fields.size() > 0) {
                throw new AssertionError("there are " + fields.size()
                        + " unitialized field(s) after "
                        + JoinPointHelper.getAsShortString(thisJoinPoint)
                        + ": " + ReflectionHelper.toShortString(fields));
            }
        }
	}

}
