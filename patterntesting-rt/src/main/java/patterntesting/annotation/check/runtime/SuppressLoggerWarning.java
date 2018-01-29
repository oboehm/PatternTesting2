/*
 * $Id: SuppressLoggerWarning.java,v 1.4 2016/12/31 15:04:58 oboehm Exp $
 *
 * Copyright (c) 2014-2017 by Oliver Boehm
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
 * (c)reated 06.02.2014 by oliver (ob@oasd.de)
 */

package patterntesting.annotation.check.runtime;

import java.lang.annotation.*;

/**
 * Use this annotation to suppress the warnings of the LoggerAspect in
 * "patterntesting.check.runtime".
 *
 * @author oliver
 * @since 1.4.1 (06.02.2014)
 */
@Documented
@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface SuppressLoggerWarning {

}
