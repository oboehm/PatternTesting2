/*
 * $Id: AbstractExceptionAspect.aj,v 1.1 2012/08/12 17:48:45 oboehm Exp $
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
 * (c)reated 12.08.2012 by oliver (ob@oasd.de)
 */

package patterntesting.check.ct.exception;

/**
 * Methods and Constructors should not throw an Exception, a Throwable or either
 * an Error. They should throw a specific Exception. Also if you throw an 
 * exception: don't use the general {@link RuntimeException}, {@link Exception}
 * {@link Throwable} or {@link Error}. Use a more specific one.
 * <p>
 * If you write your own aspect you must tell this aspect where you want to
 * see the warnings. This is done by overwriting the abstract pointcut
 * <code>applicationCode</code>.
 * </p>
 * 
 * @author oliver (ob@aosd.de)
 * @since 1.3 (12.08.2012)
 * @version $Revision: 1.1 $
 */
public abstract aspect AbstractExceptionAspect {
    
    /**
     * Specify what the application code is that should be subject to the
     * pattern test.
     * <br/>
     * Ex: <code>pointcut applicationCode(): within(patterntesting.sample.*)</code>
     */
    public abstract pointcut applicationCode();

    /**
     * Methods or constructors should not throw an {@link RuntimeException}.
     * This is the pointcut for it.
     */
    pointcut throwsRuntimeException() :
        execution(* *..*.*(..) throws java.lang.RuntimeException)
        || execution(*..*.new(..) throws java.lang.RuntimeException);

    /**
     * Methods or constructors should not throw an {@link Exception}.
     * This is the pointcut for it.
     */
    pointcut throwsException() :
        execution(* *..*.*(..) throws java.lang.Exception)
        || execution(*..*.new(..) throws java.lang.Exception);

    /**
     * Methods or constructors should not throw a {@link Throwable}.
     * This is the pointcut for it.
     */
    pointcut throwsThrowable() :
        execution(* *..*.*(..) throws java.lang.Throwable)
        || execution(*..*.new(..) throws java.lang.Throwable);
    
    /**
     * Methods or constructors should not throw an {@link Error} or subclass of
     * an {@link Error}. This is the pointcut for it.
     */
    pointcut throwsError() :
        execution(* *..*.*(..) throws java.lang.Error+)
            || execution(*..*.new(..) throws java.lang.Error+);
    
    declare warning : throwsRuntimeException() && applicationCode() :
        "don't throw an RuntimeException - use a more specific one!";
    
    declare warning : throwsException() && applicationCode() :
        "don't throw an Exception - use a more specific one!";
    
    declare warning : throwsThrowable() && applicationCode() :
        "don't throw a Throwable - use a more specific one!";
    
    declare warning : throwsError() && applicationCode() :
        "An Error is intended for unrecoverable errors and need not be part of the signature!";

    /**
     * We can't set a pointcut to a throw statement. But we can do it indirect
     * by setting a pointcut to the constructor of a {@link RuntimeException}.
     */
    pointcut throwingRuntimeException() :
        call(java.lang.RuntimeException.new(..)) && applicationCode();
    
    /**
     * We can't set a pointcut to a throw statement. But we can do it indirect
     * by setting a pointcut to the constructor of a {@link Exception}.
     */
    pointcut throwingException() :
        call(java.lang.Exception.new(..)) && applicationCode();
    
    /**
     * We can't set a pointcut to a throw statement. But we can do it indirect
     * by setting a pointcut to the constructor of a {@link Throwable}.
     */
    pointcut throwingThrowable() :
        call(java.lang.Throwable.new(..)) && applicationCode();
    
    /**
     * We can't set a pointcut to a throw statement. But we can do it indirect
     * by setting a pointcut to the constructor of a {@link Error}.
     */
    pointcut throwingError() :
        call(java.lang.Error.new(..)) && applicationCode();
    
    declare warning : throwingRuntimeException() && withincode(* *..*.*()):
        "don't throw a unspecific RuntimeException - use a more specific one";

    declare warning : throwingException() && withincode(* *..*.*()):
        "don't throw a unspecific Exception - use a more specific one";

    declare warning : throwingThrowable() && withincode(* *..*.*()):
        "don't throw a unspecific Throwable - use a more specific one";

    declare warning : throwingError() && withincode(* *..*.*()):
        "don't throw a unspecific Error - use a more specific one";

}
