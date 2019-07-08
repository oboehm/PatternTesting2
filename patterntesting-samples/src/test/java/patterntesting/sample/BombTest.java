/*
 * Copyright (c) 2010-2019 by Oliver Boehm
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
 * (c)reated 21.01.2010 by oliver (ob@oasd.de)
 */

package patterntesting.sample;

import org.junit.jupiter.api.Test;
import patterntesting.exception.ExceptionFactory;
import patterntesting.runtime.util.Assertions;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is another example how to use the ExceptionFactory in JUnit tests.
 * It was also intended as an example for the RunTestsParallel annotation.
 * But this attempt failed. Why? Because the Exception thrown by the
 * ExceptionFactory was caught be the wrong test thread. Remember - the
 * ExceptionFactory is a singleton which controls the whole application!
 *
 * @author oliver
 * @since 1.0 (21.01.2010)
 */
public class BombTest {

    private final Bomb bomb = new Bomb();

    /**
     * This is only a dummy test if we want to disable other tests in htis
     * JUnit test here.
     *
     * @since 1.0
     */
    @Test
    public void testBomb() {
        assertNotNull(bomb);
    }

    /**
     * This test is an example how to use the ExceptionFactory in JUnit tests.
     * Test method for {@link Bomb#start()}.
     *
     * @throws InterruptedException this exception will be provoked
     * @see ExceptionFactory
     */
    @Test
    public void testStart() throws InterruptedException {
        assertTrue(Assertions.ENABLED, "asserts must be enabled ('java -ea ...')");
        assertThrows(InterruptedException.class, () -> {
            ExceptionFactory exceptionFactory = ExceptionFactory.getInstance();
            exceptionFactory.setFire(InterruptedException.class);
            exceptionFactory.activateOnce();
            bomb.start();
        });
    }

    /**
     * This method is only called because the tick() method needs a long time
     * (1 second) but does nothing. This method was a good candidate for
     * "@RunWith(ParallelRunner.class)" for JUnit 4. But it does not work
     * because we use the ExceptionFactory to provoke Exceptions. And this
     * exceptions are caught sometimes by the wrong test thread.
     *
     * @throws InterruptedException if test was interrupted from external
     */
    @Test
    public void testTick() throws InterruptedException {
        bomb.tick();
    }

}

