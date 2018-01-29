/*
 * $Id: SmokeRunnerIgnoreTest.java,v 1.4 2016/12/18 20:19:38 oboehm Exp $
 *
 * Copyright (c) 2010 by Oliver Boehm
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
 * (c)reated 23.04.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import static org.junit.Assert.fail;

import org.apache.logging.log4j.*;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This JUnit tests should be ignored completely because the whole class is
 * annotated with {@code @Ignore}.
 * <p>
 * NOTE: You may find the '@Ignore' annotation commented out. The reaseon for
 * this is the quality gate in SonarQube, which does not like skipped unit
 * tests.
 * </p>
 *
 * @author oliver
 * @since 1.0 (23.04.2010)
 */
@RunWith(SmokeRunner.class)
//@Ignore
public final class SmokeRunnerIgnoreTest extends SmokeRunnerTest {

    private static final Logger LOG = LogManager.getLogger(SmokeRunnerIgnoreTest.class);

    /**
     * This test should be never called if this test class is annotated with
     * '@Ignore'.
     */
    @Test
    public void testIgnore() {
        Ignore annotation = this.getClass().getAnnotation(Ignore.class);
        if (annotation == null) {
            LOG.info("testIgnore() is executed because no @Ignore annotation was found.");
        } else {
            fail("this class should be completely ignored because of annotation " + annotation);
        }
    }

}
