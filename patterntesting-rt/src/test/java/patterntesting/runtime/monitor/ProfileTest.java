/*
 * Copyright (c) 2008-2023 by Oliver Boehm
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
 * (c)reated 22.12.2008 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.monitor;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import patterntesting.runtime.annotation.ProfileMe;

import javax.management.JMException;
import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

/**
 * The Class ProfileTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.17 $
 * @since 22.12.2008
 */
public final class ProfileTest {

    private static final Logger log = LoggerFactory.getLogger(ProfileTest.class);
    private static final MBeanServer mbeanServer = ManagementFactory
            .getPlatformMBeanServer();
    private static final ProfileStatistic statistic = ProfileStatistic.getInstance();

    /**
     * A method of the Dummy class is called so that the ProflieStatistic for
     * it is activated.
     */
    @BeforeAll
    public static void registerDummy() {
        Dummy.hello();
    }

    /**
     * Instantiates a new profile test.
     */
    @ProfileMe
    public ProfileTest() {
        log.debug(this + " created");
    }

    /**
     * To be able to start the JUnit tests outside Eclipse this main method
     * is provided. If you start it with
     * <code>-Dcom.sun.management.jmxremote</code>.
     * In the JConsole you'll see the methods of the Dummy class which has
     * "0 hits", i.e. they are never called.
     *
     * @param args is ignored (no arguments are required)
     *
     * @throws Throwable the throwable
     */
    public static void main(final String[] args) throws Throwable {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass(ProfileTest.class))
                .build();
        Launcher launcher = LauncherFactory.create();
        TestPlan testPlan = launcher.discover(request);
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);
        Thread.sleep(300000);
    }

    /**
     * Test profiler.
     *
     * @throws JMException the JM exception
     */
    @Test
    @ProfileMe
    public void testProfiler() throws JMException {
        int calls = 2;
        for (int i = 0; i < calls; i++) {
            callDummy();
        }
        Integer maxCalls = (Integer) mbeanServer.getAttribute(statistic.getMBeanName(), "MaxHits");
        assertTrue(maxCalls >= calls);
    }

    @ProfileMe
    private void callDummy() {
        String hello = Dummy.hello();
        log.info("Dummy.hello() says '{}'.", hello);
    }

    /**
     * Test run.
     *
     * @throws JMException the JM exception
     */
    @Test
    public void testRun() throws JMException {
        mbeanServer.invoke(statistic.getMBeanName(), "logStatistic", null, null);
    }

    /**
     * Test get profile monitor.
     */
    @Test
    public void testGetProfileMonitor() {
    		log.info("testGetProfileMonitor() is started.");
        synchronized (statistic) {
            callDummy();    // we must call it for this test!
            ProfileMonitor monitor = statistic.getProfileMonitor(this.getClass(),
                    "callDummy()");
            if (monitor == null) {
                log.error("No statistic found for {} and method callDummy().", this.getClass());
                statistic.logStatistic();
                fail("callDummy() does not appear in statistic.");
            }
            assertTrue(monitor.getHits() > 0, "no hit received for " + monitor);
        }
    }

    /**
     * Test hits.
     */
    @Test
    public void test0Hits() {
        synchronized (statistic) {
            checkDummyNotCalled("dontCallMe()");
        }
    }

    /**
     * Dummy.neverCalled() is marked as "@DontProfileMe". So it should not
     * appear as profiled method.
     */
    @Test
    public void testNotProfiled() {
        checkNotProfiled(Dummy.class, "neverCalled()");
        Dummy.dontProfileMe();
        checkNotProfiled(Dummy.class, "dontProfileMe()");
    }

    /**
     * We don't want to see the 0-hits entries of the superclass, e.g.
     * 0 hits for Object.wait() - they have no value for us.
     */
    @Test
    public void test0HitsSuperClass() {
        checkNotProfiled(Object.class, "wait()");
    }

    /**
     * Test0 hits after reset.
     */
    @Test
    public void test0HitsAfterReset() {
        synchronized (statistic) {
            statistic.reset();
            test0Hits();
        }
    }

    /**
     * Test0 hits constructor.
     */
    @Test
    public void test0HitsConstructor() {
        synchronized (statistic) {
            check0HitsConstructor();
        }
    }

    private void check0HitsConstructor() {
        Dummy.hello();
        ProfileMonitor monitor = statistic.getProfileMonitor("new " + Dummy.class.getName() + "()");
        assertTrue(monitor == null || monitor.getHits() == 0, "hits recorded with " + monitor);
    }

    /**
     * Test0 hits constructor after reset.
     */
    @Test
    public void test0HitsConstructorAfterReset() {
        synchronized (statistic) {
            statistic.reset();
            check0HitsConstructor();
        }
    }

    private static void checkDummyNotCalled(final String method) {
        synchronized (statistic) {
            ProfileMonitor monitor = statistic.getProfileMonitor(Dummy.class, method);
            assertTrue(monitor == null || monitor.getHits() == 0, "hits recorded with " + monitor);
        }
    }

    /**
     * The method of the given class should never appear as profiled method.
     *
     * @param cl checked class
     * @param method checked method
     */
    private static void checkNotProfiled(final Class<?> cl, final String method) {
        synchronized (statistic) {
            ProfileMonitor monitor = statistic.getProfileMonitor(cl, method);
            assertNull(monitor, "found: " + monitor);
        }
    }

}
