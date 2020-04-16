/*
 * Copyright (c) 2017-2020 by Oliver Boehm
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import patterntesting.runtime.annotation.IntegrationTest;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Junit tests for {@link NetworkTester} class.
 *
 * @author oboehm
 */
public final class NetworkTesterTest {

    private static final Logger LOG = LogManager.getLogger(NetworkTesterTest.class);

    /**
     * Test method for {@link NetworkTester#assertExists(URI)}.
     */
    @Test
    public void testAssertExists() {
        assumeTrue(NetworkTester.isOnline("patterntesting.org", 80), "patterntesting.org is offline");
        URI uri = URI.create("http://patterntesting.org");
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
     * To get a port for testing we ask the 'netstat' command.
     */
    @Test
    public void testAssertOnlineInetAddressPort() {
        checkAssertOnline(InetAddress.getLoopbackAddress(), 135);
    }

    private static void checkAssertOnline(InetAddress address, int port) {
        checkAssertOnline(new InetSocketAddress(address, port));
    }

    private static void checkAssertOnline(InetSocketAddress address) {
        InetAddress inetAddr = address.getAddress();
        try (Socket socket = new Socket(address.getHostName(), address.getPort())) {
            LOG.debug("{} is online, because {} could be created.", address, socket);
            if (inetAddr == null) {
                NetworkTester.assertOnline(address);
            } else {
                NetworkTester.assertOnline(inetAddr);
            }
        } catch (IOException ioe) {
            LOG.debug("{} is probably offline:", address, ioe);
            if (inetAddr == null) {
                NetworkTester.assertOffline(address);
            } else {
                NetworkTester.assertOffline(inetAddr, address.getPort());
            }
        }
    }

    /**
     * Test method for {@link NetworkTester#assertOnline(InetAddress)}.
     *
     * @throws UnknownHostException if localhost is unknown
     */
    @Test
    public void testAssertOnlineInetAddress() throws UnknownHostException {
        InetAddress localHost = InetAddress.getLocalHost();
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

}
