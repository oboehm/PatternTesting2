/*
 * $Id: ComparableTesterTest.java,v 1.7 2016/12/29 07:42:35 oboehm Exp $
 *
 * Copyright (c) 2011 by Oliver Boehm
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
 * (c)reated 21.09.2011 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import org.junit.jupiter.api.Test;
import patterntesting.runtime.annotation.IntegrationTest;
import patterntesting.runtime.junit.test.BlackSheep;
import patterntesting.runtime.junit.test.Person;
import patterntesting.runtime.junit.test.Sheep;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * JUnit test for {@link ComparableTester}.
 *
 * @author oliver (ob@aosd.de)
 * @since 1.2 (21.09.2011)
 */
public class ComparableTesterTest {

    /**
     * Test method for {@link ComparableTester#assertCompareTo(Comparable, Comparable)}.
     */
    @Test
    public void testAssertCompareTo() {
        Person p1 = new Person("Mickey");
        Person p2 = new Person("Goofy");
        ComparableTester.assertCompareTo(p1, p1);
        ComparableTester.assertCompareTo(p1, p2);
    }

    /**
     * Test method for {@link ComparableTester#assertCompareTo(Comparable, Comparable)}.
     */
    @Test
    public void testAssertCompareToFailing() {
        assertThrows(AssertionError.class, () -> {
            Sheep dolly = new Sheep("Dolly");
            Sheep daisy = new Sheep("Daisy");
            ComparableTester.assertCompareTo(dolly, daisy);
        });
    }

    /**
     * Test method for {@link ComparableTester#assertCompareTo(Class)}.
     */
    @Test
    public void testAssertEqualsClass() {
        ComparableTester.assertCompareTo(Person.class);
    }

    /**
     * Test method for {@link ComparableTester#assertCompareToOfPackage(String)}.
     */
    @Test
    @IntegrationTest
    public void testAssertCompareToPackageExluded() {
        ComparableTester.assertCompareToOfPackage("patterntesting.runtime", Sheep.class,
                BlackSheep.class);
    }

    /**
     * Test method for {@link ComparableTester#assertCompareTo(Package, Pattern...)}.
     */
    @Test
    public void testAssertCompareToPackagePattern() {
        ComparableTester.assertCompareTo(Sheep.class.getPackage(), Pattern.compile(".*Sheep"));
    }

    /**
     * Test method for {@link ComparableTester#assertCompareTo(Package)}.
     */
    @Test
    public void testAssertCompareToPackage() {
        assertThrows(AssertionError.class, () -> ComparableTester.assertCompareTo(this.getClass().getPackage()));
    }

}
