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
 * (c)reated 13.11.2008 by oliver (ob@oasd.de)
 */
package patterntesting.concurrent;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.jupiter.api.Test;
import patterntesting.annotation.concurrent.GuardedBy;
import patterntesting.annotation.concurrent.Synchronized;
import patterntesting.annotation.concurrent.ThreadSafe;
import patterntesting.runtime.util.ThreadUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This is the test class for the SynchronizedAspect.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 13.11.2008
 */
@ThreadSafe
@Synchronized
public final class SynchronizedStaticTest implements Runnable {

	private static Logger log = LoggerFactory.getLogger(SynchronizedStaticTest.class);

	/**
	 * Test multi threading.
	 *
	 * @throws InterruptedException the interrupted exception
	 */
	@Test
	public final void testMultiThreading() throws InterruptedException {
		Runnable r = new SynchronizedStaticTest();
		Thread t1 = new Thread(r, "t1");
		Thread t2 = new Thread(r, "t2");
		int n = counter;
        log.info("starting t1 and t2...");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        log.info("thread t1 and t2 has finished");
        assertEquals(n + 2, counter, "lost update");
	}

	/////   do some stuff to test multi-threading   ///////////////////////////

	/** this counter needs to be synchronized */
	private static int counter = 1;

    /**
     * To give multiple threads the chance for a "lost update"
     * this implementation is slowed down.
     * <p>
     * If @Synchronized for class is missing you'll get a warning here.
     * </p>
     */
	@GuardedBy("SynchronizedStaticAspect")
	@Synchronized
    public static void incrementCounter() {
        int n = counter;
        log.debug("counter read (" + n + ")");
        ThreadUtil.sleep(20);
        n++;
        log.debug("counter increased (" + n + ")");
        ThreadUtil.sleep(20);
        counter = n;
        log.debug("counter written (" + n + ")");
    }

	/**
	 * Run.
	 *
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		incrementCounter();
	}

}
