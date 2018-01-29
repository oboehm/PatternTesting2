/*
 * $Id: AbstractIdentifiable.java,v 1.7 2016/12/10 20:55:22 oboehm Exp $
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
 * This is an example how an identifiable class can be implemented.
 *
 * @author oliver
 * @since 1.0.3 (13.09.2010)
 */
public abstract class AbstractIdentifiable implements Identifiable,
        Comparable<AbstractIdentifiable> {

    private static final long serialVersionUID = 20100913L;
    private static Long lastId = 0L;
    private final long id;

    /**
     * Instantiates a new abstract identifiable.
     */
    public AbstractIdentifiable() {
        this.id = getNextId();
    }

    private static synchronized long getNextId() {
        long nextId = System.currentTimeMillis();
        if (nextId == lastId) {
            nextId++;
        }
        lastId = nextId;
        return nextId;
    }

    /**
     * Gets the id.
     *
     * @return the id
     * @see patterntesting.runtime.junit.test.Identifiable#getId()
     */
    @Override
	public long getId() {
        return this.id;
    }

    /**
     * If two Idenfiables has the same id a 0 is returned.
     *
     * @param other the other
     * @return 0 if the other object has the same id.
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
	public int compareTo(AbstractIdentifiable other) {
        return (int) (this.getId() - other.getId());
    }

   /**
     * Equals.
     *
     * @param other the other
     * @return true, if successful
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object other) {
        if (other == null) {
            return false;
        }
        try {
            return this.equals((AbstractIdentifiable) other);
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
    public boolean equals(final AbstractIdentifiable other) {
        return this.id == other.id;
    }

    /**
     * Hash code.
     *
     * @return the int
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (int) this.id;
    }

    /**
     * To string.
     *
     * @return the string
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + id + "]";
    }

}

