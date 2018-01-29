/*
 * $Id: TypePattern.java,v 1.2 2016/12/10 20:55:24 oboehm Exp $
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
 * (c)reated 12.01.2014 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.util.regex;

import java.util.regex.Pattern;

/**
 * A type pattern which matches patterns like "java..*" (all tpyes of the JDK).
 * These are the same patterns as AspectJ allows.
 *
 * @author oliver
 * @since 1.4.1 (12.01.2014)
 */
public class TypePattern {

	private final Pattern pattern;

	/**
	 * Instantiates a new type pattern.
	 *
	 * @param pattern
	 *            the pattern
	 */
	public TypePattern(final String pattern) {
		this.pattern = Pattern.compile(pattern);
	}

	/**
	 * Matches.
	 *
	 * @param object
	 *            the object
	 * @return true, if successful
	 */
	public boolean matches(final Object object) {
		if (object == null) {
			return false;
		}
		return matches(object.getClass());
	}

	/**
	 * Matches.
	 *
	 * @param clazz
	 *            the clazz
	 * @return true, if successful
	 */
	public boolean matches(final Class<?> clazz) {
		if (clazz == null) {
			return false;
		}
		return matches(clazz.getName());
	}

	/**
	 * Matches.
	 *
	 * @param classname
	 *            the classname
	 * @return true, if successful
	 */
	public boolean matches(final String classname) {
		if (classname == null) {
			return false;
		}
		return this.pattern.matcher(classname).matches();
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.pattern + ")";
	}

}
