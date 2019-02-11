/**
 * $Id: LoggerTest.java,v 1.5 2016/01/06 20:47:32 oboehm Exp $
 *
 * Copyright (c) 2007 by Oliver Boehm
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
 * (c)reated 04.12.2007 by oliver (ob@oasd.de)
 */
package patterntesting.tool.aspectj;

import org.aspectj.lang.reflect.SourceLocation;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * The Class LoggerTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 04.12.2007
 */
public final class LoggerTest {

	private final Logger logger = Logger.getInstance();

	/**
	 * Test method for {@link patterntesting.tool.aspectj.Logger#getInstance()}.
	 */
	@Test
	public void testGetInstance() {
		Logger instance = Logger.getInstance();
		assertNotNull(instance);
	}

	/**
	 * Test method for {@link patterntesting.tool.aspectj.Logger#getResultFile()}.
	 */
	@Test
	public void testGetResultFile() {
		File file = logger.getResultFile();
		assertTrue(file.delete(), file + " can't be deleted");
		logger.reset();
		logger.logPTViolation(getSourceLocationMock(), "test-message");
		assertTrue(file.exists(), file + " doesn't exist");
	}

	private SourceLocation getSourceLocationMock() {
		SourceLocation mock = createMock(SourceLocation.class);
		reset(mock);
		expect(mock.getFileName()).andStubReturn("mock-file");
		expect(mock.getLine()).andStubReturn(42);
		replay(mock);
		return mock;
	}

}
