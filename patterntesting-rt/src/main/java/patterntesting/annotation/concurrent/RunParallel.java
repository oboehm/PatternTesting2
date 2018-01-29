/*
 * $Id: RunParallel.java,v 1.6 2017/02/05 18:42:36 oboehm Exp $
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
 * (c)reated 13.01.2009 by oliver (ob@oasd.de)
 */
package patterntesting.annotation.concurrent;

import java.lang.annotation.*;

/**
 * Mark methods or constructors which should be run parallel in different
 * threads with this annotation. This can be useful e.g. for testing.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.6 $
 * @since 13.01.2009
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.CONSTRUCTOR, ElementType.METHOD })
public @interface RunParallel {

    /**
     * Number of parallel runs.
     *
     * @return the int
     */
    int value() default 2;
}
