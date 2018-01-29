/*
 * $Id: RunTestOnTest.java,v 1.8 2016/12/23 07:59:18 oboehm Exp $
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
 * (c)reated 27.01.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit.v3;

import org.apache.logging.log4j.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import junit.framework.TestCase;
import patterntesting.runtime.annotation.RunTestOn;
import patterntesting.runtime.junit.SmokeRunner;

/**
 * This is the JUnit3 test for the RunTestOnAspect.
 *
 * @author oliver
 * @since 1.0 (27.01.2010)
 */
@RunWith(SmokeRunner.class)
public final class RunTestOnTest extends TestCase {

    private static final Logger log = LogManager.getLogger(RunTestOnTest.class);

    /**
     * This test should run only on a Mac. On all other platforms the test is
     * skipped (if not it will fail).
     */
    @RunTestOn(osName = "Mac OS X", hide = true)
    public void testRunOnMac() {
        String os = System.getProperty("os.name");
        log.info("os.name=" + os);
        if (!os.equalsIgnoreCase("Mac OS X")) {
            fail("this test should run only on MacOS");
        }
    }

    /**
     * This test should run only on Linux. On all other platforms the test is
     * skipped (if not it will fail).
     */
    @RunTestOn(osName = "Linux", hide = true)
    public void testRunOnLinux() {
        String os = System.getProperty("os.name");
        log.info("os.name=" + os);
        if (!os.equalsIgnoreCase("Linux")) {
            fail("this test should run only on Linux");
        }
    }

    /**
     * This test should run only on Windows. On all other platforms the test is
     * skipped (if not it will fail).
     */
    @RunTestOn(osName = "Win", hide = true)
    public void testRunOnWindows() {
        String os = System.getProperty("os.name");
        log.info("os.name=" + os);
        if (!os.startsWith("Win")) {
            fail("this test should run only on Linux");
        }
    }

    /**
     * This test should be never executed because a "fake" version does not
     * exist! And you should not see it in the list of executed unit tests
     * because the flag "hide" is set to true.
     */
    @RunTestOn(osName = "Mac OS X", osVersion = "fake", hide = true)
    @Test
    public void testRunOnFakeVersion() {
        fail("this test should be never executed");
    }

}

