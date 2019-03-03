/*
 * $Id: EncodingAspect.aj,v 1.1 2012/08/09 18:38:37 oboehm Exp $
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

import patterntesting.annotation.check.ct.SuppressEncodingWarning;

/**
 * @author oliver (ob@aosd.de)
 * @since 1.3 (09.08.2012)
 * @version $Revision: 1.1 $
 */
public aspect EncodingAspect extends AbstractEncodingAspect {

    /**
     * For every class, every constructor and every method which is not marked
     * by <code>@SuppressEncodingWarning</code the use of the a Stream
     * constructors with undefined encoding should give a warning.
     *
     * @see SuppressEncodingWarning
     * @see AbstractEncodingAspect
     */
    public pointcut applicationCode() :
        !(@within(SuppressEncodingWarning) || @withincode(SuppressEncodingWarning));

}

