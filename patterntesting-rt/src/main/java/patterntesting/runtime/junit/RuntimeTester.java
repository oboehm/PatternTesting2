/*
 * $Id: RuntimeTester.java,v 1.8 2016/12/18 20:19:38 oboehm Exp $
 *
 * Copyright (c) 2011 by Oliver Boehm
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
 * (c)reated 09.05.2011 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;

/**
 * This is a tester to do some runtime checking like memory checks and other
 * stuff.
 *
 * @author oliver
 * @since 1.1.1 (09.05.2011)
 */
public final class RuntimeTester {

	private static final Logger LOG = LogManager.getLogger(RuntimeTester.class);

	/** Utility class - no need to instantiate it. */
	private RuntimeTester() {
	}

	/**
	 * If you have some tests which needs a certain amount of memory this test
	 * is for you. This tests guarantees you that the VM is started with enough
	 * memory (usually option "-Xmx").
	 *
	 * @param required
	 *            the max memory in MB
	 */
	public static void assertMaxMemory(final int required) {
		long mem = (Runtime.getRuntime().maxMemory() + 0x7FFFF) / 0x100000;
		LOG.debug("VM was started with " + mem + " MB max memory.");
		if (required > mem) {
			String hint = (" (use 'java -Xmx" + required + "m ...')");
			if (SystemUtils.JAVA_VENDOR.toUpperCase().startsWith("IBM")) {
				hint = "";
			}
			throw new AssertionError(required + " MB max memory required but only " + mem + " MB available" + hint);
		}
	}

	/**
	 * If you need a certain amount of free memory you can assert it with this
	 * method here.
	 *
	 * @param required
	 *            the required free memory in MB
	 */
	public static void assertFreeMemory(final int required) {
		long free = Runtime.getRuntime().freeMemory() / 0x100000;
		if (required > free) {
			System.gc();
		}
		free = (Runtime.getRuntime().freeMemory() + 0x7FFFF) / 0x100000;
		LOG.debug(free + " MB memory is free.");
		if (required > free) {
			long maxMem = Runtime.getRuntime().maxMemory() / 0x100000 + required + 4 - free;
			String hint = " (use 'try -Xmx" + maxMem + "m ...')";
			throw new AssertionError(required + " MB free memory required but only " + free + " MB are free" + hint);
		}
	}

}
