/**
 * $Id: LogThrowable.java,v 1.4 2016/12/10 20:55:18 oboehm Exp $
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
 * (c)reated 09.10.2008 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.annotation;

import java.lang.annotation.*;

import patterntesting.runtime.log.SimpleLog;

/**
 * You want to see an Exception or Error in the log if it happens? Use this
 * annotation to mark those classes you want to be logged.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.4 $
 * @since 09.10.2008
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD })
public @interface LogThrowable {

	/**
	 * Normally a RuntimeException will be logged with "WARN" as log level. If
	 * you want to change it you can do here.
	 *
	 * @return the int
	 * @see SimpleLog (for the different log levels)
	 */
	int value() default SimpleLog.LOG_LEVEL_WARN;

}
