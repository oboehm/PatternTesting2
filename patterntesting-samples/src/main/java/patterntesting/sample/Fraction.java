/*
 * $Id: Fraction.java,v 1.6 2016/12/21 22:09:02 oboehm Exp $
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
 * (c)reated 19.10.2008 by oliver (ob@oasd.de)
 */
package patterntesting.sample;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patterntesting.runtime.annotation.NotYetImplemented;
import patterntesting.runtime.annotation.UnsupportedOperation;

import java.math.BigDecimal;

/**
 * This is a litte demo class to show how to use the @UnsupportedOperation
 * annotation.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 19.10.2008
 */
public final class Fraction implements Comparable<Fraction> {

    private static final Logger log = LogManager.getLogger(Fraction.class);
    private final int counter;
    private final int denominator;

    /**
     * Instantiates a new fraction.
     *
     * @param counter the counter
     * @param denominator the denominator
     */
    public Fraction(final int counter, final int denominator) {
        this.counter = counter;
        if (denominator == 0) {
            throw new IllegalArgumentException("illegal fraction x/0");
        }
        this.denominator = denominator;
    }

    /**
     * Checks if is negative.
     *
     * @return true, if is negative
     */
    public boolean isNegative() {
        return this.toBigDecimal().compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * This is an example of an automatically generated method by Eclipse.
     * If you extends this class by the Comparable interface but haven't
     * implement the compareTo method you can ask Eclipse to do that by
     * the quick fix "Add unimplemented methods".
     * <p>
     * The problem with such generated default implementation is that
     * the result may look ok but you can't rely on it. For this reason you
     * should throw an RuntimeException or something else if you have not
     * implement it. This is where the @NotYetImplemented annotation
     * comes in.
     * </p>
     *
     * @param o the o
     *
     * @return the int
     */
    @NotYetImplemented
    public int compareTo(final Fraction o) {
        // Auto-generated method stub
        return 0;
    }

    /**
     * Because we have implemented (or want to implement) the
     * compareTo(..) method we should overwrite the equals method.
     *
     * @param other the other object
     * @return true if the objects are equal
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object other) {
        if (other instanceof Fraction) {
            return compareTo((Fraction) other) == 0;
        } else {
            log.debug("can't compare " + this + " with " + other);
            return false;
        }
    }

    /**
     * Because we overwrite the equals() method we have also to overwrite
     * the hashCode() implementation.
     *
     * @return the hash code
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    /**
     * This is a very simple implementation for toBigDecimal().
     *
     * @return the fraction as decimal
     */
    public BigDecimal toBigDecimal() {
        return BigDecimal.valueOf(this.counter / (double) this.denominator);
    }

    /**
     * Because of rounding problems this method should be no longer used.
     * So it is now marked as @UnsupportedOperation.
     * <p>
     * Normally I would mark it also as "@Deprecated". But since it is only
     * an example I abstain from it.
     * </p>
     *
     * @return the double
     */
    @UnsupportedOperation
    public double toDouble() {
        return this.counter / (double) this.denominator;
    }

	/**
	 * To string.
	 *
	 * @return the string
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		return this.counter + "/" + this.denominator;
	}

}
