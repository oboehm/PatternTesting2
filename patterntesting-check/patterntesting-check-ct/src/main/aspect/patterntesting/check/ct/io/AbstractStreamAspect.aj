/**
 * $Id: AbstractStreamAspect.aj,v 1.1 2012/08/09 18:38:37 oboehm Exp $
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
 * (c)reated 01.04.2009 by oliver (ob@aosd.de)
 */
package patterntesting.check.ct.io;

/**
 * Since JDK 1.1 you should use the Writer and Reader classes and not the
 * Stream classes. So you should get a warning if you try to use the old
 * Stream classes. The only exception may be the PrintStream class because
 * System.out and System.err depends on it.
 * <br/>
 * If you write your own aspect you must tell this aspect where you want to
 * see the warnings. This is done by overwriting the abstract pointcut
 * <code>applicationCode</code>.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 01.04.2009
 * @version $Revision: 1.1 $
 */
public abstract aspect AbstractStreamAspect {

    /**
     * Specify what the application code is that should be subject to the
     * pattern test.
     * <br/>
     * Ex: <code>pointcut applicationCode(): within(patterntesting.sample.*)</code>
     */
    public abstract pointcut applicationCode();

    pointcut newStream() :
        call(public java.io.*Stream.new(..));

    declare warning : newStream() && applicationCode() :
        "don't use Stream class - use Reader or Writer";

}
