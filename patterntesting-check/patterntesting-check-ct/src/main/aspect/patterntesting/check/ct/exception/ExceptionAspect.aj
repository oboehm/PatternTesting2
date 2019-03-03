/*
 * $Id: ExceptionAspect.aj,v 1.1 2012/08/12 17:48:45 oboehm Exp $
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
 * (c)reated 11.08.2012 by oliver (ob@oasd.de)
 */

package patterntesting.check.ct.exception;

import patterntesting.annotation.check.ct.SuppressExceptionWarning;

/**
 * Methods and Constructors should not throw an Exception, a Throwable or either
 * an Error. They should throw a specific Exception. Also if you throw an 
 * exception: don't use the general {@link RuntimeException}, {@link Exception}
 * {@link Throwable} or {@link Error}. Use a more specific one.
 * 
 * @author oliver (ob@aosd.de)
 * @since 1.3 (11.08.2012)
 * @version $Revision: 1.1 $
 */
public aspect ExceptionAspect extends AbstractExceptionAspect {

    /**
     * For every class, every constructor and every method which is not marked
     * by <code>@SuppressExceptionWarning</code> a warning should be printed if
     * the signature contains a <code>... throws Exception</code> or a similar
     * unspecific exception.
     *
     * @see SuppressExceptionWarning
     */
    public pointcut applicationCode() :
        !(@within(SuppressExceptionWarning)
        || @withincode(SuppressExceptionWarning)
        || execution(@SuppressExceptionWarning * *..*.*(..))
        || execution(@SuppressExceptionWarning *..*.new(..))
        );
    
}

