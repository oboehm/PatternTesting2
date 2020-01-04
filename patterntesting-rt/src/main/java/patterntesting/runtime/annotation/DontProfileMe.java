/*
 * Copyright (c) 2008-2020 by Oliver Boehm
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
 * You have marked a class with "@ProfileMe" but don't want to see all methods?
 * Just mark these methods or constructors with "@DontProfileMe".
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @see ProfileMe
 * @see patterntesting.runtime.monitor.ProfileStatistic
 * @see patterntesting.runtime.monitor.ProfileStatistic
 * @see patterntesting.runtime.monitor.ProfileMonitor
 * @since 0.9
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.CONSTRUCTOR, ElementType.METHOD })
public @interface DontProfileMe {

}
