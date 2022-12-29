/*
 * $Id: ThreadUtil.java,v 1.12 2016/12/18 20:19:36 oboehm Exp $
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

import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;
import org.slf4j.*;

/**
 * The Class ThreadUtil.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.12 $
 * @since 30.09.2008
 */
public final class ThreadUtil {

	private static final Logger LOG = LoggerFactory.getLogger(ThreadUtil.class);
	private static final long TIME_IN_MILLIS;

	static {
		TIME_IN_MILLIS = calibrateMillis();
		LOG.debug("timeInMillis calibrated (" + TIME_IN_MILLIS + "ms)");
	}

	/** To avoid instantiation it's now private. */
	private ThreadUtil() {
	}

	/**
	 * Sleep.
	 */
	public static void sleep() {
		sleep(TIME_IN_MILLIS);
	}

	/**
	 * Sleep.
	 *
	 * @param millis
	 *            the millis
	 */
	public static void sleep(final long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ignored) {
			LOG.info(ignored + " ignored");
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Sleep.
	 *
	 * @param millis
	 *            the millis
	 * @param nanos
	 *            the nanos
	 */
	public static void sleep(final long millis, final int nanos) {
		try {
			Thread.sleep(millis, nanos);
		} catch (InterruptedException ignored) {
			LOG.info(ignored + " ignored");
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Using now also the constants MINUTES, HOURS and DAYS which are new with
	 * Java 6.
	 *
	 * @param time
	 *            e.g. 5
	 * @param unit
	 *            e.c. SECONDS
	 */
	public static void sleep(final int time, final TimeUnit unit) {
		switch (unit) {
		case NANOSECONDS:
			sleep(0L, time);
			break;
		case MICROSECONDS:
			sleep(0L, time * 1000);
			break;
		case MILLISECONDS:
			sleep(time);
			break;
		case SECONDS:
			sleep(time * 1000L);
			break;
		case MINUTES:
			sleep(time * 1000L * 60);
			break;
		case HOURS:
			sleep(time * 1000L * 60 * 60);
			break;
		case DAYS:
			sleep(time * 1000L * 60 * 60 * 24);
			break;
		default:
			LOG.warn("unknown TimeUnit " + unit + " interpreting it as WEEKS");
			sleep(time * 1000L * 60 * 60 * 24 * 7);
			break;
		}
	}

	/**
	 * Determines the timer resolution for the sleep() method. On windows this
	 * is 15 ms, on Linux and MacOS 1 ms.
	 *
	 * @return 1 (ms) on Linux and MacOS, 15 (ms) on Windows
	 */
	private static long calibrateMillis() {
		long t0 = nextTimeMillis();
		long t1 = nextTimeMillis();
		return t1 - t0;
	}

	/**
	 * Go to the next timer tick.
	 *
	 * @return the next timestamp
	 */
	private static long nextTimeMillis() {
		long t0 = System.currentTimeMillis();
		while (t0 == System.currentTimeMillis()) { // SUPPRESS CHECKSTYLE
			// do nothing
		}
		return System.currentTimeMillis();
	}

	/**
	 * Gets the timer resolution in millis.
	 *
	 * @return the timer resolution in millis
	 */
	public static long getTimerResolutionInMillis() {
		return TIME_IN_MILLIS;
	}

}
