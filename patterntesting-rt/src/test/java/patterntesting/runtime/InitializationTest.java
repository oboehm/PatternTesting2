/*
 * $Id: InitializationTest.java,v 1.8 2016/12/30 20:52:26 oboehm Exp $
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
 * (c)reated 22.11.2008 by oliver (ob@oasd.de)
 */
package patterntesting.runtime;

import static org.junit.Assert.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.*;
import org.junit.*;

import patterntesting.runtime.init.*;
import patterntesting.runtime.util.Assertions;

/**
 * When a class fails during static initialization (e.g. during a static block
 * in the beginning) you'll get later an suspicious ClassNotFoundException.
 * This is simulated with the test class.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.8 $
 * @since 22.11.2008
 */
public final class InitializationTest {

	private static final Logger LOG = LogManager.getLogger(InitializationTest.class);
	private static final String testClassName = CrashBean.class.getName();

	/**
	 * Setup before class.
	 */
	@BeforeClass
	public static void setupBeforeClass() {
		try {
			Class<?> cl = Class.forName(testClassName);
			LOG.info("{} succesful loaded.", cl);
		} catch (Throwable t) {
			LOG.info("Cannot load '{}':", testClassName, t);
		}
	}

	/**
	 * Test call static method.
	 */
	@Test
	public void testCallStaticMethod() {
		LOG.info("date: " + CrashBean.getDate());
	}

	/**
	 * Test no class def found error.
	 *
	 * @throws ClassNotFoundException the class not found exception
	 */
	@Test
	public void testNoClassDefFoundError() throws ClassNotFoundException {
		Class<?> cl = Class.forName(testClassName);
		LOG.info("got " + cl);
	}

	/**
	 * Test class loading.
	 *
	 * @throws ClassNotFoundException the class not found exception
	 */
	@Test
	public void testClassLoading() throws ClassNotFoundException {
		ClassLoader loader = this.getClass().getClassLoader();
		Class<?> cl = loader.loadClass(testClassName);
		LOG.info("got " + cl);
	}

    /**
     * Test assertions.
     */
    @Test
    public void testAssertions() {
        assertTrue("assertions must be enabled", Assertions.ENABLED);
    }

	/**
	 * Test run on initializer bug.
	 */
	@SuppressWarnings("unused")
    @Test
	public void testRunOnInitializerBug() {
	    try {
	        new RunOnInitializerBug();
	        fail("AssertionError expected");
	    } catch (AssertionError expected) {
	        String msg = expected.getMessage();
	        LOG.info(msg);
	        assertTrue('"' + msg + "\" does not contain unitialized variable",
                    StringUtils.contains(msg, "value"));
	    }
	}

}
