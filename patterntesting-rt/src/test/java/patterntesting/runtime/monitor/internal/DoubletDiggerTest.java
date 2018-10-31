/*
 * $Id: DoubletDiggerTest.java,v 1.8 2016/12/18 20:19:38 oboehm Exp $
 *
 * Copyright (c) 2016 by Oliver Boehm
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
 * (c)reated 09.03.2016 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.monitor.internal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import patterntesting.runtime.junit.CollectionTester;
import patterntesting.runtime.log.LogWatch;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

/**
 * Unit tests for {@link DoubletDigger} class.
 *
 * @author oliver
 * @since 1.6 (09.03.2016)
 */
public final class DoubletDiggerTest {

    private static final Logger LOG = LogManager.getLogger(DoubletDiggerTest.class);
    private final DoubletDigger digger = new DoubletDigger(new ClasspathDigger());

    /**
     * Test method for {@link DoubletDigger#getDoublet(Class, int)}.
     */
    @Test
    public void testGetDoublet() {
        Class<?> clazz = String.class;
        if (digger.isDoublet(clazz)) {
            assertThat(digger.getDoublet(clazz, 0), not(digger.getDoublet(clazz, 1)));
        }
    }

    /**
     * Test method for {@link DoubletDigger#toString()}.
     */
    @Test
    public void testToString() {
        String s = digger.toString();
        assertThat("looks like default implementation", s, not(containsString("DoubletDigger@")));
        LOG.info("s = \"{}\"", s);
    }

    /**
     * For testing the performance between the different implementations we
     * reset first the doubletList of the ClasspathMonitor.
     */
    @Test
    public void testGetDoubletListPerformance() {
        LOG.info("Testing performance of getDoubletList...");
        warmUpGetDoubletList();
        synchronized (digger) {
            digger.reset();
            LOG.info("{} was resetted for getDoubletListSerial().", digger);
            LogWatch w1 = new LogWatch();
            List<Class<?>> doubletList = digger.getDoubletListSerial();
            w1.stop();
            List<Class<?>> d1 = new ArrayList<>(doubletList);
            digger.reset();
            LOG.info("{} was resetted for getDoubletListParallel().", digger);
            LogWatch w2 = new LogWatch();
            List<Class<?>> d2 = digger.getDoubletListParallel();
            w2.stop();
            LOG.info("getDoubletList() needs           {}.", w1);
            LOG.info("getDoubletListParallized() needs {}.", w2);
            CollectionTester.assertEquals(d1, d2);
            if (w2.getElapsedTime() > w1.getElapsedTime()) {
                LOG.warn("getDoubletList() is faster!!!");
            }
            LOG.info(d2.size() + " doublets found");
        }
    }

    private void warmUpGetDoubletList() {
        LogWatch watch = new LogWatch();
        digger.getDoubletListParallel();
        digger.getDoubletListSerial();
        LOG.debug("Warm-up of getDoubletList finished after {}.", watch);
    }

}

