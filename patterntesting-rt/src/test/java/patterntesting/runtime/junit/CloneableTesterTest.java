/*
 * $Id: CloneableTesterTest.java,v 1.14 2016/12/18 20:19:38 oboehm Exp $
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
 * (c)reated 06.08.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.jupiter.api.Test;
import patterntesting.runtime.annotation.IntegrationTest;
import patterntesting.runtime.junit.test.BlackSheep;
import patterntesting.runtime.junit.test.Sheep;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit test for CloneableTester class.
 *
 * @author oliver
 * @since 1.0.2 (06.08.2010)
 */
public final class CloneableTesterTest implements Cloneable {

    private static final Logger log = LoggerFactory.getLogger(CloneableTesterTest.class);

    /**
     * Test method for {@link CloneableTester#assertCloning(java.lang.Class)}.
     */
    @Test
    public void testAssertCloning() {
        CloneableTester.assertCloning(Date.class);
    }

    /**
     * Test method for {@link CloneableTester#assertCloning(Collection)}.
     */
    @Test
    public void testAssertCloningCollection() {
        Collection<Class<? extends Cloneable>> clones = new ArrayList<>();
        clones.add(Date.class);
        CloneableTester.assertCloning(clones);
    }

    /**
     * The Sheep class is a class with a wrong (or not) implemented clone()
     * method. So we expect an AssertionError for the CloneableTester.
     */
    @Test
    public void testAssertCloningSheep() {
        checkWrongCloning(Sheep.class, "clone() is not public in " + Sheep.class);
    }

    /**
     * The BlackSheep class is a class with clone implementation which returns
     * the wrong type.
     */
    @Test
    public void testAssertCloningBlackSheep() {
        checkWrongCloning(BlackSheep.class, BlackSheep.class.getName() + ".clone() creates another type: Sheep");
    }

    private static void checkWrongCloning(final Class<? extends Cloneable> cloneClass,
            final String expectedMessage) {
        try {
            CloneableTester.assertCloning(cloneClass);
            fail("AssertionError expected");
        } catch (AssertionError expected) {
            String msg = expected.getMessage();
            assertEquals(expectedMessage, msg);
        }
    }

    /**
     * Test method for {@link CloneableTester#assertCloningOfPackage(java.lang.String)}.
     */
    @SuppressWarnings("unchecked")
    @IntegrationTest
    @Test
    public void testAssertCloningOfPackage() {
        CloneableTester.assertCloningOfPackage("patterntesting.runtime", Sheep.class,
                BlackSheep.class, CloneableTesterTest.class);
    }

    /**
     * Test method for {@link CloneableTester#assertCloning(Package)}.
     */
    @Test
    public void testAssertCloningPackage() {
        assertThrows(AssertionError.class, () -> CloneableTester.assertCloning(Sheep.class.getPackage()));
    }

    /**
     * Test method for
     * {@link CloneableTester#assertCloning(Package, Pattern...)}.
     */
    @Test
    public void testAssertCloningPackagePattern() {
        CloneableTester.assertCloning(Sheep.class.getPackage(), Pattern.compile(".*Sheep"));
    }

    /**
     * If the tested class cannot be cloned because an
     * {@link InvocationTargetException} was thrown we want to see the cause for
     * this {@link InvocationTargetException} in the thrown
     * {@link AssertionError}. This is the unit test for <a
     * href="http://sourceforge.net/p/patterntesting/bugs/24/">bug 24</a>.
     */
    @Test
    public void testInvocationTargetException() {
        try {
            CloneableTester.assertCloning(CloneableTesterTest.class);
        } catch (AssertionError expected) {
            log.info("AssertionError expected", expected);
            assertEquals(UnsupportedOperationException.class, expected.getCause().getClass());
        }
    }

    /**
     * Because we implements Cloneable we should implement the clone method.
     *
     * @return the object
     * @throws CloneNotSupportedException the clone not supported exception
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new UnsupportedOperationException("bumm");
    }

}
