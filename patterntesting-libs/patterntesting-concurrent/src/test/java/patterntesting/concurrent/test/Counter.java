/*
 * $Id: Counter.java,v 1.5 2016/12/18 21:56:49 oboehm Exp $
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

package patterntesting.concurrent.test;

import org.apache.logging.log4j.*;

import patterntesting.annotation.concurrent.RunParallel;

/**
 * This is a test class for the RunParallelTest. It counts only how often
 * it is created.
 *
 * @author oliver
 * @version $Revision: 1.5 $
 * @since 10.07.2009
 */
public final class Counter {

	private static final Logger log = LogManager.getLogger(Counter.class);
	
    /** Counts how often this class will be created. */
    private static int created = 0;
    
    private long createdAt = System.currentTimeMillis();

    /**
     * Instantiates a new counter.
     */
    @RunParallel(2)
    public Counter() {
    	created++;
    	log.info(created + ". Counter created");
    }
    
    /**
     * Gets the number of instances.
     *
     * @return the number of instances
     */
    public static int getNumberOfInstances() {
        return created;
    }

    /**
     * To string.
     *
     * @return the string
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Counter" + createdAt;
    }

}
