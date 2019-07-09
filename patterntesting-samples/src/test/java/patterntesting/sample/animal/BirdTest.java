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
 * (c)reated 22.01.2009 by oliver (ob@oasd.de)
 */
package patterntesting.sample.animal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * The Class BirdTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 0.9 (22.01.2009)
 */
public class BirdTest {

    /**
     * Test method for {@link Bird#Bird(String)}.
     */
    @Test
    public final void testPenguin() {
        Bird tux = new Penguin("Tux");
        assertNotNull(tux);
    }

    /**
     * Why can we have a NullPointerException here? Look at the Bird class -
     * in the constructor there the abstract method "sayHello()" is called
     * which accesses an internal attribute (born). But this attribute is not
     * yet initialized.
     * <p>
     * Activate the uncommented sayHello()-call in the constructor of the
     * Bird class and you will see NPE.
     * </p>
     */
    @Test
    public final void testDuck() {
        new Duck("Donald");
    }

}
