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

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import patterntesting.runtime.jmx.MBeanHelper;
import patterntesting.runtime.monitor.ProfileMonitor;
import patterntesting.runtime.monitor.ProfileStatistic;
import patterntesting.runtime.monitor.ProfileStatisticTest;

import javax.management.openmbean.TabularData;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link SqlStatistic} class.
 *
 * @author oliver
 * @since 1.4.2 (16.04.2014)
 */
public class SqlStatisticTest extends ProfileStatisticTest {

    private static Logger log = LogManager.getLogger(SqlStatisticTest.class);
    private static String[] sqls = {
        "SELECT a FROM b",
        "SELECT a FROM c",
        "SELECT a FROM d",
        "SELECT a FROM e",
        "SELECT a FROM f"
    };
    private final SqlStatistic instance = SqlStatistic.getInstance();

    /**
     * Gets the profile statistic.
     *
     * @return the profile statistic
     * @see ProfileStatisticTest#getProfileStatistic()
     */
    @Override
    protected ProfileStatistic getProfileStatistic() {
        return SqlStatistic.getInstance();
    }

    /**
     * Prepare statistic.
     *
     * @see ProfileStatisticTest#prepareStatistic()
     */
    @BeforeEach
    @Override
    public void prepareStatistic() {
        this.startStopMonitors(sqls);
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
    @Override
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
        assertTrue(s, s.contains("SqlStatistic"));
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

    /**
     * Test method for {@link SqlStatistic#getProfileMonitor(String)}.
     */
    @Override
	@Test
    public void testGetProfileMonitor() {
        String sql = sqls[0];
        ProfileMonitor started = SqlStatistic.start(sql);
        started.stop();
        ProfileMonitor mon = instance.getProfileMonitor(sql);
        assertNotNull(mon);
        assertEquals(started.getLabel(), mon.getLabel());
        assertTrue("at least 1 hit expected", mon.getHits() > 0);
        log.info("mon = {}", mon);
    }

    /**
     * Test register as shutdown hook.
     */
    @Override
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
        checkHits(ProfileStatistic.getInstance(), sqls[0], false);
    }

    private static void checkHits(final ProfileStatistic statistic, final String label, final boolean expected) {
        ProfileMonitor mon = statistic.getProfileMonitor(label);
        if (expected) {
            assertTrue("hits expected for " + mon, mon.getHits() > 0);
        } else {
            assertTrue("no hits expected for " + mon, (mon == null) || (mon.getHits() == 0));
        }
    }

    /**
     * Test method for {@link SqlStatistic#dumpStatisticTo(File)}. For the SQL
     * statistic it is imporant that the label is quoted because a semicolon
     * (";") could be part of a SQL statement. This is tested here.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testDumpStatisticToFile() throws IOException {
        File dumpFile = new File("target", "sql-stat.csv");
        instance.dumpStatisticTo(dumpFile);
        List<String> lines = FileUtils.readLines(dumpFile, StandardCharsets.UTF_8);
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            assertTrue("line " + i + ": " + line, line.startsWith("\""));
        }
    }

    /**
     * Test method for {@link SqlStatistic#registerAsMBean(String)}.
     */
    @Override
	@Test
    public void testRegisterAsMBean() {
        String mbeanName = "test.mon.SqlStat";
        SqlStatistic.registerAsMBean(mbeanName);
        assertTrue("not registered: " + mbeanName, MBeanHelper.isRegistered(mbeanName));
    }

}

