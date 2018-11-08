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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patterntesting.runtime.exception.DetailedAssertionError;
import patterntesting.runtime.junit.internal.XrayClassLoader;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This tester examines classes. E.g. if looks if classes can be loaded
 * independent from other classes.
 * <p>
 * If you want to test some dependencies to a given class you can use this
 * tester here. This functionality was moved from the old ClassTester class in
 * the experimental package to here.
 * </p>
 *
 * @author oboehm (ob@aosd.de)
 * @since 1.6 (04.11.2015)
 */
public class ClassTester extends AbstractTester {

	private static final Logger LOG = LogManager.getLogger(ClassTester.class);
	private static final ClassTester INSTANCE = new ClassTester();

	private final Class<?> clazz;
	private final XrayClassLoader classLoader = AccessController.doPrivileged(new PrivilegedAction<XrayClassLoader>() {
		@Override
		public XrayClassLoader run() {
			return new XrayClassLoader();
		}
	});

	/** Private constructor for internal use. */
	private ClassTester() {
		this.clazz = this.getClass();
	}

	/**
	 * Instantiates a new class tester.
	 *
	 * @param classname
	 *            the classname
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	public ClassTester(final String classname) throws ClassNotFoundException {
		this.clazz = this.classLoader.loadClass(classname);
		if (clazz == null) {
			throw new ClassNotFoundException(classname);
		}
	}

	/**
	 * Gets the class name.
	 *
	 * @return the class name
	 */
	public String getClassName() {
		return this.clazz.getName();
	}

	/**
	 * Gets the dependencies of the class name. To get all dependencies we load
	 * all declared methods, fields and other stuff.
	 *
	 * @return the dependencies of
	 */
	public Set<Class<?>> getDependencies() {
		this.clazz.getDeclaredAnnotations();
		this.clazz.getDeclaredClasses();
		this.clazz.getInterfaces();
		this.clazz.getDeclaredConstructors();
		this.clazz.getDeclaredFields();
		this.clazz.getDeclaredMethods();
		return classLoader.getLoadedClasses();
	}

	/**
	 * Checks if the stored class depends on another class. Because the given
	 * otherClass is using probably another class loader we compare the class
	 * name to decide if it is loaded.
	 *
	 * @param otherClass
	 *            the other class
	 * @return true, if successful
	 */
	public boolean dependsOn(final Class<?> otherClass) {
		return dependsOn(otherClass.getName());
	}

	/**
	 * Checks if the stored class depends on another class.
	 *
	 * @param otherClassname
	 *            the other classname
	 * @return true, if successful
	 */
	public boolean dependsOn(final String otherClassname) {
		Set<Class<?>> dependencies = this.getDependencies();
		for (Class<?> cl : dependencies) {
			if (otherClassname.equals(cl.getName())) {
				return true;
			}
		}
		return false;
	}

	///// STATIC UTILITY METHODS //////////////////////////////////////////

	/**
	 * Gets the dependencies of the given class name.
	 *
	 * @param classname
	 *            the classname
	 * @return the dependencies of
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	public static Set<Class<?>> getDependenciesOf(final String classname) throws ClassNotFoundException {
		return new ClassTester(classname).getDependencies();
	}

	/**
	 * Checks if the given class depends on another class.
	 *
	 * @param classname
	 *            the classname
	 * @param otherClass
	 *            the other class
	 * @return true, if successful
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	public static boolean dependsOn(final String classname, final Class<?> otherClass) throws ClassNotFoundException {
		return new ClassTester(classname).dependsOn(otherClass);
	}

	/**
	 * Every class has an static initializer which will be called after a class
	 * is loaded. To find problems with such initializers or with dependencies
	 * to other classes which cannot be solved this method tries to load the
	 * class with an isolated classloader and will throw an
	 * {@link AssertionError} if it can't.
	 * <p>
	 * We expect a String as parameter and not a class because if we receive a
	 * class as parameter this class was already loaded before and we need not
	 * to check it again if it can be loaded.
	 * </p>
	 *
	 * @param classname
	 *            the class name.
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	public static void tryStaticInitializer(final String classname) throws ClassNotFoundException {
		try {
			INSTANCE.load(classname);
		} catch (ExceptionInInitializerError ex) {
			throw new DetailedAssertionError("cannot initialize class " + classname, ex);
		} catch (NoClassDefFoundError ex) {
			throw new DetailedAssertionError("cannot find class definition of " + classname, ex);
		} catch (IllegalAccessError ex) {
			LOG.warn("Cannot access {}:", classname, ex);
		}
	}

	private void load(final String classname) throws ClassNotFoundException {
		Class<?> loaded = Class.forName(classname);
		assertEquals(classname, loaded.getName());
		LOG.trace("{} was loaded by name.", loaded);
		loadClassWith(Thread.currentThread().getContextClassLoader(), classname);
		loadClassWith(classLoader, classname);
	}

	private static void loadClassWith(final ClassLoader classloader, final String classname)
			throws ClassNotFoundException {
		Class<?> initialized = Class.forName(classname, true, classloader);
		assertEquals(initialized.getName(), classname);
		LOG.trace("{} was loaded and initialized by {}.", initialized, classloader);
		Class<?> loaded = classloader.loadClass(classname);
		assertEquals(initialized, loaded);
	}

}
