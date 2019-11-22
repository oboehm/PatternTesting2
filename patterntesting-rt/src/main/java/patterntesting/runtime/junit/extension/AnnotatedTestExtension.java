/*
 * Copyright (c) 2019 by Oliver L. Boehm
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
 * (c)reated 08.11.2019 by oboehm (ob@oasd.de)
 */
package patterntesting.runtime.junit.extension;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.launcher.*;
import patterntesting.runtime.annotation.IntegrationTest;
import patterntesting.runtime.util.Environment;

/**
 * The class AnnotatedTestExtension.
 *
 * @author oboehm
 * @since 2.0 (08.11.2019)
 */
public class AnnotatedTestExtension implements Extension, TestExecutionListener {

    private static final Logger LOG = LogManager.getLogger();

    static {
        LOG.info("AnnotationTestExtension is registered as JUnit extension and TestExecutionListener.");
    }

}
