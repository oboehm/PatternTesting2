/*
 * $Id: IORuntimeExceptionTest.java,v 1.1 2016/12/30 20:52:26 oboehm Exp $
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
 * (c)reated 30.12.2016 by oboehm (ob@oasd.de)
 */

package patterntesting.runtime.io;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

/**
 * Unit tests for {@link IORuntimeException}.
 * 
 * @author oboehm
 * @version $Revision: 1.1 $
 * @since 1.7 (30.12.2016)
 */
public final class IORuntimeExceptionTest {
    
    /**
     * Test method for {@link IORuntimeException#IORuntimeException(String)}.
     */
    @Test
    public void testIORuntimeExceptionString() {
        IORuntimeException ex = new IORuntimeException("tape lost");
        assertEquals("tape lost", ex.getMessage());
    }

    /**
     * Test method for {@link IORuntimeException#IORuntimeException(Throwable)}.
     */
    @Test
    public void testIORuntimeExceptionThrowable() {
        Throwable cause = new IOException("disk error");
        IORuntimeException ex = new IORuntimeException(cause);
        assertEquals(cause, ex.getCause());
        assertThat(cause.getMessage(), not(isEmptyOrNullString()));
    }

}
