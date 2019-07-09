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

/**
 * The Class Bird.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 0.9 (22.01.2009)
 */
public abstract class Bird {

    private final String name;

    /**
     * This is a bad example. Why? You should never call an abstract method
     * inside a constructor.
     * So, if you uncomment "sayHello()" it you'll get a warning message.
     * Also BirdTest.testDuck() will fail.
     *
     * @param name the name
     */
    protected Bird(final String name) {
        this.name = name;
        //sayHello();
    }

    /**
     * Say hello.
     */
    public abstract void sayHello();

    /**
     * To string.
     *
     * @return the string
     * @see Object#toString()
     */
    @Override
    public final String toString() {
        return this.getClass().getSimpleName() + " " + this.name;
    }

}
