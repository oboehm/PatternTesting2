/*
 * Copyright (c) 2008-2019 by Oliver Boehm
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
 * (c)reated 01.02.2009 by oliver (ob@oasd.de)
 */
package patterntesting.sample.dbc;

import static patterntesting.runtime.dbc.DbC.*;

import java.util.*;

/**
 * This is an example how the Collections utilties can be implemented using
 * DbC.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @see java.util.Collections
 * @since 0.9 (01.02.2009)
 */
public class Collections {
    
    /** Utility class - no need to instantiate it. */
    private Collections() {}

    /**
     * Binary search.
     *
     * @param <T> the generic type
     * @param list the list
     * @param key the key
     * @return the int
     */
    public static <T> int binarySearch(
            final List<? extends Comparable<? super T>> list, final T key) {
        require(isSorted(list), "list must be sorted");
        assert isSorted(list) : "list must be sorted";
        int found = java.util.Collections.binarySearch(list, key);
        ensure(found < list.size(), "found index out of bound");
        ensure(found >= -list.size(), "insertion point out of bound");
        return found;
    }

    /**
     * Checks if is sorted.
     *
     * @param <T> the generic type
     * @param list the list
     * @return true, if is sorted
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean isSorted(
            final List<? extends Comparable<? super T>> list) {
        if (list.isEmpty()) {
            return true;
        }
        Iterator<? extends Comparable<? super T>> iterator = list.iterator();
        Comparable<? super T> prev = iterator.next();
        while(iterator.hasNext()) {
            Comparable<? super T> next = iterator.next();
            if (prev.compareTo((T) next) > 0) {
                return false;
            }
            prev = next;
        }
        return true;
    }

}
