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
 * (c)reated 01.04.2009 by oliver (ob@aosd.de)
 */
package patterntesting.check.ct.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.slf4j.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import patterntesting.annotation.check.ct.SuppressEncodingWarning;

/**
 * JUnit and compile tests for the {@link EncodingAspect}.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.4 $
 * @since 09-Aug-2012
 */
public final class EncodingTest {

    private static final Logger log = LoggerFactory.getLogger(EncodingTest.class);
    private static final String INPUT = "Hello world!";
    private InputStream istream;
    private ByteArrayOutputStream ostream;
    
    /**
     * Sets up the streams for testing.
     */
    @BeforeEach
    public void setUpStreams() {
        istream = new ByteArrayInputStream(INPUT.getBytes(StandardCharsets.US_ASCII));
        ostream = new ByteArrayOutputStream();
    }
    
    /**
     * Close streams.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @AfterEach
    public void closeStreams() throws IOException {
        istream.close();
        ostream.close();
    }

    /**
     * Here we want to see what happens if we call the undefinedEncoding()
     * method definde below.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testUndefinedEncoding() throws IOException {
        undefinedEncoding(istream, ostream);
        checkOutput(ostream);
    }
    
    /**
     * Here we want to see what happens if we call the definedEncoding()
     * method definde below.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testDefinedEncoding() throws IOException {
        definedEncoding(istream, ostream, "UTF-8");
        checkOutput(ostream);
    }
    
    /**
     * This is an example for undefined encoding.
     * Remove the {@link SuppressEncodingWarning} annotation below to see
     * the warnings.
     *
     * @param istream the istream
     * @param ostream the ostream
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @SuppressEncodingWarning
    private static void undefinedEncoding(final InputStream istream, final OutputStream ostream) throws IOException {
        Reader ir = new InputStreamReader(istream);
        Writer ow = new OutputStreamWriter(ostream);
        byte[] ba = "PatternTesting".getBytes();
        String s = new String(ba);
        log.info("Hello {}", s);
        IOUtils.copy(istream, ostream);
        ir.close();
        ow.close();
    }

    /**
     * And this is an example for defined encoding.
     *
     * @param istream the istream
     * @param ostream the ostream
     * @param encoding the encoding
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void definedEncoding(final InputStream istream, final OutputStream ostream, final String encoding)
            throws IOException {
        Reader ir = new InputStreamReader(istream, encoding);
        Writer ow = new OutputStreamWriter(ostream, encoding);
        byte[] ba = "PatternTesting".getBytes(encoding);
        String s = new String(ba, encoding);
        log.info("Hello {}", s);
        IOUtils.copy(istream, ostream);
        ir.close();
        ow.close();
    }

    private static void checkOutput(ByteArrayOutputStream ostream) {
        String output = ostream.toString();
        log.info("input: \"{}\" / output: \"{}\"", INPUT, ostream);
        assertEquals(INPUT, output);
    }

}
