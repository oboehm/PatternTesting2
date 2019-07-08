/*
 * $Id: IdGenerator.java,v 1.3 2016/01/06 20:47:37 oboehm Exp $
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
 * (c)reated 29.09.2008 by oliver (ob@oasd.de)
 */

package patterntesting.sample;

/**
 * The Class IdGenerator.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.3 $
 * @since 29.09.2008
 */
public final class IdGenerator {

    /** The instance. */
    private static volatile IdGenerator instance;
    private int nextId;

    /**
     * This is a very expensive constructor ;-) It should be called only once
     * by getInstance().
     *
     * @see #getInstance()
     */
    protected IdGenerator() {
        nextId = 1;
    }

    /**
     * Gets the single instance of IdGenerator.
     *
     * @return single instance of IdGenerator
     */
    public static IdGenerator getInstance() {
        if (instance == null) {
            instance = new IdGenerator();
        }
        return instance;
    }

    /**
     * The next id is the old one + 1. And this method must be synchronized
     * otherwise you risc a lost update.
     *
     * @return the next id
     */
    public synchronized int getNextId() {
        int n = this.nextId;
        n++;
        this.nextId = n;
        return this.nextId;
    }

}
