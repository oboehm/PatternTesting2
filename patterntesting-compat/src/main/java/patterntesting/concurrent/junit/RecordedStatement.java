/*
 * $Id: RecordedStatement.java,v 1.12 2016/12/18 21:56:49 oboehm Exp $
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
 * (c)reated 21.03.2010 by oliver (ob@oasd.de)
 */

package patterntesting.concurrent.junit;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;
import patterntesting.runtime.junit.internal.ProfiledStatement;

/**
 * This class is used by the {@link ParallelRunner} to run the statements in
 * parallel and to record it for later evaluation.
 *
 * @author oliver
 * @since 1.0 (21.03.2010)
 */
public final class RecordedStatement extends ProfiledStatement {

    private static final Logger log = LoggerFactory.getLogger(RecordedStatement.class);
    private Throwable thrown;

    /**
     * The default constructor for this class if the call of the
     * frameworkMethod was ok.
     *
     * @param testClass the test class
     * @param frameworkMethod the FrameworkMethod
     */
    public RecordedStatement(final TestClass testClass, final FrameworkMethod frameworkMethod) {
        super(testClass, frameworkMethod);
    }

    /**
     * The default constructor for this class if the call of the
     * frameworkMethod failed with an exception.
     *
     * @param testClass the test class
     * @param frameworkMethod the FrameworkMethod
     * @param thrown the exception to be recorded
     */
    public RecordedStatement(final TestClass testClass, final FrameworkMethod frameworkMethod,
            final Throwable thrown) {
        this(testClass, frameworkMethod);
        this.thrown = thrown;
    }

    /**
     * If the called test method throws an exception this exception can be set
     * here.
     *
     * @param t the thrown exception
     */
    public void setThrown(final Throwable t) {
        this.thrown = t;
    }

    /**
     * Plays the recorded action, throwing a {@code Throwable} if anything
     * went wrong. Additionally the measured times will be printed to the
     * log. As logger we will use the logger of ParallelRunner because this
     * is an internal class and the user probably expects this output for
     * logger of ParallelRunner.
     *
     * @throws Throwable the recorded Throwable
     */
    @Override
    public void evaluate() throws Throwable {
        if (this.thrown != null) {
            if (log.isTraceEnabled()) {
                log.trace("recorded " + this.getMethodName() + " throws "
                        + thrown);
            }
            throw thrown;
        }
    }

    /**
     * If the expected exception matches the recorded exception the test is
     * marked as "ok" (i.e. the thrown exception is removed from the record).
     * <p>
     * If the expected exception does not match the same AssertionError as
     * in {@link org.junit.internal.runners.statements.ExpectException} is
     * stored.
     * </p>
     *
     * @param expected the expected exception
     */
    public void setExpected(final Class<? extends Throwable> expected) {
        if (this.thrown == null) {
            this.thrown = new AssertionError("Expected exception: " + expected.getName());
        } else {
            if (expected.isAssignableFrom(this.thrown.getClass())) {
                this.thrown = null;
            } else {
                this.thrown = new AssertionError(
                        "Unexpected exception, expected<" + expected.getName()
                                + "> but was<"
                                + this.thrown.getClass().getName() + ">");
            }
        }
    }

    /**
     * Returns true if no exception was thrown.
     * @return true if recorded statement has no exception.
     */
    public boolean success() {
        return this.thrown == null;
    }

    /**
     * This is the opposite of {@link #success()}.
     * @return true if recorded statement has an exception.
     */
    public boolean failed() {
        return this.thrown != null;
    }

}

