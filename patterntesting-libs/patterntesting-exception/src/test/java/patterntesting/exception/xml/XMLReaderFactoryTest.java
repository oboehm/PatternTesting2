/*
 * Copyright (c) 2008-2019 by Oliver Boehm
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

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.jupiter.api.Assertions.*;


/**
 * The Class XMLReaderFactoryTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 17.12.2008
 */
public final class XMLReaderFactoryTest {

    private static final Logger log = LoggerFactory.getLogger(XMLReaderFactoryTest.class);

    /**
     * We have the wrong system resource in the classpath so this method
     * may throw a SAXException.
     *
     * @see org.xml.sax.helpers.XMLReaderFactory#createXMLReader()
     */
    @Test
    public void testCreateXMLReader() {
        synchronized (System.class) {
            try {
                XMLReader xmlReader = XMLReaderFactory.createXMLReader();
                log.info("xmlReader=" + xmlReader);
                assertNotNull(xmlReader);
            } catch (SAXException saxe) {
                log.info(saxe.getMessage());
                assertThat(saxe.getMessage(), containsString("check system resource"));
            }
        }
    }

    /**
     * Test set property.
     */
    @Test
    public void testSetProperty() {
        synchronized (System.class) {
            String propertyName = SAXHelper.DRIVER_PROPERTY;
            try {
                System.setProperty(propertyName,
                        "org.apache.xerces.parsers.SAXParser");
                checkCreateXMLReader(SAXException.class,
                        "check property \"" + propertyName + "\"");
            } finally {
                System.getProperties().remove(propertyName);
            }
        }
    }

    private void checkCreateXMLReader(final Class<? extends Throwable> clazz,
                                      final String expected) {
        try {
            XMLReaderFactory.createXMLReader();
            fail(clazz.getSimpleName() + "(" + expected + ") expected");
        } catch (Throwable t) {
            log.debug(t.getMessage(), t);
            assertEquals(clazz, t.getClass());
            assertThat(t.getMessage(), endsWith(expected));
        }
    }

}
