/*
 * $Id: MapTesterTest.java,v 1.5 2016/12/10 20:55:22 oboehm Exp $
 *
 * Copyright (c) 2013 by Oliver Boehm
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
 * (c)reated 18.12.2013 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import java.util.*;

import org.junit.Test;

/**
 * Unit tests for {@link MapTester} class.
 *
 * @author oliver
 * @since 1.4 (18.12.2013)
 */
public class MapTesterTest {

    private final Map<String, Object> m1 = new HashMap<>();
    private final Map<Object, Object> m2 = new Properties();

    /**
     * Test method for {@link MapTester#assertEquals(Map, Map)}.
     * Two empty maps should be considered as empty even if they have different
     * types.
     */
    @Test
    public void testAssertEqualsEmptyMaps() {
        MapTester.assertEquals(m1, m2);
    }

    /**
     * Test method for {@link MapTester#assertEquals(Map, Map)}.
     */
    @Test
    public void testAssertEquals() {
        fill(m1, "one", 1, "two", 2);
        fill(m2, "two", 2, "one", 1);
        MapTester.assertEquals(m1, m2);
        MapTester.assertEquals(m2, m1);
        MapTester.assertEquals(m1, m1);
        MapTester.assertEquals(m2, m2);
    }

    /**
     * This is the test case for <a
     * href="https://sourceforge.net/p/patterntesting/bugs/28/">bug 28</a>.
     */
    @Test
    public void testAssertEqualsWithNullValue() {
        m1.put("nix", null);
        MapTester.assertEquals(m1, m1);
    }

    /**
     * This is another test case for <a
     * href="https://sourceforge.net/p/patterntesting/bugs/28/">bug 28</a>.
     */
    @Test(expected = AssertionError.class)
    public void testAssertNotEqualsWithNullValue() {
        fill(m1, "one", 1, "two", null);
        fill(m2, "two", 2, "one", 1);
        MapTester.assertEquals(m1, m2);
    }

    /**
     * Test method for {@link MapTester#assertEquals(Map, Map)}.
     */
    @Test(expected = AssertionError.class)
    public void testAssertNotEqualsValues() {
        fill(m1, "one", 1, "two", 2, "three", 3);
        fill(m2, "one", 1, "two", 2, "three", 4711);
        MapTester.assertEquals(m1, m2);
    }

    /**
     * Test method for {@link MapTester#assertEquals(Map, Map)}.
     */
    @Test(expected = AssertionError.class)
    public void testAssertNotEqualsKeys() {
        fill(m1, "one", 1, "two", 2, "three", 3);
        fill(m2, "one", 1, "two", 2, "four", 3);
        MapTester.assertEquals(m1, m2);
    }

    /**
     * Test method for {@link MapTester#assertEqualKeys(Map, Map)}.
     */
    @Test
    public void testAssertEqualKeys() {
        fill(m1, "one", -1, "two", -2);
        fill(m2, "two", 22, "one", 11);
        MapTester.assertEqualKeys(m1, m2);
    }

    /**
     * Test method for {@link MapTester#assertEqualKeys(Map, Map)}.
     */
    @Test(expected = AssertionError.class)
    public void testAssertNotEqualKeys() {
        fill(m1, "one", -1, "two", -2);
        fill(m2, "two", 22, "three", 3);
        MapTester.assertEqualKeys(m1, m2);
    }

    /**
     * Test method for {@link MapTester#assertEqualValues(Map, Map)}.
     */
    @Test
    public void testAssertEqualValues() {
        fill(m1, "one", 1, "two", 2);
        fill(m2, "zwei", 2, "eins", 1);
        MapTester.assertEqualValues(m1, m2);
    }

    /**
     * Test method for {@link MapTester#assertEqualValues(Map, Map)}.
     */
    @Test(expected = AssertionError.class)
    public void testAssertNotEqualValues() {
        fill(m1, "one", 1, "two", 2);
        fill(m2, "two", 2, "three", 3);
        MapTester.assertEqualValues(m1, m2);
    }

    private static void fill(final Map<? extends Object, ? extends Object> map, final Object... keyValues) {
        @SuppressWarnings("unchecked")
        Map<Object, Object> objMap = (Map<Object, Object>) map;
        for (int i = 0; i < keyValues.length; i+=2) {
            objMap.put(keyValues[i], keyValues[i+1]);
        }
    }

}

