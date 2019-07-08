/*
 * Copyright (c) 2019 by Oliver Boehm
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
 * (c)reated 08.07.2019 by oliver (ob@aosd.de)
 */
package patterntesting.check.ct;

import org.junit.jupiter.api.Test;
import patterntesting.annotation.check.ct.SuppressJUnitWarning;

/**
 * This aspect tries to detect some JUnit <em>Anti Patterns</em> like
 * <dl>
 *  <dt>Superficial Test Coverage</dt>
 *      <dd>no support</dd>
 *  <dt>Overly Complex Tests</dt>
 *      <dd>no support</dd>
 *  <dt>Catching Unexpected Exceptions</dt>
 *      <dd>You'll get a warning if you catch a warning in a test method.</dd>
 * </dl>
 * <br/>
 * For a complete list of JUnit anti patterns see
 * {@link "http://www.exubero.com/junit/antipatterns.html"}.
 * You can suppress the warnings of this aspect by marking a method or class
 * with @SuppressJUnitWarning.
 * <br/>
 * NOTE: Only JUnit 5 is supported by this aspect.
 *
 * @see SuppressJUnitWarning,
 *      http://www.exubero.com/junit/antipatterns.html
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 2.0 (08.07.2019)
 */
public aspect JUnit5Aspect {

    /**
     * A JUnit test method is recognized at the @Test annotation.
     * If you mark a method (or class) with @SuppressJUnitWarning you
     * can suppress the checks of this aspect.
     */
    pointcut withinTestMethod() :
        @withincode(Test)
        && !@withincode(SuppressJUnitWarning)
        && !@within(SuppressJUnitWarning)
        ;

    /**
     * Catching Unexpected Exceptions <br/>
     * This anti-pattern seems to catch out a lot of developers who are new to
     * unit testing. When writing production code, developers are generally
     * aware of the problems of uncaught exceptions, and so are relatively
     * diligent about catching exceptions and logging the problem. In the case
     * of unit tests, however, this pattern is completly wrong!
     *
     * @see <a href=
     *      "http://www.exubero.com/junit/antipatterns.html#Catching_Unexpected_Exceptions">
     *      Catching Unexpected Exceptions</a>
     */
    declare warning :
        withinTestMethod() && handler(java.lang.Throwable+) :
            "normally you don't need to catch exceptions inside JUnit tests, you can use 'assertThrows(..)' or ignore them";

}
