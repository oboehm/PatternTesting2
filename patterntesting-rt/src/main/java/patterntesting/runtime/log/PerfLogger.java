/*
 * Copyright (c) 2014-2023 by Oliver Boehm
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

import org.slf4j.LoggerFactory;
import org.slf4j.*;

/**
 * This class is a kind of performance logger to be able to log the execution
 * times of methods or code segments which may need a little bit longer. It can
 * be used as a replacement of a normal logger.
 * <p>
 * We use a {@link LogWatch} to measure the times between a
 * {@link #start(String, Object...)} and {@link #end(String, Object...)} call.
 * We store this {@link LogWatch} as {@link ThreadLocal} because loggers are
 * normally used as static variables. So we can be sure that each thread has its
 * own {@link LogWatch}.
 * </p>
 *
 * @author oliver
 * @version $Revision: 1.12 $
 * @since 1.4.1 (17.03.2014)
 */
public final class PerfLogger {

	private final Logger wrapped;

	/** The local stopwatch. */
	private final ThreadLocal<LogWatch> timer = ThreadLocal.withInitial(() -> new LogWatch());

	/**
	 * Instantiates a new perf logger.
	 */
	public PerfLogger() {
		this(PerfLogger.class);
	}

	/**
	 * Instantiates a new perf logger.
	 *
	 * @param clazz
	 *            the clazz
	 */
	public PerfLogger(final Class<?> clazz) {
		this(LoggerFactory.getLogger(clazz));
	}

	/**
	 * Instantiates a new perf logger.
	 *
	 * @param logger
	 *            the logger
	 */
	public PerfLogger(final Logger logger) {
		this.wrapped = logger;
	}

	/**
	 * Start the log for the given message. The default level for the logged
	 * message is "INFO".
	 *
	 * @param format
	 *            the format
	 * @param args
	 *            the args
	 */
	public void start(final String format, final Object... args) {
		this.start(SimpleLog.LOG_LEVEL_INFO, format, args);
	}

	/**
	 * Start the log for the given message. For the valid log levels see
	 *
	 * @param level
	 *            the level
	 * @param format
	 *            the format
	 * @param args
	 *            the args {@link SimpleLog}.
	 * @see SimpleLog
	 */
	public void start(final int level, final String format, final Object... args) {
		this.log(level, format, args);
		timer.get().start();
	}

	/**
	 * End the log with the given message. The output will contain the measure
	 * time from the last {@link #start(String, Object...)} log. So be sure to
	 * call one of the start methods before.
	 * <p>
	 * The default level for the logged message is "INFO".
	 * </p>
	 *
	 * @param format
	 *            the format
	 * @param args
	 *            the args
	 */
	public void end(final String format, final Object... args) {
		this.end(SimpleLog.LOG_LEVEL_INFO, format, args);
	}

	/**
	 * End the log with the given message. The output will contain the measure
	 * time from the last {@link #start(String, Object...)} log. So be sure to
	 * call one of the start methods before.
	 * <p>
	 * It is recommended to use the same as in
	 * </p>
	 *
	 * @param logLevel
	 *            the log level
	 * @param format
	 *            the format
	 * @param args
	 *            the args {@link #start(int, String, Object...)}. If the
	 *            reported times is greater than 1 minute the log level will be
	 *            increased at least to "INFO".
	 */
	public void end(int logLevel, final String format, final Object... args) {
		LogWatch watch = timer.get();
		watch.stop();
		long millis = watch.getElapsedTime();
		int level = logLevel;
		if ((millis > 60000L) && (level < SimpleLog.LOG_LEVEL_INFO)) {
			this.wrapped.trace("Log level will be increased from {} to 'INFO'", level);
			level = SimpleLog.LOG_LEVEL_INFO;
		}
		this.log(level, format + " finished after " + watch + ".", args);
	}

	/**
	 * Log.
	 *
	 * @param level
	 *            the level
	 * @param format
	 *            the format
	 * @param args
	 *            the args
	 */
	public void log(final int level, final String format, final Object... args) {
		switch (level) {
		case SimpleLog.LOG_LEVEL_TRACE:
			this.trace(format, args);
			break;
		case SimpleLog.LOG_LEVEL_DEBUG:
			this.debug(format, args);
			break;
		case SimpleLog.LOG_LEVEL_INFO:
			this.info(format, args);
			break;
		case SimpleLog.LOG_LEVEL_WARN:
			this.warn(format, args);
			break;
		case SimpleLog.LOG_LEVEL_ERROR:
		case SimpleLog.LOG_LEVEL_FATAL:
			this.error(format, args);
			break;
		default:
			this.info("Level " + level + ": " + format, args);
			break;
		}
	}

	/**
	 * Error.
	 *
	 * @param format
	 *            the format
	 * @param args
	 *            the args
	 */
	public void error(final String format, final Object... args) {
		wrapped.error(format, args);
	}

	/**
	 * Warn.
	 *
	 * @param format
	 *            the format
	 * @param args
	 *            the args
	 */
	public void warn(final String format, final Object... args) {
		wrapped.warn(format, args);
	}

	/**
	 * Info.
	 *
	 * @param format
	 *            the format
	 * @param args
	 *            the args
	 */
	public void info(final String format, final Object... args) {
		wrapped.info(format, args);
	}

	/**
	 * Debug.
	 *
	 * @param format
	 *            the format
	 * @param args
	 *            the args
	 */
	public void debug(final String format, final Object... args) {
		wrapped.debug(format, args);
	}

	/**
	 * Trace.
	 *
	 * @param format
	 *            the format
	 * @param args
	 *            the args
	 */
	public void trace(final String format, final Object... args) {
		wrapped.trace(format, args);
	}

	/**
	 * Checks if is debug enabled.
	 *
	 * @return true, if is debug enabled
	 */
	public boolean isDebugEnabled() {
		return wrapped.isDebugEnabled();
	}

}
