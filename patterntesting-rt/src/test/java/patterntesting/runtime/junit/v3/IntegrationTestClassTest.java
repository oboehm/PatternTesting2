/*
 * $Id: IntegrationTestClassTest.java,v 1.7 2016/12/18 20:19:38 oboehm Exp $
 *
 * Copyright (c) 2010 by Oliver Boehm
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
 * (c)reated 14.03.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit.v3;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.runner.RunWith;

import junit.framework.TestCase;
import patterntesting.runtime.annotation.IntegrationTest;
import patterntesting.runtime.junit.SmokeRunner;
import patterntesting.runtime.util.Environment;

/**
 * This is the JUnit3 test for the correct handling of the IntegrationTest
 * annotation. In contradiction to IntegrationTestTest the annotation is put
 * before the class (not method).
 *
 * @author oliver
 * @see IntegrationTest
 * @since 1.0 (14.03.2010)
 */
@RunWith(SmokeRunner.class)
@IntegrationTest
public class IntegrationTestClassTest extends TestCase {

    private static final Logger log = LogManager.getLogger(IntegrationTestClassTest.class);

    /**
     * Normally this setUp method should be not executed. Only if the system
     * property {@link Environment#INTEGRATION_TEST} is set.
     *
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() {
        checkExecutionOf("setUp");
    }

    /**
     * Normally this tearDown method should be not executed. Only if the system
     * property {@link Environment#INTEGRATION_TEST} is set.
     *
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() {
        checkExecutionOf("tearDown");
    }

    /**
     * Normally this test should be not executed. Only if the system property
     * {@link Environment#INTEGRATION_TEST} is set.
     */
    public void testIntegrationTest() {
        checkExecutionOf("testIntegrationTest");
    }

    private static void checkExecutionOf(final String method) {
        log.info("executing " + method + "()...");
        if (!Environment.isPropertyEnabled(Environment.INTEGRATION_TEST)) {
            fail(method + "() should be not executed!");
        }
    }

}

