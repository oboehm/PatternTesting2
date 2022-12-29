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

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import patterntesting.runtime.util.Assertions;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * The Class ContractTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.6 $
 * @since 30.01.2009
 */
public class ContractTest {

    private static Logger log = LoggerFactory.getLogger(ContractTest.class);
    private final ContractViolator rowdie = new ContractViolator();

    /**
     * Sets the up.
     */
    @BeforeEach
    public void setUp() {
        assumeTrue(Assertions.ENABLED, "assertions must be enabled ('java -ea ...')");
    }

    /**
     * Test method for {@link patterntesting.runtime.dbc.Contract#invariant()}.
     */
    @Test
    public final void testInvariant() {
        assertThrows(ContractViolation.class, () -> rowdie.violateInvariant());
    }

    /**
     * Test invariant ok.
     */
    @Test
    public final void testInvariantOk() {
        assertTrue(rowdie.isValid());
    	log.debug("invariant() is ok -> no ContractViolation");
    }

    
    
    static class ContractViolator implements Contract {

        private static Logger LOG = LoggerFactory.getLogger(ContractViolator.class);
        private boolean valid = true;

        public void violateInvariant() {
            this.valid = false;
            LOG.info("you'll get a ContractViolation soon (invariant violated)");
        }

        @Override
        public boolean invariant() {
            return isValid();
        }

        public boolean isValid() {
            return this.valid;
        }

    }
    
}
