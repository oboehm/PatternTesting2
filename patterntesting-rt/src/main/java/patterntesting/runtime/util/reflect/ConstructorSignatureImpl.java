/*
 * $Id: ConstructorSignatureImpl.java,v 1.9 2016/12/10 20:55:23 oboehm Exp $
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
 * (c)reated 04.04.2009 by oliver (ob@aosd.de)
 */
package patterntesting.runtime.util.reflect;

import java.lang.reflect.Constructor;

import org.aspectj.lang.reflect.ConstructorSignature;

import patterntesting.runtime.util.Converter;

/**
 * The Class ConstructorSignatureImpl.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.9 $
 * @since 04.04.2009
 */
public class ConstructorSignatureImpl implements ConstructorSignature {

	private final Constructor<?> ctor;

	/**
	 * Instantiates a new constructor signature impl.
	 *
	 * @param ctor
	 *            the ctor
	 */
	public ConstructorSignatureImpl(final Constructor<?> ctor) {
		this.ctor = ctor;
	}

	/**
	 * Gets the constructor.
	 *
	 * @return the constructor
	 * @see org.aspectj.lang.reflect.ConstructorSignature#getConstructor()
	 */
	@Override
	public Constructor<?> getConstructor() {
		return this.ctor;
	}

	/**
	 * Gets the exception types.
	 *
	 * @return the exception types
	 * @see org.aspectj.lang.reflect.CodeSignature#getExceptionTypes()
	 */
	@Override
	public Class<?>[] getExceptionTypes() {
		return this.ctor.getExceptionTypes();
	}

	/**
	 * Gets the parameter names.
	 *
	 * @return the parameter names
	 * @see org.aspectj.lang.reflect.CodeSignature#getParameterNames()
	 */
	@Override
	public String[] getParameterNames() {
		throw new UnsupportedOperationException("getParameterNames() is not supported");
	}

	/**
	 * Gets the parameter types.
	 *
	 * @return the parameter types
	 * @see org.aspectj.lang.reflect.CodeSignature#getParameterTypes()
	 */
	@Override
	public Class<?>[] getParameterTypes() {
		return this.ctor.getParameterTypes();
	}

	/**
	 * Gets the declaring type.
	 *
	 * @return the declaring type
	 *
	 * @see org.aspectj.lang.Signature#getDeclaringType()
	 */
	@Override
	public Class<?> getDeclaringType() {
		return this.ctor.getDeclaringClass();
	}

	/**
	 * Gets the declaring type name.
	 *
	 * @return the declaring type name
	 *
	 * @see org.aspectj.lang.Signature#getDeclaringTypeName()
	 */
	@Override
	public String getDeclaringTypeName() {
		return this.getDeclaringType().getName();
	}

	/**
	 * Gets the modifiers.
	 *
	 * @return the modifiers
	 * @see org.aspectj.lang.Signature#getModifiers()
	 */
	@Override
	public int getModifiers() {
		return this.ctor.getModifiers();
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 * @see org.aspectj.lang.Signature#getName()
	 */
	@Override
	public String getName() {
		return "<init>";
	}

	/**
	 * To long string.
	 *
	 * @return the string
	 * @see org.aspectj.lang.Signature#toLongString()
	 */
	@Override
	public String toLongString() {
		return this.ctor.toGenericString();
	}

	/**
	 * To short string.
	 *
	 * @return the string
	 * @see org.aspectj.lang.Signature#toShortString()
	 */
	@Override
	public String toShortString() {
		return this.getDeclaringType().getSimpleName() + "(" + Converter.toShortString(this.getParameterTypes()) + ")";
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getDeclaringTypeName() + "(" + Converter.toShortString(this.getParameterTypes()) + ")";
	}

}
