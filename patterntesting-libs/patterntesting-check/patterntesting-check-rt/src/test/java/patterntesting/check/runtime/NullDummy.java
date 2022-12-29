/*
 * Copyright (c) 2008-2019 by Oliver Boehm
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
 * (c)reated 12.09.2008 by oliver (ob@oasd.de)
 */
package patterntesting.check.runtime;

import org.slf4j.*;

import patterntesting.annotation.check.runtime.*;

/**
 * This test dummy accepts null and returns null and is a test class for
 * the NullPointerTrap and NullPointerTrapTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @see NullPointerTrapTest
 * @since 12.09.2008
 */
@NullArgsAllowed
@MayReturnNull
public final class NullDummy {

	private static Logger log = LoggerFactory.getLogger(NullDummy.class);

	/**
	 * Instantiates a new null dummy.
	 *
	 * @param s the s
	 */
	public NullDummy(final String s) {
		log.debug(this + " created with s=" + s);
	}
	
	/**
	 * Ignores the given string string.
	 *
	 * @param s the ignored string
	 */
	public void setAndIgnoreString(final String s) {
	    log.debug("setAndIgnoreString(\"" + s + "\") called");
	}

	/**
	 * Accept all.
	 *
	 * @param s the s
	 */
	public static void acceptAll(final String s) {
		log.debug("received: " + s);
	}

	/**
	 * Return null.
	 *
	 * @return the object
	 */
	public static Object returnNull() {
		return null;
	}

}
