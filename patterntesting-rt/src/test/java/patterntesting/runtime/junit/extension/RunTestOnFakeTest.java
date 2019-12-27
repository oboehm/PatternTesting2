/*
 * Copyright (c) 2010-20202 by Oliver Boehm
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
 * (c)reated 06.08.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit.extension;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import patterntesting.runtime.annotation.RunTestOn;

import static org.junit.jupiter.api.Assertions.fail;


/**
 * This test should be never executed because a "fake" on Mac does not exist.
 * It is a unit test for the RunTestOn annotation before a class definition.
 * <p>
 * At the moment (v2.0) the flag 'hide = true' does not work. This flag is
 * reserved for a future version.
 * </p>
 *
 * @author oliver
 * @since 1.1 (06.08.2010)
 */
@RunTestOn(osName = "Mac OS X", osArch = "fake", hide = true)
@ExtendWith(SmokeTestExtension.class)
public final class RunTestOnFakeTest {

    /**
     * Test dummy which should be never called.
     */
    @Test
    public void testDummy() {
        fail("should be never called because class is annotated by @RunTestOn");
    }

}

