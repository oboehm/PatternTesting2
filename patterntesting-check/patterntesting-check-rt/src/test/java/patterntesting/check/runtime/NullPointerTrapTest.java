/*
 * $Id: NullPointerTrapTest.java,v 1.11 2016/12/18 21:59:31 oboehm Exp $
 *
 * Copyright (c) 2007 by Oliver Boehm
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
 * (c)reated 11.12.2007 by oliver (ob@oasd.de)
 */
package patterntesting.check.runtime;

import static org.junit.Assert.*;

import org.apache.commons.logging.impl.SimpleLog;
import org.junit.Test;
import org.apache.logging.log4j.*;

import patterntesting.annotation.check.runtime.*;
import patterntesting.runtime.annotation.LogThrowable;

/**
 * This is the test suite for the NullPointerTrap aspect.
 * It uses annotations to mark the methods which allows null as parameter
 * or null as return value.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.11 $
 * @since 11.12.2007
 */
public final class NullPointerTrapTest extends AbstractRuntimeTest {

	private static final Logger LOG = LogManager.getLogger(NullPointerTrapTest.class);

	/**
	 * Test null arg.
	 */
    @Test(expected = AssertionError.class)
	public void testNullArg() {
	    echo(null);
	}

	/**
	 * Test null arg2.
	 */
    @Test(expected = AssertionError.class)
    public void testNullArg2() {
        echo("one", null);
    }

	/**
	 * Test null args allowed by annotation.
	 */
	@Test
	public void testNullArgsAllowedByAnnotation() {
		acceptAll(null);
	}

	/**
	 * This test method is allowed to have <i>null</i> as argument.
	 * This is marked by the NullArgsAllowed annotation.
	 * <p>
	 * This method is protected because it is an example and should be
	 * visible in the JavaDoc.
     * </p>
	 *
	 * @param arg can be null
	 * @see NullArgsAllowed
	 */
	@NullArgsAllowed
	protected void acceptAll(final String arg) {
		if (arg == null) {
			LOG.debug("(null) argument given");
		}
	}

	/**
	 * Test null return allowed by annotation.
	 */
	@Test
	public void testNullReturnAllowedByAnnotation() {
		Object nix = getNullObject();
		assertNull(nix);
	}

	/**
	 * This method can return a null value. This is marked by the
	 * MayReturnNull annotation.
	 * <p>
	 * This method is protected because it is an example and should be
	 * visible in the JavaDoc.
     * </p>
	 *
	 * @return null
	 * @see MayReturnNull
	 */
	@MayReturnNull
	protected Object getNullObject() {
		return null;
	}

	/**
	 * In the NullDummy all is allowed - null as arguments and null as
	 * return value.
	 */
	@Test
	public void testNullDummyCreation() {
		new NullDummy(null);
	}

	/**
	 * Test null dummy with null args.
	 */
	@Test
	public void testNullDummyWithNullArgs() {
		NullDummy.acceptAll(null);
	}

	/**
	 * Test null dummy null return.
	 */
	@Test
	public void testNullDummyNullReturn() {
		NullDummy.returnNull();
	}

	/**
	 * Test echo.
	 */
	@Test
	public void testEcho() {
	    assertEquals("hello", echo("hello"));
	}

	/**
	 * This test should fail because null as arguments and as return value is
	 * not allowed for the echo method.
	 */
    @Test(expected = AssertionError.class)
	public void testNullEcho() {
    	echo(null);
	}

    /**
     * This test should fail because null as arguments and as return value is
     * not allowed for the staticEcho method.
     */
    @Test(expected = AssertionError.class)
    public void testNullStaticEcho() {
        echo(null);
    }

	/**
	 * This method is here to avoid comiler warnings about non-matching aspects
	 * in (Abstract)NullPointerTrap.
	 *
	 * @param s the s
	 *
	 * @return the string
	 */
    @LogThrowable(SimpleLog.LOG_LEVEL_INFO)
	public String echo(final String s) {
	    return s;
	}

    /**
     * Echo.
     *
     * @param s1 the s1
     * @param s2 the s2
     *
     * @return the string
     */
    public String echo(final String s1, final String s2) {
        return s1 + s2;
    }

    /**
     * This is only a (silly) example for a method where an argument can be
     * null.
     *
     * @param s1 a valid String
     * @param s2 a valid String
     * @param s3 may be null
     *
     * @return a concatenated string
     */
    public String echo(final String s1, final String s2, @MayBeNull final String s3) {
        if (s3 == null) {
            return s1 + s2;
        } else {
            return s1 + s2 + s3;
        }
    }

    /**
     * This is a static echo method.
     *
     * @param s the s
     * @return the string
     */
    public static String staticEcho(final String s) {
        return s;
    }

    /**
     * Test default constructor.
     */
    @Test
    public final void testDefaultConstructor() {
        Object obj = new NullPointerTrapTest();
        assertNotNull(obj);
    }

    /**
     * Test inner class default constructor.
     */
    @Test
    public void testInnerClassDefaultConstructor() {
        Object obj = new InnerClass();
        assertNotNull(obj);
    }

    /**
     * This inner class causes some trouble because if you call
     * <code>new InnerClass()</code> the Java compiler adds an additional
     * (invisible) parameter to this class. So the pointcut
     * <code>execution(*..*.new(*, ..))</code> matches also this inner
     * constructor.
     *
     * @author <a href="boehm@javatux.de">oliver</a>
     * @since 18.03.2009
     * @version $Revision: 1.11 $
     */
    private static class InnerClass {
        //public InnerClass() {}
    }

    /**
     * Test may be null argument.
     */
    @Test
    public void testMayBeNullArgument() {
        echo("hello ", "world", null);
    }

}
