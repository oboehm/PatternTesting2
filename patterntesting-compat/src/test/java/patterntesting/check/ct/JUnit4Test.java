/*
 * $Id: JUnit4Test.java,v 1.6 2016/12/18 21:58:55 oboehm Exp $
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
 * (c)reated 15.03.2009 by oliver (ob@aosd.de)
 */
package patterntesting.check.ct;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import patterntesting.annotation.check.ct.SuppressJUnitWarning;

import static org.junit.Assert.*;

/**
 * This is the test class for the JUnitAspect.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.6 $
 * @since 15.03.2009
 */
public final class JUnit4Test {

    private static final Logger LOG = LoggerFactory.getLogger(JUnit4Test.class);

    /**
     * This test method should give a warning that exceptions should be handled
     * by using the @Test annotation,
     * e.g. "@Test(expected=RuntimeException.class)"
     */
    @SuppressJUnitWarning   // uncomment this and you'll see the warning
    @Test
    public void testDummy() {
        try {
            LOG.info("testDummy() does nothing");
        } catch (RuntimeException e) {
            LOG.error("test failed", e);
            fail("test failed");
        }
    }

    /**
     * Test assert true.
     */
    @Test
    public void testAssertTrue() {
        boolean b = true;
        //assertTrue(b);    // uncomment it to see the warning
        assertTrue("only a test", b);
    }

    /**
     * Test equals.
     */
    @SuppressJUnitWarning   // not really needed because test is ok
    public void testEquals() {
        int i = 42;
        assertEquals(42, i);
    }

}
