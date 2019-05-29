/*
 * $Id: SocketExceptionHelper.java,v 1.10 2016/02/13 21:37:29 oboehm Exp $
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
 * (c)reated 12.05.2010 by oliver (ob@oasd.de)
 */

package patterntesting.exception.net;

import java.net.*;

import org.apache.commons.lang3.StringUtils;

/**
 * The Class SocketExceptionHelper with some utility methods for better
 * SocketExceptions or NoRouteToHostExceptions.
 *
 * @author oliver
 * @since 1.0 (12.05.2010)
 */
public final class SocketExceptionHelper {

    /** No need for to instantiate this class (utility class). */
    private SocketExceptionHelper() {
    }

    /**
     * If the given NoRouteToHostException has only the default message
     * ("No route to host") this message will be replaced by a better one.
     *
     * @param e the original NoRouteToHostException
     * @param connection the connection
     * @return a NoRouteToHostException which contains the host name or address
     */
    public static NoRouteToHostException getBetterNoRouteToHostException(
            final NoRouteToHostException e, final URLConnection connection) {
        String msg = e.getMessage();
        URL url = connection.getURL();
        if ("No route to host".equalsIgnoreCase(msg)) {
            msg = "No route to " + url.getHost();
        } else {
            msg = msg + " (" + url.getHost() + ")";
        }
        return new NoRouteToHostException(msg);
    }
    
    /**
     * If the given NoRouteToHostException has only the default message
     * ("No route to host") this message will be enrich the message by the
     * given hostname and port number.
     *
     * @param ex the ex
     * @param host the host
     * @param port the port
     * @return the better no route to host exception
     * @since 1.2.10-YEARS
     */
    public static NoRouteToHostException getBetterNoRouteToHostException(
            final NoRouteToHostException ex, final String host, final int port) {
        String msg = ex.getMessage();
        if (msg.contains(host)) {
            return ex;
        }
        if ("No route to host".equalsIgnoreCase(msg)) {
            msg = "No route to " + host + ":" + port;
        } else {
            msg = msg + " (" + host + ":" + port + ")";
        }
        return new NoRouteToHostException(msg);
    }

    /**
     * If the given ConnectException has only the default message
     * ("Connection refused") this message will be enrich the message by the
     * given hostname and port number.
     *
     * @param ex the ex
     * @param host the host
     * @param port the port
     * @return the better exception
     * @since 1.2.10-YEARS
     */
    public static ConnectException getBetterConnectException(
            final ConnectException ex, final String host, final int port) {
        String msg = ex.getMessage();
        if (msg.contains(host)) {
            return ex;
        }
        if ("Connection refused".equalsIgnoreCase(msg)) {
            msg = "Connection to " + host + ":" + port + " refused";
        } else {
            msg = msg + " (" + host + ":" + port + ")";
        }
        return new ConnectException(msg);
    }

    /**
     * The given SocketException is checked if it contains the host in the
     * error message. If not it is appended.
     *
     * @param e the original SocketException
     * @param connection the URLConnection
     * @return a better SocketException if in the original the host is missing
     */
    public static SocketException getBetterSocketException(final SocketException e,
            final URLConnection connection) {
        URL url = connection.getURL();
        String host = url.getHost();
        int port = url.getPort();
        return DetailedSocketException.of(e, host, port);
    }

}

