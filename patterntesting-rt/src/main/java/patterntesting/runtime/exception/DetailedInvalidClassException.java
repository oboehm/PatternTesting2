/*
 * $Id: DetailedInvalidClassException.java,v 1.4 2016/12/10 20:55:20 oboehm Exp $
 *
 * Copyright (c) 2016 by Oliver Boehm
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
 * (c)reated 13.03.2016 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.exception;

import java.io.InvalidClassException;

/**
 * The problem with the {@link InvalidClassException} is that a constructor with
 * a cause is missing. This is the reason why this derived exceptions exists.
 *
 * @author oliver
 * @since 1.6 (13.03.2016)
 */
public class DetailedInvalidClassException extends InvalidClassException {

	private static final long serialVersionUID = 20160313L;

	/**
	 * Instantiates a new detailed invalid class exception.
	 *
	 * @param reason
	 *            the reason
	 * @param cause
	 *            the cause
	 */
	public DetailedInvalidClassException(String reason, Throwable cause) {
		super(reason);
		this.initCause(cause);
	}

}
