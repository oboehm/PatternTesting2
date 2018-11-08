/*
 * $Id: FileTester.java,v 1.16 2016/12/30 22:54:13 oboehm Exp $
 *
 * Copyright (c) 2011-2017 by Oliver Boehm
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express orimplied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * (c)reated 06.04.2011 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import patterntesting.runtime.io.ExtendedFile;
import patterntesting.runtime.io.FileInputStreamReader;
import patterntesting.runtime.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

/**
 * The Class FileTester.
 *
 * @author oliver
 * @since 1.1 (06.04.2011)
 */
public class FileTester {

    /** Utility class - no need to instantiate it. */
    private FileTester() {
    }

    /**
     * Asserts that the content of two files are equal. If they are not equals
     * the first different line or byte will be shown.
     *
     * @param file1 the first file
     * @param file2 the second file
     * @throws AssertionError if the check fails
     */
    public static void assertContentEquals(final File file1, final File file2) throws AssertionError {
        assertContentEquals(file1, file2, Charset.defaultCharset());
    }

    /**
     * Asserts that the content of two files are equal. If they are not equals
     * the first different line or byte will be shown.
     *
     * @param file1 the first file
     * @param file2 the second file
     * @param from the line number from which the comparison is started
     *        (starting with 1)
     * @param to the last line number where the comparison ends.
     * @throws AssertionError if the check fails
     */
    public static void assertContentEquals(final File file1, final File file2, final int from, final int to)
            throws AssertionError {
        assertContentEquals(file1, file2, from, to, Charset.defaultCharset());
    }

    /**
     * Asserts that the content of two files are equal. If they are not equals
     * the first different line or byte will be shown.
     * <p>
     * The encoding is only important for the output if the two files are not
     * equals.
     * </p>
     *
     * @param file1 the first file
     * @param file2 the second file
     * @param encoding the encoding
     * @throws AssertionError if the check fails
     */
    public static void assertContentEquals(final File file1, final File file2, final String encoding)
            throws AssertionError {
        assertContentEquals(file1, file2, Charset.forName(encoding));
    }

    /**
     * Asserts that the content of two files are equal. If they are not equals
     * the first different line or byte will be shown.
     * <p>
     * The encoding is only important for the output if the two files are not
     * equals.
     * </p>
     *
     * @param file1 the first file
     * @param file2 the second file
     * @param from the line number from which the comparison is started
     *        (starting with 1)
     * @param to the last line number where the comparison ends.
     * @param encoding the encoding
     * @throws AssertionError if the check fails
     */
    public static void assertContentEquals(final File file1, final File file2, final int from, final int to,
            final String encoding) throws AssertionError {
        assertContentEquals(file1, file2, from, to, Charset.forName(encoding));
    }

    /**
     * Asserts that the content of two files are equal. If they are not equals
     * the first different line or byte will be shown.
     * <p>
     * The encoding is only important for the output if the two files are not
     * equals.
     * </p>
     *
     * @param file1 the first file
     * @param file2 the second file
     * @param encoding the encoding
     * @throws AssertionError if the check fails
     */
    public static void assertContentEquals(final File file1, final File file2, final Charset encoding)
            throws AssertionError {
        try {
            if (FileUtils.contentEquals(file1, file2)) {
                return;
            }
            assertContentEquals(file1, file2, 1, Integer.MAX_VALUE, encoding);
        } catch (IOException ioe) {
            throwAssertionError("can't compare " + file1 + " with " + file2, ioe);
        }
    }

    /**
     * Asserts that the content of two files are equal. If they are not equals
     * the first different line or byte will be shown.
     * <p>
     * The encoding is only important for the output if the two files are not
     * equals.
     * </p>
     *
     * @param file1 the first file
     * @param file2 the second file
     * @param from the line number from which the comparison is started
     *        (starting with 1)
     * @param to the last line number where the comparison ends.
     * @param encoding the encoding
     * @throws AssertionError if the check fails
     */
    public static void assertContentEquals(final File file1, final File file2, final int from, final int to,
            final Charset encoding) throws AssertionError {
        try (Reader r1 = new FileInputStreamReader(file1, encoding);
                Reader r2 = new FileInputStreamReader(file2, encoding)) {
            IOTester.assertContentEquals(r1, r2, from, to);
        } catch (IOException ioe) {
            throwAssertionError("can't compare " + file1 + " with " + file2, ioe);
        }
    }

    /**
     * Asserts that the content of two files are equal. For comparison the lines
     * are converted before by the given {@link StringConverter}. This allows
     * you e.g. to ignore white spaces (by using
     * {@link StringConverter#IGNORE_WHITESPACES} as parameter) or to ignore
     * upper / lower case problems ({@link StringConverter#LOWER_CASE}).
     * <p>
     * The {@link StringConverter} implementation allows you also to combine
     * different converters for some exotic problems.
     * </p>
     *
     * <pre>
     * FileTester.assertContentEquals(f1, f2, StringConverter.IGNORE_WHITESPACES)
     * </pre>
     * <p>
     * This is e.g. the call for you if whitespaces can be ignored for
     * comparison.
     * </p>
     *
     * @param file1 the first file
     * @param file2 the second file
     * @param converter the converter
     * @throws AssertionError if the check fails
     * @see #assertContentEquals(File, File, Charset, Pattern...)
     * @since 1.6
     */
    public static void assertContentEquals(final File file1, final File file2, final StringConverter converter)
            throws AssertionError {
        assertContentEquals(file1, file2, Charset.defaultCharset(), converter);
    }

    /**
     * Asserts that the content of two files are equal. For comparison the lines
     * are converted before by the given {@link StringConverter}. This allows
     * you e.g. to ignore white spaces (by using
     * {@link StringConverter#IGNORE_WHITESPACES} as parameter) or to ignore
     * upper / lower case problems ({@link StringConverter#LOWER_CASE}).
     *
     * @param file1 the first file
     * @param file2 the second file
     * @param charset the charset
     * @param converter the converter
     * @see #assertContentEquals(File, File, Charset, StringConverter,
     *      Pattern...)
     * @since 1.6
     */
    public static void assertContentEquals(final File file1, final File file2, final Charset charset,
            final StringConverter converter) {
        assertContentEquals(file1, file2, charset, converter, new Pattern[0]);
    }

    /**
     * Asserts that the content of two files are equal. For comparison the lines
     * are converted before by the given {@link StringConverter}. This allows
     * you e.g. to ignore white spaces (by using
     * {@link StringConverter#IGNORE_WHITESPACES} as parameter) or to ignore
     * upper / lower case problems ({@link StringConverter#LOWER_CASE}).
     *
     * @param file1 the first file
     * @param file2 the second file
     * @param converter the converter
     * @param ignores the pattern for the lines which should be ignored
     * @see #assertContentEquals(File, File, Charset, StringConverter,
     *      Pattern...)
     * @since 1.6
     */
    public static void assertContentEquals(final File file1, final File file2, final StringConverter converter,
            final Pattern... ignores) {
        assertContentEquals(file1, file2, Charset.defaultCharset(), converter, ignores);
    }

    /**
     * Asserts that the content of two files are equal. For comparison the lines
     * are converted before by the given {@link StringConverter}. This allows
     * you e.g. to ignore white spaces (by using
     * {@link StringConverter#IGNORE_WHITESPACES} as parameter) or to ignore
     * upper / lower case problems ({@link StringConverter#LOWER_CASE}).
     * <p>
     * Lines which matches the given 'ignores' pattern will be ignored for
     * comparison. This is done before the converter is executed. E.g. the
     * 'ignores' pattern must match the original line.
     * </p>
     *
     * @param file1 the first file
     * @param file2 the second file
     * @param encoding the encoding
     * @param converter the converter
     * @param ignores the ignores
     * @see #assertContentEquals(File, File, Charset, Pattern...)
     * @since 1.6
     */
    public static void assertContentEquals(final File file1, final File file2, final Charset encoding,
            final StringConverter converter, final Pattern... ignores) {
        try (Reader r1 = new FileInputStreamReader(file1, encoding);
                Reader r2 = new FileInputStreamReader(file2, encoding)) {
            IOTester.assertContentEquals(r1, r2, converter, ignores);
        } catch (IOException ioe) {
            throwAssertionError("can't compare " + file1 + " with " + file2, ioe);
        }
    }

    /**
     * Asserts that the content of two files are equal. Lines which matches the
     * given 'ignores' pattern will be ignored for comparison. E.g. if you want
     * to ignore two property files and want to ignore the comments and empty
     * lines you could use
     *
     * <pre>
     *     Pattern.compile("#.*"), Pattern.compile("[ \\t]*"
     * </pre>
     *
     * as ignores parameters.
     * <p>
     * If the two files are not equals the first different line or byte will be
     * shown.
     * </p>
     *
     * @param file1 the first file
     * @param file2 the second file
     * @param ignores the line pattern which should be ignored
     * @throws AssertionError if the check fails
     * @see #assertContentEquals(File, File, Charset, Pattern...)
     * @since 1.4
     */
    public static void assertContentEquals(final File file1, final File file2, final Pattern... ignores)
            throws AssertionError {
        assertContentEquals(file1, file2, Charset.defaultCharset(), ignores);
    }

    /**
     * Asserts that the content of two files are equal. Lines which matches the
     * given 'ignores' pattern will be ignored for comparison. E.g. if you want
     * to ignore two property files and want to ignore the comments and empty
     * lines you could use
     *
     * <pre>
     *     Pattern.compile("#.*"), Pattern.compile("[ \\t]*"
     * </pre>
     *
     * as ignores parameters.
     * <p>
     * If the two files are not equals the first different line or byte will be
     * shown.
     * </p>
     *
     * @param file1 the first file
     * @param file2 the second file
     * @param encoding the file encoding
     * @param ignores the line pattern which should be ignored
     * @throws AssertionError if the check fails
     * @since 1.4
     */
    public static void assertContentEquals(final File file1, final File file2, final Charset encoding,
            final Pattern... ignores) throws AssertionError {
        try (Reader r1 = new FileInputStreamReader(file1, encoding);
                Reader r2 = new FileInputStreamReader(file2, encoding)) {
            IOTester.assertContentEquals(r1, r2, ignores);
        } catch (IOException ioe) {
            throwAssertionError("can't compare " + file1 + " with " + file2, ioe);
        }
    }

    /**
     * Two files are considered equals if the would point to the same file
     * location on the disk.
     *
     * @param file1 e.g. file "/a/b/c"
     * @param file2 e.g. file "/a/b/../b/c"
     */
    public static void assertEquals(final File file1, final File file2) {
        Assertions.assertEquals(new ExtendedFile(file1).normalize(), new ExtendedFile(file2).normalize());
    }

    private static void throwAssertionError(final String msg, final Throwable t) throws AssertionError {
        AssertionError error = new AssertionError(msg);
        error.initCause(t);
        throw error;
    }

}
