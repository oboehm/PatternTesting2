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
 * (c)reated 05.12.2009 by oliver (ob@oasd.de)
 */

package patterntesting.concurrent.junit;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.net.MalformedURLException;

import static org.junit.Assert.assertEquals;


/**
 * The Class JUnit4ExecutorTest.
 *
 * @author oliver
 * @since 05.12.2009
 */
@Ignore("@RunTestsParallel is no longer supported")
public final class JUnit4ExecutorTest extends AbstractJUnitExecutorTest {

    private static final Logger log = LoggerFactory.getLogger(JUnit4ExecutorTest.class);
    private RunTestsParallelJUnit4Test junit4Test = new RunTestsParallelJUnit4Test();

    /**
     * The default constructor.
     */
    public JUnit4ExecutorTest() {
        this.junitTest = junit4Test;
    }

    /**
     * We initialize here the JUnitExecutor.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        executor = new JUnit4Executor(RunTestsParallelJUnit4Test.class);
    }

    /**
     * Only to see of JUnit4Executor can be instantiated a 2nd time.
     */
    @Test
    public void testPlayTestAgain() {
        if (executor.isRunParallelEnabled()) {
            executor.playTest("testDummy", junit4Test);
            assertEquals(1, RunTestsParallelJUnit4Test.setUpBeforeClassCalled);
        } else {
            log.info("this test is only relevant for parallel runs");
        }
    }
    
    /**
     * RunTestsParallelJUnitTest.testExpectedException() throws a
     * MalformedURLException we want to see here.
     * @since 1.0
     */
    @Test(expected = MalformedURLException.class)
    public void testPlayTestWithException() {
        executor.playTest("testExpectedException", junit4Test);
    }

}
