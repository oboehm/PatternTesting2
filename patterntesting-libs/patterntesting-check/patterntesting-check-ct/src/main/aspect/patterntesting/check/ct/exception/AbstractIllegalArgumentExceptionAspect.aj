/*
 * $Id: AbstractIllegalArgumentExceptionAspect.aj,v 1.1 2012/08/12 17:59:54 oboehm Exp $
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
 * (c)reated 31.01.2012 by oliver (ob@oasd.de)
 */

package patterntesting.check.ct.exception;

/**
 * A method with no parameters can't throw an IllegalArgumentException 
 * because it has no arguments.
 * <p>
 * If you write your own aspect you must tell this aspect where you want to
 * see the warnings. This is done by overwriting the abstract pointcut
 * <code>applicationCode</code>.
 * </p>
 * <p>
 * Since 1.3 this aspect was moved to the exception package.
 * </p>
 * 
 * @author oliver (ob@aosd.de)
 * @since 1.2.10-YEARS (31.01.2012)
 */
public abstract aspect AbstractIllegalArgumentExceptionAspect {

    /**
     * Specify what the application code is that should be subject to the
     * pattern test.
     * <br/>
     * Ex: <code>pointcut applicationCode(): within(patterntesting.sample.*)</code>
     */
    public abstract pointcut applicationCode();

    /**
     * We can't set a pointcut to a throw statement. But we can do it indirect
     * by setting a pointcut to the constructor of an IllegalArgumentException.
     */
    pointcut throwingIllegalArgumentException() :
        call(java.lang.IllegalArgumentException.new(..)) && applicationCode();
    
    declare warning : throwingIllegalArgumentException() && withincode(* *..*.*()):
        "don't throw an IllegalArgumentException for a method with no arguments";
    
    declare warning : throwingIllegalArgumentException() && withincode(*..*.new()):
        "don't throw an IllegalArgumentException for a default constructor";

}

