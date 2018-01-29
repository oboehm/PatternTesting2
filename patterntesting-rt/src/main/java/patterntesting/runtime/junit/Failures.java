/*
 * $Id: Failures.java,v 1.7 2016/12/18 20:19:38 oboehm Exp $
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

package patterntesting.runtime.junit;

import java.util.HashMap;
import java.util.Map;

import patterntesting.runtime.util.Converter;

/**
 * For the ObjectTester and other tester classes we need a chance to collect the
 * thrown Assertions and throw it at the end of a combined or complex check.
 *
 * @author oliver
 * @since 1.0.3 (13.09.2010)
 */
public final class Failures extends AssertionError {

	private static final long serialVersionUID = 20100913L;
	private final Map<Class<?>, AssertionError> errors = new HashMap<>();

	/**
	 * Adds the given class and error to the internal error colllection.
	 *
	 * @param clazz
	 *            the clazz
	 * @param error
	 *            the error
	 */
	public void add(final Class<?> clazz, final AssertionError error) {
		errors.put(clazz, error);
	}

	/**
	 * Gets the errors.
	 *
	 * @return the errors
	 */
	public Map<Class<?>, AssertionError> getErrors() {
		return errors;
	}

	/**
	 * Checks for errors.
	 *
	 * @return true, if successful
	 */
	public boolean hasErrors() {
		return !this.errors.isEmpty();
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return Converter.toString(this.errors.keySet()) + " failed";
	}

}
