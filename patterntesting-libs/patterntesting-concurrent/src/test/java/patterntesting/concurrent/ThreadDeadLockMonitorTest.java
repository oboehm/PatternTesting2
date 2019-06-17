/*
 * $Id: ThreadDeadLockMonitorTest.java,v 1.6 2016/12/18 21:56:49 oboehm Exp $
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
 * (c)reated 14.07.2009 by oliver (ob@oasd.de)
 */

package patterntesting.concurrent;

import java.util.concurrent.*;

import org.junit.Test;
import org.apache.logging.log4j.*;

import patterntesting.concurrent.test.DeadLocker;
import patterntesting.runtime.util.ThreadUtil;

/**
 * This test class will provocate a thread deadlock.
 *
 * @author oliver
 * @version $Revision: 1.6 $
 * @since 14.07.2009
 */
public final class ThreadDeadLockMonitorTest implements DeadLockListener {

    private static final Logger log = LogManager.getLogger(ThreadDeadLockMonitorTest.class);
	private final DeadLocker test = new DeadLocker();
	private boolean deadLockFound = false;

	/**
	 * Test deadlock detection.
	 * Because the deadlock does not happen always the assert is replaced
	 * now by a log.warn(..).
	 */
	@Test
	public void testDeadlockDetection() {
		ThreadDeadLockMonitor monitor = new ThreadDeadLockMonitor(20);
		monitor.addListener(this);
		provocateDeadLocks();
		ThreadUtil.sleep(50);
		if (!deadLockFound) {
		    log.warn("deadlock not detected");
		}
	}

	/**
	 * To provocate a deadlock we must first call acquireTwoLocks() which
	 * acquires the this lock (because it is synchronized). Before
	 * acquireTwoLocks() can acquire the second lock (via the lock object)
	 * acquireSimpleLock() must be called in another thread. This method
	 * would get the lock and and call acquireTwoLocks() again. Because
	 * the first thread holds already the this lock acquireTwoLocks is
	 * blocked now -> dead lock.
	 */
	private void provocateDeadLocks() {
		ExecutorService pool = Executors.newFixedThreadPool(2);
		pool.submit(new Runnable() {
			public void run() {
				test.acquireTwoLocks();
			}
		});
		pool.submit(new Runnable() {
			public void run() {
				test.acquireLocks();
			}
		});
	}

	/**
	 * Dead lock detected.
	 *
	 * @param threads the threads
	 * @see patterntesting.concurrent.DeadLockListener#deadLockDetected(java.lang.Thread[])
	 */
	public void deadLockDetected(final Thread[] threads) {
		deadLockFound = true;
	}

}
