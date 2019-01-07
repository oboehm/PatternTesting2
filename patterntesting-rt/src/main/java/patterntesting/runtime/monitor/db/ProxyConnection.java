/*
 * $Id: ProxyConnection.java,v 1.18 2016/12/18 20:19:38 oboehm Exp $
 *
 * Copyright (c) 2012-2014 by Oliver Boehm
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
 * (c)reated 29.09.2012 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.monitor.db;

import patterntesting.runtime.annotation.IgnoreForSequenceDiagram;

import java.lang.reflect.InvocationHandler;
import java.sql.Connection;

/**
 * This is a dynamic proxy for a JDBC connection which monitors together with
 * the {@link ConnectionMonitor} the different newInstance() and close() call.
 * <p>
 * Note: Since 1.4.2 this class was moved from package
 * "patterntesting.runtime.db" to here.
 * </p>
 *
 * @author oliver (ob@aosd.de)
 * @version $Revision: 1.18 $
 * @since 1.3 (29.09.2012)
 * @deprecated since 2.0, use {@link clazzfish.jdbc.ProxyConnection}
 */
@Deprecated
@IgnoreForSequenceDiagram
public class ProxyConnection extends clazzfish.jdbc.ProxyConnection implements InvocationHandler {

	/**
	 * Creates a new proxy instance for the given connection.
	 *
	 * @param connection
	 *            the connection
	 * @return the connection
	 */
	public static Connection newInstance(final Connection connection) {
		return clazzfish.jdbc.ProxyConnection.newInstance(connection);
	}

	/**
	 * Instantiates a new proxy connection. This constructor is called from
	 * {@link #newInstance(Connection)} which is of no interest for us. We want
	 * to store the real caller so we ignore the {@link ProxyConnection} class
	 * but also the {@link ProxyDriver} class (ProxyDriver also calls this
	 * constructor indirectly) and other non-interesting classes.
	 *
	 * @param connection
	 *            the connection
	 */
	protected ProxyConnection(final Connection connection) {
		super(connection);
	}
	
}
