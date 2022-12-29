/*
 * $Id: TimeMachineAspectTest.java,v 1.5 2016/12/18 20:19:39 oboehm Exp $
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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.util.Date;

import org.slf4j.LoggerFactory;
import org.slf4j.*;
import org.junit.jupiter.api.Test;

import patterntesting.runtime.annotation.TimeMachine;
import patterntesting.runtime.util.Converter;

/**
 * Unit tests for TimeMachineAspect.
 *
 * @author oliver
 * @version $Revision: 1.5 $
 * @since 1.6 (06.09.2015)
 */
public final class TimeMachineAspectTest {

    private static final Logger LOG = LoggerFactory.getLogger(TimeMachineAspectTest.class);

    /**
     * The {@link TimeMachine} annotation does not work as expected because this
     * method is called from JUnit via reflexion. So you see a corresponding
     * compiler warning if you use @TimeMachine and @Test together.
     */
    //@TimeMachine(today = "01-Sep-2015")
    @Test
    public void testTimeMachineDirect() {
        Date now = today();
        LOG.info("now = {}", now);
    }

    /**
     * Here we test the {@link TimeMachine} annotation together with the
     * TimeMachineAspect if it works as expected. Because the annotation
     * works only on methods which are not called via reflexion we put the
     * annotation to the extraced 'callToday()' method.
     */
    @Test
    public void testTimeMachineBelow() {
        Date now = callToday();
        assertEquals("01-Sep-2015", Converter.toString(now, "dd-MMM-yyyy"));
    }

    @TimeMachine(today = "01-Sep-2015")
    private Date callToday() {
        return today();
    }

    private static Date today() {
        return new Date();
    }

    /**
     * Here we test the {@link TimeMachine} annotation by putting it to
     * an inserted method.
     */
    @Test
    public void testTimeMachineWithInsertedMethod() {
        insertedMethod();
    }

    @TimeMachine(today = "02-Sep-2015")
    private void insertedMethod() {
        Date now = new Date();
        assertEquals("02-Sep-2015", Converter.toString(now, "dd-MMM-yyyy"));
    }

    /**
     * Here we test if we can manipulate a current timestamp in millis.
     */
    @Test
    public void testTimestamp() {
        long expected = Timestamp.valueOf("2015-09-25 12:34:56").getTime();
        assertEquals(expected, getCurrentTimeMillis());
    }

    @TimeMachine(today = "25-Sep-2015 12:34:56")
    private long getCurrentTimeMillis() {
        long time = System.currentTimeMillis();
        LOG.info("Current time is {}.", new Date(time));
        return time;
    }

}

