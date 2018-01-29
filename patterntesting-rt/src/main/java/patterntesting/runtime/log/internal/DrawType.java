/*
 * $Id: DrawType.java,v 1.10 2016/12/10 20:55:24 oboehm Exp $
 *
 * Copyright (c) 2015 by Oliver Boehm
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
 * (c)reated 03.06.2015 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.log.internal;

/**
 * The different types of drawings.
 */
public enum DrawType {

	/** The unknown. */
	UNKNOWN,

	/** The actor. */
	ACTOR,

	/** The object. */
	OBJECT,

	/** The place holder. */
	PLACEHOLDER_OBJECT,

	/** The create message. */
	CREATE_MESSAGE,

	/** The message. */
	MESSAGE,

	/** The return message. */
	RETURN_MESSAGE;

}
