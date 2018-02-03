/*
 * $Id: ProfileStatisticTest.java,v 1.21 2016/12/18 20:19:39 oboehm Exp $
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
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.AdviceSignature;
import org.aspectj.lang.reflect.CatchClauseSignature;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import patterntesting.runtime.jmx.MBeanHelper;
import patterntesting.runtime.util.SignatureHelper;
import patterntesting.runtime.util.ThreadUtil;

import javax.management.JMException;
import javax.management.MBeanServer;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.UUID;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link ProfileStatistic} class.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.21 $
 * @since 22.12.2008
 */
public class ProfileStatisticTest {

    private static final Logger log = LogManager.getLogger(ProfileStatisticTest.class);
    private static final MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
    private static Signature[] signatures = new Signature[5];
    private final ProfileStatistic profileStatistic = getProfileStatistic();

    /**
     * Gets the profile statistic. This method should be overwritten by
     * subclasses.
     *
     * @return the profile statistic
     */
    protected ProfileStatistic getProfileStatistic() {
        return ProfileStatistic.getInstance();
    }

    /**
     * Sets up some 5 different monitors for testing.
     */
    @BeforeClass
    public static void setUp() {
        signatures[0] = createMock(Signature.class);
        signatures[1] = createMock(AdviceSignature.class);
        signatures[2] = createMock(CatchClauseSignature.class);
        signatures[3] = createMock(CodeSignature.class);
        signatures[4] = createMock(ConstructorSignature.class);
    }

    /**
     * For some tests we need some statistic dates. These are provided by
     * this method.
     */
    @Before
    public void prepareStatistic() {
        synchronized (profileStatistic) {
            String[] labels = new String[signatures.length];
            for (int i = 0; i < signatures.length; i++) {
                labels[i] = SignatureHelper.getAsString(signatures[i]);
            }
            startStopMonitors(labels);
        }
    }

    /**
     * Starts the monitors for the given labels.
     *
     * @param labels the labels
     */
    protected void startStopMonitors(final String... labels) {
        ProfileMonitor[] monitors = new ProfileMonitor[labels.length];
        int[] hits = new int[labels.length];
        for (int i = 0; i < labels.length; i++) {
            monitors[i] = this.getProfileStatistic().startProfileMonitorFor(labels[i]);
            hits[i] = monitors[i].getHits();
        }
        int n = profileStatistic.getMonitors().length;
        for (int i = 0; i < labels.length; i++) {
            monitors[i].stop();
            log.info("monitor[" + i + "] = " + monitors[i]);
            if (n < profileStatistic.getMaxSize()) {
                assertEquals(hits[i] + 1, monitors[i].getHits());
            }
            assertTrue(monitors[i].getLastValue() >= 0.0);
        }
    }

    /**
     * In the past we have sometimes a NullPointerException when the
     * reset()-method is called the first time. This should not happen.
     * <p>
     * After a reset the {@link ProfileStatistic} for methods with hit = 0
     * should not be lost. The list of methods are needed to find "dead"
     * methods.
     * </p>
     */
    @Test
    public void testReset() {
        synchronized (profileStatistic) {
            profileStatistic.init(Dummy.class);
            profileStatistic.reset();
            ProfileMonitor[] monitors = profileStatistic.getSortedMonitors();
            assertTrue(profileStatistic + " should not be empty", monitors.length > 0);
        }
    }

    /**
     * Test max hits.
     */
    @Test
    public final void testMaxHits() {
        synchronized (profileStatistic) {
            ProfileMonitor profMon = ProfileStatistic.start(signatures[0]);
            profMon.stop();
            assertTrue(getProfileStatistic().getMaxHits() > 0);
        }
    }

    /**
     * Test max avg.
     */
    @Test
    public final void testMaxAvg() {
        synchronized (profileStatistic) {
            ProfileMonitor profMon = ProfileStatistic.start(signatures[1]);
            ThreadUtil.sleep();
            profMon.stop();
            ProfileStatistic statistic = getProfileStatistic();
            double maxAvg = statistic.getMaxAvg();
            log.info("maxAvg: " + maxAvg + " ms from " + statistic.getMaxAvgLabel());
            assertTrue("maxAvg=" + maxAvg, maxAvg > 0.0);
        }
    }

    /**
     * Test m bean registration.
     *
     * @throws JMException the JM exception
     */
    @Test
    public final void testMBeanRegistration() throws JMException {
        Integer expected = profileStatistic.getMaxHits();
        Integer hits = (Integer) mbeanServer.getAttribute(profileStatistic.getMBeanName(), "MaxHits");
        log.info("max hits: " + hits);
        assertTrue(hits >= 0);
        assertEquals(expected, hits);
    }

    /**
     * Test log statistic.
     */
    @Test
    public final void testLogStatistic() {
        getProfileStatistic().logStatistic();
    }

    /**
     * Test dump statistic.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public final void testDumpStatistic() throws IOException {
        File dumpfile = profileStatistic.dumpStatistic();
        checkDumpfile(dumpfile);
    }

    /**
     * Test method for {@link ProfileStatistic#dumpStatistic(String)}.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public final void testDumpStatisticString() throws IOException {
        File dumpfile = new File("target", "profstat.csv");
        profileStatistic.dumpStatistic(dumpfile.getPath());
        checkDumpfile(dumpfile);
    }

    private static void checkDumpfile(final File dumpfile) {
        try {
            assertTrue(dumpfile + " does not exist", dumpfile.exists());
            log.info("Statistic was dumped to {} ({} bytes).", dumpfile, dumpfile.length());
        } finally {
            if (dumpfile.delete()) {
                log.info("{} is deleted.", dumpfile);
            }
        }
    }

    /**
     * Test get sorted monitors.
     */
    @Test
    public final void testGetSortedMonitors() {
        ProfileMonitor[] monitors = profileStatistic.getSortedMonitors();
        assertTrue(monitors[0].getTotal() >= monitors[1].getTotal());
    }

    /**
     * Test method for {@link ProfileStatistic#getProfileMonitor(String)}.
     */
    @Test
    public void testGetProfileMonitor() {
        Signature sig = signatures[0];
        ProfileMonitor started = ProfileStatistic.start(sig);
        started.stop();
        ProfileMonitor mon = profileStatistic.getProfileMonitor(sig);
        assertEquals(started.getLabel(), mon.getLabel());
        assertTrue("at least 1 hit expected", mon.getHits() > 0);
        log.info("mon = {}", mon);
    }

    /**
     * Test method for {@link ProfileStatistic#setMaxSize(int)}.
     *
     * @since 1.6
     */
    @Test
    public void testSetSize() {
    	profileStatistic.setMaxSize(4);
    	assertEquals(4, profileStatistic.getMaxSize());
    }

    /**
     * To avoid too much data the size of the statistic should be limited to
     * maximal 100 entries. This is tested here.
     *
     * @since 1.6
     */
    @Test
    public void testGetSize() {
        int newSize = profileStatistic.getMonitors().length + 5;
    	profileStatistic.setMaxSize(newSize);
    	for (int i = 0; i < 10; i++) {
			this.startStopMonitors(UUID.randomUUID().toString());
		}
    	ProfileMonitor[] monitors = profileStatistic.getSortedMonitors();
    	log.info("Size of monitors is {}.", monitors.length);
        assertTrue("expected: " + monitors.length + " <= " + newSize, monitors.length <= newSize);
    }

    /**
     * Test register as shutdown hook.
     */
    @Test
    public void testRegisterAsShutdownHook() {
        ProfileStatistic.addAsShutdownHook();
    }

    /**
     * Test method for {@link ProfileStatistic#registerAsMBean(String)}.
     */
    @Test
    public void testRegisterAsMBean() {
        String mbeanName = "test.monitor.ProfileStatistic";
        ProfileStatistic.registerAsMBean(mbeanName);
        assertTrue("not registered: " + mbeanName, MBeanHelper.isRegistered(mbeanName));
    }

    /**
     * Resets the ProfileStatistc to a default value for tests after this class.
     */
    @After
    public void resetProfileStatistic() {
        profileStatistic.setMaxSize(100);
    }
    
}
