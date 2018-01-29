/*
 * $Id: FileTesterTest.java,v 1.15 2016/12/30 22:54:13 oboehm Exp $
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
 * (c)reated 06.04.2011 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import org.apache.logging.log4j.*;
import org.junit.Test;

import patterntesting.runtime.util.StringConverter;

/**
 * UnitTests for {@link FileTester} class.
 *
 * @author oliver
 * @since 1.1 (06.04.2011)
 */
public final class FileTesterTest {

    private static final Logger LOG = LogManager.getLogger(FileTesterTest.class);
    private static final File DOSFILE1 = new File(
            "src/test/resources/patterntesting/runtime/junit/dosfile1.txt");
    private static final File FILE1 = new File(
            "src/test/resources/patterntesting/runtime/junit/file1.txt");
    private static final File FILE2 = new File(
            "src/test/resources/patterntesting/runtime/junit/file2.txt");
    private static final File FILE3 = new File(
            "src/test/resources/patterntesting/runtime/junit/file3.txt");
    private static final File FILE1COMMENTED = new File(
            "src/test/resources/patterntesting/runtime/junit/file1commented.txt");
    private static final File FILE1SPACES = new File(
            "src/test/resources/patterntesting/runtime/junit/file1spaces.txt");

    /**
     * Test method for {@link FileTester#assertContentEquals(File, File)}.
     */
    @Test
    public void testAssertContentEquals() {
        FileTester.assertContentEquals(FILE1, FILE2);
        try {
            FileTester.assertContentEquals(FILE1, FILE3);
            fail("AssertionError expected");
        } catch (AssertionError expected) {
            checkAssertionError(expected);
        }
    }
    
    /**
     * If one of the compared files should causes an IOException this should
     * be part of the thrown {@link AssertionError}.
     */
    @Test
    public void testAssertContentEqualsWithIOException() {
        File notExisting = new File("/not/existing");
        assertFalse("should not exist for this test: " + notExisting, notExisting.exists());
        try {
            FileTester.assertContentEquals(FILE1, notExisting, StandardCharsets.UTF_8);
            fail("AssertionError expected");
        } catch (AssertionError expected) {
            LOG.info("expected: ",  expected);
            assertEquals(FileNotFoundException.class, expected.getCause().getClass());
        }
    }

    /**
     * Two files with same context but different linefeeds should be considered
     * as equals. This is the unit test for bug #23 (see
     * http://sourceforge.net/p/patterntesting/bugs/23/).
     */
    @Test
    public void testAssertContentEqualsDosUnix() {
        FileTester.assertContentEquals(FILE1, DOSFILE1);
        try {
            FileTester.assertContentEquals(DOSFILE1, FILE3);
            fail("AssertionError expected");
        } catch (AssertionError expected) {
            checkAssertionError(expected);
        }
    }
    
    /**
     * If one of the files for an {@link FileTester} does not exist we want
     * to see it as part of the assertion error.
     */
    @Test
    public void testAssertContentEqualsFileDoesNotExist() {
    	File notExisting = new File("file.does", "not.exist");
    	try {
    		FileTester.assertContentEquals(FILE1, notExisting);
    	} catch (AssertionError expected) {
    		String msg = expected.getMessage();
    		LOG.info("msg = \"{}\"", msg, expected);
    		assertThat(msg, containsString(notExisting.toString()));
    	}
    }

    /**
     * Test method for {@link FileTester#assertContentEquals(File, File, int, int)}.
     */
    @Test
    public void testAssertContentEqualsFromTo() {
        FileTester.assertContentEquals(FILE2, FILE3, 1, 3);
        try {
            FileTester.assertContentEquals(FILE2, FILE3, 1, 4);
            fail("AssertionError expected");
        } catch (AssertionError expected) {
            checkAssertionError(expected);
        }
    }

    /**
	 * Test method for
	 * {@link FileTester#assertContentEquals(File, File, Pattern...)}.
	 */
    @Test
    public void testAssertContentEqualsIgnores() {
        FileTester.assertContentEquals(FILE1, FILE1COMMENTED, Pattern.compile("#.*"));
        try {
            FileTester.assertContentEquals(FILE1, FILE1COMMENTED, Pattern.compile("##.*"));
            fail("AssertionError expected");
        } catch (AssertionError expected) {
            checkAssertionError(expected);
            assertTrue("line expected: " + expected.getMessage(), expected.getMessage().contains("line 4/5"));
        }
    }

    private static void checkAssertionError(final AssertionError expected) {
        String msg = expected.getMessage();
        LOG.info(msg);
        assertTrue(msg, msg.contains("line"));
    }

    /**
	 * Test method for
	 * {@link FileTester#assertContentEquals(File, File, StringConverter)}.
	 */
    @Test
    public void testAssertContentEqualsIgnoreWhitespaces() {
		FileTester.assertContentEquals(FILE1, FILE1SPACES, StringConverter.IGNORE_WHITESPACES);
    }

    /**
     * This is a test for
     * {@link FileTester#assertContentEquals(File, File, String)} to see if two
     * files with encoding "ISO-8859-1" are compared correct.
     */
    @Test
    public void testAssertContentEqualsWithEncoding() {
        File f1 = new File("src/test/resources/patterntesting/runtime/junit/iso-8859-1.txt");
        File f2 = f1;
        FileTester.assertContentEquals(f1, f2, "ISO-8859-1");
    }

    /**
     * The same test as before but now with 'from' and 'to' parameters.
     */
    @Test
    public void testAssertContentEqualsFromToWithEncoding() {
        FileTester.assertContentEquals(FILE2, FILE3, 1, 3, "UTF8");
    }

    /**
     * The interesting thing for this text is the ouptput. So watch the log to
     * see if encoding works correct.
     */
    @Test
    public void testAssertContentNotEquals() {
        File iso = new File("src/test/resources/patterntesting/runtime/junit/iso-8859-1.txt");
        try {
            FileTester.assertContentEquals(iso, FILE3, "ISO-8859-1");
            fail("AssertionError expected");
        } catch (AssertionError expected) {
            String msg = expected.getMessage();
            LOG.info(msg);
            String expectedPrefix = "iso-8859-1.txt | file3.txt: line 4 -";
            assertEquals(expectedPrefix, msg.substring(0, expectedPrefix.length()));
        }
    }
    
    /**
     * Test method for {@link FileTester#assertEquals(java.io.File, java.io.File)}.
     */
    @Test
    public void testAssertEquals() {
        File f1 = new File("/a/b/c");
        File f2 = new File("/a/b/c");
        FileTester.assertEquals(f1, f2);
        File f3 = new File("/a/b/../b/c");
        FileTester.assertEquals(f1, f3);
    }

}

