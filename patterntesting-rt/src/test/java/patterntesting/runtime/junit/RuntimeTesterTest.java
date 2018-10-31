/*
 * $Id: RuntimeTesterTest.java,v 1.3 2016/10/24 20:22:07 oboehm Exp $
 *
 * Copyright (c) 2011 by Oliver Boehm
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
 * (c)reated 10.05.2011 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * JUnit test for RuntimeTester.
 *
 * @author oliver
 * @since 1.1 (10.05.2011)
 */
public final class RuntimeTesterTest {

    private static final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 0x100000);

    /**
     * Test method for {@link RuntimeTester#assertMaxMemory(int)}.
     * Nothing should happen here because the condition with the required
     * memory should be fulfilled.
     */
    @Test
    public void testAssertMaxMemory() {
        RuntimeTester.assertMaxMemory(maxMemory / 2);
    }

    /**
     * Test method for {@link RuntimeTester#assertMaxMemory(int)}.
     * Here we require more max memory as the VM could give us. So we
     * exepect an AssertionError.
     */
    @Test
    public void testAssertMaxMemoryShouldFail() {
        assertThrows(AssertionError.class, () -> RuntimeTester.assertMaxMemory(maxMemory * 2));
    }

    /**
     * Test method for {@link RuntimeTester#assertFreeMemory(int)}.
     * Nothing should happen here because the required memory of 1 MB
     * should be always fullfilled. If not start the VM with more memory.
     */
    @Test
    public void testAssertFreeMemory() {
        RuntimeTester.assertFreeMemory(1);
    }

    /**
     * Test method for {@link RuntimeTester#assertFreeMemory(int)}.
     * Here we require max memory and all of it should not be available
     * as free memory.
     */
    @Test
    public void testAssertFreeMemoryShouldFail() {
        assertThrows(AssertionError.class, () -> RuntimeTester.assertFreeMemory(maxMemory));
    }

}

