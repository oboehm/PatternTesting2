/*
 * $Id: MemoryEater.java,v 1.5 2016/12/21 22:09:02 oboehm Exp $
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
 * (c)reated 01.07.2009 by oliver
 */
package patterntesting.sample;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patterntesting.runtime.monitor.MemoryGuard;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This class eats memory, i.e. it uses more and more memory when it's started,
 * It is an example for the use of the MemoryGuard class.
 *
 * @author oliver
 * @version $Revision: 1.5 $
 * @see MemoryGuard
 * @since 01.07.2009
 */
public final class MemoryEater {

	private static final Logger log = LogManager.getLogger(MemoryEater.class);

	/**
	 * We want to log all 100 ms the free memory.
	 */
	public MemoryEater() {
		try {
			MemoryGuard.logFreeMemory(100);
		} catch (InterruptedException e) {
			log.info("free memory will be not logged", e);
		}
	}

	/**
	 * Uses 9/10 of the free memory for caching but nobody will use it
	 * (remember it is only a demo).
	 */
	public void eatCache() {
		long mem = Runtime.getRuntime().maxMemory() / 1024;
		long cacheSize = mem * 9 / 10;
		log.info(mem + " KB is free - will eat " + cacheSize + " KB of it");
		Collection<byte[]> byteCache = new ArrayList<byte[]>();
		for (int i = 0; i < cacheSize; i++) {
			byte[] b = new byte[1024];
			byteCache.add(b);
		}
		log.info(cacheSize + " KB eaten - will now return");
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(final String[] args) {
		MemoryEater memEater = new MemoryEater();
		memEater.eatCache();
	}

}
