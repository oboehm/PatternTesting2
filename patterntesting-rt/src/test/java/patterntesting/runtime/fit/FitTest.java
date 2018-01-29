/*
 * $Id: FitTest.java,v 1.5 2016/12/18 20:19:39 oboehm Exp $
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
 * (c)reated 25.08.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.fit;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import fit.Counts;
import patterntesting.runtime.annotation.IntegrationTest;
import patterntesting.runtime.junit.SmokeRunner;


/**
 * This is not really a unit test but a class to start the integration tests
 * with FIT.
 *
 * @author oliver
 * @since 1.0.2 (25.08.2010)
 */
@RunWith(SmokeRunner.class)
@IntegrationTest
public class FitTest {

    private static final Logger log = LogManager.getLogger(FitTest.class);

    /**
     * This test method starts FIT for the overview document.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testOverview() throws IOException {
        Counts counts = runFIT(new File("src/site/xdoc/doc/overview.html"));
        assertEquals(0, counts.exceptions);
        assertEquals(0, counts.wrong);
    }

    private static Counts runFIT(final File input) throws IOException {
        File output = getOutFile(input.getName());
        log.info("generating " + output + "...");
        FitRunner fitRunner = new FitRunner(input, output);
        fitRunner.run();
        return fitRunner.fixture.counts;
    }

    private static File getOutFile(final String resource) {
        String name = new File(resource).getName();
        File dir = new File("target/site/fit");
        if (dir.mkdirs()) {
            log.info("created: " + dir);
        }
        return new File(dir, name);
    }

}

