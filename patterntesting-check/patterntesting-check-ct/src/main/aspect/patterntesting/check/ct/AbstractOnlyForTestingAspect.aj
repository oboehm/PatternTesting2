/**
 * $Id: AbstractOnlyForTestingAspect.aj,v 1.2 2012/01/31 19:19:29 oboehm Exp $
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
 * (c)reated 19.02.2009 by oliver (ob@oasd.de)
 */
package patterntesting.check.ct;

import patterntesting.annotation.check.ct.OnlyForTesting;

/**
 * This aspect declares an error if methods annotated by "@OnlyForTesting"
 * are not called from a test method.
 * <br/>
 * If you write your own aspect you must tell this aspect where you want to
 * see the warnings. This is done by overwriting the abstract pointcut
 * <code>applicationCode</code>.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 19.02.2009
 * @version $Revision: 1.2 $
 */
public abstract aspect AbstractOnlyForTestingAspect {

    /**
     * Specify methods which should be considered as test method.
     * Normally these are classed with the @Test or @OnlyForTesting
     * annotation.
     * <br/>
     * If you use JUnit 3 you can use this abstract pointcut to define the
     * JUnit3 test methods as test methods.
     * <br/>
     * Ex: <code>public pointcut applicationCode(): withincode(TestCase+.test*)</code>
     * @see OnlyForTestingAspect
     */
    public abstract pointcut testCode();

    pointcut disallowedCalls() :
        !testCode() &&
            (call(@OnlyForTesting *..*.new(..))
            || call(@OnlyForTesting * *..*.*(..)))
        ;

    declare error : disallowedCalls() :
        "this call is only allowed from test code!";

}
