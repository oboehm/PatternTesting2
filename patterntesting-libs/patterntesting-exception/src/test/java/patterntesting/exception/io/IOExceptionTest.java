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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is the JUnit test for the IOExceptionAspect.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.11 $
 * @since 10.09.2008
 */
public class IOExceptionTest {

	private static final Logger log = LoggerFactory.getLogger(IOExceptionTest.class);
	private final File missingDir = new File("not", "existing");

	/**
	 * Test create temp file.
	 */
    @Test
	public final void testCreateTempFile() {
        assertThrows(DirNotFoundException.class, () -> File.createTempFile("exception", "expected", missingDir));
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
	 */
	@Test
	public final void testCreateTempFileWithDirSuffix() {
		try {
			File.createTempFile("test", missingDir.toString());
			fail("IOException expected for suffix " + missingDir);
		} catch (IOException expected) {
			checkMessage(expected);
		}
	}

	/**
	 * Test create new file.
	 */
    @Test
	public final void testCreateNewFile() {
        assertThrows(DirNotFoundException.class, () -> {
            File file = new File(SystemUtils.getJavaIoTmpDir(), missingDir.toString());
            file.createNewFile();
        });
	}

    /**
     * Test file not found exception input stream.
     *
     * @throws IOException the io exception
     */
    @Test
    public final void testFileNotFoundExceptionInputStream() throws IOException {
        String filename = "hugo";
        File file = new File(filename);
        assertFalse(file.exists(), file + " should not exist");
        try (InputStream istream = new FileInputStream(file)) {
            fail("FileNotFoundException expected for " + file);
        } catch (FileNotFoundException expected) {
            checkMessage(expected);
        }
        try (InputStream istream = new FileInputStream(filename)) {
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
        assertFalse(file.exists(), file + " should not exist");
        assertTrue(file.createNewFile(), "can't create " + file);
        assertTrue(file.setReadOnly(), "can't set " + file + " readonly");
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
     */
    @Test
    public final void testFileNotFoundExceptionOutputStreamMissingDir() {
        assertThrows(DirNotFoundException.class, () -> {
            File file = new File(missingDir, "test.txt");
            OutputStream ostream = new FileOutputStream(file);
            ostream.close();
        });
    }

    /**
     * Do you get a FileNotFoundException for RandomAccessFile? Let's check it!.
     *
     * @throws IOException the io exception
     */
    @Test
    public final void testFileNotFoundExceptionRandomAccess() throws IOException {
        String filename = "hugo";
        File file = new File(filename);
        assertFalse(file.exists(), file + " should not exist");
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            fail("FileNotFoundException expected for " + file);
        } catch (FileNotFoundException expected) {
            checkMessage(expected);
        }
        try (RandomAccessFile raf = new RandomAccessFile(filename, "r")) {
            fail("FileNotFoundException expected for " + filename);
        } catch (FileNotFoundException expected) {
            checkMessage(expected);
        }
    }

	private void checkMessage(final IOException expected) {
		log.info(expected.getLocalizedMessage());
		String msg = expected.getMessage();
        assertNotEquals("No such file or directory", msg, "filename missing in '" + msg + "'");
	}

	private void checkMessage(final FileNotFoundException expected) {
	    log.info(expected.getLocalizedMessage());
	    String msg = expected.getMessage().trim();
	    String filename = StringUtils.substringBefore(msg,
                " (No such file or directory)");
	    File file = new File(filename);
	    assertTrue(file.isAbsolute(), file + " should be absolute");
	}

}
