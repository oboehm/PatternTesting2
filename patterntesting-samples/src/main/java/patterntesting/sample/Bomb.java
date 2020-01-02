/*
 * $Id: Bomb.java,v 1.2 2016/01/06 20:47:37 oboehm Exp $
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
 * (c)reated 09.03.2009 by oliver (ob@aosd.de)
 */
package patterntesting.sample;

import patterntesting.annotation.check.ct.SuppressSystemOutWarning;
import patterntesting.annotation.exception.TestException;
import patterntesting.exception.ExceptionFactory;

import javax.management.JMException;

/**
 * This is a simple bomb which ticks for one hour and then makes "bumm".
 * It is an example how to use the @TestException annotation to test the
 * software with unexpected exceptions.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.2 $
 * @since 09.03.2009
 */
public final class Bomb {

    /**
     * The main method.
     *
     * @param args the arguments
     *
     * @throws JMException if ExceptionFactory can't be registered to JMX
     */
    public static void main(final String... args) throws JMException {
        ExceptionFactory.registerAsMBean();
        Bomb bomb = new Bomb();
        bomb.start2();
    }

    /**
     * Start the bomb and make "bumm" after an hour.
     *
     * @throws InterruptedException the interrupted exception
     */
    @SuppressSystemOutWarning
    public void start() throws InterruptedException {
        for (int i = 0; i < 3600; i++) {
            tick();
        }
        System.out.println("BUMM");
    }

    /**
     * Start the bomb and make "bumm" after an hour.
     */
    @SuppressSystemOutWarning
    public void start2() {
        for (int i = 0; i < 3600; i++) {
            try {
                tick();
            } catch (InterruptedException e) {
                System.out.println("*TACK*");
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("BUMM");
    }

    /**
     * Tick.
     *
     * @throws InterruptedException the interrupted exception
     */
    @TestException
    @SuppressSystemOutWarning
    void tick() throws InterruptedException {
        System.out.println("tick");
        Thread.sleep(1000);
    }

}
