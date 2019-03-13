/*
 * $Id: ExceptionFactoryTest.java,v 1.13 2016/12/18 21:57:35 oboehm Exp $
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
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package patterntesting.exception;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.SocketException;

import org.junit.*;
import org.apache.logging.log4j.*;

import patterntesting.runtime.jmx.MBeanHelper;

/**
 * The Class ExceptionFactoryTest.
 */
public final class ExceptionFactoryTest extends AbstractExceptionTest {

    private static final Logger log = LogManager.getLogger(ExceptionFactoryTest.class);
    private final ExceptionFactory factory = ExceptionFactory.getInstance();

    /**
     * Setup.
     */
    @Before
    public void setUp() {
        factory.reset();
    }

    /**
     * Test provoke.
     */
    @Test(expected = InterruptedException.class)
    public void testProvoke() {
        synchronized (factory) {
            factory.activate();
            assertTrue(factory.isActive());
            factory.provoke(InterruptedException.class);            
        }
    }

    /**
     * Test deactivate.
     */
    @Test
    public void testDeactivate() {
        synchronized (factory) {
            factory.deactivate();
            assertFalse(factory.isActive());
            factory.provoke(InterruptedException.class);
        }
    }

    /**
     * Test activate once.
     */
    @Test
    public void testActivateOnce() {
        synchronized (factory) {
            factory.deactivate();
            factory.activateOnce();
            try {
                factory.provoke(ClassCastException.class);
                fail("ClassCastException expected");
            } catch (ClassCastException expected) {
                log.info("caught " + expected);
            }
            factory.provoke(ClassNotFoundException.class);
        }
    }

    /**
     * Tests setLimitedTo with a class as parameter.
     */
    @Test
    public void testSetFire() {
        synchronized (factory) {
            factory.setFire(ClassCastException.class);
            factory.provoke(InterruptedException.class);
        }
    }

    /**
     * Tests setLimitedTo with a string as parameter.
     * @throws ClassNotFoundException the class not found exception
     */
    @Test
    public void testSetFireString() throws ClassNotFoundException {
        synchronized (factory) {
            factory.setFire("java.lang.ClassCastException");
            factory.provoke(InterruptedException.class);
        }
    }

    /**
     * Tests provokeOneOf with exact match.
     */
	@Test(expected = ClassCastException.class)
    public void testProvokeOneOfExactMatch() {
        Class<? extends Throwable>[] throwables = getThrowableArrayWith(ClassCastException.class);
		synchronized (factory) {
		    factory.activate();
	        factory.setFire(ClassCastException.class);
	        factory.provokeOneOf(throwables);
        }
	}

    /**
     * Tests provokeOneOf with a subclass of the fired exception.
     */
    @Test(expected = SocketException.class)
    public void testProvokeOneOf() {
        Class<? extends Throwable>[] throwables = getThrowableArrayWith(IOException.class);
        synchronized (factory) {
            factory.activate();
            factory.setFire(SocketException.class);
            factory.provokeOneOf(throwables);
        }
    }

    private Class<? extends Throwable>[] getThrowableArrayWith(
            Class<? extends Throwable> t) {
        @SuppressWarnings("unchecked")
        Class<? extends Throwable>[] throwables = (Class<? extends Throwable>[]) Array
				.newInstance(Class.class, 2);
		throwables[0] = InterruptedException.class;
		throwables[1] = t;
        return throwables;
    }
    
    /**
     * Test method for {@link ExceptionFactory#setScope(String)}.
     */
    @Test
    public void testSetScope() {
        String classname = this.getClass().getName();
        factory.setScope(classname);
        assertEquals(classname, factory.getScope());
    }

    /**
     * Test method for {@link ExceptionFactory#resetScope()}.
     */
    @Test
    public void testResetScope() {
        factory.resetScope();
        assertEquals("all classes", factory.getScope());
    }

    /**
     * Test MBean registration.
     */
    @Test
    public void testMBeanRegistration() {
    	assertTrue("factory is not registered", MBeanHelper.isRegistered(factory));
    }
    
}
