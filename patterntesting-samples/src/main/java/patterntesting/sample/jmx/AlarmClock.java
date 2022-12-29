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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import patterntesting.runtime.jmx.MBeanRegistry;
import patterntesting.runtime.util.ThreadUtil;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * This is a simple example how you can to write an MBean and how you can use
 * the MBeanRegistry interface to register the MBean at the MBean server.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 0.9 (22.02.2009)
 */
public final class AlarmClock extends TimerTask implements AlarmClockMBean,
        MBeanRegistry {

    private static final Logger log = LoggerFactory.getLogger(AlarmClock.class);
    private Date alarmTime;
    private Timer timer;

    /**
     * Instantiates a new alarm clock.
     */
    public AlarmClock() {
        this.alarmTime = new Date();
    }

    /**
     * Instantiates a new alarm clock.
     *
     * @param alarmTime the alarm time
     */
    public AlarmClock(final Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    /**
     * Gets the time.
     *
     * @return the time
     * @see AlarmClockMBean#getTime()
     */
    public Date getTime() {
        return new Date();
    }

    /**
     * Gets the alarm time.
     *
     * @return the alarm time
     * @see AlarmClockMBean#getAlarmTime()
     */
    public synchronized Date getAlarmTime() {
        return alarmTime;
    }

    /**
     * Sets the alarm time.
     *
     * @param alarmTime the new alarm time
     * @see AlarmClockMBean#setAlarmTime(Date)
     */
    public synchronized void setAlarmTime(final Date alarmTime) {
        this.alarmTime = (Date) alarmTime.clone();
        this.activateAlarm();
    }

    /**
     * Gets the time to wait in millis.
     *
     * @return the time to wait in millis
     * @see AlarmClockMBean#getTimeToWaitInMillis()
     */
    public synchronized long getTimeToWaitInMillis() {
        return this.alarmTime.getTime() - System.currentTimeMillis();
    }

    /**
     * Sets the time to wait in millis.
     *
     * @param t the new time to wait in millis
     * @see AlarmClockMBean#setTimeToWaitInMillis(long)
     */
    public synchronized void setTimeToWaitInMillis(final long t) {
        setAlarmTime(new Date(System.currentTimeMillis() + t));
    }

    /**
     * We can use the timer only once so first a new timer is created.
     * This is also true for the AlarmClock (which implements the TimerTask).
     * We need a new AlarmClock for the newly created timer here.
     */
    private void activateAlarm() {
        timer = new Timer("AlarmClock");
        timer.schedule(new AlarmClock(this.alarmTime), this.alarmTime);
    }

    /**
     * Deactivate alarm.
     *
     * @see AlarmClockMBean#deactivateAlarm()
     */
    public synchronized void deactivateAlarm() {
        if (timer == null) {
            log.debug("alarm already deactivated");
        } else {
            timer.cancel();
            this.alarmTime = new Date();
            log.debug("alarm deactivated");
        }
    }

    /**
     * Checks if is activated.
     *
     * @return true, if is activated
     */
    public synchronized boolean isActivated() {
        return this.alarmTime.after(new Date());
    }

    /**
     * Ring.
     *
     * @see AlarmClockMBean#ring()
     */
    public synchronized void ring() {
        log.info("rrrrring");
        this.alarmTime = null;
    }

    /**
     * Run.
     *
     * @see TimerTask#run()
     */
    @Override
    public void run() {
        this.ring();
    }

    /**
     * The main method.
     *
     * @param args the arguments
     *
     * @throws JMException the JM exception
     */
    public static void main(final String[] args) throws JMException {
        registerAlarmClockManual();
        registerAlarmClockSimple();
        ThreadUtil.sleep(300, TimeUnit.SECONDS);
    }

    /**
     * This is an example how to register an AlarmClock at the MBeanServer.
     *
     * @throws JMException the JM exception
     */
    public static void registerAlarmClockManual() throws JMException {
        ObjectName name = new ObjectName(
                "patterntesting.sample.jmx:type=ManualRegisteredClock");
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        server.registerMBean(new AlarmClock(), name);
        log.info(name + " registered at " + server);
    }

    /**
     * This is an example how to register an AlarmClock provided by the
     * default imolmentation of the MBeanRegistry interface.
     *
     * @throws JMException the JM exception
     * @see MBeanRegistry
     */
    public static void registerAlarmClockSimple() throws JMException {
        AlarmClock clock = new AlarmClock();
        clock.registerAsMBean();
    }

}
