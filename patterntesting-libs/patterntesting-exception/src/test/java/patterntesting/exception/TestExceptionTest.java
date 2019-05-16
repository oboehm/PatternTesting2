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
 * (c)reated 03.03.2009 by oliver (ob@oasd.de)
 */
package patterntesting.exception;


import java.io.*;
import java.net.SocketException;

import org.junit.jupiter.api.Test;
import patterntesting.annotation.exception.TestException;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * The Class TestExceptionTest.
 * <p>
 * You may ask why the exceptionFactory attribute (which is static) is used
 * for synchronization. The reason is that the ExceptionFactory is a singleton.
 * So all settings are global and could affect other tests. These could
 * confuses other threads in a mulit-threading environment.
 * This is also the reason why all stuff is set up in the test methods itself
 * and not in a special setup method.
 * </p>
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.11 $
 * @since 03.03.2009
 */
public final class TestExceptionTest extends AbstractExceptionTest implements Serializable {

    private static final long serialVersionUID = 20100709L;
    private static final ExceptionFactory exceptionFactory = ExceptionFactory
            .getInstance();

    /**
	 * Test expected exception.
	 */
    @Test
    public void testExpectedException() {
	    synchronized (exceptionFactory) {
	        activateOnce(InterruptedException.class);
	        assertThrows(InterruptedException.class, () -> mayThrowException());
        }
    }

    /**
     * Test expected exception.
     */
    @Test
    public void testExpectedSubexception() {
        synchronized (exceptionFactory) {
            activateOnce(SocketException.class);
            assertThrows(SocketException.class, () -> mayThrowIOException(false));
        }
    }

    @TestException
    private void mayThrowException() throws InterruptedException {
        Thread.sleep(1);
    }

    @TestException
    private void mayThrowIOException(final boolean yes) throws IOException {
        if (yes) {
            throw new IOException("have a nice day");
        }
    }

    /**
     * Test method for {@link ExceptionFactory#setScope(Class)}.
     * Because we set the scope to this test class here we expected
     * an InterruptedException.
     */
    @Test
    public void testScope() {
        synchronized (exceptionFactory) {
            activateOnce(InterruptedException.class, this.getClass());
            assertThrows(InterruptedException.class, () -> mayThrowException());
        }
    }

    /**
     * Test method for {@link ExceptionFactory#setScope(Class)}.
     * Because we set the scope to another class but not this test class
     * we expect no InteruptedException
     *
     * @throws InterruptedException should not happen
     */
    @Test
    public void testOutOfScope() throws InterruptedException {
        synchronized (exceptionFactory) {
            activateOnce(InterruptedException.class, String.class);
            mayThrowException();
        }
    }

    /**
     * Test method for {@link ExceptionFactory#resetScope()}.
     * Because we reset the scope to "all classes" we expected
     * an InterruptedException.
     */
    @Test
    public void testResetScope() {
        synchronized (exceptionFactory) {
            activateOnce(InterruptedException.class, String.class);
            exceptionFactory.resetScope();
            assertThrows(InterruptedException.class, () -> mayThrowException());
        }
    }

    /**
     * Test method for {@link ExceptionFactory#setScope(Class)}.
     * Because we set the scope to a super class of this test class here
     * we expected an InterruptedException.
     */
    @Test
    public void testSuperclassScope() {
        synchronized (exceptionFactory) {
            activateOnce(InterruptedException.class, AbstractExceptionTest.class);
            assertThrows(InterruptedException.class, () -> mayThrowException());
        }
    }

    /**
     * Test method for {@link ExceptionFactory#setScope(Class)}.
     * Because we set the scope to an interface of this test class here
     * we expected an InterruptedException.
     */
    @Test
    public void testInterfaceScope() {
        synchronized (exceptionFactory) {
            activateOnce(InterruptedException.class, Serializable.class);
            assertThrows(InterruptedException.class, () -> mayThrowException());
        }
    }

    private void activateOnce(final Class<? extends Throwable> exception) {
        exceptionFactory.reset();
        exceptionFactory.setFire(exception);
        exceptionFactory.activateOnce();
    }

    private void activateOnce(final Class<? extends Throwable> exception, final Class<?> target) {
        activateOnce(exception);
        exceptionFactory.setScope(target);
    }

}
