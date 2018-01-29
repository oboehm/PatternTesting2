/**
 * $Id: NotYetImplementedAspect.aj,v 1.3 2016/12/18 20:19:39 oboehm Exp $
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
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express orimplied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * (c)reated 30.04.2009 by oliver (ob@aosd.de)
 */
package patterntesting.runtime;

import org.aspectj.lang.annotation.SuppressAjWarnings;

import patterntesting.runtime.annotation.NotYetImplemented;

/**
 * This aspect handles the <tt>@NotYetImplemented</tt> annotation.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 0.9
 */
public aspect NotYetImplementedAspect {

    pointcut applicationCode() :
        execution(@NotYetImplemented * *..*.*(..));

    /**
     * Throw an UnsupportedOpertionExcpeption for each method annotated with
     * <tt>@NotYetImplemented</tt>.
     *
     * @param a the annotation with the message for the exception
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    after(NotYetImplemented a) : applicationCode() && @annotation(a) {
        UnsupportedOperationAspect.throwUnsupportedOperationException(
                a.value(), thisJoinPoint);
    }

}
