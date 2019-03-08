/*
 * $Id: AbstractRuntimeTest.java,v 1.3 2016/08/13 19:10:09 oboehm Exp $
 *
 * Copyright (c) 2015 by Oliver Boehm
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
 * (c)reated 11.01.2015 by oliver (ob@oasd.de)
 */

package patterntesting.check.runtime;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;

import patterntesting.runtime.util.Assertions;

/**
 * The Class AbstractRuntimeTest.
 *
 * @author oliver
 * @version $Revision: 1.3 $
 * @since 1.6 (11.01.2015)
 */
public class AbstractRuntimeTest {

    /**
     * For successful testing you must enable assertions otherwise some tests
     * can fail.
     * <p>
     * You can enable assertions on the Sun-VM with the option "-ea"
     * (java -ea ...).
     * </p>
     */
    @BeforeClass
    public static void testAssertionsEnabled() {
        assertTrue("assertion is disabled - enable it with 'java -ea ...'",
                Assertions.ENABLED);
    }

}
