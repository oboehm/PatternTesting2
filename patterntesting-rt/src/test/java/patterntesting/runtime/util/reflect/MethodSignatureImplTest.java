/*
 * $Id: MethodSignatureImplTest.java,v 1.6 2016/12/18 20:19:41 oboehm Exp $
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
 * (c)reated 03.04.2009 by oliver (ob@aosd.de)
 */
package patterntesting.runtime.util.reflect;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * The Class MethodSignatureImplTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.6 $
 * @since 03.04.2009
 */
public class MethodSignatureImplTest {

    private static Method testMethod;
    private static MethodSignatureImpl signature;

    /**
     * Sets the up before class.
     *
     * @throws Exception the exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        testMethod = String.class.getMethod("hashCode", new Class[0]);
        signature = new MethodSignatureImpl(testMethod);
    }

    /**
     * Test method for {@link MethodSignatureImpl#getMethod()} .
     */
    @Test
    public final void testGetMethod() {
        assertEquals(testMethod, signature.getMethod());
    }

    /**
     * Test method for {@link MethodSignatureImpl#getReturnType()} .
     */
    @Test
    public final void testGetReturnType() {
        assertEquals(int.class, signature.getReturnType());
    }

    /**
     * Test method for {@link MethodSignatureImpl#getExceptionTypes()} .
     */
    @Test
    public final void testGetExceptionTypes() {
        Class<?>[] exceptionTypes = signature.getExceptionTypes();
        assertEquals(0, exceptionTypes.length);
    }

    /**
     * Test method for {@link MethodSignatureImpl#getParameterTypes()} .
     */
    @Test
    public final void testGetParameterTypes() {
        Class<?>[] parameterTypes = signature.getParameterTypes();
        assertEquals(0, parameterTypes.length);
    }

    /**
     * Test method for {@link MethodSignatureImpl#getDeclaringType()} .
     */
    @Test
    public final void testGetDeclaringType() {
        assertEquals(String.class, signature.getDeclaringType());
    }

    /**
     * Test method for {@link MethodSignatureImpl#getDeclaringTypeName()} .
     */
    @Test
    public final void testGetDeclaringTypeName() {
        assertEquals(String.class.getName(), signature.getDeclaringTypeName());
    }

    /**
     * Test method for {@link MethodSignatureImpl#getModifiers()} .
     */
    @Test
    public final void testGetModifiers() {
        assertEquals(Modifier.PUBLIC, signature.getModifiers());
    }

    /**
     * Test method for {@link MethodSignatureImpl#getName()}.
     */
    @Test
    public final void testGetName() {
        assertEquals("hashCode", signature.getName());
    }

    /**
     * Test method for {@link MethodSignatureImpl#getParameterNames()}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public final void testGetParameterNames() {
        assertNotNull(signature.getParameterNames());
    }

    /**
     * Test to string.
     */
    @Test
    public final void testToString() {
        assertEquals("int java.lang.String.hashCode()", signature.toString());
    }

    /**
     * Test method for {@link MethodSignatureImpl#toShortString()}.
     */
    @Test
    public final void testToShortString() {
        String shortString = signature.toShortString();
        assertTrue("shorter string expected: " + shortString, shortString.length() <= signature.toString().length());
    }

    /**
     * Test method for {@link MethodSignatureImpl#toLongString()}.
     */
    @Test
    public final void testToLongString() {
        assertEquals("public int java.lang.String.hashCode()", signature.toLongString());
    }

}
