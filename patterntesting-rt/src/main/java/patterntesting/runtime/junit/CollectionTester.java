/*
 * $Id: CollectionTester.java,v 1.12 2016/12/18 20:19:38 oboehm Exp $
 *
 * Copyright (c) 2012 by Oliver Boehm
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
 * (c)reated 13.01.2012 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patterntesting.runtime.util.Converter;

import java.util.Collection;
import java.util.List;

/**
 * If you want to assert that the content of two collections are equals use this
 * tester here.
 *
 * @author oliver (ob@aosd.de)
 * @since 1.2 (13.01.2012)
 */
public final class CollectionTester {

	private static final Logger LOG = LogManager.getLogger(CollectionTester.class);

	/** Utility class - no need to instantiate it. */
	private CollectionTester() {
	}

	/**
	 * Checks if each object in collection c1 is equals to that of Collection
	 * c2. If not this method will tell you which element is different. If the
	 * collection have different size this method will tell you which element is
	 * missing.
	 * <p>
	 * The order of the elements is not relevant. I.e. two collections with
	 * different ordering of its elements are equals if they have both the same
	 * elements (independent from the internal ordering).
	 * </p>
	 * <p>
	 * Note: Use Lists if ordering is important.
	 * </p>
	 *
	 * @param c1
	 *            the c1
	 * @param c2
	 *            the c2
	 */
	public static void assertEquals(final Collection<?> c1, final Collection<?> c2) {
		assertEquals(c1, c2, "collections differs");
	}

	/**
	 * Assert equals. This method is not for public use but is used by
	 * MapTester. This is the reason why this method is 'protected'.
	 *
	 * @param c1
	 *            the c1
	 * @param c2
	 *            the c2
	 * @param msg
	 *            the msg
	 */
	protected static void assertEquals(final Collection<?> c1, final Collection<?> c2, final String msg) {
		if (c1.size() < c2.size()) {
			assertEquals(c2, c1);
		} else {
			assertEqualsSize(c1, c2);
			for (Object elem1 : c1) {
				if (!c2.contains(elem1)) {
					logMissingElement(elem1, c2, msg);
					throw new AssertionError(msg + ": [" + Converter.toString(elem1) + "] is not in "
							+ Converter.toString(c2));
				}
			}
		}
	}

	private static void logMissingElement(Object element, final Collection<?> collection, final String msg) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("{}: [{}] is not in {}.", msg, Converter.toLongString(element),
					Converter.toLongString(collection));
		} else if (LOG.isDebugEnabled()) {
			LOG.debug("{}: [{}] is not in {}.", msg, Converter.toString(element), Converter.toString(collection));
		}
	}

	/**
	 * Checks if each object in list l1 is equals to that of list l2. If not
	 * this method will tell you which element is different. If the collection
	 * have different size this method will tell you which element is missing.
	 * <p>
	 * Because two lists are compared the order of the element is important.
	 * I.e. two lists with the same elements but different order are not equals.
	 * </p>
	 *
	 * @param l1
	 *            the list 1
	 * @param l2
	 *            the list 2
	 */
	public static void assertEquals(final List<?> l1, final List<?> l2) {
		if (l1.size() < l2.size()) {
			assertEquals(l2, l1);
		} else {
			assertEqualsSize(l1, l2);
			for (int i = 0; i < l1.size(); i++) {
				Object elem1 = l1.get(i);
				Object elem2 = l2.get(i);
				if (!elem1.equals(elem2)) {
					logDifferentElements(i, elem1, elem2);
					throw new AssertionError((i + 1) + ". element differs (" + Converter.toShortString(elem1) + " != "
							+ Converter.toShortString(elem2) + ")");
				}
			}
		}
	}

	private static void logDifferentElements(int i, Object elem1, Object elem2) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("{}. element differs ({} != {}).", i + 1, Converter.toLongString(elem1),
					Converter.toLongString(elem2));
		} else if (LOG.isDebugEnabled()) {
			LOG.debug("{}. element differs ({} != {}).", i + 1, Converter.toString(elem1), Converter.toString(elem2));
		}
	}

	private static void assertEqualsSize(Collection<?> c1, Collection<?> c2) throws AssertionError {
		if (c1.size() > c2.size()) {
			for (Object element : c1) {
				if (!c2.contains(element)) {
					logMissingElement(element, c2);
					throw new AssertionError("\"" + Converter.toString(element)
							+ "\" is missing in one collection (size: " + c1.size() + " / " + c2.size() + " elements)");
				}
			}
		}
	}

	private static void logMissingElement(Object element, Collection<?> collection) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("Element \"{}\" is missing in {}.", Converter.toLongString(element),
					Converter.toLongString(collection));
		} else if (LOG.isDebugEnabled()) {
			LOG.debug("Element \"{}\" is missing in {}.", Converter.toString(element), Converter.toLongString(collection));
		} else {
			LOG.info("Element \"{}\" is missing in {}.", Converter.toString(element), Converter.toString(collection));
		}
	}

}
