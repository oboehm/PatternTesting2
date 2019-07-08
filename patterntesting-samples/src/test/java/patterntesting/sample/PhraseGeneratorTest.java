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
 * (c)reated 19.06.2009 by oliver (ob@aosd.de)
 */
package patterntesting.sample;

import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The Class PhraseGeneratorTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.3 $
 * @since 19.06.2009
 */
public final class PhraseGeneratorTest {

    private static final Log log = LogFactoryImpl.getLog(PhraseGeneratorTest.class);
    private PhraseGenerator phraseGenerator = new PhraseGenerator();
    private File testFile;
    /**
     * This is a test phrase with umlauts from Southern Germany (Suebian):
     * "mir koennet aelles ausser hochdeutsch" (sorry, this can't be
     * translated. It is only an example to see if encoding matters...)
     */
    private static final String TESTPHRASE = "mir k\u00f6nnet \u00e4lles au\u00dfer hochdeutsch";

    /**
     * Setup.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @BeforeEach
    public void setUp() throws IOException {
        testFile = File.createTempFile("phrase", ".txt");
        log.info("using " + testFile + " for testing...");
    }

    /**
     * Test method for {@link PhraseGenerator#writeToStream(File)}.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testWriteStream() throws IOException {
        checkReadWriteStream();
    }

    private void checkReadWriteStream() throws IOException {
        String phrase = phraseGenerator.getPhrase();
        phraseGenerator.writeToStream(testFile);
        String stored = phraseGenerator.readFromStream(testFile);
        assertEquals(phrase, stored);
    }

    /**
     * For testing we use now a phrase from Southern Germany (Suebian):
     * "mir koennet aelles ausser hochdeutsch" (sorry, this can't be
     * translated. It is only an example to see if encoding matters...)
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testWriteStreamUmlaute() throws IOException {
        phraseGenerator.setPhrase(TESTPHRASE);
        checkReadWriteStream();
    }

    /**
     * You know the set test phrase already from
     * {@link #testWriteStreamUmlaute()}.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testWriteTo() throws IOException {
        phraseGenerator.setPhrase(TESTPHRASE);
        phraseGenerator.writeTo(testFile);
        String stored = phraseGenerator.readFrom(testFile);
        assertEquals(TESTPHRASE, stored);
    }

    /**
     * This is a test method for {@link PhraseGenerator#writeToRandomAccessFile(File)}
     * and an example to see what happens if you would read the file with
     * another read method.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testWriteToRandomAccessFile() throws IOException {
        String phrase = phraseGenerator.getPhrase();
        phraseGenerator.writeToRandomAccessFile(testFile);
        String stored = phraseGenerator.readFromRandomAccessFile(testFile);
        assertEquals(phrase, stored);
        log.info("phrase read with readFromRandomAccesFile: " + stored);
        stored = phraseGenerator.readFrom(testFile);
        log.info("phrase read with wrong read method: " + stored);
    }

}
