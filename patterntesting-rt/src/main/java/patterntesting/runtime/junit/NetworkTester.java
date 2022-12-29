/*
 * $Id: NetworkTester.java,v 1.11 2017/08/07 07:04:02 oboehm Exp $
 *
 * Copyright (c) 2017 by Oliver Boehm
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
 * (c)reated 26.07.2017 by oboehm (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * With the NetworkTester you can assert if a host is online or offline.
 *
 * @author oboehm
 * @version $Revision: 1.11 $
 * @since 1.8 (26.07.2017)
 */
public final class NetworkTester {

    private static final Logger LOG = LoggerFactory.getLogger(NetworkTester.class);
    private static final int CONNECTION_TIMEOUT_IN_MINUTES = 2;

    /** Utility class - no need to instantiate it. */
    private NetworkTester() {
    }

    /**
     * Asserts, that the given host is online.
     *
     * @param host the hostname or IP address
     */
    public static void assertOnline(String host) {
        assertOnline(host, CONNECTION_TIMEOUT_IN_MINUTES, TimeUnit.MINUTES);
    }

    /**
     * Checks if the given host is online.
     *
     * @param host the hostname or IP address
     * @return true if host is online
     * @since 2.0
     */
    public static boolean isOnline(String host) {
        return isOnline(host, CONNECTION_TIMEOUT_IN_MINUTES, TimeUnit.MINUTES);
    }

    /**
     * Asserts, that the given host is online. The given time is the maximal
     * time we try to connect to the given host.
     *
     * @param host hostname or ip address
     * @param time how long should we try to connect to host?
     * @param unit the time unit
     */
    public static void assertOnline(String host, int time, TimeUnit unit) {
        assertTrue(isOnline(host, time, unit), host + " is offline");
    }

    /**
     * Checks if the given host is online. The given time is the maximal
     * time we try to connect to the given host.
     *
     * @param host hostname or ip address
     * @param time how long should we try to connect to host?
     * @param unit the time unit
     * @return true if host is online
     * @since 2.0
     */
    public static boolean isOnline(String host, int time, TimeUnit unit) {
        PortScanner scanner = scanPortsOf(host, time, unit);
        return scanner.openPortDetected();
    }

    private static PortScanner scanPortsOf(String host, int timeout, TimeUnit unit) {
        PortScanner scanner = new PortScanner(host);
        scanner.scanPorts(timeout, unit);
        return scanner;
    }

    /**
     * Asserts, that the given host is online at the given port.
     *
     * @param host the hostname or IP address
     * @param port the port between 0 and 0xFFFF
     */
    public static void assertOnline(String host, int port) {
        assertTrue(isOnline(host, port), host + ":" + port + " is offline");
    }

    /**
     * Asserts, that the socket address is online.
     *
     * @param host the hostname or IP address
     */
    public static void assertOnline(InetSocketAddress host) {
        assertOnline(host.getHostName(), host.getPort());
    }

    /**
     * Asserts, that the given host is online.
     *
     * @param host the IP address
     */
    public static void assertOnline(InetAddress host) {
        assertOnline(host.getHostName());
    }

    /**
     * Asserts, that the given host is online. The given time is the maximal
     * time we try to connect to the given host.
     *
     * @param host hostname or ip address
     * @param time how long should we try to connect to host?
     * @param unit the time unit
     */
    public static void assertOnline(InetAddress host, int time, TimeUnit unit) {
        assertOnline(host.getHostName(), time, unit);
    }


    /**
     * Asserts, that the given host is online at the given port.
     *
     * @param host the IP address
     * @param port the port between 0 and 0xFFFF
     */
    public static void assertOnline(InetAddress host, int port) {
        assertOnline(host.getHostName(), port);
    }


    /**
     * Checks if the given host is online.
     *
     * @param host the hostname or IP address
     * @param port the port between 0 and 0xFFFF
     * @return true if host is online
     * @since 2.0.2
     */
    public static boolean isOnline(String host, int port) {
        return isOnline(host, port, 20, TimeUnit.SECONDS);
    }

    /**
     * Checks if the given host is online in the given time distance.
     *
     * @param host the hostname or IP address
     * @param port the port between 0 and 0xFFFF
     * @param timeout timeout
     * @param unit    e.g. {@link TimeUnit#SECONDS}
     * @return true if host is online
     * @since 2.3
     */
    public static boolean isOnline(String host, int port, long timeout, TimeUnit unit) {
        LOG.debug("Connecting to {}:{}...", host, port);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Socket> fs = executor.submit(() -> new Socket(host, port));
        try (Socket socket = fs.get(timeout, unit)) {
            LOG.debug("Connecting to {}:{} was successful.", host, port);
            return socket.isConnected();
        } catch (IOException | InterruptedException | ExecutionException | TimeoutException ex) {
            LOG.debug("Connecting to {}:{} failed ({}).", host, port, ex.toString());
            LOG.trace("Details:", ex);
            return false;
        }
    }

    /**
     * Asserts, that the given host is offline. This is the opposite of
     * {@link #assertOnline(String)}.
     *
     * @param host the hostname or IP address
     */
    public static void assertOffline(String host) {
        assertOffline(host, CONNECTION_TIMEOUT_IN_MINUTES, TimeUnit.MINUTES);
    }

    /**
     * Asserts, that the given host is offline. The given time is the maximal
     * time we try to connect to the given host. Normally it takes about at
     * least 8 minutes to realize that a host is offline. So if you want to
     * wait a shorter time use this method.
     *
     * @param host hostname or ip address
     * @param time how long should we try to connect to host?
     * @param unit the time unit
     */
    public static void assertOffline(String host, int time, TimeUnit unit) {
        PortScanner scanner = scanPortsOf(host, time, unit);
        assertFalse(scanner.openPortDetected(), host + " is online");
    }

    /**
     * Asserts, that the port of the given host is offline.
     *
     * @param host the hostname or IP address
     * @param port the port between 0 and 0xFFFF
     */
    public static void assertOffline(String host, int port) {
        assertFalse(isOnline(host, port), host + ":" + port + " is online");
    }

    /**
     * Asserts, that the socket address is offline.
     *
     * @param host the hostname or IP address
     */
    public static void assertOffline(InetSocketAddress host) {
        assertOffline(host.getHostName(), host.getPort());
    }

    /**
     * Asserts, that the given host is offline. This is the opposite of
     * {@link #assertOnline(InetAddress)}.
     *
     * @param host the hostname or IP address
     */
    public static void assertOffline(InetAddress host) {
        assertOffline(host.getHostName());
    }

    /**
     * Asserts, that the given host is offline. The given time is the maximal
     * time we try to connect to the given host. Normally it takes about at
     * least 8 minutes to realize that a host is offline. So if you want to
     * wait a shorter time use this method.
     *
     * @param host hostname or ip address
     * @param time how long should we try to connect to host?
     * @param unit the time unit
     */
    public static void assertOffline(InetAddress host, int time, TimeUnit unit) {
        assertOffline(host.getHostName(), time, unit);
    }

    /**
     * Asserts, that the port of the given host is offline.
     *
     * @param host the hostname or IP address
     * @param port the port between 0 and 0xFFFF
     */
    public static void assertOffline(InetAddress host, int port) {
        assertOffline(host.getHostName(), port);
    }

    /**
     * Asserts, that the given URI exists and is reachable.
     *
     * @param uri a valid URI
     */
    public static void assertExists(URI uri) {
        assertTrue(exists(uri));
    }

    /**
     * Asserts, that the given URL exists and is reachable.
     *
     * @param url a valid URL
     */
    public static void assertExists(URL url) {
        assertTrue(exists(url));
    }

    /**
     * Checks, if the given URI exists and is reachable.
     *
     * @param uri a valid URI
     * @return true if uri exists
     * @since 2.0.2
     */
    public static boolean exists(URI uri) {
        try {
            return exists(uri.toURL());
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("invalid URI: " + uri, ex);
        }
    }

    /**
     * Checks, if the given URL exists and is reachable.
     *
     * @param url a valid URL
     * @return true if url exists
     * @since 2.0.2
     */
    public static boolean exists(URL url) {
        try {
            URLConnection connection = url.openConnection();
            LOG.trace("Got connection {} to {}.", connection,  url);
            connection.connect();
            LOG.debug("{} is online.", url);
            return true;
        } catch (IOException ioe) {
            LOG.debug("Cannot open {} ({}).", url, ioe.getMessage());
            LOG.trace("Details:", ioe);
            return false;
        }
    }



    private static class PortScanner implements Observer {

        private final List<Future<Boolean>> startedThreads = new CopyOnWriteArrayList<>();
        private final String host;
        private long endTime;
        long openPort = 0;

        public PortScanner(String host) {
            this.host = host;
        }

        /**
         * First implementation uses Thread to start each PortKnocker. But this
         * results in a OutOfMemoryException on a Mac. Now we uses an
         * {@link ExecutorService} together with a thread pool of 512 threads
         * (1000 threads didn't work).
         *
         * @param timeout timeout
         * @param unit    time unit
         */
        public void scanPorts(int timeout, TimeUnit unit) {
            endTime = System.currentTimeMillis() + unit.toMillis(timeout);
            ExecutorService es = Executors.newFixedThreadPool(512);
            for (int port = 1; (port <= 0xFFFF) && (openPort == 0) && (System.currentTimeMillis() < endTime); port++) {
                PortKnocker knocker = new PortKnocker(new InetSocketAddress(host, port), this);
                Future<Boolean> t = es.submit(knocker);
                startedThreads.add(t);
            }
        }

        public boolean openPortDetected() {
            try {
                for (Future<Boolean> t : startedThreads) {
                    if (System.currentTimeMillis() >= endTime) {
                        break;
                    }
                    Boolean online = t.get(endTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
                    if (Boolean.TRUE.equals(online)) {
                        return true;
                    }
                }
            } catch (InterruptedException | ExecutionException | TimeoutException | CancellationException ex) {
                LOG.debug("Wait was cancelled:", ex);
                Thread.currentThread().interrupt();
            }
            return openPort > 0;
        }

        @Override
        public void update(Observable o, Object arg) {
            boolean online = (Boolean) arg;
            if (online) {
                PortKnocker knocker = (PortKnocker) o;
                openPort = knocker.getAddress().getPort();
                LOG.debug("Open port {} for host '{}' found.",  openPort, host);
                stopThreads();
            }
        }

        private void stopThreads() {
            for (Future<?> t : startedThreads) {
                if (!t.isDone() && !t.isCancelled()) {
                    t.cancel(true);
                }
            }
            startedThreads.clear();
        }

    }



    private static class PortKnocker extends Observable implements Callable<Boolean> {

        private final InetSocketAddress address;
        private final Observer observer;

        public PortKnocker(InetSocketAddress address, Observer observer) {
            this.address = address;
            this.observer = observer;
        }

        public InetSocketAddress getAddress() {
            return address;
        }

        @Override
        public Boolean call() {
            boolean online = isOnline(address.getHostName(), address.getPort());
            observer.update(this, online);
            return online;
        }

    }



}
