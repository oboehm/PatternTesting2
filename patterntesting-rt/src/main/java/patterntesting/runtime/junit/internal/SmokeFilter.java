/*
 * $Id: SmokeFilter.java,v 1.22 2016/12/18 20:19:41 oboehm Exp $
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
 * (c)reated 03.05.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit.internal;

import java.lang.annotation.Annotation;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;

import patterntesting.annotation.check.runtime.MayReturnNull;
import patterntesting.runtime.NullConstants;
import patterntesting.runtime.annotation.*;
import patterntesting.runtime.util.Converter;
import patterntesting.runtime.util.Environment;

/**
 * This filter handles the different annotations like Broken, RunTestOn,
 * SkipTestOn, IntegrationTest or SmokeTest.
 *
 * @author oliver
 * @since 1.0 (03.05.2010)
 */
public final class SmokeFilter extends Filter {

	private static final Logger LOG = LogManager.getLogger(SmokeFilter.class);
	private final Map<Description, Boolean> shouldRunCache = new HashMap<>();
	private Date today = new Date();
	private Environment env = Environment.INSTANCE;
	private int filteredOut = 0;

	/**
	 * For testing (e.g. to check the past) you can set the today's date.
	 *
	 * @param date
	 *            the new today's date
	 */
	public void setToday(final Date date) {
		this.today = new Date(date.getTime());
	}

	/**
	 * For testing you can set environment.
	 *
	 * @param env
	 *            the new environment
	 */
	public void setEnvironment(final Environment env) {
		this.env = env;
	}

	/**
	 * Describes the kind of test e.g. "IntegrationTest".
	 *
	 * @return e.g. "all tests including integration tests"
	 * @see org.junit.runner.manipulation.Filter#describe()
	 */
	@Override
	public String describe() {
		if (Environment.isPropertyEnabled(Environment.INTEGRATION_TEST)) {
			return "all tests including integration tests";
		}
		if (Environment.isPropertyEnabled(Environment.RUN_SMOKE_TESTS)) {
			return "tests marked as @SmokeTest";
		}
		return "all tests (except tests marked as @Broken)";
	}

	/**
	 * Handles the different annotations like Broken, RunTestOn, SkipTestOn,
	 * IntegrationTest or SmokeTest. We use a cache (shouldRunCache) here in
	 * this implementation to avoid to much logging of "...run method / skip
	 * method..." messages.
	 *
	 * @param description
	 *            containing the JUnit class and test method
	 * @return depends on the annotation
	 * @see org.junit.runner.manipulation.Filter#shouldRun(org.junit.runner.Description)
	 */
	@Override
	public boolean shouldRun(final Description description) {
		Boolean value = shouldRunCache.get(description);
		if (value != null) {
			return value;
		}
		try {
			if (Environment.SMOKE_TEST_ENABLED) {
				value = isSmokeTest(description);
			} else {
				value = !isIntegrationTestIgnored(description);
			}
			shouldRunCache.put(description, value);
			if (!value) {
				filteredOut++;
			}
			return value;
		} catch (IllegalArgumentException iae) {
			LOG.warn("{} has incomplete Annotation(s):", description, iae);
			return false;
		}
	}

	/**
	 * Should be ignored.
	 *
	 * @param description
	 *            the description
	 * @return true, if successful
	 */
	public boolean shouldBeIgnored(final Description description) {
		if (Environment.SMOKE_TEST_ENABLED) {
			return !isSmokeTest(description);
		}
		return isSkipTestOn(description) || !isRunTestOn(description) || isBroken(description)
				|| isIntegrationTestIgnored(description);
	}

	private boolean isIntegrationTestIgnored(Description description) {
		return !Environment.INTEGRATION_TEST_ENABLED && isIntegrationTest(description);
	}

	/**
	 * Returns true if the given framework methods (encapsulated in the
	 * description) should be hidden if it is not executed.
	 *
	 * @param description
	 *            the description
	 * @return true, if successful
	 */
	public boolean shouldBeHidden(final Description description) {
		try {
			if (shouldBeIgnored(description)) {
				SkipTestOn skipOn = getAnnotation(description, SkipTestOn.class);
				if (skipOn != null) {
					return skipOn.hide();
				}
				RunTestOn runOn = getAnnotation(description, RunTestOn.class);
				if (runOn != null) {
					return runOn.hide();
				}
				Broken broken = getAnnotation(description, Broken.class);
				if (broken != null) {
					return broken.hide();
				}
			}
		} catch (IllegalArgumentException ignored) {
			LOG.debug("Evaluation of {} is ignored:", description, ignored);
		}
		return false;
	}

	private boolean isIntegrationTest(final Description description) {
		IntegrationTest it = getAnnotation(description, IntegrationTest.class);
		if (it != null) {
			if (LOG.isInfoEnabled()) {
				LOG.info("{} SKIPPED because {}.", description.getDisplayName(), it.value());
			}
			return true;
		}
		return false;
	}

	private boolean isSmokeTest(final Description description) {
		SmokeTest smokeTest = description.getAnnotation(SmokeTest.class);
		if (smokeTest != null) {
			if (LOG.isInfoEnabled()) {
				LOG.info("Run {} because {}.", description.getDisplayName(), smokeTest.value());
			}
			return true;
		}
		return false;
	}

	/**
	 * Returns true if the given method has the RunTestOn annotation and the
	 * condition of it is true.
	 *
	 * @param description
	 *            with the FrameworkMethod
	 * @return depends on the condition
	 */
	private static boolean isRunTestOn(final Description description) {
		RunTestOn runOn = getAnnotation(description, RunTestOn.class);
		if (runOn == null) {
			return true;
		}
		TestOn testOn = new TestOn();
		testOn.setOsNames(runOn.value(), runOn.osName());
		testOn.setOsArchs(runOn.osArch());
		testOn.setOsVersions(runOn.osVersion());
		testOn.setHosts(runOn.host());
		testOn.setJavaVersions(runOn.javaVersion());
		testOn.setJavaVendors(runOn.javaVendor());
		testOn.setUsers(runOn.user());
		testOn.setSystemProps(runOn.property());
		testOn.setDays(runOn.day());
		testOn.setTimes(runOn.time());
		if (!testOn.isValueGiven()) {
			throw new IllegalArgumentException(description + ":  @RunTestOn has no value");
		}
		if (testOn.matches()) {
			LOG.info("{} executed {}.", description, testOn.getReason());
			return true;
		}
		return false;
	}

	/**
	 * Returns true if the given method has the SkipTestOn annotation and the
	 * condition of it is true.
	 *
	 * @param description
	 *            with the FrameworkMethod
	 * @return depends on the condition
	 */
	private static boolean isSkipTestOn(final Description description) {
		SkipTestOn skipOn = getAnnotation(description, SkipTestOn.class);
		if (skipOn == null) {
			return false;
		}
		TestOn testOn = new TestOn();
		testOn.setOsNames(skipOn.value(), skipOn.osName());
		testOn.setOsArchs(skipOn.osArch());
		testOn.setOsVersions(skipOn.osVersion());
		testOn.setHosts(skipOn.host());
		testOn.setJavaVersions(skipOn.javaVersion());
		testOn.setJavaVendors(skipOn.javaVendor());
		testOn.setUsers(skipOn.user());
		testOn.setSystemProps(skipOn.property());
		testOn.setDays(skipOn.day());
		testOn.setTimes(skipOn.time());
		if (!testOn.isValueGiven()) {
			throw new IllegalArgumentException(description + ":  @SkipTestOn has no value");
		}
		if (testOn.matches()) {
			LOG.info("{} SKIPPED because {}.", description, testOn.getReason());
			return true;
		}
		return false;
	}

	private boolean isBroken(final Description description) {
		Broken broken = getAnnotation(description, Broken.class);
		return broken != null && isBroken(DescriptionUtils.getMethodNameOf(description), broken);
	}

	@MayReturnNull
	private static <T extends Annotation> T getAnnotation(final Description description,
			final Class<T> annotationType) {
		T annotation = description.getAnnotation(annotationType);
		if (annotation != null) {
			return annotation;
		}
		Class<?> testClass = DescriptionUtils.getTestClassOf(description);
		if (testClass == null) {
			LOG.debug("No test class extracted from {}.", description);
			return null;
		} else {
			return testClass.getAnnotation(annotationType);
		}
	}

	/**
	 * Checks for a given method if the condition of the given Broken
	 * annotations is fulfilled.
	 * <p>
	 * NOTE: The method has default visibility
	 * </p>
	 *
	 * @param method
	 *            the method
	 * @param broken
	 *            the broken
	 * @return true, if is broken
	 */
	public boolean isBroken(final String method, final Broken broken) {
		String why = broken.why();
		if (StringUtils.isEmpty(why)) {
			why = broken.value();
		}
		Date till = Converter.toDate(broken.till());
		if (!NullConstants.NULL_DATE.equals(till) && till.after(today)) {
			LOG.info(method + "() SKIPPED till {} because {}.", broken.till(), why);
			return true;
		}
		TestOn testOn = new TestOn(env);
		testOn.setOsNames(broken.osName());
		testOn.setOsArchs(broken.osArch());
		testOn.setOsVersions(broken.osVersion());
		testOn.setHosts(broken.host());
		testOn.setJavaVersions(broken.javaVersion());
		testOn.setJavaVendors(broken.javaVendor());
		testOn.setUsers(broken.user());
		testOn.setSystemProps(broken.property());
		if (testOn.matches()) {
			if (NullConstants.NULL_DATE.equals(till)) {
				if (testOn.hasReason()) {
					LOG.info("{}() SKIPPED because {}.", method, testOn.getReason());
				}
				return true;
			} else {
				LOG.debug("{}() started, because it should be fixed since {}.", method, broken.till());
				return false;
			}
		}
		return false;
	}

	/**
	 * Checks if there are some methods filtered out.
	 *
	 * @return true if some methods were filtered
	 */
	public boolean hasFiltered() {
		return this.filteredOut > 0;
	}

	/**
	 * Gets the number of methods which was filtered out.
	 *
	 * @return the filtered number
	 */
	public int getFilteredNumber() {
		return this.filteredOut;
	}

	/**
	 * Prints the description of this filter.
	 *
	 * @return a String including the result of the describe() method
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "filter for " + this.describe();
	}

}
