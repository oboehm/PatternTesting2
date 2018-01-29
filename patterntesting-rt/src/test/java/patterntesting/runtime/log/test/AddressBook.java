/*
 * $Id: AddressBook.java,v 1.3 2016/12/10 20:55:19 oboehm Exp $
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

import java.util.*;

import patterntesting.runtime.annotation.DrawSequenceDiagram;

/**
 * Together with the {@link Address} class this class is intented for testing
 * the generation of sequence diagrams in SequenceDiagramTest.
 *
 * @author oliver
 * @since 1.4.1 (14.01.2014)
 */
public final class AddressBook {

    private final SortedSet<Address> addresses = new TreeSet<>();

    /**
     * Gets the addresses.
     *
     * @return the addresses
     */
    public Collection<Address> getAddresses() {
        return this.addresses;
    }

    /**
     * Adds the address.
     *
     * @param address the address
     */
    @DrawSequenceDiagram
    public void addAddress(final Address address) {
        addresses.add(address);
    }

    /**
     * Adds the addresses.
     *
     * @param newAddresses the new addresses
     */
    public void addAddresses(final Collection<Address> newAddresses) {
        for (Address address : newAddresses) {
            this.addAddress(address);
        }
    }

    /**
     * Adds the addresses form.
     *
     * @param other the other
     */
    public void addAddressesForm(final AddressBook other) {
        this.addAddresses(other.getAddresses());
    }

    /**
     * Gets the addresses of.
     *
     * @param city the city
     * @return the addresses of
     */
    public SortedSet<Address> getAddressesOf(final City city) {
        SortedSet<Address> result = new TreeSet<>();
        for (Address address : addresses) {
            if (city.equals(address.getCity())) {
                result.add(address);
            }
        }
        return result;
    }

    /**
     * To string.
     *
     * @return the string
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " with " + this.addresses.size() + " addresses";
    }

}

