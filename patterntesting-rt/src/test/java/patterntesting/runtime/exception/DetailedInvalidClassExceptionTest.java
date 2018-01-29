/*
 * $Id: DetailedInvalidClassExceptionTest.java,v 1.1 2016/03/13 21:44:26 oboehm Exp $
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
 * Unit tests for {@link DetailedInvalidClassException}.
 *
 * @author oliver
 * @since 1.6 (13.03.2016)
 */
public final class DetailedInvalidClassExceptionTest {

    /**
     * Test method for {@link java.lang.Throwable#getCause()}.
     */
    @Test
    public void testGetCause() {
        Throwable cause = new IllegalStateException("world");
        DetailedInvalidClassException ex = new DetailedInvalidClassException("hello", cause);
        assertEquals(cause, ex.getCause());
    }

}

