/*
 * Copyright (c) 2015 by Oli B.
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
 * (c)reated 03.08.2015 by Oli B. (boehm@javatux.de)
 */

package patterntesting.runtime.util;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

/**
 * This is the base class for all converter classes which transform a string
 * into another string. This can be a converter ignoring white spaces or a
 * converter transforming all strings to upper case.
 * <p>
 * This class can be uses as addtional parameter for the FileTester or IOTester
 * class in the junit package.
 * </p>
 *
 * @author oliver (boehm@javatux.de)
 * @since 1.6 (03.08.2015)
 */
public abstract class StringConverter {

	/** This converter echoes only the input string. */
	public static final StringConverter ECHO = new EchoConverter();

	/** Use this converter if you want to convert the string into upper case. */
	public static final StringConverter UPPER_CASE = new UpperCaseConverter();

	/** Use this converter if you want to convert the string into lower case. */
	public static final StringConverter LOWER_CASE = new LowerCaseConverter();

	/** Use this converter if you want to ignore white spaces. */
	public static final StringConverter IGNORE_WHITESPACES = new WhitespaceIgnorer();

	/**
	 * This is the method for conversion.
	 *
	 * @param input
	 *            the input
	 * @return the string
	 */
	public abstract String convert(final String input);

	/**
	 * With this method you can combine several converters.
	 *
	 * @param others
	 *            the other converters
	 * @return the string converter
	 */
	public final StringConverter and(final StringConverter... others) {
		return new AndConverter(this, others);
	}

	///// DIFFERENT TYPES OF CONVERTS /////////////////////////////////////

	/**
	 * A simple converter.
	 */
	private static class EchoConverter extends StringConverter {
		/**
		 * Echos only the input and does no conversion.
		 *
		 * @param input
		 *            the input
		 * @return the input in upper case
		 * @see StringConverter#convert(java.lang.String)
		 */
		@Override
		public String convert(final String input) {
			return input;
		}
	}

	/**
	 * The AndConverter combines different converters.
	 */
	private static class AndConverter extends StringConverter {
		private final List<StringConverter> converters = new ArrayList<>();

		/**
		 * Instantiates a new and converter and collects the given converters.
		 *
		 * @param first
		 *            the first
		 * @param others
		 *            the others
		 */
		public AndConverter(final StringConverter first, final StringConverter... others) {
			converters.add(first);
			converters.addAll(Arrays.asList(others));
		}

		/**
		 * Echos only the input and does no conversion.
		 *
		 * @param input
		 *            the input
		 * @return the input in upper case
		 * @see StringConverter#convert(java.lang.String)
		 */
		@Override
		public String convert(final String input) {
			String output = input;
			for (StringConverter stringConverter : converters) {
				output = stringConverter.convert(output);
			}
			return output;
		}
	}

	/**
	 * A simple upper case converter.
	 */
	private static class UpperCaseConverter extends StringConverter {
		/**
		 * Convert the input to upper case.s
		 *
		 * @param input
		 *            the input
		 * @return the input in upper case
		 * @see StringConverter#convert(java.lang.String)
		 */
		@Override
		public String convert(final String input) {
			return input.toUpperCase();
		}
	}

	/**
	 * A simple lower case converter.
	 */
	private static class LowerCaseConverter extends StringConverter {
		/**
		 * Convert the input to lower case.
		 *
		 * @param input
		 *            the input
		 * @return the input in upper case
		 * @see StringConverter#convert(java.lang.String)
		 */
		@Override
		public String convert(final String input) {
			return input.toLowerCase();
		}
	}

	/**
	 * A simple converter which ignores white spaces.
	 */
	private static class WhitespaceIgnorer extends StringConverter {
		/**
		 * Ignores the white spaces.
		 *
		 * @param input
		 *            the input
		 * @return the input in upper case
		 * @see StringConverter#convert(java.lang.String)
		 */
		@Override
		public String convert(final String input) {
			return StringUtils.trimToEmpty(input).replaceAll("\\s", "");
		}
	}

}
