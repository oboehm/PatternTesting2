/*
 * $Id: AbstractTraceAspect.aj,v 1.2 2016/10/24 20:22:06 oboehm Exp $
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
 * (c)reated 30.09.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.log;

import org.aspectj.lang.annotation.SuppressAjWarnings;

/**
 * This aspect handles the logging output. It may slow down your application
 * because it uses the stacktrace for determing the right indent for pretty
 * printing. So use this aspect with care.
 *
 * @author oliver
 * @since 1.0.3 (30.09.2010)
 */
public abstract aspect AbstractTraceAspect {

    /**
     * Application code.
     */
    public abstract pointcut applicationCode();

    private pointcut allMethods() :
        execution(*..*.new(..)) || execution(* *..*.*(..));

    private pointcut voidMethods() :
        execution(void *..*.*(..));

    private pointcut nonVoidMethods() :
        execution(* *..*.*(..)) && !voidMethods();

    private pointcut constructors() :
        execution(*..*.new(..));

    private pointcut setFields() :
        set(* *..*);

    /**
     * Trace the start of a method or piece of code.
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    before() : applicationCode() && (allMethods() || staticinitialization(*..*)) {
        Trace.start(thisJoinPoint);
    }

    /**
     * Trace the exception handling.
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    before() : applicationCode() && handler(Throwable+) {
        Trace.trace(thisJoinPoint);
    }

    /**
     * Trace the result of a finished method.
     *
     * @param result the return value
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    after() returning(Object result) : applicationCode() &&
            (nonVoidMethods()) {
        Trace.end(thisJoinPoint, result);
    }

    /**
     * Trace the end of a method, constructor or initialization code.
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    after() returning : applicationCode() &&
            (constructors() || voidMethods() || staticinitialization(*..*)) {
        Trace.end(thisJoinPoint);
    }

    /**
     * Trace a thrown exception.
     *
     * @param t the thrown exception
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    after() throwing(Throwable t) : applicationCode() && allMethods() {
        Trace.throwing(thisJoinPoint, t);
    }

    /**
     * Trace the setting of an attribute.
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    after() : applicationCode() && setFields() {
        Trace.trace(thisJoinPoint);
    }

}
