/*
 * $Id: Address.java,v 1.5 2016/12/10 20:55:19 oboehm Exp $
 *
 * Copyright (c) 2014 by Oliver Boehm
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
 * (c)reated 14.01.2014 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.log.test;

/**
 * This class has some logic inside to test the generation of sequence
 * diagrams.
 *
 * @author oliver
 * @since 1.4.1 (14.01.2014)
 */
public final class Address implements Comparable<Address> {

    private final String name;
    private City city;

    /**
     * Instantiates a new address.
     *
     * @param name the name
     */
    public Address(final String name) {
        this(name, new City("somewhere"));
    }

    /**
     * Instantiates a new address.
     *
     * @param name the name
     * @param city the city
     */
    public Address(final String name, final City city) {
        this.name = name;
        this.city = city;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the city.
     *
     * @return the city
     */
    public City getCity() {
        return city;
    }

    /**
     * Sets the city.
     *
     * @param cityName the new city
     */
    public void setCity(final String cityName) {
        this.city = new City(cityName);
    }

    /**
     * Compare to.
     *
     * @param other the other
     * @return the int
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
	public int compareTo(final Address other) {
        if (this.name.equalsIgnoreCase(other.name)) {
            return this.city.compareTo(other.city);
        } else {
            return this.name.compareToIgnoreCase(other.name);
        }
    }

    /**
     * Equals.
     *
     * @param obj the obj
     * @return true, if successful
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Address)) {
            return false;
        }
        Address other = (Address) obj;
        return this.compareTo(other) == 0;
    }

    /**
     * Hash code.
     *
     * @return the int
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    /**
     * To string.
     *
     * @return the string
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.name;
    }

}

