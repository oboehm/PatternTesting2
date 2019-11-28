/*
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
 * (c)reated 05.02.2010 by oliver (ob@oasd.de)
 */

package patterntesting.concurrent.junit;

import junit.framework.TestCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

/**
 * Although it is no good idea to mix JUnit 3 classes with JUnit 4 annotations
 * it is not forbidden.
 *
 * @author oliver
 * @since 1.0 (05.02.2010)
 */
@RunWith(ParallelRunner.class)
public final class RunTestsParallelJUnitMixedTest extends TestCase {

    private static final Logger log = LogManager.getLogger(RunTestsParallelJUnitMixedTest.class);
    private final Date now = new Date();

    /**
     * Sets the up.
     *
     * @see TestCase#setUp()
     */
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() {
        setUpStarted();
    }

    /**
     * Only to print the date.
     */
    @Before
    public void setUpStarted() {
        logTime("setUpStarted() called");
    }

    /**
     * Simple test method which should return normally.
     */
    @Test
    public void testOk() {
        logTime("testOk() called");
    }

    /**
     * Simple test that expects to fail.
     */
    @Test
    public void testFail() {
        logTime("testFail() called");
        try {
            fail("ups");
        } catch (AssertionError expected) {
            log.info("expected: " + expected);
        }
    }

    /**
     * This test method is only called if this is a JUnit 4 test.
     */
    @Test(expected = AssertionError.class)
    public void notCalledWithJUnit3() {
        fail("I should be not called with JUnit 3!");
    }

    private void logTime(final String msg) {
        log.info(msg + " " + now);
    }

}

