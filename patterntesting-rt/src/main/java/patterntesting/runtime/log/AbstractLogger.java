/*
 * $Id: AbstractLogger.java,v 1.11 2016/12/23 20:05:01 oboehm Exp $
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
 * (c)reated 07.09.2013 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.log;

import java.io.*;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.*;

/**
 * This is a common super class for all classes which do a kind of logging. It
 * provides an {@link OutputStream} and guarantees that this stream is closed at
 * least at shutdown.
 *
 * @author oliver
 * @since 1.3.1 (07.09.2013)
 */
public abstract class AbstractLogger extends Thread implements AutoCloseable {

	private final static Logger LOG = LogManager.getLogger(AbstractLogger.class);
	private final OutputStream logStream;

	/**
	 * Instantiates a new abstract logger.
	 *
	 * @param ostream
	 *            the ostream
	 */
	protected AbstractLogger(final OutputStream ostream) {
		this.logStream = ostream;
		Runtime.getRuntime().addShutdownHook(this);
		LOG.debug("{} is registered as shutdown hook for closing {}.", this, this.logStream);
	}

	/**
	 * Creates a file in the temp directory. If this was not successful a file
	 * <code>prefix + suffix</code> is returned as result.
	 *
	 * @param prefix
	 *            the prefix
	 * @param suffix
	 *            the suffix
	 * @return the file
	 */
	protected static File createTempLogFile(final String prefix, final String suffix) {
		try {
			return File.createTempFile(prefix, suffix);
		} catch (IOException ioe) {
			LOG.info("Cannot create temporary log file.", ioe);
			return new File(prefix + suffix);
		}
	}

	/**
	 * Gets the log stream.
	 *
	 * @return the logStream
	 */
	protected final OutputStream getLogStream() {
		return logStream;
	}

	/**
	 * This method is called at shutdown to close the open stream. Otherwise the
	 * closing "&lt;/object-stream&gt;" tag in the generated file would be
	 * missing (it is generated by the used XStream library).
	 *
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		IOUtils.closeQuietly(this.logStream);
		LOG.debug("{} is closed.", this.logStream);
	}

	/**
	 * Closes the stream with the logged objects.
	 */
	public void close() {
		IOUtils.closeQuietly(this.logStream);
		Runtime.getRuntime().removeShutdownHook(this);
		LOG.debug("{} is closed and {} removed as shutdown hook.", this.logStream, this);
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " with " + this.logStream;
	}

}