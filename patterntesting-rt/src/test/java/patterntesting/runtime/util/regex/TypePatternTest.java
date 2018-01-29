/*
 * $Id: TypePatternTest.java,v 1.2 2016/12/18 20:19:41 oboehm Exp $
 *
 * Copyright (c) 2014 by Oliver Boehm
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
 * (c)reated 12.01.2014 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.util.regex;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

/**
 * Junit tests for {@link TypePattern} class.
 *
 * @author oliver
 * @since 1.4.1 (12.01.2014)
 */
public class TypePatternTest {

    /**
     * Test method for {@link TypePattern#matches(Class)}.
     */
    @Test
    public void testMatchesObject() {
        TypePattern pattern = new TypePattern("java.util.Date");
        assertTrue("expected: " + pattern + " matches", pattern.matches(Date.class));
    }

}

