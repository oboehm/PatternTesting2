/*
 * Copyright (c) 2008-2019 by Oliver Boehm
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
 * (c)reated 22.02.2009 by oliver (ob@oasd.de)
 */
package patterntesting.sample.jmx;

import org.junit.jupiter.api.Test;
import patterntesting.runtime.util.ThreadUtil;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The Class AlarmClockTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 0.9 (22.02.2009)
 */
public class AlarmClockTest {

    private final AlarmClock alarmClock = new AlarmClock();

    /**
     * Test method for {@link AlarmClock#setAlarmTime(java.util.Date)}.
     */
    @Test
    public final void testSetAlarmTime() {
        alarmClock.setTimeToWaitInMillis(200);
        assertTrue(alarmClock.isActivated(), "alarmClock should be activated");
        ThreadUtil.sleep(300);
        assertFalse(alarmClock.isActivated(), "alarmClock should be deactivated");
    }

    /**
     * Test method for {@link AlarmClock#deactivateAlarm()}.
     */
    @Test
    public final void testDeactivateAlarm() {
        alarmClock.setTimeToWaitInMillis(1000);
        alarmClock.deactivateAlarm();
        assertFalse(alarmClock.isActivated());
    }

    /**
     * Test deactivate alarm twice.
     */
    @Test
    public final void testDeactivateAlarmTwice() {
        alarmClock.deactivateAlarm();
        alarmClock.setTimeToWaitInMillis(1000);
        alarmClock.deactivateAlarm();
        alarmClock.deactivateAlarm();
        assertFalse(alarmClock.isActivated());
    }

    /**
     * Test set alarm time twice.
     */
    @Test
    public final void testSetAlarmTimeTwice() {
        alarmClock.setTimeToWaitInMillis(1000);
        alarmClock.setTimeToWaitInMillis(200);
        assertTrue(alarmClock.isActivated(), "alarmClock should be activated");
        alarmClock.deactivateAlarm();
        assertFalse(alarmClock.isActivated(), "alarmClock should be deactivated");
    }

}
