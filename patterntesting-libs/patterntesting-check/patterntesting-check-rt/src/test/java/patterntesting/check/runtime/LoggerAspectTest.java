/*
 * $Id: LoggerAspectTest.java,v 1.5 2016/12/18 21:59:31 oboehm Exp $
 *
 * Copyright (c) 2014 by Oliver Boehm
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
 * (c)reated 06.02.2014 by oliver (ob@oasd.de)
 */

package patterntesting.check.runtime;

import org.slf4j.*;

import org.junit.jupiter.api.Test;
import patterntesting.annotation.check.runtime.SuppressLoggerWarning;

/**
 * This is the test class for the LoggerAspect. To see the warnings in the log
 * comment out "@SuppressLoggerWarning" before the class definition of this
 * unit test.
 *
 * @author oliver
 * @since 1.4.1 (06.02.2014)
 */
@SuppressLoggerWarning
public class LoggerAspectTest {

    private static final Logger LOG = LoggerFactory.getLogger(Logger.class);

    /**
     * This is one of the test methods for the LoggerAspect for the static
     * {@link Logger} used in this unit test (see above).
     *
     * You should see a warning in the log because
     * {@link LoggerFactory#getLogger(Class)} is not called with
     * "LoggerAspectTest.class" as argument.
     * <p>
     * Watch the log - it is more or less a manual test.
     * </p>
     */
    @Test
    public void testStaticLogger() {
        LOG.info("Hello static world!");
    }

    /**
     * This is one of the test methods for the LoggerAspect. You should see
     * a warning in the log because {@link LoggerFactory#getLogger(Class)}
     * is not called with "LoggerAspectTest.class" as argument.
     * <p>
     * Watch the log - it is more or less a manual test.
     * </p>
     */
    @Test
    public void testSLF4JLogger() {
        Logger logger = LoggerFactory.getLogger(String.class);
        logger.info("Hello {}!", logger);
    }

    /**
     * This is the same test as {@link #testSLF4JLogger()} but now for the JDK
     * logger.
     */
    @Test
    public void testJdkLogger() {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger("java.lang.String");
        logger.info("Hello " + logger);
        logger = java.util.logging.Logger.getLogger("Hello");
        logger.info("World");
    }

    /**
     * This is the same test as {@link #testSLF4JLogger()} but now for the Log4J
     * logger.
     */
    @Test
    public void testLog4JLogger() {
        org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(String.class);
        logger.info("Hello " + logger);
    }

    /**
     * This is the same test as {@link #testSLF4JLogger()} but now for commons
     * logging.
     */
    @Test
    public void testCommonsLogger() {
        org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(String.class);
        logger.info("Hello " + logger);
    }

}
