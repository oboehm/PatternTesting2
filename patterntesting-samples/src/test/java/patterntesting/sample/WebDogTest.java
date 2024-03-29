/*
 * Copyright (c) 2009-2019 by Oliver Boehm
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import patterntesting.annotation.check.ct.OnlyForTesting;
import patterntesting.annotation.concurrent.RunBackground;
import patterntesting.runtime.junit.NetworkTester;
import patterntesting.runtime.log.LogRecorder;
import patterntesting.runtime.util.ThreadUtil;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * The Class WebDogTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.2 $
 * @since 09.03.2009
 */
public final class WebDogTest {

    private WebDog dog;
    private static final String TEST_URL = "http://google.com";

    /**
     * Setup.
     *
     * @throws MalformedURLException the malformed url exception
     */
    @BeforeEach
    public void setUp() throws MalformedURLException {
        dog = new WebDog(new URL(TEST_URL));
    }

    /**
     * Test method for {@link WebDog#ping()}.
     */
    @Test
    public void testPing() {
        assumeTrue(NetworkTester.isOnline(URI.create(TEST_URL).getHost(), 80), TEST_URL + " is not online");
        dog.ping();
        int rc = dog.getResponseCode();
        assertThat(rc, greaterThanOrEqualTo(200));
    }

    /**
     * Test response code200.
     */
    @Test
    public void testResponseCode200() {
        checkLog(200, TEST_URL + " is ok");
    }

    /**
     * Test response code303.
     */
    @Test
    public void testResponseCode303() {
        checkLog(303, "too much traffic to " + TEST_URL);
    }

    /**
     * Test response code404.
     */
    @Test
    public void testResponseCode404() {
        checkLog(404, TEST_URL + " has Alzheimer");
    }

    @Test
    public void testResponseCode500() {
        checkLog(500, TEST_URL + " responded with 500");
    }

    /**
     * This method must be marked as "@OnlyForTesting" because otherwise it
     * can't call checkLog(..) which is itself marked as "OnlyForTesting".
     *
     * @param rc return code
     * @param expected expected log message
     */
    @OnlyForTesting
    private void checkLog(final int rc, final String expected) {
        LogRecorder recorder = new LogRecorder();
        dog.setResponseCode(rc);
        dog.logMessage(recorder);
        assertEquals(expected, recorder.getText());
    }

    @Test
    public void testWatch() {
        runWatch();
        ThreadUtil.sleep(200, TimeUnit.MILLISECONDS);
        dog.stop();
    }

    @RunBackground
    private void runWatch() {
        dog.watch();
    }

    @Test
    public void testMain() {
        WebDog.main("nir://wana");
    }

    /**
     * Here we are outside the test methods. So we can't call methods which are
     * marked with @OnlyForTesting (this is statically checked).
     * When we call methods wich are marked @PublicForTesting we'll get an
     * AssertionError (this is dynamically checked).
     * NOTE: you must enable assertions to get the AssertionError
     * (java -ea ...)
     *
     * @param args the args
     *
     * @throws MalformedURLException the malformed url exception
     */
    public static void main(final String[] args) throws MalformedURLException {
        WebDog webDog = new WebDog(new URL(TEST_URL));
        // webDog.setResponseCode(200); // this will get a compile error
        // and here you'll get the AssertionError if assertions enabled
        webDog.logMessage(new LogRecorder());
    }

}
