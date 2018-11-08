/*
 * $Id: StringTester.java,v 1.6 2016/12/18 20:19:38 oboehm Exp $
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
 * (c)reated 24.01.2012 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This tester provides some special assert methods for strings to simplify
 * JUnit testing.
 *
 * @author oboehm
 * @since 1.2.10-YEARS (24.01.2012)
 */
public final class StringTester {

	/** Utility class - no need to instantiate it. */
	private StringTester() {
	}

	/**
	 * Check if the given string is not null and not empty.
	 *
	 * @param s
	 *            the string
	 */
	public static void assertNotEmpty(final String s) {
		assertNotNull(s);
		if (s.length() == 0) {
			throw new AssertionError("String is empty");
		}
	}

	/**
	 * Check if the given string is null or empty. This method was introduced
	 * for symmetric reason to {@link #assertNotEmpty(String)}.
	 *
	 * @param s
	 *            the s
	 */
	public static void assertEmpty(final String s) {
		if (s != null) {
			assertEquals(0, s.length(), '"' + s + "\" is not empty");
		}
	}

}
