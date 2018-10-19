/*
 * $Id: MemoryGuardTest.java,v 1.8 2016/12/18 20:19:39 oboehm Exp $
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
 * (c)reated 19.01.2009 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.monitor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import patterntesting.runtime.annotation.IntegrationTest;
import patterntesting.runtime.junit.SmokeRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * The Class MemoryGuardTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.8 $
 * @since 19.01.2009
 */
@RunWith(SmokeRunner.class)
public final class MemoryGuardTest {

    private static final Logger LOG = LogManager.getLogger(MemoryGuardTest.class);

    /**
     * Test method for {@link patterntesting.runtime.monitor.MemoryGuard#getFreeMemoryInPercent()}.
     */
    @Test
    public void testGetFreeMemoryInPercent() {
        int rate = MemoryGuard.getFreeMemoryInPercent();
        LOG.info("free memory: " + rate + " %");
        assertTrue(rate + " should be positiv", rate >= 0);
        assertTrue(rate + " < 100 expected", rate <= 100);
    }

    /**
     * Test get free memory.
     */
    @Test
    public void testGetFreeMemory() {
        long freeMem = MemoryGuard.getFreeMemory();
        LOG.info("free memory: " + MemoryGuard.getFreeMemoryAsString());
        assertTrue(freeMem > 0);
        assertTrue(freeMem < Runtime.getRuntime().maxMemory());
    }

    /**
     * Test log memory.
     */
    @Test
    public void testLogMemory() {
        MemoryGuard.logMemory();
    }

    /**
     * Test log memory log.
     */
    @Test
    public void testLogMemoryLog() {
        MemoryGuard.logMemory(LOG);
    }

    /**
     * Test log memory log4 j.
     */
    @Test
    public void testLogMemoryLog4J() {
        Logger lg = LogManager.getLogger("test");
        MemoryGuard.logMemory(lg);
    }

    /**
     * Test log free memory.
     *
     * @throws InterruptedException the interrupted exception
     */
    @Test
    @IntegrationTest
    public void testLogFreeMemory() throws InterruptedException {
        MemoryGuard.logFreeMemory(10);
    }

    /**
     * What happens if logFreeMemory(..) is called twice? The running
     * background thread should be cancelled and a new one with the new
     * polling time should be started.
     * Watch the log to see if it works.
     *
     * @throws InterruptedException the interrupted exception
     */
    @Test
    @IntegrationTest
    public void testLogFreeMemoryTwice() throws InterruptedException {
    	MemoryGuard.logFreeMemory(20);
    }

    /**
     * Test method for {@link MemoryGuard#getMemoryLogMessage()}.
     */
    @Test
    public void testGetMemoryLogMessage() {
        String msg = MemoryGuard.getMemoryLogMessage();
        assertNotNull(msg);
        LOG.info("msg = \"{}\"", msg);
    }

    /**
     * After the end of this test no background logging should be done any
     * more.
     *
     * @throws InterruptedException the interrupted exception
     */
    @AfterClass
    public static void stopBackgroundLogger() throws InterruptedException {
        MemoryGuard.logFreeMemory(0);
        try {
            Thread.sleep(1);
        } catch (InterruptedException ie) {
            LOG.info("Could not sleep:", ie);
        }
        LOG.info("Background logging should be stopped now.");
    }

}
