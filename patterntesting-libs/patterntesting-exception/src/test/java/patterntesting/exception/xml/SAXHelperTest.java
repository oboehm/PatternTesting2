/*
 * $Id: SAXHelperTest.java,v 1.7 2016/01/06 20:46:12 oboehm Exp $
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
 * (c)reated 17.12.2008 by oliver (ob@oasd.de)
 */
package patterntesting.exception.xml;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * The Class SAXHelperTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @see org.xml.sax.helpers.XMLReaderFactory
 * @since 17.12.2008
 */
public final class SAXHelperTest {

    /** default class name for testing */
    private static final String driverClassName = "nirwana.org.apache.xerces.parsers.SAXParser";

    /**
     * Test method for
     * {@link patterntesting.exception.xml.SAXHelper#betterSAXException(org.xml.sax.SAXException)}
     * .
     */
    @Test
    public void testBetterSAXException()  {
        SAXException saxe = createSAXException();
        checkSAXException(saxe, saxe.getMessage());
    }

    /**
     * Test better sax exception with system property.
     */
    @Test
    public void testBetterSAXExceptionWithSystemProperty() {
        checkSAXException("something.else", "");
    }

    /**
     * If the classname of the SAX driver is identical to the system property
     * given by propertyName we expect a more detail error message.
     */
    @Test
    public void testBetterSAXExceptionWithWrongSystemProperty() {
        checkSAXException(driverClassName, " - check property \""
                + SAXHelper.DRIVER_PROPERTY + "\"");
    }

    private static void checkSAXException(final String driver, final String suffix) {
        synchronized (System.class) {
            try {
                System.setProperty(SAXHelper.DRIVER_PROPERTY, driver);
                SAXException saxe = createSAXException();
                String expected = saxe.getMessage() + suffix;
                checkSAXException(saxe, expected);
            } finally {
                System.getProperties().remove(SAXHelper.DRIVER_PROPERTY);
            }
        }
    }

    /**
     * Test driver resource.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testDriverResource() throws IOException {
        String service = SAXHelper.DRIVER_RESOURCE;
        InputStream in = ClassLoader.getSystemResourceAsStream(service);
        assertNotNull(in, "can't load " + service);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    in, StandardCharsets.UTF_8));
            String className = reader.readLine();
            reader.close();
            assertEquals("nirwana.test.driver", className);
        } finally {
            in.close();
        }
    }

    /**
     * Test better sax exception with system resource.
     */
    @Test
    public void testBetterSAXExceptionWithSystemResource() {
        String className = "nirwana.test.driver";
        SAXException saxe = createSAXException(className);
        String expected = saxe.getMessage() + " - check system resource \""
            + SAXHelper.DRIVER_RESOURCE + "\"";
        checkSAXException(saxe, expected);
    }

    private static void checkSAXException(final SAXException orig, final String expected) {
        SAXException betterException = SAXHelper.betterSAXException(orig);
        assertEquals(expected, betterException.getMessage());
    }



    /////   Exception Creation Zone   /////////////////////////////////////////

    private static SAXException createSAXException() {
        return createSAXException(driverClassName);
    }

    private static SAXException createSAXException(final String className) {
        ClassNotFoundException e = new ClassNotFoundException(className);
        return new SAXException(
                "SAX2 driver class " + className + " not found", e);
    }

}
