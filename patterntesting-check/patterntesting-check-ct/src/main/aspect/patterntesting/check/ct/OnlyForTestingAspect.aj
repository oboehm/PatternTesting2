/**
 * $Id: OnlyForTestingAspect.aj,v 1.2 2011/12/29 16:46:18 oboehm Exp $
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

import org.junit.*;
import patterntesting.annotation.check.ct.OnlyForTesting;

/**
 * This aspect (together with AbstractOnlyForTestingAspect) declares an error
 * if methods annotated by "@OnlyForTesting" are not called from a test method.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 19.02.2009
 * @version $Revision: 1.2 $
 *
 * @see AbstractOnlyForTestingAspect
 */
public aspect OnlyForTestingAspect extends AbstractOnlyForTestingAspect {

    /**
     * Test code are not only JUnit test methods (annotated with "@Test")
     * but also the setup and teardown methods.
     */
    public pointcut testCode() :
        testJUnit4Code() || @withincode(OnlyForTesting)
            || @within(OnlyForTesting);
            ;
    
    private pointcut testJUnit4Code():
        @withincode(Test)
            || @withincode(Before) || @withincode(BeforeClass)
            || @withincode(After)  || @withincode(AfterClass)
            ;
    
}
