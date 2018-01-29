/*
 * $Id: StringTesterTest.java,v 1.2 2016/10/24 20:22:06 oboehm Exp $
 *
 * Copyright (c) 2016 by Oliver Boehm
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
 * (c)reated 21.05.2016 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import org.junit.Test;

/**
 * Unit tests for {@link StringTester} class.
 *
 * @author oliver (ob@aosd.de)
 */
public class StringTesterTest {

    /**
     * Test method for {@link StringTester#assertNotEmpty(String)}.
     */
    @Test
    public void testAssertNotEmpty() {
        StringTester.assertNotEmpty("hello world");
    }

    /**
     * Test method for {@link StringTester#assertNotEmpty(String)}.
     */
    @Test(expected = AssertionError.class)
    public void testAssertNotEmptyWithEmpty() {
        StringTester.assertNotEmpty("");
    }

    /**
     * Test method for {@link StringTester#assertEmpty(String)}.
     */
    @Test
    public void testAssertEmpty() {
        StringTester.assertEmpty("");
    }

}

