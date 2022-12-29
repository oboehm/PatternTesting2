/*
 * $Id: MemoryGuard.java,v 1.16 2016/12/18 20:19:36 oboehm Exp $
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
 * (c)reated 19.01.2009 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.monitor;

import org.slf4j.LoggerFactory;
import org.slf4j.*;

import patterntesting.runtime.util.Converter;

/**
 * The Class MemoryGuard.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.16 $
 * @since 19.01.2009
 */
public final class MemoryGuard {

	private static final Logger LOG = LoggerFactory.getLogger(MemoryGuard.class);
	private static final long MAX_MEM = Runtime.getRuntime().maxMemory();
	private static BackgroundLogger backgroundTask;

	/** No need to instantiate it (utility class). */
	private MemoryGuard() {
	}

	/**
	 * Gets the free memory.
	 *
	 * @return the free memory
	 */
	public static long getFreeMemory() {
		Runtime runtime = Runtime.getRuntime();
		long totalMem = runtime.totalMemory();
		return runtime.freeMemory() + (MAX_MEM - totalMem);
	}

	/**
	 * Gets the free memory in percent.
	 *
	 * @return the free memory in percent
	 */
	public static int getFreeMemoryInPercent() {
		long freeMem = getFreeMemory();
		return (int) ((freeMem + 50) * 100 / MAX_MEM);
	}

	/**
	 * Gets the free memory as string.
	 *
	 * @return the free memory as string
	 */
	public static String getFreeMemoryAsString() {
		return Converter.getMemoryAsString(getFreeMemory());
	}

	/**
	 * Log memory.
	 *
	 * @see #logMemory(Logger)
	 */
	public static void logMemory() {
		logMemory(LOG);
	}

	/**
	 * Logs a message if free memory falls down below x% of maximal heap size
	 * where x is:
	 * <ul>
	 * <li>x &lt; 1%: logs a fatal message</li>
	 * <li>1% &lt;= x &lt; 2%: logs an error</li>
	 * <li>1% &lt;= x &lt; 10%: logs a warning</li>
	 * <li>10% &lt;= x &lt; 20%: logs an info message</li>
	 * <li>20% &lt;= x &lt; 50%: logs debug message</li>
	 * <li>x &gt;= 50%: tracing</li>
	 * </ul>
	 *
	 * @param lg
	 *            the log
	 */
	public static void logMemory(final Logger lg) {
		int freeMemRate = getFreeMemoryInPercent();
		if (freeMemRate < 10) {
			System.gc();
			lg.trace("gc() called because free memory is below 10% ({}%)", freeMemRate);
			freeMemRate = getFreeMemoryInPercent();
		}
		String msg = getMemoryLogMessage(freeMemRate);
		if (freeMemRate < 2) {
			lg.error(msg);
		} else if (freeMemRate < 10) {
			lg.warn(msg);
		} else if (freeMemRate < 20) {
			lg.info(msg);
		} else if (freeMemRate < 50) {
			lg.debug(msg);
		} else {
			lg.trace(msg);
		}
	}

	/**
	 * Gets the memory log message.
	 *
	 * @return the memory log message
	 */
	public static String getMemoryLogMessage() {
		int freeMemRate = getFreeMemoryInPercent();
		return getMemoryLogMessage(freeMemRate);
	}

	private static String getMemoryLogMessage(final int freeMemRate) {
		return freeMemRate + "% of memory is free (" + getFreeMemoryAsString() + ")";
	}

	/**
	 * Every x milliseconds the free memory is checked and printed into the log
	 * (according the log level). This is done with in a separated background
	 * thread.
	 *
	 * You can only start the background logging if you set the log level of
	 * this class to DEBUG. To stop the background logging set the interval to
	 * 0.
	 *
	 * @param interval
	 *            (the sleep intervall in milliseconds, e.g. 300000 for 5 min.)
	 */
	public static synchronized void logFreeMemory(final long interval) {
		if (LOG.isDebugEnabled()) {
			if (backgroundTask == null) {
				backgroundTask = new BackgroundLogger(interval);
				LOG.trace("starting " + backgroundTask + "...");
				Thread backgroundThread = new Thread(backgroundTask, "background");
				backgroundThread.setDaemon(true);
				backgroundThread.setPriority(Thread.MIN_PRIORITY);
				backgroundThread.start();
			} else {
				backgroundTask.setInterval(interval);
				LOG.trace("Interval of {} was changed.", backgroundTask);
			}
		} else {
			LOG.info("Set the log level of {} to DEBUG to enable background logging.", MemoryGuard.class);
		}
	}

	/**
	 * This inner class do the job for us. It polls the memory size and logs it.
	 *
	 * @author oliver
	 * @since 01.07.2009
	 */
	static class BackgroundLogger implements Runnable {

		private long interval;

		/**
		 * Instantiates a new background logger.
		 *
		 * @param interval
		 *            the interval
		 */
		public BackgroundLogger(final long interval) {
			this.interval = interval;
		}

		/**
		 * Sets the interval.
		 *
		 * @param interval
		 *            the new interval
		 */
		public void setInterval(final long interval) {
			this.interval = interval;
		}

		/**
		 * Run. An interval of 0 will end this thread.
		 *
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			while (interval > 0) {
				logMemory();
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					LOG.debug("{} interrupted:", this, e);
					Thread.currentThread().interrupt();
					break;
				}
			}
			LOG.debug("{} is stopped.", this);
		}

		/**
		 * To string.
		 *
		 * @return the string
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return this.getClass().getSimpleName() + "(" + interval + "ms)";
		}
	}

}
