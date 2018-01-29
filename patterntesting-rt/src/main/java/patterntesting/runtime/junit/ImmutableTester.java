/*
 * Copyright (c) 2016 by Oli B.
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
 * (c)reated 09.02.2016 by Oli B. (ob@aosd.de)
 */

package patterntesting.runtime.junit;

import java.lang.reflect.*;
import java.util.*;

import javax.annotation.concurrent.Immutable;

import org.apache.logging.log4j.*;

import patterntesting.runtime.monitor.ClasspathMonitor;

/**
 * This is a utility class to test if a class is really (strict) Immuable.
 * Immutable with this tester means, that <em>all</em> fields must be final.
 *
 * @author oboehm (ob@aosd.de)
 * @since 1.6 (09.02.2016)
 */
public final class ImmutableTester extends AbstractTester {

	private static final Logger LOG = LogManager.getLogger(ImmutableTester.class);
	private static final ClasspathMonitor classpathMonitor = ClasspathMonitor.getInstance();

	/**
	 * Assert that a class is immutable.
	 * <p>
	 * An immutable class cannot be changed. I.e. the attributes of this class
	 * should be final - and this is tested by this method. This is a strict
	 * form an immutable check. If you don't agree with this strict check do not
	 * use this method.
	 * </p>
	 *
	 * @param clazz
	 *            the clazz to be examined
	 * @since 1.6.1
	 */
	public static void assertImmutable(final Class<? extends Object> clazz) {
		for (Field field : clazz.getDeclaredFields()) {
			int modifiers = field.getModifiers();
			if (!Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)) {
				throw new AssertionError(field + " should be final in immutable " + clazz);
			}
		}
		LOG.trace("{} seems to be immutable.", clazz);
	}

	/**
	 * Check for each class with the {@link Immutable} annotation if it is
	 * really (strict) Immutable and has on non-final field.
	 * <p>
	 * To get a name of a package call {@link Package#getPackage(String)}. But
	 * be sure that you can't get <em>null</em> as result. In this case use
	 * {@link #assertImmutableOfPackage(String)}.
	 * </p>
	 *
	 * @param pkg
	 *            the package e.g. "patterntesting.runtime"
	 * @see #assertImmutableOfPackage(String)
	 * @since 1.6.1
	 */
	public static void assertImmutable(final Package pkg) {
		assert pkg != null;
		assertImmutableOfPackage(pkg.getName());
	}

	/**
	 * Check for each class with the {@link Immutable} annotation if it is
	 * really (strict) Immutable and has on non-final field.
	 * <p>
	 * To get a name of a package call {@link Package#getPackage(String)}. But
	 * be sure that you can't get <em>null</em> as result. In this case use
	 * {@link #assertImmutableOfPackage(String, Class...)}.
	 * </p>
	 *
	 * @param pkg
	 *            the package e.g. "patterntesting.runtime"
	 * @param excluded
	 *            classes which are excluded from the check
	 * @see #assertImmutableOfPackage(String, Class...)
	 * @since 1.6.1
	 */
	public static void assertImmutable(final Package pkg, final Class<?>... excluded) {
		assert pkg != null;
		assertImmutableOfPackage(pkg.getName(), excluded);
	}

	/**
	 * Check for each class with the {@link Immutable} annotation if it is
	 * really (strict) Immutable and has on non-final field.
	 * <p>
	 * This method does the same as {@link #assertImmutable(Package)} but was
	 * introduced by {@link Package#getPackage(String)} sometimes return null if
	 * no class of this package is loaded.
	 * </p>
	 *
	 * @param packageName
	 *            the package name e.g. "patterntesting.runtime"
	 * @see #assertImmutable(Package)
	 * @since 1.6.1
	 */
	public static void assertImmutableOfPackage(final String packageName) {
		Collection<Class<? extends Object>> classes = getImmutableClasses(packageName);
		assertImmutable(classes);
	}

	/**
	 * Check for each class with the {@link Immutable} annotation if it is
	 * really (strict) Immutable and has on non-final field.
	 *
	 * @param packageName
	 *            the package name e.g. "patterntesting.runtime"
	 * @param excluded
	 *            classes which should be excluded from the check
	 * @see #assertImmutableOfPackage(String)
	 * @since 1.6.1
	 */
	public static void assertImmutableOfPackage(final String packageName, final Class<?>... excluded) {
		Collection<Class<? extends Object>> classes = getImmutableClasses(packageName);
		for (int i = 0; i < excluded.length; i++) {
			classes.remove(excluded[i]);
		}
		assertImmutable(classes);
	}

	/**
	 * Checks for each class in the given collection if it is immutable. I.e. if
	 * the class has only final fields.
	 *
	 * @param classes
	 *            the classes
	 */
	public static void assertImmutable(final Collection<Class<? extends Object>> classes) {
		for (Class<? extends Object> clazz : classes) {
			assertImmutable(clazz);
		}
	}

	private static Collection<Class<? extends Object>> getImmutableClasses(final String packageName) {
		assert packageName != null;
		Collection<Class<? extends Object>> concreteClasses = classpathMonitor.getConcreteClassList(packageName);
		Collection<Class<? extends Object>> classes = new ArrayList<>(concreteClasses.size());
		for (Class<? extends Object> clazz : concreteClasses) {
			if (clazz.getAnnotation(Immutable.class) != null) {
				LOG.debug("{} has @Immutable annotation.", clazz);
				classes.add(clazz);
			}
		}
		return classes;
	}

}
