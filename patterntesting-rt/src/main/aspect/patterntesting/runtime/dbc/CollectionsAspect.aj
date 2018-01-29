/**
 * $Id: CollectionsAspect.aj,v 1.4 2016/12/18 20:19:36 oboehm Exp $
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
 * (c)reated 03.02.2009 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.dbc;

import static patterntesting.runtime.dbc.DbC.require;

import java.util.*;

import org.aspectj.lang.annotation.SuppressAjWarnings;

import patterntesting.runtime.util.Assertions;

/**
 * Some of the java.util.Collections calls, where a precondition can be
 * expressed, are wrapped using this aspect.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 0.9
 */
public aspect CollectionsAspect {

    /**
     * For a binary search the given list must be sorted. This is asserted
     * here.
     *
     * @param l the list which should be sorted
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @SuppressAjWarnings({"adviceDidNotMatch"})
    before(List l) : call(int java.util.Collections.binarySearch(List, Object))
            && !within(Collections)
            && args(l, *) {
        if (Assertions.ENABLED) {
            require(isSorted(l), "list is unsorted");
        }
    }

    /**
     * For a binary search the given list must be sorted. This is asserted
     * here.
     *
     * @param l the list which should be sorted
     * @param c the comparator
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @SuppressAjWarnings({"adviceDidNotMatch"})
    before(List l, Comparator c) : call(int java.util.Collections.binarySearch(List, Object, Comparator))
            && !within(Collections)
            && args(l, *, c) {
        if (Assertions.ENABLED) {
            require(isSorted(l, c), "list is unsorted");
        }
    }

    /**
     * Do you want to know if your list is sorted? Then you can use this
     * method.
     *
     * @param <T>  the type
     * @param list the list
     * @return true if given list is sorted
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean isSorted(
            List<? extends Comparable<? super T>> list) {
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

    /**
     * Do you want to know if your list is sorted? Then you can use this
     * method.
     *
     * @param <T> the type
     * @param list the list
     * @param c a single character
     * @return true if given list is sorted
     */
    public static <T> boolean isSorted(List<? extends T> list,
            Comparator<? super T> c) {
        if (list.isEmpty()) {
            return true;
        }
        Iterator<? extends T> iterator = list.iterator();
        T prev = iterator.next();
        while (iterator.hasNext()) {
            T next = iterator.next();
            if (c.compare(prev, next) > 0) {
                return false;
            }
            prev = next;
        }
        return true;
    }

}
