/*
 * $Id: ObjectTester.java,v 1.55 2017/11/09 20:34:50 oboehm Exp $
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
 * (c)reated 21.07.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import clazzfish.monitor.ClasspathMonitor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.jupiter.api.Assertions;
import patterntesting.runtime.exception.DetailedAssertionError;
import patterntesting.runtime.util.Converter;
import patterntesting.runtime.util.ReflectionHelper;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * This is a utility class to check some important methods of a class like the
 * {@link Object#equals(Object)} or {@link Object#hashCode()} method. Before
 * v1.1 the methods are named "checkEquals" or "checkCompareTo". Since 1.1 these
 * methods have now an "assert" prefix ("assertEquals" or "assertCompareTo").
 *
 * @author oliver
 * @since 1.0.3 (21.07.2010)
 */
public final class ObjectTester extends AbstractTester {

	private static final Logger LOG = LoggerFactory.getLogger(ObjectTester.class);
	private static final ClasspathMonitor classpathMonitor = ClasspathMonitor.getInstance();

	/** Utility class - no need to instantiate it. */
	private ObjectTester() {
	}

	/**
	 * Check equality of the given objects. They must be equals otherwise an
	 * AssertionError will be thrown. And if <i>A == B</i> also
	 * <i>B == A</i> must be true (commutative law, i.e. it is a symmetrical
	 * operation).
	 * <p>
	 * If two objects are equals they must have also the same hash code (but not
	 * the other way around). This condition is also checked here.
	 * </p>
	 * <p>
	 * Often programmers forget that the {@link Object#equals(Object)} method
	 * can be called with <em>null</em> as argument and should return
	 * <em>false</em> as result. So this case is also tested here.
	 * </p>
	 *
	 * @param o1
	 *            the 1st object
	 * @param o2
	 *            the 2nd object
	 * @throws AssertionError
	 *             if the check fails
	 * @since 1.1
	 */
	@SuppressWarnings("unchecked")
	public static void assertEquals(final Object o1, final Object o2) throws AssertionError {
		if (o1 instanceof Package) {
			Package pkg = (Package) o1;
			if (o2 instanceof Class) {
				Class<?> c2 = (Class<?>) o2;
				Class<?>[] excluded = { c2 };
				assertEquals(pkg, excluded);
			} else if (o2 instanceof Pattern) {
				assertAllOfPackage(pkg.getName(), (Pattern) o2);
			}
		} else {
			Assertions.assertEquals(o1, o2, o1.getClass() + ": objects are not equals!");
			Assertions.assertEquals(o2, o1, o1.getClass() + ": equals not symmetrical (A == B, but B != A)");
			Assertions.assertEquals(o1.hashCode(), o2.hashCode(),
					o1.getClass() + ": objects are equals but hashCode differs!");
			if (o1 instanceof Comparable<?>) {
				ComparableTester.assertCompareTo((Comparable<Comparable<?>>) o1, (Comparable<Comparable<?>>) o1);
			}
			assertEqualsWithNull(o1);
		}
		LOG.info("equals/hashCode implementation of " + o1.getClass() + " seems to be ok");
	}

	/**
	 * Checks if the two given objects are really not equals. The following
	 * conditions should be fullfilled: if (a != b) then also (b != a) should be
	 * true. This is tested here.
	 *
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @throws AssertionError
	 *             the assertion error
	 * @since 1.5
	 */
	public static void assertNotEquals(final Object a, final Object b) throws AssertionError {
		assertFalse(a.equals(b), "expected: '" + a + "' != '" + b + "'");
		assertFalse(b.equals(a),
				a.getClass() + ": equals not symmetrical (A != B, but B == A) with A = '" + a + "' and B = '" + b + "'");
	}

	/**
	 * Null as argument for the equals method should always return 'false' and
	 * should not end with a NullPointerException.
	 *
	 * @param obj
	 *            the obj
	 */
	private static void assertEqualsWithNull(final Object obj) {
		try {
			assertFalse(obj.equals(null), obj.getClass().getName() + ".equals(null) should return 'false'");
		} catch (RuntimeException re) {
			throw new DetailedAssertionError(
					obj.getClass().getName() + ".equals(..) implementation does not check (correct) for null argument",
					re);
		}
	}

	/**
	 * The given object will be serialized and deserialized to get a copy of
	 * that object. The copy must be equals to the original object.
	 * <p>
	 * If the check fails (e.g. if copy and original object are not equals) an
	 * {@link AssertionError} will be thrown.
	 * </p>
	 *
	 * @param obj
	 *            the object
	 * @throws NotSerializableException
	 *             if obj is not serializable
	 * @since 1.1
	 */
	public static void assertEquals(final Serializable obj) throws NotSerializableException {
		Object clone = clone(obj);
		assertEquals(obj, clone);
	}

	/**
	 * The given object will be cloned to get a copy of that object. The copy
	 * must be equals to the original object.
	 *
	 * @param obj
	 *            the obj
	 * @throws AssertionError
	 *             the assertion error
	 * @since 1.1
	 */
	public static void assertEquals(final Cloneable obj) throws AssertionError {
		Object clone = CloneableTester.getCloneOf(obj);
		assertEquals(obj, clone);
	}

	/**
	 * This method will create two objects of the given class using the default
	 * constructor. So three preconditions must be true:
	 * <ol>
	 * <li>the class must not be abstract</li>
	 * <li>there must be a (public) default constructor</li>
	 * <li>it must be Cloneable, Serializable or return always the same object
	 * </li>
	 * </ol>
	 * That a constructor creates equals objects is not true for all classes.
	 * For example the default constructor of the Date class will generate
	 * objects with different timestamps which are not equal. But most classes
	 * should meet the precondition.
	 *
	 * @param clazz
	 *            the clazz
	 * @throws AssertionError
	 *             if the check fails
	 * @since 1.1
	 */
	public static void assertEquals(final Class<?> clazz) throws AssertionError {
		LOG.trace("checking {}.equals...", clazz);
		Object o1 = newInstanceOf(clazz);
		if (o1 instanceof Cloneable) {
			assertEquals((Cloneable) o1);
		} else if (o1 instanceof Serializable) {
			try {
				assertEquals((Serializable) o1);
			} catch (NotSerializableException nse) {
				throw new AssertionError(nse);
			}
		} else {
			Object o2 = newInstanceOf(clazz);
			assertEquals(o1, o2);
		}
	}

	/**
	 * Check for each class in the given collection if the equals() method is
	 * implemented correct.
	 *
	 * @param <T> the generic type
	 * @param classes the classes
	 * @throws Failures the collected assertion errors
	 * @since 1.1
	 */
	public static <T> void assertEquals(final Collection<Class<? extends T>> classes) throws Failures {
		Failures failures = new Failures();
		for (Class<?> clazz : classes) {
			try {
				assertEquals(clazz);
			} catch (AssertionError e) {
				LOG.warn("equals/hashCode implementation of " + clazz + " is NOT OK (" + e.getMessage() + ")");
				failures.add(clazz, e);
			}
		}
		if (failures.hasErrors()) {
			throw failures;
		}
	}

	/**
	 * Check for each class in the given package if the equals() method is
	 * implemented correct.
	 * <p>
	 * To get a name of a package call {@link Package#getPackage(String)}. But
	 * be sure that you can't get <em>null</em> as result. In this case use
	 * {@link #assertEqualsOfPackage(String)}.
	 * </p>
	 *
	 * @param pkg
	 *            the package e.g. "patterntesting.runtime"
	 * @see #assertEqualsOfPackage(String)
	 * @since 1.1
	 */
	public static void assertEquals(final Package pkg) {
		assert pkg != null;
		assertEqualsOfPackage(pkg.getName());
	}

	/**
	 * Check for each class in the given package if the equals() method is
	 * implemented correct.
	 * <p>
	 * To get a name of a package call {@link Package#getPackage(String)}. But
	 * be sure that you can't get <em>null</em> as result. In this case use
	 * {@link #assertEqualsOfPackage(String, Class...)}.
	 * </p>
	 *
	 * @param pkg
	 *            the package e.g. "patterntesting.runtime"
	 * @param excluded
	 *            classes which are excluded from the check
	 * @see #assertEqualsOfPackage(String, Class...)
	 * @since 1.1
	 */
	public static void assertEquals(final Package pkg, final Class<?>... excluded) {
		assert pkg != null;
		assertEqualsOfPackage(pkg.getName(), excluded);
	}

	/**
	 * Check for each class in the given package if the equals() method is
	 * implemented correct. E.g. if your unit test classes ends all with
	 * "...Test" and you want to remove them from the check you can call
	 *
	 * <pre>
	 * ObjectTester.assertEquals(pkg, Pattern.compile(".*Test"));
	 * </pre>
	 *
	 * @param pkg
	 *            the package
	 * @param excluded
	 *            class pattern which should be excluded from the check
	 * @since 1.6
	 */
	public static void assertEquals(final Package pkg, final Pattern... excluded) {
		assertEqualsOfPackage(pkg.getName(), excluded);
	}

	/**
	 * Check for each class in the given package if the equals() method is
	 * implemented correct.
	 * <p>
	 * This method does the same as {@link #assertEquals(Package)} but was
	 * introduced by {@link Package#getPackage(String)} sometimes return null if
	 * no class of this package is loaded.
	 * </p>
	 *
	 * @param packageName
	 *            the package name e.g. "patterntesting.runtime"
	 * @see #assertEquals(Package)
	 * @since 1.1
	 */
	public static void assertEqualsOfPackage(final String packageName) {
		Collection<Class<? extends Object>> classes = getClassesWithDeclaredEquals(packageName);
		assertEquals(classes);
	}

	/**
	 * Check for each class in the given package if the equals() method is
	 * implemented correct.
	 *
	 * @param packageName
	 *            the package name e.g. "patterntesting.runtime"
	 * @param excluded
	 *            classes which should be excluded from the check
	 * @see #assertEqualsOfPackage(String)
	 * @since 1.1
	 */
	public static void assertEqualsOfPackage(final String packageName, final Class<?>... excluded) {
		List<Class<?>> excludedList = Arrays.asList(excluded);
        Collection<Class<? extends Object>> classes = getClassesWithDeclaredEquals(packageName);
        LOG.debug("{} will be excluded from check.", excludedList);
        removeClasses(classes, excludedList);
        assertEquals(classes);
	}

	/**
	 * Check for each class in the given package if the equals() method is
	 * implemented correct. E.g. if your unit test classes ends all with
	 * "...Test" and you want to remove them from the check you can call
	 *
	 * <pre>
	 * ObjectTester.assertEqualsOfPackage("my.package", Pattern.compile(".*Test"));
	 * </pre>
	 *
	 * @param packageName
	 *            the package name e.g. "patterntesting.runtime"
	 * @param excluded
	 *            class pattern which should be excluded from the check
	 * @since 1.6
	 */
	public static void assertEqualsOfPackage(final String packageName, final Pattern... excluded) {
		Collection<Class<? extends Object>> classes = getClassesWithDeclaredEquals(packageName);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Pattern {} will be excluded from check.", Converter.toShortString(excluded));
		}
		removeClasses(classes, excluded);
		assertEquals(classes);
	}

	private static Collection<Class<? extends Object>> getClassesWithDeclaredEquals(final String packageName) {
		assert packageName != null;
		Collection<Class<? extends Object>> concreteClasses = classpathMonitor.getConcreteClassList(packageName);
		Collection<Class<? extends Object>> classes = new ArrayList<>(concreteClasses.size());
		for (Class<? extends Object> clazz : concreteClasses) {
			if (!Modifier.isPublic(clazz.getModifiers())) {
				LOG.debug(clazz + " will be ignored (class is not public)");
				continue;
			}
			if (!hasEqualsDeclared(clazz)) {
				LOG.debug(clazz + " will be ignored (equals(..) not overwritten)");
				continue;
			}
			classes.add(clazz);
		}
		return classes;
	}

	/**
	 * If you want to know if a class (or one of its super classes, except
	 * object) has overwritten the equals method you can use this method here.
	 *
	 * @param clazz
	 *            the clazz
	 * @return true, if successful
	 */
	public static boolean hasEqualsDeclared(final Class<?> clazz) {
		try {
			Method method = clazz.getMethod("equals", Object.class);
			Class<?> declaring = method.getDeclaringClass();
			return !declaring.equals(Object.class);
		} catch (SecurityException e) {
			LOG.info("can't get equals(..) method of " + clazz, e);
			return false;
		} catch (NoSuchMethodException ex) {
			LOG.trace("The equals method is not overwritten:", ex);
			return false;
		}
	}

	/**
	 * Check equality of the given objects by using the compareTo() method.
	 * Because casting an object to the expected Comparable is awesome we
	 * provide this additional method here
	 *
	 * @param o1
	 *            the first object (must be of type Comparable)
	 * @param o2
	 *            the second object (must be of type Comparable)
	 * @throws AssertionError
	 *             if the check fails
	 * @see ComparableTester#assertCompareTo(Comparable, Comparable)
	 * @since 1.1
	 */
	@SuppressWarnings("unchecked")
	public static void assertCompareTo(final Object o1, final Object o2) throws AssertionError {
		ComparableTester.assertCompareTo((Comparable<Comparable<?>>) o1, (Comparable<Comparable<?>>) o2);
	}

	/**
	 * If a object is only partially initalized it sometimes can happen, that
	 * calling the toString() method will result in a NullPointerException. This
	 * should not happen so there are several check methods available where you
	 * can proof it.
	 *
	 * @param obj
	 *            the object to be checked
	 * @since 1.1
	 */
	public static void assertToString(final Object obj) {
		if (hasToStringDefaultImpl(obj)) {
			LOG.info(obj.getClass() + " has default implementation of toString()");
		}
	}

	/**
	 * Normally you should overwrite the toString() method for better logging
	 * and debugging. This is the method to check it.
	 *
	 * @param obj
	 *            the object to be checked
	 * @return true, if object has default implementation
	 */
	public static boolean hasToStringDefaultImpl(final Object obj) {
		try {
			String s = obj.toString();
			return s.startsWith(obj.getClass().getName() + "@");
		} catch (RuntimeException ex) {
			LOG.info("The toString implementation of " + obj.getClass()
					+ " seems to be overwritten because error happens:", ex);
			return false;
		}
	}

	/**
	 * Normally you should overwrite the toString() method for better logging
	 * and debugging. This is the method to check it.
	 *
	 * @param clazz
	 *            the clazz
	 * @return true, if object has default implementation
	 */
	public static boolean hasToStringDefaultImpl(final Class<?> clazz) {
		Object obj = newInstanceOf(clazz);
		return hasToStringDefaultImpl(obj);
	}

	/**
	 * Starts all known checks like checkEquals(..), checks from the
	 * SerializableTester (if the given class is serializable) or from other
	 * classes.
	 *
	 * @param <T>
	 *            the generic type
	 * @param clazz
	 *            the clazz to be checked.
	 * @since 1.1
	 */
	@SuppressWarnings("unchecked")
	public static <T> void assertAll(final Class<? extends T> clazz) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("checking all of " + clazz + "...");
		}
		if (hasEqualsDeclared(clazz)) {
			assertEquals(clazz);
		}
		if (hasToStringDefaultImpl(clazz)) {
			LOG.info(clazz + " has default implementation of toString()");
		}
		if (clazz.isAssignableFrom(Serializable.class)) {
			try {
				SerializableTester.assertSerialization(clazz);
			} catch (NotSerializableException e) {
				throw new AssertionError(e);
			}
		}
		if (clazz.isAssignableFrom(Cloneable.class)) {
			CloneableTester.assertCloning((Class<Cloneable>) clazz);
		}
	}

	/**
	 * Check all.
	 *
	 * @param <T>
	 *            the generic type
	 * @param classes
	 *            the classes to be checked
	 * @since 1.1
	 */
	public static <T> void assertAll(final Collection<Class<? extends T>> classes) {
		for (Class<? extends T> clazz : classes) {
			assertAll(clazz);
		}
	}

	/**
	 * Starts all known checks for all classes of the given package.
	 * <p>
	 * To get a name of a package call {@link Package#getPackage(String)}. But
	 * be sure that you can't get <em>null</em> as result. In this case use
	 * {@link #assertAllOfPackage(String)}.
	 * </p>
	 *
	 * @param pkg
	 *            the package e.g. "patterntesting.runtime"
	 * @since 1.1
	 */
	public static void assertAll(final Package pkg) {
		assert pkg != null;
		assertAllOfPackage(pkg.getName());
	}

	/**
	 * Starts all known checks for all classes of the given package except for
	 * the "excluded" classes.
	 * <p>
	 * To get a name of a package call {@link Package#getPackage(String)}. But
	 * be sure that you can't get <em>null</em> as result. In this case use
	 * {@link #assertEqualsOfPackage(String, Class...)}.
	 * </p>
	 *
	 * @param pkg
	 *            the package e.g. "patterntesting.runtime"
	 * @param excluded
	 *            classes which are excluded from the check
	 * @see #assertAllOfPackage(String, Class...)
	 * @since 1.1
	 */
	public static void assertAll(final Package pkg, final Class<?>... excluded) {
		assert pkg != null;
		assertAllOfPackage(pkg.getName(), excluded);
	}

	/**
	 * Starts all known checks for all classes of the given package.
	 *
	 * @param packageName
	 *            the package e.g. "patterntesting.runtime"
	 * @since 1.1
	 */
	public static void assertAllOfPackage(final String packageName) {
		assert packageName != null;
		assertAllOfPackage(packageName, new Class<?>[0]);
	}

	/**
	 * Starts all known checks for all classes of the given package but not for
	 * the "excluded" classes.
	 *
	 * @param packageName
	 *            the package name e.g. "patterntesting.runtime"
	 * @param excluded
	 *            classes which should be excluded from the check
	 * @see #assertAllOfPackage(String)
	 * @since 1.1
	 */
	public static void assertAllOfPackage(final String packageName, final Class<?>... excluded) {
        assert packageName != null;
		List<Class<?>> excludedList = Arrays.asList(excluded);
        LOG.debug("{} will be excluded from check.", excludedList);
        Collection<Class<? extends Object>> classes = classpathMonitor.getConcreteClassList(packageName);
        classes.removeAll(excludedList);
        removeMemberClasses(classes);
        assertAll(classes);
	}

	/**
	 * Starts all known checks for all classes of the given package but not for
	 * the "excluded" classes. E.g. if your unit test classes ends all with
	 * "...Test" and you want to remove them from the check you can call
	 *
	 * <pre>
	 * ObjectTester.assertEqualsOfPackage(&quot;my.package&quot;, Pattern.compile(&quot;.*Test&quot;));
	 * </pre>
	 *
	 * @param packageName
	 *            the package name e.g. "patterntesting.runtime"
	 * @param excluded
	 *            class pattern which should be excluded from the check
	 * @since 1.6
	 */
	public static void assertAllOfPackage(final String packageName, final Pattern... excluded) {
		assert packageName != null;
		Collection<Class<? extends Object>> classes = classpathMonitor.getConcreteClassList(packageName);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Pattern {} will be excluded from check.", Converter.toShortString(excluded));
		}
		removeClasses(classes, excluded);
		removeMemberClasses(classes);
		assertAll(classes);
	}

	private static void removeMemberClasses(final Collection<Class<? extends Object>> classes) {
		Collection<Class<? extends Object>> memberClasses = new ArrayList<>();
		for (Class<? extends Object> clazz : classes) {
			if (clazz.isMemberClass()) {
				memberClasses.add(clazz);
			}
		}
		LOG.debug("Member classes {} will be also excluded from check.", memberClasses);
		classes.removeAll(memberClasses);
	}

	/**
	 * New instance of.
	 *
	 * @param clazz
	 *            the clazz
	 * @return the object
	 */
	static Object newInstanceOf(final Class<?> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("can't instantiate " + clazz, e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("can't access ctor of " + clazz, e);
		}
	}

	/**
	 * Clone.
	 *
	 * @param orig
	 *            the orig
	 * @return the serializable
	 * @throws NotSerializableException
	 *             the not serializable exception
	 */
	static Serializable clone(final Serializable orig) throws NotSerializableException {
		byte[] bytes = Converter.serialize(orig);
		try {
			return Converter.deserialize(bytes);
		} catch (ClassNotFoundException canthappen) {
			throw new IllegalArgumentException("cannot clone " + orig, canthappen);
		}
	}

	/**
	 * Clone.
	 *
	 * @param orig
	 *            the orig
	 * @return the object
	 */
	static Object clone(final Object orig) {
		if (orig instanceof Cloneable) {
			return CloneableTester.getCloneOf((Cloneable) orig);
		}
		try {
			return clone((Serializable) orig);
		} catch (ClassCastException e) {
			LOG.trace("{} is not serializable - fallback to attribute cloning", orig.getClass(), e);
		} catch (NotSerializableException nse) {
			LOG.warn("can't serialize {} - fallback to attribute cloning", orig.getClass(), nse);
		}
		Object clone = newInstanceOf(orig.getClass());
		Field[] fields = orig.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			if (ReflectionHelper.isStatic(fields[i])) {
				continue;
			}
			try {
				Object value = fields[i].get(orig);
				fields[i].set(clone, value);
			} catch (IllegalAccessException ex) {
				LOG.debug(fields[i] + " is ignored:", ex);
			}
		}
		return clone;
	}

}
