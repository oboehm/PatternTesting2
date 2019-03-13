/*
 * $Id: XMLReaderFactoryTest.java,v 1.11 2016/12/18 21:57:35 oboehm Exp $
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

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import static org.junit.Assert.*;


/**
 * The Class XMLReaderFactoryTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.11 $
 * @since 17.12.2008
 */
public final class XMLReaderFactoryTest {

    private static final Logger log = LogManager
            .getLogger(XMLReaderFactoryTest.class);

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
                assertTrue(StringUtils
                        .contains(saxe.getMessage(), "check system resource"));
            }
        }
    }

    /**
     * Test set property.
     *
     * @throws SAXException the SAX exception
     */
    @Test
    public void testSetProperty() throws SAXException {
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
            String msg = t.getMessage();
            assertTrue('"' + msg + "\" does not end with \"" + expected + '"',
                    msg.endsWith(expected));
        }
    }

}
