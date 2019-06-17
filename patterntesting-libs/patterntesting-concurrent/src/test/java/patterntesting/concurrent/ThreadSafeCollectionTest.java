/**
 * $Id: ThreadSafeCollectionTest.java,v 1.6 2016/12/18 21:56:49 oboehm Exp $
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
 * (c)reated 06.10.2008 by oliver (ob@oasd.de)
 */
package patterntesting.concurrent;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

import org.junit.Test;
import org.apache.logging.log4j.*;

import patterntesting.annotation.concurrent.ForceThreadSafeCollection;

/**
 * This is only a test class to check if the warnings from the
 * ForceThreadSafeCollection aspect are working.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.6 $
 * @see patterntesting.annotation.concurrent.ForceThreadSafeCollection
 * @since 06.10.2008
 */
//@ForceThreadSafeCollection
public class ThreadSafeCollectionTest {

	private static Logger log = LogManager.getLogger(ThreadSafeCollectionTest.class);

	/**
	 * If you mark this test method with "@ForceThreadSafeCollection" you
	 * should get a warning of each line.
	 */
	//@ForceThreadSafeCollection
	@Test
	public final void unsafeMethod() {
		Map<Long, BigDecimal> primeNumbers = new LinkedHashMap<Long, BigDecimal>();
		Map<Long, BigDecimal> map = new TreeMap<Long, BigDecimal>(primeNumbers);
		Set<Long> set = new TreeSet<Long>(map.keySet());
		List<Long> list = new ArrayList<Long>(set);
		Queue<Long> queue = new PriorityQueue<Long>(list);
        log.info(queue + " created");
	}

	/**
	 * These are the same calls as in unsafeMethod() except that the
	 * thread-safe collection classes are used.
	 *
	 * @see ThreadSafeCollectionTest#unsafeMethod()
	 */
	@ForceThreadSafeCollection
	@Test
	public final void safeMethod() {
		Map<Long, BigDecimal> primeNumbers = new Hashtable<Long, BigDecimal>();
        Map<Long, BigDecimal> map = new ConcurrentHashMap<Long, BigDecimal>(primeNumbers);
		Set<Long> set = new CopyOnWriteArraySet<Long>(map.keySet());
		List<Long> list = new Vector<Long>(set);
		Queue<Long> queue = new PriorityBlockingQueue<Long>(list);
		log.info(queue + " created");
	}

}
