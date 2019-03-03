/**
 * $Id: AbstractStaticAspect.aj,v 1.3 2012/08/12 17:48:45 oboehm Exp $
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

/**
 * If you are in a web context or/and a cluster static variables are normally
 * not a good idea. Use better final statics or at least transient static
 * variables.
 * <br/>
 * If you write your own aspect you must tell this aspect where you want to
 * see the warnings. This is done by overwriting the abstract pointcut
 * <code>applicationCode</code>.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 17.11.2008
 * @version $Revision: 1.3 $
 */
public abstract aspect AbstractStaticAspect {
	
    /**
     * Specify what is application code that should be subject to the
     * pattern test.
     * <br/>
     * If you want to allow the use of static variables in testcode exclude it
     * in <i>applicationCode</i>.
     *
     * Ex: <code>pointcut applicationCode(): within(patterntesting.samples.*)</code>
     */
    public abstract pointcut applicationCode();

    pointcut criticalStaticVars() :
    	set(static !final * *..*)
    	&& !set(static transient * *..*)
    	;

    declare warning : criticalStaticVars() && applicationCode() :
    	"don't use non final (or non transient) static variable";

}
