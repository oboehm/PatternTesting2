/*
 * $Id: ClassloaderException.java,v 1.3 2016/12/10 20:55:20 oboehm Exp $
 *
 * Copyright (c) 2014 by Oliver Boehm
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
 * (c)reated 25.08.2014 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.exception;

/**
 * If you have some problems with the {@link ClassLoader} you can use this
 * exception here for throwing. We had some problems with the IBM classloader
 * under Websphere - this is the reason, why this exception was introduced.
 *
 * @author oliver
 * @version $Revision: 1.3 $
 * @since 1.5 (25.08.2014)
 */
public class ClassloaderException extends RuntimeException {

	private static final long serialVersionUID = 20140825L;

	/**
	 * Instantiates a new classloader exception.
	 *
	 * @param cloader
	 *            the cloader
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public ClassloaderException(final ClassLoader cloader, final String message, final Throwable cause) {
		super(message + " (" + cloader + ")", cause);
	}

}
