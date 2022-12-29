/*
 * $Id: Assertions.java,v 1.14 2016/12/18 20:19:36 oboehm Exp $
 *
 * Copyright (c) 2008 by Oliver Boehm
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
 * (c)reated 31.01.2009 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.util;

import org.slf4j.LoggerFactory;
import org.slf4j.*;

/**
 * If you want to know if assertions are enabled (java option "-ea"), you can
 * use this helper class. At runtime you can use the 'jconsole' and look at the
 * java.lang.Runtime MXBean (java.lang.management.RuntimeMXBean). Here you can
 * look at the attribute "InputArguments" if "-ea" is set.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.14 $
 * @since 31.01.2009
 */
public final class Assertions {

	private static final Logger LOG = LoggerFactory.getLogger(Assertions.class);

	/** Utility class - no need to instantiate it */
	private Assertions() {
	}

	/** The Constant enabled. */
	public static final boolean ENABLED;

	static {
		boolean assertsEnabled = false;
		try {
			assert false;
			LOG.info("Assertions are disabled - call 'java -ea' (SunVM) to enable it).");
		} catch (AssertionError expected) {
			assertsEnabled = true;
			if (LOG.isTraceEnabled()) {
				LOG.trace("Assertions are enabled:", expected);
			} else {
				LOG.info("Assertions are enabled.");
			}
		}
		ENABLED = assertsEnabled;
	}

	/**
	 * If you want to know if the JavaVM was started with "Assertion enabled"
	 * (option -ea for SunVM) you can use this method.
	 * <p>
	 * Or you can ask the RuntimeMXBean for the input arguements and look for
	 * the argument "-ea".
	 * </p>
	 *
	 * @return true, if are enabled
	 * @see java.lang.management.RuntimeMXBean#getInputArguments()
	 */
	public static boolean areEnabled() {
		return ENABLED;
	}

}
