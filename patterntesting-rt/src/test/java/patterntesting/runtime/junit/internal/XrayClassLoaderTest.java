/*
 * $Id: XrayClassLoaderTest.java,v 1.6 2016/12/18 20:19:38 oboehm Exp $
 *
 * Copyright (c) 2010 by Oliver Boehm
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
 * (c)reated 26.12.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit.internal;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.Test;

/**
 * The Class XrayClassLoaderTest.
 *
 * @author oliver
 * @since 1.1 (26.12.2010)
 */
public final class XrayClassLoaderTest  {

    private static final Logger log = LogManager.getLogger(XrayClassLoaderTest.class);
    private final XrayClassLoader classLoader = AccessController.doPrivileged(new PrivilegedAction<XrayClassLoader>() {
        @Override
		public XrayClassLoader run() {
            return new XrayClassLoader();
        }
    });

    /**
     * Test method for {@link XrayClassLoader#loadClass(java.lang.String)}.
     *
     * @throws ClassNotFoundException the class not found exception
     */
    @Test
    public void testLoadClass() throws ClassNotFoundException {
        Class<?> loaded = classLoader.loadClass("patterntesting.runtime.monitor.ClasspathMonitor");
        assertNotNull(loaded);
        Set<Class<?>> loadedClasses = classLoader.getLoadedClasses();
        assertTrue(loadedClasses.size() + " class(es) loaded", loadedClasses.size() > 1);
        log.info("loaded classes:");
        for (Class<?> cl : loadedClasses) {
            log.info("\t" + cl);
        }
    }

    /**
     * Checks if a class can be instantiated with a loaded class. For the
     * success of this test is important that the used class has no dependencies
     * to another class.
     *
     * @throws ClassNotFoundException the class not found exception
     * @throws SecurityException the security exception
     * @throws NoSuchMethodException the no such method exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws IllegalAccessException the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     */
    @Test
    public void testInitClass() throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Class<?> loaded = classLoader.loadClass("patterntesting.runtime.junit.test.Sheep");
        assertNotNull(loaded);
        log.info(classLoader.getLoadedClasses().size() + " classes loaded before invoke");
        Method method = loaded.getMethod("baa");
        Object obj = method.invoke(null);
        assertNotNull(obj);
        log.info(classLoader.getLoadedClasses().size() + " classes loaded after invoke");
    }

    /**
	 * Test method for {@link XrayClassLoader#loadClass(String, boolean)}.
	 * Here we test a class which is already loaded by the classloader.
	 *
	 * @throws ClassNotFoundException the class not found exception
	 */
	@Test
	public void testLoadClassLoaded() throws ClassNotFoundException {
		checkLoadClass(XrayClassLoaderTest.class);
	}

	/**
	 * Test method for {@link XrayClassLoader#loadClass(String, boolean)}.
	 * Classes from the boot classpath can be only loaded by the system
	 * classloader. So we check here if this is handled correct.
	 *
	 * @throws ClassNotFoundException the class not found exception
	 */
	@Test
	public void testLoadJavaClass() throws ClassNotFoundException {
		checkLoadClass(Object.class);
	}

	private void checkLoadClass(final Class<?> testClass) throws ClassNotFoundException {
		Class<?> loadedClass = classLoader.loadClass(testClass.getName(), false);
		assertEquals(testClass.getName(), loadedClass.getName());
	}

}

