/**
 * $Id: UnsupportedOperationAspect.aj,v 1.3 2016/12/18 20:19:39 oboehm Exp $
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
 * (c)reated 19.10.2008 by oliver (ob@oasd.de)
 */
package patterntesting.runtime;

import java.text.MessageFormat;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.SuppressAjWarnings;

import patterntesting.runtime.annotation.UnsupportedOperation;

/**
 * Method with an @UnsupportedOperation will throw an
 * UnsupportedOperationException.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 0.8
 */
public aspect UnsupportedOperationAspect {

	pointcut applicationCode() :
		execution(@UnsupportedOperation * *..*.*(..));

    /**
     * Throw an UnsupportedOpertionExcpeption for each method annotated with
     * <tt>@UnsupportedOperation</tt>.
     *
     * @param a the annotation with the message for the exception
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
	after(UnsupportedOperation a) : applicationCode() && @annotation(a) {
		throwUnsupportedOperationException(a.value(), thisJoinPoint);
	}

	/**
	 * Helper method to throw an UnsupportedOperationException.
	 *
	 * @param fmt the format string
	 * @param jp the joinpoint
	 */
	protected static void throwUnsupportedOperationException(String fmt,
            JoinPoint jp) {
	    String sig = jp.getSignature().getName() + "()";
        String msg = MessageFormat.format(fmt, sig);
        throw new UnsupportedOperationException(msg);
    }

}
