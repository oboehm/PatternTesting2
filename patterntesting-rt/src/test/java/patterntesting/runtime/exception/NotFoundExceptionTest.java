/*
 * $Id: NotFoundExceptionTest.java,v 1.3 2016/12/18 20:19:38 oboehm Exp $
 *
 * Copyright (c) 2014 by Oliver Boehm
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
 * (c)reated 18.09.2014 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.Test;

/**
 * Unit tests for {@link NotFoundException}.
 *
 * @author oliver
 * @version $Revision: 1.3 $
 */
public class NotFoundExceptionTest {

    private static final Logger LOG = LogManager.getLogger(NotFoundException.class);

    /**
     * Test method for {@link NotFoundException#NotFoundException(String, Throwable)}.
     */
    @Test
    public void testNotFoundExceptionStringThrowable() {
        Throwable cause = new IllegalArgumentException("huhu");
        try {
            throw new NotFoundException("searching...", cause);
        } catch (NotFoundException expected) {
            LOG.debug("Excpected exception happenend:", expected);
            assertEquals(cause, expected.getCause());
        }
    }

    /**
     * Test method for {@link NotFoundException#NotFoundException(String)}.
     */
    @Test
    public void testNotFoundExceptionString() {
        NotFoundException ex = new NotFoundException("test");
        assertEquals("test", ex.getMessage());
    }

    /**
     * Test method for {@link NotFoundException#NotFoundException(Object)}.
     */
    @Test
    public void testValueNotFoundExceptionObject() {
        Pattern pattern = Pattern.compile("world");
        NotFoundException ex = new NotFoundException(pattern);
        String msg = ex.getMessage();
        assertTrue(msg, msg.contains("world"));
    }

    /**
     * Test method for {@link NotFoundException#NotFoundException(Object)}.
     */
    @Test
    public void testValueNotFoundExceptionDate() {
        Date now = new Date();
        NotFoundException ex = new NotFoundException(now);
        String msg = ex.getMessage();
        assertTrue(msg, msg.contains("value"));
    }

}

