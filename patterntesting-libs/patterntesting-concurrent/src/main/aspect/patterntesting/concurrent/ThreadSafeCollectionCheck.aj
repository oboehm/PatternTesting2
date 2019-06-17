/*
 * $Id: ThreadSafeCollectionCheck.aj,v 1.1 2011/12/22 17:28:11 oboehm Exp $
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

import patterntesting.annotation.concurrent.ForceThreadSafeCollection;

/**
 * If you mark a class with "@ForceThreadSafeCollection" you should use only the
 * thread-safe Java collections, e.g.
 * <ul>
 * <li>Map: Hashtable, SynchronizedMap or ConcurrentMap</li>
 * <li>Collection: Vector, CopyOnWriteArrayList, CopyOnWriteArraySet or
 * SynchronizedList
 * </li>
 * <li>Queue: BlockingQueue or ConcurrentLinkedQueue</li>
 * </ul>
 * (see "Java Concurrency in Practice" (Brian Goetz), p. 52)
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 06.10.2008
 */
public aspect ThreadSafeCollectionCheck extends AbstractThreadSafeCollectionCheck {
    
	/**
	 * Application code.
	 */
	public pointcut applicationCode() :
		within(@ForceThreadSafeCollection *)
		|| withincode(@ForceThreadSafeCollection *..*.new(..))
		|| withincode(@ForceThreadSafeCollection !synchronized * *..*.*(..));	

}
