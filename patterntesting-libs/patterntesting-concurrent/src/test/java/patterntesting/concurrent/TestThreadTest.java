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
 * (c)reated 01.12.2008 by oliver (ob@oasd.de)
 */
package patterntesting.concurrent;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.jupiter.api.Test;
import patterntesting.annotation.concurrent.TestThread;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This is the test class for the (Abstract)TestThreadAspect.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 01.12.2008
 */
@TestThread
public final class TestThreadTest implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(TestThreadTest.class);

    /**
     * Test multi threading.
     *
     * @throws InterruptedException the interrupted exception
     */
    @Test
    public final void testMultiThreading() throws InterruptedException {
        Thread t1 = new Thread(new TestThreadTest(), "t1");
        Thread t2 = new Thread(new TestThreadTest(), "t2");
        setCounter(10);
        t1.start();
        t2.start();
        log.info("Threads t1 and t2 started");
        t1.join();
        t2.join();
        // each thread increases the counter 5 times
        assertEquals(20, counter, "lost update");
    }

    /////   do some stuff to test multi-threading   ///////////////////////////

    /** this counter needs to be synchronized */
    private static int counter = 1;

    /**
     * To give multiple threads the chance for a "lost update"
     * this implementation is slowed down via TestThreadAspect
     * and @TestThread.
     * <p>
     * This method must be synchronized otherwise this testcase may fail.
     * </p>
     */
    @TestThread
    public static synchronized void incrementCounter() {
        int n = counter;
        n++;
        counter = n;
    }

    /**
     * Sets the counter.
     *
     * @param n the new counter
     */
    public static void setCounter(final int n) {
        counter = n;
    }

    /**
     * Run.
     *
     * @see java.lang.Runnable#run()
     */
    public void run() {
        for (int i = 0; i < 5; i++) {
            incrementCounter();
        }
    }

}
