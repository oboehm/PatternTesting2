/*
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
 * (c)reated 18.12.2011 by oliver (ob@oasd.de)
 */

package patterntesting.concurrent.junit;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import patterntesting.concurrent.experimental.math.FibonacciTest;

import static org.junit.Assert.assertEquals;

/**
 * JUnit tests for {@link ParallelProxyRunner} class.
 * 
 * @author oliver
 * @since 1.2 (18.12.2011)
 */
public final class ParallelProxyRunnerTest {
    
    private static ParallelProxyRunner proxyRunner;

    /**
     * Sets the up a ProxyRunner.
     *
     * @throws InitializationError the initialization error
     */
    @BeforeClass
    public static void setUpProxyRunner() throws InitializationError {
        proxyRunner = new ParallelProxyRunner(FibonacciTest.class);
    }

    /**
     * Test method for {@link patterntesting.runtime.junit.ProxyRunner#getDelegateRunner()}.
     */
    @Test
    public void testGetDelegateRunner() {
        ParentRunner<FrameworkMethod> delegateRunner = proxyRunner.getDelegateRunner();
        assertEquals(SpringJUnit4ClassRunner.class, delegateRunner.getClass());
    }

}

