/*
 * $Id: ProxyDriver.java,v 1.10 2016/12/18 20:19:38 oboehm Exp $
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
 * (c)reated 27.03.2014 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.monitor.db;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This JDBC driver acts like a proxy between PatternTesting and the real JDBC
 * driver to be able to monitor JDBC access. It was inspired by the JAMonDriver
 * of the JAMon framework.
 * <p>
 * This driver is registered for JDBC URLs beginning with "<em>jdbc:proxy:</em>
 * ...". This prefix must follow the real driver path. E.g. if you want to use
 * HSQL as database your URL make look like
 * "<em>jdbc:proxy:hsqldb:file:/tmp/oli</em>".
 * </p>
 *
 * @author oliver
 * @version $Revision: 1.10 $
 * @since 1.4.1 (27.03.2014)
 * @deprecated since 2.0, use {@link clazzfish.jdbc.ProxyDriver}
 */
@Deprecated
public class ProxyDriver extends clazzfish.jdbc.ProxyDriver implements Driver {

	/**
	 * Registers the driver as JDBC driver.
	 * <p>
	 * Because the implementation how the {@link DriverManager} finds the
	 * corresponding driver for a given JDBC-URL is a little stupid - it tries
	 * out all drivers if it can work with the URL. In case of problems (e.g. if
	 * the network is down) it throws the first {@link SQLException} it got and
	 * rethrow it - but this may be not the reason for the error message that
	 * the protocoll "jdbc:proxy:..." can not be understood.
	 * </p>
	 * <p>
	 * This is the reason why first all registered drivers are first
	 * deregistered and the registered again. This guarantees that the
	 * ProxyDriver is the first registered driver.
	 * </p>
	 */
	public static void register() {
		clazzfish.jdbc.ProxyDriver.register();
	}

	/**
	 * Gets the real JDBC URL of the underlying driver.
	 *
	 * @param jdbcURL
	 *            the jdbc url, e.g. "jdbc:proxy:hsqldb:mem:testdb"
	 * @return the real driver name
	 */
	public static String getRealURL(final String jdbcURL) {
		return clazzfish.jdbc.ProxyDriver.getRealURL(jdbcURL);
	}

	/**
	 * Gets the real driver name of the underlying driver.
	 *
	 * @param jdbcURL
	 *            the jdbc url, e.g. "jdbc:proxy:hsqldb:mem:testdb"
	 * @return the real driver name
	 */
	public static String getRealDriverName(final String jdbcURL) {
		return clazzfish.jdbc.ProxyDriver.getRealDriverName(jdbcURL);
	}

	/**
	 * Gets the real driver.
	 *
	 * @param jdbcURL
	 *            the jdbc url, e.g. "jdbc:proxy:hsqldb:mem:testdb"
	 * @return the real driver
	 */
	public static Driver getRealDriver(final String jdbcURL) {
		return clazzfish.jdbc.ProxyDriver.getRealDriver(jdbcURL);
	}

}
