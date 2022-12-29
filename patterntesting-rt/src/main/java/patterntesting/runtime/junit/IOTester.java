/*
 * $Id: IOTester.java,v 1.17 2017/11/09 20:34:50 oboehm Exp $
 *
 * Copyright (c) 2011 by Oliver Boehm
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
 * (c)reated 30.03.2011 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.jupiter.api.Assertions;
import patterntesting.annotation.check.runtime.MayReturnNull;
import patterntesting.runtime.io.LineReader;
import patterntesting.runtime.util.StringConverter;

import java.io.*;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

/**
 * With the IOTester you can assert if two files have equals content or if two
 * directories have the same structure with equal files.
 * <p>
 * Note: This test is not yet tested with binary files.
 * </p>
 *
 * @author oliver
 * @since 1.1 (30.03.2011)
 */
public final class IOTester {

	private static final Logger LOG = LoggerFactory.getLogger(IOTester.class);

	/** Utility class - no need to instantiate it. */
	private IOTester() {
	}

	/**
	 * Asserts that the content of two InputStream are equal. If they are not
	 * equals the first different line or byte will be shown.
	 *
	 * @param in1
	 *            the first InputStream
	 * @param in2
	 *            the second InputStream
	 * @throws AssertionError
	 *             if the check fails
	 */
	public static void assertContentEquals(final InputStream in1, final InputStream in2) throws AssertionError {
		assertContentEquals(in1, in2, Charset.defaultCharset());
	}

	/**
	 * Asserts that the content of two InputStream are equal. If they are not
	 * equals the first different line or byte will be shown.
	 *
	 * @param in1
	 *            the first InputStream
	 * @param in2
	 *            the second InputStream
	 * @param encoding
	 *            the encoding
	 * @throws AssertionError
	 *             if the check fails
	 */
	public static void assertContentEquals(final InputStream in1, final InputStream in2, final Charset encoding)
			throws AssertionError {
		assertContentEquals(in1, in2, encoding, new Pattern[0]);
	}

	/**
	 * Asserts that the content of two InputStream are equal. If they are not
	 * equals the first different line or byte will be shown.
	 *
	 * @param in1
	 *            the first InputStream
	 * @param in2
	 *            the second InputStream
	 * @param from
	 *            the line number from which the comparison is started (starting
	 *            with 1)
	 * @param to
	 *            the last line number where the comparison ends.
	 * @throws AssertionError
	 *             if the check fails
	 */
	public static void assertContentEquals(final InputStream in1, final InputStream in2, final int from, final int to)
			throws AssertionError {
		assertContentEquals(in1, in2, Charset.defaultCharset(), from, to);
	}

	/**
	 * Asserts that the content of two InputStream are equal. If they are not
	 * equals the first different line or byte will be shown.
	 *
	 * @param in1
	 *            the first InputStream
	 * @param in2
	 *            the second InputStream
	 * @param encoding
	 *            the encoding
	 * @param from
	 *            the line number from which the comparison is started (starting
	 *            with 1)
	 * @param to
	 *            the last line number where the comparison ends.
	 * @throws AssertionError
	 *             if the check fails
	 */
	public static void assertContentEquals(final InputStream in1, final InputStream in2, final Charset encoding,
			final int from, final int to) throws AssertionError {
		assertContentEquals(new InputStreamReader(in1, encoding), new InputStreamReader(in2, encoding), from, to);
	}

	/**
	 * Asserts that the content of two Readers are equal. If they are not equals
	 * the first different line or byte will be shown.
	 *
	 * @param in1
	 *            the first InputStream
	 * @param in2
	 *            the second InputStream
	 * @param ignores
	 *            the lines matching this pattern will be ignored
	 * @throws AssertionError
	 *             if the check fails
	 */
	public static void assertContentEquals(final InputStream in1, final InputStream in2, final Pattern... ignores)
			throws AssertionError {
		assertContentEquals(in1, in2, Charset.defaultCharset(), ignores);
	}

	/**
	 * Asserts that the content of two Readers are equal. If they are not equals
	 * the first different line or byte will be shown.
	 *
	 * @param in1
	 *            the first InputStream
	 * @param in2
	 *            the second InputStream
	 * @param encoding
	 *            the encoding
	 * @param ignores
	 *            the lines matching this pattern will be ignored
	 * @throws AssertionError
	 *             if the check fails
	 */
	public static void assertContentEquals(final InputStream in1, final InputStream in2, final Charset encoding,
			final Pattern... ignores) throws AssertionError {
		assertContentEquals(new InputStreamReader(in1, encoding), new InputStreamReader(in2, encoding), ignores);
	}

	/**
     * Asserts that the content of two Readers are equal. If they are not equals
     * the first different line or byte will be shown.
     *
     * @param r1 the first Reader
     * @param r2 the second Reader
     * @throws AssertionError if the check fails
     */
	// TODO: shold work also for binary files
	public static void assertContentEquals(final Reader r1, final Reader r2) throws AssertionError {
		assertContentEquals(new BufferedReader(r1), new BufferedReader(r2));
	}

	/**
     * Asserts that the content of two Readers are equal. If they are not equals
     * the first different line or byte will be shown.
     *
     * @param r1 the first Reader
     * @param r2 the second Reader
     * @param from the line number from which the comparison is started
     *        (starting with 1)
     * @param to the last line number where the comparison ends.
     * @throws AssertionError if the check fails
     */
	public static void assertContentEquals(final Reader r1, final Reader r2, final int from, final int to)
			throws AssertionError {
		assertContentEquals(new LineReader(r1), new LineReader(r2), from, to);
	}

	/**
     * Asserts that the content of two Readers are equal. If they are not equals
     * the first different line or byte will be shown.
     *
     * @param r1 the first Reader
     * @param r2 the second Reader
     * @param ignores the lines matching this pattern will be ignored
     * @throws AssertionError if the check fails
     */
	public static void assertContentEquals(final Reader r1, final Reader r2, final Pattern... ignores)
			throws AssertionError {
		assertContentEquals(new LineReader(r1), new LineReader(r2), ignores);
	}

	/**
     * Asserts that the content of two Readers are equal. If they are not equals
     * the first different line or byte will be shown.
     * <p>
     * For comparison the lines are converted before by the given
     * {@link StringConverter}. This allows you e.g. to ignore white spaces (by
     * using {@link StringConverter#IGNORE_WHITESPACES} as parameter) or to
     * ignore upper / lower case problems ({@link StringConverter#LOWER_CASE}).
     * </p>
     *
     * @param r1 the first Reader
     * @param r2 the second Reader
     * @param converter the converter
     * @throws AssertionError if the check fails
     * @since 1.6
     */
	public static void assertContentEquals(final Reader r1, final Reader r2, final StringConverter converter)
			throws AssertionError {
		assertContentEquals(r1, r2, converter, new Pattern[0]);
	}

	/**
     * Asserts that the content of two Readers are equal. If they are not equals
     * the first different line or byte will be shown.
     *
     * @param r1 the first Reader
     * @param r2 the second Reader
     * @throws AssertionError if the check fails
     */
	public static void assertContentEquals(final BufferedReader r1, final BufferedReader r2) throws AssertionError {
		assertContentEquals(r1, r2, 1, Integer.MAX_VALUE);
	}

	/**
     * Asserts that the content of two Readers are equal. If they are not equals
     * the first different line or byte will be shown.
     *
     * @param r1 the first Reader
     * @param r2 the second Reader
     * @param from the line number from which the comparison is started
     *        (starting with 1)
     * @param to the last line number where the comparison ends.
     * @throws AssertionError if the check fails
     */
	public static void assertContentEquals(final LineReader r1, final LineReader r2, final int from, final int to)
			throws AssertionError {
		try {
			r1.skipLines(from - 1);
			r2.skipLines(from - 1);
			String prefix = getMsgPrefix(r1, r2);
			for (int lineNo = from; lineNo <= to; lineNo++) {
				String line1 = r1.readLine();
				String line2 = r2.readLine();
				if ((line1 == null) && (line2 == null)) {
					return;
				}
				Assertions.assertEquals(line1, line2, prefix + lineNo + " -");
			}
		} catch (IOException ioe) {
			throw new AssertionError(ioe);
		}
	}

	/**
     * Asserts that the content of two Readers are equal. If they are not equals
     * the first different line or byte will be shown.
     *
     * @param r1 the first Reader
     * @param r2 the second Reader
     * @param ignores the lines matching this pattern will be ignored
     * @throws AssertionError if the check fails
     */
	public static void assertContentEquals(final LineReader r1, final LineReader r2, final Pattern... ignores)
			throws AssertionError {
		try {
			String prefix = getMsgPrefix(r1, r2);
			for (;;) {
				String line1 = nextLine(r1, ignores);
				String line2 = nextLine(r2, ignores);
				if ((line1 == null) && (line2 == null)) {
					return;
				}
				if (r1.getLineNumber() == r2.getLineNumber()) {
					Assertions.assertEquals(line1, line2, prefix + r1.getLineNumber() + " -");
				} else {
					Assertions.assertEquals(line1, line2, prefix + r1.getLineNumber() + "/" + r2.getLineNumber() + " -");
				}
			}
		} catch (IOException ioe) {
			throw new AssertionError(ioe);
		}
	}

	/**
	 * Asserts that the content of two Readers are equal. If they are not equals
	 * the first different line or byte will be shown.
	 * <p>
	 * For comparison the lines are converted before by the given
	 * {@link StringConverter}. This allows you e.g. to ignore white spaces (by
	 * using {@link StringConverter#IGNORE_WHITESPACES} as parameter) or to
	 * ignore upper / lower case problems ({@link StringConverter#LOWER_CASE}).
	 * </p>
	 * <p>
	 * Lines which matches the given 'ignores' pattern will be ignored for
	 * comparison. This is done before the converter is executed. E.g. the
	 * 'ignores' pattern must match the original line.
	 * </p>
	 *
	 * @param r1
	 *            the r1
	 * @param r2
	 *            the r2
	 * @param converter
	 *            the converter
	 * @param ignores
	 *            the ignores
	 * @since 1.6
	 */
	public static void assertContentEquals(final Reader r1, final Reader r2, final StringConverter converter,
			final Pattern[] ignores) {
		assertContentEquals(new LineReader(r1), new LineReader(r2), converter, ignores);
	}

	/**
	 * Asserts that the content of two Readers are equal. If they are not equals
	 * the first different line or byte will be shown.
	 * <p>
	 * For comparison the lines are converted before by the given
	 * {@link StringConverter}. This allows you e.g. to ignore white spaces (by
	 * using {@link StringConverter#IGNORE_WHITESPACES} as parameter) or to
	 * ignore upper / lower case problems ({@link StringConverter#LOWER_CASE}).
	 * </p>
	 * <p>
	 * Lines which matches the given 'ignores' pattern will be ignored for
	 * comparison. This is done before the converter is executed. E.g. the
	 * 'ignores' pattern must match the original line.
	 * </p>
	 *
	 * @param r1
	 *            the r1
	 * @param r2
	 *            the r2
	 * @param converter
	 *            the converter
	 * @param ignores
	 *            the ignores
	 */
	public static void assertContentEquals(final LineReader r1, final LineReader r2, final StringConverter converter,
			final Pattern[] ignores) {
		try {
			String prefix = getMsgPrefix(r1, r2);
			for (;;) {
				String line1 = nextLine(r1, ignores);
				String line2 = nextLine(r2, ignores);
				if ((line1 == null) && (line2 == null)) {
					return;
				}
				line1 = converter.convert(line1);
				line2 = converter.convert(line2);
				if (r1.getLineNumber() == r2.getLineNumber()) {
					Assertions.assertEquals(line1, line2, prefix + r1.getLineNumber() + " -");
				} else {
					Assertions.assertEquals(line1, line2, prefix + r1.getLineNumber() + "/" + r2.getLineNumber() + " -");
				}
			}
		} catch (IOException ioe) {
			throw new AssertionError(ioe);
		}
	}

	private static String getMsgPrefix(final LineReader r1, final LineReader r2) {
		String s1 = r1.toString();
		String s2 = r2.toString();
		if (s1.contains("@") && s2.contains("@")) {
			return "line ";
		}
		File f1 = new File(s1);
		File f2 = new File(s2);
		if (f1.exists() && f2.exists()) {
			return getMsgPrefix(f1, f2);
		}
		return s1 + " | " + s2 + ": line ";
	}

	private static String getMsgPrefix(final File f1, final File f2) {
		File d1 = f1.getParentFile();
		File d2 = f2.getParentFile();
		if (d1.equals(d2)) {
			return f1.getName() + " | " + f2.getName() + ": line ";
		}
		return f1 + " | " + f2 + ": line ";
	}

	@MayReturnNull
	private static String nextLine(final BufferedReader reader, final Pattern... ignores) throws IOException {
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			if (matches(line, ignores)) {
				LOG.trace("\"{}\" maches {} and is ignored.", line, ignores);
			} else {
				return line;
			}
		}
		return null;
	}

	private static boolean matches(final String line, final Pattern[] ignores) {
		for (int i = 0; i < ignores.length; i++) {
			if (ignores[i].matcher(line).matches()) {
				return true;
			}
		}
		return false;
	}

}
