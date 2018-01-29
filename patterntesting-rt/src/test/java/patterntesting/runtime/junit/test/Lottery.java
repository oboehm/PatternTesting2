/*
 * $Id: Lottery.java,v 1.8 2016/12/10 20:55:22 oboehm Exp $
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
 * (c)reated 13.09.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit.test;

import java.util.*;

/**
 * This is a simple example for a (German) lottery with an overwritten equals
 * method.
 *
 * @author oliver
 * @since 1.0.3 (13.09.2010)
 */
public class Lottery implements Gamble {

    /** The numbers. */
    protected final Integer[] numbers = new Integer[6];

    private static final Random random = new Random(System.currentTimeMillis());
    private final List<Integer> wheel = new ArrayList<>(49);

    /**
     * Instantiates a new lottery.
     */
    public Lottery() {
        initWheel();
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = drawNumber();
        }
    }

    private void initWheel() {
        for (int i = 1; i <= 49; i++) {
            wheel.add(Integer.valueOf(i));
        }
    }

    private Integer drawNumber() {
        int chance = random.nextInt(wheel.size());
        Integer ball = wheel.get(chance);
        wheel.remove(chance);
        return ball;
    }

    /**
     * Gets the numbers.
     *
     * @return the numbers
     */
    public Integer[] getNumbers() {
        return this.numbers;
    }

    /**
     * This equals method does not check for 'null' argument which is a common
     * anti pattern.
     *
     * @param other the other
     * @return true, if successful
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object other) {
        try {
            return equals((Lottery) other);
        } catch (ClassCastException cce) {
            return false;
        }
    }

    /**
     * Equals.
     *
     * @param other the other
     * @return true, if successful
     */
    public boolean equals(final Lottery other) {
        for (int i = 0; i < numbers.length; i++) {
            if (!this.numbers[i].equals(other.numbers[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Hash code.
     *
     * @return the int
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int n = 0;
        for (int i = 0; i < numbers.length; i++) {
            n = n * 49 + numbers[i];
        }
        return n;
    }

    /**
     * To string.
     *
     * @return the string
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Lottery " + Arrays.toString(numbers);
    }

}

