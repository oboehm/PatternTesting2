/*
 * $Id: LineReader.java,v 1.8 2016/12/18 20:19:36 oboehm Exp $
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
 * (c)reated 13.11.2013 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.io;

import java.io.*;

import org.slf4j.LoggerFactory;
import org.slf4j.*;

/**
 * If extends {@link BufferedReader} but provides you the line number in which
 * the reader is reading.
 *
 * @author oliver
 * @since 1.4 (13.11.2013)
 */
public final class LineReader extends BufferedReader {

	private static final Logger LOG = LoggerFactory.getLogger(LineReader.class);
	private int lineNumber = 0;

	/**
	 * Instantiates a new line reader.
	 *
	 * @param in
	 *            the in
	 */
	public LineReader(final Reader in) {
		super(in);
	}

	/**
	 * Instantiates a new line reader.
	 *
	 * @param in
	 *            the in
	 * @param sz
	 *            the sz
	 */
	public LineReader(final Reader in, final int sz) {
		super(in, sz);
	}

	/**
	 * Gets the line number.
	 *
	 * @return the line number
	 */
	public int getLineNumber() {
		return this.lineNumber;
	}

	/**
	 * Read.
	 *
	 * @return the int
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @see java.io.BufferedReader#read()
	 */
	@Override
	public int read() throws IOException {
		int ch = super.read();
		if (ch == '\n') {
			this.lineNumber++;
		}
		return ch;
	}

	/**
	 * Read.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @return the int
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @see java.io.BufferedReader#read(char[], int, int)
	 */
	@Override
	public int read(final char[] arg0, final int arg1, final int arg2) throws IOException {
		throw new UnsupportedOperationException("not yet implemented");
	}

	/**
	 * Read line.
	 *
	 * @return the string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @see java.io.BufferedReader#readLine()
	 */
	@Override
	public String readLine() throws IOException {
		lineNumber++;
		return super.readLine();
	}

	/**
	 * Reset.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @see java.io.BufferedReader#reset()
	 */
	@Override
	public void reset() throws IOException {
		super.reset();
		lineNumber = 0;
	}

	/**
	 * Skip.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the long
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @see java.io.BufferedReader#skip(long)
	 */
	@Override
	public long skip(final long arg0) throws IOException {
		throw new UnsupportedOperationException("not supported");
	}

	/**
	 * Skip lines.
	 *
	 * @param n
	 *            the number of lines to skip
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void skipLines(final int n) throws IOException {
		LOG.trace("Skipping {} line(s).", n);
		for (int i = 0; i < n; i++) {
			String line = this.readLine();
			if (line == null) {
				break;
			}
			LOG.trace(line);
		}
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (this.lock == null) {
			return "unsynchronized " + this.getClass().getSimpleName();
		}
		String s = this.lock.toString();
		if (s.contains("@")) {
			return this.getClass().getSimpleName() + "@" + Integer.toString(this.hashCode(), Character.MAX_RADIX);
		}
		return s;
	}

}
