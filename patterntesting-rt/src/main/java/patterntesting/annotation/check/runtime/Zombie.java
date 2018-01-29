/*
 * $Id: Zombie.java,v 1.4 2016/12/31 15:04:58 oboehm Exp $
 *
 * Copyright (c) 2015-2017 by Oliver Boehm
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
 * (c)reated 11.01.2015 by oliver (ob@oasd.de)
 */

package patterntesting.annotation.check.runtime;

import java.lang.annotation.*;

/**
 * With this annotation you can mark your class or method as "dead" if you want
 * to remove it and you are not sure if they really is dead. Together with
 * PatternTesting Check-RT you will be informed in the log file if your class or
 * method (or whatever you've marked as "dead") is called.
 *
 * @author oliver
 * @version $Revision: 1.4 $
 * @since 1.6 (11.01.2015)
 */
@Documented
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Zombie {

    /**
     * You can change the default string to give a reason why the class or
     * method is marked as "Zombie".
     *
     * @return the string
     */
    String value() default "marked as @Zombie";

}
