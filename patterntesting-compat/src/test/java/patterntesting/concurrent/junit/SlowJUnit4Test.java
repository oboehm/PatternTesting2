/*
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
 * (c)reated 16.03.2010 by oliver (ob@oasd.de)
 */

package patterntesting.concurrent.junit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;
import org.junit.runner.RunWith;
import patterntesting.runtime.annotation.IntegrationTest;
import patterntesting.runtime.util.Environment;
import patterntesting.runtime.util.ThreadUtil;

import static org.junit.Assert.*;

/**
 * This is an example for a JUnit test with slow test methods. This class
 * should help us to test the speedup of parallel testing.
 * 
 * @author oliver
 * @since 1.0 (16.03.2010)
 */
@RunWith(ParallelRunner.class)
public final class SlowJUnit4Test {
    
    /** The Constant TIME_TO_WAIT. */
    static final int TIME_TO_WAIT = 50;
    private static final Logger log = LogManager.getLogger(SlowJUnit4Test.class);
    private static long t0 = System.currentTimeMillis();
    private int counter = 0;
    private static boolean started = false;

    /**
     * Only to see when the tests were started.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        started = true;
        logTime("setUpBeforeClass");
    }

    /**
     * Only to see when the tests ends.
     */
    @AfterClass
    public static void tearDownAfterClass() {
        logTime("tearDownAfterClass");
    }

    /**
     * Only to see when the setUp() method was started.
     */
    @Before
    public void setUp() {
        logTime("setUp"); 
        assertEquals(0, counter);
        counter++;
   }

    /**
     * Only to see when the tearDown() method was started.
     */
    @After
    public void tearDown() {
        logTime("tearDown");
        assertEquals(1, counter);
        counter--;
    }
    
    /**
     * Only to have a test method which calls (tests) logTime().
     */
    @Test
    public void testLogTime() {
        logTime("testLogTime");
        assertEquals(1, counter);
    }
    
    /**
     * Only to have a test method which calls (tests) logTime()
     * and throws an (expected) Exception.
     */
    @Test(expected = Exception.class)
    public void testLogTimeWithException() {
        logTime("testLogTime");
        assertEquals(1, counter);
        throw new RuntimeException("it's time to throw an exception");
    }
    
    /**
     * To have again a test method which calls (tests) logTime().
     */
    @Test
    public void testLogTimeAgain() {
        logTime("testLogTime");
        assertEquals(1, counter);
    }
    
    /**
     * Because this method has no "@Test" annotation it should be never called.
     */
    public void noTestMethod() {
        fail("this test should be never called");
    }
    
    /**
     * This test should be ignored.
     */
    @Test
    @Ignore("intentionally left blank")
    public void testIgnore() {
        fail("this test should be ignored");
    }
    
    /**
     * This test should only be executed if system property
     * {@link Environment#INTEGRATION_TEST} is set.
     */
    @Test
    @IntegrationTest
    public void testIntegrationTest() {
        if (!Environment.INTEGRATION_TEST_ENABLED) {
            fail("this test should be run only if " + Environment.INTEGRATION_TEST + " is set");
        }
    }
    
    /**
     * This is an example for a method which needs a little bit longer as
     * expected.
     * 
     * @param method name of the logged method
     */
    private static void logTime(final String method) {
        assertTrue("setUpBeforeClass() was not called", started);
        long t = System.currentTimeMillis() - t0;
        assertTrue("clock moves backwards", t >= 0);
        log.info("stardate " + t + ":\t" + method + "() started");
        ThreadUtil.sleep(TIME_TO_WAIT);
    }

}

