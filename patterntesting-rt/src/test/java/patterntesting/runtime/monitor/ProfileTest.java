/*
 * $Id: ProfileTest.java,v 1.17 2017/05/11 20:08:56 oboehm Exp $
 *
 * Copyright (c) 2008 by Oliver Boehm
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


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import patterntesting.runtime.annotation.ProfileMe;

import javax.management.JMException;
import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;

import static org.junit.Assert.*;

/**
 * The Class ProfileTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.17 $
 * @since 22.12.2008
 */
public final class ProfileTest {

    private static final Logger log = LogManager.getLogger(ProfileTest.class);
    private static final MBeanServer mbeanServer = ManagementFactory
            .getPlatformMBeanServer();
    private static final ProfileStatistic statistic = ProfileStatistic.getInstance();

    /**
     * A method of the Dummy class is called so that the ProflieStatistic for
     * it is activated.
     */
    @BeforeClass
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
        JUnitCore.runClasses(ProfileTest.class);
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
     * @throws InterruptedException the interrupted exception
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
            if (statistic.isResetted()) {
            		log.info("{} was resetted.", statistic);
            		callDummy();
            }
            ProfileMonitor monitor = statistic.getProfileMonitor(this.getClass(),
                    "callDummy()");
            if (monitor == null) {
            		log.error("No statistic found for {} and method callDummy().", this.getClass());
            		statistic.logStatistic();
            		fail("callDummy() does not appear in statistic.");
            }
            assertTrue("no hit received for " + monitor, monitor.getHits() > 0);
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
        ProfileMonitor monitor = statistic.getProfileMonitor("new "
                + Dummy.class.getName() + "()");
        assertEquals(0, monitor.getHits());
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
            assertEquals(0, monitor.getHits());
        }
    }

    /**
     * The method of the given class should never appear as profiled method.
     *
     * @param cl
     * @param method
     */
    private static void checkNotProfiled(final Class<?> cl, final String method) {
        synchronized (statistic) {
            ProfileMonitor monitor = statistic.getProfileMonitor(cl, method);
            assertNull("found: " + monitor, monitor);
        }
    }

}
