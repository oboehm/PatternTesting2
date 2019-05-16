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
 */
package patterntesting.exception.net;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * This is the JUnit Test for the ConnectExceptionAspect.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 23.03.2009
 */
public class ConnectExceptionTest {

    private static final Logger log = LogManager.getLogger(ConnectExceptionTest.class);

    /**
     * We expect here an ConnectException because we try to connect to a socket
     * which does not exist. But do we get the right (better) message?
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public final void testSocketCreation() throws IOException {
        try {
            Socket socket = new Socket("localhost", 47111);             // SUPPRESS CHECKSTYLE
            socket.close();
            fail("ConnectException expected for localhost:47111");
        } catch (ConnectException expected) {
            checkMessage(expected, "localhost", 47111);
        }
    }

    /**
     * Tests the {@link NoRouteToHostException}. If you create a socket with
     * an IP address and e.g. the network is down you'll get this exception.
     * The message should contain at least the host and port where you wanted
     * to connect to.
     * <p>
     * Maybe you should be offline if you start this test. Or you should be not
     * in a private 10.x.x.x network where the used IP address exists.
     * </p>
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public final void testNoRouteToHostException() throws IOException {
        try {
            Socket socket = new Socket("10.11.12.13", 80);
            socket.close();
            fail("ConnectException expected for 10.11.12.13:80");
        } catch (SocketException expected) {
            log.debug("Connection failed:", expected);
            checkMessage(expected, "10.11.12.13", 80);
        }
    }

    private static void checkMessage(final SocketException expected, final String host,
            final int port) {
        log.info(expected.getLocalizedMessage());
        String msg = expected.getMessage();
        assertTrue(msg.contains(host + ":" + port), "hostname and port missing in '" + msg + "'");
    }

}
