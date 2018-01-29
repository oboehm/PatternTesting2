/*
 * $Id: ContractTest.java,v 1.6 2016/12/18 20:19:41 oboehm Exp $
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
 * (c)reated 30.01.2009 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.dbc;

import static org.junit.Assert.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.Before;
import org.junit.Test;

import patterntesting.runtime.util.Assertions;

/**
 * The Class ContractTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.6 $
 * @since 30.01.2009
 */
public class ContractTest implements Contract {

    private static Logger log = LogManager.getLogger(ContractTest.class);
    private boolean valid = true;

    /**
     * Sets the up.
     */
    @Before
    public void setUp() {
        this.valid = true;
		assertTrue("assertion must be enabled ('java -ea ...')",
				Assertions.areEnabled());
    }

    /**
     * Test method for {@link patterntesting.runtime.dbc.Contract#invariant()}.
     */
    @Test(expected = ContractViolation.class)
    public final void testInvariant() {
        this.valid = false;
        log.info("you'll get a ContractViolation soon (invariant violated)");
    }

    /**
     * Test invariant ok.
     */
    @Test
    public final void testInvariantOk() {
    	log.debug("invariant() is ok -> no ContractViolation");
    }

    /**
     * The invariant method should be always return true.
     *
     * @return true, if invariant
     */
    @Override
	public boolean invariant() {
        return isValid();
    }

    /**
     * Checks if is valid.
     *
     * @return true, if is valid
     */
    public boolean isValid() {
        return this.valid;
    }

}

/**
 * $Log: ContractTest.java,v $
 * Revision 1.6  2016/12/18 20:19:41  oboehm
 * dependency to SLF4J removed
 *
 * Revision 1.5  2016/12/10 20:55:22  oboehm
 * code reformatted and cleaned up
 *
 * Revision 1.4  2016/01/06 20:46:32  oboehm
 * javadoc tags corrected
 *
 * Revision 1.3  2011/07/09 21:43:22  oboehm
 * switched from commons-logging to SLF4J
 *
 * Revision 1.2  2010/12/31 15:31:52  oboehm
 * checkstyle warnings reduced (bug 2859499)
 *
 * Revision 1.1  2010/01/05 13:26:18  oboehm
 * begin with 1.0
 *
 * Revision 1.3  2009/12/19 22:34:09  oboehm
 * trailing spaces removed
 *
 * Revision 1.2  2009/09/25 14:49:43  oboehm
 * javadocs completed with the help of JAutodoc
 *
 * Revision 1.1  2009/02/03 19:46:54  oboehm
 * DbC support moved from patterntesting-check to here
 *
 * Revision 1.4  2009/02/01 21:14:56  oboehm
 * require() and insure() moved to DbC
 * (to be imported static)
 *
 * Revision 1.3  2009/01/31 18:33:31  oboehm
 * DbC can now be activated via 'java -ea ...'
 *
 * $Source: /cvsroot/patterntesting/PatternTesting10/patterntesting-rt/src/test/java/patterntesting/runtime/dbc/ContractTest.java,v $
 */
