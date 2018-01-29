/*
 * $Id: UnsupportedOperationTest.java,v 1.7 2016/12/18 20:19:39 oboehm Exp $
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
 * (c)reated 19.10.2008 by oliver (ob@oasd.de)
 */
package patterntesting.runtime;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.AbstractCollection;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.Test;

import patterntesting.runtime.annotation.UnsupportedOperation;

/**
 * The Class UnsupportedOperationTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.7 $
 * @since 19.10.2008
 */
public class UnsupportedOperationTest extends AbstractCollection<String> {

	private static final Logger log = LogManager
			.getLogger(UnsupportedOperationTest.class);

	/**
	 * Iterator.
	 *
	 * @return the iterator
	 * @see java.util.AbstractCollection#iterator()
	 */
    @Override
    @UnsupportedOperation
    public Iterator<String> iterator() {
        // Auto-generated method stub
        return null;
    }

    /**
     * Size.
     *
     * @return the int
     * @see java.util.AbstractCollection#size()
     */
    @Override
    @UnsupportedOperation("under construction")
    public int size() {
        // Auto-generated method stub
        return 0;
    }

    @UnsupportedOperation("don''t call {0}")
    private int unsupported() {
        return -1;
    }

	/**
	 * Test implementation.
	 */
    @Test(expected = RuntimeException.class)
	public final void testImplementation() {
		iterator();
		size();
	}

	/**
	 * Test runtime exception.
	 */
	@Test
	public final void testRuntimeException() {
		try {
			iterator();
			fail("RuntimeException expected");
		} catch (RuntimeException expected) {
			log.info(expected.toString());
			assertEquals("iterator() is not supported", expected.getMessage());
		}
	}

    /**
     * Test exception message.
     */
    @Test
    public final void testExceptionMessage() {
        try {
            size();
            fail("RuntimeException expected");
        } catch (RuntimeException expected) {
            log.info(expected.toString());
            assertEquals("under construction", expected.getMessage());
        }
    }


    /**
     * Test exception message with method.
     */
    @Test
    public final void testExceptionMessageWithMethod() {
        try {
            unsupported();
            fail("RuntimeException expected");
        } catch (RuntimeException expected) {
            log.info(expected.toString());
            assertEquals("don't call unsupported()", expected.getMessage());
        }
    }

}
