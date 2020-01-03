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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import patterntesting.annotation.concurrent.RunBackground;
import patterntesting.exception.ExceptionFactory;
import patterntesting.runtime.util.Assertions;
import patterntesting.runtime.util.ThreadUtil;

import javax.management.JMException;

import java.util.concurrent.TimeUnit;

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

    @BeforeAll
    public static void checkAssertionsEnabled() {
        assertTrue(Assertions.ENABLED, "asserts must be enabled ('java -ea ...')");
    }

    /**
     * This is only a dummy test if we want to disable other tests in this
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
     * @see ExceptionFactory
     */
    @Test
    public void testStart() {
        assertThrows(InterruptedException.class, () -> {
            ExceptionFactory exceptionFactory = ExceptionFactory.getInstance();
            exceptionFactory.setFire(InterruptedException.class);
            exceptionFactory.activateOnce();
            bomb.start();
        });
    }

    /**
     * This method is only called because the tick() method needs a long time
     * (1 second) but does nothing.
     *
     * @throws InterruptedException if test was interrupted from external
     */
    @Test
    public void testTick() throws InterruptedException {
        bomb.tick();
    }

    @Test
    public void testBumm() {
        Bomb fastBomb = new Bomb(1);
        fastBomb.startInterruptable();
    }

    @Test
    public void testMain() throws JMException {
        runMain();
        ThreadUtil.sleep(1, TimeUnit.SECONDS);
        ExceptionFactory exceptionFactory = ExceptionFactory.getInstance();
        exceptionFactory.setFire(InterruptedException.class);
        exceptionFactory.activateOnce();
        ThreadUtil.sleep(1, TimeUnit.SECONDS);
    }

    @RunBackground
    private void runMain() throws JMException {
        Bomb.main();
    }

}

