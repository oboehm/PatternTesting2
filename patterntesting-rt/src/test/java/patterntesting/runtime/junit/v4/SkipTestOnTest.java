/*
 * $Id: SkipTestOnTest.java,v 1.18 2016/12/18 20:19:36 oboehm Exp $
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
 * (c)reated 25.01.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit.v4;

import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;

import patterntesting.runtime.annotation.IntegrationTest;
import patterntesting.runtime.annotation.SkipTestOn;
import patterntesting.runtime.junit.SmokeRunner;
import patterntesting.runtime.junit.internal.SmokeFilter;


/**
 * This is the JUnit4 test for the SkipTestOnAspect.
 *
 * @author oliver
 * @since 1.0 (25.01.2010)
 */
@RunWith(SmokeRunner.class)
public final class SkipTestOnTest {

    private static final Logger LOG = LogManager.getLogger(SkipTestOnTest.class);
    private static String osName = System.getProperty("os.name");
    private static String osArch = System.getProperty("os.arch");
    private static String osVersion = System.getProperty("os.version");
    private static String javaVmName = System.getProperty("java.vm.name");
    private static String javaVersion = System.getProperty("java.version");
    private static String javaVendor = System.getProperty("java.vendor");

    /**
     * This setup method is only inserted to see on which platform the test
     * is started.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        LOG.info(SkipTestOnTest.class.getSimpleName() + " is started on "
                + javaVmName + " " + javaVersion + " (" + javaVendor
                + ") under " + osName + " " + osVersion + " (" + osArch + ")");
    }

    /**
     * If no value in SkipTestOn annotation is given an IllegalArgumentException
     * should be thrown.
     *
     * @throws NoSuchMethodException the no such method exception
     * @throws SecurityException the security exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyAnnotation() throws NoSuchMethodException, SecurityException {
        methodWithEmptyAnnotation();
        SmokeFilter smFilter = new SmokeFilter();
        Method m = this.getClass().getMethod("methodWithEmptyAnnotation");
        Description description = Description.createTestDescription(this.getClass(), "methodWithEmptyAnnotation", m.getAnnotations());
        smFilter.shouldBeIgnored(description);
    }

    /**
     * Method with empty annotation.
     */
    @SkipTestOn
    public void methodWithEmptyAnnotation() {
        LOG.info("Nothing defined in @SkipTestOn.");
    }

    /**
     * If you have a Mac this test should be skipped. If you don't have one
     * the test will be executed but should not fail.
     */
    @SkipTestOn("Mac OS X")
    @Test
    public void testSkipOnMacOS() {
        if (SystemUtils.IS_OS_MAC) {
            fail("this test should be skipped for Mac");
        }
        LOG.info("testSkipOnMac() was excecuted on " + osName);
    }

    /**
     * You can use "Mac OS X" or "Mac" to skip this test on a Mac.
     * This is the test for "Mac".
     */
    @SkipTestOn(osName = "Mac", hide = true)
    @Test
    public void testSkipOnMac() {
        if (SystemUtils.IS_OS_MAC) {
            fail("this test should be skipped for Mac");
        }
        LOG.info("testSkipOnMac() was excecuted on " + osName);
    }

    /**
     * If you have Linux or a Mac this test should be skipped. If you don't
     * have either the test will be executed but should not fail.
     */
    @SkipTestOn(osName = {"Linux", "Mac OS X"}, hide = true)
    @Test
    public void testSkipOnMacAndLinux() {
        if (osName.equalsIgnoreCase("Linux")
                || osName.equalsIgnoreCase("Mac OS X")) {
            fail("this test should be skipped for Linux or Mac");
        }
        LOG.info("testSkipOnMacAndLinux() was excecuted on " + osName);
    }

    /**
     * This test should be skipped on an Intel Mac with 64 bit.
     */
    @SkipTestOn(osName = "Mac OS X", osArch = "x86_64")
    @Test
    public void testSkipOnIntelMac64Bit() {
        if ("Mac OS X".equalsIgnoreCase(osName) && ("x86_64".equalsIgnoreCase(osArch))) {
            fail("this test should be skipped for an Intel Mac 64 bit");
        }
    }

    /**
     * This test should be skipped on an Intel Mac with 32 or 64 bit.
     */
    @SkipTestOn(osName = "Mac OS X", osArch = {"x86_32", "x86_64"})
    @Test
    public void testSkipOnIntelMac() {
        if ("Mac OS X".equalsIgnoreCase(osName)
                && (osArch.equalsIgnoreCase("x86_62") || osArch
                        .equalsIgnoreCase("x86_64"))) {
            fail("this test should be skipped for an Intel Mac");
        }
        LOG.info("testSkipOnMacAndLinux() was excecuted on " + osName + " " + osArch);
    }

    /**
     * This test should be never skipped because a "fake" architecture does not exist!.
     */
    @SkipTestOn(osName = "Mac OS X", osArch = "fake")
    @Test(expected = AssertionError.class)
    public void testSkipOnFakeMac() {
        fail("this test should fail otherwise it is not executed");
    }

    /**
     * This test should be skipped on Snow Leopard (Mac OS X 10.6).
     */
    @SkipTestOn(osName = "Mac OS X", osVersion = "10.6")
    @Test
    public void testSkipOnSnowLeopard() {
        if ("Mac OS X".equalsIgnoreCase(osName) && osVersion.startsWith("10.6")) {
            fail("this test should be skipped on Snow Leopard");
        }
        LOG.info("testSkipOnSnowLeopard() was excecuted on " + osName + " "
                + osVersion);
    }

    /**
     * This test should be skipped on the third and fourth release of
     * Snow Leopard (Mac OS X 10.6.2 and 10.6.3).
     */
    @SkipTestOn(osName = "Mac OS X", osVersion = {"10.6.3", "10.6.2"})
    @Test
    public void testSkipOnSnowLeopard23() {
        if ("Mac OS X".equalsIgnoreCase(osName)
                && (osVersion.startsWith("10.6.2") || osVersion
                        .startsWith("10.6.3"))) {
            fail("this test should be skipped on Snow Leopard");
        }
        LOG.info("testSkipOnSnowLeopard23() was excecuted on " + osName + " "
                + osVersion);
    }

    /**
     * This test should be never skipped because a "fake" version does not exist!.
     */
    @SkipTestOn(osName = "Mac OS X", osVersion = "fake")
    @Test(expected = AssertionError.class)
    public void testSkipOnFakeVersion() {
        fail("this test should fail otherwise it is not executed");
    }

    /**
     * This test should be always skipped.
     *
     * @throws UnknownHostException if the local hostname can't be identified
     */
    @SkipTestOn(host = { "127.0.0.1", "localhost" }, hide = true)
    @IntegrationTest("this test needs too long if we are offline")
    @Test
    public void testSkipOnLocalhost() throws UnknownHostException {
        fail("this test should be never executed on " + InetAddress.getLocalHost());
    }

    /**
     * This test should be never skipped because we have no "unknown" host.
     */
    @SkipTestOn(host = "unknown")
    @IntegrationTest("this test needs to long if we are offline")
    @Test(expected = AssertionError.class)
    public void testSkipOnUnknownHost() {
        fail("this test should fail otherwise it is not executed");
    }

    /**
     * This test should be never executed on Java 5.
     */
    @SkipTestOn(javaVersion = "1.5")
    @Test
    public void testSkipOnJava5() {
        if (javaVersion.startsWith("1.5")) {
            fail("this test should be never executed with JDK " + javaVersion);
        }
        LOG.info("testSkipOnJava5() was executed on JDK " + javaVersion);
    }

    /**
     * This test should be never executed on Java 6.
     */
    @SkipTestOn(javaVersion = "1.6", hide = true)
    @Test
    public void testSkipOnJava6() {
        if (javaVersion.startsWith("1.6")) {
            fail("this test should be never executed with JDK " + javaVersion);
        }
        LOG.info("testSkipOnJava6() was executed on JDK " + javaVersion);
    }

    /**
     * This test should be never skipped because a JDK "FiX" does not exist!.
     */
    @SkipTestOn(javaVersion = "FiX")
    @Test(expected = AssertionError.class)
    public void testSkipOnJavaFiX() {
        fail("this test should fail otherwise it is not executed");
    }

    /**
     * This test should be always skipped on a VM from Apple.
     */
    @SkipTestOn(javaVendor = "Apple Inc.")
    @Test
    public void testSkipOnAppleVM() {
        if (javaVendor.equalsIgnoreCase("Apple Inc.")) {
            fail("this test should be never executed for VM of " + javaVendor);
        }
        LOG.info("testSkipOnAppleVM() was executed on VM of " + javaVendor);
    }

    /**
     * This test should be never skipped because a VM from a vendor "Buggy"
     * does not exist.
     */
    @SkipTestOn(javaVendor = "Buggy")
    @Test(expected = AssertionError.class)
    public void testSkipOnBuggyVM() {
        fail("this test should fail otherwise it is not executed");
    }

    /**
     * This test should be always skipped because the property "java.home",
     * which is used here, should exist.
     */
    @SkipTestOn(property = "java.home", hide = true)
    @Test
    public void testSkipOnProperty() {
        fail("this test should be always skipped");
    }

    /**
     * This test should be executed because the property "nir.wana",
     * which is used here, should not exist.
     */
    @SkipTestOn(property = "nir.wana")
    @Test(expected = AssertionError.class)
    public void testSkipOnPropertyNirwana() {
        if (System.getProperty("nir.wana") == null) {
            fail("this test should fail otherwise it is not executed");
        }
    }

    /**
     * This test should not run on weekend. This is tested here.
     */
    @SkipTestOn(day = { 1, 2, 3, 4, 5 }, hide = true)
    @Test
    public void testSkipOnWorkingDays() {
        int d = RunTestOnTest.getDayOfWeek();
        if (d < 6) {
            fail("This test should not be skipped on day " + d);
        }
    }

    /**
     * This test should only skipped on weekend. This is tested here.
     */
    @SkipTestOn(day = { 6, 7 }, hide = true)
    @Test
    public void testRunOnWeekend() {
        int d = RunTestOnTest.getDayOfWeek();
        if (d > 5) {
            fail("This test should not be skipped on day " + d);
        }
    }

    /**
     * Skip test the whole day.
     */
    @SkipTestOn(time = { "0:00-8:00", "8:00-16:00", "16:00-24:00" }, hide = true)
    @Test
    public void testRunWholeDay() {
        fail("This test should be skipped the whole day.");
    }

}
