/*
 * $Id: SmokeRunner.java,v 1.44 2016/12/18 20:19:38 oboehm Exp $
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
 * (c)reated 29.03.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.*;
import org.junit.*;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.MultipleFailureException;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.*;

import patterntesting.runtime.annotation.RunTestOn;
import patterntesting.runtime.annotation.SkipTestOn;
import patterntesting.runtime.junit.internal.ProfiledStatement;
import patterntesting.runtime.junit.internal.SmokeFilter;

/**
 * This is the eXtended Runner for JUnit 4 which handles the SmokeTest and other
 * annotations. In previous (intermediate) version it was called "XRunner". But
 * because this may be a little bit confusing name it was renamed to
 * "SmokeRunner" for the final version.
 * <p>
 * It can be used together with the {@code @RunWith} annotation of JUnit 4.
 * </p>
 *
 * @author oliver
 * @since 1.0 (29.03.2010)
 */
@SuppressWarnings("deprecation")
public class SmokeRunner extends ParentRunner<FrameworkMethod> {

	private static final Logger LOG = LoggerFactory.getLogger(SmokeRunner.class);
	private final SmokeFilter xfilter = new SmokeFilter();

	/**
	 * Creates a SmokeRunner to run klass methods.
	 *
	 * @param klass
	 *            the test class to run
	 * @throws InitializationError
	 *             if the test class is malformed
	 */
	public SmokeRunner(final Class<?> klass) throws InitializationError {
		super(klass);
	}

	/**
	 * The ParentRunner allows us no access to the filter. So we added this
	 * method here (e.g. needed by the ParallelRunner).
	 *
	 * @return the SmokeFilter
	 */
	protected final SmokeFilter getFilter() {
		return this.xfilter;
	}

	/**
	 * Returns the methods that run tests. Default implementation returns all
	 * methods annotated with {@code @Test} on this class and superclasses that
	 * are not overridden.
	 * <p>
	 * Since 1.5 there was a hide flag for the {@link RunTestOn} and
	 * {@link SkipTestOn} introduced where the unit test should not be appear in
	 * the list of the ignored tests if it will not be executed. So we filter
	 * out these tests here.
	 * </p>
	 *
	 * @return the methods that run tests
	 */
	@Override
	protected List<FrameworkMethod> getChildren() {
		Description suiteDescription = Description.createSuiteDescription(getName(), getRunnerAnnotations());
		if (xfilter.shouldBeHidden(suiteDescription)) {
			Collection<Annotation> annotations = suiteDescription.getAnnotations();
			for (Annotation a : annotations) {
				if (a.toString().contains("hide=true")) {
					throw new UnsupportedOperationException("@" + a.annotationType().getSimpleName()
							+ "(hide=true) is only supported in method annotations of " + suiteDescription);
				}
			}
			throw new UnsupportedOperationException(
					"flag 'hide=true' is only supported in method annotations of " + suiteDescription);
		}
		List<FrameworkMethod> testMethods = getTestMethods();
		return filtered(testMethods);
	}

	private List<FrameworkMethod> filtered(final List<FrameworkMethod> testMethods) {
		List<FrameworkMethod> nonHiddenMethods = new ArrayList<>();
		for (FrameworkMethod method : testMethods) {
			if (xfilter.shouldBeHidden(describeChild(method))) {
				LOG.trace("{} is hidden.", method);
			} else {
				nonHiddenMethods.add(method);
			}
		}
		return nonHiddenMethods;
	}

	/**
	 * Gets the test methods. This method can be overriden by subclasses (like
	 * the ProxyRunner) which also want to filter out test methods with
	 * "hide=true".
	 *
	 * @return the test methods
	 */
	protected List<FrameworkMethod> getTestMethods() {
		TestClass testClass = this.getTestClass();
		Class<?> javaClass = testClass.getJavaClass();
		if (ProfiledStatement.isTestCaseClass(testClass)) {
			return getJUnit3TestMethods(javaClass);
		}
		return testClass.getAnnotatedMethods(Test.class);
	}

	/**
	 * Create a <code>Description</code> of a single test named
	 * <code>name</code> in the class <code>clazz</code>. Generally, this will
	 * be a leaf <code>Description</code>. (see also
	 * {@link BlockJUnit4ClassRunner})
	 *
	 * @param child
	 *            the name of the test (a method name for test annotated with
	 *            {@link org.junit.Test})
	 * @return the description
	 * @see org.junit.runners.ParentRunner#describeChild(java.lang.Object)
	 */
	@Override
	protected Description describeChild(final FrameworkMethod child) {
		return Description.createTestDescription(getTestClass().getJavaClass(), child.getName(),
				child.getAnnotations());
	}

	/**
	 * Checks the annotation of the method marked as "@BeforeClass" and add (or
	 * filters out) the beforeClass method (needed to solve.
	 *
	 * @param statement
	 *            the statement(s) before
	 * @return the statement(s) after with the BeforeClass method
	 * @see org.junit.runners.ParentRunner#withBeforeClasses(org.junit.runners.model.Statement)
	 */
	@Override
	protected Statement withBeforeClasses(final Statement statement) {
		List<FrameworkMethod> filtered = filter(BeforeClass.class);
		if (filtered.isEmpty()) {
			return statement;
		}
		return new RunBefores(statement, filtered, null);
	}

	/**
	 * Checks the annotation of the method marked as "@AfterClass" and add (or
	 * filters out) the afterClass method (needed to solve.
	 *
	 * @param statement
	 *            the statement(s) before
	 * @return the statement(s) after with the AfterClass method
	 */
	@Override
	protected Statement withAfterClasses(final Statement statement) {
		List<FrameworkMethod> filtered = filter(AfterClass.class);
		if (filtered.isEmpty()) {
			return statement;
		}
		return new RunAfters(statement, filtered, null);
	}

	private List<FrameworkMethod> filter(final Class<? extends Annotation> annotationClass) {
		List<FrameworkMethod> methods = this.getTestClass().getAnnotatedMethods(annotationClass);
		return filter(methods);
	}

	private List<FrameworkMethod> filter(final List<FrameworkMethod> methods) {
		List<FrameworkMethod> filtered = new ArrayList<>();
		for (FrameworkMethod fm : methods) {
			if (!this.xfilter.shouldBeIgnored(describeChild(fm))) {
				filtered.add(fm);
			} else if (LOG.isTraceEnabled()) {
				LOG.trace(fm.getMethod() + " is filtered out");
			}
		}
		return filtered;
	}

	/**
	 * Runs the test corresponding to {@code child}, which can be assumed to be
	 * an element of the list returned by {@link ParentRunner#getChildren()}.
	 * Subclasses are responsible for making sure that relevant test events are
	 * reported through {@code notifier}
	 *
	 * @param method
	 *            the method
	 * @param notifier
	 *            the notifier
	 */
	@Override
	@SuppressWarnings("all")
	protected void runChild(final FrameworkMethod method, final RunNotifier notifier) {
		Description description = describeChild(method);
		try {
			if (shouldBeIgnored(method)) {
				notifier.fireTestIgnored(description);
				return;
			}
		} catch (IllegalArgumentException iae) {
			notifier.fireTestStarted(description);
			fireTestAssumptionFailed(notifier, description, iae);
			notifier.fireTestFinished(description);
			return;
		}
		notifier.fireTestStarted(description);
		Statement stmt = methodBlock(method);
		try {
			stmt.evaluate();
		} catch (AssumptionViolatedException ex) {
			fireTestAssumptionFailed(notifier, description, ex);
		} catch (Throwable e) {
			addFailure(notifier, e, description);
		} finally {
			logStatement(stmt);
			notifier.fireTestFinished(description);
		}
	}

	/**
	 * In JUnit 4.5 and newer we can use the fireTestAssumptionFailed(..) of the
	 * {@link RunNotifier} class. But JUnit 4.4 does not provide this method.
	 * <p>
	 * We could map it to the {@link RunNotifier#fireTestFailure(Failure)}
	 * method - but this does not work for JUnit 4.5 (some internal JUnit tests
	 * will fail if you try that). We could compile with JUnit 4.5, run the
	 * tests with JUnit 4.4 and see what will happen. Perhaps we can catch the
	 * exception and call the {@link RunNotifier#fireTestFailure(Failure)}
	 * method. We could also give up because the architecture of JUnit has
	 * changed too much between 4.4 and 4.5 - this is, what we do now.
	 * </p>
	 *
	 * @param notifier
	 *            the notifier
	 * @param description
	 *            the description
	 * @param ex
	 *            the ex
	 * @since 1.2.20
	 */
	protected static void fireTestAssumptionFailed(final RunNotifier notifier, final Description description,
			final Exception ex) {
		notifier.fireTestAssumptionFailed(new Failure(description, ex));
	}

	/**
	 * We will give a subclass (like e.g. ParallelRunner) the chance to report
	 * the statement with its own logger.
	 *
	 * @param stmt
	 *            the stmt to be logged
	 */
	protected void logStatement(final Statement stmt) {
		LOG.info("{}", stmt);
	}

	/**
	 * Here we handle annotations like {@code @Ignore} where the execution of
	 * the test method should be ignored.
	 *
	 * @param method
	 *            the test method
	 * @return true or false
	 */
	protected final boolean shouldBeIgnored(final FrameworkMethod method) {
		Ignore ignore = method.getAnnotation(Ignore.class);
		if (ignore != null) {
			if (LOG.isDebugEnabled()) {
				String reason = ignore.value();
				if (StringUtils.isNotEmpty(reason)) {
					reason = " (" + reason + ")";
				}
				LOG.debug(this.getTestClass().getName() + "." + method.getName() + " ignored" + reason);
			}
			return true;
		}
		return this.xfilter.shouldBeIgnored(describeChild(method));
	}

	/**
	 * Should be run.
	 *
	 * @param method
	 *            the method
	 * @return true, if successful
	 */
	protected final boolean shouldBeRun(final FrameworkMethod method) {
		return !this.xfilter.shouldBeIgnored(describeChild(method));
	}

	/**
	 * This method was inspired from an internal JUnit class
	 * (EachTestNotifier#addFailure(Throwable)).
	 *
	 * @param notifier
	 *            the notifier
	 * @param targetException
	 *            the target exception
	 * @param description
	 *            the description
	 */
	protected final void addFailure(final RunNotifier notifier, final Throwable targetException,
			final Description description) {
		if (targetException instanceof MultipleFailureException) {
			MultipleFailureException mfe = (MultipleFailureException) targetException;
			for (Throwable each : mfe.getFailures()) {
				addFailure(notifier, each, description);
			}
			return;
		}
		notifier.fireTestFailure(new Failure(description, targetException));
	}

	/**
	 * Creates a RunStatement for the given test method.
	 *
	 * @param method
	 *            the test method
	 * @return a created RunStatement
	 */
	protected Statement methodBlock(final FrameworkMethod method) {
		return new ProfiledStatement(this.getTestClass(), method);
	}

	/**
	 * Here we look after public void methods with "test" as prefix and with no
	 * arguments.
	 * <p>
	 * NOTE: This method is public because it is also needed by
	 * patterntesting.concurrent.junit.JUnit3Executor
	 * </p>
	 *
	 * @param testClass
	 *            the test class
	 * @return a list of public methods starting with prefix "test"
	 */
	public static List<FrameworkMethod> getJUnit3TestMethods(final Class<?> testClass) {
		List<FrameworkMethod> children = new ArrayList<>();
		Method[] methods = testClass.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			if (isTestMethod(method)) {
				FrameworkMethod child = new FrameworkMethod(method);
				children.add(child);
			}
		}
		return children;
	}

	private static boolean isTestMethod(Method method) {
		int mod = method.getModifiers();
		if ((method.getParameterTypes().length > 0) || Modifier.isStatic(mod)) {
			return false;
		}
		Class<?> returnType = method.getReturnType();
		if (!"void".equalsIgnoreCase(returnType.toString())) {
			return false;
		}
		if (method.getName().startsWith("test")) {
			if (Modifier.isPublic(mod)) {
				return true;
			}
			LOG.warn(method + " isn't public");
		}
		return false;
	}

}
