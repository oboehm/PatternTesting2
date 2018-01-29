/*
 * $Id: BinarySerializer.java,v 1.2 2016/12/10 20:55:18 oboehm Exp $
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
 * (c)reated 30.11.2013 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.io;

import java.io.*;

/**
 * This is the default serializer which uses the default mechanism proved by the
 * JDK. For this reason the serialized object must be {@link Serializable}.
 *
 * @author oliver
 * @since 1.4 (30.11.2013)
 */
public final class BinarySerializer extends AbstractSerializer {

	/**
	 * Creates the {@link ObjectInputStream} that deserializes a stream of
	 * objects from an InputStream using the default mechnism of Java. For this
	 * reason the serialized objects must be {@link Serializable}.
	 *
	 * @param in
	 *            the input stream
	 * @return the object input stream
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Override
	public ObjectInputStream createObjectInputStream(final InputStream in) throws IOException {
		return new ObjectInputStream(in);
	}

	/**
	 * Creates the {@link ObjectOutputStream} that serializees a stream of
	 * objects to the {@link OutputStream} using the default mechnism of Java.
	 * For this reason the serialized objects must be {@link Serializable}.
	 *
	 * @param out
	 *            the out
	 * @return the object output stream
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @see AbstractSerializer#createObjectOutputStream(java.io.OutputStream)
	 */
	@Override
	public ObjectOutputStream createObjectOutputStream(final OutputStream out) throws IOException {
		return new ObjectOutputStream(out);
	}

}
