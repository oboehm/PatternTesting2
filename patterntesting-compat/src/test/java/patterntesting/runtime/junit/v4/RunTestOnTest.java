/*
 * $Id: RunTestOnTest.java,v 1.14 2016/12/18 20:19:36 oboehm Exp $
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

package patterntesting.runtime.junit.v4;

import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.LoggerFactory;
import org.slf4j.*;
import org.junit.*;
import org.junit.runner.RunWith;

import patterntesting.runtime.annotation.RunTestOn;
import patterntesting.runtime.junit.SmokeRunner;


/**
 * This is the JUnit4 test for the RunTestOn annotation.
 *
 * @author oliver
 * @since 1.0 (27.01.2010)
 */
@RunWith(SmokeRunner.class)
public final class RunTestOnTest {

    private static final Logger LOG = LoggerFactory.getLogger(RunTestOnTest.class);
    private static String osName = System.getProperty("os.name");
    private static String osArch = System.getProperty("os.arch");
    private static String osVersion = System.getProperty("os.version");

    static {
        LOG.info(RunTestOnTest.class + " is started on " + osName + " "
                + osVersion + " (" + osArch + ")");
    }

    /**
     * This setup method is inserted to see if the statements are really
     * skipped.
     */
    @RunTestOn(osName = "Nirwana OS")
    @BeforeClass
    public static void setUpBeforeClass() {
        fail("setUpBeforeClass() should not be executed on " + osName);
    }

    /**
     * This teardown method is inserted to see if the statements are really
     * skipped.
     */
    @RunTestOn(osName = "Nirwana OS")
    @AfterClass
    public static void tearDownAfterClass() {
        fail("tearDownAfterClass() should not be executed on " + osName);
    }

    /**
     * If you have a Mac this test should be executed. If you don't have one
     * and test test will be executed by accident it will fail.
     */
    @RunTestOn(osName = "Mac OS X", hide = true)
    @Test
    public void testRunOnMac() {
        if (!"Mac OS X".equalsIgnoreCase(osName)) {
            fail("this test should be run only on Mac");
        }
    }

    /**
     * If you have Linux or a Mac this test should be executed. If you don't
     * have either and the test will be executed by accident it will fail.
     */
    @RunTestOn({"Linux", "Mac OS X"})
    @Test
    public void testRunOnMacAndLinux() {
        if (!osName.equalsIgnoreCase("Linux")
                && !osName.equalsIgnoreCase("Mac OS X")) {
            fail("this test should be run only on Linux or Mac");
        }
    }

    /**
     * This test should be executed on an Intel Mac with 64 bit.
     */
    @RunTestOn(osName = "Mac OS X", osArch = "x86_64", hide = true)
    @Test
    public void testRunOnIntelMac64Bit() {
        if (!("Mac OS X".equalsIgnoreCase(osName) && ("x86_64"
                .equalsIgnoreCase(osArch)))) {
            fail("this test should be run only on an Intel Mac 64 bit");
        }
        LOG.info("testRunOnIntelMac64Bit() was excecuted on " + osName + " "
                + osArch);
    }

    /**
     * This test should be executed on an Intel Mac with 32 or 64 bit.
     */
    @RunTestOn(osName = "Mac OS X", osArch = {"x86_32", "x86_64"}, hide = true)
    @Test
    public void testRunOnIntelMac() {
        if (!"Mac OS X".equalsIgnoreCase(osName)
                || (!(osArch.equalsIgnoreCase("x86_62") || osArch
                        .equalsIgnoreCase("x86_64")))) {
            fail("this test should be run only on an an Intel Mac");
        }
        LOG.info("testRunOnMacAndLinux() was excecuted on " + osName + " "
                + osArch);
    }

    /**
     * This test should be never executed because a "fake" architecture does not exist!.
     */
    @RunTestOn(osName = "Mac OS X", osArch = "fake", hide = true)
    @Test
    public void testRunOnFakeMac() {
        fail("this test should be never executed");
    }

    /**
     * This test should be excecuted only on Snow Leopard (Mac OS X 10.6).
     */
    @RunTestOn(osName = "Mac OS X", osVersion = "10.6", hide = true)
    @Test
    public void testRunOnSnowLeopard() {
        if (!"Mac OS X".equalsIgnoreCase(osName) || !osVersion.startsWith("10.6")) {
            fail("this test should be executed only on Snow Leopard");
        }
        LOG.info("testRunOnSnowLeopard() was excecuted on " + osName + " "
                + osVersion);
    }

    /**
     * This test should be executed only on the third and fourth release of
     * Snow Leopard (Mac OS X 10.6.2 and 10.6.3).
     */
    @RunTestOn(osName = "Mac OS X", osVersion = {"10.6.3", "10.6.2"}, hide = true)
    @Test
    public void testRunOnSnowLeopard23() {
        if (!("Mac OS X".equalsIgnoreCase(osName)
                && (osVersion.startsWith("10.6.2") || osVersion
                        .startsWith("10.6.3")))) {
            fail("this test should be executed only on Snow Leopard 10.6.2/3");
        }
        LOG.info("testRunOnSnowLeopard23() was excecuted on " + osName + " "
                + osVersion);
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

    /**
     * This test should not run on weekend. This is tested here.
     */
    @RunTestOn(day = { 1, 2, 3, 4, 5 }, hide = true)
    @Test
    public void testRunOnWorkingDays() {
        int d = getDayOfWeek();
        if (d > 5) {
            fail("This test should not be executed on day " + d);
        }
    }

    /**
     * This test should only run on weekend. This is tested here.
     */
    @RunTestOn(day = { 6, 7 }, hide = true)
    @Test
    public void testRunOnWeekend() {
        int d = getDayOfWeek();
        if (d < 6) {
            fail("This test should not be executed on day " + d);
        }
    }

    /**
     * Test run whole day.
     */
    @RunTestOn(time = { "0:00-8:00", "8:00-16:00", "16:00-24:00" })
    @Test
    public void testRunWholeDay() {
        LOG.info("testRunWholeDay() is executed.");
    }

    /**
     * Gets the day of week.
     *
     * @return the day of week
     */
    protected static int getDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int dow = cal.get(Calendar.DAY_OF_WEEK);
        return ((dow + 5) % 7 + 1);
    }

}
