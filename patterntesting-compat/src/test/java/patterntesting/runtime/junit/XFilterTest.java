/*
 * $Id: XFilterTest.java,v 1.9 2016/12/18 20:19:38 oboehm Exp $
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
 * (c)reated 03.05.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import patterntesting.runtime.annotation.SmokeTest;
import patterntesting.runtime.junit.internal.SmokeFilter;
import patterntesting.runtime.junit.v4.IntegrationTestTest;
import patterntesting.runtime.util.Environment;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * The Class XFilterTest.
 *
 * @author oliver
 * @since 1.0 (03.05.2010)
 */
public class XFilterTest {

    private static final Logger log = LogManager.getLogger(XFilterTest.class);
    private final Filter filter = new SmokeFilter();

    /**
     * Test method for {@link patterntesting.runtime.junit.internal.SmokeFilter#describe()}.
     */
    @Test
    public void testDescribe() {
        String description = filter.describe();
        log.info("description: " + description);
        assertNotNull(description);
    }

    /**
     * Test method for {@link patterntesting.runtime.junit.internal.SmokeFilter#shouldRun(org.junit.runner.Description)}.
     * @throws NoSuchMethodException should not happen
     */
    @Test
    @SmokeTest
    public void testShouldRunAlways() throws NoSuchMethodException {
        checkShouldRun(XFilterTest.class.getMethod("testShouldRunAlways"), true);
    }

    /**
     * Test method for {@link patterntesting.runtime.junit.internal.SmokeFilter#shouldRun(org.junit.runner.Description)}.
     * @throws NoSuchMethodException should not happen
     */
    @Test
    public void testShouldRunIntegrationTest() throws NoSuchMethodException {
        checkShouldRun(IntegrationTestTest.class.getMethod("testIntegrationTest"),
                Environment.INTEGRATION_TEST_ENABLED);
    }

    /**
     * Test method for {@link patterntesting.runtime.junit.internal.SmokeFilter#shouldRun(org.junit.runner.Description)}.
     * @throws NoSuchMethodException should not happen
     */
    @Test
    public void testShouldRunSmokeTest() throws NoSuchMethodException {
        Method smoker = XFilterTest.class.getMethod("testShouldRunAlways");
        Method nonSmoker = XFilterTest.class.getMethod("testShouldRunSmokeTest");
        checkShouldRun(smoker, true);
        checkShouldRun(nonSmoker, !Environment.SMOKE_TEST_ENABLED);
    }

    private void checkShouldRun(final Method method, final boolean expected) {
        Description description = Description.createTestDescription(
                method.getDeclaringClass(), method.getName(), method.getAnnotations());
        assertEquals(expected, filter.shouldRun(description));
    }

}

