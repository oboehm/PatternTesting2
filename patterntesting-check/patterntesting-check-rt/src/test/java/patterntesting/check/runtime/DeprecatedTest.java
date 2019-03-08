/**
 * $Id: DeprecatedTest.java,v 1.8 2016/12/18 21:59:31 oboehm Exp $
 *
 * Copyright (c) 2008 by Oliver Boehm
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

import org.junit.Test;
import org.apache.logging.log4j.*;
import org.apache.logging.log4j.LogManager;

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
    @Test(expected = RuntimeException.class)
    public void testDeprecatedCall() {
        deprecatedMethod();
    }

    @Deprecated
    private static void deprecatedMethod() {
        log.info("I should be never called because I'm deprecated");
    }

    /**
     * Test get deprecated attribute.
     */
    @Test(expected = RuntimeException.class)
    public void testGetDeprecatedAttribute() {
        log.info("deprecatedAttribute = " + deprecatedAttribute);
    }

    /**
     * Test set deprecated attribute.
     */
    @Test(expected = RuntimeException.class)
    public void testSetDeprecatedAttribute() {
        deprecatedAttribute = 2;
    }

    /**
     * Test create deprecated class.
     */
    @Test(expected = RuntimeException.class)
    public void testCreateDeprecatedClass() {
        DeprecatedClass dc = new DeprecatedClass();
        log.info(dc + " created.");
    }

}
