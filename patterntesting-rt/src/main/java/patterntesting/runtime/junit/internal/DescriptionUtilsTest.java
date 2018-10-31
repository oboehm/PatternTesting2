/*
 * $Id: DescriptionUtilsTest.java,v 1.1 2017/08/17 06:42:00 oboehm Exp $
 *
 * Copyright (c) 2017 by Oliver Boehm
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
 * (c)reated 17.08.2017 by oboehm (ob@oasd.de)
 */

package patterntesting.runtime.junit.internal;

import org.junit.jupiter.api.Test;
import org.junit.runner.Description;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link DescriptionUtils} class.
 *
 * @author oboehm
 * @version $Revision: 1.1 $
 */
public final class DescriptionUtilsTest {

    /**
     * Test method for {@link DescriptionUtils#getTestClassOf(Description)}.
     */
    @Test
    public void testGetTestClassOf() {
        Class<?> testClass = this.getClass();
        Description description = Description.createSuiteDescription(testClass.getName());
        assertEquals(testClass, DescriptionUtils.getTestClassOf(description));
    }

    /**
     * This is the test method for bug 38.
     */
    @Test
    public void testBug38() {
        Class<?> testClass = this.getClass();
        Description description = Description.createTestDescription(testClass.getName() + "||", "testBug38");
        assertEquals(testClass, DescriptionUtils.getTestClassOf(description));
    }

}
