/**
 * $Id: StaticAspect.aj,v 1.2 2012/08/12 17:48:45 oboehm Exp $
 *
 * Copyright (c) 2008 by Oliver Boehm
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
 * (c)reated 17.11.2008 by oliver (ob@oasd.de)
 */
package patterntesting.check.ct;

import patterntesting.annotation.check.ct.*;

/**
 * If you are in a web context or/and a cluster static variables are normally
 * not a good idea. Use better final statics or at least transient static
 * variables. This aspect together with AbstractStaticAspect will print a
 * warning if it finds such variables.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 17.11.2008
 * @version $Revision: 1.2 $
 *
 * @see AbstractStaticAspect
 */
public aspect StaticAspect extends AbstractStaticAspect {
	
    /**
     * Only for classes marked with "@DamnStaticVars" a warning will be
     * printed.
     */
	public pointcut applicationCode() :
		@within(DamnStaticVars);

}
