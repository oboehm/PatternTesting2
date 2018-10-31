/*
 * $Id: ObjectTesterTest.java,v 1.34 2016/12/18 20:19:38 oboehm Exp $
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
 * (c)reated 21.07.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import org.junit.jupiter.api.Test;
import patterntesting.runtime.annotation.IntegrationTest;
import patterntesting.runtime.annotation.SkipTestOn;
import patterntesting.runtime.init.Crash;
import patterntesting.runtime.init.RunOnInitializerBug;
import patterntesting.runtime.junit.test.*;
import patterntesting.runtime.monitor.Dummy;

import java.io.NotSerializableException;
import java.util.Date;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit test for ObjectTester class.
 *
 * @author oliver
 * @since 1.1 (21.07.2010)
 */
public final class ObjectTesterTest {

    private static Boolean hasCorrectHashCode = true;

    /**
     * Test method for {@link ObjectTester#assertEquals(Object, Object)}.
     */
    @Test
    public void testAssertEquals() {
        ObjectTester.assertEquals("hello", "hello");
    }

    /**
     * Test method for {@link ObjectTester#assertEquals(Object, Object)}.
     */
    @Test
    public void testAssertInequality() {
        assertThrows(AssertionError.class, () -> ObjectTester.assertEquals("hello", "world"));
    }

    /**
     * Test method for {@link ObjectTester#assertNotEquals(Object, Object)}.
     */
    @Test
    public void testAssertNotEquals() {
        ObjectTester.assertNotEquals("hello", "world");
    }

    /**
     * Test method for {@link ObjectTester#assertNotEquals(Object, Object)}.
     */
    @Test
    public void testAssertNotEqualsWithEquals() {
        assertThrows(AssertionError.class, () -> ObjectTester.assertNotEquals("hello", "hello"));
    }

    /**
     * Test method for {@link ObjectTester#assertEquals(java.io.Serializable)}.
     *
     * @throws NotSerializableException if test object is not serializable
     */
    @Test
    public void testAssertEqualsSerializable() throws NotSerializableException {
        ObjectTester.assertEquals("hello");
    }

    /**
     * Test method for {@link ObjectTester#assertEquals(Class)}.
     */
    @Test
    public void testAssertEqualsClass() {
        ObjectTester.assertEquals(Person.class);
    }

    /**
     * The {@link Lottery} class does not implement the equals method
     * correct. If you have a derived {@link PredictableLottery} object
     * and call equals with 'null' as parameter you will get an
     * NullPointerException.
     *
     * @since 1.2.20
     */
    @Test
    public void testAssertEqualsNull() {
        assertThrows(AssertionError.class, () -> {
            ObjectTester.assertEquals(PredictableLottery.class);
            Lottery lottery = new PredictableLottery();
            assertFalse(lottery.equals(null));
        });
    }

    /**
     * Two sheeps with the same name should be equals. But the sheep class
     * hasn't overwritten this method so we expect an error here.
     */
    @Test
    public void testAssertEqualsSheep() {
        assertThrows(AssertionError.class, () -> {
            Sheep sh1 = new Sheep("Dolly");
            Sheep sh2 = new Sheep("Dolly");
            ObjectTester.assertEquals(sh1, sh2);
        });
    }

    /**
     * Also for the Sheep class we expect an error.
     */
    @Test
    public void testAssertEqualsSheepClass() {
        assertThrows(AssertionError.class, () -> ObjectTester.assertEquals(Sheep.class));
    }

    /**
     * Test method for {@link ObjectTester#assertEquals(Cloneable)}.
     */
    @Test
    public void testAssertEqualsCloneable() {
        ObjectTester.assertEquals(Date.class);
    }

    /**
     * Test method for {@link ObjectTester#assertEquals(Package, Class...)}.
     */
    @IntegrationTest
    @Test
    public void testAssertEqualsOfPackage() {
        ObjectTester.assertEqualsOfPackage("patterntesting.runtime", ObjectTesterTest.class,
                Lottery.class, PredictableLottery.class, Dummy.class);
    }

    /**
     * Test method for {@link ObjectTester#assertEquals(Package, Pattern...)}.
     */
    @IntegrationTest
    @Test
    public void testAssertEqualsPackagePattern() {
        Package pkg = this.getClass().getPackage();
        ObjectTester.assertEquals(pkg, Pattern.compile(".*Test"), Pattern.compile(".*Lottery"),
                Pattern.compile(Dummy.class.getName()));
    }

    /**
     * Test method for {@link ObjectTester#assertEquals(Package, Pattern...)}
     * <p>
     * Because of the use of Tomcat 8 for testing which needs a least Java 7
     * we skip this test on Java 6.
     * </p>
     */
    @Test
    @SkipTestOn(javaVersion="1.6")
    public void testAssertEqualsOfPackagePattern() {
        ObjectTester.assertEqualsOfPackage("patterntesting.runtime", Pattern.compile(".*Test"),
                Pattern.compile(".*Lottery"), Pattern.compile(Dummy.class.getName()));
    }

    /**
     * Test method for {@link ObjectTester#assertEquals(Package)}.
     */
    @Test
    public void testAssertEqualsOfPackageFailure() {
        assertThrows(AssertionError.class, () -> {
            synchronized (ObjectTesterTest.class) {
                hasCorrectHashCode = false;
                try {
                    ObjectTester.assertEqualsOfPackage("patterntesting.runtime.junit");
                } finally {
                    hasCorrectHashCode = true;
                }
            }
        });
    }

    /**
     * Test method for
     * {@link ObjectTester#assertEqualsOfPackage(String, Class...)}.
     */
    @Test
    public void testAssertEqualsOfPackageExcluded() {
        synchronized (ObjectTesterTest.class) {
            hasCorrectHashCode = false;
            try {
                ObjectTester.assertEqualsOfPackage("patterntesting.runtime.junit", ObjectTesterTest.class,
                        Lottery.class, PredictableLottery.class);
            } finally {
                hasCorrectHashCode = true;
            }
        }
    }

    /**
     * Here we want to test if classes derived from an interface are really
     * excluded.
     */
    @Test
    public void testAssertEqualsOfPackageInterfaceExcluded() {
        ObjectTester.assertEquals(Gamble.class.getPackage(), Gamble.class);
    }

    /**
     * This is the test case for
     * <a href="https://sourceforge.net/p/patterntesting/bugs/17/">bug 17</a>.
     * It checks if the package parameter is recognized correct.
     *
     * @since 1.0.3
     */
    @Test
    public void testBug17() {
        ObjectTester.assertEquals(Person.class.getPackage(), Person.class, Lottery.class, PredictableLottery.class);
    }

    /**
     * Test method for {@link ObjectTester#hasToStringDefaultImpl(Object)}.
     */
    @Test
    public void testHasToStringDefaultImpl() {
        String msg = this + " has toString() not overwritten";
        assertTrue(ObjectTester.hasToStringDefaultImpl(this), msg);
        assertTrue(ObjectTester.hasToStringDefaultImpl(this.getClass()), msg);
    }

    /**
     * Test method for {@link ObjectTester#assertAll(Class)}.
     */
    @Test
    public void testCheckAll() {
        ObjectTester.assertAll(String.class);
    }

    /**
     * Test method for {@link ObjectTester#assertAllOfPackage(String)}.
     */
    @IntegrationTest
    @Test
    public void testCheckAllOfPackage() {
        ObjectTester.assertAllOfPackage("patterntesting.runtime", Pattern.compile(".*Test"),
                Pattern.compile(".*Lottery"),
                Pattern.compile(Crash.class.getName()),
                Pattern.compile(RunOnInitializerBug.class.getName()),
                Pattern.compile(Dummy.class.getName()));
    }



    /////   overwritten methods   /////////////////////////////////////////////

    /**
     * To be able to test equality we overwrite here the equals method.
     * Each ObjectTesterTest objects are considered as equals.
     *
     * @param other the other object
     * @return always true
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object other) {
        if (other == null) {
            return false;
        }
        return true;
    }

    /**
     * For testing purposes we can control if the hashCode implementation is
     * correct or not.
     *
     * @return the hashCode
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        if (hasCorrectHashCode) {
            return 0;
        }
        return super.hashCode();
    }

}

