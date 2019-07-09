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

import java.util.*;

import patterntesting.annotation.check.ct.SuppressSystemOutWarning;

/**
 * The Class Duck.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 0.9 (22.01.2009)
 */
public final class Duck extends Bird {

    private final Calendar born = new GregorianCalendar();

    /**
     * Instantiates a new duck.
     *
     * @param name the name
     */
    public Duck(final String name) {
        super(name);
    }

    /**
     * Say hello.
     *
     * @see Bird#sayHello()
     */
    @Override
    @SuppressSystemOutWarning
    public void sayHello() {
        System.out.println("Quak, I'm " + this + " and was born at "
                + born.getTime());
    }

}
