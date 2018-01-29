/**
 * $Id: Synchronized.java,v 1.6 2017/02/05 18:42:36 oboehm Exp $
 *
 * Copyright (c) 2008 by Oliver Boehm
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express orimplied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * (c)reated 13.11.2008 by oliver (ob@oasd.de)
 */
package patterntesting.annotation.concurrent;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * Instead of synchronized methods you can mark these methods as
 * "@Synchronized". The SynchronizedAspect in patterntesting.concurrent will use
 * ReentrantLock to realize it and to avoid deadlocks. If you set the log level
 * to TRACE you can get additional information about the locks and waiting
 * threads.
 * <p>
 * The default value for the timeout is 1800 seconds (30 minutes). If you want a
 * shorter value you can use the timeout and unit attributes. Example:
 * <code>@Synchronized(timeout=10, unit=TimeUnit.SECONDS)</code> will set the
 * timeout to 10 seconds.
 * </p>
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @see TimeUnit
 * @since 13.11.2008
 */
@Documented
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Synchronized {

    /**
     * The default timeout is 30 minutes (1800 seconds).
     *
     * @return the long
     */
    long timeout() default 1800L;

    /**
     * The unit of time (with TimeUnit.SECONDS as default).
     *
     * @return the time unit
     */
    TimeUnit unit() default TimeUnit.SECONDS;

}
