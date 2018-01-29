/*
 * $Id: SuppressExceptionWarning.java,v 1.3 2016/12/10 20:55:19 oboehm Exp $
 *
 * Copyright (c) 2012 by Oliver Boehm
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
 * (c)reated 12.08.2012 by oliver (ob@oasd.de)
 */

package patterntesting.annotation.check.ct;

import java.lang.annotation.*;

/**
 * You can use this annotation to suppress warnings about declaring an
 * unspecified {@link Exception} or {@link Throwable} for methods or
 * constructors. Or warnings if you try to throw an unspecific
 * {@link Throwable}, {@link Exception} or {@link RuntimeException}.
 *
 * @author oliver (ob@aosd.de)
 * @version $Revision: 1.3 $
 * @since 1.3 (12.08.2012)
 */
@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface SuppressExceptionWarning {

}
