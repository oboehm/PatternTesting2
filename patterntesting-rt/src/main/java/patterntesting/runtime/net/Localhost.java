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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;

/**
 * This (static) class represents your local host. You can ask it for the local
 * address and other things.
 *
 * @author oliver
 * @since 1.0 (30.01.2010)
 */
public final class Localhost {

	private static final Logger LOG = LoggerFactory.getLogger(Localhost.class);

	private static final Collection<InetAddress> inetAddresses = new ArrayList<>();

	/*
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
	 * @param hosts an array of hosts
	 * @return true if matches one of the given hosts.
	 */
	public static boolean matches(final String... hosts) {
		return matches(20, TimeUnit.SECONDS, hosts);
	}

	/**
	 * Here we try to get all network addresses to compare it against the given
	 * hosts in a given time. The host can be given as hostname (e.g. "localhost")
	 * or as IP address (e.g. "127.0.0.1").
	 *
	 * @param timeout – the maximum time to wait
	 * @param unit    – the time unit of the timeout argument
	 * @param hosts   - an array of hosts
	 * @return true if matches one of the given hosts
	 * @since 2.3
	 */
	public static boolean matches(long timeout, TimeUnit unit, final String... hosts) {
		ExecutorService executor = Executors.newFixedThreadPool(hosts.length);
		Future<Boolean>[] future = new Future[hosts.length];
		for (int i = 0; i < hosts.length; i++) {
			future[i] = matches(executor, hosts[i]);
		}
		for (int i = 0; i < hosts.length; i++) {
			try {
				if (future[i].get(timeout, unit)) {
					return true;
				}
			} catch (InterruptedException | ExecutionException | TimeoutException ex) {
				LOG.info("Match with {} failed ({}).", hosts[i], ex.toString());
				LOG.debug("Details:", ex);
			}
		}
		return false;
	}

	/**
	 * Here we try to get all network addresses to compare it against the given
	 * hosts in a given time. The host can be given as hostname (e.g. "localhost")
	 * or as IP address (e.g. "127.0.0.1").
	 *
	 * @param timeout – the maximum time to wait
	 * @param hosts   - an array of hosts
	 * @return true if matches one of the given hosts
	 * @since 2.6
	 */
	public static boolean matches(Duration timeout, String... hosts) {
		return matches(timeout.toMillis(), TimeUnit.MILLISECONDS, hosts);
	}

	private static Future<Boolean> matches(ExecutorService executor, String host) {
		return executor.submit(() -> matches(host));
	}

	/**
	 * Here we try to get all network addresses to compare it against the given
	 * hosts.
	 *
	 * @param host either IP address (e.g. "127.0.0.1") or hostname (e.g.
	 *             "localhost")
	 * @return true if matches the given host
	 */
	public static boolean matches(final String host) {
        for (InetAddress address : inetAddresses) {
            if (host.equals(address.getHostAddress()) || host.equals(address.getHostName())) {
                return true;
            }
        }
		return false;
	}

}
