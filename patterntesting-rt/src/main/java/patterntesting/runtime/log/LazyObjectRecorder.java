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
 * (c)reated 01.09.2013 by Oli B. (boehm@javatux.de)
 */

package patterntesting.runtime.log;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.*;
import org.aspectj.lang.JoinPoint;

import patterntesting.annotation.check.runtime.NullArgsAllowed;
import patterntesting.runtime.util.JoinPointHelper;
import patterntesting.runtime.util.SignatureHelper;

/**
 * In contradiction to {@link ObjectRecorder} this class only records joinpoints
 * and return values if they are different from the last record. I.e. If a
 * return value is always the same for the same joinpoint this pair is only
 * recorded once.
 *
 * @author oliver (boehm@javatux.de)
 * @since 1.3.1 (01.09.2013)
 */
public class LazyObjectRecorder extends ObjectRecorder {

	private static final Logger LOG = LoggerFactory.getLogger(LazyObjectRecorder.class);
	private final Map<String, ValueContainer> cachedJoinpoints = new HashMap<>();

	/**
	 * Instantiates a new lazy object recorder.
	 */
	public LazyObjectRecorder() {
		super();
	}

	/**
	 * Instantiates a new lazy object recorder.
	 *
	 * @param logFile
	 *            the log file
	 */
	public LazyObjectRecorder(final File logFile) {
		super(logFile);
	}

	/**
	 * Instantiates a new lazy object recorder.
	 *
	 * @param ostream
	 *            the ostream
	 */
	public LazyObjectRecorder(final OutputStream ostream) {
		super(ostream);
	}

	/**
	 * Both things are logged with this method: the call of a method (joinPoint)
	 * and the return value of this method. Constructors or method of type
	 * 'void' are not recorded because the have no return value.
	 * <p>
	 * Because the given joinPoint cannot be used as key for a map in
	 *
	 * @param joinPoint
	 *            the joinpoint
	 * @param returnValue
	 *            the return value {@link ObjectPlayer} it is saved as string.
	 *            As a side effect this will speedup the serialization stuff and
	 *            shorten the generated record file.
	 *            </p>
	 *            <p>
	 *            The given return value will be only stored if it is not the
	 *            same as the last time.
	 *            </p>
	 */
	@Override
	@NullArgsAllowed
	public void log(final JoinPoint joinPoint, final Object returnValue) {
		String statement = JoinPointHelper.getAsLongString(joinPoint);
		if ((returnValue != null) && (SignatureHelper.hasReturnType(joinPoint.getSignature()))) {
			try {
				saveLazy(statement, returnValue);
			} catch (IOException ioe) {
				LOG.debug("Logging failed:", ioe);
				LOG.info("{} = {}", statement, returnValue);
			}
		} else {
			LOG.trace("Not recorded: {}", statement);
		}
	}

	@SuppressWarnings("deprecation")
	private void saveLazy(final String statement, final Object returnValue) throws IOException {
		ValueContainer saved = this.cachedJoinpoints.get(statement);
		if (saved == null) {
			this.cachedJoinpoints.put(statement, new ValueContainer(returnValue));
			save(statement, returnValue);
		} else {
			if (ObjectUtils.equals(saved.value, returnValue)) {
				LOG.trace("cached: {} = {}", statement, returnValue);
				saved.count++;
			} else {
				saveCache(statement, saved);
				saved.setValue(returnValue);
				save(statement, returnValue);
			}
		}
	}

	private void saveCache(final String statement, final ValueContainer saved) throws IOException {
		LOG.trace("saving: {} = {}", statement, saved);
		for (int i = 0; i < saved.count; i++) {
			save(statement, saved.value);
		}
	}

	// ------------------------------------------------------------------------

	/**
	 * This container acts as a kind of cache. The count attribute is used to
	 * store the number of (unsaved) values.
	 */
	private static class ValueContainer {

		protected Object value;
		protected int count;

		protected ValueContainer(final Object value) {
			this.value = value;
		}

		protected void setValue(final Object newValue) {
			this.value = newValue;
			this.count = 0;
		}

		@Override
		public String toString() {
			return value + " (" + count + " x cached)";
		}

	}

}
