/*
 * $Id: SocketExceptionTest.java,v 1.8 2016/12/18 21:57:35 oboehm Exp $
 *
 * Copyright (c) 2010 by Oliver Boehm
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
 * (c)reated 11.05.2010 by oliver (ob@oasd.de)
 */

package patterntesting.exception.net;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;

import static org.junit.Assert.*;


/**
 * The Class SocketExceptionTest.
 *
 * @author oliver
 * @since 1.0 (11.05.2010)
 */
public final class SocketExceptionTest {

    private static final Logger log = LogManager.getLogger(SocketExceptionTest.class);

    /**
     * Creates a NoRouteToHostException and examines the thrown exception.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testHttpUrlConnection() throws IOException {
        URL url = new URL("http://255.255.255.255");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            connection.getResponseCode();
            fail("NoRouteToHostException expected");
        } catch (SocketException se) {
            checkMessage(se, url);
        }
    }

    private static void checkMessage(final SocketException e, final URL url) {
        String host = url.getHost();
        String msg = e.getMessage();
        assertTrue(host + " not inside '" + msg + "'", StringUtils.contains(
                msg, host));
        log.info("expected message: " + msg);
    }

}

