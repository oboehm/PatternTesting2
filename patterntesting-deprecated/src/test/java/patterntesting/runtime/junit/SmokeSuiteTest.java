/*
 * $Id: SmokeSuiteTest.java,v 1.4 2016/12/18 20:19:38 oboehm Exp $
 *
 * Copyright (c) 2012 by Oliver Boehm
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
 * (c)reated 30.12.2011 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import org.junit.Test;
import org.junit.runner.Runner;
import patterntesting.runtime.annotation.Broken;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * JUnit tests for {@link SmokeSuite} class.
 *
 * @author oliver
 * @since 1.2 (30.12.2011)
 */
public final class SmokeSuiteTest {

    /**
     * If run by {@link SmokeSuite} this test should be called by the
     * {@link SmokeRunner}. This is tested here.
     */
    @Test
    public void testRunner() {
        if (calledBy(SmokeSuite.class)) {
            assertTrue("not called by " + SmokeRunner.class, calledBy(SmokeRunner.class));
        }
    }

    /**
     * This test should be ignored if called by {@link SmokeSuite}.
     */
    @Broken("for testing")
    @Test
    public void testBroken() {
        if (calledBy(SmokeSuite.class)) {
            fail("should be not called because marked ad @Broken");
        }
    }

    private static boolean calledBy(final Class<? extends Runner> caller) {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        String callerName = caller.getName();
        for (int i = 0; i < stackTrace.length; i++) {
            if (callerName.equals(stackTrace[i].getClassName())) {
                return true;
            }
        }
        return false;
    }

}
