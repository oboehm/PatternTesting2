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
 * (c)reated 31.08.2013 by Oli B. (boehm@javatux.de)
 */

package patterntesting.runtime.log;

import java.io.*;

import org.apache.commons.io.IOUtils;
import org.slf4j.*;
import org.aspectj.lang.JoinPoint;

import patterntesting.annotation.check.runtime.NullArgsAllowed;
import patterntesting.runtime.io.*;
import patterntesting.runtime.util.JoinPointHelper;

/**
 * This class allows you to record objects. Later you can replay the logged
 * objects, e.g. to simulate external you can:
 * <ul>
 * <li>record the call to the external system,</li>
 * <li>replay the recorded objects.</li>
 * </ul>
 * You must write your own aspect to realize it.
 * <p>
 * If you want a pure java solution you can try
 * <a href="http://jtor.sourceforge.net/">ThOR</a>, the Java Test Object
 * Recorder.
 * </p>
 *
 * @author oliver (boehm@javatux.de)
 * @since 1.3.1 (31.08.2013)
 */
public class ObjectRecorder extends AbstractLogger {

	private static final Logger LOG = LoggerFactory.getLogger(ObjectRecorder.class);
	private static final AbstractSerializer serializer = AbstractSerializer.getInstance();
	private final ObjectOutputStream objStream;

	/**
	 * Instantiates a new object recorder. The logged objects are stored in a
	 * temporary file.
	 */
	public ObjectRecorder() {
		this(createTempLogFile("objects", ".rec"));
	}

	/**
	 * Instantiates a new object logger. The logged objects will be stored in
	 * the given log file.
	 *
	 * @param logFile
	 *            the log file
	 */
	public ObjectRecorder(final File logFile) {
		this(ExtendedFile.createOutputStreamFor(logFile));
		LOG.info("Objects will be recorded to \"{}\".", logFile);
	}

	/**
	 * Instantiates a new object logger to the given stream.
	 *
	 * @param ostream
	 *            the ostream
	 */
	public ObjectRecorder(final OutputStream ostream) {
		super(ostream);
		try {
			this.objStream = serializer.createObjectOutputStream(ostream);
		} catch (IOException ioe) {
			throw new IllegalArgumentException("cannot use " + ostream, ioe);
		}
	}

	/**
	 * Closes the stream with the logged objects.
	 */
	@Override
	public void close() {
		IOUtils.closeQuietly(this.objStream);
		super.close();
	}

	/**
	 * Both things are logged with this method: the call of a method (joinPoint)
	 * and the return value of this method. Constructors or method of type
	 * 'void' are not recorded because the have no return value.
	 * <p>
	 * Because the given joinPoint cannot be used as key for a map in
	 * {@link ObjectPlayer} it is saved as string. As a side effect this will
	 * speedup the serialization stuff and shorten the generated record file.
	 * </p>
	 *
	 * @param joinPoint
	 *            the joinpoint
	 * @param returnValue
	 *            the return value
	 */
	@NullArgsAllowed
	public void log(final JoinPoint joinPoint, final Object returnValue) {
		String statement = JoinPointHelper.getAsLongString(joinPoint);
		try {
			save(statement, returnValue);
		} catch (IOException ioe) {
			LOG.debug("Logging to {} failed:", objStream, ioe);
			LOG.info("{} = {}", statement, returnValue);
		}
	}

	/**
	 * Saves the given joinPoint / returnValue pair to the log stream.
	 *
	 * @param joinPoint
	 *            the join point
	 * @param returnValue
	 *            the return value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	protected void save(final String joinPoint, final Object returnValue) throws IOException {
		LOG.debug("RECORD: {} = {}", joinPoint, returnValue);
		objStream.writeObject(joinPoint);
		objStream.writeObject(returnValue);
		objStream.flush();
	}

}
