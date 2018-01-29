/*
 * $Id: SimpleLog.java,v 1.5 2016/12/10 20:55:20 oboehm Exp $
 *
 * Copyright (c) 2011 by Oliver Boehm
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
 * (c)reated 09.07.2011 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.log;

/**
 * Before the switch from commons-logging to SLF4J we used the constants from
 * SimpleLog in some log annotations and log aspects. Because we don't want a
 * dependency to commons-logging (because of problems of bundling it as OSGi-
 * package) we must define it now ourself. This is done here.
 * <p>
 * The constants are the same as in
 * <a href="org.apache.commons.logging.impl.SimpleLog" >org.apache.commons.
 * logging.impl.SimpleLog</a> so it does not matter which of the SimpleLog
 * constants you use.
 * </p>
 *
 * @author oliver
 * @since 1.1.1 (09.07.2011)
 */
public final class SimpleLog {

	/** "Trace" level logging. */
	public static final int LOG_LEVEL_TRACE = 1;
	/** "Debug" level logging. */
	public static final int LOG_LEVEL_DEBUG = 2;
	/** "Info" level logging. */
	public static final int LOG_LEVEL_INFO = 3;
	/** "Warn" level logging. */
	public static final int LOG_LEVEL_WARN = 4;
	/** "Error" level logging. */
	public static final int LOG_LEVEL_ERROR = 5;
	/** "Fatal" level logging. */
	public static final int LOG_LEVEL_FATAL = 6;

	/** No need to instantiate it. */
	private SimpleLog() {
	}

}
