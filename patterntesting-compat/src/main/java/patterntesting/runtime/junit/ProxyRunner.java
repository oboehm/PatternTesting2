/*
 * $Id: ProxyRunner.java,v 1.9 2016/12/18 20:19:38 oboehm Exp $
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
 * (c)reated 11.11.2011 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.*;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import patterntesting.runtime.annotation.DelegateTo;
import patterntesting.runtime.junit.internal.DescriptionUtils;
import patterntesting.runtime.junit.internal.ModelInitializationError;
import patterntesting.runtime.util.ReflectionHelper;

/**
 * This is a JUnit runner which delegates the call to one (or perhaps several)
 * other JUnit runners.
 *
 * @author oliver (ob@aosd.de)
 * @since 1.2 (11.11.2011)
 */
public class ProxyRunner extends SmokeRunner {

	private static final Logger LOG = LoggerFactory.getLogger(ProxyRunner.class);
	private ParentRunner<FrameworkMethod> delegateRunner;

	/**
	 * Instantiates a new proxy runner.
	 *
	 * @param testClass
	 *            the test class
	 * @throws InitializationError
	 *             the initialization error
	 */
	public ProxyRunner(final Class<?> testClass) throws InitializationError {
		super(testClass);
	}

	/**
	 * Gets the Runner defined by <code>&#064;DelegateTo</code>.
	 * <p>
	 * Note: This class is public for testing reasons.
	 * </p>
	 *
	 * @return the delegate runner
	 */
	public final ParentRunner<FrameworkMethod> getDelegateRunner() {
		if (this.delegateRunner == null) {
			try {
				this.delegateRunner = createDelegateRunner();
			} catch (InitializationError ie) {
				throw new IllegalStateException("cannot create delegate runner", ie);
			}
		}
		return this.delegateRunner;
	}

	private ParentRunner<FrameworkMethod> createDelegateRunner() throws InitializationError {
		Class<? extends ParentRunner<FrameworkMethod>> runnerClass = getDelegateRunnerClass();
		try {
			Constructor<? extends ParentRunner<FrameworkMethod>> ctor = runnerClass.getDeclaredConstructor(Class.class);
			return ctor.newInstance(this.getTestClass().getJavaClass());
		} catch (SecurityException e) {
			throw new ModelInitializationError(e);
		} catch (ReflectiveOperationException e) {
			throw new ModelInitializationError(e);
		} catch (IllegalArgumentException e) {
			throw new ModelInitializationError(e);
		}
	}

	private Class<? extends ParentRunner<FrameworkMethod>> getDelegateRunnerClass() {
		DelegateTo delegateTo = this.getTestClass().getJavaClass().getAnnotation(DelegateTo.class);
		return delegateTo.value();
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 * @see org.junit.runner.Runner#getDescription()
	 */
	@Override
	public Description getDescription() {
		Description description = this.getDelegateRunner().getDescription();
		Collection<Annotation> annotationList = description.getAnnotations();
		Annotation[] annotations = annotationList.toArray(new Annotation[annotationList.size()]);
		Description filtered = DescriptionUtils.createTestDescription(description, annotations);
		for (Description descr : description.getChildren()) {
			if (this.getFilter().shouldBeHidden(descr)) {
				LOG.trace("{} is hidden.", descr);
			} else {
				filtered.addChild(descr);
			}
		}
		return filtered;
	}

	/**
	 * Gets the test methods.
	 *
	 * @return the test methods
	 * @see patterntesting.runtime.junit.SmokeRunner#getTestMethods()
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected List<FrameworkMethod> getTestMethods() {
		return (List<FrameworkMethod>) ReflectionHelper.invokeMethod(this.getDelegateRunner(), "getChildren");
	}

	/**
	 * Run child. This method may be not called (e.g. it is not called by
	 * SpringJUnit4ClassRunner) but must be implemented because of the abstract
	 * superclass (ParentRunner).
	 *
	 * @param method
	 *            the method
	 * @param notifier
	 *            the notifier
	 * @see org.junit.runners.ParentRunner#runChild(java.lang.Object,
	 *      org.junit.runner.notification.RunNotifier)
	 */
	@Override
	protected void runChild(final FrameworkMethod method, final RunNotifier notifier) {
		Description description = describeChild(method);
		try {
			if (shouldBeIgnored(method)) {
				notifier.fireTestIgnored(description);
				return;
			}
		} catch (IllegalArgumentException iae) {
			fireTestAssumptionFailed(notifier, description, iae);
			notifier.fireTestFinished(description);
			return;
		}
		long startTime = System.currentTimeMillis();
		ReflectionHelper.invokeMethod(delegateRunner, "runChild", method, notifier);
		logMethod(method, System.currentTimeMillis() - startTime);
	}

	private static void logMethod(final FrameworkMethod method, final long time) {
		LOG.info("{}.{} (" + time + " ms)", method.getMethod().getDeclaringClass().getName(), method.getName());
	}

}
