/*
 * $Id: SqlStatistic.java,v 1.12 2016/12/18 20:19:38 oboehm Exp $
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
 * (c)reated 16.04.2014 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.monitor.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import clazzfish.jdbc.monitor.ProfileMonitor;
import patterntesting.runtime.monitor.ProfileStatistic;
import patterntesting.runtime.monitor.db.internal.StasiPreparedStatement;
import patterntesting.runtime.monitor.db.internal.StasiStatement;
import patterntesting.runtime.util.Converter;
import patterntesting.runtime.util.StackTraceScanner;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * This class monitors and measures SQL statements. To be able to distinguish
 * them from methods profiling it is a separate class derived from
 * {@link ProfileStatistic}.
 *
 * @author oliver
 * @since 1.4.2 (16.04.2014)
 * @deprecated since 2.0, use {@link clazzfish.jdbc.SqlStatistic}
 */
@Deprecated
public class SqlStatistic extends clazzfish.jdbc.AbstractStatistic implements SqlStatisticMBean {

	private static final Logger LOG = LogManager.getLogger(SqlStatistic.class);
	private static final SqlStatistic SQL_INSTANCE;

	/**
	 * rootMonitor *must* be initialized before isJamonAvailable() is called.
	 * Otherwise you'll get a NullPointerException because in JamonAvailable()
	 * rootMonitor will be accessed (so rootMonitor must be initialized before!)
	 */
	static {
		SQL_INSTANCE = new SqlStatistic();
	}

	/**
	 * Gets the single instance of SqlStatistic.
	 *
	 * @return single instance of SqlStatistic
	 */
	public static SqlStatistic getInstance() {
		return SQL_INSTANCE;
	}

	private SqlStatistic() {
		super("SQL");
	}

	/**
	 * Normally the {@link SqlStatistic} is already registered as MBean. So you
	 * don't need to register it manually. But if you want to register it under
	 * your own name you can use this method here. This can be useful if your
	 * application runs on an application server with several other active
	 * applications.
	 *
	 * @param name
	 *            e.g "my.company.SqlStat"
	 * @since 1.6
	 */
	public static void registerAsMBean(final String name) {
		SQL_INSTANCE.registerMeAsMBean(name);
	}

	/**
	 * To start a new statistic call this method. In contradiction to
	 * {@link ProfileStatistic#reset()} old {@link ProfileMonitor}s will be not
	 * kept.
	 */
	@Override
	public void reset() {
		synchronized (SqlStatistic.class) {
			this.resetRootMonitor();
		}
	}

	/**
	 * Log statistic.
	 */
	@Override
	public void logStatistic() {
		logMe();
	}

	/**
	 * Dump statistic.
	 *
	 * @return the name of the dump file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public File dumpStatistic() throws IOException {
		return dumpMe();
	}

	/**
	 * This operation dumps the statistic to the given file.
	 *
	 * @param filename the file name
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @since 1.5
	 */
	@Override
	public void dumpStatistic(String filename) throws IOException {
		dumpMe(new File(filename));
	}

	/**
	 * Start.
	 *
	 * @param sql
	 *            the sql
	 * @return the started profile monitor
	 */
	public static ProfileMonitor start(final String sql) {
		return SQL_INSTANCE.startProfileMonitorFor(sql.trim());
	}

	/**
	 * Stops the given 'mon' and logs the given command with the needed time if
	 * debug is enabled.
	 *
	 * @param mon
	 *            the mon
	 * @param command
	 *            the command
	 */
	public static void stop(final ProfileMonitor mon, final String command) {
		stop(mon, command, Void.TYPE);
	}

	/**
	 * Stops the given 'mon' and logs the given command with the needed time if
	 * debug is enabled.
	 *
	 * @param mon
	 *            the mon
	 * @param command
	 *            the command
	 * @param returnValue
	 *            the return value
	 */
	public static void stop(final ProfileMonitor mon, final String command, final Object returnValue) {
		mon.stop();
		if (LOG.isDebugEnabled()) {
			String msg = '"' + command + "\" returned with " + Converter.toShortString(returnValue) + " after "
					+ mon.getLastTime();
			if (LOG.isTraceEnabled()) {
				StackTraceElement[] stacktrace = StackTraceScanner.getCallerStackTrace(new Pattern[0],
						SqlStatistic.class, StasiStatement.class, StasiPreparedStatement.class);
				LOG.trace("{}\n\t{}", msg, Converter.toLongString(stacktrace).trim());
			} else {
				LOG.debug("{}.", msg);
			}
		}
	}

	/**
	 * You can register the instance as shutdown hook. If the VM is terminated
	 * the profile values are logged and dumped to a CSV file in the tmp
	 * directory.
	 *
	 * @see #logStatistic()
	 * @see #dumpStatistic()
	 */
	public static void addAsShutdownHook() {
		Runtime.getRuntime().addShutdownHook(SQL_INSTANCE);
	}

}
