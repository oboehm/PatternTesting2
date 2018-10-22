/*
 * $Id: CollectionsTest.java,v 1.11 2016/12/18 20:19:41 oboehm Exp $
 *
 * Copyright (c) 2008 by Oliver Boehm
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
 * (c)reated 03.02.2009 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.dbc;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import patterntesting.runtime.util.Assertions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * For the Collections class of JDK you can define some preconditions which are
 * collected in {@link CollectionsAspect}. This is the test class for this
 * aspect.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.11 $
 * @since 03.02.2009
 */
public class CollectionsTest {

    private static final List<String> sortedList = new ArrayList<>();
    private static final List<String> unsortedList = new ArrayList<>();
    private static final List<StringBuilder> sortedListBuffer = new ArrayList<>();
    private static final List<StringBuilder> unsortedListBuffer = new ArrayList<>();

    /**
     * Sets the up before class.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        setUpSortedList();
        setUpUnsortedList();
        for (String s : sortedList) {
            sortedListBuffer.add(new StringBuilder(s));
        }
        for (String s : unsortedList) {
            unsortedListBuffer.add(new StringBuilder(s));
        }
    }

    private static void setUpSortedList() {
        sortedList.add("Capuccino");
        sortedList.add("Espresso");
        sortedList.add("Irish Coffee");
        sortedList.add("Java");
        sortedList.add("Mocha");
    }

    private static void setUpUnsortedList() {
        unsortedList.add("Java");
        unsortedList.add("Mocha");
        unsortedList.add("Capuccino");
        unsortedList.add("Irish Coffee");
        unsortedList.add("Espresso");
    }

    /**
     * Sets the up.
     */
    @Before
    public final void setUp() {
        assertTrue("assertions must be enabled", Assertions.ENABLED);
    }

    /**
     * The Class StringBuilderComparator.
     */
    static class StringBuilderComparator implements Comparator<StringBuilder>, Serializable {

        private static final long serialVersionUID = 20100126L;

        /**
         * Compare.
         *
         * @param o1 the o1
         * @param o2 the o2
         * @return the int
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
		public int compare(final StringBuilder o1, final StringBuilder o2) {
            return o1.toString().compareTo(o2.toString());
        }

    }

    /**
     * Test method for {@link Collections#binarySearch(List, Object)}.
     */
    @Test
    public final void testBinarySearchFound() {
        int found = Collections.binarySearch(sortedList, "Java");
        assertEquals(3, found);
    }

    /**
     * Test binary search with comparator found.
     */
    @Test
    public final void testBinarySearchWithComparatorFound() {
        int found = Collections.binarySearch(sortedListBuffer,
                new StringBuilder("Java"), new StringBuilderComparator());
        assertEquals(3, found);
    }

    /**
     * Test binary search not found.
     */
    @Test
    public final void testBinarySearchNotFound() {
        int notfound = Collections.binarySearch(sortedList, "Latte");
        assertEquals(-5, notfound);
    }

    /**
     * Test binary search unsorted.
     */
    @Test(expected = ContractViolation.class)
    public final void testBinarySearchUnsorted() {
        Collections.binarySearch(unsortedList, "Java");
    }

    /**
     * Test binary search unsorted list buffer.
     */
    @Test(expected = ContractViolation.class)
    public final void testBinarySearchUnsortedListBuffer() {
        Collections.binarySearch(unsortedListBuffer, new StringBuilder("Java"),
                new StringBuilderComparator());
    }

}
