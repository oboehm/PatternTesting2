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
import java.util.*;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.*;
import org.aspectj.lang.JoinPoint;

import patterntesting.runtime.io.*;
import patterntesting.runtime.util.*;

/**
 * This is the counterpart to {@link ObjectRecorder} class. It can be used to
 * replay recorded objects.
 *
 * @author oliver (boehm@javatux.de)
 * @since 1.3.1 (31.08.2013)
 */
public final class ObjectPlayer {

	private static final Logger LOG = LogManager.getLogger(ObjectPlayer.class);
	private static final AbstractSerializer SERIALIZER = AbstractSerializer.getInstance();
	private final Map<String, List<Object>> loggedJoinpoints = new HashMap<>();

	/**
	 * Instantiates a new object player. The logged objects will be loaded from
	 * the given log file.
	 *
	 * @param logFile
	 *            the log file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public ObjectPlayer(final File logFile) throws IOException {
		load(logFile);
	}

	/**
	 * Gets the return value for the given joinpoint. The return values are
	 * returned in the same order as the were recorded. I.e. if for the same
	 * joinpoint first the value "1" and then the value "2" is recorded you'll
	 * get first "1", then "2" as the return value.
	 *
	 * @param joinPoint
	 *            the join point
	 * @return the return value
	 */
	public Object getReturnValue(final JoinPoint joinPoint) {
		String statement = JoinPointHelper.getAsLongString(joinPoint);
		if (SignatureHelper.hasReturnType(joinPoint.getSignature())) {
			return getReturnValue(statement);
		}
		LOG.debug("REPLAY: {}", statement);
		return null;
	}

	private Object getReturnValue(final String joinPoint) {
		Object returnValue = null;
		if (loggedJoinpoints.containsKey(joinPoint)) {
			List<Object> loggedReturnValues = loggedJoinpoints.get(joinPoint);
			returnValue = loggedReturnValues.get(0);
			if (loggedReturnValues.size() > 1) {
				loggedReturnValues.remove(0);
			}
		} else {
			LOG.trace("Not recorded: {}", joinPoint);
		}
		LOG.debug("REPLAY: {} = {}", joinPoint, returnValue);
		return returnValue;
	}

	/**
	 * If you want to use an always recored object log you can load it with this
	 * method. This allows you to use different files for logging and loading of
	 * recorded objects.
	 *
	 * @param logFile
	 *            the log file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void load(final File logFile) throws IOException {
		try (FileInputStream istream = new BetterFileInputStream(logFile)) {
			if (FilenameUtils.isExtension(logFile.getName(), "gz")) {
				LOG.debug("Loading \"{}\" as compressed file...", logFile);
				load(new GZIPInputStream(istream));
			} else {
				LOG.debug("Loading \"{}\" as normal file...", logFile);
				load(istream);
			}
			LOG.debug("Loading \"{}\" sucessfully finished.", logFile);
		}
	}

	/**
	 * If you want to use an always recorded object log you can load it with
	 * this method. This allows you to use different files for logging and
	 * loading of recorded objects.
	 *
	 * @param istream
	 *            the istream
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void load(final InputStream istream) throws IOException {
		ObjectInputStream oistream = SERIALIZER.createObjectInputStream(istream);
		try {
			load(oistream);
			LOG.debug("{} joinpoint(s) are read from {}.", this.loggedJoinpoints.size(), istream);
		} catch (ClassNotFoundException cnfe) {
			throw new IOException("unknown object in " + istream, cnfe);
		} finally {
			oistream.close();
		}
	}

	private void load(ObjectInputStream oistream) throws ClassNotFoundException {
		try {
			while (true) {
				String jp = (String) oistream.readObject();
				Object retValue = oistream.readObject();
				logToMemory(jp, retValue);
				if (retValue == null) {
					LOG.debug("No more objects in {} available.", oistream);
					break;
				}
			}
		} catch (IOException ioe) {
			LOG.debug("Reading of {} stopped ({}).", oistream, ioe.getMessage());
			LOG.trace("Details:", ioe);
		}
	}

	private void logToMemory(final String joinPoint, final Object returnValue) {
		List<Object> loggedReturnValues = new ArrayList<>();
		if (loggedJoinpoints.containsKey(joinPoint)) {
			loggedReturnValues = loggedJoinpoints.get(joinPoint);
		}
		loggedReturnValues.add(returnValue);
		loggedJoinpoints.put(joinPoint, loggedReturnValues);
		if (LOG.isTraceEnabled()) {
			LOG.trace("logged (" + loggedReturnValues.size() + "): {} = {}", joinPoint,
					Converter.toString(returnValue));
		}
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return loggedJoinpoints.hashCode();
	}

	/**
	 * Equals.
	 *
	 * @param obj
	 *            the obj
	 * @return true, if successful
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof ObjectPlayer)) {
			return false;
		}
		ObjectPlayer other = (ObjectPlayer) obj;
		return isEquals(this.loggedJoinpoints, other.loggedJoinpoints);
	}

	@SuppressWarnings("deprecation")
	private static boolean isEquals(final Map<String, List<Object>> m1, final Map<String, List<Object>> m2) {
		if (m1.size() != m2.size()) {
			return false;
		}
		for (Entry<String, List<Object>> entry : m2.entrySet()) {
			if (!ObjectUtils.equals(m1.get(entry.getKey()), entry.getValue())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " with " + loggedJoinpoints.size() + " joinpoint(s).";
	}

}
