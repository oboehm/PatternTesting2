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
package patterntesting.check.ct.io;

import java.io.PrintStream;

/**
 * Pattern Test that ensures that <code>System.out</code> and
 * <code>System.err</code> are not used in the code. This is
 * to prevent the usage of <code>System.out.println("")</code>
 * calls (and variations).
 * <br/>
 * If you write your own aspect you must tell this aspect where you want to
 * see the warnings. This is done by overwriting the abstract pointcut
 * <code>applicationCode</code>.
 * <br/>
 * Note: With AspectJ it is not possible to write a compile time
 * check that only ensures that <code>println()</code> calls are
 * not allowed. It is possible for runtime checks but we have
 * preferred to make a compile-time test for simplicity. Thus,
 * code that manipulate <code>System.out</code> and
 * <code>System.err</code> for other purpose that using the
 * <code>println()</code> methods will need to be added to the
 * list of items to exclude from the tests, hence the abstract
 * <code>allowedCode</code> pointcut.
 *
 * @author <a href="mailto:jerome.bernard@corp.vizzavi.net">Jerome Bernard</a>
 * @author <a href="mailto:vmassol@apache.org">Vincent Massol</a>
 *
 * @version $Id: AbstractSopAspect.aj,v 1.1 2012/08/09 18:38:37 oboehm Exp $
 */
public abstract aspect AbstractSopAspect
{
    /**
     * Specify what is application code that should be subject to the
     * pattern test.
     *
     * Ex: <code>pointcut applicationCode(): within(patterntesting.*)</code>
     */
    public abstract pointcut applicationCode();

    /**
     * Specify which code is allowed to manipulate <code>System.out</code>
     * and <code>System.err</code>.
     */
    public abstract pointcut allowedCode();

    /**
     * Invalid system logging.
     */
    pointcut invalidSystemLogging() :
        (get(PrintStream java.lang.System.out) || get(PrintStream java.lang.System.err))
        && applicationCode()
        && !allowedCode();

    /**
     * Declare logging to stdout and stderr being warnings
     * (and no longer compiling errors as till PatternTesting 0.5)
     */
    declare warning: invalidSystemLogging() :
        "No logging should be done using the default output and error streams.";

}
