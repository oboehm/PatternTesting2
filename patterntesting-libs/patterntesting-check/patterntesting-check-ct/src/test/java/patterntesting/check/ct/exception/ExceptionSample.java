/*
 * $Id: ExceptionSample.java,v 1.2 2016/01/06 20:47:42 oboehm Exp $
 *
 * Copyright (c) 2012 by Oliver Boehm
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
 * (c)reated 11.08.2012 by oliver (ob@oasd.de)
 */

package patterntesting.check.ct.exception;

import patterntesting.annotation.check.ct.SuppressExceptionWarning;

/**
 * This is a sample class for the ExceptionAspect.
 *
 * @author oliver (ob@aosd.de)
 * @version $Revision: 1.2 $
 * @since 1.3 (11.08.2012)
 */
public class ExceptionSample {
    
    private final String something;
    
    /**
     * Throwing an unspecific {@link Exception} should result in a warning from
     * PatternTesting Check-CT.
     * To see the warning remove the {@link SuppressExceptionWarning}.
     *
     * @throws Exception the exception
     */
    @SuppressExceptionWarning
    public ExceptionSample() throws Exception {
        this.something = "sample";
    }
    
    /**
     * Throwing an unspecific {@link RuntimeException} should result in a
     * warning from PatternTesting Check-CT.
     * To see the warning remove the {@link SuppressExceptionWarning}.
     *
     * @param text the text
     * @throws RuntimeException the runtime exception
     */
    @SuppressExceptionWarning
    public ExceptionSample(final StringBuilder text) throws RuntimeException {
        this.something = text.toString();
    }
    
    /**
     * Throwing an unspecific {@link Throwable} should result in a warning from
     * PatternTesting Check-CT.
     * To see the warning remove the {@link SuppressExceptionWarning}.
     *
     * @param text the text
     * @throws Throwable the throwable
     */
    @SuppressExceptionWarning
    public ExceptionSample(final String text) throws Throwable {
        this.something = text;
    }
    
    /**
     * Throwing a fatal {@link Error} (or any subclass of it) should not be
     * part of the signature. So this should result in a warnging from
     * PatterTesting Check-CT.
     * To see the warning remove the {@link SuppressExceptionWarning}.
     *
     * @param s1 the s1
     * @param s2 the s2
     * @throws AssertionError the assertion error
     */
    @SuppressExceptionWarning
    public ExceptionSample(final String s1, final String s2) throws AssertionError {
        this.something = s1 + s2;
    }
    
    /**
     * Gets something. This method was added to avoid the compiler warning
     * about unused <code>something</code> attribute.
     *
     * @return the something
     */
    public String getSomething() {
        return this.something;
    }
    
    /**
     * Throwing an unspecific {@link RuntimeException} should result in a
     * warning from PatternTesting Check-CT.
     * To see the warning remove the {@link SuppressExceptionWarning}.
     *
     * @throws RuntimeException the exception
     */
    @SuppressExceptionWarning
    public void throwRuntimeException() throws RuntimeException {
        throw new RuntimeException("just for fun");
    }
    
    /**
     * Throwing an unspecific {@link Exception} should result in a warning from
     * PatternTesting Check-CT.
     * To see the warning remove the {@link SuppressExceptionWarning}.
     *
     * @throws Throwable the exception
     */
    @SuppressExceptionWarning
    public void throwException() throws Throwable {
        throw new Exception("ups");
    }

    /**
     * Throwing an unspecific {@link Throwable} should result in a warning from
     * PatternTesting Check-CT.
     * To see the warning remove the {@link SuppressExceptionWarning}.
     *
     * @throws Throwable the exception
     */
    @SuppressExceptionWarning
    public void throwThrowable() throws Throwable {
        throw new Throwable("tataaa");
    }
    
    /**
     * Throwing a fatal {@link Error} (or any subclass of it) should not be
     * part of the signature. So this should result in a warnging from
     * PatterTesting Check-CT.
     * To see the warning remove the {@link SuppressExceptionWarning}.
     *
     * @throws AssertionError the error
     */
    @SuppressExceptionWarning
    public void throwAssertionError() throws AssertionError {
        throw new AssertionError("aaaaaaargh");
    }
    
    /**
     * Throwing a fatal {@link Error} (or any subclass of it) should not be
     * part of the signature. So this should result in a warnging from
     * PatterTesting Check-CT.
     * To see the warning remove the {@link SuppressExceptionWarning}.
     *
     * @throws Error the error
     */
    @SuppressExceptionWarning
    public void throwError() throws Error {
        throw new Error("internal error");
    }

}

