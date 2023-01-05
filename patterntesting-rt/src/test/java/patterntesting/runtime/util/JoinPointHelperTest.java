/*
 * Copyright (c) 2008-2023 by Oliver Boehm
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
 * (c)reated 23.10.2008 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.util;

import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.Test;
import patterntesting.runtime.annotation.DontLogMe;
import patterntesting.runtime.annotation.ProfileMe;
import patterntesting.runtime.mock.JoinPointMock;

import javax.annotation.concurrent.Immutable;
import java.lang.annotation.Annotation;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The Class JoinPointHelperTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.17 $
 * @since 23.10.2008
 */
public final class JoinPointHelperTest {

    /**
     * Test method for {@link JoinPointHelper#getArgsAsString(Object[])} .
     */
    @Test
    public void testGetArgsAsString() {
        Object[] args = {1, "2" };
        assertEquals("(1, \"2\")", JoinPointHelper.getArgsAsString(args));
    }

    /**
     * Test method for {@link JoinPointHelper#getArgsAsShortString(Object[])} .
     */
    @Test
    public void testGetArgsAsShortString() {
        Object[] args = {1, "a very long string which should be abbreviated", 2.718281828459045235, true };
        assertEquals("(1, \"a very long string wh...\", 2.71828, ..)",
                JoinPointHelper.getArgsAsShortString(args));
    }
    
    /**
     * Test method for {@link JoinPointHelper#getArgsAsShortString(Object[])} .
     */
    @Test
    public void testGetArgsAsShortStringNull() {
        Object[] nullArgs = null;
        assertEquals("()", JoinPointHelper.getArgsAsShortString(nullArgs));
    }

    /**
     * Test method for {@link JoinPointHelper#getArgsAsShortString(Object[])} .
     */
    @Test
    public void testGetClassArgsAsShortString() {
        Object[] args = { String.class, Date.class };
        assertEquals("(String, Date)", JoinPointHelper.getArgsAsShortString(args));
    }

    /**
     * That's not really a test method here. Set the trace level for
     * patterntesting.runtime.monitor.AbstractProfileAspect to TRACE and watch
     * the log, if you see the password "top secret" (you shouldn't see it!).
     *
     * @since 08-May-2009
     */
    @Test
    public void testDontLogMeArgAsString() {
        dontLogMeArgMethod("James", "top secret");
    }

	@SuppressWarnings("unused")
    @ProfileMe
    private static void dontLogMeArgMethod(String user, @DontLogMe String password) {
        ThreadUtil.sleep();
    }

	/**
	 * Test method for {@link JoinPointHelper#getCallerOf(JoinPoint)}.
	 */
	@Test
	public void testGetCallerOf() {
	    StackTraceElement caller = getCaller();
	    assertEquals(this.getClass().getName(), caller.getClassName());
	    assertEquals("testGetCallerOf", caller.getMethodName());
	}

	private StackTraceElement getCaller() {
        JoinPointMock jp = new JoinPointMock(this);
        jp.setSignature("getCaller");
        return JoinPointHelper.getCallerOf(jp);
	}

	/**
	 * Test method for {@link JoinPointHelper#getCallerClass()}.
	 */
	@Test
	public void getGetCaller() {
	    checkGetCaller(this.getClass());
	}

    private void checkGetCaller(final Class<?> expected) {
        assertEquals(expected, JoinPointHelper.getCallerClass());
    }

    /**
     * Test method for {@link JoinPointHelper#getParameterAnnotations(JoinPoint)}.
     */
    @Test
    public void testGetParameterAnnotations() {
        JoinPointMock jp = new JoinPointMock(this);
        Annotation[][] parameterAnnotations = JoinPointHelper.getParameterAnnotations(jp);
        assertNotNull(parameterAnnotations);
    }

    /**
     * Test method for {@link JoinPointHelper#getClassAnnotation(JoinPoint, Class)}.
     */
    @Test
    public void testGetClassAnnotation() {
        JoinPointMock jp = new JoinPointMock(this);
        Annotation annotation = JoinPointHelper.getClassAnnotation(jp, Immutable.class);
        assertNull(annotation);
    }

}
