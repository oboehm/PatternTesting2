/*
 * Copyright (c) 2008-2019 by Oliver Boehm
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
 * (c)reated 17.02.2009 by oliver (ob@oasd.de)
 */
package patterntesting.check.runtime;

import org.apache.logging.log4j.*;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * The Class DeprecatedTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.8 $
 * @since 17.02.2009
 */
public final class DeprecatedTest extends AbstractRuntimeTest {

    private static final Logger log = LogManager.getLogger(DeprecatedTest.class);
    @Deprecated
    private int deprecatedAttribute = 1;

    /**
     * Test deprecated call.
     */
    @Test//(expected = RuntimeException.class)
    public void testDeprecatedCall() {
        assertThrows(RuntimeException.class, DeprecatedTest::deprecatedMethod);
    }

    @Deprecated
    private static void deprecatedMethod() {
        log.info("I should be never called because I'm deprecated");
    }

    /**
     * Test get deprecated attribute.
     */
    @Test
    public void testGetDeprecatedAttribute() {
        assertThrows(RuntimeException.class, () -> log.info("deprecatedAttribute = " + deprecatedAttribute));
    }

    /**
     * Test set deprecated attribute.
     */
    @Test
    public void testSetDeprecatedAttribute() {
        assertThrows(RuntimeException.class, () -> deprecatedAttribute = 2);
    }

    /**
     * Test create deprecated class.
     */
    @Test
    public void testCreateDeprecatedClass() {
        assertThrows(RuntimeException.class, () -> {
            DeprecatedClass dc = new DeprecatedClass();
            log.info(dc + " created.");
        });
    }

}
