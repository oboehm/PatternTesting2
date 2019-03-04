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
 * (c)reated 17.03.2014 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.log;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The Class LogWatch is a simple stop watch to be able to measure and log code
 * segments which need a little bit longer.
 *
 * @author oliver
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
	 */
	@Override
	public void start() {
		this.reset();
		super.start();
		this.nanoStartTime = System.nanoTime();
	}

	/**
	 * Stop.
	 */
	@Override
	public void stop() {
		this.nanoEndTime = System.nanoTime();
		super.stop();
	}

	/**
	 * Reset.
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
		return getTimeAsString(millis);
	}
	
	/**
	 * Gets the time as string with the corresponding unit. Unit can be "ms"
	 * (for milliseconds) or "seconds".
	 * <p>
	 * Before 2.0 this method was part of the Converter class.
	 * </p>
	 *
	 * @param timeInMillis the time in millis
	 * @return the time as string
	 * @since 2.0
	 */
	public static String getTimeAsString(final double timeInMillis) {
		return getTimeAsString(timeInMillis, Locale.getDefault());
	}

	/**
	 * Gets the time as string with the corresponding unit. Unit can be "ms"
	 * (for milliseconds) or "seconds".
	 * <p>
	 * Before 2.0 this method was part of the Converter class.
	 * </p>
	 *
	 * @param timeInMillis the time in millis
	 * @param locale the locale
	 * @return the time as string
	 * @since 2.0
	 */
	public static String getTimeAsString(final double timeInMillis, final Locale locale) {
		if (timeInMillis > 1.0) {
			return getTimeAsString((long) timeInMillis, locale);
		}
		Format nf = new DecimalFormat("#.###", new DecimalFormatSymbols(locale));
		return nf.format(timeInMillis) + " ms";
	}

    /**
     * Gets the time as string with the corresponding unit. Unit can be "ms"
     * (for milliseconds) or "seconds".
     * <p>
     * Before 2.0 this method was part of the Converter class.
     * </p>
     *
     * @param timeInMillis the time in millis
     * @return the time as string
     * @since 2.0
     */
    public static String getTimeAsString(final long timeInMillis) {
    	return getTimeAsString(timeInMillis, Locale.ENGLISH);
    }

	/**
	 * Gets the time as string with the corresponding unit. Unit can be "ms"
	 * (for milliseconds) or "seconds".
	 *
	 * @param timeInMillis the time in millis
	 * @param locale the locale
	 * @return the time as string
	 */
	public static String getTimeAsString(final long timeInMillis, final Locale locale) {
		ResourceBundle bundle = ResourceBundle.getBundle("patterntesting.runtime.log.messages", locale,
				ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_DEFAULT));
		if (timeInMillis > 18000000L) {
			return ((timeInMillis + 1800000L) / 3600000L) + " " + bundle.getString("hours");
		} else if (timeInMillis > 300000L) {
			return ((timeInMillis + 30000L) / 60000L) + " " + bundle.getString("minutes");
		} else if (timeInMillis > 5000L) {
			return ((timeInMillis + 500L) / 1000L) + " " + bundle.getString("seconds");
		}
		return timeInMillis + " ms";
	}

}
