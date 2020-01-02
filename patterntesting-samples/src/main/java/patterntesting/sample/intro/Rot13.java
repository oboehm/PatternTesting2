/*
 * Copyright (c) 2010-2020 by Oliver Boehm
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
 * (c)reated 17.05.2010 by oliver (ob@oasd.de)
 */

package patterntesting.sample.intro;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * The Rot13 algorithm is a very simple algorithm for crypting.
 * It simple rotates each character by the next 13th character.
 * This means "a" will be replaced by "n", "b" by "o" ...,
 * "m" by "z", "n" by "a" and so on.
 * <p>
 * The original example is inspired from
 * http://www.aosd.de/buecher/AOP_AspectJ/Uebung/Kap11-Instr/loesung.html
 * from the German book "Aspekt-Orientierte Programmierung mit AspectJ 5".
 * </p>
 *
 * @author oliver
 * @since 1.0 (17.05.2010)
 */
public final class Rot13 implements Comparable<Rot13>, Serializable, Cloneable, Cryptable {

    private static final long serialVersionUID = 20111228L;

    /** The StringBuffer is used here only for demo purposes. */
    private StringBuffer crypted;

    /**
     * Default constructor for a new Rot13 object.
     */
    public Rot13() {
        this.crypted = new StringBuffer();
    }

    /**
     * Instantiates a new Rot13 object.
     *
     * @param s the string to crypt
     */
    public Rot13(final String s) {
        this.crypted = new StringBuffer(crypt(s));
    }

    /**
     * Sets the message.
     *
     * @param s the new message
     * @see Cryptable#setMessage(String)
     */
    public void setMessage(final String s) {
        this.crypted = new StringBuffer(crypt(s));
    }

    /**
     * We add the given string to the internal crypted string.
     *
     * @param s the string
     */
    public void add(final String s) {
        this.crypted.append(crypt(s));
    }

    /**
     * Nothing to do - it's already crypted!.
     *
     * @return the already crypted string
     * @see Cryptable#crypt()
     */
    public String crypt() {
        return this.crypted.toString();
    }

    /**
     * Decrypts the crypted string.
     *
     * @return the crypted string decrypted.
     */
    public String decrypt() {
        return decrypt(this.crypted);
    }

    /**
     * Crypts the given string.
     *
     * @param s the string to be crypted
     * @return the crypted string
     */
    public static String crypt(final String s) {
        StringBuilder sbuf = new StringBuilder(s);
        for (int i = 0; i < sbuf.length(); i++) {
            sbuf.setCharAt(i, crypt(sbuf.charAt(i)));
        }
        return sbuf.toString();
    }

    /**
     * Not only the content of the given file will be encrypted but also
     * a new file with the encrypted content will be created. And the filename
     * is the encrypted filename ;-)
     *
     * @param file the input file
     * @return the newly created file
     * @throws IOException if file can't be read or written
     */
    public static File crypt(final File file) throws IOException {
        String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        String cryptedFilename = crypt(file.getName());
        File cryptedFile = new File(file.getParentFile(), cryptedFilename);
        FileUtils.writeStringToFile(cryptedFile, crypt(content), StandardCharsets.UTF_8);
        return cryptedFile;
    }

    /**
     * If you want a to crypt a web site you can use this crypt method here.
     *
     * @param uri e.g. new URI("http://patterntesting.org")
     * @return the crypted web site
     * @throws IOException e.g. if web site is down
     */
    public static String crypt(final URI uri) throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(uri);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httpget, responseHandler);
        httpclient.getConnectionManager().shutdown();
        return crypt(responseBody);
    }

    /**
     * It's the same algorithm as 'crypt'.
     *
     * @param s the string to be decrypted
     * @return the decrypted string
     * @see #crypt(String)
     */
    public static String decrypt(final String s) {
        return crypt(s);
    }

    /**
     * Decrypt.
     *
     * @param sb the sb
     * @return the string
     */
    public static String decrypt(final StringBuffer sb) {
        return decrypt(sb.toString());
    }

    /**
     * Crypts the given character.
     *
     * @param ch the given character
     * @return the crypted character
     */
    public static char crypt(final char ch) {
        char c = ch;
        if (Character.isLetter(c)) {
            char z = ((Character.isLowerCase(c)) ? 'z' : 'Z');
            c += 13;
            if (c > z) {
                c -= 26;
            }
        }
        return c;
    }

    /**
     * For better debugging and logging the crypted string is returned here.
     * @return the crypted string
     */
    @Override
    public String toString() {
        return this.crypted.toString();
    }

    /**
     * Checks if two Rot13 objects has the same (crypted) content.
     *
     * @param other the other object to compare
     * @return true, if successful
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object other) {
        if (other instanceof Rot13) {
            return this.equals((Rot13) other);
        } else {
            return false;
        }
    }

    /**
     * Checks if two Rot13 objects has the same (crypted) content.
     *
     * @param other the other object to compare
     * @return true, if successful
     * @see #equals(Object)
     */
    public boolean equals(final Rot13 other) {
        if (other == null) {
            return false;
        }
        return this.toString().equals(other.toString());
    }

    /**
     * Hash code.
     *
     * @return the hash code
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    /**
     * Compare two objects.
     *
     * @param other the other object
     * @return 0 if
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final Rot13 other) {
        return this.decrypt().compareTo(other.decrypt());
    }

    /**
     * Clone the object.
     *
     * @return the object
     * @see Object#clone()
     */
    @Override
    public Object clone() {
        return new Rot13(this.decrypt());
    }

}

