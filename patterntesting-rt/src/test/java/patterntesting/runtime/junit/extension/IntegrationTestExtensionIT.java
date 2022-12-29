/*
 * Copyright (c) 2019 by Oliver Boehm
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * (c)reated 06.11.2019 by oboehm (ob@oasd.de)
 */
package patterntesting.runtime.junit.extension;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import patterntesting.runtime.util.Environment;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Unit tests for {@link IntegrationTestExtension}.
 */
@ExtendWith(IntegrationTestExtension.class)
class IntegrationTestExtensionIT {

    private static final Logger LOG = LoggerFactory.getLogger(IntegrationTestExtensionIT.class);

    @BeforeAll
    static void beforeAll() {
        shouldRunOnlyIfTestsEnabled();
    }

    @BeforeEach
    void beforeEach() {
        shouldRunOnlyIfTestsEnabled();
    }

    @Test
    void integrationTest() {
        shouldRunOnlyIfTestsEnabled();
    }

    @AfterEach
    void afterEach() {
        shouldRunOnlyIfTestsEnabled();
    }

    @AfterAll
    static void afterAll() {
        shouldRunOnlyIfTestsEnabled();
    }

    private static void shouldRunOnlyIfTestsEnabled() {
        if (Environment.INTEGRATION_TEST_ENABLED) {
            LOG.info("Integration tests are enabled ('-D{}=true').", Environment.INTEGRATION_TEST);
        } else {
            fail("Test should be enabled for integration tests");
        }
    }

}