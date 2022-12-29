/*
 * $Id: PerfLoggerTest.java,v 1.8 2016/12/18 20:19:36 oboehm Exp $
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
 * (c)reated 23.03.2014 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.log;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.LoggerFactory;
import org.slf4j.*;
import org.junit.jupiter.api.Test;

/**
 * JUnit tests for {@link PerfLogger} class.
 *
 * @author oliver
 * @version $Revision: 1.8 $
 * @since 1.4.1 (23.03.2014)
 */
public class PerfLoggerTest {

    private static final Logger log = LoggerFactory.getLogger(PerfLoggerTest.class);
    private final static PerfLogger perfLogger = new PerfLogger(log);

    /**
     * With this test we want to test the {@link StopWatch}.
     */
    @Test
    public void testStopWatch() {
        StopWatch sw = new StopWatch();
        sw.start();
        int n = 1000;
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += i;
        }
        sw.stop();
        log.info("sum(1..{}) = {} ({})", n, sum, sw);
    }

    /**
     * Test method for {@link PerfLogger#end(java.lang.String, java.lang.Object[])}.
     */
    @Test
    public void testEnd() {
        perfLogger.start("testEnd()");
        perfLogger.end("testEnd()");
    }

    /**
     * Test method for {@link PerfLogger#log(int, String, Object...)}.
     */
    @Test
    public void testLog() {
        for (int level = SimpleLog.LOG_LEVEL_TRACE; level <= SimpleLog.LOG_LEVEL_FATAL; level++) {
            perfLogger.log(level, "Log level is {}.", level);
        }
    }

}

