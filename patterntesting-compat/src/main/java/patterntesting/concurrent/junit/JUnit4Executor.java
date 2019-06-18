/*
 * $Id: JUnit4Executor.java,v 1.6 2016/01/06 20:47:14 oboehm Exp $
 *
 * Copyright (c) 2009 by Oliver Boehm
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
 * (c)reated 30.11.2009 by oliver (ob@oasd.de)
 */

package patterntesting.concurrent.junit;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * This class is responsible for starting a JUnit4 test method.
 * It is responsible for the setup for JUnit 4. The rest is done by
 * the super class.
 *
 * @author oliver
 * @since 30.11.2009
 */
public class JUnit4Executor extends JUnitExecutor {

    /**
     * Instantiates a new j unit4 executor.
     *
     * @param clazz the class with the test methods (JUnit 4)
     */
    public JUnit4Executor(final Class<?> clazz) {
        super(clazz);
        this.recordResults();
    }
    
    /**
     * Record results.
     *
     * @see patterntesting.concurrent.junit.JUnitExecutor#recordResults()
     */
    @Override
    protected final void recordResults() {
        this.setupMethods();
        if (this.isRunParallelEnabled()) {
            this.callSetupBeforeClass();
            this.recordTestMethods();
        }
    }

    /**
     * Here we look after methods with the JUnit annotations to find the
     * test methods but also the setup and teardown methods.
     */
    private void setupMethods() {
        Method[] methods = this.getTestClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            Annotation testAnnotation = method.getAnnotation(Test.class);
            if (testAnnotation != null) {
                results.put(method.getName(), new Result(method));
                continue;
            }
            if ((this.setupMethod == null)
                    && (method.getAnnotation(Before.class) != null)) {
                this.setupMethod = method;
                continue;
            }
            if ((this.setupBeforeClassMethod == null)
                    && (method.getAnnotation(BeforeClass.class) != null)) {
                this.setupBeforeClassMethod = method;
                continue;
            }
            if ((this.teardownMethod == null)
                    && (method.getAnnotation(After.class) != null)) {
                this.teardownMethod = method;
                continue;
            }
        }
    }

}
