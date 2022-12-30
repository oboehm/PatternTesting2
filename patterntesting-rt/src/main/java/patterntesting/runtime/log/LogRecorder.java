/*
 * Copyright (c) 2008-2022 by Oliver Boehm
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
 * (c)reated 09.10.2008 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.helpers.AbstractLogger;
import patterntesting.annotation.check.runtime.NullArgsAllowed;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The Class LogRecorder can only record log messages. It ignores the level.
 * <p>
 * This class can be useful for testing if you want to check if a specific
 * message appears in the log.
 * </p>
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.16 $
 * @since 09.10.2008
 */
public final class LogRecorder extends AbstractLogger implements Logger {

    private static final long serialVersionUID = 20161228L;
    private static final Logger LOG = LoggerFactory.getLogger(LogRecorder.class);
    private final List<String> objects = new ArrayList<>();
	private final List<Throwable> exceptions = new ArrayList<>();

	@NullArgsAllowed
	private synchronized void record(final String obj, final Throwable t) {
		this.objects.add(obj);
		this.exceptions.add(t);
	}

	@NullArgsAllowed
	private void record(final Marker arg0, final Object arg1, final Throwable arg2) {
	    String prefix = (arg0 == null) ? "" : arg0 + ": ";
		this.record(prefix + arg1, arg2);
	}

	private void record(Marker marker, String format, Object[] args, Throwable t) {
		if (args == null || args.length == 0) {
			record(marker, format, t);
		} else {
			String msg = format;
			for (Object obj : args) {
				msg = msg.replaceFirst("\\{}", Objects.toString(obj));
			}
			record(marker, msg, t);
		}
	}

	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText() {
		StringBuilder text = new StringBuilder();
		for (Object obj : this.objects) {
			text.append(obj);
		}
		return text.toString().trim();
	}

	/**
	 * Gets the record.
	 *
	 * @return the record
	 */
	public String getRecord() {
		return getText();
	}

	/**
	 * Gets the number of records.
	 *
	 * @return the number of records
	 */
	public int getNumberOfRecords() {
		return this.objects.size();
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String classname = this.getClass().getSimpleName();
		if (this.getNumberOfRecords() == 1) {
			return classname + "(\"" + getRecord() + "\")";
		} else {
			return classname + " with " + this.getNumberOfRecords() + " records";
		}
	}

	@Override
	protected String getFullyQualifiedCallerName() {
		throw new UnsupportedOperationException("getFullyQualifiedCallerName not yet implemented");
	}

	@Override
	protected void handleNormalizedLoggingCall(Level level, Marker marker, String message, Object[] arguments,
											   Throwable t) {
        LOG.trace("handleNormalizedLoggingCall({}, {}, \"{}\", {}, {}", level, marker, message, arguments, t);
        this.record(marker, message, arguments, t);
	}

	@Override
	public boolean isTraceEnabled() {
		return true;
	}

	@Override
	public boolean isTraceEnabled(Marker marker) {
		return isTraceEnabled();
	}

	@Override
	public boolean isDebugEnabled() {
		return true;
	}

	@Override
	public boolean isDebugEnabled(Marker marker) {
		return isDebugEnabled();
	}

	@Override
	public boolean isInfoEnabled() {
		return true;
	}

	@Override
	public boolean isInfoEnabled(Marker marker) {
		return isInfoEnabled();
	}

	@Override
	public boolean isWarnEnabled() {
		return true;
	}

	@Override
	public boolean isWarnEnabled(Marker marker) {
		return isWarnEnabled();
	}

	@Override
	public boolean isErrorEnabled() {
		return true;
	}

	@Override
	public boolean isErrorEnabled(Marker marker) {
		return isErrorEnabled();
	}

}
