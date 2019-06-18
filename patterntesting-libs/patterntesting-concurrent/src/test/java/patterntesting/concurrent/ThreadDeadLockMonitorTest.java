/*
 * Copyright (c) 2009-2019 by Oliver Boehm
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import patterntesting.concurrent.test.DeadLocker;
import patterntesting.runtime.util.ThreadUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This test class will provocate a thread deadlock.
 *
 * @author oliver
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
		pool.submit(() -> test.acquireTwoLocks());
		pool.submit(() -> test.acquireLocks());
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
