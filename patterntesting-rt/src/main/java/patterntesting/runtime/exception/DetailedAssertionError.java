/*
 * $Id: DetailedAssertionError.java,v 1.3 2016/12/10 20:55:20 oboehm Exp $
 *
 * Copyright (c) 2013 by Oliver Boehm
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
 * (c)reated 02.01.2014 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.exception;

import java.lang.reflect.InvocationTargetException;

/**
 * This is a kind of better {@link AssertionError} which gives you some more
 * details of the given cause. It was introduced because with in Java 6 you have
 * no constructor
 * {@link DetailedAssertionError#DetailedAssertionError(Object, Throwable)}.
 *
 * @author oliver
 * @since 1.4 (02.01.2014)
 */
public class DetailedAssertionError extends AssertionError {

	private static final long serialVersionUID = 20140102L;
	private final Throwable cause;

	/**
	 * Instantiates a new detailed assertion error.
	 *
	 * @param detailMessage
	 *            the detail message
	 * @param cause
	 *            the cause
	 */
	public DetailedAssertionError(final Object detailMessage, final Throwable cause) {
		super(detailMessage);
		this.cause = cause;
	}

	/**
	 * Gets the cause.
	 *
	 * @return the cause
	 * @see java.lang.Throwable#getCause()
	 */
	@Override
	public Throwable getCause() {
		if (this.cause instanceof InvocationTargetException) {
			Throwable origCause = this.cause.getCause();
			if (origCause != null) {
				return origCause;
			}
		}
		return this.cause;
	}

}
