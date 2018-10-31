/*
 * Copyright (c) 2013 by Oli B.
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
 * (c)reated 29.11.2013 by Oli B. (boehm@javatux.de)
 */

package patterntesting.runtime.util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {@link AssertArg} class.
 *
 * @author oliver (boehm@javatux.de)
 * @since 1.4 (29.11.2013)
 */
public class AssertArgTest {

    private static Validator validator;

    /**
     * Sets the up validator.
     */
    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        validator = vf.getValidator();
    }

    /**
     * Test method for {@link AssertArg#isValid(Object, Validator)}.
     */
    @Test
    public void testIsValid() {
        assertThrows(IllegalArgumentException.class, () -> {
            final DummyUser argument = new DummyUser();
            AssertArg.isValid(argument, validator);
        });
    }

    /**
     * Test method for {@link AssertArg#isValid(Object, Validator)}.
     */
    @Test
    public void testIsInvalid() {
        final Object argument = "test-argument";
        AssertArg.isValid(argument, validator);
    }



    /**
     * This example is a stripped down example from
     * http://techpatches.blogspot.de/2013/11/bean-validation-hibernate-validator.html.
     */
    protected static class DummyUser {

        private String firstName;
        private String lastName;

        @Max(message="Age should be max 2 digits", value = 2)
        private short age;

        /**
         * Gets the first name.
         *
         * @return the first name
         */
        @NotNull(message="First name is mandatory.")
        public String getFirstName() {
            return firstName;
        }

        /**
         * Gets the last name.
         *
         * @return the last name
         */
        @Pattern(regexp="[a-zA-Z]{1,5}$",message="Last name should not contain special characters")
        public String getLastName() {
            return lastName;
        }

        /**
         * Gets the age.
         *
         * @return the age
         */
        public short getAge() {
            return age;
        }

        /**
         * To string.
         *
         * @return the string
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " " + this.lastName;
        }

    }

}

