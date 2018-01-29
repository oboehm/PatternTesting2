/*
 * $Id: BrokenFixtureTest.java,v 1.6 2016/12/18 20:19:41 oboehm Exp $
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
 * (c)reated 27.08.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.fit.fixture;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import patterntesting.runtime.annotation.IntegrationTest;
import patterntesting.runtime.junit.SmokeRunner;

/**
 * The Class BrokenFixtureTest.
 *
 * @author oliver
 * @since 1.0.2 (27.08.2010)
 */
@RunWith(SmokeRunner.class)
@IntegrationTest
public class BrokenFixtureTest {

    private final BrokenFixture fixture = new BrokenFixture();

    /**
     * Sets up the fixture.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        fixture.reset();
    }

    /**
     * Test method for {@link BrokenFixture#isExecuted()}
     * and Attribute = "till".
     */
    @Test
    public void testTill() {
        fixture.Attribute = "till";
        fixture.Value = "\"26-Aug-2010\"";
        checkTill("25.08.2010", "no");
        checkTill("26.08.2010", "yes");
        checkTill("27.08.2010", "yes");
    }

    private void checkTill(final String actual, final String expected) {
        fixture.Actual = actual;
        assertEquals(expected, fixture.isExecuted());
    }

    /**
     * Test method for {@link BrokenFixture#isExecuted()}
     * and Attribute = "osName".
     */
    @Test
    public void testOsName() {
        fixture.Attribute = "osName";
        fixture.Actual = "Linux";
        checkOsName("{\"Linux\", \"Mac OS X\"}", "no");
        checkOsName("\"Mac OS X\"", "yes");
    }

    private void checkOsName(final String names, final String expected) {
        fixture.Value = names;
        assertEquals(expected, fixture.isExecuted());

    }

}

