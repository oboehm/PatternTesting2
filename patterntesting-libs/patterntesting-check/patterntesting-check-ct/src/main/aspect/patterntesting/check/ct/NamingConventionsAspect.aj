/**
 * $Id: NamingConventionsAspect.aj,v 1.1 2011/12/22 17:15:08 oboehm Exp $
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
 * (c)reated 21.03.2009 by oliver (ob@aosd.de)
 */
package patterntesting.check.ct;

/**
 * According the Sun naming conventions for Java underscores should be
 * only used for constants.
 *
 * @{link "http://java.sun.com/docs/codeconv/html/CodeConventions.doc8.html"}
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 21.03.2009
 * @version $Revision: 1.1 $
 */
public aspect NamingConventionsAspect {

    pointcut attributesWithUnderscores() :
        set(* *..*.*_*);

    pointcut allowedUnderscores() :
        set(static final * *..*.*_*);

    /**
     * No underscores in attribute names! Only in constants (static finals)
     * underscores are allowed.
     *
     * @see http://java.sun.com/docs/codeconv/html/CodeConventions.doc8.html
     */
    declare warning : attributesWithUnderscores() && !allowedUnderscores() :
        "don't use underscores in attribute names (only in constants)";



    pointcut methodsWithUnderscores() :
        withincode(* *..*.*_*(..));

    /**
     * No underscores in method names!
     * @see http://java.sun.com/docs/codeconv/html/CodeConventions.doc8.html
     */
    declare warning : methodsWithUnderscores() :
        "don't use underscores in method names";



    pointcut classesWithUnderscores() :
        within(*..*_*);

    /**
     * No underscores in classnames!
     * @see http://java.sun.com/docs/codeconv/html/CodeConventions.doc8.html
     */
    declare warning : classesWithUnderscores() :
        "don't use underscores in classnames";

}

/**
 * $Log: NamingConventionsAspect.aj,v $
 * Revision 1.1  2011/12/22 17:15:08  oboehm
 * directory structure adapted to preferences of maven-aspectj-plugin
 *
 * Revision 1.1  2010/01/05 16:44:14  oboehm
 * begin with 1.0
 *
 * Revision 1.3  2009/12/20 17:30:35  oboehm
 * trailing spaces removed
 *
 * Revision 1.2  2009/09/18 13:55:51  oboehm
 * javadoc warnings fixed
 *
 * Revision 1.1  2009/03/25 22:02:26  oboehm
 * initial setup
 *
 * Revision 1.1  2009/03/21 22:04:27  oboehm
 * print a warning if an indentifier with underscore is detected
 *
 * $Source: /cvsroot/patterntesting/PatternTesting10/patterntesting-check-ct/src/main/aspect/patterntesting/check/ct/NamingConventionsAspect.aj,v $
 */
