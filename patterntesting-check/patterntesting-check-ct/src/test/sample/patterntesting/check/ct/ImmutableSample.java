/**
 * Copyright (c) 2008-2019 by Oliver Boehm
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
 * (c)reated 29.09.2008 by oliver (ob@oasd.de)
 */
package patterntesting.check.ct;

import org.junit.Test;

import javax.annotation.concurrent.Immutable;

/**
 * With this test class you can check if the ImmutableAspect is working
 * correct.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 29.09.2008
 * @version $Revision: 1.2 $
 */
@Immutable
public final class ImmutableSample {

    private int w = 1;    // should give a warning
	protected int x;
	private transient int y = 1;
	private final int z = 2;

	/**
	 * This test changes the internal attribute x if you remove the comment.
	 */
	@Test
	public void testMutable() {
		x = 2;	   // should give a warning
		y = z;	   // this is allowed because y is transient
		y = x;	   // you should see a warning here because x should be final
		//z = 3;   // not allowed -> final
	}

}
