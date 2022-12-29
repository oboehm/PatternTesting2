/*
 * $Id: StackTraceScannerTest.java,v 1.6 2016/12/30 20:52:26 oboehm Exp $
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
 * (c)reated 24.01.2014 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.util;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * JUnit tests for {@link StackTraceScanner}.
 *
 * @author oliver
 * @since 1.4.1 (24.01.2014)
 */
public class StackTraceScannerTest {

    private static final Logger LOG = LoggerFactory.getLogger(StackTraceScannerTest.class);

    /**
     * Default constructor.
     */
    public StackTraceScannerTest() {}

    /**
     * Test method for {@link StackTraceScanner#find(String, String)}.
     */
    @Test
    public void testFind() {
        StackTraceElement element = StackTraceScanner.find(StackTraceScannerTest.class, "testFind");
        assertNotNull(element);
        LOG.info("found: {}", element);
    }

    /**
     * Test method for {@link StackTraceScanner#findConstructor(Class)}.
     */
    @SuppressWarnings("unused")
    @Test
    public void testFindConstructor() {
        new FindCallerOfCtorTest("testFindConstructor");
    }

    /**
     * Test method for {@link StackTraceScanner#getCallerOf(Class, String)}.
     */
    @Test
    public void testGetCallerOf() {
        checkGetCallerOf("checkGetCallerOf", "testGetCallerOf");
    }

    private static void checkGetCallerOf(final String callee, final String caller) {
        StackTraceElement element = StackTraceScanner.getCallerOf(StackTraceScannerTest.class, callee);
        assertEquals(caller, element.getMethodName());
    }

    /**
     * Test method for {@link StackTraceScanner#getCallerOfConstructor(Class)}.
     */
    @SuppressWarnings("unused")
    @Test
    public void testGetCallerOfConstructor() {
        new FindCallerOfCtorTest("testGetCallerOfConstructor");
    }

    /**
     * Test method for {@link StackTraceScanner#getCallerClass()}.
     */
    @Test
    public void testGetCallerClass() {
        checkGetCallerClass(this.getClass());
    }

    private static void checkGetCallerClass(final Class<?> expected) {
        assertEquals(expected, StackTraceScanner.getCallerClass());
    }

    /**
     * Test method for
     * {@link StackTraceScanner#getCallerClass(Pattern[], Class...)}.
     */
    @Test
    public void testGetCallerClassExcluded() {
        Class<?> callerClass = StackTraceScanner.getCallerClass();
        Class<?> callerCallerClass = StackTraceScanner.getCallerClass(new Pattern[0], callerClass);
        assertThat(callerClass, not(equalTo(callerCallerClass)));
        LOG.info("Caller of {} is {}.", callerClass, callerCallerClass);
    }

    /**
     * Test method for {@link StackTraceScanner#getCallerStackTrace()}. As
     * result we expect the stacktrace of this method here.
     */
    @Test
    public void testGetCallerStacktrace() {
        StackTraceElement[] stacktrace = StackTraceScanner.getCallerStackTrace();
        Class<?> callerClass = StackTraceScanner.getCallerClass();
        assertEquals(callerClass.getName(), stacktrace[0].getClassName());
    }
    
    
    static class FindCallerOfCtorTest {
        public FindCallerOfCtorTest(final String caller) {
            StackTraceScannerTest test = new StackTraceScannerTest();
            StackTraceElement ctorElement = StackTraceScanner.findConstructor(FindCallerOfCtorTest.class);
            StackTraceElement callerElement = StackTraceScanner.getCallerOfConstructor(FindCallerOfCtorTest.class);
            assertEquals(FindCallerOfCtorTest.class.getName(), ctorElement.getClassName());
            assertEquals(caller, callerElement.getMethodName());
        }
    }

}
