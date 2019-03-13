/*
 * $Id: AbstractExceptionTest.java,v 1.3 2016/08/13 19:09:58 oboehm Exp $
 *
 * Copyright (c) 2010 by Oliver Boehm
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
 * (c)reated 08.07.2010 by oliver (ob@oasd.de)
 */

package patterntesting.exception;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import patterntesting.runtime.util.Assertions;

/**
 * This is the abstract super class needed for TestExceptionClass but also
 * for other test classed.
 *
 * @author oliver
 * @since 1.1 (08.07.2010)
 */
public abstract class AbstractExceptionTest {

    /**
     * For this JUnit tests assertions must be enabled. If this is not the
     * case this test method will fail.
     * <p>
     * You can enable assertions on the Sun-VM with the option "-ea"
     * (java -ea ...).
     * </p>
     */
    @Test
    public final void testAssertionsEnabled() {
        assertTrue("assertion is disabled - enable it with 'java -ea ...'",
                Assertions.ENABLED);
    }

}
