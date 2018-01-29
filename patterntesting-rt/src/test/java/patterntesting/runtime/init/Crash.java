/*
 * $Id: Crash.java,v 1.3 2016/12/03 19:00:50 oboehm Exp $
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
 * (c)reated 23.11.2008 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.init;

/**
 * The Class Crash.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.3 $
 * @since 23.11.2008
 */
public class Crash {

	/**
	 * Instantiates a new crash object.
	 */
	public Crash() {
		throw new RuntimeException("crashed");
	}

}
