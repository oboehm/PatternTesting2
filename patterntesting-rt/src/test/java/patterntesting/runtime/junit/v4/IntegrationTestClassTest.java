/*
 * $Id: IntegrationTestClassTest.java,v 1.9 2016/12/18 20:19:36 oboehm Exp $
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
 * (c)reated 15.03.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit.v4;

import static org.junit.Assert.fail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.*;
import org.junit.runner.RunWith;

import patterntesting.runtime.annotation.IntegrationTest;
import patterntesting.runtime.junit.SmokeRunner;
import patterntesting.runtime.util.Environment;

/**
 * This is the JUnit4 test for the correct handling of the IntegrationTest
 * annotation. In contradiction to IntegrationTestTest the annotation is put
 * before the class (not method).
 *
 * @author oliver
 * @see IntegrationTest
 * @since 1.0 (15.03.2010)
 */
@RunWith(SmokeRunner.class)
@IntegrationTest
//@Ignore
public final class IntegrationTestClassTest {

    private static final Logger log = LogManager.getLogger(IntegrationTestClassTest.class);

    /**
     * Normally this setUp method should be not executed. Only if the system
     * property {@link Environment#INTEGRATION_TEST} is set.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        checkExecutionOf("setUpBeforeClass");
    }

    /**
     * Normally this setUp method should be not executed. Only if the system
     * property {@link Environment#INTEGRATION_TEST} is set.
     */
    @Before
    public void setUp() {
        checkExecutionOf("setUp");
    }

    /**
     * Normally this setUp method should be not executed. Only if the system
     * property {@link Environment#INTEGRATION_TEST} is set.
     */
    @After
    public void tearDown() {
        checkExecutionOf("tearDown");
    }

    /**
     * Normally this setUp method should be not executed. Only if the system
     * property {@link Environment#INTEGRATION_TEST} is set.
     */
    @AfterClass
    public static void tearDownAfterClass() {
        checkExecutionOf("tearDownAfterClass");
    }

    /**
     * Normally this test should be not executed. Only if the system property
     * {@link Environment#INTEGRATION_TEST} is set.
     */
    @Test
    public void testIntegrationTest() {
        checkExecutionOf("testIntegrationTest");
    }

    /**
     * Normally this test should be not executed. Only if the system property
     * {@link Environment#INTEGRATION_TEST} is set.
     */
    @Test(expected = RuntimeException.class)
    public void testIntegrationTestWithException() {
        checkExecutionOf("testIntegrationTest");
        throw new RuntimeException("hello world!");
    }

    private static void checkExecutionOf(final String method) {
        log.info("executing " + method + "()...");
        if (!Environment.isPropertyEnabled(Environment.INTEGRATION_TEST)) {
            fail(method + "() should be not executed!");
        }
    }

}

