/*
 * $Id: Dummy.java,v 1.7 2016/12/18 20:19:39 oboehm Exp $
 *
 * Copyright (c) 2009 by Oliver Boehm
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
 * (c)reated 02.04.2009 by oliver (ob@aosd.de)
 */
package patterntesting.runtime.monitor;

import patterntesting.runtime.annotation.DontProfileMe;
import patterntesting.runtime.annotation.ProfileMe;

/**
 * This is only a dummy class for profiling.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.7 $
 * @since 02.04.2009
 */
@ProfileMe
public final class Dummy {

    /**
     * Instantiates a new dummy.
     */
    public Dummy() {
        throw new RuntimeException("I should not be called - also not for testing");
    }

    /**
     * Hello.
     *
     * @return the string
     */
    public static String hello() {
        return "hello";
    }

    /**
     * This is an example for a method which is never called. You should
     * see it in the profile statistic with 0 hits!
     */
    public static void dontCallMe() {
        throw new RuntimeException("don't call me!");
    }

    /**
     * This is method which is called but should not appear in the profile
     * statistic.
     *
     * @return 42 (always - remember, this class is called "Dummy")
     */
    @DontProfileMe
    public static int dontProfileMe() {
        return 42;
    }

    /**
     * This is an exmple for a method which is never called but (in
     * contradiction to "dontCallMe()") should not appear in the profile
     * statistic.
     */
    @DontProfileMe
    public void neverCalled() {
        throw new RuntimeException("I should be never called!");
    }

    /**
     * All Dummy objects are considered as equals.
     *
     * @param other the other dummy object
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
     * Because all Dummy objects are equals they must return all the same
     * hashCode. Otherwise the ObjectTesterTest will fail!
     *
     * @return always the same hashCode
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return 4711;
    }

}
