/*
 * $Id: ThreadDeadLockMonitor.java,v 1.8 2016/12/18 21:56:49 oboehm Exp $
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
 * (c)reated 10.07.2009 by oliver (ob@oasd.de)
 */

package patterntesting.concurrent;

import java.lang.management.*;
import java.util.*;
import java.util.concurrent.*;

import org.slf4j.*;

/**
 * You can use JMX and the 'jconsole' to find dead locks. But perhaps you can't
 * connect to JMX because your application server is behind a firewall or JMX
 * is disabled. So you use this class to log it and to send a notification to
 * your class.
 * <p>
 * After an idea of the magazin "Java Magazin" (April/May 09).
 * </p>
 *
 * @author oliver
 * @version $Revision: 1.8 $
 * @since 10.07.2009
 */
public final class ThreadDeadLockMonitor {

	private static final Logger log = LoggerFactory.getLogger(ThreadDeadLockMonitor.class);
	private final Timer threadCheck = new Timer(true);
	private final ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
	private final Collection<DeadLockListener> listeners = new CopyOnWriteArraySet<DeadLockListener>();

	/**
	 * The default check interval will be 5 minutes, i.e. this class checks
	 * every 5 minutes if there is a dead lock.
	 */
	public ThreadDeadLockMonitor() {
		this(300, TimeUnit.SECONDS);
	}

	/**
	 * Every interval a check for dead locks will be started.
	 *
	 * @param interval e.g. 5
	 * @param unit e.g. TimeUnit.SECONDS
	 */
	public ThreadDeadLockMonitor(final long interval, final TimeUnit unit) {
		this(TimeUnit.MILLISECONDS.convert(interval, unit));
	}

	/**
	 * Instantiates a new thread dead lock monitor.
	 *
	 * @param timeInMillis the time in millis
	 */
	public ThreadDeadLockMonitor(final long timeInMillis) {
		threadCheck.schedule(new TimerTask() {
			@Override
			public void run() {
				checkForDeadLocks();
			}
		}, timeInMillis, timeInMillis);
		addListener(new DeadLockLogger());
	}

	private void checkForDeadLocks() {
		long[] ids = findDeadLockThreads();
		if ((ids == null) || (ids.length == 0)) {
			log.trace("no deadlocks found");
		} else {
			Thread[] threads = new Thread[ids.length];
			for (int i = 0; i < ids.length; i++) {
				threads[i] = findMatchingThreads(ids[i]);
			}
			notifyListeners(threads);
		}
	}

	private long[] findDeadLockThreads() {
	    return mbean.findMonitorDeadlockedThreads();
	}

	private Thread findMatchingThreads(final long id) {
		for (Thread thread : Thread.getAllStackTraces().keySet()) {
			if (thread.getId() == id) {
				return thread;
			}
		}
		throw new IllegalStateException("thread " + id + " not found");
	}

	/**
	 * Adds the listener.
	 *
	 * @param l the l
	 *
	 * @return true, if successful
	 */
	public boolean addListener(final DeadLockListener l) {
		return listeners.add(l);
	}

	/**
	 * Removes the listener.
	 *
	 * @param l the l
	 *
	 * @return true, if successful
	 */
	public boolean removeListener(final DeadLockListener l) {
		return listeners.remove(l);
	}

	private void notifyListeners(final Thread[] threads) {
		for (DeadLockListener l : listeners) {
			l.deadLockDetected(threads);
		}
	}

}
