/*
 * $Id: JUnitHelper.java,v 1.6 2016/12/18 20:19:41 oboehm Exp $
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

import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.runners.model.FrameworkMethod;

import patterntesting.annotation.check.runtime.MayReturnNull;

/**
 * This is a helper class which contains some static helper methods for JUnit.
 *
 * @author oliver
 * @since 1.0 (29.03.2010)
 */
public final class JUnitHelper {

	private static final Logger LOG = LogManager.getLogger(JUnitHelper.class);

	/** No need to instantiate it (utility class). */
	private JUnitHelper() {
	}

	/**
	 * Returns the given name as FrameworkMethod. This method is package visible
	 * because it use also by JUnit3Executor.
	 *
	 * @param testClass
	 *            the JUnit3 test class
	 * @param name
	 *            e.g. "setUp"
	 * @return null if name was not found
	 */
	@MayReturnNull
	public static FrameworkMethod getFrameworkMethod(final Class<?> testClass, final String name) {
		try {
			Method method = testClass.getDeclaredMethod(name);
			return new FrameworkMethod(method);
		} catch (NoSuchMethodException e) {
			LOG.debug("Method '" + name + "' not found in " + testClass + ":", e);
			return null;
		}
	}

}
