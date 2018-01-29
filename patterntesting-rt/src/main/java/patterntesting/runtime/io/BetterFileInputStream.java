/*
 * Copyright (c) 2013 by Oli B.
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
 * (c)reated 14.09.2013 by Oli B. (boehm@javatux.de)
 */

package patterntesting.runtime.io;

import java.io.*;

/**
 * Whenever you need a {@link FileInputStream} and want a better toString
 * implementation you can use the class BetterFileInputStream. "Better" means
 * that you see the name if the input file in the log which is much more helpful
 * than the default implementation of {@link Object#toString()}, especially for
 * logging and debugging.
 *
 * @author oliver (boehm@javatux.de)
 * @see FileInputStream
 * @since 1.3.1 (14.09.2013)
 */
public final class BetterFileInputStream extends FileInputStream {

	private final File file;

	/**
	 * Instantiates a new better file input stream.
	 *
	 * @param name
	 *            the name
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @see FileInputStream#FileInputStream(String)
	 */
	public BetterFileInputStream(final String name) throws FileNotFoundException {
		super(name);
		this.file = new File(name);
	}

	/**
	 * Instantiates a new better file input stream.
	 *
	 * @param file
	 *            the file
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @see FileInputStream#FileInputStream(File)
	 */
	public BetterFileInputStream(final File file) throws FileNotFoundException {
		super(file);
		this.file = file;
	}

	/**
	 * This toString implementation reports the name of the input file which is
	 * much more helpful than the default implementation of
	 * {@link Object#toString()}, especially for logging and debugging.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "input stream for file \"" + this.file + '"';
	}

}
