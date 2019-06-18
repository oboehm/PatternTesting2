/*
 * $Id: ParallelSuite.java,v 1.4 2016/01/06 20:47:14 oboehm Exp $
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

package patterntesting.concurrent.junit;

import org.junit.runner.Runner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import patterntesting.concurrent.junit.internal.ParallelBuilder;
import patterntesting.runtime.util.Environment;

import java.util.List;

/**
 * The Class ParallelSuite can be used if you want to run the JUnit test
 * classes in parallel. In case of problems (or if you want to see how much
 * faster the parallel run is) you can disable parallelity by setting the
 * system property {@link Environment#DISABLE_THREADS}
 * (see {@link Environment#areThreadsAllowed()}).
 *
 * @author oliver
 * @since 1.2 (30.12.2011)
 */
public final class ParallelSuite extends Suite {

    /**
     * Instantiates a new parallel suite.
     *
     * @param klass the klass
     * @param runners the runners
     * @throws InitializationError the initialization error
     */
    public ParallelSuite(Class<?> klass, List<Runner> runners) throws InitializationError {
        super(klass, runners);
    }

    /**
     * Instantiates a new parallel suite.
     *
     * @param klass the klass
     * @param builder the builder
     * @throws InitializationError the initialization error
     */
    public ParallelSuite(Class<?> klass, RunnerBuilder builder) throws InitializationError {
        super(klass, new ParallelBuilder(builder));
    }

    /**
     * Instantiates a new parallel suite.
     *
     * @param builder the builder
     * @param klass the klass
     * @param suiteClasses the suite classes
     * @throws InitializationError the initialization error
     */
    public ParallelSuite(RunnerBuilder builder, Class<?> klass, Class<?>[] suiteClasses)
            throws InitializationError {
        super(new ParallelBuilder(builder), klass, suiteClasses);
    }

    /**
     * Instantiates a new parallel suite.
     *
     * @param builder the builder
     * @param classes the classes
     * @throws InitializationError the initialization error
     */
    public ParallelSuite(RunnerBuilder builder, Class<?>[] classes) throws InitializationError {
        super(new ParallelBuilder(builder), classes);
    }

    /**
     * Instantiates a new parallel suite.
     *
     * @param klass the klass
     * @param suiteClasses the suite classes
     * @throws InitializationError the initialization error
     */
    public ParallelSuite(Class<?> klass, Class<?>[] suiteClasses) throws InitializationError {
        super(klass, suiteClasses);
    }

    /**
     * We will append the sign for parallel ("||") at the end of the
     * description as indication that the tests will run in parallel.
     *
     * @return name with "||" appended
     * @see org.junit.runners.ParentRunner#getName()
     */
    @Override
    protected String getName() {
        if (Environment.areThreadsAllowed()) {
            String name = super.getName();
            return name + "||";
        }
        return super.getName();
    }

}

