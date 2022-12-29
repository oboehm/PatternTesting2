/*
 * $Id: JoinPointMock.java,v 1.4 2016/12/18 20:19:41 oboehm Exp $
 *
 * Copyright (c) 2013 by Oliver Boehm
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
 * (c)reated 11.11.2013 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.mock;

import java.lang.reflect.Method;

import org.slf4j.LoggerFactory;
import org.slf4j.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.SourceLocation;

import patterntesting.runtime.exception.NotFoundException;
import patterntesting.runtime.util.reflect.MethodSignatureImpl;

/**
 * This is a simple mock for the {@link JoinPoint} interface. It does not
 * implement the equals method because JoinPointImpl from the AspectJ also
 * do not implement it.
 */
public final class JoinPointMock implements JoinPoint {

    private static final Logger log = LoggerFactory.getLogger(JoinPointMock.class);
    private final Object thisObject;
    private final Object[] args = new Object[0];
    private Signature signature;

    /**
     * Instantiates a new join point mock.
     *
     * @param executing the executing
     */
    public JoinPointMock(final Object executing) {
        this.thisObject = executing;
        try {
            Method method = thisObject.getClass().getMethod("toString");
            signature = new MethodSignatureImpl(method);
        } catch (NoSuchMethodException nsme) {
            log.warn("cannot get toString method for " + thisObject, nsme);
        }
    }

    /**
     * To short string.
     *
     * @return the string
     * @see org.aspectj.lang.JoinPoint#toShortString()
     */
    @Override
	public String toShortString() {
        return this.toString();
    }

    /**
     * To long string.
     *
     * @return the string
     * @see org.aspectj.lang.JoinPoint#toLongString()
     */
    @Override
	public String toLongString() {
        return this.toString();
    }

    /**
     * To string.
     *
     * @return the string
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Mock with " + this.signature;
    }

    /**
     * Gets the this.
     *
     * @return the this
     * @see org.aspectj.lang.JoinPoint#getThis()
     */
    @Override
	public Object getThis() {
        return this.thisObject;
    }

    /**
     * Gets the target.
     *
     * @return the target
     * @see org.aspectj.lang.JoinPoint#getTarget()
     */
    @Override
	public Object getTarget() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Gets the args.
     *
     * @return the args
     * @see org.aspectj.lang.JoinPoint#getArgs()
     */
    @Override
	public Object[] getArgs() {
        return this.args;
    }

    /**
     * Gets the signature.
     *
     * @return the signature
     * @see org.aspectj.lang.JoinPoint#getSignature()
     */
    @Override
	public Signature getSignature() {
        return this.signature;
    }

    /**
     * Sets the signature.
     *
     * @param element the new signature
     */
    public void setSignature(final StackTraceElement element) {
        this.setSignature(element.getMethodName());
    }

    /**
     * Sets the signature.
     *
     * @param methodName the new signature
     */
    public void setSignature(final String methodName) {
        Method method = getMethod(methodName);
        signature = new MethodSignatureImpl(method);
    }

    private Method getMethod(final String methodName) {
        Class<?> clazz = thisObject.getClass();
        try {
           return clazz.getMethod(methodName);
        } catch (NoSuchMethodException nsme) {
            Method[] methods = clazz.getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                if (methodName.equals(methods[i].getName())) {
                    return methods[i];
                }
            }
            throw new NotFoundException(
                    "method \"" + methodName + "\" not found for " + clazz, nsme);
        }
    }

    /**
     * Gets the source location.
     *
     * @return the source location
     * @see org.aspectj.lang.JoinPoint#getSourceLocation()
     */
    @Override
	public SourceLocation getSourceLocation() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Gets the kind.
     *
     * @return the kind
     * @see org.aspectj.lang.JoinPoint#getKind()
     */
    @Override
	public String getKind() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Gets the static part.
     *
     * @return the static part
     * @see org.aspectj.lang.JoinPoint#getStaticPart()
     */
    @Override
	public StaticPart getStaticPart() {
        throw new UnsupportedOperationException("not yet implemented");
    }

}
