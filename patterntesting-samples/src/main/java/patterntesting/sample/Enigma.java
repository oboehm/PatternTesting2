/*
 * $Id: Enigma.java,v 1.5 2016/01/06 22:40:00 oboehm Exp $
 *
 * Copyright (c) 2009 by Oliver Boehm
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

import java.security.GeneralSecurityException;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import patterntesting.runtime.annotation.*;

/**
 * The Enigma was a cipher machine of the Germans during World War II (see
 * <a href="http://en.wikipedia.org/wiki/Enigma_machine">Enigma machine</a>).
 * This class is only a demo how to use the different cipher engines provided by
 * the JDK. And it is an example to show the use of the <tt>@ProfileMe</tt>
 * annotation.
 * <p>
 * See <a href=
 * "http://www.owasp.org/index.php/Using_the_Java_Cryptographic_Extensions">
 * Using the Java Cryptographic Extensions</a>.
 * </p>
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.5 $
 * @since 10.06.2009
 */
public final class Enigma {

    /** To avoid instantiation of this utility class. */
    private Enigma() {}

    /**
     * This is a very simple example of a crypt routine. It uses a simple
     * XOR with the given password. As far as I know this was the algorithm
     * on the VAX in the good old days as processor speed was measured in kHz.
     * <p>
     * This is also an examples for <code>@ProfileMe</code> together with the
     * <code>@DontLogMe</code> annotation for the given password.
     * Normally you should never log a password in producution.
     * </p>
     *
     * @param bytes input data which is crypted after the call
     * @param password the secret password with shouldn't appear in a log
     * @return the crypted data
     */
    @ProfileMe
    public static byte[] simpleCrypt(final byte[] bytes,
            @DontLogMe final String password) {
        byte[] key = password.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (bytes[i] ^ key[i % key.length]);
        }
        return bytes;
    }

    /**
     * Encrypt.
     *
     * @param bytes the bytes to be encrypted
     * @param password the password
     * @param algorithm "DES", "DESede" or "TripleDES"
     * @return the crypted bytes
     * @throws GeneralSecurityException if crypt fails
     */
    @ProfileMe
    public static byte[] encrypt(final byte[] bytes,
            @DontLogMe final String password, final String algorithm)
            throws GeneralSecurityException {
        return crypt(bytes, password, algorithm, Cipher.ENCRYPT_MODE);
    }

    /**
     * Decrypt.
     *
     * @param bytes the bytes to be decrypted
     * @param password the password
     * @param algorithm "DES", "DESede" or "TripleDES"
     * @return the crypted bytes
     * @throws GeneralSecurityException if crypt fails
     */
    @ProfileMe
    public static byte[] decrypt(final byte[] bytes,
            @DontLogMe final String password, final String algorithm)
            throws GeneralSecurityException {
        return crypt(bytes, password, algorithm, Cipher.DECRYPT_MODE);
    }

    private static byte[] crypt(final byte[] bytes, final String password,
            final String algorithm, final int mode)
            throws GeneralSecurityException {
        SecretKey key = getSecretKey(password, algorithm);
        Cipher c = Cipher.getInstance(algorithm);
        c.init(mode, key);
        return c.doFinal(bytes);
    }

    /**
     * The different algorithm needs different password length.
     * <dl>
     *  <dt>"DES"</dt>
     *      <dd>exactly 8 characters</dd>
     *  <dt>"DESede" or "TripleDES"</dt>
     *      <dd>24 bytes</dd>
     * </dl>
     *
     * @param password "DES", "DESede" or "TripleDES"
     * @param algorithm
     * @return
     */
    private static SecretKey getSecretKey(String password,
            final String algorithm) {
        password += "xxxxxxxxxxxxxxxxxxxxxxxx";
        if (algorithm.equals("DES")) {
            password = password.substring(0, 8);
        } else if (algorithm.equals("DESede") || algorithm.equals("TripleDES")) {
            password = password.substring(0, 24);
        }
        return new SecretKeySpec(password.getBytes(), algorithm);
    }

}
