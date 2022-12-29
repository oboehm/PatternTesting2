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

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import patterntesting.runtime.util.Assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static patterntesting.runtime.dbc.DbC.ensure;
import static patterntesting.runtime.dbc.DbC.require;

/**
 * The Class DbCTest.
 *
 * @author oliver
 */
public class DbCTest {

	private static final Logger log = LoggerFactory.getLogger(DbCTest.class);

	/**
	 * Setup.
	 */
	@BeforeEach
	public void setUp() {
        assumeTrue(Assertions.areEnabled(), "assertions must be enabled ('java -ea ...')");
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
    @Test
    public final void testRequireFalse() {
        assertThrows(ContractViolation.class, () -> {
            require(false);
            log.warn("you should never see this message (precondition is false!)");
        });
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
     * Test method for {@link DbC#ensure(boolean)}.
     */
    @Test
    public final void testEnsureFalse() {
        assertThrows(ContractViolation.class, () -> {
            log.info("you'll get a ContractViolation soon");
            ensure(false);
        });
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
