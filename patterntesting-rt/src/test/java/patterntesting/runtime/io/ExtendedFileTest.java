/*
 * $Id: ExtendedFileTest.java,v 1.12 2016/12/28 21:11:23 oboehm Exp $
 *
 * Copyright (c) 2014 by Oliver Boehm
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
 * (c)reated 27.08.2014 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.io;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import patterntesting.runtime.junit.FileTester;
import patterntesting.runtime.junit.ObjectTester;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ExtendedFile} class.
 *
 * @author oliver
 * @since 1.5 (27.08.2014)
 */
public class ExtendedFileTest {

	private static final Logger LOG = LoggerFactory.getLogger(ExtendedFileTest.class);
	private static File dummy;
	
	/**
     * Here we create a dummy file for testing in the tmp directory.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @BeforeAll
    public static void setUpDummyFile() throws IOException {
        dummy = File.createTempFile("dummy", ".tst");
        LOG.info("File '{}' was created for testing.", dummy);
    }

    /**
     * Test method for {@link ExtendedFile#endsWith(File)}.
     */
    @Test
    public void testEndsWith() {
        ExtendedFile file = new ExtendedFile("/tmp/web/WEB-INF/classes");
        File webinf = new File("WEB-INF");
        File webinfClasses = new File(webinf, "classes");
        assertTrue(file.endsWith(webinfClasses), file + " should end with " + webinfClasses);
        assertFalse(file.endsWith(webinf), file + " should not end with " + webinf);
    }

    /**
     * Test method for {@link ExtendedFile#getBaseDir(File)}.
     */
    @Test
    public void testBase() {
        File base = new File("/tmp", "web");
        File webinfClasses = new File("WEB-INF", "classes");
        ExtendedFile file = new ExtendedFile(base, webinfClasses);
        FileTester.assertEquals(base, file.getBaseDir(webinfClasses));
    }

    /**
     * Test method for {@link ExtendedFile#equals(Object)}.
     */
    @Test
    public void testEqualsFileFile() {
        String name = dummy.getName();
        File file = new ExtendedFile(dummy.getParent(), "./" + name);
        ObjectTester.assertEquals(new ExtendedFile(dummy), file);
        assertEquals(file, dummy, "expected: " + file + " == " + dummy);
    }

    /**
     * On MacOS "/tmp" and "/private/tmp" are the same directory. So a file
     * in this two directories should be recognized as equals.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testEqualsFileOnDifferentPath() throws IOException {
        assumeTrue(SystemUtils.IS_OS_MAC,"this runs only on Mac");
        String name = dummy.getName();
        File tmp = new ExtendedFile("/tmp", name);
        assertTrue(tmp.createNewFile());
        try {
            File privateTmp = new ExtendedFile("/private/tmp", name);
            assertTrue(privateTmp.exists(), privateTmp + " does not exist");
            assertEquals(tmp, privateTmp);
        } finally {
            tmp.deleteOnExit();
        }
    }
    
    /**
     * Test method for {@link ExtendedFile#validate(File)} with an existing
     * file.
     *
     * @throws FileNotFoundException the file not found exception
     */
    @Test
    public void testValidateFileExisting() throws FileNotFoundException {
        File existing = new File("src/test/resources/log4j2.xml");
        assertTrue(existing.exists(), "should exist: " + existing);
        ExtendedFile.validate(existing);
    }

	/**
	 * Test method for {@link ExtendedFile#validate(File)} with a non
	 * existing file.
	 */
	@Test
	public void testValidateFileNotExisting() {
	    assertThrows(FileNotFoundException.class, () -> {
            File notExisting = new File("/not/existing/file");
            assertFalse(notExisting.exists(), notExisting + " should not exist for this test");
            ExtendedFile.validate(notExisting);
        });
	}

    /**
     * Under Windows filenames are not case sensitve. I.e. the file "test.txt"
     * and "test.TXT" are the same under Windows but not on Unix. This may be
     * one of the reason why a test works on Windows but fails on the CI server
     * which runs on Linux. In the case you would like to have an hint what
     * wents wrong.
     */
    @Test
    public void testValidateFileExistsOnWindows() {
        File expected = new File("src/test/resources/log4j2.xml");
        File wrong = spy(new File("src/test/resources/log4j2.XML"));
        doReturn(false).when(wrong).exists();
        checkValidate(expected, wrong);
    }
    
    /**
     * This is the same test as before but with a dir instead of a file.
     */
    @Test
    public void testValidateDirExistsOnWindows() {
        File expected = new File("src/test/resources/log4j2.xml");
        File wrongDir = spy(new File("src/test/RESOURCES"));
        File wrongFile = spy(new File(wrongDir, "log4j2.xml"));
        when(wrongFile.exists()).thenReturn(false);
        when(wrongFile.getParentFile()).thenReturn(wrongDir);
        when(wrongDir.exists()).thenReturn(false);
        checkValidate(expected, wrongFile);
    }

    /**
     * Here we change the first directory entry to see if
     * {@link ExtendedFile#validate(File)} works correct.
     */
    @Test
    public void testValidateRootDir() {
        File expected = new File("src/test/resources/log4j2.xml");
        File srcDir = spy(new File("SRC/"));
        File testDir = spy(new File(srcDir, "test"));
        File resourcesDir = spy(new File(testDir, "resources"));
        File log4jFile = spy(new File(resourcesDir, "log4j2.xml"));
        when(log4jFile.exists()).thenReturn(false);
        when(resourcesDir.getParentFile()).thenReturn(testDir);
        when(testDir.getParentFile()).thenReturn(srcDir);
        when(srcDir.exists()).thenReturn(false);
        checkValidate(expected, log4jFile);
    }

    private static void checkValidate(File expected, File wrongFile) {
        try {
            ExtendedFile.validate(wrongFile);
            LOG.info("{} and {} are identical files on {}.", wrongFile, expected, SystemUtils.OS_NAME);
        } catch (FileNotFoundException happensOnUnix) {
            LOG.debug("{} validated:", wrongFile, happensOnUnix);
            String msg = happensOnUnix.getMessage();
            LOG.info(msg);
            assertThat(msg, containsString(expected.getPath()));
        }
    }
    
    /**
     * In the first version of {@link ExtendedFile#validate(File)} a NPE
     * happens if the given file has no directory. This should not happen
     * again.
     */
    @Test
    public void testValidateFileOnly() {
        assertThrows(FileNotFoundException.class, () -> ExtendedFile.validate(new File("hello.world")));
    }

    /**
     * Delete dummy file.
     */
    @AfterAll
    public static void deleteDummyFile() {
        dummy.deleteOnExit();
        LOG.info("File '{}' will be deleted on exit.", dummy);
    }

}
