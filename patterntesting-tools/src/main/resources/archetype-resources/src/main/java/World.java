/*
 * $Id: World.java,v 1.2 2016/01/06 20:08:40 oboehm Exp $
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
 * (c)reated 03.01.2009 by oliver (ob@oasd.de)
 */
package ${package};

import patterntesting.annotation.check.ct.SuppressSystemOutWarning;
import patterntesting.runtime.annotation.ProfileMe;
import patterntesting.runtime.monitor.ProfileStatistic;

/**
 * This is an example where it makes sense to use System.out or System.err.
 * And it is an example how to find method which are never used (e.g. the
 * printBye()-method).
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 03.01.2009
 * @version $Revision: 1.2 $
 */
@ProfileMe
public class World {

    /** Utility class - no need to instantiate it. */
    private World() {}
    
    /**
     * The main method must not be marked with @SuppressSystemOutWarning.
     * <p>
     * Look at the log if main has finished. You should see the values of
     * the ProfileStatic class. The methods with hits = 0 are the methods
     * which are never called.
     * </p>
     *
     * @param args arguments (will be ignored)
     */
    public static void main(final String[] args) {
        System.out.println("Hello World!");
        printHelloAgain();
        ProfileStatistic.addAsShutdownHook();
        // Let's wait for 5 Minute to have the chance to start 'jconsole'
        //ThreadUtil.sleep(5, TimeUnit.MINUTES);
    }

    @SuppressSystemOutWarning
    private static void printHelloAgain() {
        System.out.println("Hello again!");
    }

    /**
     * This is an example of a methode which is never called. Start main
     * with the VM options (Java 5 only)
     * -Dcom.sun.management.jmxremote.local.only=false
     * -Dcom.sun.management.jmxremote
     * and set a breakpoint at the end of main(). Then start the 'jconsole',
     * open the patterntesting-MBean "ProfileStatistic" and press
     * "logStatistic" or "dumpStatistic" as MBean operation.
     */
    @SuppressSystemOutWarning
    public static void printBye() {
        System.out.println("Bye!");
    }

}
