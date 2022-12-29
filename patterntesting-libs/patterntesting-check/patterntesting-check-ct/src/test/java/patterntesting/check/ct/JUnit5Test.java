/*
 * Copyright (c) 2019 by Oliver Boehm
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
 * (c)reated 08.07.2019 by oliver (ob@aosd.de)
 */
package patterntesting.check.ct;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.jupiter.api.Test;
import patterntesting.annotation.check.ct.SuppressJUnitWarning;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * This is the test class for the JUnitAspect.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 2.0
 */
public final class JUnit5Test {

    private static final Logger LOG = LoggerFactory.getLogger(JUnit5Test.class);

    /**
     * This test method should give a warning that exceptions should be handled
     * by using the @Test annotation,
     * e.g. "@Test(expected=RuntimeException.class)"
     */
    @SuppressJUnitWarning   // comment this and you'll see the warning
    @Test
    public void testDummy() {
        try {
            LOG.info("testDummy() does nothing");
        } catch (RuntimeException e) {
            LOG.error("test failed", e);
            fail("test failed");
        }
    }

}
