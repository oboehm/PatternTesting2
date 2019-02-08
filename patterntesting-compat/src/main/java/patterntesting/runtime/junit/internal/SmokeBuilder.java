/*
 * $Id: SmokeBuilder.java,v 1.4 2016/12/10 20:55:22 oboehm Exp $
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

package patterntesting.runtime.junit.internal;

import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.RunnerBuilder;

import patterntesting.runtime.junit.SmokeRunner;

/**
 * This builder returns a SmokeRunner for the test class. But only if the test
 * class is not annotated by another Runner.
 *
 * @author oliver
 * @since 1.2 (30.12.2011)
 */
public class SmokeBuilder extends RunnerBuilder {

	private final RunnerBuilder fallback;

	/**
	 * Instantiates a new smoke builder.
	 *
	 * @param builder
	 *            the builder
	 */
	public SmokeBuilder(final RunnerBuilder builder) {
		this.fallback = builder;
	}

	/**
	 * Returns a Runner for class. But only if the given testClass is not
	 * annotated with another Runner.
	 *
	 * @param testClass
	 *            the test class
	 * @return a SmokeRunner
	 * @throws Throwable
	 *             the throwable
	 * @see org.junit.runners.model.RunnerBuilder#runnerForClass(java.lang.Class)
	 */
	@Override
	public Runner runnerForClass(final Class<?> testClass) throws Throwable {
		Runner runner = this.fallback.runnerForClass(testClass);
		if (isDefaultRunner(runner)) {
			return new SmokeRunner(testClass);
		}
		return runner;
	}

	private static boolean isDefaultRunner(Runner runner) {
		Class<? extends Runner> runnerClass = runner.getClass();
		return runnerClass.equals(BlockJUnit4ClassRunner.class) || runnerClass.equals(JUnit38ClassRunner.class);
	}

}
