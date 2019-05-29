/*
 * $Id: SocketExceptionAspect.aj,v 1.2 2012/03/09 21:10:15 oboehm Exp $
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

import java.io.*;
import java.net.*;

import org.aspectj.lang.annotation.SuppressAjWarnings;

/**
 * Sometimes you get a NoRouteToHostException with "No route to host" as the
 * only message. Why does not says the exception which host?
 * So we need this aspect here which will do that for us.
 *
 * @author oliver
 * @since 1.0 (12.05.2010)
 */
public aspect SocketExceptionAspect {

    /**
     * Here we wrap the methods of URLConnection which can throw a
     * SocketException like a NoRouteToHostException.
     *
     * @param connection the wrapped URLConnection
     * @return the same as the called method
     * @throws IOException in case of an error
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    Object around(final URLConnection connection) throws IOException :
            call(public * URLConnection+.*(..) throws IOException)
            && target(connection) {
        try {
            return proceed(connection);
        } catch (NoRouteToHostException e) {
            throw SocketExceptionHelper.getBetterNoRouteToHostException(e, connection);
        } catch (SocketException e) {
            throw SocketExceptionHelper.getBetterSocketException(e, connection);
        }
    }

    /**
     * If a socket is created you may get a ConnectException with
     * "connection refused". What you don't see is the host and the port
     * in the exception. So this is now provided by this advice.
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    Socket around(final String host, final int port) throws IOException :
            call(public Socket.new(String, int, ..) throws IOException)
            && args(host, port) {
        try {
            return proceed(host, port);
        } catch (ConnectException ce) {
            throw SocketExceptionHelper.getBetterConnectException(ce, host, port);
        } catch (NoRouteToHostException ex) {
            throw SocketExceptionHelper.getBetterNoRouteToHostException(ex, host, port);
        } catch (SocketException ex) {
            throw SocketExceptionHelper.getBetterSocketException(ex, host, port);
        }
    }

}

