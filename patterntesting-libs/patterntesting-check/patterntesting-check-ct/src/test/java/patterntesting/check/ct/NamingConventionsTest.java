/*
 * $Id: NamingConventionsTest.java,v 1.4 2016/01/06 20:47:42 oboehm Exp $
 *
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
 * (c)reated 21.03.2009 by oliver (ob@aosd.de)
 */
package patterntesting.check.ct;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * This is the test class for NamingConventionsAspect.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.4 $
 * @since 21.03.2009
 */
public final class NamingConventionsTest {

    private static final char UNDER_SCORE = '_';
    // you'll see a warning if you uncomment the next line
    //private char under_score = UNDER_SCORE;
    private final char underscore = UNDER_SCORE;

    // you'll see a warning if you uncomment this method
//    @Test
//    public void test_dummy() {
//        assertEquals(UNDER_SCORE, underscore);
//    }

    /**
     * Test dummy.
     */
    @Test
    public void testDummy() {
        assertEquals(UNDER_SCORE, underscore);
    }

}
