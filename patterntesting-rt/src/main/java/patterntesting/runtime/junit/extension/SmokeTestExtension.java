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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.launcher.TestExecutionListener;
import patterntesting.runtime.NullConstants;
import patterntesting.runtime.annotation.*;
import patterntesting.runtime.junit.internal.TestOn;
import patterntesting.runtime.util.Converter;
import patterntesting.runtime.util.Environment;

import java.lang.reflect.Method;
import java.util.Date;
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

    private static final Logger LOG = LoggerFactory.getLogger(SmokeTestExtension.class);
    private final Date today = new Date();
    private final Environment env = Environment.INSTANCE;

    static {
        LOG.debug("{} is registered as JUnit extension and TestExecutionListener.", SmokeTestExtension.class);
    }

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        Class<?> testClass = context.getRequiredTestClass();
        ConditionEvaluationResult result = shouldRun(testClass);
        if (result.isDisabled()) {
            return result;
        }
        Optional<Method> testMethod = context.getTestMethod();
        if (testMethod.isPresent()) {
            return shouldRun(testMethod.get());
        }
        return ConditionEvaluationResult.enabled("Tests for " + testClass + " are enabled");
    }

    private ConditionEvaluationResult shouldRun(Class<?> testClass) {
        if ((Environment.SMOKE_TEST_ENABLED) && !AnnotationSupport.isAnnotated(testClass, SmokeTest.class)) {
            LOG.debug("Tests for {} are disabled because class is not marked as @SmokeTest.", testClass);
            return ConditionEvaluationResult.disabled(testClass + " disabled by '-D" + Environment.RUN_SMOKE_TESTS + "'");
        }
        if (AnnotationSupport.isAnnotated(testClass, IntegrationTest.class) && !Environment.INTEGRATION_TEST_ENABLED) {
            LOG.debug("Tests for {} are disabled because class is marked with @IntegrationTest.", testClass);
            return ConditionEvaluationResult
                    .disabled(testClass + " disabled - use '-D" + Environment.INTEGRATION_TEST + "=true' to enable it");
        }
        ConditionEvaluationResult result = getBrokenEvaluationResult(testClass);
        if (!result.isDisabled()) {
            result = getRunOnEvaluationResult(testClass);
        }
        if (!result.isDisabled()) {
            result = getSkipOnEvaluationResult(testClass);
        }
        return result;
    }

    private ConditionEvaluationResult shouldRun(Method testMethod) {
        if ((Environment.SMOKE_TEST_ENABLED) && !AnnotationSupport.isAnnotated(testMethod, SmokeTest.class)) {
            LOG.debug("{} is disabled because method is not marked as @SmokeTest.", testMethod);
            return ConditionEvaluationResult.disabled(testMethod + " disabled for " + Environment.RUN_SMOKE_TESTS);
        }
        if (AnnotationSupport.isAnnotated(testMethod, IntegrationTest.class) && !Environment.INTEGRATION_TEST_ENABLED) {
            LOG.debug("{} is disabled because test is marked with @IntegrationTest.", testMethod);
            return ConditionEvaluationResult
                    .disabled(testMethod + " disabled - use '-D" + Environment.INTEGRATION_TEST + "=true' to enable it");
        }
        ConditionEvaluationResult result = getBrokenEvaluationResult(testMethod);
        if (!result.isDisabled()) {
            result = getRunOnEvaluationResult(testMethod);
        }
        if (!result.isDisabled()) {
            result = getSkipOnEvaluationResult(testMethod);
        }
        return result;
    }

    private ConditionEvaluationResult getBrokenEvaluationResult(Class<?> testClass) {
        Optional<Broken> annotation = AnnotationSupport.findAnnotation(testClass, Broken.class);
        if (annotation.isPresent() && isBroken(testClass.getName(), annotation.get())) {
            return ConditionEvaluationResult.disabled(testClass + " is disabled because class marked as @Broken");
        }
        return ConditionEvaluationResult.enabled(testClass + " is enabled");
    }

    private ConditionEvaluationResult getBrokenEvaluationResult(Method testMethod) {
        Optional<Broken> annotation = AnnotationSupport.findAnnotation(testMethod, Broken.class);
        if (annotation.isPresent() && isBroken(testMethod.getName(), annotation.get())) {
            return ConditionEvaluationResult.disabled(testMethod + " is disabled because method marked as @Broken");
        }
        return ConditionEvaluationResult.enabled(testMethod + " is enabled");
    }

    private ConditionEvaluationResult getRunOnEvaluationResult(Class<?> testClass) {
        Optional<RunTestOn> annotation = AnnotationSupport.findAnnotation(testClass, RunTestOn.class);
        if (annotation.isPresent() && !isRunTestOn(testClass.getName(), annotation.get())) {
            return ConditionEvaluationResult.disabled(testClass + " is disabled because condition of @RunTestOn does not match");
        }
        return ConditionEvaluationResult.enabled(testClass + " is enabled");
    }

    private ConditionEvaluationResult getRunOnEvaluationResult(Method testMethod) {
        Optional<RunTestOn> annotation = AnnotationSupport.findAnnotation(testMethod, RunTestOn.class);
        if (annotation.isPresent() && !isRunTestOn(testMethod.getName(), annotation.get())) {
            return ConditionEvaluationResult.disabled(testMethod + " is disabled because condition of @RunTestOn does not match");
        }
        return ConditionEvaluationResult.enabled(testMethod + " is enabled");
    }

    private ConditionEvaluationResult getSkipOnEvaluationResult(Class<?> testClass) {
        Optional<SkipTestOn> annotation = AnnotationSupport.findAnnotation(testClass, SkipTestOn.class);
        if (annotation.isPresent() && isSkipTestOn(testClass.getName(), annotation.get())) {
            return ConditionEvaluationResult.disabled(testClass + " is disabled because condition of @SkipTestOn matches");
        }
        return ConditionEvaluationResult.enabled(testClass + " is enabled");
    }

    private ConditionEvaluationResult getSkipOnEvaluationResult(Method testMethod) {
        Optional<SkipTestOn> annotation = AnnotationSupport.findAnnotation(testMethod, SkipTestOn.class);
        if (annotation.isPresent() && isSkipTestOn(testMethod.getName(), annotation.get())) {
            return ConditionEvaluationResult.disabled(testMethod + " is disabled because condition of @SkipTestOn matches");
        }
        return ConditionEvaluationResult.enabled(testMethod + " is enabled");
    }

    private boolean isBroken(String method, Broken broken) {
        String why = broken.why();
        if (StringUtils.isEmpty(why)) {
            why = broken.value();
        }
        Date till = Converter.toDate(broken.till());
        if (!NullConstants.NULL_DATE.equals(till) && till.after(today)) {
            LOG.info(method + "() SKIPPED till {} because {}.", broken.till(), why);
            return true;
        }
        TestOn testOn = new TestOn(env);
        testOn.setOsNames(broken.osName());
        testOn.setOsArchs(broken.osArch());
        testOn.setOsVersions(broken.osVersion());
        testOn.setHosts(broken.host());
        testOn.setJavaVersions(broken.javaVersion());
        testOn.setJavaVendors(broken.javaVendor());
        testOn.setUsers(broken.user());
        testOn.setSystemProps(broken.property());
        if (testOn.matches()) {
            if (NullConstants.NULL_DATE.equals(till)) {
                if (testOn.hasReason()) {
                    LOG.info("{}() SKIPPED because {}.", method, testOn.getReason());
                }
                return true;
            } else {
                LOG.debug("{}() started, because it should be fixed since {}.", method, broken.till());
                return false;
            }
        }
        return false;
    }

    private static boolean isRunTestOn(String method, RunTestOn runOn) {
        TestOn testOn = new TestOn();
        testOn.setOsNames(runOn.value(), runOn.osName());
        testOn.setOsArchs(runOn.osArch());
        testOn.setOsVersions(runOn.osVersion());
        testOn.setHosts(runOn.host());
        testOn.setJavaVersions(runOn.javaVersion());
        testOn.setJavaVendors(runOn.javaVendor());
        testOn.setUsers(runOn.user());
        testOn.setSystemProps(runOn.property());
        testOn.setDays(runOn.day());
        testOn.setTimes(runOn.time());
        if (!testOn.isValueGiven()) {
            throw new IllegalArgumentException(method + ":  @RunTestOn has no value");
        }
        if (testOn.matches()) {
            LOG.info("{} executed {}.", method, testOn.getReason());
            return true;
        }
        return false;
    }

    private static boolean isSkipTestOn(String method, SkipTestOn skipOn) {
        TestOn testOn = new TestOn();
        testOn.setOsNames(skipOn.value(), skipOn.osName());
        testOn.setOsArchs(skipOn.osArch());
        testOn.setOsVersions(skipOn.osVersion());
        testOn.setHosts(skipOn.host());
        testOn.setJavaVersions(skipOn.javaVersion());
        testOn.setJavaVendors(skipOn.javaVendor());
        testOn.setUsers(skipOn.user());
        testOn.setSystemProps(skipOn.property());
        testOn.setDays(skipOn.day());
        testOn.setTimes(skipOn.time());
        if (!testOn.isValueGiven()) {
            throw new IllegalArgumentException(method + ":  @SkipTestOn has no value");
        }
        if (testOn.matches()) {
            LOG.info("{} SKIPPED because {}.", method, testOn.getReason());
            return true;
        }
        return false;
    }

}
