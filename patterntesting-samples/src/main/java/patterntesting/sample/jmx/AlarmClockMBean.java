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

import java.util.Date;

/**
 * The Interface AlarmClockMBean.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 0.9 (22.02.2009)
 */
public interface AlarmClockMBean {

    /**
     * Gets the time.
     *
     * @return the actual time
     */
    Date getTime();

    /**
     * Gets the alarm time.
     *
     * @return the alarm time
     * @see AlarmClockMBean#getAlarmTime()
     */
    Date getAlarmTime();

    /**
     * Sets the alarm time.
     *
     * @param alarmTime the new alarm time
     * @see AlarmClockMBean#setAlarmTime(Date)
     */
    void setAlarmTime(final Date alarmTime);

    /**
     * Gets the time to wait in millis.
     *
     * @return the time to wait in millis
     * @see AlarmClockMBean#getTimeToWaitInMillis()
     */
    long getTimeToWaitInMillis();

    /**
     * Sets the time to wait in millis.
     *
     * @param t the new time to wait in millis
     * @see AlarmClockMBean#setTimeToWaitInMillis(long)
     */
    void setTimeToWaitInMillis(final long t);

    /**
     * Deactivate alarm.
     * @see AlarmClockMBean#deactivateAlarm()
     */
    void deactivateAlarm();

    /**
     * Ring.
     * @see AlarmClockMBean#ring()
     */
    void ring();

}
