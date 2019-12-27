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
 * (c)reated 02.01.2012 by oliver (ob@oasd.de)
 */

package patterntesting.concurrent.junit.internal;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import patterntesting.concurrent.junit.ParallelSuite;

/**
 * JUnit suite for internal package.
 * 
 * @author oliver
 * @since 1.2 (02.01.2012)
 */
@RunWith(ParallelSuite.class)
@SuiteClasses({ BackgroundRunnerTest.class, RecordingRunNotifierTest.class })
public class InternalTests {

}

