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
 * (c)reated 19.10.2008 by oliver (ob@oasd.de)
 */
package patterntesting.sample;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import patterntesting.annotation.check.ct.SuppressJUnitWarning;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is an example for the annotations "@UnsupportedOperation" and
 * and "@SuppressJUnitWarning". And it shows you that it is normally no good
 * idea to handle exceptions in a JUnit test.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.6 $
 * @since 19.10.2008
 */
public class FractionTest {

	private static final Logger log = LogManager.getLogger(FractionTest.class);

    /**
     * Test method for {@link Fraction#compareTo(Fraction)}.
     */
    @Test
    @SuppressJUnitWarning
    public final void testCompareToEquals() {
        Fraction oneHalf = new Fraction(1, 2);
        Fraction twoQuarters = new Fraction(2, 4);
        Fraction one = new Fraction(1, 1);
        try {
	        assertEquals(0, oneHalf.compareTo(twoQuarters));
            assertTrue(oneHalf.compareTo(one) < 0, "wrong result for " + oneHalf + " < " + one);
        } catch (UnsupportedOperationException mayhappen) {
        	log.info("Implementation of compareTo() seems not to be finished",
        			mayhappen);
        	log.info("We except this exception here because it is only an example.");
        }
    }

    /**
     * This is an example how you should not write a JUnit test.
     * Don't catch the exception here.
     */
    //@Test     // if you uncomment this you'll see the warning
    public final void testIllegalConstructionWrong() {
        try {
            new Fraction(1, 0);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            log.info(expected.getLocalizedMessage());
        }
    }

    /**
     * Test illegal construction.
     */
    @Test
    public final void testIllegalConstruction() {
        assertThrows(IllegalArgumentException.class, () -> new Fraction(1,0));
    }

    /**
     * Test illegal argument exception.
     */
    @Test
    @SuppressJUnitWarning
    public final void testIllegalArgumentException() {
        try {
            new Fraction(1, 0);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            assertEquals("illegal fraction x/0", expected.getMessage());
        }
    }

    /**
     * This is an example. It shows you that it is better to use assertEqual
     * instead of the assertTrue method.
     * <p>
     * Probably it would be better to use the compareTo() method here.
     * But it is only an example.
     * </p>
     */
    @Test
    public final void testToBigDecimal() {
        Fraction fraction = new Fraction(5, 2);
        BigDecimal expected = BigDecimal.valueOf(25L, 1);
        //assertTrue(expected.equals(fraction.toBigDecimal()));
        assertEquals(expected, fraction.toBigDecimal());
    }

    /**
     * Because toDouble() is marked as "@UnsupportedOperation" we expect
     * an UnsupportedOperationException here now.
     */
    @Test
    public final void testToDouble() {
        assertThrows(UnsupportedOperationException.class, () -> {
            Fraction fraction = new Fraction(1, 2);
            assertEquals(0.5, fraction.toDouble(), 0.001);
        });
    }

    /**
     * This is an example how to use assertTrue(..) - use the variant with
     * the String parameter.
     */
    @Test
    public final void testIsNegative() {
        Fraction fraction = new Fraction(-1, 10);
        assertTrue(fraction.isNegative(), fraction + ".isNegative()");
    }

}
