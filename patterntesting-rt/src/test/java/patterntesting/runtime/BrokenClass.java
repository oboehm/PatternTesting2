/*
 * $Id: BrokenClass.java,v 1.1 2010/01/05 13:26:17 oboehm Exp $
 *
 * Copyright (c) 2009 by Oliver Boehm
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
 * (c)reated 23.11.2009 by oliver (ob@oasd.de)
 */

package patterntesting.runtime;

import patterntesting.runtime.annotation.Broken;

/**
 * This is a class which is marked as @Broken. So you should get an
 * AssertionError (if assertions are enabled) if you instantiate this
 * class or call any method of this class.
 *
 * @author oliver
 * @since 23.11.2009
 */
@Broken
public class BrokenClass {

}
