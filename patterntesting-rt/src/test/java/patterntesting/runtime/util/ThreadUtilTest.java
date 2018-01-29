/*
 * $Id: ThreadUtilTest.java,v 1.6 2016/12/18 20:19:37 oboehm Exp $
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
 * (c)reated 30.09.2008 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.util;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.Test;

import patterntesting.runtime.log.LogWatch;

/**
 * The Class ThreadUtilTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.6 $
 * @since 30.09.2008
 */
public class ThreadUtilTest {

    private static final Logger LOG = LogManager.getLogger(ThreadUtilTest.class);
	/** minimal timer resolution */
    private static final long minT = ThreadUtil.getTimerResolutionInMillis();

	/**
	 * Test calibrate.
	 */
	@Test
	public void testCalibrate() {
		assertTrue(minT > 0L);
	}

    /**
     * Test method for
     * {@link patterntesting.runtime.util.ThreadUtil#sleep(long)}.
     */
    @Test
    public void testSleepLong() {
        long t = System.currentTimeMillis();
        ThreadUtil.sleep(minT);
        assertTrue((System.currentTimeMillis() - t) >= 0);
    }

    /**
     * Test method for
     * {@link patterntesting.runtime.util.ThreadUtil#sleep(long, int)}.
     */
	@Test
	public void testSleepLongInt() {
		long t = System.currentTimeMillis();
		ThreadUtil.sleep(minT, 500);
		assertTrue((System.currentTimeMillis() - t) >= 0);
	}

	/**
	 * Test sleep time unit with {@link TimeUnit#NANOSECONDS}.
	 */
	@Test
	public void testSleepNanoSecond() {
		checkSleep(TimeUnit.NANOSECONDS);
	}

    /**
     * Test sleep time unit with {@link TimeUnit#MICROSECONDS}.
     */
    @Test
    public void testSleepMicroSecond() {
        checkSleep(TimeUnit.MICROSECONDS);
    }

    /**
     * Test sleep time unit with {@link TimeUnit#MILLISECONDS}.
     */
    @Test
    public void testSleepMilliSecond() {
        checkSleep(TimeUnit.MILLISECONDS);
    }

    private void checkSleep(TimeUnit unit) {
        LogWatch watch = new LogWatch();
        ThreadUtil.sleep(1, unit);
        long delta = watch.getTimeInNanos();
		assertTrue("positiv number expected: " + delta, delta > 0);
		LOG.info("Sleep of 1 {} needs {} ({} ns).", unit, watch, delta);
    }

}
