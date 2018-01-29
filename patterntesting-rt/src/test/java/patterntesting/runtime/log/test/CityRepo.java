/*
 * $Id: CityRepo.java,v 1.4 2016/12/18 20:19:38 oboehm Exp $
 *
 * Copyright (c) 2015 by Oliver Boehm
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
 * (c)reated 02.06.2015 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.log.test;

import java.util.ArrayList;
import java.util.Collection;

import patterntesting.runtime.exception.NotFoundException;

/**
 * A simple repository to store cities. This class is abstract because
 * it provides only static methods.
 *
 * @author oliver
 * @version $Revision: 1.4 $
 * @since 1.6 (02.06.2015)
 */
public abstract class CityRepo {

    private static Collection<City> cities = new ArrayList<>();

    /** No need to instantiate it. */
    private CityRepo() {
    }

    /**
     * Adds the city.
     *
     * @param city the city
     */
    public static void addCity(final City city) {
        cities.add(city);
    }

    /**
     * Gets the city.
     *
     * @param name the name
     * @return the city
     */
    public static City getCity(final String name) {
        for (City city : cities) {
            if (name.equals(city.getName())) {
                return city;
            }
        }
        throw new NotFoundException(name);
    }

}

