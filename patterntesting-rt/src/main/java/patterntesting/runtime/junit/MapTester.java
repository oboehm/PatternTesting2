/*
 * $Id: MapTester.java,v 1.9 2016/12/18 20:19:38 oboehm Exp $
 *
 * Copyright (c) 2013 by Oliver Boehm
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
 * (c)reated 18.12.2013 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.LoggerFactory;
import org.slf4j.*;

import patterntesting.runtime.util.Converter;

/**
 * This class asserts (among other things) that two maps are equals.
 *
 * @author oliver
 * @since 1.4 (18.12.2013)
 */
public class MapTester {

	private static final Logger LOG = LoggerFactory.getLogger(MapTester.class);

	/** Utility class - no need to instantiate it. */
	private MapTester() {
	}

	/**
	 * Checks if each key-value pair in map m1 is equals to that of map m2. If
	 * not this method will tell you which pair is different. If the maps have
	 * different size this method will tell you which pair is missing.
	 *
	 * @param m1
	 *            the m1
	 * @param m2
	 *            the m2
	 */
	@SuppressWarnings("unchecked")
	public static void assertEquals(final Map<?, ?> m1, final Map<?, ?> m2) {
		Map<Object, Object> map1 = (Map<Object, Object>) m1;
		if (m1.size() < m2.size()) {
			assertEquals(m2, m1);
		} else if (m1.size() > m2.size()) {
			for (Entry<Object, Object> entry : map1.entrySet()) {
				if (!(m2.containsKey(entry.getKey()))) {
					if (LOG.isTraceEnabled()) {
						LOG.trace("<{}> is missing in {}.", Converter.toLongString(entry), Converter.toLongString(m2));
					} else if (LOG.isDebugEnabled()) {
						LOG.debug("<{}> is missing in {}.", Converter.toString(entry), Converter.toString(m2));
					}
					throw new AssertionError("<" + entry + "> is missing in " + Converter.toShortString(m2));
				}
			}
		} else {
			assertEqualSizedMaps(m2, map1);
		}
	}

	private static void assertEqualSizedMaps(final Map<?, ?> m2, Map<Object, Object> m1) throws AssertionError {
		for (Entry<Object, Object> entry : m1.entrySet()) {
			Object value2 = m2.get(entry.getKey());
			if (entry.getValue() == null) {
				if (value2 != null) {
					LOG.debug("Key \"{}\" has different values: null <--> {}.", entry.getKey(),
							Converter.toString(value2));
					throw new AssertionError("key \"" + entry.getKey() + "\" has different values: null <--> "
							+ Converter.toShortString(value2));
				}
			} else if (value2 == null) {
				LOG.debug("{{}}\" is missing in {}.", entry, Converter.toString(m2));
				throw new AssertionError("{" + entry + "} is missing in " + Converter.toShortString(m2));
			} else if (!(value2.equals(entry.getValue()))) {
				LOG.debug("Key \"{}\" has different values: {} <--> {}.", entry.getKey(),
						Converter.toString(entry.getValue()), Converter.toString(value2));
				throw new AssertionError("key \"" + entry.getKey() + "\" has different values: "
						+ Converter.toShortString(entry.getValue()) + " <--> " + Converter.toShortString(value2));
			}
		}
	}

	/**
	 * Checks if each key in map m1 is equals to that of map m2. If not this
	 * method will tell you which key is different. If the maps have different
	 * size this method will tell you which key is missing.
	 *
	 * @param m1
	 *            the m1
	 * @param m2
	 *            the m2
	 */
	public static void assertEqualKeys(final Map<?, ?> m1, final Map<?, ?> m2) {
		CollectionTester.assertEquals(m1.keySet(), m2.keySet(), "keys differs");
	}

	/**
	 * Checks if each value in map m1 is equals to that of map m2. If not this
	 * method will tell you which map is different. If the maps have different
	 * size this method will tell you which value is missing.
	 *
	 * @param m1
	 *            the m1
	 * @param m2
	 *            the m2
	 */
	public static void assertEqualValues(final Map<?, ?> m1, final Map<?, ?> m2) {
		CollectionTester.assertEquals(m1.values(), m2.values(), "values differs");
	}

}
