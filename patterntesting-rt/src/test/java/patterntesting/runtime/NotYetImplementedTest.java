/*
 * $Id: NotYetImplementedTest.java,v 1.7 2016/12/18 20:19:39 oboehm Exp $
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

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.jupiter.api.Test;
import patterntesting.runtime.annotation.NotYetImplemented;

import java.util.AbstractCollection;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The Class NotYetImplementedTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.7 $
 * @since 30-Apr-2009
 */
public class NotYetImplementedTest extends AbstractCollection<String> {

	private static final Logger log = LoggerFactory
			.getLogger(NotYetImplementedTest.class);

	/**
	 * Iterator.
	 *
	 * @return the iterator
	 * @see java.util.AbstractCollection#iterator()
	 */
    @Override
    @NotYetImplemented
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
    @NotYetImplemented("under construction")
    public int size() {
        // Auto-generated method stub
        return 0;
    }

    @NotYetImplemented("don''t call {0} today")
    private int notImplemented() {
        return -1;
    }

	/**
	 * Test implementation.
	 */
    @Test
	public final void testImplementation() {
        assertThrows(RuntimeException.class, () -> {
            iterator();
            size();
        });
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
			assertEquals("iterator() is not yet implemented", expected
					.getMessage());
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
            notImplemented();
            fail("RuntimeException expected");
        } catch (RuntimeException expected) {
            log.info(expected.toString());
            assertEquals("don't call notImplemented() today", expected
                    .getMessage());
        }
    }

}
