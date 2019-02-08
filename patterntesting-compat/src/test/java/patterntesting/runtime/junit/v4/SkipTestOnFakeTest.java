/*
 * $Id: SkipTestOnFakeTest.java,v 1.4 2016/12/18 20:19:36 oboehm Exp $
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
 * (c)reated 06.08.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit.v4;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import patterntesting.runtime.annotation.SkipTestOn;
import patterntesting.runtime.junit.SmokeRunner;



/**
 * This test should be never skipped because a "fake" on Mac does not exist.
 * It is a unit test for the SkipTestOn annotation before a class definition.
 *
 * @author oliver
 * @since 1.0.2 (06.08.2010)
 */
@SkipTestOn(osName = "Mac OS X", osArch = "fake")
@RunWith(SmokeRunner.class)
public final class SkipTestOnFakeTest {

    private static final Logger log = LogManager.getLogger(SkipTestOnFakeTest.class);

    /**
     * Test dummy which should be always called.
     */
    @Test
    public void testDummy() {
        log.info("testDummy() called");
    }

}

