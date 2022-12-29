/*
 * $Id: ComparableTester.java,v 1.11 2017/11/09 20:34:50 oboehm Exp $
 *
 * Copyright (c) 2011 by Oliver Boehm
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
 * (c)reated 21.09.2011 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.jupiter.api.Assertions;
import clazzfish.monitor.ClasspathMonitor;
import patterntesting.runtime.util.Converter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This utility class checks classes which implements the {@link Comparable}
 * interface. E.g. for two objects which are equals it is expected that the
 * {@link Comparable#compareTo(Object)} method returns 0.
 *
 * @author oliver (ob@aosd.de)
 * @since 1.2 (21.09.2011)
 */
public final class ComparableTester extends AbstractTester {

	private static final Logger LOG = LoggerFactory.getLogger(ComparableTester.class);
	private static final ClasspathMonitor classpathMonitor = ClasspathMonitor.getInstance();

	/** Utility class - no need to instantiate it. */
	private ComparableTester() {
	}

	/**
	 * The {@link Comparable#compareTo(Object)} method should return 0 if the
	 * given objects are eqals. If they are not equals the shouldn't return 0.
	 * This is checked here.
	 *
	 * @param c1
	 *            the first Comparable
	 * @param c2
	 *            the second Comparable
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void assertCompareTo(final Comparable c1, final Comparable c2) {
		int ret1 = c1.compareTo(c2);
		int ret2 = c2.compareTo(c1);
		if (c1.equals(c2)) {
			String msg = c1.getClass() + ": compareTo(..) should return 0 for equals objects";
			Assertions.assertEquals(0, ret1, msg);
			Assertions.assertEquals(0, ret2, msg);
		} else {
			String msg = c1.getClass() + ": compareTo(..) should return not 0 for not equals objects " + c1 + " and "
					+ c2;
			Assertions.assertTrue(ret1 != 0, msg);
			Assertions.assertTrue(ret2 != 0, msg);
			msg = c1.getClass() + ": <" + c2 + ">.compareTo(<" + c1 + ">) should return " + (-ret2) + " (not " + ret2
					+ ")";
			if (ret1 < 0) {
				Assertions.assertTrue(ret2 > 0, msg);
			} else {
				Assertions.assertTrue(ret2 < 0, msg);
			}
		}
		LOG.info("compareTo implementation of " + c1.getClass() + " seems to be ok");
	}

	/**
	 * This method will create two objects of the given class using the default
	 * constructor and compares them. So two preconditions must be true:
	 * <ol>
	 * <li>the class must not be abstract</li>
	 * <li>there must be a (public) default constructor</li>
	 * </ol>
	 *
	 * @param clazz
	 *            the clazz
	 * @throws AssertionError
	 *             if the check fails
	 */
	@SuppressWarnings("rawtypes")
	public static void assertCompareTo(final Class<? extends Comparable> clazz) throws AssertionError {
		LOG.trace("checking {}.compareTo(..)...", clazz);
		Comparable<?> comp = (Comparable<?>) ObjectTester.newInstanceOf(clazz);
		Comparable<?> clone = (Comparable<?>) ObjectTester.clone(comp);
		assertCompareTo(comp, clone);
	}

	/**
	 * Check for each class in the given collection if the compareTo method
	 * works as expected.
	 *
	 * @param classes
	 *            a collection of classes to be checked
	 */
	@SuppressWarnings("rawtypes")
	public static void assertCompareTo(final Collection<Class<? extends Comparable>> classes) {
		for (Class<? extends Comparable> clazz : classes) {
			assertCompareTo(clazz);
		}
	}

	/**
	 * Check for each {@link Comparable} class in the given package if the
	 * compareTo(..) method works as expected.
	 * <p>
	 * To get a name of a package call {@link Package#getPackage(String)}. But
	 * be sure that you can't get <em>null</em> as result. In this case use
	 * {@link #assertCompareToOfPackage(String)}.
	 * </p>
	 *
	 * @param pkg
	 *            the package e.g. "patterntesting.runtime"
	 * @see #assertCompareToOfPackage(String)
	 */
	public static void assertCompareTo(final Package pkg) {
		assert pkg != null;
		assertCompareToOfPackage(pkg.getName());
	}

	/**
	 * Check for each {@link Comparable} class in the given package if the
	 * compareTo(..) method works as expected.
	 * <p>
	 * To get a name of a package call {@link Package#getPackage(String)}. But
	 * be sure that you can't get <em>null</em> as result. In this case use
	 * {@link #assertCompareToOfPackage(String)}.
	 * </p>
	 *
	 * @param pkg
	 *            the package e.g. "patterntesting.runtime"
	 * @param excluded
	 *            class pattern which should be excluded from the check
	 * @see #assertCompareToOfPackage(String, Pattern...)
	 * @since 1.6
	 */
	public static void assertCompareTo(final Package pkg, final Pattern... excluded) {
		assert pkg != null;
		assertCompareToOfPackage(pkg.getName(), excluded);
	}

	/**
	 * Check for each {@link Comparable} class in the given package if the
	 * compareTo(..) method works as expected.
	 *
	 * @param packageName
	 *            the package name e.g. "patterntesting.runtime"
	 */
	@SuppressWarnings("rawtypes")
	public static void assertCompareToOfPackage(final String packageName) {
		assert packageName != null;
		Collection<Class<? extends Comparable>> comparables = getComparableClasses(packageName);
		assertCompareTo(comparables);
	}

	/**
     * Check for each {@link Comparable} class in the given package if the
     * compareTo(..) method works as expected.
     *
     * @param packageName the package name e.g. "patterntesting.runtime"
     * @param excluded classes which should be excluded from the check
     * @see #assertCompareToOfPackage(String)
     */
	@SuppressWarnings("rawtypes")
    @SafeVarargs
    public static void assertCompareToOfPackage(final String packageName,
			final Class<? extends Comparable<?>>... excluded) {
        Collection<Class<? extends Comparable>> classes = getComparableClasses(packageName);
        List<Class<? extends Comparable<?>>> excludedList = Arrays.asList(excluded);
        LOG.debug("{} will be excluded from check.", excludedList);
        removeClasses(classes, excludedList);
        assertCompareTo(classes);
	}

	/**
	 * Check for each {@link Comparable} class in the given package if the
	 * compareTo(..) method works as expected.
	 *
	 * @param packageName
	 *            the package name e.g. "patterntesting.runtime"
	 * @param excluded
	 *            classes which should be excluded from the check
	 * @see #assertCompareToOfPackage(String)
	 * @since 1.6
	 */
	@SuppressWarnings("rawtypes")
	public static void assertCompareToOfPackage(final String packageName, final Pattern... excluded) {
		Collection<Class<? extends Comparable>> classes = getComparableClasses(packageName);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Pattern {} will be excluded from check.", Converter.toShortString(excluded));
		}
		removeClasses(classes, excluded);
		assertCompareTo(classes);
	}

	@SuppressWarnings("rawtypes")
	private static Collection<Class<? extends Comparable>> getComparableClasses(final String packageName) {
		Collection<Class<? extends Comparable>> comparables = classpathMonitor.getClassList(packageName,
				Comparable.class);
		return comparables;
	}

}
