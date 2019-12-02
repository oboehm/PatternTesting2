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
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.launcher.*;
import patterntesting.runtime.annotation.IntegrationTest;
import patterntesting.runtime.annotation.SmokeTest;
import patterntesting.runtime.util.Environment;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * This extension replaces the SmokeRunner class for JUnit 4. The name is
 * derived from the {@link patterntesting.runtime.annotation.SmokeTest}
 * annotation which allows you to mark tests as "SmokeTest". But it works
 * also for the other JUnit annotations like {@link IntegrationTest} or
 * {@link patterntesting.runtime.annotation.Broken}.
 *
 * @author oboehm
 * @since 2.0 (08.11.2019)
 */
public class SmokeTestExtension implements ExecutionCondition, TestExecutionListener {

    private static final Logger LOG = LogManager.getLogger();

    static {
        LOG.debug("{} is registered as JUnit extension and TestExecutionListener.", SmokeTestExtension.class);
    }

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        Class<?> testClass = context.getRequiredTestClass();
        if (AnnotationSupport.isAnnotated(testClass, IntegrationTest.class) && !Environment.INTEGRATION_TEST_ENABLED) {
            LOG.debug("Tests for {} are disabled because test is marked with @IntegrationTest.", testClass);
            return ConditionEvaluationResult
                    .disabled(testClass + " disabled - use '-D" + Environment.INTEGRATION_TEST + "=true' to enable it");
        }
        Optional<Method> testMethod = context.getTestMethod();
        if (testMethod.isPresent() && !AnnotationSupport.isAnnotated(testMethod, SmokeTest.class) && Environment.SMOKE_TEST_ENABLED) {
            LOG.debug("{} is disabled because method is not marked as @SmokeTest.", testMethod.get());
            return ConditionEvaluationResult.disabled(testMethod.get() + " disabled for " + Environment.RUN_SMOKE_TESTS);
        }
        return ConditionEvaluationResult.enabled("Tests for " + testClass + " are enabled");
    }

}
