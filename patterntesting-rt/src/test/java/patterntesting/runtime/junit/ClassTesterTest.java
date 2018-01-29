/*
 * Copyright (c) 2015 by Oli B.
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
 * (c)reated 04.11.2015 by Oli B. (ob@aosd.de)
 */

package patterntesting.runtime.junit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Unit tests for {@link ClassTester} class.
 *
 * @author oboehm (ob@aosd.de)
 * @since 1.6 (04.11.2015)
 */
public final class ClassTesterTest {

	private static final Logger LOG = LogManager.getLogger(ClassTesterTest.class);
    private static ClassTester classTester;

    /**
     * Sets up the classTester.
     *
     * @throws ClassNotFoundException the class not found exception
     */
    @BeforeClass
    public static void setUpClassTester() throws ClassNotFoundException {
        classTester = new ClassTester("patterntesting.runtime.monitor.ClasspathMonitor");
    }

    /**
     * Test method for {@link ClassTester#getDependencies()}.
     */
    @Test
    public void testGetDependencies() {
        Set<Class<?>> dependencies = classTester.getDependencies();
        int size = dependencies.size();
        LOG.info(classTester.getClassName() + " depends on " + size + " other classes");
        assertTrue("only " + size + " dependent classed detected", size > 5);
    }

    /**
     * Test method for {@link ClassTester#dependsOn(Class)}.
     */
    @Test
    public void testDependsOn() {
        assertTrue(classTester.dependsOn(Logger.class));
    }

	/**
	 * Test method for {@link ClassTester#tryStaticInitializer(String)}.
	 *
	 * @throws ClassNotFoundException the class not found exception
	 */
	@Test
	public void testTryStaticInitializerOk() throws ClassNotFoundException {
		ClassTester.tryStaticInitializer(ClassTesterTest.class.getName());
	}

	/**
	 * Test method for {@link ClassTester#tryStaticInitializer(String)}.
	 *
	 * @throws ClassNotFoundException the class not found exception
	 */
	@Test(expected = ClassNotFoundException.class)
	public void testTryStaticInitializerWithMissingClass() throws ClassNotFoundException {
		ClassTester.tryStaticInitializer("does.not.exist");
	}

	/**
	 * Test method for {@link ClassTester#tryStaticInitializer(String)}.
	 *
	 * @throws ClassNotFoundException the class not found exception
	 */
	@Test(expected = AssertionError.class)
	public void testTryStaticInitializer() throws ClassNotFoundException {
		ClassTester.tryStaticInitializer("patterntesting.runtime.junit.test.Unloadable");
	}

	/**
	 * Test method for {@link ClassTester#getDependenciesOf(String)}.
	 *
	 * @throws ClassNotFoundException the class not found exception
	 */
	@Test
	public void testGetDependenciesOf() throws ClassNotFoundException {
	    Set<Class<?>> dependencies = ClassTester.getDependenciesOf(this.getClass().getName());
        assertFalse("non empty set expected", dependencies.isEmpty());
        LOG.info("dependencies = {}", dependencies);
	}

	/**
	 * Test method for {@link ClassTester#dependsOn(String, Class)}.
	 *
	 * @throws ClassNotFoundException the class not found exception
	 */
	@Test
	public void testGetDependsOn() throws ClassNotFoundException {
	    assertTrue(ClassTester.dependsOn(this.getClass().getName(), ClassTester.class));
	}

}
