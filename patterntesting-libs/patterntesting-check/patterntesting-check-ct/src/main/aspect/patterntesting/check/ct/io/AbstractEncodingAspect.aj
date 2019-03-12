/*
 * $Id: AbstractEncodingAspect.aj,v 1.1 2012/08/09 18:38:37 oboehm Exp $
 *
 * Copyright (c) 2012 by Oliver Boehm
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
 * (c)reated 09.08.2012 by oliver (ob@oasd.de)
 */

package patterntesting.check.ct.io;

import java.io.*;

/**
 * This aspect will warn you about method calls with undefinded encodings.
 * <p>
 * If you write your own aspect you must tell this aspect where you want to
 * see the warnings. This is done by overwriting the abstract pointcut
 * <code>applicationCode</code>.
 * </p>
 * 
 * @author oliver (ob@aosd.de)
 * @since 1.3 (09.08.2012)
 * @version $Revision: 1.1 $
 */
public abstract aspect AbstractEncodingAspect {

    /**
     * Specify what the application code is that should be subject to the
     * pattern test.
     * <br/>
     * Ex: <code>pointcut applicationCode(): within(patterntesting.sample.*)</code>
     */
    public abstract pointcut applicationCode();

    pointcut undefinedEncodingConstructor() :
        call(public InputStreamReader.new(InputStream))
            || call(public OutputStreamWriter.new(OutputStream))
            || call(public java.lang.String.new(byte[]))
            ;

    pointcut undefinedEncodingCall() :
        call(public byte[] java.lang.String.getBytes())
            ;

    declare warning : undefinedEncodingConstructor() && applicationCode() :
        "use another constructor with encoding parameter to avoid platform dependency";


    declare warning : undefinedEncodingCall() && applicationCode() :
        "use method with additional encoding parameter to avoid platform dependency";

}

