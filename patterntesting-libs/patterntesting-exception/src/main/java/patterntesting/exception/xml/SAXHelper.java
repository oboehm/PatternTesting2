/*
 * $Id: SAXHelper.java,v 1.11 2016/12/18 21:57:35 oboehm Exp $
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

import java.io.*;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.*;
import org.xml.sax.SAXException;

import patterntesting.annotation.check.runtime.MayReturnNull;

/**
 * The class SAXHelper was introduced to be able to provide better
 * SAXExceptions.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.11 $
 * @since 17.12.2008
 */
public final class SAXHelper {

    private static final Logger LOG = LoggerFactory.getLogger(SAXHelper.class);

    /** The Constant DRIVER_PROPERTY. */
    public static final String DRIVER_PROPERTY = "org.xml.sax.driver";

    /** The Constant DRIVER_RESOURCE. */
    public static final String DRIVER_RESOURCE = "META-INF/services/" + DRIVER_PROPERTY;

    /** Utility class - no need to instantiate it. */
    private SAXHelper() {}

    /**
     * Transforms a SAXException with too less infos into a better SAXException
     * with more infos.
     * <p>
     * NOTE: Amonst other things the system property "org.xml.sax.driver" are
     * used to get a better exception. Normally this should be synchronized
     * because other threads may changes this system property so that this
     * method may misinterpret this property. But it is very unlikely (except
     * for testing) that this happen. So no synchronization is done to avoid
     * blocked threads. So in some very, very rare circumstances the "better
     * SAXException" may be perhaps confuses you (or the tester ;-).
     * </p>
     *
     * @param saxe the saxe
     * @return the sAX exception
     */
    public static SAXException betterSAXException(final SAXException saxe) {
        String msg = saxe.getMessage();
        String className = parseMessage(msg);
        if (className == null) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("unknown message '" + msg + "' - returning " + saxe
                        + "...");
            }
            return saxe;
        }
        if (isDriverPropertySetWith(className)) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("conflict with property " + DRIVER_PROPERTY + "="
                        + className + " detected");
            }
            msg = msg + " - check property \"" + DRIVER_PROPERTY + "\"";
            return betterSAXException(saxe, msg);
        } else if (isDriverResourceSetWith(className)) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("conflict with resource " + DRIVER_RESOURCE + "="
                        + className + " detected");
            }
            msg = msg + " - check system resource \"" + DRIVER_RESOURCE + "\"";
            return betterSAXException(saxe, msg);
        } else {
            return saxe;
        }
    }

    /**
     * Better sax exception.
     *
     * @param saxe the saxe
     * @param newMessage the new message
     *
     * @return the sAX exception
     */
    public static SAXException betterSAXException(final SAXException saxe, final String newMessage) {
        Exception cause = saxe.getException();
        SAXException betterException = new SAXException(newMessage, cause);
        betterException.setStackTrace(saxe.getStackTrace());
        return betterException;
    }

    @MayReturnNull
    private static String parseMessage(final String msg) {
        String prefix = "SAX2 driver class ";
        if (msg.startsWith(prefix)) {
            return StringUtils.substringBetween(msg, prefix, " ");
        } else {
            return null;
        }
    }

    private static boolean isDriverPropertySetWith(final String className) {
        String prop = System.getProperty(DRIVER_PROPERTY);
        return className.equals(prop);
    }

    private static boolean isDriverResourceSetWith(final String className) {
        InputStream in = ClassLoader.getSystemResourceAsStream(DRIVER_RESOURCE);
        if (in == null) {
            return false;
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    in, "UTF8"));
            String driver = reader.readLine();
            reader.close();
            if (StringUtils.isBlank(driver)) {
                LOG.warn("no classname found in " + DRIVER_RESOURCE);
                return false;
            }
            return driver.equals(className);
        } catch (IOException ioe) {
            LOG.warn("Cannot read " + DRIVER_RESOURCE + ":", ioe);
            return false;
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

}
