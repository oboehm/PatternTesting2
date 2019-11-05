/*
 * Copyright (c) 2015-2019 by Oli B.
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
 * (c)reated 26.04.2015 by Oli B. (boehm@javatux.de)
 */

package patterntesting.sample.misc;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import patterntesting.runtime.junit.ObjectTester;

/**
 * This unit tests demonstrate the usage of the different XxxTester classes.
 *
 * @author oliver (boehm@javatux.de)
 * @since 1.5.2 (26.04.2015)
 */
public class SemiAutomatedTest {

    /**
     * This test demonstrate how the {@link ObjectTester} class can be used to
     * guarantee that the {@link Object#equals(Object)} and
     * {@link Object#hashCode()} are implemented correct if they are
     * overwritten.
     */
    @Test
    public void testEquals() {
        ObjectTester.assertEqualsOfPackage("patterntesting.sample",
                Pattern.compile(".*Anti.*"), Pattern.compile(".*CrazyCookie"));
    }

    /**
     * Let's see if the equals method of JDK itself are correct. We must
     * exclude the Date classes because they default constructor creates
     * different (= not equals) objects.
     */
    @Test
    public void testEqualsJava() {
        ObjectTester.assertEqualsOfPackage("java", Pattern.compile("java.*\\.Date"));
    }

}

