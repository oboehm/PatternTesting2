/*
 * $Id: SkipTestOnTest.java,v 1.6 2016/12/18 20:19:38 oboehm Exp $
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

package patterntesting.runtime.junit.v3;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.runner.RunWith;

import junit.framework.TestCase;
import patterntesting.runtime.annotation.SkipTestOn;
import patterntesting.runtime.junit.SmokeRunner;


/**
 * This is the JUnit3 test for the SkipTestOnAspect.
 *
 * @author oliver
 * @since 1.0 (25.01.2010)
 */
@RunWith(SmokeRunner.class)
public final class SkipTestOnTest extends TestCase {

    private static final Logger log = LogManager.getLogger(SkipTestOnTest.class);

    /**
     * If you have a Mac this test should be skipped. If you don't have one
     * the test will be executed but should not fail.
     */
    @SkipTestOn("Mac OS X")
    public void testSkipOnMac() {
        String os = System.getProperty("os.name");
        log.info("os.name=" + os);
        if (os.equalsIgnoreCase("Mac OS X")) {
            fail("this test should be skipped for MacOS");
        }
    }

}

