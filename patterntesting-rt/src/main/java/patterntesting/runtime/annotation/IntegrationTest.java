/*
 * $Id: IntegrationTest.java,v 1.5 2016/12/10 20:55:18 oboehm Exp $
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
 * (c)reated 05.03.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.annotation;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;

/**
 * This annotation allows you to mark classes which are not really a unit test
 * but a integration test. By default this classes are not executed by a normal
 * test run with JUnit. Only if you set the system property
 * {@link patterntesting.runtime.util.Environment#INTEGRATION_TEST} these tests
 * will executed.
 *
 * You can use this annotation if
 * <ul>
 * <li>your JUnit test takes too long because it is a integration test,</li>
 * <li>you must be online for a JUnit test,</li>
 * <li>your JUnit test needs a database access,</li>
 * <li>your JUnit tests is to slow,</li>
 * <li>other good reason why the test should not be executed each time.</li>
 * </ul>
 *
 * @author oliver
 * @since 1.0 (05.03.2010)
 */
@Documented
@Target({ ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Test
@Tag("integration")
public @interface IntegrationTest {

	/**
	 * You can give a reason why this test is an integration test or should be
	 * skipped, e.g. "needs online access". This reason is printed to the log.
	 *
	 * @return the string
	 */
	String value() default "this is marked as @IntegrationTest";

}
