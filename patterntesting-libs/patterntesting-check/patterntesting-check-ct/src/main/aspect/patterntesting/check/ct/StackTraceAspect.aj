/**
 * $Id: StackTraceAspect.aj,v 1.2 2013/04/28 16:46:03 oboehm Exp $
 *
 * Copyright (c) 2008 by Oliver Boehm
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
 * (c)reated 14.09.2008 by oliver (ob@oasd.de)
 */
package patterntesting.check.ct;

import patterntesting.annotation.check.ct.SuppressStackTraceWarning;

/**
 * Using e.printStacktrace() is not a good idea. So this aspect together with
 * AbstractStackTraceAspect will print a warning for this call.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 14.09.2008
 * @version $Revision: 1.2 $
 *
 * @see AbstractStackTraceAspect
 */
public aspect StackTraceAspect extends AbstractStackTraceAspect {

    /**
     * Look at the whole code to find e.printStacktrace()
     */
    @Override
    public pointcut applicationCode() :  within(*..*);

    /**
     * You can suppress the warning with "@SuppressStackTraceWarning".
     */
    @Override
    public pointcut allowedCode() :
        within(@SuppressStackTraceWarning *)
        || withincode(@SuppressStackTraceWarning *..*.new(..))
        || withincode(@SuppressStackTraceWarning * *..*.*(..))
        ;

}
