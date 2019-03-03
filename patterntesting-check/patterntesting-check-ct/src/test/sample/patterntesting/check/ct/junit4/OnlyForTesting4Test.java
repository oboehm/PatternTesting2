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
package patterntesting.check.ct.junit4;

import org.apache.commons.logging.*;
import org.junit.Test;

import patterntesting.annotation.check.ct.OnlyForTesting;

/**
 * This is the test class for (Abstract)OnlyForTestingAspect.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 19.02.2009
 */
public final class OnlyForTesting4Test {

    private static final Log log = LogFactory.getLog(OnlyForTesting4Test.class);

    /**
     * Test allowed use.
     */
    @Test
    public void testAllowedUse() {
        onlyForTesting();
        canCallOnlyForTesting();
        new OnlyForTesting4Test("hello world");
    }

    /**
     * Test disallowed use.
     */
    @Test
    public void testDisallowedUse() {
        cannotCallOnlyForTesting();
    }

    /**
     * Instantiates a new OnlyForTestingTest object.
     */
    public OnlyForTesting4Test() {}

    /**
     * Instantiates a new OnlyForTestingTest object.
     *
     * @param hello the hello
     */
    @OnlyForTesting
    protected OnlyForTesting4Test(final String hello) {
        log.info(hello);
    }

    @OnlyForTesting
    private static void onlyForTesting() {
        log.info("I was called only for testing from a test method...");
    }

    private static void cannotCallOnlyForTesting() {
        // you'll get an error if you uncomment one of the next statements
        onlyForTesting();
        new OnlyForTesting4Test("hello world");
    }

    @OnlyForTesting
    private static void canCallOnlyForTesting() {
        onlyForTesting();
    }

}
