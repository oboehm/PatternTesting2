/**
 * $Id: DeadLocker.java,v 1.6 2016/12/18 21:56:49 oboehm Exp $
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

package patterntesting.concurrent.test;

import org.slf4j.*;

import patterntesting.runtime.util.ThreadUtil;

/**
 * This is a helper class for ThreadDeadLockMonitorTest to be able to
 * provocate a thread deadlock.
 *
 * @author oliver
 * @version $Revision: 1.6 $
 * @since 14.07.2009
 */
public final class DeadLocker {

	private static final Logger log = LoggerFactory.getLogger(DeadLocker.class);
	private final Object lock = new Object();

	/**
	 * Acquires two locks: "this" (because it is synchronized) and "lock".
	 */
	public synchronized void acquireTwoLocks() {
		ThreadUtil.sleep();
		synchronized (lock) {
			log.info("acquireTwoLocks(): I got the lock " + lock);
		}
		log.info("acquireLock(): lock released");
	}

	/**
	 * Acquires also two locks: first "lock" then "this" (via the call of
	 * acquireTwoLocks().
	 */
	public void acquireLocks() {
		synchronized (lock) {
			log.info("acquireLocks(): I got the lock " + lock);
			acquireTwoLocks();
		}
		log.info("acquireLock(): lock released");
	}

}
