/*
 * $Id: NotNullTest.java,v 1.8 2016/12/18 21:59:31 oboehm Exp $
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
 * (c)reated 06.12.2008 by oliver (ob@oasd.de)
 */
package patterntesting.check.runtime;

import javax.validation.constraints.NotNull;

import org.apache.logging.log4j.*;

import org.junit.jupiter.api.Test;
import patterntesting.annotation.check.runtime.MayReturnNull;
import patterntesting.annotation.check.runtime.NullArgsAllowed;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * The Class NotNullTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.8 $
 * @since 06.12.2008
 */
@NullArgsAllowed
@MayReturnNull
public class NotNullTest {

    private static final Logger log = LogManager.getLogger(NotNullTest.class);
    @NotNull private static String s0 = "static-test";
    @NotNull private String s1 = "test";
    private String s2;
    private static final String nullString = null;
    private static final Long nullLong = null;

    /**
     * The Class NotNullConstructor.
     */
    @NullArgsAllowed
    class NotNullConstructor {

        /**
         * Instantiates a new NotNullConstructor.
         *
         * @param s the s
         */
        public NotNullConstructor(@NotNull final String s) {
            s2 = s;
        }

        /**
         * Instantiates a new NotNullConstructor.
         *
         * @param l the l
         */
        public NotNullConstructor(final Long l) {
            s2 = s1 + l;
        }

        /**
         * Instantiates a new NotNullConstructor.
         *
         * @param x the x
         * @param y the y
         */
        public NotNullConstructor(@NotNull final String x, final String y) {
          s2 = y;
          s1 = y + s2;
        }
    }

    /**
     * Test static null string.
     */
    @Test
    public final void testStaticNullString() {
        assertThrows(AssertionError.class, () -> {
            log.info("s0 = " + s0);
            s0 = null;
        });
    }

    /**
     * Test null string.
     */
    @Test
    public final void testNullString() {
        assertThrows(AssertionError.class, () -> {
            log.info("s1 = " + s1);
            s1 = null;
        });
    }

    /**
     * Test ctor not null arg.
     */
    @Test
    public final void testCtorNotNullArg() {
        assertThrows(AssertionError.class, () -> {
            new NotNullConstructor("something");
            new NotNullConstructor(nullString);
        });
    }

    /**
     * Test ctor null arg.
     */
    @Test
    public final void testCtorNullArg() {
        new NotNullConstructor(nullLong);
    }

    /**
     * Test ctor null args.
     */
    @Test
    public final void testCtorNullArgs() {
        assertThrows(AssertionError.class, () -> new NotNullConstructor(nullString, "test"));
    }

    /**
     * Test null arg.
     */
    @Test
    public final void testNullArg() {
        assertThrows(AssertionError.class, () -> method(nullString));
    }

    /**
     * Test null args.
     */
    @Test
    public final void testNullArgs() {
        assertThrows(AssertionError.class, () -> method("test", nullString));
    }

    /**
     * Test null return.
     */
    @Test
    public final void testNullReturn() {
        assertThrows(AssertionError.class, () -> {
            String s = returnNull();
            log.info("s = " + s);
        });
    }

    private void method(@NotNull final String s) {
        log.info("s = " + s);
    }

    private void method(final String one, @NotNull final String two) {
        log.info("one = " + one + ", two = " + two);
    }

    @NotNull private String returnNull() {
        return null;
    }

}
