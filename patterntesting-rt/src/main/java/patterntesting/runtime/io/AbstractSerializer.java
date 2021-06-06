/*
 * $Id: AbstractSerializer.java,v 1.8 2016/12/18 20:19:36 oboehm Exp $
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

import clazzfish.monitor.ClasspathMonitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patterntesting.runtime.exception.DetailedInvalidClassException;

import java.io.*;
import java.net.URI;

/**
 * This is the common superclass for all Serializer casses in PatternTesting.
 * This class was introduced to be able to abstract from the used XStream
 * library in the log package. This allows us to declare the xstream library as
 * "optional" in the POM file.
 *
 * @author oliver
 * @since 1.4 (30.11.2013)
 */
public abstract class AbstractSerializer {

	private static final Logger LOG = LogManager.getLogger(AbstractSerializer.class);
	private static final AbstractSerializer instance;

	static {
		if (isXStreamAvailable()) {
			instance = new XStreamSerializer();
		} else {
			instance = new BinarySerializer();
		}
	}

	private static boolean isXStreamAvailable() {
		String xstreamClassname = "com.thoughtworks.xstream.XStream";
		URI xstreamURI = ClasspathMonitor.getInstance().whichClass(xstreamClassname);
		if (xstreamURI == null) {
			LOG.debug("{} not found in classpath for serialization.", xstreamClassname);
			return false;
		} else {
			LOG.debug("{} found in {} for serialization.", xstreamClassname, xstreamURI);
			return true;
		}
	}

	/**
	 * This static method returns an instance of the default serializer.
	 * Normally this is the {@link XStreamSerializer} if the xstream library is
	 * detected in the classpath. If not the {@link BinarySerializer} (which
	 * requires the serialized objects to be {@link Serializable}) will be used
	 * as fallback.
	 *
	 * @return the default serializer
	 */
	public static AbstractSerializer getInstance() {
		return instance;
	}

	/**
	 * Creates the {@link ObjectInputStream} that deserializes a stream of
	 * objects from an InputStream.
	 *
	 * @param in
	 *            the input stream
	 * @return the object input stream
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public abstract ObjectInputStream createObjectInputStream(InputStream in) throws IOException;

	/**
	 * Creates the {@link ObjectOutputStream} that serializees a stream of
	 * objects to the {@link OutputStream}.
	 *
	 * @param out
	 *            the output stream
	 * @return the object output stream
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public abstract ObjectOutputStream createObjectOutputStream(OutputStream out) throws IOException;

	/**
	 * Loads a single ojbect from the given file.
	 *
	 * @param file
	 *            the file
	 * @return the object
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public Object load(final File file) throws IOException {
		InputStream istream = new FileInputStream(file);
		try {
			return load(istream);
		} finally {
			istream.close();
		}
	}

	/**
	 * Loads a single ojbect from the given stream.
	 *
	 * @param in
	 *            the in
	 * @return the object
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public Object load(final InputStream in) throws IOException {
		ObjectInputStream objStream = this.createObjectInputStream(in);
		try {
			return objStream.readObject();
		} catch (ClassNotFoundException ex) {
			throw new DetailedInvalidClassException("cannot load from " + in, ex);
		}
	}

	/**
	 * Saves a single object to the given file.
	 *
	 * @param object
	 *            the object
	 * @param file
	 *            the file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void save(final Object object, final File file) throws IOException {
		OutputStream ostream = new FileOutputStream(file);
		try {
			save(object, ostream);
			ostream.flush();
		} finally {
			ostream.close();
		}
	}

	/**
	 * Saves a single object to the given stream.
	 *
	 * @param object
	 *            the object
	 * @param out
	 *            the out
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void save(final Object object, final OutputStream out) throws IOException {
		ObjectOutputStream objStream = this.createObjectOutputStream(out);
		objStream.writeObject(object);
		objStream.flush();
	}

}
