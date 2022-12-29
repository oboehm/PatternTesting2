/*
 * $Id: ArrayTester.java,v 1.8 2017/09/01 20:33:17 oboehm Exp $
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
 * (c)reated 27.08.2014 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import java.util.Arrays;

import org.slf4j.*;

import patterntesting.runtime.util.Converter;

/**
 * If you want to assert that the content of two arrays are equals use this
 * tester here.
 *
 * @author oliver
 * @since 1.5 (27.08.2014)
 */
public final class ArrayTester {

	private static final Logger LOG = LoggerFactory.getLogger(ArrayTester.class);

	/** Utility class - no need to instantiate it. */
	private ArrayTester() {
	}

	/**
	 * Checks if each object in array a1 is equals to that of array a2. If not
	 * this method will tell you which element is different. If the arrays have
	 * different size this method will tell you which element is missing.
	 * <p>
	 * Because two arrays are compared the order of the element is important.
	 * I.e. two arrays with the same elements but different order are not
	 * equals.
	 * </p>
	 *
	 * @param a1
	 *            the first array
	 * @param a2
	 *            the second array
	 */
	public static void assertEquals(final Object[] a1, final Object[] a2) {
		CollectionTester.assertEquals(Arrays.asList(a1), Arrays.asList(a2));
	}

	/**
	 * Checks if each object in array a1 is equals to that of array a2. If not
	 * this method will tell you which element is different. If the arrays have
	 * different size this method will tell you which element is missing.
	 * <p>
	 * Because two arrays are compared the order of the element is important.
	 * I.e. two arrays with the same elements but different order are not
	 * equals.
	 * </p>
	 *
	 * @param a1 the first array
	 * @param a2 the second array
	 */
	public static void assertEquals(final byte[] a1, final byte[] a2) {
		if (a1.length != a2.length) {
			LOG.debug("Arrays have different lengths ({} != {}).", a1.length, a2.length);
			throw new AssertionError("arrays have different lengths (" + a1.length + " != " + a2.length + ")");
		} else {
			for (int i = 0; i < a1.length; i++) {
				assertEquals(a1, a2, i);
			}
		}
	}

	private static void assertEquals(final byte[] a1, final byte[] a2, final int i) {
		if (a1[i] != a2[i]) {
			if (LOG.isTraceEnabled()) {
				LOG.trace("{}. element differs ({} != {}).", i + 1, Converter.toLongString(a1[i]),
						Converter.toLongString(a2[i]));
			} else if (LOG.isDebugEnabled()) {
				LOG.debug("{}. element differs ({} != {}).", i + 1, Converter.toString(a1[i]),
						Converter.toString(a2[i]));
			}
			throw new AssertionError((i + 1) + ". element differs (" + Converter.toShortString(a1[i]) + " != "
					+ Converter.toShortString(a2[i]) + ")");
		}
	}

}
