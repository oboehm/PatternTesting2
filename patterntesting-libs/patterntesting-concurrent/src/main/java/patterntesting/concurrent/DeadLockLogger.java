/**
 * $Id: DeadLockLogger.java,v 1.7 2016/12/18 21:56:49 oboehm Exp $
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

import org.apache.logging.log4j.*;

import patterntesting.runtime.util.Converter;

/**
 * The Class DeadLockLogger.
 *
 * @author oliver
 * @version $Revision: 1.7 $
 * @since 14.07.2009
 */
public final class DeadLockLogger implements DeadLockListener {

	private static final Logger LOG = LogManager.getLogger(DeadLockLogger.class);

	/**
	 * Dead lock detected.
	 *
	 * @param threads the threads
	 * @see patterntesting.concurrent.DeadLockListener#deadLockDetected(java.lang.Thread[])
	 */
	public void deadLockDetected(final Thread[] threads) {
		if (LOG.isWarnEnabled()) {
			LOG.warn("deadlock in " + threads.length + " threads detected: "
					+ Converter.toString(threads));
		}
	    for (int i = 0; i < threads.length; i++) {
            dumpThread(threads[i]);
        }
	}

    private void dumpThread(Thread thread) {
        StackTraceElement[] stackTrace = thread.getStackTrace();
        if (stackTrace.length == 0) {
            LOG.info("{} is blocked but there is no stacktrace available.", thread);
        } else if (LOG.isDebugEnabled()) {
            StringBuilder buf = new StringBuilder();
            for (StackTraceElement element : stackTrace) {
                buf.append("\n\tat ");
                buf.append(element);
            }
            LOG.debug("{} is blocked{}", thread, buf);
        } else if (LOG.isInfoEnabled()) {
            LOG.info("{} is blocked at {}.", thread, stackTrace[0]);
        }
    }

}
