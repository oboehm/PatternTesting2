/*
 * $Id: ModelInitializationError.java,v 1.4 2016/12/18 20:19:41 oboehm Exp $
 *
 * Copyright (c) 2012 by Oliver Boehm
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
 * (c)reated 19.04.2012 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit.internal;

import org.junit.runners.model.InitializationError;

import java.util.ArrayList;
import java.util.List;

/**
 * This error introduced for the support of JUnit 4.7 because in this version
 * the used constructor is missing.
 *
 * @author oliver (ob@aosd.de)
 * @since 1.2.20 (19.04.2012)
 */
public class ModelInitializationError extends InitializationError {

	private static final long serialVersionUID = 20120419L;

	/**
	 * Instantiates a new model initialization error.
	 *
	 * @param error
	 *            the error
	 */
	public ModelInitializationError(final Throwable error) {
		super(asList(error));
	}

	private static List<Throwable> asList(Throwable error) {
		List<Throwable> errors = new ArrayList<>(1);
		errors.add(error);
		return errors;
	}

}
