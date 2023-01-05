/*
 * Copyright (c) 2009-2023 by Oliver Boehm
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
 * (c)reated 06.05.2009 by oliver (ob@aosd.de)
 */
package patterntesting.runtime.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The Class ReflectionHelperTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.17 $
 * @since 06.05.2009
 */
public final class ReflectionHelperTest {

    private static final Logger LOG = LoggerFactory.getLogger(ReflectionHelperTest.class);

    /** The Constant NULL_STRING. */
    protected static final String NULL_STRING = null;

    /** The null string. */
    protected String nullString;            // SUPPRESS CHECKSTYLE

    /** The not null. */
    protected String notNull = "not null";  // SUPPRESS CHECKSTYLE
    private final String privateNullString = null;
    private String privateKey = "myPrivateKey";
    private int id;

    /**
     * This constructor inserted to avoid some warnings about never read local
     * attributes.
     */
    public ReflectionHelperTest() {
        LOG.trace("privateNullString={}", privateNullString);
    }

    /**
     * Test method for
     * {@link patterntesting.runtime.util.ReflectionHelper#getUninitializedNonStaticFields(java.lang.Object)}
     * .
     */
    @Test
    public void testGetUninitializedFields() {
        ReflectionHelperTest obj = new ReflectionHelperTest();
        obj.nullString = null;
        Collection<Field> fields = ReflectionHelper
                .getUninitializedNonStaticFields(obj);
        LOG.info("found: " + fields);
        assertEquals(2, fields.size());
    }

    /**
     * Test get to short string field.
     *
     * @throws NoSuchFieldException the no such field exception
     */
    @Test
    public void testGetToShortStringField() throws NoSuchFieldException {
        Field field = ReflectionHelper.getField(this.getClass(), "nullString");
        assertEquals("String nullString", ReflectionHelper.toShortString(field));
    }

    /**
     * Test method for {@link ReflectionHelper#invokeMethod(Object, String, Object...)}.
     */
    @Test
    public void testInvokePublicMethod() {
        String x = "for testing";
        assertEquals(x.toUpperCase(), ReflectionHelper.invokeMethod(x, "toUpperCase"));
    }

    /**
     * Test method for {@link ReflectionHelper#invokeMethod(Object, String, Object...)}
     * with an argument.
     */
    @Test
    public void testInvokeMethodWithArg() {
        String hello = "hello world";
        assertEquals(hello.substring(6), ReflectionHelper.invokeMethod(hello, "substring", 6));
    }

    /**
     * Test method for {@link ReflectionHelper#invokeMethod(Object, String, Object...)}
     * with a string argument.
     */
    @Test
    public void testInvokeMethodWithStringArg() {
        String hello = "hello";
        assertEquals(hello.concat(" world"),
                ReflectionHelper.invokeMethod(hello, "concat", " world"));
    }

    /**
     * Test method for {@link ReflectionHelper#invokeMethod(Object, String, Object...)}
     * with two arguments.
     */
    @Test
    public void testInvokeMethodWith2Args() {
        String hello = "hello";
        assertEquals(hello.replaceAll(".l", "LL"),
                ReflectionHelper.invokeMethod(hello, "replaceAll", ".l", "LL"));
    }

    /**
     * Test method for {@link ReflectionHelper#invokeMethod(Object, String, Object...)}.
     */
    @Test
    public void testInvokePrivateMethod() {
        String hello = sayHello();
        assertEquals(hello, ReflectionHelper.invokeMethod(this, "sayHello"));
    }

    /**
     * Test method for {@link ReflectionHelper#invokeMethod(Object, String, Object...)}.
     */
    @Test
    public void testInvokePrivateMethodWithArg() {
        String hello = sayHello("world");
        assertEquals(hello, ReflectionHelper.invokeMethod(this, "sayHello", "world"));
    }

    private String sayHello() {
        return "hello";
    }

    private String sayHello(final String arg) {
        return "hello " + arg;
    }

    /**
     * Test method for {@link ReflectionHelper#getMethod(Class, String, Class...)}.
     *
     * @throws NoSuchMethodException the no such method exception
     */
    @Test
    public void testGetMethod() throws NoSuchMethodException {
        Method method = ReflectionHelper.getMethod(ReflectionHelperTest.class, "sayHello",
                String.class);
        assertEquals("sayHello", method.getName());
    }

    /**
     * Test method for {@link ReflectionHelper#setFieldValue(Object, String, String)}.
     *
     * @throws ReflectiveOperationException the reflective operation exception
     */
    @Test
    public void testSetFieldValue() throws ReflectiveOperationException {
        String testValue = "PublicKey";
        ReflectionHelper.setFieldValue(this, "privateKey", testValue);
        assertEquals(testValue, this.privateKey);
    }

    /**
     * Test method for {@link ReflectionHelper#hasId(Object)}.
     */
    @Test
    public void testHasId() {
        Date now = new Date();
        assertFalse(ReflectionHelper.hasId(now), "has no id: " + now);
    }

    /**
     * Test method for {@link ReflectionHelper#getId(Object)}.
     */
    @Test
    public void testGetIdFromField() {
        this.id = 4711;
        assertTrue(ReflectionHelper.hasId(this), "has id: " + this);
        assertEquals(4711, ReflectionHelper.getId(this));
    }

    /**
     * Another test method for {@link ReflectionHelper#getId(Object)}.
     */
    @Test
    public void testGetIdFromGetter() {
        Thread thread = Thread.currentThread();
        assertTrue(ReflectionHelper.hasId(thread), "has id: " + thread);
        assertEquals(thread.getId(), ReflectionHelper.getId(thread));
    }
    
    /**
     * Test method for {@link ReflectionHelper#getId(Object)}.
     */
    @Test
    public void testGetIdNotExisting() {
        assertThrows(IllegalArgumentException.class, () -> ReflectionHelper.getId(new Date()));
    }

}
