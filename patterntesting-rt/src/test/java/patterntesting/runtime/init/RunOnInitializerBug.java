/*
 * $Id: RunOnInitializerBug.java,v 1.4 2016/02/13 19:03:03 oboehm Exp $
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
 * (c)reated 06.05.2009 by oliver (ob@aosd.de)
 */
package patterntesting.runtime.init;

import patterntesting.runtime.annotation.GuardInitialization;

/**
 * This class is an example for the Run-on Initializer Bug as described by Eric
 * Allen in <a href="http://flylib.com/books/en/2.4.1.16/1/">Chapter 19: The
 * Run-On Initialization</a>. The (default) constructor does not initialize all
 * values and leaves some attributes null.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.4 $
 * @since 06.05.2009
 */
@GuardInitialization
public class RunOnInitializerBug {

    private final String key;
    private Integer value;

    /**
     * Instantiates a new run on initializer bug.
     */
    public RunOnInitializerBug() {
        this.key = "unknown";
    }

    /**
     * If you call this method you will probably get a NullPointerException
     * because the object is not set up correct and does not initialize the
     * attribute "value".
     *
     * @return the string
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return key + "=" + value.intValue();
    }

}

/**
 * $Log: RunOnInitializerBug.java,v $
 * Revision 1.4  2016/02/13 19:03:03  oboehm
 * javadoc corrected
 *
 * Revision 1.3  2016/01/06 20:46:26  oboehm
 * javadoc tags corrected
 *
 * Revision 1.2  2010/12/31 14:40:14  oboehm
 * source clean up...
 *
 * Revision 1.1  2010/01/05 13:26:18  oboehm
 * begin with 1.0
 *
 * Revision 1.4  2009/12/19 22:34:09  oboehm
 * trailing spaces removed
 *
 * Revision 1.3  2009/09/25 14:49:44  oboehm
 * javadocs completed with the help of JAutodoc
 *
 * Revision 1.2  2009/09/18 13:54:51  oboehm
 * javadoc warnings fixed
 *
 * Revision 1.1  2009/05/08 20:41:08  oboehm
 * https://sourceforge.net/support/tracker.php?aid=2786846 is fixed
 *
 * $Source: /cvsroot/patterntesting/PatternTesting10/patterntesting-rt/src/test/java/patterntesting/runtime/init/RunOnInitializerBug.java,v $
 */
