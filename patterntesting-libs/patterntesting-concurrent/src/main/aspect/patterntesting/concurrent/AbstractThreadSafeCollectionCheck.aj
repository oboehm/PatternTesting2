/*
 * $Id: AbstractThreadSafeCollectionCheck.aj,v 1.1 2011/12/22 17:28:11 oboehm Exp $
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
 * (c)reated 07.10.2008 by oliver (ob@oasd.de)
 */
package patterntesting.concurrent;

import java.util.*;
import java.util.concurrent.*;

/**
 * Classes addressed by the pointcut "applicationCode" should use only the
 * thread-safe Java collections, e.g.
 * <ul>
 * <li>Map: Hashtable, SynchronizedMap or ConcurrentMap</li>
 * <li>Collection: Vector, CopyOnWriteArrayList, CopyOnWriteArraySet or
 * SynchronizedList
 * </li>
 * <li>Queue: BlockingQueue or ConcurrentLinkedQueue</li>
 * </ul>
 * (see "Java Concurrency in Practice" (Brian Goetz), p. 52).
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 06.10.2008
 * @version $Revision: 1.1 $
 */
public abstract aspect AbstractThreadSafeCollectionCheck {

	/**
	 * Specify what the application code is that should be subject to the
	 * pattern test.
	 * <br/>
	 * Ex: <code>pointcut applicationCode(): within(patterntesting.sample.*)</code>
	 */
	public abstract pointcut applicationCode();

	/**
	 * Allowed Map constructors.
	 */
	pointcut allowedMapConstructors() :
		call(Hashtable+.new(..))
		|| call(ConcurrentMap+.new(..));
	
	/**
	 * Allowed Set constructors.
	 */
	pointcut allowedSetConstructors() :
		call(CopyOnWriteArraySet+.new(..))
		|| allowedMapConstructors();
	
	/**
	 * Allowed List constructors.
	 */
	pointcut allowedListConstructors() :
		call(Vector+.new(..))
		|| call(CopyOnWriteArrayList+.new(..));
	
	/**
	 * Allowed Queue constructors.
	 */
	pointcut allowedQueueConstructors() :
		call(BlockingQueue+.new(..))
		|| call(ConcurrentLinkedQueue+.new(..));
	
	/**
	 * Forbidden HashMap constructors.
	 */
	public pointcut forbiddenHashMapConstructors() :
		call(HashMap+.new(..));
	
	/**
	 * Forbidden Map constructors.
	 */
	public pointcut forbiddenMapConstructors() :
		call(Map+.new(..))
		&& !forbiddenHashMapConstructors()
		&& !allowedMapConstructors();
	
	/**
	 * Forbidden Set constructors.
	 */
	public pointcut forbiddenSetConstructors() :
		call(Set+.new(..))
		&& !allowedSetConstructors();
	
	/**
	 * Forbidden List constructors.
	 */
	public pointcut forbiddenListConstructors() :
		call(List+.new(..))
		&& !allowedListConstructors();
	
	/**
	 * Forbidden Queue constructors.
	 */
	pointcut forbiddenQueueConstructors() :
		call(Queue+.new(..))
		&& !allowedQueueConstructors();
	
	/**
	 * Declare warning for forbidden call of HashMap constructor.
	 */
	declare warning :
			forbiddenHashMapConstructors()
			&& applicationCode() :
		"use ConcurrentHashMap or HashTable instead of HashMap";
	
	/**
	 * Declare warning for forbidden call of Map constructor.
	 */
	declare warning :
			forbiddenMapConstructors()
			&& applicationCode() :
		"use thread-safe library collection";
	
	/**
	 * Declare warning for forbidden call of Set constructor.
	 */
	declare warning :
			forbiddenSetConstructors()
			&& applicationCode() :
		"use CopyOnWriteArraySet";

	/**
	 * Declare warning for forbidden call of List constructor.
	 */
	declare warning :
			forbiddenListConstructors()
			&& applicationCode() :
		"use Vector or CopyOnWriteArrayList";
	
	/**
	 * Declare warning for forbidden call of Queue constructor.
	 */
	declare warning :
			forbiddenQueueConstructors()
			&& applicationCode() :
		"use BlockingQueue or ConcurrentLinkedQueue";
	
}
