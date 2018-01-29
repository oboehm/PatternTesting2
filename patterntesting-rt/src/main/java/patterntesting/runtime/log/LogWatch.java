/*
 * $Id: LogWatch.java,v 1.12 2016/12/18 20:19:38 oboehm Exp $
 *
 * Copyright (c) 2014 by Oliver Boehm
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
 * (c)reated 17.03.2014 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.log;

import java.util.Locale;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;

import patterntesting.runtime.util.Converter;

/**
 * The Class LogWatch is a simple stop watch to be able to measure and log code
 * segments which need a little bit longer.
 *
 * @author oliver
 * @version $Revision: 1.12 $
 * @since 1.4.1 (17.03.2014)
 */
public final class LogWatch extends StopWatch {

	private static final Logger LOG = LogManager.getLogger(LogWatch.class);
	private long nanoStartTime;
	private long nanoEndTime;

	/**
	 * Instantiates a new log watch.
	 */
	public LogWatch() {
		super();
		this.start();
	}

	/**
	 * Start.
	 *
	 * @see org.apache.commons.lang3.time.StopWatch#start()
	 */
	@Override
	public void start() {
		this.reset();
		super.start();
		this.nanoStartTime = System.nanoTime();
	}

	/**
	 * Stop.
	 *
	 * @see org.apache.commons.lang3.time.StopWatch#stop()
	 */
	@Override
	public void stop() {
		this.nanoEndTime = System.nanoTime();
		super.stop();
	}

	/**
	 * Reset.
	 *
	 * @see org.apache.commons.lang3.time.StopWatch#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		this.nanoStartTime = 0L;
		this.nanoEndTime = 0L;
	}

	/**
	 * Gets the elapsed time from the start call. This method is a convenience
	 * method if you are coming from Perf4J or Speed4J. It also allows us to
	 * switch to one of these frameworks if it may be necessary.
	 *
	 * @return the elapsed time in milliseconds
	 */
	public long getElapsedTime() {
		return this.getTime();
	}

	/**
	 * Gets the elapsed time from the start call in nano seconds.
	 *
	 * @return the nano time
	 * @deprecated use {@link #getTimeInNanos()}
	 */
	@Override
	@Deprecated
	public long getNanoTime() {
		return this.getTimeInNanos();
	}

	/**
	 * Gets the elapsed time from the start call in nano seconds.
	 * <p>
	 * This method was called "getNanoTime" before but was now named in
	 * "getTimeInNanons" to fit better in the naming schema - there is a similar
	 * method {@link #getTimeInMillis()}.
	 * </p>
	 *
	 * @return the nano time
	 * @since 1.4.2
	 */
	public long getTimeInNanos() {
		long endTime = (this.nanoEndTime == 0) ? System.nanoTime() : this.nanoEndTime;
		long nanoTime = endTime - this.nanoStartTime;
		if (nanoTime < 0) {
			long milliTime = this.getTime();
			LOG.info("Will use {} ms as fallback because there was an overflow with nanoTime.", milliTime);
			nanoTime = 1000000 * milliTime;
		}
		return nanoTime;
	}

	/**
	 * Gets the elapsed time from the start call in milli seconds.
	 *
	 * @return the time in millis
	 * @since 1.4.2
	 */
	public double getTimeInMillis() {
		return this.getTimeInNanos() / 1000000.0;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		double millis = this.getTimeInMillis();
		if (millis > 6000000.0) {
			return super.toString();
		}
		return Converter.getTimeAsString(millis, Locale.ENGLISH);
	}

}
