/*
 * Copyright (c) 2014-2019 by Oliver Boehm
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
 * (c)reated 23.03.2014 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.log;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import patterntesting.runtime.util.ThreadUtil;

import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link LogWatch} class.
 *
 * @author oliver
 * @since 1.4.1 (23.03.2014)
 */
public class LogWatchTest {

    private static Logger log = LoggerFactory.getLogger(LogWatchTest.class);
    private LogWatch watch;

    /**
     * Sets the up the {@link LogWatch} and starts it automatically.
     */
    @BeforeEach
    public void setUpWatch() {
        watch = new LogWatch();
    }

    /**
     * Test method for {@link LogWatch#toString()}.
     */
    @Test
    public void testToString() {
        ThreadUtil.sleep();
        watch.stop();
        String s = watch.toString();
        assertThat(s, not(containsString("[default]:")));
        log.info("watch = {}.", watch);
    }

    /**
     * Vor a very short time we do not want to see "0 ms" as result.
     * We expect a more exact time.
     */
    @Test
    public void testShortTime() {
        watch.stop();
        String s = watch.toString();
        assertThat("not exact", s, not(equalTo("0 ms")));
        log.info("watch = {}.", watch);
    }

    /**
     * If the watch is running we expect two different times when we call
     * {@link LogWatch#getTimeInNanos()}.
     */
    @Test
    public void testGetNanoTime() {
        long t1 = watch.getTimeInNanos();
        ThreadUtil.sleep();
        long t2 = watch.getTimeInNanos();
        assertThat(t1, lessThan(t2));
    }

    /**
     * If the watch is stopped the returned time should not change.
     */
    @Test
    public void testStop() {
        watch.stop();
        long t1 = watch.getTimeInNanos();
        long t2 = watch.getTimeInNanos();
        assertEquals(t1, t2);
    }

    @Test
    public void testGetTimeAsString() {
        String s = LogWatch.getTimeAsString(15000.0, Locale.GERMAN);
        assertThat(s, not(containsString("second")));
    }

    @Test
    public void getGetHoursAsString() {
        String s = LogWatch.getTimeAsString(6 * 3600 * 1000L, Locale.ENGLISH);
        assertEquals("6 hours", s);
    }

}
