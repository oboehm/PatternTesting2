/*
 * $Id: TestOnTest.java,v 1.6 2016/12/18 20:19:38 oboehm Exp $
 *
 * Copyright (c) 2011 by Oliver Boehm
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
 * (c)reated 20.11.2011 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit.internal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Time;

import org.junit.Test;

/**
 * JUnit test of {@link TestOn}.
 *
 * @author oliver
 * @since 1.1 (20.11.2011)
 */
public class TestOnTest {

    private final TestOn testOn = new TestOn();

    /**
     * Test method for {@link TestOn#matches()} with hosts attribute set.
     *
     * @throws UnknownHostException the unknown host exception
     */
    @Test
    public void testMatchesLocalhost() throws UnknownHostException {
        InetAddress localhost = InetAddress.getLocalHost();
        String hostname = localhost.getHostName();
        String[] hostpatterns = { hostname.substring(0, hostname.length() - 2) + "*" };
        testOn.setHosts(hostpatterns);
        assertTrue('"' + hostpatterns[0] + "\" does not match " + localhost, testOn.matches());
    }

    /**
     * Test method for {@link TestOn#matches()} with time attribute set to
     * whole day.
     */
    @Test
    public void testMatchesWholeDay() {
        testOn.setTimes(new String[] { "0:00-24:00" });
        assertTrue("0:00-24:00 is not matched", testOn.matches());
    }

    /**
     * Test method for {@link TestOn#matches()} with time attribute set to
     * a range outside the actual time.
     */
    @Test
    public void testMatchesNoTime() {
        Time inOneMinute = new Time(System.currentTimeMillis() + 60000L);
        Time inTwoMinutes = new Time(System.currentTimeMillis() + 120000L);
        String timeRange = inOneMinute.toString() + "-" + inTwoMinutes.toString();
        testOn.setTimes(new String[] { timeRange });
        assertFalse(timeRange + " is matched", testOn.matches());
    }

    /**
     * Test method for {@link TestOn#matches()} with time attribute. Here we
     * test time ranges like "18:00-06:00", which does not end at midnight.
     */
    @Test
    public void testMatchesTimeOverMidnight() {
        Time now = new Time(System.currentTimeMillis());
        if ("12:00:00".compareTo(now.toString()) > 0) {
            testOn.setTimes(new String[] { "23:00-12:00" });
        } else {
            testOn.setTimes(new String[] { "12:00-1:00" });
        }
        assertTrue(testOn + " does not match", testOn.matches());
    }

}

