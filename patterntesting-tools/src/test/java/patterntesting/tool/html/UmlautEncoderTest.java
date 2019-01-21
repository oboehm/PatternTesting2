/*
 * $Id: UmlautEncoderTest.java,v 1.5 2016/12/30 19:07:44 oboehm Exp $
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
 * (c)reated 06.01.2011 by oliver (ob@oasd.de)
 */

package patterntesting.tool.html;

import static org.junit.Assert.*;

import java.io.*;
import java.nio.charset.*;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.*;
import org.junit.Test;

import patterntesting.runtime.io.ExtendedFile;

/**
 * JUnit test for UmlautEncoder.
 * 
 * @author oliver
 * @since 1.1 (06.01.2011)
 */
public final class UmlautEncoderTest {
    
    private static final Logger log = LogManager.getLogger(UmlautEncoderTest.class);
    private static final File INPUT_ISO = new File(
            "src/test/resources/patterntesting/tool/html/input-iso8859.html");
    private static final File INPUT_UTF = new File(
            "src/test/resources/patterntesting/tool/html/input-utf8.html");
    private static final File REF_ISO = new File(
            "src/test/resources/patterntesting/tool/html/output-iso8859.html");
    private static final File REF_UTF = new File(
            "src/test/resources/patterntesting/tool/html/output-utf8.html");

    /**
     * Test method for {@link UmlautEncoder#encode(String)}.
     */
    @Test
    public void testEncodeString() {
        String prefix = "<html><head/><body><p>";
        String suffix = "</p></body></html>";
        String input = prefix + "\u00e4\u00f6\u00fc\u00df\u00c4\u00d6\u00dc" + suffix;
        String expected = prefix + "&auml;&ouml;&uuml;&szlig;&Auml;&Ouml;&Uuml;" + suffix;
        assertEquals(expected, UmlautEncoder.encode(input));
    }
    
    /**
     * Test method for {@link UmlautEncoder#guessEncoding(java.io.File)}.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testGuessEncoding() throws IOException {
        Charset encoding = UmlautEncoder.guessEncoding(INPUT_ISO);
        assertEquals(Charset.forName("ISO-8859-1"), encoding);
    }

    /**
     * Test method for {@link UmlautEncoder#encode(java.io.File)} with file
     * in UTF-8 encoding.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testEncodeFileUTF() throws IOException {
        checkEncodeFile(INPUT_UTF, REF_UTF);
    }
    
    /**
     * Test method for {@link UmlautEncoder#encode(java.io.File)} with file
     * in ISO-8859-1 encoding.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testEncodeFileISO() throws IOException {
        checkEncodeFile(INPUT_ISO, REF_ISO);
    }

    private void checkEncodeFile(final File input, final File ref) throws IOException {
        File output = File.createTempFile("output", ".html");
        UmlautEncoder.encode(input, output);
        assertEqualContent(ref, output);
        if (output.delete()) {
            log.debug("deleted: " + output);
        }
    }

    /**
     * Test method for {@link UmlautEncoder#encode(java.io.File)} if the given
     * file is a directory.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testEncodeDir() throws IOException {
        File testDir = ExtendedFile.getTmpdir("dir" + System.currentTimeMillis());
        assertTrue("can't create " + testDir, testDir.mkdir());
        log.info("created: " + testDir);
        try {
            FileUtils.copyFileToDirectory(INPUT_ISO, testDir);
            FileUtils.copyFileToDirectory(INPUT_UTF, testDir);
            UmlautEncoder.encode(testDir);
            assertEqualContent(REF_ISO, new File(testDir, INPUT_ISO.getName()));
            assertEqualContent(REF_UTF, new File(testDir, INPUT_UTF.getName()));
        } finally {
            FileUtils.deleteDirectory(testDir);
            log.info("deleted: " + testDir);
        }
    }
    
    private static void assertEqualContent(final File expected, final File actual) throws IOException {
        assertEquals(FileUtils.readFileToString(expected, StandardCharsets.UTF_8),
                FileUtils.readFileToString(actual, StandardCharsets.UTF_8));
    }

}

