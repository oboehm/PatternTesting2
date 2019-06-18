/*
 * $Id: ParallelBuilder.java,v 1.3 2016/01/06 20:08:23 oboehm Exp $
 *
 * Copyright (c) 2012 by Oliver Boehm
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
 * (c)reated 30.12.2011 by oliver (ob@oasd.de)
 */

package patterntesting.concurrent.junit.internal;

import org.junit.runner.Runner;
import org.junit.runners.model.RunnerBuilder;
import patterntesting.runtime.junit.internal.SmokeBuilder;
import patterntesting.runtime.util.Environment;

/**
 * This builder will return an Runner which start the JUnit test in background,
 * records the result and presents it, when they are needed
 * (http://hwellmann.blogspot.com/2009/12/running-parameterized-junit-tests-in.html
 * would be another approach to do it).
 * <p>
 * If threads are disabled (see {@link Environment#areThreadsAllowed()} a
 * normal runner is used. I.e. in this case the JUnit test will be started
 * normal (and not in the background).
 * </p>
 *
 * @author oliver
 * @since 1.2 (30.12.2011)
 */
public class ParallelBuilder extends SmokeBuilder {

    /**
     * Instantiates a new parallel builder.
     *
     * @param builder the builder
     */
    public ParallelBuilder(final RunnerBuilder builder) {
        super(builder);
    }

    /**
     * Runner for class.
     *
     * @param testClass the test class
     * @return the runner
     * @throws Throwable the throwable
     * @see SmokeBuilder#runnerForClass(Class)
     */
    @Override
    public Runner runnerForClass(final Class<?> testClass) throws Throwable {
        Runner runner = super.runnerForClass(testClass);
        if (Environment.areThreadsAllowed()) {
            BackgroundRunner bgRunner = new BackgroundRunner(runner);
            bgRunner.startRunner();
            return bgRunner;
        } else {
            return runner;
        }
    }

}

