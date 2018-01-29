/**
 * $Id: ContractViolation.java,v 1.5 2016/12/10 20:55:23 oboehm Exp $
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
 * (c)reated 29.01.2009 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.dbc;

/**
 * The Class ContractViolation.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.5 $
 * @since 29.01.2009
 */
public class ContractViolation extends AssertionError {

	private static final long serialVersionUID = 20090129L;

	/**
	 * Instantiates a new contract violation.
	 *
	 * @param detailMessage
	 *            the detail message
	 */
	public ContractViolation(final Object detailMessage) {
		super(detailMessage);
	}

}
