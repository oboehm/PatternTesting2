/*
 * $Id: BrokenTest.java,v 1.5 2016/12/18 20:19:38 oboehm Exp $
 *
 * Copyright (c) 2009 by Oliver Boehm
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

package patterntesting.runtime.junit.v3;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.runner.RunWith;

import junit.framework.TestCase;
import patterntesting.runtime.annotation.Broken;
import patterntesting.runtime.junit.SmokeRunner;

/**
 * Since 0.9.8 you can mark JUnit tests as broken. This is the JUnit-4 test
 * for testing this feature.
 *
 * @author oliver
 * @since 23.11.2009
 */
@RunWith(SmokeRunner.class)
public class BrokenTest extends TestCase {

    private static final Logger log = LogManager.getLogger(BrokenTest.class);

    /**
     * Dummy test to see if JUnit is working.
     */
    public void testDummy() {
        log.debug("testDummy() called");
    }

    /**
     * Dummy test which should be skipped (because it is marked as "@Broken").
     */
    @Broken(hide = true)
    public void testBroken() {
        fail("this JUnit test should not be called because it is marked as @Broken");
    }

}
