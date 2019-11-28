/*
 * Copyright (c) 2009 by Oliver Boehm
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
 * (c)reated 30.11.2009 by oliver (ob@oasd.de)
 */

package patterntesting.concurrent.junit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;
import org.junit.runner.RunWith;
import patterntesting.runtime.util.ThreadUtil;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This is a spike solution to see if JUnit tests could be started in parallel.
 * And it is also a test to see if the JUnit tests are really faster if run in
 * parallel. Here the result for the different TIME_TO_WAIT values:
 * <table>
 *  <caption>Test Results</caption>
 *  <thead>
 *   <tr>
 *    <th>TIME_TO_WAIT</th><th>normal</th><th>with ParallelRunner</th>
 *   <tr>
 *  </thead>
 *  <tbody>
 *   <tr>
 *    <td> 100 ms</td><td>0.4 s</td><td>0.3 s</td>
 *   </tr>
 *   <tr>
 *    <td> 300 ms</td><td>1.0 s</td><td>0.5 s</td>
 *   </tr>
 *   <tr>
 *    <td>1000 ms</td><td>3.1 s</td><td>1.0 s</td>
 *   <tr>
 *  </tbody>
 * </table>
 * <p>
 * (The tests were run on a MacBook Pro with 2.26GHz Duo Core processor).
 * </p>
 * <p>
 * There are three test methods each needing the TIME_TO_WAIT ms to run.
 * </p>
 *
 * @author oliver
 * @since 30.11.2009
 */
@RunWith(ParallelRunner.class)
public final class RunTestsParallelJUnit4Test {

    private static final Logger log = LogManager.getLogger(RunTestsParallelJUnit4Test.class);
    private static int setUpCalled;

    /** The Constant TIME_TO_WAIT. */
    protected static final int TIME_TO_WAIT = 50;

    /** Is only for testing visible. */
    public static int setUpBeforeClassCalled;

    /**
     * Default constructor.
     */
    public RunTestsParallelJUnit4Test() {
        log.info(this + " created.");
    }

    /**
     * Here we set some static attributes which we will test in
     * tearDownAfterClass().
     * @see #tearDownAfterClass()
     */
    @BeforeClass
    synchronized public static void setUpBeforeClass() {
        setUpCalled = 1;
        setUpBeforeClassCalled++;
        log.info("setUpBeforeClass(): called=" + setUpCalled);
    }

    /**
     * Here we test some static attributes we have set up in
     * setUpBeforeClass(). Because this class can be called several times
     * in parallel the statement
     * <code>assertEquals(1, setUpBeforeClassCalled);</code>
     * was removed at the end of this method.
     *
     * @see #setUpBeforeClass()
     */
    @AfterClass
    synchronized public static void tearDownAfterClass() {
        log.info("tearDownAfterClass(): checking called=" + setUpCalled);
        if (setUpBeforeClassCalled > 1) {
            log.info("setUpBeforeClass() was called {} times (which is ok for parallel calls)",
                    setUpBeforeClassCalled);
        }
    }

    /**
     * Here we count how often setUp() was called.
     */
    @Before
    synchronized public void setUp() {
        setUpCalled++;
        log.info("setUp(): called=" + setUpCalled);
    }

    /**
     * Here we log how often setUp() was called.
     */
    @After
    public void tearDown() {
        log.info("tearDown(): called=" + setUpCalled);
    }

    /**
     * Only a test dummy.
     */
    @Test
    public void testDummy() {
        log.info("testDummy(): called=" + setUpCalled);
        ThreadUtil.sleep(TIME_TO_WAIT);
    }

    /**
     * Simple test method which should return normally.
     */
    @Test
    public void testOk() {
        log.info("testOk(): called=" + setUpCalled);
        ThreadUtil.sleep(TIME_TO_WAIT);
        assertTrue("called was not initialized", setUpCalled >= 1);
    }

    /**
     * Simple test that expects to fail.
     */
    @Test(expected = AssertionError.class)
    public void testFail() {
        log.info("testFail(): called=" + setUpCalled);
        ThreadUtil.sleep(TIME_TO_WAIT);
        fail("ups");
    }

    /**
     * Here we test if expected exceptions are thrown correct.
     *
     * @throws MalformedURLException is expected because the used URL is wrong
     * @since 1.0
     */
    @Test(expected = MalformedURLException.class)
    public void testExpectedException() throws MalformedURLException {
        new URL("x");
    }

}
