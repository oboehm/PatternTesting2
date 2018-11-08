/*
 * $Id: ProfiledStatement.java,v 1.12 2016/12/18 20:19:41 oboehm Exp $
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

package patterntesting.runtime.junit.internal;

import junit.framework.TestCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Test.None;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;
import patterntesting.annotation.check.runtime.MayReturnNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * The ProfiledStatement measures also the time the different setUp() and
 * tearDown() methods need. In contradiction to the original JUnit statement
 * this statement is not only a wrapper around the test method but contains also
 * the setUp and tearDown methods. And it is able to handle JUnit4 <b>and</b>
 * JUnit3 methods.
 *
 * @author oliver
 * @since 1.0 (29.03.2010)
 */
public class ProfiledStatement extends Statement {

	private static final Logger LOG = LogManager.getLogger(ProfiledStatement.class);
	private final TestClass testClass;
	private final FrameworkMethod frameworkMethod;
	private long startTime;
	private long startTimeTest;
	private long startTimeAfters;
	private long endTime;

	/**
	 * The default constructor for this class if the call of the frameworkMethod
	 * is ok.
	 *
	 * @param testClass
	 *            the test class
	 * @param frameworkMethod
	 *            the FrameworkMethod
	 */
	public ProfiledStatement(final TestClass testClass, final FrameworkMethod frameworkMethod) {
		this.testClass = testClass;
		this.frameworkMethod = frameworkMethod;
	}

	/**
	 * May be needed by some subclasses.
	 *
	 * @return the method name
	 */
	protected final String getMethodName() {
		return this.frameworkMethod.getName();
	}

	/**
	 * Invokes the test method.
	 *
	 * @throws Throwable
	 *             the throwable
	 * @see org.junit.runners.model.Statement#evaluate()
	 */
	@Override
	public void evaluate() throws Throwable {
		Object target = testClass.getOnlyConstructor().newInstance();
		try {
			this.startTimer();
			invokeBefores(target);
			this.startTestTimer();
			invokeTest(target);
			this.startAftersTimer();
			invokeAfters(target);
		} catch (InvocationTargetException ite) {
			Throwable targetException = ite.getTargetException();
			if (targetException == null) {
				throw ite;
			}
			throw targetException;
		} finally {
			if (this.startTimeTest == 0) {
				this.startTestTimer();
			}
			if (this.startTimeAfters == 0) {
				this.startAftersTimer();
			}
			this.endTimer();
		}
	}

	/**
	 * We do not only call the test here but check if an excpected exception
	 * appears or not.
	 *
	 * @param target
	 *            the test target
	 * @throws Throwable
	 *             thrown by the test method
	 */
	private void invokeTest(final Object target) throws Throwable {
		Class<? extends Throwable> expected = getExpectedException();
		try {
			frameworkMethod.invokeExplosively(target);
			if ((expected != null) && (expected != None.class)) {
				throw new AssertionError("Expected exception: " + expected.getName());
			}
		} catch (Throwable t) { // NOSONAR
			if ((expected != null) && (expected != None.class) && (expected.isAssignableFrom(t.getClass()))) {
				LOG.debug("Expected exception appears:", t);
				return;
			}
			throw t;
		}
	}

	@MayReturnNull
	private Class<? extends Throwable> getExpectedException() {
		Test test = frameworkMethod.getAnnotation(Test.class);
		if (test == null) {
			return None.class;
		}
		return test.expected();
	}

	/**
	 * Here we invoke all setUp() methods.
	 *
	 * @param target
	 *            the instantiated JUnit test
	 * @throws Throwable
	 *             if setUp method has a problem
	 */
	private void invokeBefores(final Object target) throws Throwable {
		if (ProfiledStatement.isTestCaseClass(testClass)) {
			invoke("setUp", target);
		} else {
			List<FrameworkMethod> befores = testClass.getAnnotatedMethods(Before.class);
			invoke(befores, target);
		}
	}

	/**
	 * Here we invoke all tearDown() methods.
	 *
	 * @param target
	 *            the recorded statement
	 * @throws Throwable
	 *             if tearDown method has a problem
	 */
	private void invokeAfters(final Object target) throws Throwable {
		if (ProfiledStatement.isTestCaseClass(testClass)) {
			invoke("tearDown", target);
		} else {
			List<FrameworkMethod> afters = testClass.getAnnotatedMethods(After.class);
			invoke(afters, target);
		}
	}

	private void invoke(final String methodName, final Object target) throws Throwable {
		FrameworkMethod method = JUnitHelper.getFrameworkMethod(testClass.getJavaClass(), methodName);
		if (method != null) {
			invoke(method, target);
		}
	}

	private void invoke(final FrameworkMethod fwkMethod, final Object target) throws Throwable {
		try {
			fwkMethod.invokeExplosively(target);
		} catch (IllegalAccessException e) {
			LOG.debug("Cannot access " + fwkMethod + ":", e);
			invokeProtected(fwkMethod, target);
		} catch (InvocationTargetException e) {
			throw getThrowableFor(fwkMethod, e);
		}
	}

	private void invokeProtected(final FrameworkMethod fwkMethod, final Object target) throws Throwable {
		Method method = fwkMethod.getMethod();
		method.setAccessible(true);
		try {
			method.invoke(target);
		} catch (IllegalAccessException e) {
			throw getAssertionErrorFor(fwkMethod, e);
		} catch (InvocationTargetException e) {
			throw getThrowableFor(fwkMethod, e);
		}
	}

	private void invoke(final List<FrameworkMethod> fwkMethods, final Object target) throws Throwable {
		for (FrameworkMethod fwkMethod : fwkMethods) {
			invoke(fwkMethod, target);
		}
	}

	private AssertionError getAssertionErrorFor(final FrameworkMethod method, final Throwable t) {
		String detailedMessage = "invoke of " + method.getName() + "() failed\n" + t;
		return new AssertionError(detailedMessage);
	}

	private Throwable getThrowableFor(final FrameworkMethod fwkMethod, final InvocationTargetException e)
			throws AssertionError {
		Throwable t = e.getTargetException();
		if (t == null) {
			throw getAssertionErrorFor(fwkMethod, e);
		}
		return t;
	}

	/**
	 * Here we start the timer when the first setUp() method was called.
	 */
	public final void startTimer() {
		this.startTime = System.currentTimeMillis();
	}

	/**
	 * Here we start the timer when the test method was called.
	 */
	public final void startTestTimer() {
		this.startTimeTest = System.currentTimeMillis();
	}

	/**
	 * Here we start the timer when the first tearDown() method was called.
	 */
	public final void startAftersTimer() {
		this.startTimeAfters = System.currentTimeMillis();
	}

	/**
	 * The timer should end after the last tearDown() method was called.
	 */
	public final void endTimer() {
		this.endTime = System.currentTimeMillis();
	}

	/**
	 * Returns the name of the method with the measured times.
	 *
	 * @return the framework method with the measured times
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.testClass.getName() + "." + this.frameworkMethod.getName() + " ("
				+ (this.startTimeTest - this.startTime) + "+" + (this.startTimeAfters - this.startTimeTest) + "+"
				+ (this.endTime - this.startTimeAfters) + " ms)";
	}

	/**
	 * Checks if is test case class.
	 *
	 * @param testClass
	 *            the test class
	 * @return true if testClass is derived from TestCase
	 */
	public static boolean isTestCaseClass(final TestClass testClass) {
		return TestCase.class.isAssignableFrom(testClass.getJavaClass());
	}

}
