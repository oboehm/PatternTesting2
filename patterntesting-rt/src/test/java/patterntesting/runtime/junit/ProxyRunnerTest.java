/*
 * $Id: ProxyRunnerTest.java,v 1.4 2016/12/18 20:19:38 oboehm Exp $
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
 * (c)reated 11.11.2011 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.logging.log4j.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import patterntesting.runtime.experimental.math.FibonacciTest;

/**
 * JUnit tests for {@link ProxyRunner}.
 *
 * @author oliver (ob@aosd.de)
 * @since 1.2 (11.11.2011)
 */
public final class ProxyRunnerTest {

    private static final Logger log = LogManager.getLogger(ProxyRunnerTest.class);
    private static ProxyRunner proxyRunner;

    /**
     * Sets the up a ProxyRunner.
     *
     * @throws InitializationError the initialization error
     */
    @BeforeClass
    public static void setUpProxyRunner() throws InitializationError {
        proxyRunner = new ProxyRunner(FibonacciTest.class);
    }

    /**
     * This is only a dummy to have something for testing.
     */
    @Test
    public void testDummy() {
        log.debug("Hi, I'm the testDummy method!");
    }

    /**
     * This is another dummy to have something for testing.
     */
    @Test
    public void testAssert() {
        String hello = "hello";
        assertEquals(hello, "hello");
    }

    /**
     * Test method for {@link ProxyRunner#getDescription()}. We compare the
     * number of calculated test descriptions with the result of the
     * {@link SpringJUnit4ClassRunner}. Because some anntoations in
     * {@link FibonacciTest} has the hide flag set the expected number
     * is smaller.
     * <p>
     * If this test fails look the "hide=true" in the annotated test methods
     * and compare it with the 'expectedTests' variable.
     * </p>
     *
     * @throws InitializationError the initialization error
     */
    @Test
    public void testGetDescription() throws InitializationError {
        Description description = proxyRunner.getDescription();
        assertNotNull(description);
        Runner springRunner = new SpringJUnit4ClassRunner(FibonacciTest.class);
        Description springDescription = springRunner.getDescription();
        int expectedTests = springDescription.getChildren().size();
        assertEquals("some annotations has hide flag set", expectedTests, description.getChildren().size());
    }

    /**
     * Test method for {@link ProxyRunner#getDelegateRunner()}.
     */
    @Test
    public void testGetDelegateRunner() {
        ParentRunner<FrameworkMethod> delegateRunner = proxyRunner.getDelegateRunner();
        assertEquals(SpringJUnit4ClassRunner.class, delegateRunner.getClass());
    }

}

