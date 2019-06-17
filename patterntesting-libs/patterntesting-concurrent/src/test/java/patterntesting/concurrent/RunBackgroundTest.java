/*
 * $Id: RunBackgroundTest.java,v 1.8 2016/12/18 21:56:49 oboehm Exp $
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
package patterntesting.concurrent;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.apache.logging.log4j.*;
import org.apache.logging.log4j.LogManager;

import patterntesting.annotation.concurrent.RunBackground;
import patterntesting.runtime.log.LogRecorder;
import patterntesting.runtime.util.ThreadUtil;

/**
 * The Class RunBackgroundTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.8 $
 * @since 19.01.2009
 */
public class RunBackgroundTest {

    private static final Logger log = LogManager.getLogger(RunBackgroundTest.class);

    /**
     * Test background run.
     */
    @Test
    public final void testBackgroundRun() {
        LogRecorder watched = new LogRecorder();
        assertEquals(0, watched.getNumberOfRecords());
        slowLog(watched);
        assertEquals("Is it started as own thread?", 0, watched
                .getNumberOfRecords());
        ThreadUtil.sleep();
        log.info("record logs from background call: " + watched);
    }

    @RunBackground
    private static void slowLog(final Logger watched) {
        ThreadUtil.sleep();
        watched.info("slowLog(..) finished");
    }

}
