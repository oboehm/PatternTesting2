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
 * (c)reated 30.12.2011 by oliver (ob@oasd.de)
 */

package patterntesting.concurrent.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import patterntesting.concurrent.junit.internal.InternalTests;

/**
 * JUnit suite for junit package.
 * 
 * @author oliver
 * @since 1.2 (30.12.2011)
 */
@RunWith(ParallelSuite.class)
@SuiteClasses({ InternalTests.class,
        JUnit4ExecutorTest.class,
        RunTestsParallelJUnit4Test.class,
        RunTestsParallelJUnitMixedTest.class, SlowJUnit4Test.class })
public class JunitTests {

}
