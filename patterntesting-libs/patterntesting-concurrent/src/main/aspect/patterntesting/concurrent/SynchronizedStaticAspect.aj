/**
 * $Id: SynchronizedStaticAspect.aj,v 1.3 2016/12/18 21:56:49 oboehm Exp $
 *
 * Copyright (c) 2008 by Oliver Boehm
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
 * (c)reated 16.11.2008 by oliver (ob@oasd.de)
 */
package patterntesting.concurrent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.SuppressAjWarnings;
import patterntesting.annotation.concurrent.Synchronized;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * For synchronization we synchronize each static method mark as @Synchronized
 * by a ReentrantLock.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 0.8
 */
public aspect SynchronizedStaticAspect pertypewithin(@Synchronized *) {

	private static final Logger log = LogManager.getLogger(SynchronizedStaticAspect.class);
	private Lock classLock = new ReentrantLock();
	protected long timeout = 1800;
	protected TimeUnit unit = TimeUnit.SECONDS;
	
	/**
	 * This advice is used to get the timeout value for the wrapped static
	 * methods.
	 * 
	 * @param t the annotation with the timeout value
	 */
    @SuppressAjWarnings({"adviceDidNotMatch"})
	before(Synchronized t) :
			staticinitialization(@Synchronized *) && @annotation(t) {
		this.timeout = t.timeout();
		this.unit = t.unit();
		if (log.isTraceEnabled()) {
			log.trace("lock timeout for "
					+ thisJoinPointStaticPart.getSignature().getDeclaringType().getSimpleName()
					+ " set to " + this.timeout + " " + this.unit);
		}
	}

	/**
	 * This is the synchronization wrapper for the static methods which are
	 * synchronized by a ReentrantLock class.
	 * 
	 * @return the return value of the static method
	 */
    @SuppressAjWarnings({"adviceDidNotMatch"})
	Object around() : SynchronizedAspect.synchronizedStaticMethods() {
		if (log.isTraceEnabled()) {
			log.trace("synchronizing " + thisJoinPointStaticPart.getSignature().toShortString() + "...");
		}
		try {
			if (classLock.tryLock(timeout, unit)) {
				if (log.isTraceEnabled()) {
					log.trace("lock granted for "
							+ thisJoinPointStaticPart.getSignature().toShortString());
				}
				try {
					return proceed();
				} finally {
					classLock.unlock();
					if (log.isTraceEnabled()) {
						log.trace("lock released for "
								+ thisJoinPointStaticPart.getSignature().toShortString());
					}
				}
			} else {
				String msg = "can't get " + classLock + " for "
						+ thisJoinPointStaticPart.getSignature().toShortString();
				log.error(msg);
				throw new RuntimeException(msg);
			}
		} catch (InterruptedException ie) {
			String msg = "interrupted: "
					+ thisJoinPoint.getSignature().toShortString();
			log.warn(msg, ie);
			throw new RuntimeException(msg, ie);
		}
	}

}
