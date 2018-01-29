/*
 * $Id: SmokeFilterTest.java,v 1.7 2016/12/18 20:19:38 oboehm Exp $
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
 * (c)reated 30.07.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Method;

import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.Test;
import org.junit.runner.Description;

import patterntesting.runtime.junit.v4.*;
import patterntesting.runtime.util.Environment;

/**
 * The Class SmokeFilterTest.
 *
 * @author oliver
 * @since 1.0.2 (30.07.2010)
 */
public final class SmokeFilterTest {

    private static final Logger log = LogManager.getLogger(SmokeFilterTest.class);
    private final SmokeFilter filter = new SmokeFilter();

    /**
     * Test method for
     * {@link patterntesting.runtime.junit.internal.SmokeFilter#describe()}.
     */
    @Test
    public void testDescribe() {
        String describe = filter.describe();
        assertNotNull(describe);
        log.info(describe);
    }

    /**
     * Test method for
     * {@link patterntesting.runtime.junit.internal.SmokeFilter#shouldRun(org.junit.runner.Description)}
     * .
     */
    @Test
    public void testShouldRunDescription() {
        Description description = Description.createTestDescription(this.getClass(),
                "testShouldRunDescription");
        assertEquals(description.toString(), !Environment.SMOKE_TEST_ENABLED,
                filter.shouldRun(description));
    }

    /**
     * Test method for
     * {@link SmokeFilter#shouldRun(org.junit.runner.Description)}.
     *
     * @throws SecurityException should not happen
     * @throws NoSuchMethodException should not happen
     */
    @Test
    public void testRunTestOn() throws SecurityException, NoSuchMethodException {
        boolean shouldRun = !SystemUtils.OS_NAME.startsWith("Windows");
        checkShouldRun(RunTestOnTest.class, "testRunOnMacAndLinux", shouldRun);
    }

    /**
     * Test method for
     * {@link SmokeFilter#shouldRun(org.junit.runner.Description)}.
     *
     * @throws SecurityException should not happen
     * @throws NoSuchMethodException should not happen
     */
    @Test
    public void testRunTestOnFake() throws SecurityException, NoSuchMethodException {
        checkShouldRun(RunTestOnTest.class, "testRunOnFakeVersion", false);
    }

    /**
     * Test method for
     * {@link SmokeFilter#shouldRun(org.junit.runner.Description)}.
     *
     * @throws SecurityException should not happen
     * @throws NoSuchMethodException should not happen
     */
    @Test
    public void testSkipTestOn() throws SecurityException, NoSuchMethodException {
        boolean shouldRun = SystemUtils.OS_NAME.startsWith("Windows");
        checkShouldRun(SkipTestOnTest.class, "testSkipOnMacAndLinux", shouldRun);
    }

    /**
     * Test method for
     * {@link SmokeFilter#shouldRun(org.junit.runner.Description)}.
     *
     * @throws SecurityException should not happen
     * @throws NoSuchMethodException should not happen
     */
    @Test
    public void testSkipTestOnFake() throws SecurityException, NoSuchMethodException {
        checkShouldRun(SkipTestOnTest.class, "testSkipOnFakeVersion", true);
    }

    /**
     * Test method for
     * {@link SmokeFilter#shouldRun(org.junit.runner.Description)}.
     *
     * @throws SecurityException should not happen
     * @throws NoSuchMethodException should not happen
     */
    @Test
    public void testBroken() throws SecurityException, NoSuchMethodException {
        checkShouldRun(BrokenTest.class, "testBroken", false);
    }

    /**
     * Test method for
     * {@link SmokeFilter#shouldRun(org.junit.runner.Description)}.
     *
     * @throws SecurityException should not happen
     * @throws NoSuchMethodException should not happen
     */
    @Test
    public void testBrokenAndFixed() throws SecurityException, NoSuchMethodException {
        checkShouldRun(BrokenTest.class, "testBrokenAndFixed", true);
    }

    private void checkShouldRun(final Class<?> testClass, final String methodName, final boolean yes) throws SecurityException, NoSuchMethodException {
        Description description = createTestDescription(testClass, methodName);
        if (!Environment.SMOKE_TEST_ENABLED) {
            assertEquals(!yes, filter.shouldBeIgnored(description));
        }
    }

    /**
     * Test methods marked as <code>@IntegrationTest</code> should be ignored
     * if integration tests are not enabled.
     *
     * @throws SecurityException the security exception
     * @throws NoSuchMethodException the no such method exception
     */
    @Test
    public void testIgnoreIntegrationTestMethod() throws SecurityException, NoSuchMethodException {
        checkShouldBeIgnored(IntegrationTestTest.class, "testIntegrationTest");
    }

    /**
     * All test methods inside a JUnit test class marked as
     * <code>@IntegrationTest</code> should be ignored
     * if integration tests are not enabled.
     *
     * @throws SecurityException the security exception
     * @throws NoSuchMethodException the no such method exception
     */
    @Test
    public void testIgnoreIntegrationTestClass() throws SecurityException, NoSuchMethodException {
        checkShouldBeIgnored(IntegrationTestClassTest.class, "testIntegrationTest");
    }

    private void checkShouldBeIgnored(final Class<?> testClass, final String methodName)
            throws NoSuchMethodException {
        Description description = createTestDescription(testClass, methodName);
        assertEquals(!Environment.INTEGRATION_TEST_ENABLED, filter.shouldBeIgnored(description));
    }

    private static Description createTestDescription(final Class<?> testClass,
            final String methodName) throws SecurityException, NoSuchMethodException {
        Method method = testClass.getMethod(methodName);
        return Description.createTestDescription(testClass, method.getName(),
                method.getAnnotations());
    }

}
