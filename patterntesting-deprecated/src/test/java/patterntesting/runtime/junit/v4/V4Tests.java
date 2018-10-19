/*
 * $Id: V4Tests.java,v 1.3 2016/03/30 21:02:05 oboehm Exp $
 *
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
 * (c)reated 11.01.2012 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit.v4;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * The Class V4Tests.
 *
 * @author oliver
 * @since 1.2.10-YEARS (11.01.2012)
 */
@RunWith(Suite.class)
@SuiteClasses({ BrokenTest.class, IntegrationTestClassTest.class, IntegrationTestTest.class,
        RunTestOnTest.class, SkipTestOnFakeTest.class,
        SkipTestOnTest.class, SmokeTestTest.class })
public class V4Tests {

}
