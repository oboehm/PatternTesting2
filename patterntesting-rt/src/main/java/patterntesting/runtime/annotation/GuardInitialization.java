/**
 * $Id: GuardInitialization.java,v 1.5 2016/12/10 20:55:18 oboehm Exp $
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
 * (c)reated 23.11.2008 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.annotation;

import java.lang.annotation.*;

/**
 * If your static initialization fails and you get a NoClassDefFoundError use
 * ths anntoation. Together with the InitializationAspect it will skip the
 * errornous parts of the initialisation and log the errors.
 * <p>
 * But there is another task for this annotation (together with the
 * InitializationAspect): to avoid the run-on initialization bug (see
 * http://www.ibm.com/developerwords/java/library/j-diag0416.html) the
 * attributes should be guarded. They should be all initialized after a
 * constructor has been called.
 * </p>
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.5 $
 * @since 23.11.2008
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.CONSTRUCTOR, ElementType.TYPE })
public @interface GuardInitialization {

}

/**
 * $Log: GuardInitialization.java,v $
 * Revision 1.5  2016/12/10 20:55:18  oboehm
 * code reformatted and cleaned up
 * Revision 1.4 2016/01/06 21:11:35 oboehm
 * more javadoc errors corrected
 *
 * Revision 1.3 2016/01/06 20:46:27 oboehm javadoc tags corrected
 *
 * Revision 1.2 2016/01/06 20:08:17 oboehm javadocs corrected
 *
 * Revision 1.1 2010/01/05 13:26:17 oboehm begin with 1.0
 *
 * Revision 1.4 2009/12/19 22:34:09 oboehm trailing spaces removed
 *
 * Revision 1.3 2009/09/18 13:54:51 oboehm javadoc warnings fixed
 *
 * Revision 1.2 2009/05/08 20:41:08 oboehm
 * https://sourceforge.net/support/tracker.php?aid=2786846 is fixed
 *
 * Revision 1.1 2008/11/23 21:42:04 oboehm static initialization can now be
 * protected by @GuradInitialization annotation
 *
 * $Source:
 * /cvsroot/patterntesting/PatternTesting10/patterntesting-rt/src/main/java/patterntesting/runtime/annotation/GuardInitialization.java,v
 * $
 */
