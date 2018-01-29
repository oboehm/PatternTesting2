/*
 * $Id: DetailedAssertionErrorTest.java,v 1.1 2016/03/13 17:28:53 oboehm Exp $
 *
 * Copyright (c) 2016 by Oliver Boehm
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
 * (c)reated 13.03.2016 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for {@link DetailedAssertionError} class.
 *
 * @author oliver
 */
public final class DetailedAssertionErrorTest {

    /**
     * Test method for {@link DetailedAssertionError#getCause()}.
     */
    @Test
    public void testGetCause() {
        Throwable cause = new IllegalStateException("bumm");
        DetailedAssertionError error = new DetailedAssertionError("test", cause);
        assertEquals(cause, error.getCause());
    }

}

