/*
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
 * (c)reated 19.12.2009 by oliver (ob@oasd.de)
 */

package patterntesting.concurrent.junit;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;


/**
 * The Class AbstractJUnitExecutorTest.
 *
 * @author oliver
 * @since 19.12.2009
 */
public abstract class AbstractJUnitExecutorTest {

    /** The executor. */
    protected static JUnitExecutor executor;
    
    /** The junit test. */
    protected Object junitTest;

    /**
     * Test method for {@link JUnitExecutor#playTest(String, Object)}.
     */
    @Test
    public final void testPlayTest() {
        executor.playTest("testDummy", junitTest);
    }

    /**
     * The setup method should be set.
     */
    @Test
    public final void testSetupMethod() {
        assertNotNull(executor.setupMethod);
    }

    /**
     * The teardown method should be set.
     */
    @Test
    public final void testTeardownMethod() {
        assertNotNull(executor.teardownMethod);
    }
    
    /**
     * A reset should clear the test result. And it should record the test
     * again.
     * 
     * @since 1.0
     */
    @Test
    public final void testReset() {
        executor.reset();
        assertNotNull(executor.setupMethod);
        assertNotNull(executor.teardownMethod);
        executor.playTest("testDummy", junitTest);
    }

}
