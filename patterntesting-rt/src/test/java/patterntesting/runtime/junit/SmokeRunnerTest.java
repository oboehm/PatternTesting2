/*
 * $Id: SmokeRunnerTest.java,v 1.17 2016/12/18 20:19:38 oboehm Exp $
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
 * (c)reated 29.03.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.commons.lang3.SystemUtils;
import org.junit.*;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import patterntesting.runtime.annotation.*;
import patterntesting.runtime.util.*;

/**
 * JUnit tests for {@link SmokeRunner} class.
 *
 * @author oliver
 * @since 1.0 (29.03.2010)
 */
@RunWith(SmokeRunner.class)
public class SmokeRunnerTest {

    private static boolean started = false;
    private int counter = 0;

    /**
     * Only to see if the tests were started.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        started = true;
    }

    /**
     * Here we check again if the tests were really started.
     */
    @AfterClass
    public static void tearDownAfterClass() {
        assertTrue("setUpBeforeClass() was not called", started);
        started = false;
    }

    /**
     * We increase the internal counter.
     */
    @Before
    public void setUp() {
        assertTrue("setUpBeforeClass() was not called", started);
        assertEquals(0, counter);
        counter++;
    }

    /**
     * We decrease (and check) the internal counter.
     */
    @After
    public void tearDown() {
        assertEquals(1, counter);
        counter--;
    }

    /**
     * Only to see if this test class is handled correct.
     */
    @Test
    public void testCounter() {
        assertTrue("setUpBeforeClass() was not called", started);
        assertEquals(1, counter);
    }

    /**
     * Test method for <code>SmokeRunner.testCount()</code>.
     * This is the test case for JUnit 3.
     *
     * @throws InitializationError if SmokeRunner can't be created
     */
    @Test
    public void testCount3() throws InitializationError {
        SmokeRunner runner3 = new SmokeRunner(patterntesting.runtime.junit.v3.BrokenTest.class);
        assertEquals(1, runner3.testCount());
    }

    /**
     * Test method for <code>SmokeRunner.testCount()</code>.
     * This is the test case for JUnit 4.
     *
     * @throws InitializationError if SmokeRunner can't be created
     */
    @Test
    public void testGetCount4() throws InitializationError {
        ParentRunner<?> reference = new BlockJUnit4ClassRunner(SmokeRunnerTest.class);
        SmokeRunner runner4 = new SmokeRunner(SmokeRunnerTest.class);
        assertEquals(reference.testCount(), runner4.testCount());
    }

    /**
     * For a test marked as @Ignored we expect the same result as with the
     * original {@link BlockJUnit4ClassRunner} of JUnit.
     *
     * @throws InitializationError the initialization error
     */
    @Test
    public void testTestCountIgnore() throws InitializationError {
        ParentRunner<?> reference = new BlockJUnit4ClassRunner(SmokeRunnerIgnoreTest.class);
        checkChildrenCount(reference);
        SmokeRunner runner4 = new SmokeRunner(SmokeRunnerIgnoreTest.class);
        checkChildrenCount(runner4);
        assertEquals(reference.testCount(), runner4.testCount());
    }

    /**
     * There are 5 tests for MacOS and 8 for Linux in
     * {@link patterntesting.runtime.junit.v4.RunTestOnTest} where the hidden
     * flag in {@link RunTestOn} is set.
     *
     * @throws InitializationError the initialization error
     */
    @Test
    public void testTestCountHide() throws InitializationError {
        ParentRunner<?> reference = new BlockJUnit4ClassRunner(patterntesting.runtime.junit.v4.RunTestOnTest.class);
        SmokeRunner runner4 = new SmokeRunner(patterntesting.runtime.junit.v4.RunTestOnTest.class);
        int hidden = reference.testCount() - runner4.testCount();
        int expected = SystemUtils.IS_OS_MAC ? 5 : 8;
        assertEquals(expected, hidden);
    }

    /**
     * The class RunTestOnFakeTest has a RunTestOn(hide=true) annotation on
     * class level. This is not supported because of limitation of some JUnit
     * test runners. So an {@link UnsupportedOperationException} is expected
     * here.
     *
     * @throws InitializationError the initialization error
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testTestCountNoTestMethods() throws InitializationError {
        SmokeRunner smokeRunner = new SmokeRunner(patterntesting.runtime.junit.v4.RunTestOnFakeTest.class);
        smokeRunner.getChildren();
    }

    /**
     * Check children count. The number of test methods and result of
     * <tt>testCount()</tt> should be the same.
     *
     * @param runner the runner
     * @throws InitializationError the initialization error
     */
    private static void checkChildrenCount(final ParentRunner<?> runner) throws InitializationError {
        @SuppressWarnings("unchecked")
        List<FrameworkMethod> testMethods = (List<FrameworkMethod>) ReflectionHelper.invokeMethod(
                runner, "getChildren");
        assertEquals("testCount() and size of getChildren() differs", runner.testCount(),
                testMethods.size());
    }

    /**
     * Test method for {@link SmokeRunner#getDescription()}. We compare this
     * metehod against the result from a runner of the JUnit library.
     *
     * @throws InitializationError the initialization error
     */
    @Test
    public void testGetDescription() throws InitializationError {
        SmokeRunner smokeRunner = new SmokeRunner(XFilterTest.class);
        ParentRunner<?> reference = new BlockJUnit4ClassRunner(XFilterTest.class);
        Description referenceDescription = reference.getDescription();
        Description smokeDescription = smokeRunner.getDescription();
        CollectionTester.assertEquals(referenceDescription.getChildren(),
                smokeDescription.getChildren());
    }

    /**
     * This test is only for looking at the JUnit runner of Eclipse.
     * You should see at least 20 ms as duration for this method.
     */
    @Test
    public void testDuration() {
        ThreadUtil.sleep(20);
    }

    /**
     * This test is only to see if expected exceptions are handled correct.
     */
    @Test(expected = RuntimeException.class)
    public void testException() {
        throw new IllegalArgumentException("ups");
    }

    /**
     * This test should be ignored.
     * <p>
     * This test is now commented out to avoid skipped tests for sonar.
     * In case of problems activate it again.
     * </p>
     */
    //@Test
    @Ignore
    public void testIgnored() {
        fail("this test should be ignored");
    }

    /**
     * This test should be ignored.
     * <p>
     * This test is now commented out to avoid skipped tests for sonar.
     * In case of problems activate it again.
     * </p>
     */
    //@Test
    @Ignore("intentionally to be ignored")
    public void testIgnoredWithReason() {
        fail("this test should be ignored");
    }

    /**
     * This test should be ignored.
     * <p>
     * This test is now commented out to avoid skipped tests for sonar.
     * In case of problems activate it again.
     * </p>
     */
    //@Test
    @Broken("intentionally marked as 'broken'")
    public void testBroken() {
        fail("this test should be ignored because marked as 'broken'");
    }

    /**
     * This test should only be executed if integration tests are enabled.
     */
    @Test
    @IntegrationTest
    public void testIntegrationTest() {
        if (!Environment.INTEGRATION_TEST_ENABLED) {
            fail("this test should be only run if integration tests are enabled");
        }
    }

}

