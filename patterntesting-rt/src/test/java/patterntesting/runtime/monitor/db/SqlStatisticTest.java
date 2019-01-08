/*
 * $Id: SqlStatisticTest.java,v 1.13 2016/12/18 20:19:39 oboehm Exp $
 *
 * Copyright (c) 2014 by Oliver Boehm
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
 * (c)reated 16.04.2014 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.monitor.db;

import clazzfish.jdbc.monitor.ProfileMonitor;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import patterntesting.runtime.jmx.MBeanHelper;
import patterntesting.runtime.monitor.ProfileStatistic;

import javax.management.openmbean.TabularData;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link SqlStatistic} class.
 *
 * @author oliver
 * @since 1.4.2 (16.04.2014)
 */
public class SqlStatisticTest {

    private static Logger log = LogManager.getLogger(SqlStatisticTest.class);
    private static String[] sqls = {
        "SELECT a FROM b",
        "SELECT a FROM c",
        "SELECT a FROM d",
        "SELECT a FROM e",
        "SELECT a FROM f"
    };
    private SqlStatistic instance;

    /**
     * Prepare statistic.
     */
    @BeforeEach
    public void prepareStatistic() {
        instance = SqlStatistic.getInstance();
        this.startStopMonitors(sqls);
    }

    /**
     * Starts the monitors for the given labels.
     *
     * @param labels the labels
     */
    private void startStopMonitors(final String... labels) {
        ProfileMonitor[] monitors = new ProfileMonitor[labels.length];
        int[] hits = new int[labels.length];
        for (int i = 0; i < labels.length; i++) {
            monitors[i] = this.instance.startProfileMonitorFor(labels[i]);
            hits[i] = monitors[i].getHits();
        }
        int n = instance.getMonitors().length;
        for (int i = 0; i < labels.length; i++) {
            monitors[i].stop();
            log.info("monitor[" + i + "] = " + monitors[i]);
            if (n < instance.getMaxSize()) {
                assertEquals(hits[i] + 1, monitors[i].getHits());
            }
            assertTrue(monitors[i].getLastValue() >= 0.0);
        }
    }

    /**
     * Test method for {@link SqlStatistic#getInstance()}.
     */
    @Test
    public void testGetInstance() {
        assertEquals(SqlStatistic.class, instance.getClass());
    }

    /**
     * In contrast to {@link ProfileStatistic} the SQL statements are not
     * needed after a reset. Unfortunately with JAMon 2.81 an exception entry
     * is added to the list of monitors - this is accepted in this test.
     */
    @Test
    public final void testReset() {
        instance.reset();
        TabularData statistics = instance.getStatistics();
        assertThat(statistics.size(), lessThan(2));
    }

    /**
     * The name of the class should be part of the toString implementation.
     */
    @Test
    public void testToString() {
        String s = instance.toString();
        assertTrue(s.contains("SqlStatistic"), s);
        log.info("s = \"{}\"", s);
    }

    /**
     * Test method for {@link SqlStatistic#start()}.
     */
    @Test
    public void testStart() {
        ProfileMonitor mon = SqlStatistic.start("DROP table dummy");
        mon.stop();
        log.info("SQL statistic: {}", mon);
    }

    /**
     * The label should be trimmed. This is tested here.
     */
    @Test
    public void testGetLabel() {
        ProfileMonitor mon = SqlStatistic.start("   DROP table dummy   ");
        mon.stop();
        assertEquals("DROP table dummy", mon.getLabel());
    }

	@Test
    public void testGetProfileMonitor() {
        String sql = sqls[0];
        ProfileMonitor started = SqlStatistic.start(sql);
        started.stop();
        ProfileMonitor mon = instance.getMonitor(sql);
        assertNotNull(mon);
        assertEquals(started.getLabel(), mon.getLabel());
        assertTrue(mon.getHits() > 0, "at least 1 hit expected");
        log.info("mon = {}", mon);
    }

    /**
     * Test register as shutdown hook.
     */
	@Test
    public void testRegisterAsShutdownHook() {
        SqlStatistic.addAsShutdownHook();
    }

    /**
     * Although {@link SqlStatistic} is derived from {@link ProfileStatistic} it
     * should interact independant from it. I.e. if some SQL monitor is added to
     * {@link SqlStatistic} this should not influence {@link ProfileStatistic}.
     */
    @Test
    public void testInstance() {
        checkHits(instance, sqls[0], true);
    }

    private static void checkHits(final SqlStatistic statistic, final String label, final boolean expected) {
        ProfileMonitor mon = statistic.getMonitor(label);
        if (expected) {
            assertTrue(mon.getHits() > 0, "hits expected for " + mon);
        } else {
            assertTrue((mon == null) || (mon.getHits() == 0), "no hits expected for " + mon);
        }
    }

    @Test
    public void testDumpStatisticToFile() throws IOException {
        File dumpFile = new File("target", "sql-stat.csv");
        instance.dumpMe(dumpFile);
        List<String> lines = FileUtils.readLines(dumpFile, StandardCharsets.UTF_8);
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            assertTrue(line.startsWith("\""), "line " + i + ": " + line);
        }
    }

    /**
     * Test method for {@link SqlStatistic#registerAsMBean(String)}.
     */
	@Test
    public void testRegisterAsMBean() {
        String mbeanName = "test.mon.SqlStat";
        SqlStatistic.registerAsMBean(mbeanName);
        assertTrue(MBeanHelper.isRegistered(mbeanName), "not registered: " + mbeanName);
    }

}

