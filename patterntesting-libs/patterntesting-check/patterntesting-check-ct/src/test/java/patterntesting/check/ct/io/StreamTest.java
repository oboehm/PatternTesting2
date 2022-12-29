/*
 * $Id: StreamTest.java,v 1.4 2016/12/18 21:58:55 oboehm Exp $
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
 * (c)reated 01.04.2009 by oliver (ob@aosd.de)
 */
package patterntesting.check.ct.io;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Since JDK 1.1 you should use the Writer and Reader classes and not the
 * Stream classes. So you should get a warning if you try to use the old
 * Stream classes. The only exception may be the PrintStream class because
 * System.out and System.err depends on it.
 * <p>
 * Since 1.3 the warning of the use of Stream classes must be explicitely
 * enabled because there are some reasons to use the Stream classes. E.g.
 * if you do not want to read text but bytes (binary data). You can
 * enable it with the <code>@EnableStreamWarning</code> annotation.
 * </p>
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.4 $
 * @since 01.04.2009
 */
public final class StreamTest {

    private static final Logger log = LoggerFactory.getLogger(StreamTest.class);
    private static File tmpfile;
    private static final String PHRASE = "Hello World!\n";
    private static final int PHRASE_LENGTH = PHRASE.length();

    /**
     * Sets the up before class.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @BeforeAll
    public static void setUpBeforeClass() throws IOException {
        tmpfile = File.createTempFile("test", ".txt");
    }

    /**
     * Tear down after class.
     */
    @AfterAll
    public static void tearDownAfterClass() {
        if (!tmpfile.delete()) {
            log.warn(tmpfile + " can't be deleted");
        }
    }

    /**
     * Test use stream.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testUseStream() throws IOException {
        writeStream(PHRASE);
        String read = readStream();
        assertEquals(PHRASE, read);
    }

    /**
     * If you'll use <code>@EnableStreamWarning</code> you will see a warning
     * for the use of a Stream class.
     *
     * @param phrase the phrase
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    //@EnableStreamWarning
    protected static void writeStream(final String phrase) throws IOException {
        OutputStream ostream = new FileOutputStream(tmpfile);
        ostream.write(phrase.getBytes(StandardCharsets.US_ASCII));
        ostream.close();
    }

    /**
     * If you'll use <code>@EnableStreamWarning</code> you will see a warning
     * for the use of a Stream class.
     *
     * @return the string
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    //@EnableStreamWarning
    protected static String readStream() throws IOException {
        InputStream istream = new FileInputStream(tmpfile);
        byte[] buffer = new byte[PHRASE_LENGTH];
        int n = istream.read(buffer);
        istream.close();
        if (n <= 0) {
            throw new IOException("nothing read from " + tmpfile);
        }
        return new String(buffer, StandardCharsets.US_ASCII);
    }

}
