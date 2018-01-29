/*
 * Copyright (c) 2016 by Oli B.
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
 * (c)reated 09.02.2016 by Oli B. (ob@aosd.de)
 */

package patterntesting.runtime.junit;

import javax.annotation.concurrent.Immutable;

import org.junit.Test;

import patterntesting.runtime.io.LineReader;

/**
 * JUnit tests for {@link ImmutableTester} class.
 *
 * @author oboehm (ob@aosd.de)
 */
@Immutable
public final class ImmutableTesterTest {

	/**
	 * Test method for {@link ImmutableTester#assertImmutable(Class)}.
	 */
	@Test
    public void testAssertImmutable() {
    	ImmutableTester.assertImmutable(this.getClass());
    }

    /**
	 * The LineReader class is not immutable. So we expect an
	 * {@link AssertionError} here.
	 */
    @Test(expected = AssertionError.class)
    public void testAssertNotImmutable() {
    	ImmutableTester.assertImmutable(LineReader.class);
    }

    /**
     * Test method for {@link ImmutableTester#assertImmutable(Package)}.
     */
    @Test
    public void testAssertImmutableOfPackage() {
    	ImmutableTester.assertImmutable(this.getClass().getPackage());
    }

}
