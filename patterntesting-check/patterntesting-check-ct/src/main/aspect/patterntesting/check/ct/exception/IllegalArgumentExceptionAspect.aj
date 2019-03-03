/*
 * $Id: IllegalArgumentExceptionAspect.aj,v 1.1 2012/08/12 17:59:54 oboehm Exp $
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

import patterntesting.annotation.check.ct.SuppressIllegalArgumentExceptionWarning;

/**
 * A method with no parameters can't throw an IllegalArgumentException 
 * because it has no arguments.
 * 
 * @author oliver (ob@aosd.de)
 * @since 1.2.10-YEARS (31.01.2012)
 */
public aspect IllegalArgumentExceptionAspect extends AbstractIllegalArgumentExceptionAspect {
    
    /**
     * For every class, every constructor and every method which is not marked
     * by <code>@SuppressIllegalArgumentExceptionWarning</code> every
     * constructor and every method with no parameters should give a warning.
     *
     * @see SuppressIllegalArgumentExceptionWarning
     * @see AbstractIllegalArgumentExceptionAspect
     */
    public pointcut applicationCode() :
        !(@within(SuppressIllegalArgumentExceptionWarning)
        || @withincode(SuppressIllegalArgumentExceptionWarning));
    
}

