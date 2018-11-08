/*
 * $Id: TimeMachineAspect.aj,v 1.4 2016/10/24 20:22:07 oboehm Exp $
 *
 * Copyright (c) 2015 by Oliver Boehm
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
 * (c)reated 06.09.2015 by oliver (ob@oasd.de)
 */

package patterntesting.runtime;

import org.aspectj.lang.annotation.SuppressAjWarnings;
import patterntesting.runtime.annotation.TimeMachine;
import patterntesting.runtime.util.Converter;

import java.util.Date;

/**
 * Together with the {@link TimeMachine} annotation you influence the current
 * time. This can be useful for unit or integration tests if you want to
 * set the actual date to the past (or future).
 *
 * @author oliver
 * @since 1.6 (06.09.2015)
 * @version $Revision: 1.4 $
 */
public aspect TimeMachineAspect {

    private TimeMachine timeMachine;

    /**
     * All Date calls below methods marked with the {@link TimeMachine}
     * annotation should instrumented. The problem herefor is that these
     * methods must be not call via reflexion because the AspectJ compiler
     * cannot recognize this as pointcut. This is e.g. if the most upper
     * method is called via JUnit. In this case extract the test method in
     * an extra method and mark this method with "@TimeMachine".
     */
    public pointcut applicationCode() :
        cflowbelow(call(@TimeMachine * *..*.*(..)))
        || cflowbelow(call(@TimeMachine *..*.new(..)))
        ;

    /**
     * Declare warning if a test method is also marked with the
     * {@link TimeMachine} annotation.
     */
    declare warning : execution(@Test @TimeMachine * *..*.*(..)) :
        "won't work because @Test method is called via reflexion - insert an additional method";

    /**
     * Here we store the annotation for the next pointcut.
     *
     * @param tm the annotation to store
     */
    before(TimeMachine tm) : (call(@TimeMachine * *..*.*(..)) || call(@TimeMachine *..*.new(..)))
            && @annotation(tm) {
        timeMachine = tm;
    }

    /**
     * Manipulates the {@link Date} call.
     *
     * @param a the a
     * @return the date
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    Date around() : applicationCode() && call(java.util.Date.new()) {
        return Converter.toDate(timeMachine.today());
    }

    /**
     * Manipulates the {@link System#currentTimeMillis()} call.
     *
     * @param a the a
     * @return the time in milliseconds
     */
    long around() : applicationCode() && call(long System.currentTimeMillis()) {
        return Converter.toDate(timeMachine.today()).getTime();
    }

}

