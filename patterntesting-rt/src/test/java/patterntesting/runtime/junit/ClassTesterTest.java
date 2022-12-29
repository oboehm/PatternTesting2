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

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for {@link ClassTester} class.
 *
 * @author oboehm (ob@aosd.de)
 * @since 1.6 (04.11.2015)
 */
public final class ClassTesterTest {

	private final Logger LOG = LoggerFactory.getLogger(ClassTesterTest.class);
    private static ClassTester classTester;

    /**
     * Sets up the classTester.
     *
     * @throws ClassNotFoundException the class not found exception
     */
    @BeforeAll
    public static void setUpClassTester() throws ClassNotFoundException {
        classTester = new ClassTester("clazzfish.monitor.ClasspathMonitor");
    }

    /**
     * Test method for {@link ClassTester#getDependencies()}.
     */
    @Test
    public void testGetDependencies() {
        Set<Class<?>> dependencies = classTester.getDependencies();
        int size = dependencies.size();
        LOG.info(classTester.getClassName() + " depends on " + size + " other classes");
        assertThat("only " + size + " dependent classed detected", size, greaterThan(5));
    }

    /**
     * Test method for {@link ClassTester#dependsOn(Class)}.
     */
    @Test
    public void testDependsOn() {
        assertTrue(classTester.dependsOn(org.slf4j.Logger.class));
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
	 */
	@Test
	public void testTryStaticInitializerWithMissingClass() {
		assertThrows(ClassNotFoundException.class, new Executable() {
			public void execute() throws Throwable {
				ClassTester.tryStaticInitializer("does.not.exist");
			}
		});
	}

	/**
	 * Test method for {@link ClassTester#tryStaticInitializer(String)}.
	 */
	@Test
	public void testTryStaticInitializer() {
		assertThrows(AssertionError.class, new Executable() {
			public void execute() throws Throwable {
				ClassTester.tryStaticInitializer("patterntesting.runtime.junit.test.Unloadable");
			}
		});
	}

	/**
	 * Test method for {@link ClassTester#getDependenciesOf(String)}.
	 *
	 * @throws ClassNotFoundException the class not found exception
	 */
	@Test
	public void testGetDependenciesOf() throws ClassNotFoundException {
	    Set<Class<?>> dependencies = ClassTester.getDependenciesOf(this.getClass().getName());
        assertFalse(dependencies.isEmpty(), "non empty set expected");
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
