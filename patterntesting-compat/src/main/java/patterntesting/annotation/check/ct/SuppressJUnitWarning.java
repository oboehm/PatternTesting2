/**
 * $Id: SuppressJUnitWarning.java,v 1.3 2016/12/10 20:55:19 oboehm Exp $
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
 * (c)reated 15.03.2009 by oliver (ob@aosd.de)
 */
package patterntesting.annotation.check.ct;

import java.lang.annotation.*;

/**
 * If PatternTesting reports a warning about the improber use of JUnit you can
 * suppress the warning with this annotation. You can put this annotation in
 * front of a method. If you annotate a class all warnings will be suppressed.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.3 $
 * @since 15.03.2009
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface SuppressJUnitWarning {

}

/**
 * $Log: SuppressJUnitWarning.java,v $
 * Revision 1.3  2016/12/10 20:55:19  oboehm
 * code reformatted and cleaned up
 * Revision 1.2 2016/01/06 20:46:28 oboehm
 * javadoc tags corrected
 *
 * Revision 1.1 2010/01/05 13:26:17 oboehm begin with 1.0
 *
 * Revision 1.2 2009/12/19 22:34:09 oboehm trailing spaces removed
 *
 * Revision 1.1 2009/03/26 10:15:54 oboehm annotations for
 * patterntesting-check-ct moved to patterntesting.annotation.check.ct
 *
 * $Source:
 * /cvsroot/patterntesting/PatternTesting10/patterntesting-rt/src/main/java/patterntesting/annotation/check/ct/SuppressJUnitWarning.java,v
 * $
 */