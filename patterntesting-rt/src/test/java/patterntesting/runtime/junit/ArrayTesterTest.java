/*
 * $Id: ArrayTesterTest.java,v 1.2 2016/03/14 21:02:38 oboehm Exp $
 *
 * Copyright (c) 2014 by Oliver Boehm
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
 * (c)reated 27.08.2014 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * JUnit tests for {@link ArrayTester} class.
 *
 * @author oliver
 * @since 1.5 (27.08.2014)
 */
public final class ArrayTesterTest {

    /**
     * Test method for {@link ArrayTester#assertEquals(Object[], Object[])}.
     */
    @Test
    public void testAssertEquals() {
        String[] a1 = { "one", "two", "three" };
        String[] a2 = { "one", "two", "three" };
        ArrayTester.assertEquals(a1, a2);
        ArrayTester.assertEquals(a2, a1);
    }

    /**
     * Test method for {@link ArrayTester#assertEquals(Object[], Object[])}.
     */
    @Test
    public void testAssertNotEquals() {
        assertThrows(AssertionError.class, () -> {
            String[] a1 = {"one", "two", "three"};
            String[] a2 = {"one", "two", "four"};
            ArrayTester.assertEquals(a1, a2);
        });
    }

    /**
     * Test method for {@link ArrayTester#assertEquals(Object[], Object[])}.
     */
    @Test
    public void testAssertNotEqualsSize() {
        assertThrows(AssertionError.class, () -> {
            String[] a1 = {"one", "two", "three"};
            String[] a2 = {"one", "two"};
            ArrayTester.assertEquals(a1, a2);
        });
    }

    /**
     * Test method for {@link ArrayTester#assertEquals(Object[], Object[])}.
     */
    @Test
    public void testAssertNotEqualsByteArray() {
        assertThrows(AssertionError.class, () -> {
            byte[] a1 = { 1, 2, 3 };
            byte[] a2 = { 1, 2, 4 };
            ArrayTester.assertEquals(a1, a2);
        });
    }

    /**
     * Test method for {@link ArrayTester#assertEquals(Object[], Object[])}.
     */
    @Test
    public void testAssertNotEqualsSizeByteArray() {
        assertThrows(AssertionError.class, () -> {
            byte[] a1 = {1, 2, 3};
            byte[] a2 = {1, 2};
            ArrayTester.assertEquals(a1, a2);
        });
    }

}

