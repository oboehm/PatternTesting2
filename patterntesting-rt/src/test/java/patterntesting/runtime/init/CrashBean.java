/*
 * $Id: CrashBean.java,v 1.6 2016/12/18 20:19:43 oboehm Exp $
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
 * (c)reated 22.11.2008 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.init;

import java.util.Date;

import org.slf4j.LoggerFactory;
import org.slf4j.*;

import patterntesting.runtime.annotation.GuardInitialization;

/**
 * When a class fails during static initialization (e.g. during a static block
 * in the beginning) you'll get later an suspicious ClassNotFoundException.
 * This is simulated with this test bean.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.6 $
 * @since 22.11.2008
 */
@GuardInitialization
public class CrashBean {

	/** This initialization will fail if you don't have log4j.jar in your classpath */
	private static final Logger LOG = LoggerFactory.getLogger(CrashBean.class);

	/** This initialization always fails. @see Crash#Crash() */
	protected static final Crash crash = new Crash();

	/** This initialization is ok. */
	protected static final Date createdAt;

	static {
		provocateException();
		createdAt = new Date();
		LOG.info("CrashBean initialized");
	}

	/** Utility class - no need to instantiate it */
	private CrashBean() {}

	private static void provocateException() {
		throw new RuntimeException("provocated");
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public static Date getDate() {
		return new Date();
	}

}
