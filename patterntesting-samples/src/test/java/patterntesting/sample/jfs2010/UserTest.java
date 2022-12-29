/*
 * $Id: UserTest.java,v 1.8 2016/12/20 08:13:32 oboehm Exp $
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
 * (c)reated 12.05.2010 by oliver (ob@oasd.de)
 */

package patterntesting.sample.jfs2010;

import org.slf4j.*;
import org.slf4j.LoggerFactory;

import org.junit.jupiter.api.Test;
import patterntesting.runtime.util.Assertions;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A simple JUnit test for the {@link User} class.
 *
 * @author oliver
 * @since 1.0 (12.05.2010)
 */
class UserTest {

    private static final Logger log = LoggerFactory.getLogger(UserTest.class);

    /**
     * Test method for {@link patterntesting.sample.jfs2010.User#getName()}.
     * This is more or less a dummy test. But we need at least one test
     * which is not an integration test.
     */
    @Test
    public void testGetName() {
        User hugo = new User("Hugo");
        assertEquals("Hugo", hugo.getName());
    }

    /**
     * If you start the tests with option "-ea" enabled you should see an
     * execption because a deprecated operation setName(String) is called.
     *
     * @since 1.2.10-YEARS
     */
    @Test
    public void testSetName() {
        if (!Assertions.ENABLED) {
            log.info("enable assertions to see DeprecatedCodeException ('java -ea ...')");
        }
        User oli = new User("Oliver");
        // enable next line to see DeprecatedCodeException
        // oli.setName("Oliver");
        assertEquals("Oliver", oli.getName());
    }

    /**
     * Test equals.
     */
    @Test
    public void testEquals() {
        User oli = new User("Oliver");
        User oliver = new User(oli.getName());
        assertEquals(oli, oliver);
        assertEquals(oliver, oli);
    }

    /**
     * Test not equals.
     */
    @Test
    public void testNotEquals() {
        User anton = new User("Anton");
        User berta = new User("Berta");
        assertNotEquals(anton, berta);
    }

}
