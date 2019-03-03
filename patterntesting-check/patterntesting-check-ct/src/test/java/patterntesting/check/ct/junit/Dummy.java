/*
 * $Id: Dummy.java,v 1.2 2016/12/18 21:58:55 oboehm Exp $
 *
 * Copyright (c) 2012 by Oliver Boehm
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
 * (c)reated 29.12.2011 by oliver (ob@oasd.de)
 */

package patterntesting.check.ct.junit;

import org.apache.logging.log4j.*;

import patterntesting.annotation.check.ct.OnlyForTesting;

/**
 * This is only a dummy class for testing.
 * 
 * @author oliver
 * @since 1.2 (29.12.2011)
 */
public class Dummy {
    
    private static final Logger log = LogManager.getLogger(Dummy.class);
    
    /**
     * Instantiates a new dummy. But this is allowed only for testing!
     */
    @OnlyForTesting
    public Dummy() {
        log.debug("Dummy created.");
    }

}

