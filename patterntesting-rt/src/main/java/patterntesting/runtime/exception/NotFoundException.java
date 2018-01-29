/*
 * $Id: NotFoundException.java,v 1.5 2016/12/10 20:55:20 oboehm Exp $
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
 * (c)reated 11.11.2013 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.exception;

import java.util.NoSuchElementException;
import java.util.regex.Pattern;

/**
 * If you did not find a result and want to avoid 'null" as return value you can
 * throw this exception here. Since 1.5 it is no longer derived direct from
 * {@link RuntimeException} but {@link NoSuchElementException}. The reason for
 * this change is a missing constructor like
 * {@link NotFoundException#NotFoundException(String, Throwable)} in
 * {@link NoSuchElementException}.
 *
 * @author oliver
 * @see RuntimeException
 * @since 1.4 (11.11.2013)
 */
public class NotFoundException extends NoSuchElementException {

	private static final long serialVersionUID = 20140818L;
	private final Throwable cause;

	/**
	 * Instantiates a new not found exception.
	 *
	 * @param value
	 *            the object which was not found
	 */
	public NotFoundException(final Object value) {
		this((value instanceof Pattern ? "pattern" : "value") + " \"" + value + "\" not found");
	}

	/**
	 * Instantiates a new not found exception.
	 *
	 * @param msg
	 *            the msg
	 */
	public NotFoundException(final String msg) {
		this(msg, null);
	}

	/**
	 * Instantiates a new not found exception.
	 *
	 * @param t
	 *            the cause
	 */
	public NotFoundException(final Throwable t) {
		super();
		this.cause = t;
	}

	/**
	 * Instantiates a new not found exception.
	 *
	 * @param msg
	 *            the msg
	 * @param t
	 *            the cause
	 */
	public NotFoundException(final String msg, final Throwable t) {
		super(msg);
		this.cause = t;
	}

	/**
	 * Gets the stored cause.
	 *
	 * @return the cause
	 * @see java.lang.Throwable#getCause()
	 */
	@Override
	public Throwable getCause() {
		return this.cause;
	}

}
