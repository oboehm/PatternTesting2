/**
 * $Id: StaticTest.java,v 1.4 2016/01/06 20:47:42 oboehm Exp $
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
 * (c)reated 17.11.2008 by oliver (ob@oasd.de)
 */
package patterntesting.check.ct;

import org.junit.Test;

import patterntesting.annotation.check.ct.DamnStaticVars;

import static org.junit.Assert.*;

/**
 * This is a test class for the (forbidden) use of static variables.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.4 $
 * @since 17.11.2008
 */
@DamnStaticVars
public class StaticTest {

	/**
	 * If you remove the final keyword here you should see a warning!
	 * @see StaticAspect
	 */
	private static final String dummy = "dummy";

	/**
	 * This is only a dummy test to avoid an empty test class.
	 */
	@Test
	public final void testDummy() {
		assertEquals("dummy", dummy);
	}

}
