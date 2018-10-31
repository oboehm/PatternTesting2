/*
 * $Id: CollectionTesterTest.java,v 1.6 2016/12/10 20:55:22 oboehm Exp $
 *
 * Copyright (c) 2012 by Oliver Boehm
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
 * (c)reated 13.01.2012 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * JUnit tests for {@link CollectionTester} class.
 *
 * @author oliver (ob@aosd.de)
 * @since 1.2.10-YEARS (13.01.2012)
 */
public final class CollectionTesterTest {

    /**
     * Test method for {@link CollectionTester#assertEquals(java.util.Collection, java.util.Collection)}.
     */
    @Test
    public void testAssertEquals() {
        Collection<String> c1 = Arrays.asList("one", "two", "three");
        Collection<String> c2 = new ArrayList<>(c1);
        CollectionTester.assertEquals(c1, c2);
    }

    /**
     * Test method for {@link CollectionTester#assertEquals(java.util.Collection, java.util.Collection)}.
     */
    @Test
    public void testAssertNotEquals() {
        assertThrows(AssertionError.class, () -> {
            Collection<String> c1 = Arrays.asList("one", "two", "three");
            Collection<String> c2 = Arrays.asList("one", "two", "four");
            CollectionTester.assertEquals(c1, c2);
        });
    }

    /**
     * Test method for {@link CollectionTester#assertEquals(java.util.Collection, java.util.Collection)}.
     */
    @Test
    public void testAssertNotEqualsSize() {
        assertThrows(AssertionError.class, () -> {
            Collection<String> c1 = Arrays.asList("one", "two", "three");
            Collection<String> c2 = Arrays.asList("one", "two");
            CollectionTester.assertEquals(c1, c2);
        });
    }

    /**
     * Two sets are equals if its members are equals independent from the
     * internal order.
     *
     * @since 1.4 (19.12.2013)
     */
    @Test
    public void testAssertEqualsWithSet() {
        Set<String> s1 = new HashSet<>(Arrays.asList("one", "two", "three"));
        Set<Object> s2 = new TreeSet<Object>(Arrays.asList("three", "two", "one"));
        CollectionTester.assertEquals(s1, s2);
    }

    /**
     * Two sets are not equals if at least one element is different.
     *
     * @since 1.4 (19.12.2013)
     */
    @Test
    public void testAssertNotEqualsWithSet() {
        assertThrows(AssertionError.class, () -> {
            Set<String> s1 = new HashSet<>(Arrays.asList("one", "two", "three"));
            Set<String> s2 = new HashSet<>(Arrays.asList("one", "two", "four"));
            CollectionTester.assertEquals(s1, s2);
        });
    }

}

