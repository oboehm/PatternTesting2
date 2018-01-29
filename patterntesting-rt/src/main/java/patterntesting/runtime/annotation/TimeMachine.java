/*
 * $Id: TimeMachine.java,v 1.3 2016/12/10 20:55:18 oboehm Exp $
 *
 * Copyright (c) 2015 by Oliver Boehm
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
 * (c)reated 06.09.2015 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.annotation;

import static patterntesting.runtime.NullConstants.NULL_STRING;

import java.lang.annotation.*;

/**
 * For time specific tests you can use this annotation to set the todays date.
 *
 * @author oliver
 * @version $Revision: 1.3 $
 * @since 1.6 (06.09.2015)
 */
@Documented
@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR })
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeMachine {

	/**
	 * You need a specific day for your unit test? Use this annotation together
	 * with the TimeMachineAspect, to set it. Each call of the normal Date
	 * constructur will create a Date with this day as the current date.
	 * <p>
	 * The format of the date is "dd-MMM-yyyy" or "dd-MMM-yyyy H:mm".
	 * </p>
	 * <p>
	 * NOTE: For a successful work of the TimeMachineAspect ony methods which
	 * are not called via reflexion work as expected. Because JUnit tests are
	 * called via reflexion extract your test method in an extra method
	 * "subTestMethod" and mark this "subTestMethod" with "@TimeMachine".
	 * </p>
	 *
	 * @return the string
	 */
	String today() default NULL_STRING;

}
