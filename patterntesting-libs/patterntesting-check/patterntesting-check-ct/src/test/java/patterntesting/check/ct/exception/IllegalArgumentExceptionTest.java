/*
 * $Id: IllegalArgumentExceptionTest.java,v 1.2 2016/01/06 20:08:44 oboehm Exp $
 *
 * Copyright (c) 2012 by Oliver Boehm
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
 * (c)reated 31.01.2012 by oliver (ob@oasd.de)
 */

package patterntesting.check.ct.exception;

import org.junit.jupiter.api.Test;

import patterntesting.annotation.check.ct.SuppressIllegalArgumentExceptionWarning;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * JUnit tests for the IllegalArgumentExceptionAspect.
 *
 * @author oliver (ob@aosd.de)
 * @since 1.2.10-YEARS (31.01.2012)
 */
public class IllegalArgumentExceptionTest {

    /**
     * Instantiates a new illegal argument exception test.
     * If you remove the {@link SuppressIllegalArgumentExceptionWarning} you
     * should see the warning at the throw statement.
     * <p>
     * Note: Don't set the "throwIllegalArgumentException" property. Otherwise
     * the whole JUnit test will fail because it needs the constructor here.
     * </p>
     */
    @SuppressIllegalArgumentExceptionWarning
    public IllegalArgumentExceptionTest() {
        if (System.getProperty("throwIllegalArgumentException") != null) {
            throw new IllegalArgumentException("krawumm");
        }
    }

    /**
     * Test throwing allowed.
     */
    @Test
    public void testThrowingAllowed() {
        assertThrows(IllegalArgumentException.class, () -> throwIllegalArgumentException("bumm"));
    }

    private static void throwIllegalArgumentException(final String msg) {
        throw new IllegalArgumentException(msg);
    }

    /**
     * Test the warning about abnormal use of an {@link IllegalArgumentException}.
     * If you remove the {@link SuppressIllegalArgumentExceptionWarning} you
     * should see the warning at the throw statement.
     */
    @SuppressIllegalArgumentExceptionWarning
    @Test
    public void testWarning() {
        assertThrows(IllegalArgumentException.class, () -> {throw new IllegalArgumentException();});
    }

}

