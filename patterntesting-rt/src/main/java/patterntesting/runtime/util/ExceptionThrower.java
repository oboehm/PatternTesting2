/*
 * $Id: ExceptionThrower.java,v 1.14 2016/12/18 20:19:36 oboehm Exp $
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
 * (c)reated 30.01.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.Test;

/**
 * Because we need the functionality of throwing any exception not only in
 * PatternTesting Exception but also here this functionality was shifted to
 * PatternTesting Runtime.
 * <p>
 * This class is not intended for public use. If you do so use it on your own
 * risk!
 * </p>
 *
 * @author oliver
 * @since 1.0 (30.01.2010)
 */
public final class ExceptionThrower {

	private static final Logger LOG = LogManager.getLogger(ExceptionThrower.class);

	/** only a utility class - no need to instantiate it. */
	private ExceptionThrower() {
	}

	/**
	 * Be careful - you can provoke any Exception with the method without the
	 * need to declare it with a throws statement. For example
	 *
	 * <pre>
	 * provoke(IOException.class)
	 * </pre>
	 *
	 * would throw an IOException.
	 * <p>
	 * WARNING: If the desired exception can't be instantiated an
	 * InstantiationException or IllegalAccessException may be thrown.
	 * </p>
	 * <p>
	 * WARNING(2): This method is not synchronized.
	 * </p>
	 *
	 * @param type
	 *            e.g. IOException.class
	 */
	public static void provoke(final Class<? extends Throwable> type) {
		Throwable t;
		try {
			t = create(type);
		} catch (ReflectiveOperationException ex) {
			LOG.debug("Cannot create {}:", type, ex);
			t = ex;
		}
		Thrower.provoke(t);
	}

	/**
	 * This method throws the expected exception wrapped into the {@link Test}
	 * annotation.
	 *
	 * @param test
	 *            with the expected exception
	 */
	public static void provoke(final Test test) {
		Class<? extends Throwable> expected = test.expected();
		if ((expected != Test.None.class) && (expected != null)) {
			ExceptionThrower.provoke(expected);
		}
	}

	/**
	 * Creates any desired exception you want. If the desired exception can't be
	 * instantiated an InstantiationException or IllegalAccessException may be
	 * thrown.
	 *
	 * @param type
	 *            the exception class you want to be created
	 * @return the instantiated exception or the caught exception
	 * @throws ReflectiveOperationException
	 *             the reflective operation exception
	 */
	public static Throwable create(final Class<? extends Throwable> type) throws ReflectiveOperationException {
		try {
			Constructor<? extends Throwable> ctor = type.getConstructor(String.class);
			return ctor.newInstance("created by ExceptionThrower");
		} catch (InvocationTargetException ex) {
			LOG.trace("Invocation of 'new {}(String)' failed:", type, ex);
		} catch (NoSuchMethodException ex) {
			LOG.trace("Can't call 'new {}(String)':", type, ex);
		}
		return type.newInstance();
	}

	/**
	 * The trick here is to use the constructor to throw any desired exception.
	 * So you can throw any exception without the need to have it as throws
	 * clause.
	 *
	 * @author <a href="boehm@javatux.de">oliver</a>
	 */
	static final class Thrower {
		private static Throwable throwable;

		private Thrower() throws Throwable {
			throw throwable;
		}

		/**
		 * Provoke an exception.
		 *
		 * @param t
		 *            the Throwable which should be used as provoked exception.
		 */
		public static synchronized void provoke(final Throwable t) {
			throwable = t;
			try {
				Thrower.class.newInstance();
			} catch (InstantiationException unexpected) {
				LOG.debug("can't instantiate Thrower class", unexpected);
			} catch (IllegalAccessException unexpected) {
				LOG.debug("can't access Thrower constructor", unexpected);
			}
		}
	}

}
