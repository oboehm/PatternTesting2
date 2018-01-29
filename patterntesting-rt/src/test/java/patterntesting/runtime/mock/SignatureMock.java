/*
 * $Id: SignatureMock.java,v 1.3 2016/12/10 20:55:24 oboehm Exp $
 *
 * Copyright (c) 2015 by Oliver Boehm
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
 * (c)reated 03.06.2015 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.mock;

import org.aspectj.lang.Signature;

/**
 * A simple mock for a {@link Signature}.
 *
 * @author oliver
 * @version $Revision: 1.3 $
 * @since 1.6 (03.06.2015)
 */
public final class SignatureMock implements Signature {

    private final String name;

    /**
     * Instantiates a new signature mock.
     *
     * @param methodName the method name
     */
    public SignatureMock(final String methodName) {
        this.name = methodName;
    }

    /**
     * Gets the name.
     *
     * @return the name
     * @see org.aspectj.lang.Signature#getName()
     */
    @Override
	public String getName() {
        return this.name;
    }

    /**
     * Gets the modifiers.
     *
     * @return the modifiers
     * @see org.aspectj.lang.Signature#getModifiers()
     */
    @Override
	public int getModifiers() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Gets the declaring type.
     *
     * @return the declaring type
     * @see org.aspectj.lang.Signature#getDeclaringType()
     */
    @Override
	public Class<?> getDeclaringType() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Gets the declaring type name.
     *
     * @return the declaring type name
     * @see org.aspectj.lang.Signature#getDeclaringTypeName()
     */
    @Override
	public String getDeclaringTypeName() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * To short string.
     *
     * @return the string
     * @see org.aspectj.lang.Signature#toShortString()
     */
    @Override
	public String toShortString() {
        return this.name + "(..)";
    }

    /**
     * To long string.
     *
     * @return the string
     * @see org.aspectj.lang.Signature#toLongString()
     */
    @Override
	public String toLongString() {
        return this.toShortString();
    }

    /**
     * To string.
     *
     * @return the string
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.toShortString();
    }

}

