/*
 * Copyright (c) 2013 by Oli B.
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
 * (c)reated 29.11.2013 by Oli B. (boehm@javatux.de)
 */

package patterntesting.runtime.util;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.slf4j.LoggerFactory;
import org.slf4j.*;

/**
 * This utility class is intended to check arguments. It is like the Assert
 * class in Springframework and throws an {@link IllegalArgumentException} if
 * the argument is not valid.
 * <p>
 * This class is abstract to avoid that it is instantiated. There are only
 * static methods so there is no need to instantiate it. This is the same way
 * like the Springframework does it.
 * </p>
 * <p>
 * For more information about the use of bean validation have a look at <a href=
 * "http://techpatches.blogspot.de/2013/11/bean-validation-hibernate-validator.html"
 * >Bean Validation with Hibernate Validator framework</a>
 * </p>
 * .
 *
 * @author oliver (boehm@javatux.de)
 * @since 1.4 (29.11.2013)
 */
public abstract class AssertArg {

	private static final Logger LOG = LoggerFactory.getLogger(AssertArg.class);

	/** Utility class - no need to instantiate it. */
	private AssertArg() {
	}

	/**
	 * Checks if the argument is valid. If not (or if is null) an
	 * {@link IllegalArgumentException} will be thrown.
	 *
	 * @param validatable
	 *            the validatable argument
	 */
	public static void isValid(final Validator validatable) {
		isValid(validatable, validatable);
	}

	/**
	 * Checks if the argument is valid. If not (or if is null) an
	 * {@link IllegalArgumentException} will be thrown.
	 *
	 * @param argument
	 *            the argument
	 * @param validator
	 *            the validator
	 */
	public static void isValid(final Object argument, final Validator validator) {
		if (argument == null) {
			throw new IllegalArgumentException("argument is null");
		}
		if (validator == null) {
			LOG.debug("No validator given to validate {}.", argument);
		} else {
			Set<ConstraintViolation<Object>> violations = validator.validate(argument);
			if ((violations != null) && (!violations.isEmpty())) {
				throw new IllegalArgumentException(argument + " is invalid: " + violations);
			}
		}
	}

}
