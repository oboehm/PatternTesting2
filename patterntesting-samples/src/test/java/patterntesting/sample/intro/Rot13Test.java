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
 * (c)reated 20.05.2010 by oliver (ob@oasd.de)
 */

package patterntesting.sample.intro;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import patterntesting.runtime.annotation.Broken;
import patterntesting.runtime.annotation.IntegrationTest;
import patterntesting.runtime.annotation.RunTestOn;
import patterntesting.runtime.annotation.SmokeTest;
import patterntesting.runtime.junit.CloneableTester;
import patterntesting.runtime.junit.ComparableTester;
import patterntesting.runtime.junit.ObjectTester;
import patterntesting.runtime.junit.SerializableTester;
import patterntesting.runtime.junit.extension.SmokeTestExtension;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The Class Rot13Test.
 *
 * @author oliver
 * @since 1.0 (20.05.2010)
 */
@ExtendWith(SmokeTestExtension.class)
public class Rot13Test {

    private static final Logger log = LoggerFactory.getLogger(Rot13Test.class);

    /**
     * Let's start with a simple test for the crypt method for a single
     * character.
     */
    @Test
    public final void testCryptChar() {
        assertEquals('n', Rot13.crypt('a'));
        assertEquals('b', Rot13.crypt('o'));
        assertEquals('m', Rot13.crypt('z'));
        assertEquals('n', Rot13.crypt('a'));
    }

    /**
     * Tests the constructor and toString method.
     */
    @Test
    public final void testRot13() {
        Rot13 r = new Rot13("hello");
        assertEquals("uryyb", r.toString());
    }

    /**
     * Class under test for String crypt(String).
     */
    @SmokeTest
    @Test
    public final void testCryptString() {
        String s = Rot13.crypt("Hello");
        assertEquals("Uryyb", s);
    }

    /**
     * This example is extracted from
     * <a href="http://de.wikipedia.org/wiki/ROT13">Wikipedia</a>.
     */
    @Test
    public final void testCryptSentence() {
        String s = Rot13
                .crypt("Dies ist ein Beispieltext, um ROT13 zu demonstrieren.");
        assertEquals("Qvrf vfg rva Orvfcvrygrkg, hz EBG13 mh qrzbafgevrera.", s);
    }

    /**
     * Test with uppercase letters only.
     */
    @Test
    public final void testCryptUC() {
        String s = Rot13.crypt("HELLO");
        assertEquals("URYYB", s);
    }

    /**
     * Test crypt and decrypt.
     */
    @SmokeTest
    @Test
    public final void testCryptDecrypt() {
        String s = Rot13.crypt("Hello");
        assertEquals("Hello", Rot13.decrypt(s));
        assertEquals("Hello", Rot13.crypt(s));
    }

    @Test
    public void testSetMessage() {
        Rot13 r = new Rot13();
        r.setMessage("Hello");
        assertEquals("Hello", r.decrypt());
        assertEquals(r.crypt(), r.toString());
    }

    /**
     * This ist Bob's version of testing the crypt method for files.
     * Unfortunately he uses a file which is only present on his computer.
     * So we disable this test during normal testing.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Broken(till = "01-Jul-2026")
    @Test
    public final void testCryptBobsFile() throws IOException {
        File file = new File("C:/Temp/bobsfile.txt");
        File uncryptedFile = Rot13.crypt(file);
        String content = FileUtils.readFileToString(uncryptedFile, StandardCharsets.UTF_8);
        assertEquals("Hello World!", content);
    }

    /**
     * This is now Bill's improved version of testing the crypt method for
     * files. Unfortunately this test runs only on a Windows machine :-(
     *
     * @throws IOException on a Linux or Mac system
     */
    @RunTestOn(osName = "Windows")
    @Test
    public final void testCryptBillsFile() throws IOException {
        File tempFile = new File("C:/Temp", "billsfile.txt");
        checkCrypt(tempFile);
    }

    /**
     * Here we test the crypting of a file. First we will create one in
     * the tmp directory for testing before we crypt it.
     *
     * @throws IOException if test file can't be created.
     */
    @Test
    public final void testCryptFile() throws IOException {
        File uncryptedFile = File.createTempFile("hello", ".txt");
        checkCrypt(uncryptedFile);
    }

    private void checkCrypt(final File uncryptedFile) throws IOException {
        String uncrypted = "Hello\nWorld!\n";
        writeStringToFile(uncryptedFile, uncrypted);
        log.info(uncryptedFile + " created...");
        File cryptedFile = Rot13.crypt(uncryptedFile);
        String crypted = FileUtils.readFileToString(cryptedFile, StandardCharsets.UTF_8);
        log.info("...and crypted to " + cryptedFile + " with content:\n" + crypted);
        assertEquals(uncrypted, Rot13.decrypt(crypted));
    }

    /**
     * Here we test the crypting of the content of a web site.
     *
     * @throws IOException if web site is not reachable
     * @throws URISyntaxException should not happen
     */
    @IntegrationTest("online access needed")
    @Test
    public final void testCryptURI() throws IOException, URISyntaxException {
        String crypted = Rot13.crypt(new URI("http://patterntesting.org")).trim();
        assertThat(crypted.length(), greaterThan(80));
        log.info("crypted=" + crypted.substring(0, 80) + "...");
        assertEquals('<', crypted.charAt(0));
    }

    /**
     * Just to have another integration test for another URI.
     *
     * @throws IOException if web site is not reachable
     * @throws URISyntaxException should not happen
     */
    @IntegrationTest("online access needed")
    @Test
    public final void testCryptAnotherUri() throws IOException, URISyntaxException {
        String s = Rot13.crypt(new URI("http://patterntesting.de")).trim();
        assertNotNull(s);
    }

    /**
     * If you wonder why we don't use FileUtils.writeStringToFile(File, String)
     * here: the FileUtils version would create a missing directory which is
     * not what we want here.
     *
     * @param file the file
     * @param s the string
     * @throws IOException e.g. a FileNotFoundException if dir doesn't exit
     */
    private void writeStringToFile(final File file, final String s) throws IOException {
        OutputStream ostream = new FileOutputStream(file);
        IOUtils.write(s, ostream, StandardCharsets.UTF_8);
        ostream.close();
    }

    /**
     * Test method for {@link Rot13#add(String)}.
     */
    @Test
    public final void testAdd() {
        Rot13 x = new Rot13("hell");
        x.add("o");
        assertEquals("hello", x.decrypt());
    }

    /**
     * Two Rot13 objects should be equals if they have the same content.
     * This is an example how the ObjectTester can used to check equatility.
     */
    @Test
    public final void testEquals() {
        Rot13 x = new Rot13("test");
        Rot13 y = new Rot13("test");
        assertEquals(x, y);
        ObjectTester.assertEquals(x, y);
    }

    /**
     * The {@link Rot13#compareTo(Rot13)} method should return a value &lt; 0
     * if the orignal message <code>a</code> is alphabetically before message
     * <code>b</code>.
     */
    @Test
    public final void testCompareTo() {
        Rot13 a = new Rot13("a");
        Rot13 b = new Rot13("b");
        Rot13 copy = new Rot13("a");
        assertEquals(0, a.compareTo(copy));
        int abCompare = a.compareTo(b);
        assertThat(abCompare, lessThan(0));
        int baCompare = b.compareTo(a);
        assertEquals(-abCompare, baCompare);
        // the same tests with the ComparableTester
        ComparableTester.assertCompareTo(a, copy);
        ComparableTester.assertCompareTo(a, b);
    }

    /**
     * Tests if the Rot13 class is serializable.
     *
     * @throws NotSerializableException the not serializable exception
     */
    @Test
    public void testSerializable() throws NotSerializableException {
        Rot13 x = new Rot13("serialize me");
        SerializableTester.assertSerialization(x);
    }

    /**
     * Test cloning.
     */
    @Test
    public void testCloning() {
        Rot13 orig = new Rot13("Dolly");
        CloneableTester.assertCloning(orig);
        Rot13 clone = (Rot13) orig.clone();
        clone.add(" Dollar");
        assertNotEquals(clone, orig);
    }

}

