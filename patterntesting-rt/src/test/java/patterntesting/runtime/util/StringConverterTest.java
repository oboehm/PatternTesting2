/*
 * $Id: StringConverterTest.java,v 1.5 2016/10/24 20:22:07 oboehm Exp $
 *
 * Copyright (c) 2015 by Oliver Boehm
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
 * (c)reated 05.08.2015 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for {@link StringConverter} class.
 *
 * @author oliver (ob@aosd.de)
 * @since 1.6 (05.08.2015)
 */
public final class StringConverterTest {

    /**
     * Test method for {@link StringConverter#ECHO}.
     */
    @Test
    public void testEcho() {
        assertEquals("hello echo", StringConverter.ECHO.convert("hello echo"));
    }

    /**
     * Test method for {@link StringConverter#LOWER_CASE}.
     */
    @Test
    public void testLowerCase() {
        assertEquals("hello world", StringConverter.LOWER_CASE.convert("Hello WORLD"));
    }

    /**
     * Test method for {@link StringConverter#UPPER_CASE}.
     */
    @Test
    public void testUpperCase() {
        assertEquals("THINK DIFFERENT", StringConverter.UPPER_CASE.convert("Think Different"));
    }

    /**
     * Test method for {@link StringConverter#IGNORE_WHITESPACES}.
     */
    @Test
    public void testIgnoreWhitespaces() {
        assertEquals("123", StringConverter.IGNORE_WHITESPACES.convert("1 2\t3"));
    }

    /**
     * Test method for {@link StringConverter#IGNORE_WHITESPACES}.
     */
    @Test
    public void testIgnoreWhitespacesNull() {
        assertEquals("", StringConverter.IGNORE_WHITESPACES.convert(null));
    }

    /**
     * Test method for {@link StringConverter#and(StringConverter...)}.
     */
    @Test
    public void testAnd() {
		assertEquals("lowercase",
				StringConverter.LOWER_CASE.and(StringConverter.IGNORE_WHITESPACES).convert("low er CASE"));
    }

    /**
     * Test method for {@link StringConverter#and(StringConverter...)}. Here we
     * test if the converters are started in the right range.
     */
    @Test
    public void testAndRange() {
		assertEquals("what goes up",
				StringConverter.UPPER_CASE.and(StringConverter.LOWER_CASE).convert("What goes UP"));
    }

}

