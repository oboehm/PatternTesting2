/*
 * $Id: Sheep.java,v 1.8 2016/12/18 20:19:41 oboehm Exp $
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
 * (c)reated 22.03.2011 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit.test;

import patterntesting.runtime.junit.CloneableTester;
import patterntesting.runtime.junit.ComparableTester;

/**
 * This is demo and test class for the Cloneable interface and for the
 * {@link CloneableTester}.
 * Normally it should overwrite the {@link Object#clone()} with a public
 * method.
 * And since 1.2 it is als a demo classe for the {@link Comparable} interface
 * to be able to test the {@link ComparableTester} class.
 *
 * @author oliver
 * @since 1.1 (22.03.2011)
 */
public class Sheep implements Cloneable, Comparable<Sheep> {

    private final String name;

    /**
     * Instantiates a new sheep.
     */
    public Sheep() {
        this.name = "";
    }

    /**
     * Instantiates a new sheep.
     *
     * @param name the name
     */
    public Sheep(final String name) {
        this.name = name;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    protected String getName() {
        return name;
    }

    /**
     * According {@link Object#clone()} this method should be public.
     * Because it is not the CloneableTester should report an error for it.
     *
     * @return the object
     * @throws CloneNotSupportedException the clone not supported exception
     * @see java.lang.Object#clone()
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * This is a (wrong) implementation of the {@link Comparable#compareTo(Object)}
     * method. It was generated as default implementation by Eclipse
     *
     * @param other the other sheep
     * @return 0 (always)
     */
    @Override
	public int compareTo(Sheep other) {
        return 0;
    }

    /**
     * This is only an example for a static method.
     *
     * @return the string
     */
    public static String baa() {
        return "meep";
    }

}

