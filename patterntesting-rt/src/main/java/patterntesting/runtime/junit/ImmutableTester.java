/*
 * Copyright (c) 2016-2026 by Oli B.
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.Immutable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This is a utility class to test if a class is really (strict) Immuable.
 * Immutable with this tester means, that <em>all</em> fields must be final.
 *
 * @author oboehm (ob@aosd.de)
 * @since 1.6 (09.02.2016)
 */
public final class ImmutableTester extends AbstractTester {

	private static final Logger log = LoggerFactory.getLogger(ImmutableTester.class);

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
	public static void assertImmutable(final Class<?> clazz) {
		for (Field field : clazz.getDeclaredFields()) {
			int modifiers = field.getModifiers();
			if (!Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)) {
				throw new AssertionError(field + " should be final in immutable " + clazz);
			}
		}
		log.trace("{} seems to be immutable.", clazz);
	}

	/**
	 * Check for each class with the {@link Immutable} annotation if it is
	 * really (strict) Immutable and has on non-final field.
	 *
	 * @param pkg the package e.g. "patterntesting.runtime"
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
	 *
	 * @param pkg      the package e.g. "patterntesting.runtime"
	 * @param excluded classes which are excluded from the check
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
	 *
	 * @param packageName the package name e.g. "patterntesting.runtime"
	 * @see #assertImmutable(Package)
	 * @since 1.6.1
	 */
	public static void assertImmutableOfPackage(final String packageName) {
		Collection<Class<?>> classes = getImmutableClasses(packageName);
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
		Collection<Class<?>> classes = getImmutableClasses(packageName);
        for (Class<?> aClass : excluded) {
            classes.remove(aClass);
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
	public static void assertImmutable(final Collection<Class<?>> classes) {
		for (Class<?> clazz : classes) {
			assertImmutable(clazz);
		}
	}

	private static Collection<Class<?>> getImmutableClasses(final String packageName) {
		assert packageName != null;
		Collection<Class<?>> concreteClasses = ObjectTester.getConcreteClassList(packageName);
		Collection<Class<?>> classes = new ArrayList<>(concreteClasses.size());
		for (Class<?> clazz : concreteClasses) {
			if (clazz.getAnnotation(Immutable.class) != null) {
				log.debug("{} has @Immutable annotation.", clazz);
				classes.add(clazz);
			}
		}
		return classes;
	}

}
