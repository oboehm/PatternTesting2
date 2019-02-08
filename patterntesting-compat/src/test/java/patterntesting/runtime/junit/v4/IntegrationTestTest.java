/*
 * $Id: IntegrationTestTest.java,v 1.10 2016/12/18 20:19:36 oboehm Exp $
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

package patterntesting.runtime.junit.v4;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import patterntesting.runtime.annotation.IntegrationTest;
import patterntesting.runtime.junit.SmokeRunner;
import patterntesting.runtime.util.Environment;

/**
 * This is the JUnit4 test for the correct handling of the IntegrationTest
 * annotation.
 *
 * @author oliver
 * @see IntegrationTest
 * @since 1.0 (14.03.2010)
 */
@RunWith(SmokeRunner.class)
public final class IntegrationTestTest {

    private static boolean integrationTestExecuted = false;
    private static boolean normalExecuted = false;

    /**
     * Normally this test should be not executed. Only if the system property
     * {@link Environment#INTEGRATION_TEST} is set.
     */
    @IntegrationTest
    @Test
    synchronized public void testIntegrationTest() {
        integrationTestExecuted = true;
    }

    /**
     * Normally this test should be not executed. Only if the system property
     * {@link Environment#INTEGRATION_TEST} is set.
     */
    @IntegrationTest
    @Test(expected = RuntimeException.class)
    synchronized public void testIntegrationTestWithException() {
        integrationTestExecuted = true;
        throw new RuntimeException("Do you see me?");
    }

    /**
     * This test should be always executed.
     */
    @Test
    synchronized public void testNormal() {
        normalExecuted = true;
    }

    /**
     * Here we check if testIntegrationTest() or
     * testIntegrationTestWithException() were executed.
     */
    @AfterClass
    public static void tearDownAfterClass() {
        assertTrue("testNormal() should be always executed", normalExecuted);
        if (Environment.isPropertyEnabled(Environment.INTEGRATION_TEST)) {
            assertTrue("testIntegrationTest() was not executed",
                    integrationTestExecuted);
        } else {
            assertFalse("testIntegrationTest() should be not executed",
                    integrationTestExecuted);
        }
    }

}

