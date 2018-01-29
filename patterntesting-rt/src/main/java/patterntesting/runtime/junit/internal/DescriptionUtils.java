/*
 * $Id: DescriptionUtils.java,v 1.8 2017/08/17 06:42:00 oboehm Exp $
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
 * (c)reated 19.04.2012 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit.internal;

import java.lang.annotation.Annotation;
import java.util.regex.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.*;
import org.junit.runner.Description;

import patterntesting.annotation.check.runtime.MayReturnNull;

/**
 * This class provides some utiltis for the Description class for older versions
 * of JUnit 4. In newer versions we have some more methods to use but because we
 * want to support also JUnit 4.4 we provide the missing methods here.
 * <p>
 * This class is abtract because it is a utility class and there is no need to
 * instantiate it.
 * </p>
 *
 * @author oliver
 * @since 1.2.20 (19.04.2012)
 */
public abstract class DescriptionUtils {

	private static final Logger LOG = LogManager.getLogger(DescriptionUtils.class);
	private static final Pattern METHOD_NAME_PATTERN = Pattern.compile("(.*)\\((.*)\\)");

	/** Utility class - no need to instantiate it. */
	private DescriptionUtils() {
	}

	/**
	 * Here we extract the test class from the given descripiton. In JUnit 4.4
	 * we don't have the Description or does not have a method getTestClass().
	 * We cannot provide our own "Description44" subclass of it because it is
	 * not designed for subclassing (no public constructor). Se we (re)implement
	 * the missing features for the SmokeFilter here.
	 *
	 * @param description
	 *            the description
	 * @return the test class from
	 * @since 1.2.20
	 */
	@MayReturnNull
	public static Class<?> getTestClassOf(final Description description) {
		String name = description.toString();
		Matcher matcher = Pattern.compile("(.*)\\((.*)\\)").matcher(name);
		if (matcher.matches()) {
			name = matcher.group(2);
		}
		if (name == null) {
			return null;
		}
		try {
		    if (name.endsWith("|")) {
		        name = StringUtils.substringBefore(name, "|");
		    }
			return Class.forName(name);
		} catch (ClassNotFoundException ex) {
			LOG.info("Cannot get test class from description \"{}\".", description);
			LOG.debug("Details:", ex);
			return null;
		}
	}

	/**
	 * Gets the method name the given description. This method is needed for
	 * JUnit 4.5 and older. In JUnit 4.6 and newer there is a getMethodName()
	 * available in the {@link Description} class.
	 *
	 * @param description
	 *            the description
	 * @return the method name of
	 */
	@MayReturnNull
	public static String getMethodNameOf(final Description description) {
		Matcher matcher = METHOD_NAME_PATTERN.matcher(description.getDisplayName());
		if (matcher.matches()) {
			return matcher.group(1);
		}
		return null;
	}

	/**
	 * Creates the test description.
	 *
	 * @param description
	 *            the description
	 * @param annotations
	 *            the annotations
	 * @return the description
	 */
	public static Description createTestDescription(final Description description, final Annotation[] annotations) {
		return Description.createTestDescription(getTestClassOf(description), description.getDisplayName(),
				annotations);
	}

}
