/*
 * $Id: TraceTest.java,v 1.7 2016/12/18 20:19:37 oboehm Exp $
 *
 * Copyright (c) 2010 by Oliver Boehm
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
 * (c)reated 30.09.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.log;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.jupiter.api.Test;

import patterntesting.runtime.annotation.DontTraceMe;
import patterntesting.runtime.annotation.TraceMe;

/**
 * This class is for testing of the TraceAspect. It is not really an
 * automatic unit test. It is only to see if the trace log looks ok.
 *
 * @author oliver
 * @since 1.0.3 (30.09.2010)
 */
@TraceMe
public class TraceTest {

    private static Logger log = LogManager.getLogger(TraceTest.class);

    /**
     * We want to see something in the log.
     */
    @Test
    public void testDoSomething() {
        Date today = getDate();
        try {
            info("\t\thello, today is " + today);
        } catch (InterruptedException e) {
            log.trace("ups", e);
        }
    }

    private static Date getDate() {
        return new Date();
    }

    @DontTraceMe
    private static void info(final String msg) throws InterruptedException {
        log.trace(msg);
        interrupt(msg.trim());
    }

    private static void interrupt(final String msg) throws InterruptedException {
        throw new InterruptedException(msg);
    }

}

