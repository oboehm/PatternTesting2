/**
 * $Id: LogRuntimeExceptionTest.java,v 1.8 2016/12/18 20:19:36 oboehm Exp $
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

import static org.junit.Assert.fail;

import org.apache.logging.log4j.*;
import org.junit.*;

import patterntesting.runtime.annotation.LogRuntimeException;

/**
 * The Class LogRuntimeExceptionTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.8 $
 * @since 09.10.2008
 */
@LogRuntimeException
public class LogRuntimeExceptionTest {

    private static final LogRuntimeExceptionAspect logThrowableAspect =
            LogRuntimeExceptionAspect.aspectOf();
    private static final Logger aspectLog = logThrowableAspect.getLog();
	private LogRecorder testLog;

	/**
	 * Setup.
	 */
	@Before
	public void setUp() {
		testLog = new LogRecorder();
		logThrowableAspect.setLog(testLog);
	}

	/**
	 * Tear down after class.
	 */
	@AfterClass
	public static void tearDownAfterClass() {
	    logThrowableAspect.setLog(aspectLog);
	}

	/**
	 * Test log.
	 */
	@Test
	public void testLog() {
	    synchronized (aspectLog) {
    		try {
    			throwRuntimeException("do you see me?");
    			fail("RuntimeException expected");
    		} catch (RuntimeException expected) {
    			checkLog("RuntimeException in throwRuntimeException(\"do you see me?\")");
    		}
        }
	}

    @LogRuntimeException(SimpleLog.LOG_LEVEL_INFO)
    private void throwRuntimeException(final String msg) {
        throw new RuntimeException(msg);
    }

	/**
	 * Test log NPE.
	 */
	@Test
	public void testLogNPE() {
        synchronized (aspectLog) {
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
        synchronized (aspectLog) {
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

	private void checkLog(final String expected) {
	    LogThrowableTest.checkLog(testLog, expected);
	}

}
