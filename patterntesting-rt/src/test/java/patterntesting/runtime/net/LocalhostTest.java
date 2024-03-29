/*
 * $Id: LocalhostTest.java,v 1.9 2016/12/18 20:19:39 oboehm Exp $
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
 * (c)reated 30.01.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.net;

import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.jupiter.api.Test;
import patterntesting.runtime.annotation.IntegrationTest;
import patterntesting.runtime.junit.extension.SmokeTestExtension;
import patterntesting.runtime.util.Converter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The Class LocalhostTest.
 *
 * @author oliver
 * @since 1.0 (30.01.2010)
 */
@ExtendWith(SmokeTestExtension.class)
public final class LocalhostTest {

    private static final Logger log = LoggerFactory.getLogger(LocalhostTest.class);

    /**
     * Test method for {@link Localhost#getInetAddresses()}.
     */
    @Test
    public void testGetInetAddresses() {
        Collection<InetAddress> addresses = Localhost.getInetAddresses();
        log.info("host addresses: " + addresses);
        assertFalse(addresses.isEmpty(), "I guess your host should have at least one address!");
    }

    /**
     * Test method for {@link patterntesting.runtime.net.Localhost#matches(java.lang.String)}.
     * @throws UnknownHostException if inet address of localhost can't be calculated
     */
    @Test
    @IntegrationTest("needs too long in some networks (> 2 seconds)")
    public void testMatches() throws UnknownHostException {
        assertTrue(Localhost.matches("127.0.0.1"), InetAddress.getLocalHost() + " should match 127.0.0.1");
    }

    /**
     * Test method for {@link patterntesting.runtime.net.Localhost#matches(java.lang.String)}.
     */
    @Test
    @IntegrationTest("needs too long (> 2 seconds)")
    public void testDontMatches() {
        assertFalse(Localhost.matches("unknown"), "unknown host should not match");
    }

    /**
     * Test method for {@link patterntesting.runtime.net.Localhost#matches(java.lang.String[])}.
     */
    @Test
    @IntegrationTest("needs too long (> 2 seconds)")
    public void testMatchesHosts() {
        String[] hosts = {"unknown", "unknown.nowhere"};
        assertFalse(Localhost.matches(10, TimeUnit.SECONDS, hosts), Converter.toString(hosts) + " should not match");
    }

}
