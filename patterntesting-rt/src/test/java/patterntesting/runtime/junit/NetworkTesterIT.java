/*
 * Copyright (c) 2017-2024 by Oliver Boehm
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
 * (c)reated 26.07.2017 by oboehm (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import patterntesting.runtime.annotation.IntegrationTest;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Originally Junit tests for {@link NetworkTester} class. But because you need
 * a network it is now considered to be more a interation test (suffix IT).
 *
 * @author oboehm
 */
public final class NetworkTesterIT {

    private static final Logger LOG = LoggerFactory.getLogger(NetworkTesterIT.class);

    /**
     * Test method for {@link NetworkTester#assertExists(URI)}.
     */
    @Test
    public void testAssertExists() {
        assumeTrue(NetworkTester.isOnline("github.com.org", 80), "patterntesting.org is offline");
        URI uri = URI.create("https://github.com/oboehm/PatternTesting2");
        NetworkTester.assertExists(uri);
    }

    /**
     * Test method for {@link NetworkTester#assertExists(URI)}.
     */
    @Test
    public void testAssertExistsFile() {
        File file = new File("pom.xml");
        assertTrue(file.exists(), "should exist: " + file);
        NetworkTester.assertExists(file.toURI());
    }

    @Test
    public void testAssertExistsFalse() {
        File file = new File("nirwana");
        assertFalse(file.exists(), "should not exist: " + file);
        assertFalse(NetworkTester.exists(file.toURI()));
    }

    /**
     * Test method for {@link NetworkTester#assertOnline(InetSocketAddress)}.
     * To get a port for testing we ask the 'netstat' command.
     */
    @Test
    public void testAssertOnlineInetSocketAddress() {
        checkAssertOnline(InetSocketAddress.createUnresolved("127.0.0.1", 11000));
    }

    /**
     * Test method for {@link NetworkTester#assertOffline(InetSocketAddress)}.
     * To get a port for testing we ask the 'netstat' command.
     */
    @Test
    @IntegrationTest
    public void testAssertOfflineInetSocketAddress() {
        checkAssertOnline(InetSocketAddress.createUnresolved("127.0.0.2", 11001));
    }

    /**
     * Test method for {@link NetworkTester#assertOnline(InetAddress, int)}.
     * To get a port for testing we asked the 'netstat' command.
     */
    @Test
    public void testAssertOnlineInetAddressPort() {
        checkAssertOnline(InetAddress.getLoopbackAddress(), 135);
    }

    private static void checkAssertOnline(InetAddress address, int port) {
        checkAssertOnline(new InetSocketAddress(address, port));
    }

    private static void checkAssertOnline(InetSocketAddress address) {
        LOG.info("Checking {}...", address);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Socket> fs = executor.submit(() -> new Socket(address.getHostName(), address.getPort()));
        InetAddress inetAddr = address.getAddress();
        try (Socket socket = fs.get(2, TimeUnit.SECONDS)) {
            LOG.info("{} is online, because {} could be created.", address, socket);
            if (inetAddr == null) {
                NetworkTester.assertOnline(address);
            } else {
                NetworkTester.assertOnline(inetAddr);
            }
        } catch (IOException | TimeoutException | InterruptedException | ExecutionException ex) {
            LOG.info("{} is probably offline ({}).", address, ex.toString());
            if (inetAddr == null) {
                NetworkTester.assertOffline(address);
            } else {
                NetworkTester.assertOffline(inetAddr, address.getPort());
            }
        }
    }

    /**
     * Test method for {@link NetworkTester#assertOnline(InetAddress)}.
     */
    @Test
    public void testAssertOnlineInetAddress() {
        InetAddress localHost = InetAddress.getLoopbackAddress();
        NetworkTester.assertOnline(localHost, 100, TimeUnit.MILLISECONDS);
        NetworkTester.assertOnline(localHost);
    }

    @Test
    public void testAssertOnlineInetAdressPort() throws UnknownHostException {
        assumeTrue(NetworkTester.isOnline("patterntesting.org", 80), "patterntesting.org is offline");
        InetAddress addr = InetAddress.getByName("patterntesting.org");
        NetworkTester.assertOnline(addr, 80);
    }

    @Test
    public void testAssertOnlineInetSocketAdress() {
        assumeTrue(NetworkTester.isOnline("patterntesting.org", 80), "patterntesting.org is offline");
        InetSocketAddress addr = InetSocketAddress.createUnresolved("patterntesting.org", 80);
        NetworkTester.assertOnline(addr);
    }

    @Test
    public void testAssertOfflineHost() throws UnknownHostException {
        InetAddress addr = InetAddress.getByName("128.0.0.0");
        NetworkTester.assertOffline(addr, 10, TimeUnit.MILLISECONDS);
    }

    /**
     * Unit test for issue #51.
     */
    @Test
    public void testAssertOnlineURI() {
        NetworkTester.assertOnline(URI.create("https://github.com/oboehm/PatternTesting2"));
    }

}
