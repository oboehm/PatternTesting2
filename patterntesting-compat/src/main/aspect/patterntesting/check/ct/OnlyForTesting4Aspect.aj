/*
 * Copyright (c) 2008-2019 by Oliver Boehm
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

/**
 * This aspect (together with AbstractOnlyForTestingAspect) declares an error
 * if methods annotated by "@OnlyForTesting" are not called from a test method.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 19.02.2009
 * @version $Revision: 1.2 $
 */
public aspect OnlyForTesting4Aspect {

    pointcut disallowedCalls() :
            !testCode() &&
                    (call(@patterntesting.annotation.check.ct.OnlyForTesting *..*.new(..))
                            || call(@patterntesting.annotation.check.ct.OnlyForTesting * *..*.*(..)))
            ;

    declare error : disallowedCalls() :
            "this call is only allowed from test code!";

    /**
     * Test code are not only JUnit test methods (annotated with "@Test")
     * but also the setup and teardown methods.
     */
    public pointcut testCode() :
        testJUnit4Code() || @withincode(patterntesting.annotation.check.ct.OnlyForTesting)
            || @within(patterntesting.annotation.check.ct.OnlyForTesting);
            ;

    private pointcut testJUnit4Code():
            @withincode(org.junit.Test)
                    || @withincode(org.junit.Before) || @withincode(org.junit.BeforeClass)
                    || @withincode(org.junit.After)  || @withincode(org.junit.AfterClass)
            ;

}
