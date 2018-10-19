/*
 * $Id: SmokeTestTest.java,v 1.9 2016/12/18 20:19:36 oboehm Exp $
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
 * (c)reated 02.03.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit.v4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.*;
import org.junit.runner.RunWith;

import patterntesting.runtime.annotation.SmokeTest;
import patterntesting.runtime.junit.SmokeRunner;
import patterntesting.runtime.util.Environment;


/**
 * This is the JUnit test for the use of the SmokeTest annotation.
 *
 * @author oliver
 * @since 1.0 (02.03.2010)
 */
@RunWith(SmokeRunner.class)
public final class SmokeTestTest {

    private static Logger log = LogManager.getLogger(SmokeTestTest.class);
    private static int counter = 0;
    private static int classCalled = 0;
    private int tearDownCounter;
    private static Set<String> executedTestMethods = new HashSet<>();

    /**
     * Because this class can be called more than once we do some important
     * initializations here.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        counter = 0;
        classCalled++;
    }

    /**
     * This setUp method should be not called if the SmokeTest property is set.
     */
    @Before
    public void setUp() {
        log.info("executing setUp()...");
        counter++;
        tearDownCounter = 0;
    }

    /**
     * This is a test which should be skipped of the system property
     * {@link Environment#RUN_SMOKE_TESTS} is set.
     */
    @Test
    public void testSkippedInSmokeTestMode() {
        log.info("executing testSkippedInSmokeTestMode()...");
        counter++;
        executedTestMethods.add("testSkippedInSmokeTestMode");
        if (Environment.SMOKE_TEST_ENABLED) {
            throw new RuntimeException("test should be skipped if "
                    + Environment.RUN_SMOKE_TESTS + " is set");
        }
    }

    /**
     * What happens with a normal test which is skipped in SmokeTest mode
     * but throws an expected exception - let's try it.
     */
    @Test(expected = IllegalStateException.class)
    public void testSkippedWithException() {
        log.info("executing testSkippedWithException()...");
        counter++;
        executedTestMethods.add("testSkippedWithException");
        throw new IllegalStateException("hello world!");
    }

    /**
     * This test should be always executed because it is marked as
     * "@SmokeTest".
     * Because it is the only SmokeTest in this class the counter (increased
     * in setUp()) should be one.
     */
    @SmokeTest
    @Test(expected = RuntimeException.class)
    public void testAlwaysExecuted() {
        log.info("executing testAlwaysExecuted()...");
        executedTestMethods.add("testAlwaysExecuted");
//        if (Environment.smokeTestEnabled) {
        if ((Environment.SMOKE_TEST_ENABLED) && (classCalled == 1)) {
            assertEquals("setUp() too often or not called", 1, counter);
        }
        throw new RuntimeException("do you see me?");
    }

    /**
     * This tearDown() method should be called after each test method.
     */
    @After
    public void tearDown() {
        log.info("tearDown(): counter=" + counter);
        tearDownCounter++;
        assertEquals("too often called", 1, tearDownCounter);
    }

    /**
     * Here we test which method was really executed.
     */
    @AfterClass
    public static void tearDownAfterClass() {
        checkMethod("testAlwaysExecuted", true);
        boolean shouldBeExecuted = !Environment
                .isPropertyEnabled(Environment.RUN_SMOKE_TESTS);
        checkMethod("testSkippedInSmokeTestMode", shouldBeExecuted);
        checkMethod("testSkippedWithException", shouldBeExecuted);
    }

    private static void checkMethod(final String name, final boolean wasExecuted) {
        if (executedTestMethods.contains(name) != wasExecuted) {
            fail(name + " should be " + (wasExecuted ? "" : "not")
                    + " executed");
        }
    }

}

