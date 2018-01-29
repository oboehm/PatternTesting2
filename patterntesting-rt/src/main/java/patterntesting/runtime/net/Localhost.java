/*
 * $Id: Localhost.java,v 1.7 2016/12/18 20:19:37 oboehm Exp $
 *
 * Copyright (c) 2010 by Oliver Boehm
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
 * (c)reated 30.01.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.net;

import java.net.*;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;

/**
 * This (static) class represents your local host. You can ask it for the local
 * address and other things.
 *
 * @author oliver
 * @since 1.0 (30.01.2010)
 */
public final class Localhost {

	private static final Logger LOG = LogManager.getLogger(Localhost.class);

	private static final Collection<InetAddress> inetAddresses = new ArrayList<>();

	/**
	 * We don't expect that the network addresses changes during the lifetime of
	 * the application. So we identify it only once at the start time of this
	 * class.
	 */
	static {
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = networkInterfaces.nextElement();
				Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress address = addresses.nextElement();
					inetAddresses.add(address);
				}
			}
		} catch (SocketException ex) {
			LOG.debug("can't get local NetworkInterfaces ({})", ex.getMessage());
			LOG.trace("Details:", ex);
		}
	}

	/** This is utility class which needs not to be instantiated. */
	private Localhost() {
	}

	/**
	 * Returns all local IP addresses. If you want to get the local host name
	 * use {@link InetAddress#getLocalHost()}.
	 *
	 * @return a list of the host addresses
	 */
	public static Collection<InetAddress> getInetAddresses() {
		return Collections.unmodifiableCollection(inetAddresses);
	}

	/**
	 * Here we try to get all network addresses to compare it against the given
	 * hosts. The host can be given as hostname (e.g. "localhost") or as IP
	 * address (e.g. "127.0.0.1").
	 *
	 * @param hosts
	 *            an array of hosts
	 * @return true if matches one of the given hosts.
	 */
	public static boolean matches(final String[] hosts) {
		for (int i = 0; i < hosts.length; i++) {
			if (matches(hosts[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Here we try to get all network addresses to compare it against the given
	 * hosts.
	 *
	 * @param host
	 *            either IP address (e.g. "127.0.0.1") or hostname (e.g.
	 *            "localhost")
	 * @return true if matches the given host
	 */
	public static boolean matches(final String host) {
		for (Iterator<InetAddress> iterator = inetAddresses.iterator(); iterator.hasNext();) {
			InetAddress address = iterator.next();
			if (host.equals(address.getHostAddress()) || host.equals(address.getHostName())) {
				return true;
			}
		}
		return false;
	}

}
