/*
 * $Id: Crash.java,v 1.3 2016/01/06 20:47:37 oboehm Exp $
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
 * (c)reated 24.11.2008 by oliver (ob@oasd.de)
 */
package patterntesting.sample;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import patterntesting.runtime.annotation.GuardInitialization;

/**
 * This class will crash while initializing the static part.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.3 $
 * @since 24.11.2008
 */
@GuardInitialization
public final class Crash {

    /** this initialization will crash. */
	protected static final Log log = LogFactoryImpl.getLog((String) null);

    /** Utility class - no need to instantiate it. */
	private Crash() {}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public static Date getDate() {
		return new Date();
	}

}
