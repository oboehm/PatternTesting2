/*
 * Copyright (c) 2009-2020 by Oliver Boehm
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
 * (c)reated 23.11.2009 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit.extension;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import patterntesting.runtime.annotation.Broken;
import patterntesting.runtime.annotation.RunTestOn;
import patterntesting.runtime.util.Assertions;
import patterntesting.runtime.util.Environment;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Since 0.9.8 you can mark JUnit tests as broken. This is the JUnit test
 * for testing this feature.
 * Since 2.0 you should use {@code @ExtendWith(SmokeTestExtension.class)}
 * together with {@code @Broken} to mark broken tests.
 *
 * @author oliver
 * @since 23.11.2009
 */
@ExtendWith(SmokeTestExtension.class)
public final class BrokenTest {

    private static final Logger log = LogManager.getLogger(BrokenTest.class);
    private static Map<String, Boolean> shouldBeExecuted = new HashMap<>();

    @BeforeAll
    public static void setUpShouldBeExecuted() {
        shouldBeExecuted.put("testBrokenAndFixed", false);
        shouldBeExecuted.put("testBrokenForUnknownOS", false);
        shouldBeExecuted.put("testBrokenForUnknownUser", false);
        shouldBeExecuted.put("testBrokenExpectException", true);
    }

    /**
     * Dummy test to see if JUnit is working.
     */
    @Test
    public void testDummy() {
        log.debug("testDummy() called");
    }

    /**
     * Dummy test which should be skipped (because it is marked as "@Broken").
     */
    @Broken(why = "marked as broken for testing", hide = true)
    @Test
    public void testBroken() {
        fail("this JUnit test should not be called because it is marked as @Broken");
    }

    /**
     * Dummy test which throws a RuntimeException and should be skipped
     * (because it is marked as "@Broken").
     */
    @Broken(why = "marked as broken for testing", hide = true)
    @Test
    public void testBrokenRuntimeException() {
        throw new RuntimeException("this test should be never executed");
    }

    /**
     * What happens if we combine <code>@Broken</code> with
     * <code>@SkipTestOn</code> or <code>@RunTestOn</code>?
     * The test is "broken" and should not be executed!.
     */
    @Broken(why = "marked as broken for testing", hide = true)
    @RunTestOn(day = { 1, 2, 3, 4, 5, 6, 7 }, hide = true)
    @Test
    public void testBrokenPrecedence() {
        fail("this JUnit test should not be called because it is marked as @Broken");
    }

    /**
     * The 'till' date is reached so this test should fail (e.g. throw an
     * AssertionError).
     */
    @Broken(till = "22-Nov-2009")
    @Test
    public void testBrokenAndFixed() {
        shouldBeExecuted.put("testBrokenAndFixed", true);
    }

    /**
     * For some tests wee need assertions enabled. This is tested here.
     */
    @Test
    public void testAssertion() {
        assertTrue(Assertions.ENABLED, "please enable assertions ('java -ea ...')");
    }

    /**
     * If you have a Mac this test should be skipped. If you don't have one
     * the test will be executed but should not fail.
     */
    @Broken(osName = "Mac OS X")
    @Test
    public void testBrokenForMac() {
        String os = System.getProperty("os.name");
        log.info("os.name=" + os);
        if (os.equalsIgnoreCase("Mac OS X")) {
            fail("this test should be skipped for MacOS");
        }
    }

    /**
     * If you have Linux or a Mac this test should be skipped. If you don't
     * have either the test will be executed but should not fail.
     */
    @Broken(osName = {"Linux", "Mac OS X"}, hide = true)
    @Test
    public void testBrokenForMacAndLinux() {
        String os = System.getProperty("os.name");
        log.info("os.name=" + os);
        if (os.equalsIgnoreCase("Linux") || os.equalsIgnoreCase("Mac OS X")) {
            fail("this test should be skipped for MacOS and Linux");
        }
    }

    /**
     * There should be no "unknown OS" in the world so this test should be
     * executed and produce an AssertionError.
     */
    @Broken(osName = "unknown OS")
    @Test
    public void testBrokenForUnknownOS() {
        shouldBeExecuted.put("testBrokenForUnknownOS", true);
    }

    /**
     * For user "oliver" this test should be skipped.
     */
    @Broken(user = "oliver")
    @Test
    public void testBrokenForOliver() {
        String user = System.getProperty("user.name");
        log.info("user.name=" + user);
        if (user.equalsIgnoreCase("oliver")) {
            fail("this test should be skipped for Oliver");
        }
    }

    /**
     * For users "oliver" and "stan" this test should be skipped.
     */
    @Broken(user = {"stan", "oliver"})
    @Test
    public void testBrokenForOliverAndStan() {
        String user = System.getProperty("user.name");
        log.info("user.name=" + user);
        if (user.equalsIgnoreCase("oliver") || user.equalsIgnoreCase("stan")) {
            fail("this test should be skipped for Stan & Oli");
        }
    }

    /**
     * You should not have a user account "unknown user with no name" on your
     * system otherwise this test will not be executed.
     */
    @Broken(user = "unknown user with no name")
    @Test
    public void testBrokenForUnknownUser() {
        shouldBeExecuted.put("testBrokenForUnknownUser", true);
    }

    /**
     * There was a problem with JUnit tests that expect an Exception.
     * Although the test is skipped the exception should be thrown!
     */
    @Broken(why = "marked as broken for testing", hide = true)
    @Test
    public void testBrokenExpectException() {
        assertThrows(RuntimeException.class, () -> {
            throw new RuntimeException("Ups");
        });
        shouldBeExecuted.put("testBrokenExpectException", true);
    }

    @AfterAll
    public static void testExecutions() {
        if (Environment.SMOKE_TEST_ENABLED) {
            return;
        }
        for (Map.Entry<String, Boolean> entry : shouldBeExecuted.entrySet()) {
            assertTrue(entry.getValue(), "not executed: " + entry.getKey());
        }
    }

}
