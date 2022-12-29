/*
 * Copyright (c) 2012-2020 by Oliver Boehm
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

package patterntesting.concurrent.junit.internal;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import patterntesting.runtime.junit.internal.DescriptionUtils;

import static org.junit.Assert.assertEquals;

/**
 * JUnit test for {@link BackgroundRunner} class.
 * 
 * @author oliver
 * @since 1.2 (30.12.2011)
 */
@RunWith(BackgroundRunner.class)
public final class BackgroundRunnerTest {
    
    private static final Logger log = LoggerFactory.getLogger(BackgroundRunnerTest.class);
    private static BackgroundRunner bgRunner;
    
    /**
     * Sets the up BackgroundRunner for testing.
     *
     * @throws InitializationError the initialization error
     */
    @BeforeClass
    public static void setUpRunner() throws InitializationError {
        bgRunner = new BackgroundRunner(new BlockJUnit4ClassRunner(BackgroundRunnerTest.class));
    }
    
    /**
     * This is only a test dummy to have something for testing.
     */
    @Test
    public void testDummy() {
        log.debug("testDummy() called.");
    }

    /**
     * Test method for {@link BackgroundRunner#getDescription()}.
     */
    @Test
    public void testGetDescription() {
        Class<?> testClass = DescriptionUtils.getTestClassOf(bgRunner.getDescription());
        assertEquals(BackgroundRunnerTest.class, testClass);
    }

}
