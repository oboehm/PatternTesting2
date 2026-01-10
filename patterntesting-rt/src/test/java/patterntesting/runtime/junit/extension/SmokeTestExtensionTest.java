/*
 * Copyright (c) 2024 by Oli B.
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
 * (c)reated 25.01.24 by oboehm
 */
package patterntesting.runtime.junit.extension;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.function.ThrowingConsumer;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import patterntesting.runtime.annotation.Broken;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-Test fuer {@link SmokeTestExtension} ...
 *
 * @author oboehm
 * @since 25.01.24
 */
@ExtendWith(SmokeTestExtension.class)
class SmokeTestExtensionTest {

    private static final Logger LOG = LoggerFactory.getLogger(SmokeTestExtensionTest.class);

    private final SmokeTestExtension extension = new SmokeTestExtension();

    @Test
    void evaluateExecutionConditionClass() {
        ExtensionContext context = new TestExtensionContext(BrokenClassTest.class);
        ConditionEvaluationResult result = extension.evaluateExecutionCondition(context);
        checkResult(result);
    }

    @Test
    void evaluateExecutionConditionMethod() throws NoSuchMethodException {
        Method testMethod = getClass().getMethod("testBroken");
        ExtensionContext context = new TestExtensionContext(getClass(), testMethod);
        ConditionEvaluationResult result = extension.evaluateExecutionCondition(context);
        checkResult(result);
    }

    private static void checkResult(ConditionEvaluationResult result) {
        assertNotNull(result);
        Optional<String> reason = result.getReason();
        assertTrue(reason.isPresent());
        assertThat(reason.get(), containsString("why"));
        LOG.info("Reason is {}.", reason);
    }

    @Broken(why = "to watch the log (see issue #43)", till = "2100-01-01")
    @Test
    public void testBroken() {
        fail("this JUnit test should not be called because it is marked as @Broken");
    }



    @Broken(why="broken by design")
    public static class BrokenClassTest {
    }

    private static class TestExtensionContext implements ExtensionContext {
        private final Class<?> testClass;
        private final Method testMethod;
        public TestExtensionContext(Class<?> testClass) {
            this(testClass, null);
        }
        public TestExtensionContext(Class<?> testClass, Method testMethod) {
            this.testClass = testClass;
            this.testMethod = testMethod;
        }
        @Override
        public Optional<Class<?>> getTestClass() {
            return Optional.ofNullable(testClass);
        }
        @Override
        public List<Class<?>> getEnclosingTestClasses() {
            throw new UnsupportedOperationException("getEnclosingTestClasses not yet implemented");

        }
        @Override
        public Optional<Method> getTestMethod() {
            return Optional.ofNullable(testMethod);
        }
        @Override
        public Optional<ExtensionContext> getParent() {
            throw new UnsupportedOperationException("getParent not yet implemented");
        }
        @Override
        public ExtensionContext getRoot() {
            throw new UnsupportedOperationException("getRoot not yet implemented");
        }
        @Override
        public String getUniqueId() {
            throw new UnsupportedOperationException("getUniqueId not yet implemented");
        }
        @Override
        public String getDisplayName() {
            throw new UnsupportedOperationException("getDisplayName not yet implemented");
        }
        @Override
        public Set<String> getTags() {
            throw new UnsupportedOperationException("getTags not yet implemented");
        }
        @Override
        public Optional<AnnotatedElement> getElement() {
            throw new UnsupportedOperationException("getElement not yet implemented");
        }
        @Override
        public Optional<TestInstance.Lifecycle> getTestInstanceLifecycle() {
            throw new UnsupportedOperationException("getTestInstanceLifecycle not yet implemented");
        }
        @Override
        public Optional<Object> getTestInstance() {
            throw new UnsupportedOperationException("getTestInstance not yet implemented");
        }
        @Override
        public Optional<TestInstances> getTestInstances() {
            throw new UnsupportedOperationException("getTestInstances not yet implemented");
        }
        @Override
        public Optional<Throwable> getExecutionException() {
            throw new UnsupportedOperationException("getExecutionException not yet implemented");
        }
        @Override
        public Optional<String> getConfigurationParameter(String s) {
            throw new UnsupportedOperationException("getConfigurationParameter not yet implemented");
        }
        @Override
        public <T> Optional<T> getConfigurationParameter(String s, Function<String, T> function) {
            throw new UnsupportedOperationException("getConfigurationParameter not yet implemented");
        }
        @Override
        public void publishReportEntry(Map<String, String> map) {
            throw new UnsupportedOperationException("publishReportEntry not yet implemented");
        }
        @Override
        public void publishFile(String name, MediaType mediaType, ThrowingConsumer<Path> action) {
            throw new UnsupportedOperationException("publishFile not yet implemented");

        }
        @Override
        public void publishDirectory(String name, ThrowingConsumer<Path> action) {
            throw new UnsupportedOperationException("publishDirectory not yet implemented");

        }
        @Override
        public Store getStore(Namespace namespace) {
            throw new UnsupportedOperationException("getStore not yet implemented");
        }
        @Override
        public ExecutionMode getExecutionMode() {
            throw new UnsupportedOperationException("getExecutionMode not yet implemented");
        }
        @Override
        public ExecutableInvoker getExecutableInvoker() {
            throw new UnsupportedOperationException("getExecutableInvoker not yet implemented");
        }
    }

}