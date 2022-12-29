/*
 * $Id: CrazyCookie.java,v 1.7 2016/12/21 22:09:02 oboehm Exp $
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
 * (c)reated 04.07.2009 by oliver
 */
package patterntesting.sample;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.*;
import java.util.Date;

/**
 * This is an example for a class with a dynamic serialVersionUID. You should
 * never do such a thing in a real application because you can't transfer such
 * a crazy object over the network or load it back from disk.
 * If you try it you will get something like
 * <i>java.io.InvalidClassException: patterntesting.sample.CrazyCookie;
 * local class incompatible: stream classdesc serialVersionUID = 8540013509864548425,
 * local class serialVersionUID = -5607627437350045997</i>
 * as exception.
 * <p>
 * This is an example where PatternTesting can't help you to find this abnormal
 * initialization of serialVersionUID. So don't do this in your real life.
 * Also other tools like FindBugs would not find it.
 * </p>
 *
 * @author oliver
 * @since 04.07.2009
 */
public final class CrazyCookie implements Serializable {

	/** argh, you should use a constant here!!! */
	private static final long serialVersionUID = System.currentTimeMillis();
	//private static final long serialVersionUID = 20090706;
	private static final Logger log = LoggerFactory.getLogger(CrazyCookie.class);
	private final File file = createTempFile();
	private final Date created = new Date();

	private static File createTempFile() {
		try {
			return File.createTempFile("cookie", ".bin");
		} catch (IOException ioe) {
			log.info("using 'cookie.bin' as temp file", ioe);
			return new File("cookie.bin");
		}
	}

	/**
	 * Gets the file.
	 *
	 * @return the temporary file where the cookie will be stored
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Do you want to know when the cookie was created?.
	 *
	 * @return when the cookie was created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * Store the cookie to a tempory file.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void store() throws IOException {
		try (OutputStream ostream = new FileOutputStream(file)) {
			ObjectOutput output = new ObjectOutputStream(ostream);
			output.writeObject(this);
		}
		log.debug("{} stored to {}", this, file);
	}

	/**
	 * Load.
	 *
	 * @param f the file to ble loaded
	 * @return the crazy cookie
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static CrazyCookie load(final File f) throws IOException {
		try (InputStream istream = new FileInputStream(f);
			 ObjectInput input = new ObjectInputStream(istream)) {
			return (CrazyCookie) input.readObject();
		} catch (ClassNotFoundException | ClassCastException e) {
			throw new IOException("unexpected class in " + f, e);
		}
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + created + ")";
	}

	/**
	 * Equals.
	 *
	 * @param other the other
	 * @return true, if successful
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(final Object other) {
		if (other instanceof CrazyCookie) {
			return equals((CrazyCookie) other);
		} else {
            log.debug("can't compare " + this + " with " + other);
			return false;
		}
	}

	/**
	 * Equals.
	 *
	 * @param other the other
	 * @return true, if successful
	 */
	public boolean equals(final CrazyCookie other) {
		return this.created.equals(other.created);
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getCreated().hashCode();
	}

}
