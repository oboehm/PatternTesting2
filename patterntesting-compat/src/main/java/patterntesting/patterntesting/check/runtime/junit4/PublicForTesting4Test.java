/*
 * $Id: PublicForTestingTest.java,v 1.4 2016/08/13 19:10:09 oboehm Exp $
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
 * (c)reated 18.03.2009 by oliver (ob@aosd.de)
 */
package patterntesting.patterntesting.check.runtime.junit4;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.*;
import patterntesting.annotation.check.runtime.PublicForTesting;
import patterntesting.runtime.util.Assertions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertTrue;

/**
 * The Class PublicForTestingTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.4 $
 * @since 18.03.2009
 */
public final class PublicForTesting4Test {

    private static final Log log = LogFactory.getLog(PublicForTesting4Test.class);

    /**
     * Tests if PublicForTesting methods can be called from setUp method.
     * And if assertions are enabled.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        assertTrue("assertions should be enabled", Assertions.ENABLED);
        Dummy.hello();
    }

    /**
     * Tests if PublicForTesting methods can be called from setUp method.
     * And if assertions are enabled.
     */
    @Before
    public void setUp() {
        assertTrue("assertions should be enabled", Assertions.ENABLED);
        Dummy.hello();
    }

    /**
     * Tests if PublicForTesting methods can be called from tearDown method.
     */
    @After
    public void tearDown() {
        Dummy.hello();
    }

    /**
     * Tests if PublicForTesting methods can be called from tearDown method.
     */
    @AfterClass
    public static void tearDownAfterClass() {
        Dummy.hello();
    }

    /**
     * Test method for a test call of a method marked as
     * "PublicForTesting".
     */
    @Test
    public void testCallPublicForTestingMethod() {
        log.info("calling @PublicForTesting method...");
        Dummy.hello();
    }

    /**
     * Test call direct via reflection.
     *
     * @throws NoSuchMethodException the no such method exception
     * @throws IllegalAccessException the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     */
    @Test
    public void testCallDirectViaReflection() throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        Method hello = Dummy.class.getMethod("hello");
        log.info("calling " + hello + "...");
        hello.invoke(null, (Object[]) null);
    }

    /**
     * Test call indirect via reflection.
     *
     * @throws NoSuchMethodException the no such method exception
     * @throws IllegalAccessException the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     */
    @Test
    public void testCallIndirectViaReflection() throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        Method main = this.getClass().getMethod("main", String[].class);
        log.info("calling " + main + "...");
        Object[] args = { new String[0] };
        main.invoke(null, args);
    }

    /**
     * This method is not allowed to call Dummy.hello(), so this method should
     * fail.
     *
     * @param args the arguments
     */
    public static void main(final String[] args) {
        log.info("calling @PublicForTesting method...");
        PublicForTesting4Test.helloWorld();
        Dummy.hello();
    }

    /**
     * This method should fail if it is not (directly or indirectly) called
     * by a test method.
     */
    @PublicForTesting
    public static void helloWorld() {
        log.info("hello world");
    }

}
