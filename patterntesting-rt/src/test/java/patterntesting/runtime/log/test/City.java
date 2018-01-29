/*
 * $Id: City.java,v 1.9 2016/12/10 20:55:19 oboehm Exp $
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

import org.apache.commons.lang3.ObjectUtils;

import patterntesting.runtime.annotation.DrawSequenceDiagram;

/**
 * Just another class which will be used to test the generation of seuence
 * diagrams.
 *
 * @author oliver
 * @since 1.4.1 (14.01.2014)
 */
public final class City implements Comparable<City> {

    private final int zipCode;
    private final String name;

    /**
     * Instantiates a new city.
     *
     * @param name the name
     */
    public City(final String name) {
        this(0, name);
    }

    /**
     * Instantiates a new city.
     *
     * @param zip the zip
     * @param name the name
     */
    public City(final int zip, final String name) {
        this.zipCode = zip;
        this.name = name;
    }

    /**
     * Gets the zip code.
     * <p>
     * NOTE: Normally a string would be a better representation for a zip code
     * because in some country (as e.g. in Germany) can also start with a
     * leading '0'. And probably it can also contains characters. But this
     * class is only an example!
     * </p>
     *
     * @return the zipCode
     */
    public int getZipCode() {
        return zipCode;
    }

    /**
     * The zip code is a kind of an id. Because we want to see the "id" in
     * the generated sequence diagram we provide this method here.
     *
     * @return the zip code
     */
    public int getId() {
        return this.getZipCode();
    }

    /**
     * Checks for valid zip code.
     *
     * @return true, if successful
     */
    public boolean hasValidZipCode() {
        return this.zipCode > 0;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    @DrawSequenceDiagram
    public String getName() {
        return name;
    }

    /**
     * Compare to.
     *
     * @param other the other
     * @return the int
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
	@SuppressWarnings("deprecation")
    public int compareTo(final City other) {
        if (this.hasValidZipCode() && other.hasValidZipCode()) {
            return this.zipCode - other.zipCode;
        } else {
            return ObjectUtils.toString(this.name).compareToIgnoreCase(ObjectUtils.toString(other.name));
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
        if (!(obj instanceof City)) {
            return false;
        }
        City other = (City) obj;
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
        return this.zipCode;
    }

    /**
     * To string.
     *
     * @return the string
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (this.hasValidZipCode()) {
            return this.zipCode + " " + this.name;
        } else {
            return this.name;
        }
    }

}

