/*
 * Copyright (c) 2012-2019 by Oliver Boehm
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
 * (c)reated 11.08.2012 by oliver (ob@oasd.de)
 */

package patterntesting.sample;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import patterntesting.runtime.junit.ObjectTester;
import patterntesting.runtime.junit.SerializableTester;
import patterntesting.runtime.util.Assertions;

import java.io.NotSerializableException;
import java.net.URI;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JUnit test of {@link AntiPattern} class.
 *
 * @author oliver (ob@aosd.de)
 * @since 1.3 (11.08.2012)
 */
public class AntiPatternTest {

    private static final Logger log = LogManager.getLogger(AntiPatternTest.class);

    /**
     * For successful testing you must enable assertions.
     * If assertions are not enabled this test will fail.
     * <p>
     * You can enable assertions on the Sun-VM with the option "-ea"
     * (java -ea ...).
     * </p>
     */
    @Test
    public void testAssertionsEnabled() {
        assertTrue(Assertions.ENABLED, "assertion is disabled - enable it with 'java -ea ...'");
    }

    /**
     * Because we use null as argument for
     * {@link AntiPattern#nullFlag(Boolean)} we expect an
     * {@link AssertionError} here.
     */
    @Test
    public void testNullFlag() {
        assertThrows(AssertionError.class, () -> {
            AntiPattern.nullFlag(null);
        });
    }

    /**
     * We call {@link AntiPattern#logAndReturnNull(String)} with an invalid
     * URI which causes a 'null' as return value. Because this no good
     * programming style and a common anti pattern PatternTesting will report
     * an {@link AssertionError} here.
     */
    @Test
    public void testLogAndReturnNull() {
        assertThrows(AssertionError.class, () -> {
            URI uri = AntiPattern.logAndReturnNull("not a uri");
            log.info("uri = {}", uri);
        });
    }

    /**
     * Because the {@link AntiPattern} class overrides the {@link #equals(Object)}
     * method but not the {@link Object#hashCode()} we expect an error here.
     */
    @Test
    public void testEqualsObject() {
        assertThrows(AssertionError.class, () -> {
            ObjectTester.assertEquals(AntiPattern.class);
        });
    }

    /**
     * Althouth the {@link AntiPattern} class extends {@link Serializable} it
     * is not really Serializable. So we expect an error here.
     *
     * @throws NotSerializableException the not serializable exception
     */
    @Test
    public void untestedSerializable() throws NotSerializableException {
        assertThrows(NotSerializableException.class, () -> {
            AntiPattern ap = new AntiPattern();
            ap.setRuntime(Runtime.getRuntime());
            SerializableTester.assertSerialization(ap);
        });
    }

    /**
     * Test umlauts.
     */
    @Test
    public void testUmlauts() {
        AntiPattern.umlauts();
    }

    /**
     * Test floating point currency.
     */
    @Test
    public void testFloatingPointCurrency() {
        AntiPattern.floatingPointCurrency();
    }

}

