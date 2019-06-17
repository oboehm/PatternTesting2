/**
 * $Id: DeadLockListener.java,v 1.4 2016/01/06 20:47:14 oboehm Exp $
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
 * (c)reated 10.07.2009 by oliver (ob@oasd.de)
 */

package patterntesting.concurrent;

/**
 * If you want to be notified if a dead lock happens you must implement
 * this interface.
 *
 * @author oliver
 * @version $Revision: 1.4 $
 * @since 10.07.2009
 */
public interface DeadLockListener {

	/**
	 * This method will be called if a dead lock will be detected.
	 * @param threads the detected deadlocks
	 */
	void deadLockDetected(Thread[] threads);

}
