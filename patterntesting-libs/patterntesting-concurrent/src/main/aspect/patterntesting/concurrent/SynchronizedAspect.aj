/**
 * $Id: SynchronizedAspect.aj,v 1.3 2016/12/18 21:56:49 oboehm Exp $
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
 * (c)reated 13.11.2008 by oliver (ob@oasd.de)
 */
package patterntesting.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.slf4j.*;

import patterntesting.annotation.concurrent.Synchronized;

/**
 * For synchronization we synchronize each method mark as @Synchronized
 * by a ReentrantLock. The first approach uses a ConcurrentHashMap to hold
 * the lock for each object. But this would introduce a memory leak because
 * the synchronized object will be never freed - there is always the reference
 * from the HashMap to it.
 * <br/>
 * Now we instantiate for each object a seperate aspect using the perthis
 * clause. This means you must now mark also the object as @Synchronized not
 * only the methods.
 * <br/>
 * For synchronization of static methods we keep the ConcurrentHashMap approach
 * because classes are not freed by the garbage collector.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 0.8
 */
public aspect SynchronizedAspect perthis(@this(Synchronized)) {
	
	private static final Logger log = LoggerFactory.getLogger(SynchronizedAspect.class);
	private Lock objectLock = new ReentrantLock();
	private static long timeout = 1800;
	private static TimeUnit unit = TimeUnit.SECONDS;
	private static boolean timeoutInitialized = false;
	
	@SuppressWarnings({"rawtypes"})
	private final static synchronized void initTimeout(Class cl) {
		timeout = SynchronizedStaticAspect.aspectOf(cl).timeout;
		unit = SynchronizedStaticAspect.aspectOf(cl).unit;
		timeoutInitialized = true;
		log.trace("timeout inialized with " + timeout + " " + unit);
	}
	
	/**
	 * We don't want to get with an synchronized method so we do not address
	 * it here
	 */
	pointcut synchronizedMethods() :
		execution(@Synchronized !synchronized !static * *..*.*(..))
		;
	
	pointcut synchronizedStaticMethods() :
		execution(@Synchronized !synchronized static * *..*.*(..))
		;
	
	pointcut ignoredSynchronized() :
		execution(@Synchronized * *..*.*(..))
		&& !synchronizedMethods()
		&& !synchronizedStaticMethods()
		;
	
	declare warning : ignoredSynchronized() :
		"@Synchronized is ignored here";
	
	declare warning :
	        (synchronizedStaticMethods() || synchronizedMethods())
	        && !@within(Synchronized) :
	    "@Synchronized is ignored here because @Synchronized for class is missing";

	/**
	 * Uses the Lock class of Java 5 to put a synchronization wrapper around
	 * a method. Advantage of this Lock class is the posibility to use a
	 * timeout to avoid dead locks.
	 * 
	 * @return the return value of the wrapped method
	 */
	@SuppressAjWarnings({"adviceDidNotMatch"})
	Object around() : synchronizedMethods() && @within(Synchronized) {
		if (!timeoutInitialized) {
			initTimeout(thisJoinPointStaticPart.getSignature().getDeclaringType());
		}
		if (log.isTraceEnabled()) {
			log.trace("synchronizing " + thisJoinPoint.getSignature().toShortString() + "...");
		}
		try {
			if (objectLock.tryLock(timeout, unit)) {
				if (log.isTraceEnabled()) {
					log.trace("lock granted for "
							+ thisJoinPoint.getSignature().toShortString());
				}
				try {
					return proceed();
				} finally {
					objectLock.unlock();
					if (log.isTraceEnabled()) {
						log.trace("lock released for "
								+ thisJoinPoint.getSignature().toShortString());
					}
				}
			} else {
				String msg = "can't get " + objectLock + " for "
						+ thisJoinPoint.getSignature().toShortString()
						+ " after " + timeout + " " + unit;
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
