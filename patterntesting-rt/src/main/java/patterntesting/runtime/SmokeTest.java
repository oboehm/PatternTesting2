/*
 * $Id: SmokeTest.java,v 1.10 2016/12/18 20:19:41 oboehm Exp $
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
 * (c)reated 29.07.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.FileFilter;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.Test;

/**
 * This tests checks some preconditions for PatternTesting Runtime. If one of
 * these tests fails PatternTesting may work not correct. This class is part of
 * src/main/java so that everybody can call this tests to check if one of the
 * preconditions are broken.
 * <p>
 * This class is not final so that you can extend it for your own needs.
 * </p>
 *
 * @author oliver
 * @since 1.0.2 (29.07.2010)
 */
public class SmokeTest {

	private static final Logger LOG = LogManager.getLogger(SmokeTest.class);

	/**
	 * For the ClasspathMonitor and ClassWalker we need the commons-io lib. And
	 * at least version 1.3.1 of commons-io is needed because the FileFilter and
	 * DirectoryWalker of this lib is used.
	 * <p>
	 * If you use commons-io-1.2 you may get a NoSuchMethodError and
	 * </p>
	 *
	 * <pre>
	 * java.lang.NoSuchMethodError: org.apache.commons.io.filefilter.FileFilterUtils.fileFileFilter()Lorg/apache/commons/io/filefilter/IOFileFilter;
	 *    patterntesting.runtime.monitor.internal.ClassWalker.getFileFilter(ClassWalker.java:65)
	 *    patterntesting.runtime.monitor.internal.ClassWalker.&lt;init&gt;(ClassWalker.java:48)
	 *    patterntesting.runtime.monitor.ClasspathMonitor.addClassesFromDir_aroundBody38(ClasspathMonitor.java:884)
	 *    patterntesting.runtime.monitor.ClasspathMonitor.addClassesFromDir_aroundBody39$advice(ClasspathMonitor.java:49)
	 *    patterntesting.runtime.monitor.ClasspathMonitor.addClassesFromDir(ClasspathMonitor.java:1)
	 *    patterntesting.runtime.monitor.ClasspathMonitor.addClasses(ClasspathMonitor.java:864)
	 *    patterntesting.runtime.monitor.ClasspathMonitor.getClasspathClassSet_aroundBody36(ClasspathMonitor.java:847)
	 *    patterntesting.runtime.monitor.ClasspathMonitor.getClasspathClassSet_aroundBody37$advice(ClasspathMonitor.java:49)
	 *    patterntesting.runtime.monitor.ClasspathMonitor.getClasspathClassSet(ClasspathMonitor.java:1)
	 *    patterntesting.runtime.monitor.ClasspathMonitor.&lt;init&gt;(ClasspathMonitor.java:119)
	 *    patterntesting.runtime.monitor.ClasspathMonitor.&lt;clinit&gt;(ClasspathMonitor.java:109)
	 *    ...
	 * </pre>
	 * <p>
	 * as stacktrace.
	 * </p>
	 */
	@Test
	public final void testCommonsIO() {
		try {
			FileFilter filter = FileFilterUtils.fileFileFilter();
			assertNotNull(filter);
		} catch (NoSuchMethodError e) {
			LOG.error("your commons-io lib is probably to old", e);
			fail("use commons-io-1.3.1.jar or newer (" + e + ")");
		}
	}

}
