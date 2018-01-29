/**
 * $Id: DontLogMe.java,v 1.3 2016/12/10 20:55:18 oboehm Exp $
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
 * (c)reated 08.06.2009 by oliver (ob@aosd.de)
 */
package patterntesting.runtime.annotation;

import java.lang.annotation.*;

/**
 * E.g. the ProfileAspect together with the AbstractProfileAspect logs also the
 * arguments for long running methods. But sometimes you don't want to see the
 * value of an argument, e.g.
 * <ul>
 * <li>if the argument is a password (you should never log passwords!)</li>
 * <li>if the argument contains personal infos which should not appear in the
 * log (like the age of a woman;-)</li>
 * <li>if the value of the argument would be too long and would make the output
 * unreadable</li>
 * <li>or other reasons.</li>
 * </ul>
 * Now you can prefix these arguments with this annotation.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.3 $
 * @since 08.06.2009
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface DontLogMe {

}

/**
 * $Log: DontLogMe.java,v $
 * Revision 1.3  2016/12/10 20:55:18  oboehm
 * code reformatted and cleaned up
 * Revision 1.2 2016/01/06 20:46:27 oboehm javadoc tags
 * corrected
 *
 * Revision 1.1 2010/01/05 13:26:17 oboehm begin with 1.0
 *
 * Revision 1.2 2009/12/19 22:34:09 oboehm trailing spaces removed
 *
 * Revision 1.1 2009/06/10 19:56:57 oboehm DontLogMe annotation added to hide
 * parameters logged by @ProfileMe
 *
 * $Source:
 * /cvsroot/patterntesting/PatternTesting10/patterntesting-rt/src/main/java/patterntesting/runtime/annotation/DontLogMe.java,v
 * $
 */
