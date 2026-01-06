/*
 * Copyright (c) 2010-2026 by Oliver Boehm
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
 * (c)reated 06.08.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import patterntesting.runtime.exception.DetailedAssertionError;
import patterntesting.runtime.util.Converter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This tester checks class which implements Clonable and has therefore the
 * clone method implemented. According {@link Object#clone()} the following
 * conditions should be true: <blockquote>
 *
 * <pre>
 * x.clone() != x
 * </pre>
 *
 * </blockquote> will be true, and that the expression: <blockquote>
 *
 * <pre>
 * x.clone().getClass() == x.getClass()
 * </pre>
 *
 * </blockquote> will be <i>true</i>, but these are not absolute requirements.
 * While it is typically the case that:
 *
 * <pre>
 * x.clone().equals(x)
 * </pre>
 *
 * will be <i>true</i>, this is not an absolute requirement.
 * <p>
 * So the equals method will be only checked if it is overwritten.
 * </p>
 * <p>
 * NOTE: In the future this class will be perhaps part of the ObjectTester
 * class.
 * </p>
 * <p>
 * Before v1.1 the methods are named "checkCloning". Since 1.1 these methods
 * will have now an "assert" prefix ("assertCloning").
 * </p>
 *
 * @author oliver
 * @since 1.0.2 (06.08.2010)
 */
public final class CloneableTester extends AbstractTester {

	private static final Logger log = LoggerFactory.getLogger(CloneableTester.class);

	/** Utility class - no need to instantiate it. */
	private CloneableTester() {
	}

	/**
	 * Check cloning.
	 *
	 * @param clazz
	 *            the clazz
	 * @since 1.1
	 */
	public static void assertCloning(final Class<? extends Cloneable> clazz) {
		try {
			assertCloning(clazz.getDeclaredConstructor().newInstance());
		} catch (ReflectiveOperationException e) {
			throw new DetailedAssertionError("can't instantiate " + clazz, e);
		}
	}

	/**
	 * We call the clone method of the given orig paramter.Because the clone
	 * method is normally "protected" we use reflection to call it. Then we
	 * compare the orig and cloned object which should be equals.
	 *
	 * @param orig
	 *            the original object
	 * @since 1.1
	 */
	public static void assertCloning(final Cloneable orig) {
		Cloneable clone = getCloneOf(orig);
		if (ObjectTester.hasEqualsDeclared(orig.getClass())) {
			ObjectTester.assertEquals(orig, clone);
		}
		if (!orig.getClass().equals(clone.getClass())) {
			throw new AssertionError(
					orig.getClass().getName() + ".clone() creates another type: " + clone.getClass().getSimpleName());
		}
	}

	/**
	 * Gets the clone of the given Cloneable object.
	 *
	 * @param orig
	 *            the orig
	 * @return the clone of
	 * @throws AssertionError
	 *             the assertion error
	 */
	public static Cloneable getCloneOf(final Cloneable orig) throws AssertionError {
		Class<? extends Cloneable> cloneClass = orig.getClass();
		try {
			Method cloneMethod = cloneClass.getMethod("clone");
			Cloneable clone = (Cloneable) cloneMethod.invoke(orig);
			if (clone == orig) {
				throw new AssertionError(clone + " must have another reference as original object");
			}
			return clone;
		} catch (ReflectiveOperationException ex) {
			if (hasCloneMethod(cloneClass)) {
				throw new DetailedAssertionError("clone() is not public in " + cloneClass, ex);
			}
			throw new DetailedAssertionError("can't invoke clone method found in " + cloneClass, ex);
		} catch (SecurityException e) {
			throw new DetailedAssertionError("can't access clone method of " + orig.getClass(), e);
		}
	}

	private static boolean hasCloneMethod(final Class<? extends Cloneable> cloneClass) {
		try {
			Method m = cloneClass.getDeclaredMethod("clone");
			if (log.isTraceEnabled()) {
				log.trace(m + " found in " + cloneClass);
			}
			return true;
		} catch (NoSuchMethodException e) {
			log.trace(cloneClass + " has no clone method:", e);
			return false;
		}
	}

	/**
	 * Check for each class in the given collection if it can be cloned correct.
	 *
	 * @param classes
	 *            a collection of classes to be checked
	 * @since 1.1
	 */
	public static void assertCloning(final Collection<Class<? extends Cloneable>> classes) {
		for (Class<? extends Cloneable> clazz : classes) {
			assertCloning(clazz);
		}
	}

	/**
	 * Check for each class in the given package if it can be cloned correct.
	 *
	 * @param pkg the package e.g. "patterntesting.runtime"
	 * @see #assertCloningOfPackage(String)
	 * @since 1.1
	 */
	public static void assertCloning(final Package pkg) {
		assert pkg != null;
		assertCloningOfPackage(pkg.getName());
	}

	/**
	 * Check for each class in the given package if it can be cloned correct.
	 *
	 * @param pkg      the package
	 * @param excluded class pattern which should be excluded from the check
	 * @since 1.6
	 */
	public static void assertCloning(final Package pkg, final Pattern... excluded) {
		assert pkg != null;
		assertCloningOfPackage(pkg.getName(), excluded);
	}

	/**
	 * Check for each class in the given package if it can be cloned correct.
	 *
	 * @param packageName
	 *            the package name e.g. "patterntesting.runtime"
	 * @since 1.1
	 */
	public static void assertCloningOfPackage(final String packageName) {
		assert packageName != null;
		Collection<Class<? extends Cloneable>> cloneables = getCloneableClasses(packageName);
		assertCloning(cloneables);
	}

	/**
	 * Check for each class in the given package if the clone method is
	 * implemented correct.
	 *
	 * @param packageName
	 *            the package name e.g. "patterntesting.runtime"
	 * @param excluded
	 *            classes which should be excluded from the check
	 * @see #assertCloningOfPackage(String)
	 * @since 1.1
	 */
	@SuppressWarnings("unchecked")
	public static void assertCloningOfPackage(final String packageName, final Class<? extends Cloneable>... excluded) {
        log.debug("{} will be excluded from check.", Arrays.toString(excluded));
        Collection<Class<? extends Cloneable>> classes = getCloneableClasses(packageName);
        List<Class<Cloneable>> excludedList = Arrays.asList((Class<Cloneable>[]) excluded);
        removeClasses(classes, excludedList);
        assertCloning(classes);
	}

	/**
	 * Check for each class in the given package if the clone method is
	 * implemented correct.
	 *
	 * @param packageName
	 *            the package name e.g. "patterntesting.runtime"
	 * @param excluded
	 *            pattern for classes which should be excluded from the check
	 * @see #assertCloningOfPackage(String)
	 * @since 1.6
	 */
	public static void assertCloningOfPackage(final String packageName, final Pattern... excluded) {
		Collection<Class<? extends Cloneable>> classes = getCloneableClasses(packageName);
		if (log.isDebugEnabled()) {
			log.debug("Pattern {} will be excluded from check.", Converter.toShortString(excluded));
		}
		removeClasses(classes, excluded);
		assertCloning(classes);
	}

	private static Collection<Class<? extends Cloneable>> getCloneableClasses(final String packageName) {
		return getClassList(packageName, Cloneable.class);
	}

	static <T> Collection<Class<? extends T>> getClassList(final String packageName, final Class<T> type) {
		Collection<Class<? extends T>> classes = new ArrayList<>();
		Collection<Class<?>> concreteClasses = ObjectTester.getConcreteClassList(packageName);
		for (Class<?> clazz : concreteClasses) {
			if (type.isAssignableFrom(clazz)) {
				classes.add((Class<T>) clazz);
				log.trace("subclass of {} found: {}", type, clazz);
			}
		}
		return classes;
	}

}
