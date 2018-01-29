/*
 * $Id: DbCTest.java,v 1.6 2016/12/18 20:19:41 oboehm Exp $
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
 */
package patterntesting.runtime.dbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static patterntesting.runtime.dbc.DbC.ensure;
import static patterntesting.runtime.dbc.DbC.require;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.Before;
import org.junit.Test;

import patterntesting.runtime.util.Assertions;

/**
 * The Class DbCTest.
 *
 * @author oliver
 */
public class DbCTest {

	private static final Logger log = LogManager.getLogger(DbCTest.class);

	/**
	 * Setup.
	 */
	@Before
	public void setUp() {
		assertTrue("assertion must be enabled ('java -ea ...')",
				Assertions.areEnabled());
	}

    /**
     * Test method for {@link DbC#require(boolean)}.
     */
    @Test
    public final void testRequireTrue() {
        require(true);
        log.info("you should see this message because precondition is true");
    }

    /**
     * Test require false.
     */
    @Test(expected = ContractViolation.class)
    public final void testRequireFalse() {
        require(false);
        log.warn("you should never see this message (precondition is false!)");
    }

    /**
     * Test method for {@link DbC#require(boolean, Object)}.
     */
    @Test
    public final void testRequireBooleanObject() {
        try {
            require(false, "test message");
        } catch(ContractViolation expected) {
            assertEquals("test message", expected.getMessage());
        }
    }

    /**
     * Test method for {@link DbC#ensure(boolean)}.
     */
    @Test
    public final void testEnsureTrue() {
        ensure(true);
    }

    /**
     * Test ensure false.
     */
    @Test(expected = ContractViolation.class)
    public final void testEnsureFalse() {
        log.info("you'll get a ContractViolation soon");
        ensure(false);
    }

    /**
     * Test method for {@link DbC#ensure(boolean, Object)}.
     */
    @Test
    public final void testEnsureBooleanObject() {
        try {
            ensure(false, "test message");
        } catch(ContractViolation expected) {
            assertEquals("test message", expected.getMessage());
        }
    }

}
