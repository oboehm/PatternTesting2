/*
 * $Id: IOExceptionTest.java,v 1.11 2016/12/18 21:57:35 oboehm Exp $
 *
 * Copyright (c) 2008 by Oliver Boehm
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
 * (c)reated 10.09.2008 by oliver (ob@oasd.de)
 */
package patterntesting.exception.io;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

/**
 * This is the JUnit test for the IOExceptionAspect.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.11 $
 * @since 10.09.2008
 */
public class IOExceptionTest {

	private static final Logger log = LogManager.getLogger(IOExceptionTest.class);
	private final File missingDir = new File("not", "existing");

	/**
	 * Test create temp file.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
    @Test(expected = DirNotFoundException.class)
	public final void testCreateTempFile() throws IOException {
	    File.createTempFile("exception", "expected", missingDir);
	}

	/**
	 * Test create temp file with dir prefix.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
    @Test
	public final void testCreateTempFileWithDirPrefix() throws IOException {
        try {
    	    File created = File.createTempFile(missingDir.toString(), ".test");
    	    created.delete();
    	    created.getParentFile().delete();
        } catch (DirNotFoundException expected) {
            log.info("expected: " + expected);
        }
	}

	/**
	 * Test create temp file with dir suffix.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public final void testCreateTempFileWithDirSuffix() throws IOException {
		try {
			File.createTempFile("test", missingDir.toString());
			fail("IOException expected for suffix " + missingDir);
		} catch (IOException expected) {
			checkMessage(expected);
		}
	}

	/**
	 * Test create new file.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
    @Test(expected = DirNotFoundException.class)
	public final void testCreateNewFile() throws IOException {
		File file = new File(SystemUtils.getJavaIoTmpDir(), missingDir.toString());
		file.createNewFile();
	}

    /**
     * Test file not found exception input stream.
     */
    @Test
    public final void testFileNotFoundExceptionInputStream() {
        String filename = "hugo";
        File file = new File(filename);
        assertFalse(file + " should not exist", file.exists());
        try {
            InputStream istream = new FileInputStream(file);
            IOUtils.closeQuietly(istream);
            fail("FileNotFoundException expected for " + file);
        } catch (FileNotFoundException expected) {
            checkMessage(expected);
        }
        try {
            InputStream istream = new FileInputStream(filename);
            IOUtils.closeQuietly(istream);
            fail("FileNotFoundException expected for " + filename);
        } catch (FileNotFoundException expected) {
            checkMessage(expected);
        }
    }

    /**
     * Test file not found exception output stream.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public final void testFileNotFoundExceptionOutputStream()
            throws IOException {
        String filename = "readonly.test";
        File file = new File(filename);
        assertFalse(file + " should not exist", file.exists());
        assertTrue("can't create " + file, file.createNewFile());
        assertTrue("can't set " + file + " readonly", file.setReadOnly());
        try {
            OutputStream ostream = new FileOutputStream(file);
            ostream.close();
            fail("FileNotFoundException expected for " + file);
        } catch (FileNotFoundException expected) {
            checkMessage(expected);
        }
        try {
            OutputStream ostream = new FileOutputStream(filename);
            ostream.close();
            fail("FileNotFoundException expected for " + filename);
        } catch (FileNotFoundException expected) {
            checkMessage(expected);
        } finally {
            if (file.delete()) {
                log.info(file.getAbsoluteFile() + " deleted");
            }
        }
    }

    /**
     * What happens, if a file cound be not created because part of the
     * directory path is missing?.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test(expected = DirNotFoundException.class)
    public final void testFileNotFoundExceptionOutputStreamMissingDir() throws IOException {
        File file = new File(missingDir, "test.txt");
        OutputStream ostream = new FileOutputStream(file);
        ostream.close();
    }

    /**
     * Do you get a FileNotFoundException for RandomAccessFile? Let's check it!.
     */
    @Test
    public final void testFileNotFoundExceptionRandomAccess() {
        String filename = "hugo";
        File file = new File(filename);
        assertFalse(file + " should not exist", file.exists());
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            IOUtils.closeQuietly(raf);
            fail("FileNotFoundException expected for " + file);
        } catch (FileNotFoundException expected) {
            checkMessage(expected);
        }
        try {
            RandomAccessFile raf = new RandomAccessFile(filename, "r");
            IOUtils.closeQuietly(raf);
            fail("FileNotFoundException expected for " + filename);
        } catch (FileNotFoundException expected) {
            checkMessage(expected);
        }
    }

	private void checkMessage(final IOException expected) {
		log.info(expected.getLocalizedMessage());
		String msg = expected.getMessage();
		assertFalse("filename missing in '" + msg + "'", msg
				.equals("No such file or directory"));
	}

	private void checkMessage(final FileNotFoundException expected) {
	    log.info(expected.getLocalizedMessage());
	    String msg = expected.getMessage().trim();
	    String filename = StringUtils.substringBefore(msg,
                " (No such file or directory)");
	    File file = new File(filename);
	    assertTrue(file + " should be absolute", file.isAbsolute());
	}

}
