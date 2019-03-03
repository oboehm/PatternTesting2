/*
 * $Id: SopTest.java,v 1.2 2016/01/06 20:47:42 oboehm Exp $
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
package patterntesting.check.ct.io;

import org.junit.Test;

import patterntesting.annotation.check.ct.SuppressSystemOutWarning;

/**
 * This is the test class for the (Abstract)SopAspect.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.2 $
 * @since 14.09.2008
 */
public class SopTest {

    /**
     * In the main method it should be allowed to use System.out.
     *
     * @param args the args
     */
    public static void main(final String[] args) {
        System.out.println("Hello World");
    }

	/**
	 * System.out is allowed in this test.
	 */
	@Test
	@SuppressSystemOutWarning
	public final void testAllowSystemOut() {
		System.out.println("Hello System.out!");
	}

	/**
	 * System.err is allowed in this test.
	 */
	@Test
	@SuppressSystemOutWarning
	public final void testAllowSystemErr() {
		System.err.println("Hello System.err!");
	}

}
