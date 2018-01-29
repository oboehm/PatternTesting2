/**
 * $Id: MayBeNull.java,v 1.5 2016/12/31 15:04:58 oboehm Exp $
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
 * (c)reated 15.06.2009 by oliver (ob@aosd.de)
 */
package patterntesting.annotation.check.runtime;

import java.lang.annotation.*;

/**
 * If one of an argument of a method can be null you can use this annotation to
 * mark it.
 * <p>
 * You can also mark fields with this annotation but this is at the moment not
 * evaluated. But you can use it for documentation purposes.
 * </p>
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.5 $
 * @since 15.06.2009
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
public @interface MayBeNull {

}
