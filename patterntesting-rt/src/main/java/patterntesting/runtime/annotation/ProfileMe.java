/**
 * $Id: ProfileMe.java,v 1.5 2016/12/10 20:55:18 oboehm Exp $
 *
 * Copyright (c) 2008 by Oliver Boehm
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
 * (c)reated 19.12.2008 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.annotation;

import java.lang.annotation.*;

/**
 * You want to measure a method or constructor? Mark it with @ProfileMe. You
 * want to measure all methods or constructors in a class? Mark the class
 * with @ProfileMe.
 * <p>
 * You want to find constructors or methods of a class which are never called?
 * Mark the class with @ProfileMe and look at the constructors and methods which
 * has a hit of "0". This feature works only if you mark the whole class
 * with @ProfileMe because in this case the ProfileStatisticMBean initializes
 * the ProfileMonitor for each method in this class.
 * </p>
 * <p>
 * For all other methods or constructors the attached ProfileMonitor is
 * initialized at the first call. So you don't see the ProfileMonitor if the
 * method or constructor is never called.
 * </p>
 * <p>
 * <em>Note</em>: This works only for classes which are really loaded. If a
 * class is loaded you can see it with the ClasspathMonitorMBean.
 * </p>
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.5 $
 * @see DontProfileMe
 * @see patterntesting.runtime.monitor.ProfileStatistic
 * @see patterntesting.runtime.monitor.ProfileStatistic
 * @see patterntesting.runtime.monitor.ProfileMonitor
 * @see patterntesting.runtime.monitor.ClasspathMonitor
 * @see patterntesting.runtime.monitor.ClasspathMonitorMBean
 * @since 19.12.2008
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
public @interface ProfileMe {

}
