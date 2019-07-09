/*
 * Copyright (c) 2008-2019 by Oliver Boehm
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
 * (c)reated 01.02.2009 by oliver (ob@oasd.de)
 */
package patterntesting.sample.dbc;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import patterntesting.runtime.util.Assertions;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The Class CoffeeAccountTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 01.02.2009
 */
public class CoffeeAccountTest {

    private static final Log log = LogFactoryImpl.getLog(CoffeeAccountTest.class);
    private static final List<CoffeeAccount> coffeeList = new ArrayList<>();

    /**
     * Setup before class.
     */
    @BeforeAll
    public static void setUpBeforeClass() {
        coffeeList.add(new CoffeeAccount("Cap Puccino"));
        coffeeList.add(new CoffeeAccount("Irish Coffee"));
        coffeeList.add(new CoffeeAccount("Nes Cafe"));
        coffeeList.add(new CoffeeAccount("Duke"));
        coffeeList.add(new CoffeeAccount("S. Presso"));
    }

    /**
     * The coffeeList is not sorted so the precondition for calling
     * Collections.binarySearch() is not fullfilled. We expect a thrown
     * ContractViolation.
     */
    @Test
    public final void testBinarySearchUnsorted() {
        assertThrows(AssertionError.class, () -> {
            assertTrue(Assertions.ENABLED, "assertion must be enabled for this test");
            CoffeeAccount duke = new CoffeeAccount("Duke");
            assertFalse(Collections.isSorted(coffeeList), "list must be unsorted to see ContractViolation");
            Collections.binarySearch(coffeeList, duke);
        });
    }

    /**
     * Test binary search sorted.
     */
    @Test
    public final void testBinarySearchSorted() {
        List<CoffeeAccount> sorted = new ArrayList<>(coffeeList);
        java.util.Collections.sort(sorted);
        CoffeeAccount duke = new CoffeeAccount("Duke");
        assertTrue(Collections.isSorted(sorted), "list is not sorted");
        int found = Collections.binarySearch(sorted, duke);
        log.info(duke + " found as element " + found);
        assertTrue(found >= 0, found + " elements found");
        assertTrue(found < sorted.size(), "more as" + sorted.size() + " elements found");
    }

    /**
     * Test equals.
     */
    @Test
    public final void testEquals() {
        CoffeeAccount one = new CoffeeAccount("Mocha");
        CoffeeAccount two = new CoffeeAccount("Mocha");
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
    }

    /**
     * When you will start this test method you will got a ContractViolation
     * because of the new added debitServiceFee() method.
     * <p>
     * Uncomment @Test to see the ContractViolation
     * </p>
     */
    //@Test
    public final void testGetCupOfCoffee() {
        CoffeeAccount ca = new CoffeeAccount("Donald");
        ca.payIn(25);
        ca.getCupOfCoffee();
    }

}
