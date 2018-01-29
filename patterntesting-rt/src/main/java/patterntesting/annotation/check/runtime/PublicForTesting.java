/**
 * $Id: PublicForTesting.java,v 1.6 2016/12/31 15:04:58 oboehm Exp $
 *
 * Copyright (c) 2009-2017 by Oliver Boehm
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
 * (c)reated 18.03.2009 by oliver (ob@aosd.de)
 */
package patterntesting.annotation.check.runtime;

import java.lang.annotation.*;

/**
 * With this annotation you can mark methods which are public for testing but
 * which should *never* be called from outside, but only from test methods or
 * from the class itself.
 * <p>
 * This annotation is similar to @OnlyForTesting but it cannot be statically
 * checked. So you find it unter the check.runtime package.
 * </p>
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.6 $
 * @since 18.03.2009
 */
@Documented
@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR })
@Retention(RetentionPolicy.RUNTIME)
public @interface PublicForTesting {

}
