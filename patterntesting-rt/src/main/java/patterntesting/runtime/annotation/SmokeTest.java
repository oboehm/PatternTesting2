/*
 * $Id: SmokeTest.java,v 1.6 2016/12/10 20:55:18 oboehm Exp $
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
 * (c)reated 02.03.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.annotation;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;

/**
 * For fast CI build it is helpful to mark important JUnit tests and test method
 * as SmokeTest. If the JUnit tests are started with the system property
 * "patterntesting.runSmokeTest" set then only these marked test classes and
 * methods will be executed. This will help you to start only the important
 * tests for a faster build.
 * <p>
 * For finer granularity a level could be set. This level can be set with the
 * same property, e.g. "-Dpatterntesting.runSmokeTest=5". Then only tests with
 * level 5 or less will be executed. The other tests with level 6 or higher will
 * be skipped.
 * </p>
 * <p>
 * <em>Note</em>: this feature is reserved for future use. If you would like it
 * raise a feature request.
 * </p>
 *
 * @author oliver
 * @since 1.0
 */
@Documented
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Test
@Tag("smoketest")
public @interface SmokeTest {

	/**
	 * You can change the default string to give a reason why the test will be
	 * executed in "SmokeTest mode".
	 *
	 * @return the string
	 */
	String value() default "marked as @SmokeTest";

	/**
	 * You are free to define your only levels here. Normally "1" is the highest
	 * level. You can replace "level" by "priority" if you want - it has the
	 * same meaning here.
	 *
	 * It is not forbidden to define a level "0" or a negative level.
	 *
	 * @return the int
	 */
	int level() default 1;

}
