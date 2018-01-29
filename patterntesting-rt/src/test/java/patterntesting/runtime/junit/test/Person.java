/*
 * $Id: Person.java,v 1.4 2017/07/18 06:30:31 oboehm Exp $
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

/**
 * This is an example for an identifiable person.
 *
 * @author oliver
 * @since 1.0.3 (13.09.2010)
 */
public final class Person extends AbstractIdentifiable {

    private static final long serialVersionUID = 20100913L;
    private final String name;
    private transient final int nameLength;

    /**
     * Instantiates a new person.
     */
    public Person() {
        this("nobody");
    }

    /**
     * Instantiates a new person.
     *
     * @param name the name
     */
    public Person(final String name) {
        super();
        this.name = name;
        this.nameLength = name.length();
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the length of the name. It is used as example for a problem
     * during serialization and deserialization of objects. Because the
     * 'nameLength' is transient, it is not initialized after deserialization.
     * 
     * @return the name length
     */
    public int getNameLength() {
        return this.nameLength;
    }

}

