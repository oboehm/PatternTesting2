/*
 * $Id: SmokeBuilderTest.java,v 1.3 2016/12/18 20:19:38 oboehm Exp $
 *
 * Copyright (c) 2016 by Oliver Boehm
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
 * (c)reated 24.03.2016 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit.internal;

import org.junit.Test;
import org.junit.internal.builders.JUnit4Builder;
import org.junit.runner.Runner;
import patterntesting.runtime.junit.SmokeRunner;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link SmokeBuilder} class.
 *
 * @author oliver
 */
public final class SmokeBuilderTest {

    /**
     * Test method for {@link SmokeBuilder#runnerForClass(Class)}.
     *
     * @throws Throwable the throwable
     */
    @Test
    public void testRunnerForClass() throws Throwable {
        SmokeBuilder smBuilder = new SmokeBuilder(new JUnit4Builder());
        Runner runner = smBuilder.runnerForClass(SmokeBuilderTest.class);
        assertEquals(SmokeRunner.class, runner.getClass());
    }

}

