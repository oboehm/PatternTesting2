/*
 * $Id: SmokeSuite.java,v 1.5 2016/12/18 20:19:38 oboehm Exp $
 *
 * Copyright (c) 2012 by Oliver Boehm
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
 * (c)reated 30.12.2011 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import java.util.List;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

import patterntesting.runtime.junit.internal.SmokeBuilder;

/**
 * Suite implementation which understand the same annotations like the
 * {@link SmokeRunner} class.
 * <p>
 * In JUnit 4.4 the used constructors of the superclass throws an
 * InitializationError which was located in an internal package. Because this
 * exception is deprecated since JUnit 4.5 we no longer try to support JUnit 4.4
 * (there were also other reasons we do not support JUnit 4.4 - e.g we need an
 * additional SmokeSuite(Class) constructor which would not compile without
 * tricks on new versions of JUnit).
 * </p>
 *
 * @author oliver
 * @since 1.2 (30.12.2011)
 */
public class SmokeSuite extends Suite {

	/**
	 * Instantiates a new smoke suite.
	 *
	 * @param klass
	 *            the klass
	 * @param runners
	 *            the runners
	 * @throws InitializationError
	 *             the initialization error
	 */
	public SmokeSuite(final Class<?> klass, final List<Runner> runners) throws InitializationError {
		super(klass, runners);
	}

	/**
	 * Instantiates a new smoke suite.
	 *
	 * @param klass
	 *            the klass
	 * @param builder
	 *            the builder
	 * @throws InitializationError
	 *             the initialization error
	 */
	public SmokeSuite(final Class<?> klass, final RunnerBuilder builder) throws InitializationError {
		super(klass, new SmokeBuilder(builder));
	}

	/**
	 * Instantiates a new smoke suite.
	 *
	 * @param builder
	 *            the builder
	 * @param klass
	 *            the klass
	 * @param suiteClasses
	 *            the suite classes
	 * @throws InitializationError
	 *             the initialization error
	 */
	public SmokeSuite(final RunnerBuilder builder, final Class<?> klass, final Class<?>[] suiteClasses)
			throws InitializationError {
		super(new SmokeBuilder(builder), klass, suiteClasses);
	}

	/**
	 * Instantiates a new smoke suite.
	 *
	 * @param builder
	 *            the builder
	 * @param classes
	 *            the classes
	 * @throws InitializationError
	 *             the initialization error
	 */
	public SmokeSuite(final RunnerBuilder builder, final Class<?>[] classes) throws InitializationError {
		super(new SmokeBuilder(builder), classes);
	}

	/**
	 * Instantiates a new smoke suite.
	 *
	 * @param klass
	 *            the klass
	 * @param suiteClasses
	 *            the suite classes
	 * @throws InitializationError
	 *             the initialization error
	 */
	protected SmokeSuite(final Class<?> klass, final Class<?>[] suiteClasses) throws InitializationError {
		super(klass, suiteClasses);
	}

	/**
	 * Run. It is only overwritten for better testing support.
	 *
	 * @param notifier
	 *            the notifier
	 * @see org.junit.runners.ParentRunner#run(org.junit.runner.notification.RunNotifier)
	 */
	@Override
	public void run(final RunNotifier notifier) {
		super.run(notifier);
	}

}
