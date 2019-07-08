/*
 * Copyright (c) 2009-2019 by Oliver Boehm
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
 * (c)reated 10.06.2009 by oliver (ob@aosd.de)
 */
package patterntesting.sample;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import patterntesting.runtime.annotation.ProfileMe;
import patterntesting.runtime.monitor.ProfileStatistic;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The Class EnigmaTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.4 $
 * @since 10.06.2009
 */
public final class EnigmaTest {

    /** We use a test array of 1 MB here. */
    private static byte[] testdata = new byte[0x100000];

    /**
     * Sets the up testdata.
     */
    @BeforeAll
    public static void setUpTestdata() {
        for(int i = 0; i < testdata.length; i++) {
            testdata[i] = 'x';
        }
    }

    /**
     * Tear down after class.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @AfterAll
    public static void tearDownAfterClass() throws IOException {
        ProfileStatistic.getInstance().dumpStatistic();
    }

    /**
     * Test simple crypt.
     */
    @Test
    public void testSimpleCrypt() {
        String s = "message for testing";
        byte[] crypted = Enigma.simpleCrypt(s.getBytes(), "secret");
        byte[] decrypted = Enigma.simpleCrypt(crypted, "secret");
        assertEquals(s, new String(decrypted));
    }

    /**
     * Test simple crypt performance.
     */
    @ProfileMe
    @Test
    public void testSimpleCryptPerformance() {
        Enigma.simpleCrypt(testdata, "secret");
        Enigma.simpleCrypt(testdata, "secret");
    }

    /**
     * Test crypt des.
     *
     * @throws GeneralSecurityException the general security exception
     */
    @Test
    public void testCryptDES() throws GeneralSecurityException {
        checkCrypt("DES");
    }

    /**
     * Test crypt de sede.
     *
     * @throws GeneralSecurityException the general security exception
     */
    @Test
    public void testCryptDESede() throws GeneralSecurityException {
        checkCrypt("DESede");
    }

    /**
     * Test crypt triple des.
     *
     * @throws GeneralSecurityException the general security exception
     */
    @Test
    public void testCryptTripleDES() throws GeneralSecurityException {
        checkCrypt("TripleDES");
    }

    /**
     * For symetric crypting you can use the same password for encrypting
     * and decrypting.
     *
     * @throws GeneralSecurityException
     */
    private void checkCrypt(final String algorithm) throws GeneralSecurityException {
        String s = "message for testing";
        byte[] encrypted = Enigma.encrypt(s.getBytes(), "secret", algorithm);
        byte[] decrypted = Enigma.decrypt(encrypted,    "secret", algorithm);
        assertEquals(s, new String(decrypted));
    }

    /**
     * Test crypt performance des.
     *
     * @throws GeneralSecurityException the general security exception
     */
    @ProfileMe
    @Test
    public void testCryptPerformanceDES() throws GeneralSecurityException {
        checkCryptPerformance("DES");
    }

    /**
     * Test crypt performance de sede.
     *
     * @throws GeneralSecurityException the general security exception
     */
    @ProfileMe
    @Test
    public void testCryptPerformanceDESede() throws GeneralSecurityException {
        checkCryptPerformance("DESede");
    }

    private void checkCryptPerformance(final String algorithm)
            throws GeneralSecurityException {
        byte[] crypted = Enigma.encrypt(testdata, "secret", algorithm);
        Enigma.decrypt(crypted, "secret", algorithm);
    }

}
