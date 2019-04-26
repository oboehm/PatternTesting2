/*
 * $Id: ZipExceptionTest.java,v 1.11 2016/12/18 21:57:35 oboehm Exp $
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
 * (c)reated 10.11.2010 by oliver (ob@oasd.de)
 */

package patterntesting.exception.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * JUnit test for ZipExceptionAspect.
 *
 * @author oliver
 * @since 1.1 (10.11.2010)
 */
public final class ZipExceptionTest {

    private static final Logger log = LogManager.getLogger(ZipExceptionTest.class);
    private static File emptyFile;
    private static File corruptFile;
    private static File validFile;

    /**
     * Creates a corrupt zip file for testing.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @BeforeAll
    public static void setupZipFiles() throws IOException {
        emptyFile = File.createTempFile("empty", ".zip");
        assertTrue(emptyFile.exists(), emptyFile + " can't be created");
        log.info(emptyFile + " created for testing");
        corruptFile = new File("src/test/resources/patterntesting/exception/io/corrupt.zip");
        assertTrue(corruptFile.exists(), corruptFile + " does not exist");
        validFile = new File("src/test/resources/patterntesting/exception/io/valid.zip");
        assertTrue(validFile.exists(), validFile + " does not exist");
    }

    /**
     * The constructor for a corrupt or empty Zip file fails. But the thrown
     * ZipException should contain the corrupt filename of the Zip file.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testEmptyZipFile() throws IOException {
        checkFailingConstructor(emptyFile);
    }

    /**
     * The constructor for a corrupt or empty Zip file fails. But the thrown
     * ZipException should contain the corrupt filename of the Zip file.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testCorruptZipFile() throws IOException {
        checkFailingConstructor(corruptFile);
    }

    /**
     * Also for a non existing zip file we want to have the name in the thrown
     * exception.
     */
    @Test
    public void testNotExistingZipFile() {
        checkFailingConstructor(new File("does.not.exist"));
    }

    private static void checkFailingConstructor(final File file) {
        checkFailingZipFileCtor(file);
        checkFailingJarFileCtor(file);
    }

    private static void checkFailingZipFileCtor(final File file) {
        try {
            ZipFile zip = new ZipFile(file);
            zip.close();
            fail("ZipException or IOException expected");
        } catch (IOException expected) {
            checkException(expected, file);
        }
    }

    private static void checkFailingJarFileCtor(final File file) {
        try {
            JarFile jar = new JarFile(file);
            jar.close();
            fail("ZipException or IOException expected");
        } catch (IOException expected) {
            checkException(expected, file);
        }
    }

    /**
     * The getInputStream() method should fail. The thrown ZipException should
     * contain the corrupt filename of the Zip file.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testGetInputStreamZipFile() throws IOException {
        ZipFile zipFile = new ZipFile(validFile);
        ZipEntry dummy = new ZipEntry("dummy");
        zipFile.close();
        try {
            zipFile.getInputStream(dummy);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            checkException(expected, validFile);
        }
    }

    /**
     * The getInputStream() method should fail. The thrown ZipException should
     * contain the corrupt filename of the Zip file.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testGetInputStreamJarFile() throws IOException {
        JarFile jarFile = new JarFile(validFile);
        ZipEntry dummy = new ZipEntry("dummy");
        jarFile.close();
        try {
            jarFile.getInputStream(dummy);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            checkException(expected, validFile);
        }
    }

    /**
     * Some methods can throw an IllegalStateException. This exception should
     * contain the filename of the zip file.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testIllegalStateException() throws IOException {
        ZipFile zipFile = new ZipFile(validFile);
        zipFile.close();
        try {
            zipFile.getEntry("dummy");
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            checkException(expected, validFile);
        }
    }

    private static void checkException(final Exception expected, final File file) {
        String msg = expected.getMessage();
        log.info("msg=\"" + msg + '"');
        assertTrue(msg.contains(file.getPath()), file + " not part of: " + msg);
    }

    /**
     * Deletes the corrupted zip file.
     */
    @AfterAll
    public static void teardownZipFiles() {
        emptyFile.delete();
        log.info(emptyFile + " deleted");
    }

}

