/*
 * $Id: DontTraceMe.java,v 1.4 2016/12/10 20:55:18 oboehm Exp $
 *
 * Copyright (c) 2010 by Oliver Boehm
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
 * (c)reated 30.09.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.annotation;

import java.lang.annotation.*;

/**
 * If you have marked a class with <code>@TraceMe</code> and you want to exclude
 * some methods or constructors you can mark it with this annotation.
 *
 * @author oliver
 * @since 1.0.3 (30.09.2010)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD })
public @interface DontTraceMe {

}
