/*
 * $Id: WebDog.java,v 1.6 2016/12/21 22:09:02 oboehm Exp $
 *
 * Copyright (c) 2009 by Oliver Boehm
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
 * (c)reated 09.03.2009 by oliver (ob@aosd.de)
 */
package patterntesting.sample;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patterntesting.annotation.check.ct.OnlyForTesting;
import patterntesting.annotation.check.runtime.PublicForTesting;
import patterntesting.runtime.util.ReflectionHelper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * The WebDog can watch a web server if he is working correct.
 * It is an example how you can use the @OnlyForTesting annotation to
 * support the testability of this class.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.6 $
 * @since 09.03.2009
 */
public final class WebDog {

    private static final Logger log = LogManager.getLogger(WebDog.class);
    private final URL url;
    private int responseCode;
    private boolean running = true;

    /**
     * Instantiates a new web dog.
     *
     * @param url the url
     */
    public WebDog(final URL url) {
        this.url = url;
    }

    /**
     * We are a very busy WebDog - we watch the given URL without any pause.
     */
    public void watch() {
        while (running) {
            this.ping();
        }
    }

    /**
     * Ping.
     */
    protected void ping() {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.getContentType();
            this.responseCode = getResponseCodeFrom(connection);
        } catch (IOException ioe) {
            log.error("can't connect to " + url, ioe);
            this.responseCode = -1;
        }
        logMessage(log);
    }

    /**
     * It's a little bit tricky to get the field "responseCode" of
     * HttpURLConnection because it is protected. We use reflection to
     * do it.
     *
     * @param connection
     */
    private static int getResponseCodeFrom(final HttpURLConnection connection) {
        try {
            Integer rc = (Integer) ReflectionHelper.getFieldValue(connection,
                    "responseCode");
            return rc.intValue();
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("can't get responseCode of " + connection, ex);
        }
    }

    /**
     * This method should only be called for testing.
     *
     * @param rc the rc
     */
    @OnlyForTesting
    protected void setResponseCode(final int rc) {
        this.responseCode = rc;
    }

    /**
     * Gets the response code.
     *
     * @return the response code
     */
    public int getResponseCode() {
        return this.responseCode;
    }

    /**
     * One of the requirement for our WebDog is to log the following
     * messages (remember, it is only an example):
     * <dl>
     * <dt>responseCode=303</dt>
     * <dd>too much traffic to <i>url</i></dd>
     * <dt>responsCode=404</dt>
     * <dd><i>url</i> has Alzheimer</dd>
     * </dl>
     * For testing and for the use of @PublicForTesting it is now public.
     *
     * @param lg the log
     */
    @PublicForTesting
    public void logMessage(Logger lg) {
        switch (responseCode) {
        case -1:
            lg.info("can't connect to " + url);
            break;
        case 200:
            lg.info(url + " is ok");
            break;
        case 303:
            lg.info("too much traffic to " + url);
            break;
        case 404:
            lg.info(url + " has Alzheimer");
            break;
        default:
            lg.info(url + " responded with " + this.responseCode);
            break;
        }
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(final String[] args) {
        String host = (args.length == 0) ? "http://localhost" : args[0];
        try {
            WebDog dog = new WebDog(new URL(host));
            dog.watch();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            /*
             *  If you uncomment the line above you'll get a warning.
             *  You should use better a log statement to print the exception!
             */
            log.info("oops", e);
        }
    }

}
