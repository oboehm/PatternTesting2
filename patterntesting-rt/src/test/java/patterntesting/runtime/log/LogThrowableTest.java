/*
 * $Id: LogThrowableTest.java,v 1.9 2016/12/18 20:19:37 oboehm Exp $
 *
 * Copyright (c) 2008 by Oliver Boehm
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
 * (c)reated 09.10.2008 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.log;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import patterntesting.runtime.annotation.LogThrowable;

import static org.junit.Assert.*;

/**
 * The Class LogThrowableTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.9 $
 * @since 09.10.2008
 */
@LogThrowable
public final class LogThrowableTest {

	private static final Logger log = LoggerFactory.getLogger(LogThrowableTest.class);
    private static final LogThrowableAspect logThrowableAspect = LogThrowableAspect.aspectOf();
    private static final Logger aspectLog = logThrowableAspect.getLog();
	private LogRecorder testLog;

	/**
	 * We must now call the setUp() method ourself because we can't ask
	 * JUnit to do the synchronization for us. This is the reason why this
	 * method has no "@Before" annotation.
	 */
    public void setUp() {
        testLog = new LogRecorder();
        logThrowableAspect.setLog(testLog);
    }

	/**
	 * Tear down after class.
	 */
	@AfterAll
	public static void tearDownAfterClass() {
	    logThrowableAspect.setLog(aspectLog);
	}

	/**
	 * Test log.
	 */
	@Test
	public void testLog() {
	    synchronized (logThrowableAspect) {
	        this.setUp();
	        try {
	            throwRuntimeException("do you see me?");
	            fail("RuntimeException expected");
	        } catch (RuntimeException expected) {
	            checkLog("RuntimeException in throwRuntimeException(\"do you see me?\")");
	        }
        }
	}

	@LogThrowable(SimpleLog.LOG_LEVEL_INFO)
    private void throwRuntimeException(final String msg) {
        throw new RuntimeException(msg);
    }

	/**
	 * Test log npe.
	 */
	@Test
	public void testLogNPE() {
        synchronized (logThrowableAspect) {
            this.setUp();
    		try {
    			throwNullPointerException(null);
    			fail("NullPointerException expected");
    		} catch (RuntimeException expected) {
    			checkLog("NullPointerException in throwNullPointerException((null))");
    		}
        }
	}

	private void throwNullPointerException(final String s) {
	    s.length();
	}

    /**
     * Test a static method with an NPE.
     */
    @Test
    public void testStaticMethodWithNPE() {
        synchronized (logThrowableAspect) {
            this.setUp();
            try {
                throwNullPointerExceptionWithStaticMethod(null);
                fail("NullPointerException expected");
            } catch (RuntimeException expected) {
                checkLog("NullPointerException in throwNullPointerExceptionWithStaticMethod((null))");
            }
        }
    }

    private static void throwNullPointerExceptionWithStaticMethod(final String s) {
        s.length();
    }

	/**
	 * Test log assertion error.
	 */
	@Test
	public final void testLogAssertionError() {
        synchronized (logThrowableAspect) {
            this.setUp();
    	    String s = "provocated error";
    	    try {
    	        throwAssertionError(s);
    	    } catch (AssertionError expected) {
    	        log.info(expected.toString());
    	        checkLog("AssertionError in throwAssertionError(\"" + s + "\")");
    	    }
        }
	}

	private void throwAssertionError(final String s) {
	    fail(s);
	}

	private void checkLog(final String expected) {
		checkLog(testLog, expected);
	}

	/**
	 * Check log.
	 *
	 * @param testLog the test log
	 * @param expected the expected
	 */
	protected static void checkLog(final LogRecorder testLog, final String expected) {
        log.debug(testLog.getRecord());
        assertTrue(testLog + " is blank", StringUtils.isNotBlank(testLog.getRecord()));
        assertEquals(1, testLog.getNumberOfRecords());
        assertEquals(expected, testLog.getText());
	}

}
