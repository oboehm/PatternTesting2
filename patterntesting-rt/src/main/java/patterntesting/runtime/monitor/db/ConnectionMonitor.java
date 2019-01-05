/*
 * $Id: ConnectionMonitor.java,v 1.17 2016/12/18 20:19:38 oboehm Exp $
 *
 * Copyright (c) 2012 by Oliver Boehm
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
 * (c)reated 07.10.2012 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.monitor.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patterntesting.runtime.jmx.MBeanHelper;

import javax.management.openmbean.*;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This is the monitor class for the {@link ProxyConnection} which monitors the
 * different newInstance() and close() calls. So you can ask this class how many
 * connections and which connections are open.
 * <p>
 * The instance of this class is automatically registered as MBean as soon as a
 * {@link ProxyConnection} is used.
 * </p>
 * <p>
 * Note: Since 1.4.2 this class was moved from package
 * "patterntesting.runtime.db" to here.
 * </p>
 *
 * @author oliver (ob@aosd.de)
 * @version $Revision: 1.17 $
 * @since 1.3 (07.10.2012)
 * @deprecated since 2.0, use {@link clazzfish.jdbc.ConnectionMonitor}
 */
@Deprecated
public class ConnectionMonitor extends clazzfish.jdbc.ConnectionMonitor implements ConnectionMonitorMBean {

	private static final Logger LOG = LogManager.getLogger(ConnectionMonitor.class);
	private static final ConnectionMonitor INSTANCE;
	private static final List<ProxyConnection> openConnections = new CopyOnWriteArrayList<>();

	private static int sumOfConnections = 0;

	static {
		INSTANCE = new ConnectionMonitor();
	}

	/**
	 * No need to instantiate it - we provide only some services. The
	 * constructor is protected because it is still used by the (deprecated)
	 * ConnectionMonitor in patterntesting.runtime.db.
	 */
	protected ConnectionMonitor() {
		super();
		LOG.trace("New instance of {} created.", ConnectionMonitor.class);
	}

	/**
	 * Yes, it is a Singleton because it offers only some services. So we don't
	 * need the object twice.
	 *
	 * @return the only instance
	 */
	public static ConnectionMonitor getInstance() {
		return INSTANCE;
	}

	/**
	 * If you want to monitor the connection use this method.
	 *
	 * @param connection
	 *            the original connection
	 * @return a proxy for the connection
	 */
	public static Connection getMonitoredConnection(final Connection connection) {
		return ProxyConnection.newInstance(connection);
	}

	/**
	 * Adds the connection to the list of open connections.
	 *
	 * @param proxyConnection
	 *            the proxy connection
	 */
	public static void addConnection(final ProxyConnection proxyConnection) {
		openConnections.add(proxyConnection);
		sumOfConnections++;
	}

	/**
	 * Removes the connection from the list of open connections.
	 *
	 * @param proxyConnection
	 *            the proxy connection
	 */
	public static void removeConnection(final ProxyConnection proxyConnection) {
		openConnections.remove(proxyConnection);
	}

	/**
	 * Gets the caller of the given connection.
	 *
	 * @param connection
	 *            the connection
	 * @return the caller of the connection
	 */
	public static StackTraceElement getCallerOf(final Connection connection) {
		for (ProxyConnection proxy : openConnections) {
			if (proxy.getConnection().equals(connection)) {
				return proxy.getCaller()[0];
			}
		}
		throw new IllegalArgumentException("not monitored or closed: " + connection);
	}

	/**
	 * Gets the callers.
	 *
	 * @return the callers
	 * @see ConnectionMonitorMBean#getCallers()
	 */
	@Override
	public StackTraceElement[] getCallers() {
		StackTraceElement[] callers = new StackTraceElement[openConnections.size()];
		int i = 0;
		for (ProxyConnection proxy : openConnections) {
			callers[i] = proxy.getCaller()[0];
			i++;
		}
		return callers;
	}

	/**
	 * Gets the stacktrace of the caller which opens the last connection.
	 *
	 * @return the all caller
	 */
	@Override
	public StackTraceElement[] getLastCallerStacktrace() {
		ProxyConnection lastConnection = openConnections.get(openConnections.size() - 1);
		return lastConnection.getCaller();
	}

	/**
	 * Gets the caller stacktraces of all connections.
	 *
	 * @return stacktraces of all callers
	 * @throws OpenDataException
	 *             the open data exception
	 */
	@Override
	public TabularData getCallerStacktraces() throws OpenDataException {
		String[] itemNames = { "Caller", "Stacktrace" };
		String[] itemDescriptions = { "caller name", "stacktrace" };
		try {
			OpenType<?>[] itemTypes = { SimpleType.STRING, new ArrayType<String>(1, SimpleType.STRING) };
			CompositeType rowType = new CompositeType("propertyType", "property entry", itemNames, itemDescriptions,
					itemTypes);
			TabularDataSupport data = MBeanHelper.createTabularDataSupport(rowType, itemNames);
			for (ProxyConnection proxy : openConnections) {
				StackTraceElement[] stacktrace = proxy.getCaller();
				Map<String, Object> map = new HashMap<>();
				map.put("Caller", stacktrace[0].toString());
				map.put("Stacktrace", toStringArray(stacktrace));
				CompositeDataSupport compData = new CompositeDataSupport(rowType, map);
				data.put(compData);
			}
			return data;
		} catch (OpenDataException ex) {
			LOG.warn("Cannot get caller stacktraces of " + openConnections.size() + " open connections.", ex);
			throw ex;
		}
	}

	private static String[] toStringArray(final StackTraceElement[] stacktrace) {
		String[] array = new String[stacktrace.length];
		for (int i = 0; i < stacktrace.length; i++) {
			array[i] = stacktrace[i].toString();
		}
		return array;
	}

	/**
	 * Assert that all connections are closed.
	 */
	public static void assertConnectionsClosed() {
		int count = INSTANCE.getOpenConnections();
		if (count > 0) {
			AssertionError error = new AssertionError(count + " connection(s) not closed");
			error.setStackTrace(openConnections.iterator().next().getCaller());
			throw error;
		}
	}

}
