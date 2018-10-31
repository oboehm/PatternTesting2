/*
 * $Id: ConstructorSignatureImplTest.java,v 1.6 2016/12/18 20:19:41 oboehm Exp $
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
 * (c)reated 06.04.2009 by oliver (ob@aosd.de)
 */
package patterntesting.runtime.util.reflect;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The Class ConstructorSignatureImplTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.6 $
 * @since 06.04.2009
 */
public class ConstructorSignatureImplTest {

    private static Constructor<?> testConstructor;
    private static ConstructorSignatureImpl signature;

    /**
     * Initial setup.
     *
     * @throws Exception the exception
     */
    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        testConstructor = String.class.getConstructor(new Class[0]);
        signature = new ConstructorSignatureImpl(testConstructor);
    }

    /**
     * Test method for {@link ConstructorSignatureImpl#getConstructor()} .
     */
    @Test
    public final void testGetConstructor() {
        assertEquals(testConstructor, signature.getConstructor());
    }

    /**
     * Test method for {@link ConstructorSignatureImpl#getExceptionTypes()} .
     */
    @Test
    public final void testGetExceptionTypes() {
        Class<?>[] exceptionTypes = signature.getExceptionTypes();
        assertEquals(0, exceptionTypes.length);
    }

    /**
     * Test method for {@link ConstructorSignatureImpl#getParameterTypes()} .
     */
    @Test
    public final void testGetParameterTypes() {
        Class<?>[] parameterTypes = signature.getParameterTypes();
        assertEquals(0, parameterTypes.length);
    }

    /**
     * Test method for {@link ConstructorSignatureImpl#getDeclaringType()} .
     */
    @Test
    public final void testGetDeclaringType() {
        assertEquals(String.class, signature.getDeclaringType());
    }

    /**
     * Test method for {@link ConstructorSignatureImpl#getDeclaringTypeName()} .
     */
    @Test
    public final void testGetDeclaringTypeName() {
        assertEquals(String.class.getName(), signature.getDeclaringTypeName());
    }

    /**
     * Test method for {@link ConstructorSignatureImpl#getModifiers()} .
     */
    @Test
    public final void testGetModifiers() {
        assertEquals(Modifier.PUBLIC, signature.getModifiers());
    }

    /**
     * Test method for {@link ConstructorSignatureImpl#getName()} .
     */
    @Test
    public final void testGetName() {
        assertEquals("<init>", signature.getName());
    }

    /**
     * Test method for {@link ConstructorSignatureImpl#getParameterNames()}.
     */
    @Test
    public final void testGetParameterNames() {
        assertThrows(UnsupportedOperationException.class, () -> assertNotNull(signature.getParameterNames()));
    }

    /**
     * Test method for {@link ConstructorSignatureImpl#toString()}
     */
    @Test
    public final void testToString() {
        assertEquals("java.lang.String()", signature.toString());
    }

    /**
     * Test method for {@link ConstructorSignatureImpl#toString()}
     */
    @Test
    public final void testToShortString() {
        assertEquals("String()", signature.toShortString());
    }

    /**
     * Test method for {@link ConstructorSignatureImpl#toLongString()}
     */
    @Test
    public final void testToLongString() {
        assertEquals("public java.lang.String()", signature.toLongString());
    }

}
