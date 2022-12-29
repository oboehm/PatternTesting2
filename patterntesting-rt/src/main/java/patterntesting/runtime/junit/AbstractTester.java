/*
 * $Id: AbstractTester.java,v 1.7 2016/12/18 20:19:38 oboehm Exp $
 *
 * Copyright (c) 2015 by Oliver Boehm
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
 * (c)reated 07.01.2015 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.regex.Pattern;

import org.slf4j.LoggerFactory;
import org.slf4j.*;

/**
 * This is the common superclass for some tester classes defined in this
 * package.
 *
 * @author oliver
 * @version $Revision: 1.7 $
 * @since 1.6 (07.01.2015)
 */
public abstract class AbstractTester {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractTester.class);

	/** Utility class - no need to instantiate it. */
	protected AbstractTester() {
	}

	/**
	 * Removes "excluded" from the given classes. If one of the "excluded" class
	 * is an interface or abstract class all implementing or subclasses will be
	 * excluded.
	 *
	 * @param classes
	 *            the classes
	 * @param excluded
	 *            the excluded
	 */
	protected static void removeClasses(final Collection<?> classes, final List<?> excluded) {
		classes.removeAll(excluded);
		for (Object obj : excluded) {
			Class<?> clazz = (Class<?>) obj;
			if (clazz.isInterface() || isAbstract(clazz)) {
				removeAssignableClasses(classes, clazz);
			}
		}
	}

	private static boolean isAbstract(final Class<?> clazz) {
		return Modifier.isAbstract(clazz.getModifiers());
	}

	private static void removeAssignableClasses(final Collection<?> classes, final Class<?> superclass) {
		Collection<Class<?>> toBeDeleted = new ArrayList<>();
		for (Object obj : classes) {
			Class<?> clazz = (Class<?>) obj;
			if (superclass.isAssignableFrom(clazz)) {
				if (LOG.isTraceEnabled()) {
					LOG.trace("removing " + clazz + " from list of classes...");
				}
				toBeDeleted.add(clazz);
			}
		}
		classes.removeAll(toBeDeleted);
	}

	/**
	 * Removes "excluded" from the given classes.
	 *
	 * @param <T>
	 *            the generic type
	 * @param classes
	 *            the classes
	 * @param excluded
	 *            the excluded
	 */
	protected static <T> void removeClasses(final Collection<Class<? extends T>> classes, final Pattern... excluded) {
		final List<Class<? extends T>> toBeExcluded = new ArrayList<>();
		for (Class<? extends T> clazz : classes) {
			if (matches(clazz, excluded)) {
				toBeExcluded.add(clazz);
			}
		}
		removeClasses(classes, toBeExcluded);
	}

	private static boolean matches(final Class<?> clazz, final Pattern[] excluded) {
		String classname = clazz.getName();
		for (int i = 0; i < excluded.length; i++) {
			if (excluded[i].matcher(classname).matches()) {
				return true;
			}
		}
		return false;
	}

}
