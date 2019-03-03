/*
 *========================================================================
 *
 * Copyright 2001-2004 Vincent Massol & Matt Smith.
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
 *========================================================================
 */
package patterntesting.check.ct;
/**
 * To use the <code>e.printStacktrace</code> is a bad practice.
 * <br/>
 * If you write your own aspect you must tell this aspect where you want to
 * see the warnings. This is done by overwriting the abstract pointcut
 * <code>applicationCode</code>.
 * 
 * @author mattsmith
 * @version $Id: AbstractStackTraceAspect.aj,v 1.2 2012/01/31 19:19:29 oboehm Exp $
 */
public abstract aspect AbstractStackTraceAspect
{
    /**
     * Specify what is application code that should be subject to the
     * pattern test.
     *
     * Ex: <code>pointcut applicationCode(): within(patterntesting.*)</code>
     */
    public abstract pointcut applicationCode();

    /**
     * Specify which code is allowed to manipulate
     * <code>Throwable.printStackTrace()</code>
     */
    public abstract pointcut allowedCode();

    /**
     * Invalid system logging.
     */
    pointcut invalidStackTraceLogging() :
         call (public void Throwable+.printStackTrace())
        && applicationCode()
        && !allowedCode();

    /**
     * Declare logging to stdout and stderr being compilation errors.
     */
    declare warning: invalidStackTraceLogging() :
        "No logging should be done using Throwable.printStackTrace().";
}
