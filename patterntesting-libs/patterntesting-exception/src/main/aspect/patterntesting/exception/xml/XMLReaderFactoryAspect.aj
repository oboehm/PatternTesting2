/**
 * $Id: XMLReaderFactoryAspect.aj,v 1.2 2016/12/18 21:57:35 oboehm Exp $
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

import org.slf4j.*;
import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.xml.sax.*;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * This aspect enriches some exceptions of the XMLReaderFactory class with
 * some additional hints.
 * 
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 0.9
 */
public aspect XMLReaderFactoryAspect {

    private static final Logger log = LoggerFactory.getLogger(XMLReaderFactoryAspect.class);

    /**
     * SAXExceptions are sometimes hard to read because they don't contain
     * the necessary information in the message which helps you to find the
     * cause of the exception. This advice wraps the createXMLReader() method
     * to enrich the SAXException with some additional hints.
     * 
     * @return the created XMLReader
     * @throws SAXException if the creation fails
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    XMLReader around() throws SAXException :
            call(public static XMLReader XMLReaderFactory.createXMLReader()) {
        try {
            return proceed();
        } catch (SAXException saxe) {
            if (log.isTraceEnabled()) {
                log.trace("handling " + saxe + "...");
            }
            throw SAXHelper.betterSAXException(saxe);
        }
    }

}
