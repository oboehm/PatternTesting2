/**
 * $Id: AbstractImmutableAspect.aj,v 1.2 2012/01/31 19:19:29 oboehm Exp $
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
 * (c)reated 29.11.2008 by oliver (ob@oasd.de)
 */
package patterntesting.check.ct;

/**
 * A warning is printed if attributes of an immutable class are not final.
 * <br/>
 * If you write your own aspect you must tell this aspect where you want to
 * see the warnings. This is done by overwriting the abstract pointcut
 * <code>applicationCode</code>.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 29.11.2008
 * @version $Revision: 1.2 $
 */
public abstract aspect AbstractImmutableAspect {

    /**
     * Specify classes should be considered as "immutable" and should
     * be checked.
     * <br/>
     * Ex: <code>public pointcut applicationCode(): within(@Immutable *)</code>
     * @see ImmutableAspect
     */
    public abstract pointcut applicationCode();

    declare warning: set(!transient !final * *..*)
            && !withincode(* *..*.*(..))
            && applicationCode()
            :
        "declare attribute as final or transient in immutable object";

    declare warning: set(!transient * *..*)
            && withincode(* *..*.*(..))
            && applicationCode()
            :
        "do not change attribute in immutable object (declare it final)";

    declare warning: get(!transient !final * *..*)
           && applicationCode()
           :
        "don't use non final or non transient attributes in immutable object";

}

/**
 * $Log: AbstractImmutableAspect.aj,v $
 * Revision 1.2  2012/01/31 19:19:29  oboehm
 * (Abstract)IllegalArgumentExceptionAspect added
 *
 * Revision 1.1  2011/12/22 17:15:08  oboehm
 * directory structure adapted to preferences of maven-aspectj-plugin
 *
 * Revision 1.1  2010/01/05 16:44:13  oboehm
 * begin with 1.0
 *
 * Revision 1.2  2009/12/20 17:30:35  oboehm
 * trailing spaces removed
 *
 * Revision 1.1  2009/03/25 22:02:26  oboehm
 * initial setup
 *
 * $Source: /cvsroot/patterntesting/PatternTesting10/patterntesting-check-ct/src/main/aspect/patterntesting/check/ct/AbstractImmutableAspect.aj,v $
 */
