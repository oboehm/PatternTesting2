/**
 * $Id: LogRecorder.java,v 1.16 2016/12/29 22:10:57 oboehm Exp $
 *
 * Copyright (c) 2008-2017 by Oliver Boehm
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

import java.util.*;

import org.apache.logging.log4j.*;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.spi.AbstractLogger;

import patterntesting.annotation.check.runtime.NullArgsAllowed;

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
    private static final Logger LOG = LogManager.getLogger(LogRecorder.class);
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

	
	///// new methods after switch from SLF4J to Log4J-2 //////////////////////

	@Override
	public Level getLevel() {
		throw new UnsupportedOperationException("not yet implemented");
	}

	
	///// methods from AbstractLogger /////////////////////////////////////////

	@Override
    public boolean isEnabled(Level level, Marker marker, Message message, Throwable t) {
        return isEnabled(level, marker, (Object) message, t);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, CharSequence message, Throwable t) {
        return isEnabled(level, marker, (Object) message, t);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, Object message, Throwable t) {
        return isEnabled(level, marker, Objects.toString(message), t);
    }

    /**
     * This check returns true for all levels.
     *
     * @param level the level
     * @param marker the marker
     * @param message the message
     * @param t the t
     * @return always true
     * @see org.apache.logging.log4j.spi.ExtendedLogger#isEnabled(org.apache.logging.log4j.Level, org.apache.logging.log4j.Marker, java.lang.String, java.lang.Throwable)
     */
    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Throwable t) {
        LOG.trace("isEnabled({}, {}, \"{}\", {}) = true", level, marker, message, t);
        return true;
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message) {
        return isEnabled(level, marker, message, (Throwable) null);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object... params) {
        return isEnabled(level, marker, message + ", " + Arrays.toString(params));
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0) {
        return isEnabled(level, marker, message, toArray(p0));
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1) {
        return isEnabled(level, marker, message, toArray(p0, p1));
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
        return isEnabled(level, marker, message, toArray(p0, p1, p2));
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
        return isEnabled(level, marker, message, toArray(p0, p1, p2, p3));
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3,
            Object p4) {
        return isEnabled(level, marker, message, toArray(p0, p1, p2, p3, p4));
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3,
            Object p4, Object p5) {
        return isEnabled(level, marker, message, toArray(p0, p1, p2, p3, p4, p5));
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3,
            Object p4, Object p5, Object p6) {
        return isEnabled(level, marker, message, toArray(p0, p1, p2, p3, p4, p5, p6));
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3,
            Object p4, Object p5, Object p6, Object p7) {
        return isEnabled(level, marker, message, toArray(p0, p1, p2, p3, p4, p5, p6, p7));
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3,
            Object p4, Object p5, Object p6, Object p7, Object p8) {
        return isEnabled(level, marker, message, toArray(p0, p1, p2, p3, p4, p5, p6, p7, p8));
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3,
            Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        return isEnabled(level, marker, message, toArray(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9));
    }

    private static Object[] toArray(Object... args) {
        return args;
    }

    /**
     * The message is logged and recorded here.
     *
     * @param fqcn the fqcn
     * @param level the level
     * @param marker the marker
     * @param message the message
     * @param t the t
     * @see org.apache.logging.log4j.spi.ExtendedLogger#logMessage(String, Level, Marker, Message, Throwable)
     */
    @Override
    public void logMessage(String fqcn, Level level, Marker marker, Message message, Throwable t) {
        LOG.trace("logMessage(\"{}\", {}, {}, {}, {}", fqcn, level, marker, message, t);
        LOG.log(level, marker, message, t);
        this.record(marker, message, t);
    }

}
