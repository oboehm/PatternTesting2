/*
 * $Id: SerializableTesterTest.java,v 1.14 2017/07/19 06:27:51 oboehm Exp $
 *
 * Copyright (c) 2010 by Oliver Boehm
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
 * (c)reated 21.07.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.jupiter.api.Test;
import patterntesting.runtime.annotation.IntegrationTest;
import patterntesting.runtime.junit.test.Person;
import patterntesting.runtime.junit.test.Sheep;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for SerializableTester class.
 *
 * @author oliver
 * @since 1.1 (21.07.2010)
 */
public final class SerializableTesterTest implements Serializable {

    private static final long serialVersionUID = 20140213L;
    private static final Logger LOG = LoggerFactory.getLogger(SerializableTesterTest.class);
    private final Sheep sheep = new Sheep("Sandy");

    /**
     * Test method for
     * {@link SerializableTester#assertSerialization(Serializable)}.
     *
     * @throws NotSerializableException if test object can't be serialized
     */
    @Test
    public void testAssertSerialization() throws NotSerializableException {
        Serializable object = "hello world!";
        SerializableTester.assertSerialization(object);
    }
    
    /**
     * Test method for {@link SerializableTester#deserialize(Serializable)}.
     * 
     * @throws NotSerializableException if test object can't be serialized
     */
    @Test
    public void testDeserialize() throws NotSerializableException {
        Serializable origin = new Date();
        Serializable deserialized = SerializableTester.deserialize(origin);
        assertEquals(origin, deserialized);
    }

    /**
     * Test method for
     * {@link SerializableTester#assertSerialization(Serializable)}. Here we
     * expect a {@link NotSerializableException} because this class here
     * (which is used for testing) is not serializable because of the
     * non-serializable attribute 'sheep'.
     *
     * @throws NotSerializableException if test object can't be serialized
     */
    @Test
    public void testAssertSerializationObject() throws NotSerializableException {
        assertThrows(NotSerializableException.class, () -> {
            assertNotNull(sheep);
            SerializableTester.assertSerialization(this);
        });
    }
    
    /**
     * The Person has a final transient attribute which cannot be initialized
     * after desererialization. This is the reason why we exepect here an
     * {@link AssertionError}.
     * 
     * @throws NotSerializableException if test object can't be serialized
     */
    @Test
    public void testAssertSerializationWithTransientAttribute() throws NotSerializableException {
        assertThrows(AssertionError.class, () -> SerializableTester.assertSerialization(Person.class));
    }

    /**
     * Test method for
     * {@link SerializableTester#assertSerialization(Serializable)}. Here we
     * expect a {@link NotSerializableException} because this class here
     * (which is used for testing) is not serializable because of the
     * non-serializable attribute 'sheep'.
     *
     * @throws NotSerializableException if test object can't be serialized
     */
    @Test
    public void testAssertSerializationClass() throws NotSerializableException {
        assertThrows(NotSerializableException.class, () -> SerializableTester.assertSerialization(this.getClass()));
    }

    /**
     * Test method for {@link SerializableTester#assertSerializationOfPackage(String)}.
     * @throws NotSerializableException if one of the classes can't be serialized
     */
    @IntegrationTest
    @Test
    public void testAssertSerializationOfPackage() throws NotSerializableException {
        SerializableTester.assertSerializationOfPackage("patterntesting.runtime");
    }

    /**
     * Test method for {@link SerializableTester#getSizeOf(Class)}.
     */
    @Test
    public void testGetSizeOf() {
        int size = SerializableTester.getSizeOf(String.class);
        LOG.info("Size of {} is {}.", String.class, size);
        assertThat(size, greaterThan(0));
    }

}
