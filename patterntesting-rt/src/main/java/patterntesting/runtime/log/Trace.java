/*
 * $Id: Trace.java,v 1.11 2016/12/18 20:19:38 oboehm Exp $
 *
 * Copyright (c) 2010 by Oliver Boehm
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
 * (c)reated 30.09.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.log;

import org.slf4j.LoggerFactory;
import org.slf4j.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.SourceLocation;

import patterntesting.runtime.util.Converter;
import patterntesting.runtime.util.JoinPointHelper;

/**
 * This class works together with the (Abstract)TraceAspect to provide some kind
 * of tracing information.
 *
 * @author oliver
 * @since 1.0.3 (30.09.2010)
 */
public final class Trace {

	private static final Logger LOG = LoggerFactory.getLogger(Trace.class);
	private static final int INDENT_START = calibrateStacktraceDepth();

	static {
		if (LOG.isTraceEnabled()) {
			LOG.trace("{} loaded.", Trace.class);
		} else if (LOG.isDebugEnabled()) {
			LOG.info("To see more trace information set log level to TRACE for {}.", Trace.class.getName());
		} else {
			LOG.info("To see @TraceMe information you must set log level to DEBUG for {}", Trace.class.getName());
		}
	}

	/** Utility class - no need to instantiate it. */
	private Trace() {
	}

	private static int calibrateStacktraceDepth() {
		return Thread.currentThread().getStackTrace().length;
	}

	/**
	 * Logs the trace with a start sign ("&gt;").
	 *
	 * @param joinpoint
	 *            the joinpoint
	 */
	public static void start(final JoinPoint joinpoint) {
		if (LOG.isDebugEnabled()) {
			int level = getIndentLevel();
			trace(level, "> ", joinpoint, "");
		}
	}

	/**
	 * Logs the trace with a start sign ("&lt;").
	 *
	 * @param joinpoint
	 *            the joinpoint
	 */
	public static void end(final JoinPoint joinpoint) {
		if (LOG.isDebugEnabled()) {
			int level = getIndentLevel();
			trace(level, "< ", joinpoint, "");
		}
	}

	/**
	 * Logs the trace with a start sign ("&lt;").
	 *
	 * @param joinpoint
	 *            the joinpoint
	 * @param result
	 *            the result
	 */
	public static void end(final JoinPoint joinpoint, final Object result) {
		if (LOG.isDebugEnabled()) {
			int level = getIndentLevel();
			trace(level, "< ", joinpoint, " = " + Converter.toShortString(result));
		}
	}

	/**
	 * Logs the thrown exception.
	 *
	 * @param joinpoint
	 *            the joinpoint
	 * @param t
	 *            the t
	 */
	public static void throwing(final JoinPoint joinpoint, final Throwable t) {
		if (LOG.isDebugEnabled()) {
			int level = getIndentLevel();
			trace(level, "<*", joinpoint, "");
			trace(level, " *** ", t);
		}
	}

	/**
	 * Logs the trace with a start sign ("&lt;").
	 *
	 * @param joinpoint
	 *            the joinpoint
	 * @param suffix
	 *            the suffix
	 */
	public static void end(final JoinPoint joinpoint, final String suffix) {
		if (LOG.isDebugEnabled()) {
			int level = getIndentLevel();
			trace(level, "< ", joinpoint, suffix);
		}
	}

	/**
	 * Logs the trace with a "|" sign.
	 *
	 * @param joinpoint
	 *            the joinpoint
	 */
	public static void trace(final JoinPoint joinpoint) {
		if (LOG.isDebugEnabled()) {
			int level = getIndentLevel();
			trace(level, " | ", joinpoint, "");
		}
	}

	private static void trace(final int level, final String prefix, final JoinPoint joinpoint, final String suffix) {
		if (LOG.isTraceEnabled()) {
			String loc = getLocation(joinpoint);
			LOG.trace(indent(level) + prefix + JoinPointHelper.getAsLongString(joinpoint) + suffix + loc);
		} else if (LOG.isDebugEnabled()) {
			LOG.debug(indent(level) + prefix + JoinPointHelper.getAsShortString(joinpoint) + suffix);
		}
	}

	private static void trace(final int level, final String prefix, final Throwable t) {
		if (LOG.isTraceEnabled()) {
			LOG.trace(indent(level) + prefix, t);
		} else if (LOG.isDebugEnabled()) {
			LOG.debug(indent(level) + prefix + t);
		}
	}

	private static String indent(final int level) {
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < level; i++) {
			buffer.append("  ");
		}
		return buffer.toString();
	}

	private static int getIndentLevel() {
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		return stacktrace.length - INDENT_START;
	}

	private static String getLocation(final JoinPoint joinpoint) {
		SourceLocation loc = joinpoint.getSourceLocation();
		return " (" + loc.getFileName() + ":" + loc.getLine() + ")";
	}

}
