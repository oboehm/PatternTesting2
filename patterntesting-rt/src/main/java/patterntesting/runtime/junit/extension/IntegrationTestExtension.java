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

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import patterntesting.runtime.util.Environment;

/**
 * Use this IntegrationTestExtension if you want to disable your integration
 * tests for the normal test run. Put
 * <pre>
 *     "@ExtendWith(IntegrationTestExtension.class)"
 * </pre>
 * in front of your JUnit-5 test class and your test is enabled. To enable it
 * for integration testing call your test with
 * <pre>
 *     -Dpatterntesting.integrationTest=true
 * </pre>
 * <p>
 *     NOTE: If you use e.g. Maven to run your test you can control it via
 *     maven-failsafe-plugin and '...IT' as suffix for your test class. But
 *     not all IDEs distinguish between normal tests ('...Test') and
 *     integration tests ('...IT'). With this extension you can control it
 *     by setting the system property "patterntesting.integrationTest".
 * </p>
 *
 * @author oboehm
 * @since 2.0 (06.11.2019)
 */
public class IntegrationTestExtension implements ExecutionCondition {

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(
            ExtensionContext context) {
        if (Environment.INTEGRATION_TEST_ENABLED) {
            return ConditionEvaluationResult.enabled("Integration Test enabled");
        } else {
            return ConditionEvaluationResult
                    .disabled("Test disabled - use '-D" + Environment.INTEGRATION_TEST + "=true' to enable it");
        }
    }

}
