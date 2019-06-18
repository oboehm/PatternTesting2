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
 * (c)reated 13.01.2009 by oliver (ob@oasd.de)
 */
package patterntesting.concurrent;

import org.junit.jupiter.api.Test;
import patterntesting.annotation.concurrent.RunParallel;
import patterntesting.concurrent.test.Counter;
import patterntesting.runtime.util.ThreadUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The Class RunParallelTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 13.01.2009
 */
public final class RunParallelTest {

    private int counter = 0;

    /**
     * Test parallel increment.
     */
    @Test
    public synchronized void testParallelIncrement() {
        int start = this.counter;
        doubleIncrement();
        ThreadUtil.sleep();
        assertEquals(start+2, this.counter);
    }

    @RunParallel(2)
    private synchronized int doubleIncrement() {
        int oldCounter = this.counter;
        this.counter = oldCounter + 1;
        return this.counter;
    }

    /**
     * Test triple increment.
     */
    @Test
    public synchronized void testTripleIncrement() {
        int start = this.counter;
        tripleIncrement();
        ThreadUtil.sleep();
        assertEquals(start+3, this.counter);
    }

    @RunParallel(3)
    private synchronized void tripleIncrement() {
        this.counter++;
    }

    /**
     * Test parallel creations.
     */
    @Test
    public synchronized void testParallelCreations() {
        int n0 = Counter.getNumberOfInstances();
        new Counter();
        ThreadUtil.sleep();
        assertEquals(n0 + 2, Counter.getNumberOfInstances());
    }

}
