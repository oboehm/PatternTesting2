/*
 * Copyright (c) 2008-2019 by Oliver Boehm
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
 * (c)reated 24.11.2008 by oliver (ob@oasd.de)
 */
package patterntesting.sample;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * The Class CrashTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 */
public final class CrashTest {

	private static final Log log = LogFactoryImpl.getLog(CrashTest.class);

	/**
	 * This method is only for loading the class the first time.
	 */
	@BeforeAll
	public static void setupBeforeClass() {
		String classname = Crash.class.getName();
		try {
			Class<?> cl = Class.forName(classname);
			log.info(cl + " succesful loaded");
		} catch (Throwable t) {
			log.info(classname + " can't be loaded", t);
		}
	}

	/**
	 * Without the annotation "@GuardInitialization" you will get a
	 * <pre>
	 * java.lang.NoClassDefFoundError: Could not initialize class patterntesting.sample.Crash
     *	at patterntesting.sample.CrashTest.testGetDate(CrashTest.java:55)
     *	...
     * </pre>
     * if you call the getDate() method.
	 *
	 * Test method for {@link Crash#getDate()}.
	 */
	@Test
	public void testGetDate() {
		Date date = Crash.getDate();
		assertNotNull(date);
	}

}
