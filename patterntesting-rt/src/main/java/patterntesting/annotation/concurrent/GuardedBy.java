/**
 * $Id: GuardedBy.java,v 1.5 2017/02/05 18:42:36 oboehm Exp $
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
 * (c)reated 16.01.2009 by oliver (ob@oasd.de)
 */
package patterntesting.annotation.concurrent;

import java.lang.annotation.*;

/**
 * With this annotation you can document the lock which guards the field or
 * method. This annotation is only for documentation - there is no aspect
 * available which uses it.
 * <p>
 * See also <a href=
 * "http://www.jcip.net/annotations/doc/net/jcip/annotations/GuardedBy.html">
 * GuardedBy</a> from jcip.net.
 * </p>
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.5 $
 * @since 16.01.2009
 */
@Documented
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface GuardedBy {

    /**
     * use the object (e.g. "myLock") which guardes the lock
     *
     * @return the string
     */
    String value() default "this";
}
