/*
 * $Id: JoinPointStaticPartMock.java,v 1.6 2016/12/18 20:19:41 oboehm Exp $
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

import org.aspectj.lang.JoinPoint.StaticPart;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.SourceLocation;

/**
 * This is a simple mock for the {@link StaticPart} interface.
 *
 * @author oliver
 * @version $Revision: 1.6 $
 * @since 1.6 (03.06.2015)
 */
public final class JoinPointStaticPartMock implements StaticPart {

    private final Signature signature;

    /**
     * Instantiates a new join point static part mock.
     */
    public JoinPointStaticPartMock() {
        this("???");
    }

    /**
     * Instantiates a new join point static part mock.
     *
     * @param methodName the method name
     */
    public JoinPointStaticPartMock(final String methodName) {
        this(new SignatureMock(methodName));
    }

    /**
     * Instantiates a new join point static part mock.
     *
     * @param signature the signature
     */
    public JoinPointStaticPartMock(final Signature signature) {
        this.signature = signature;
    }

    /**
     * Gets the signature.
     *
     * @return the signature
     * @see org.aspectj.lang.JoinPoint.StaticPart#getSignature()
     */
    @Override
	public Signature getSignature() {
        return this.signature;
    }

    /**
     * Gets the source location.
     *
     * @return the source location
     * @see org.aspectj.lang.JoinPoint.StaticPart#getSourceLocation()
     */
    @Override
	public SourceLocation getSourceLocation() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Gets the kind.
     *
     * @return the kind
     * @see org.aspectj.lang.JoinPoint.StaticPart#getKind()
     */
    @Override
	public String getKind() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Gets the id.
     *
     * @return the id
     * @see org.aspectj.lang.JoinPoint.StaticPart#getId()
     */
    @Override
	public int getId() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * To short string.
     *
     * @return the string
     * @see org.aspectj.lang.JoinPoint.StaticPart#toShortString()
     */
    @Override
	public String toShortString() {
        return this.signature.toShortString();
    }

    /**
     * To long string.
     *
     * @return the string
     * @see org.aspectj.lang.JoinPoint.StaticPart#toLongString()
     */
    @Override
	public String toLongString() {
        return this.signature.toLongString();
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

