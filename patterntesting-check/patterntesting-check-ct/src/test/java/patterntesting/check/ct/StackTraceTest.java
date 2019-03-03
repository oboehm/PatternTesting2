/**
 * $Id: StackTraceTest.java,v 1.4 2016/01/06 20:47:42 oboehm Exp $
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
 * (c)reated 14.09.2008 by oliver (ob@oasd.de)
 */
package patterntesting.check.ct;

import org.junit.Test;

import patterntesting.annotation.check.ct.SuppressStackTraceWarning;


/**
 * This is the test class for (Abstract)StackTraceAspect.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.4 $
 * @since 14.09.2008
 */
public final class StackTraceTest {

    /**
     * If you want to see a warning from StackTraceAspect here remove
     * the @SuppressStackTraceWarning annotation.
     */
	@SuppressStackTraceWarning
	public void testAllowPrintStackTrace() {
		try {
			throw new Exception("hello exception");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This test is only a dummy to avoid
	 * "Tests in error:
  	 * initializationError0(patterntesting.java.StackTraceAspectTest)"
     * during maven build.
	 */
	@Test
	public void testDummy() {
	}

}
