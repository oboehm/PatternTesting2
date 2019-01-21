/*
 * $Id: SmokeTest.java,v 1.3 2016/12/30 19:07:44 oboehm Exp $
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
 * (c)reated 12.01.2011 by oliver (ob@oasd.de)
 */

package patterntesting.tool;

import clazzfish.monitor.ClasspathMonitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertNotNull;


/**
 * This is a simple smoke test to check some preconditions. If these
 * conditions fail it make no sense to do start other tests.
 * 
 * @author oliver
 * @since 1.1 (12.01.2011)
 */
public class SmokeTest {
    
    private static final Logger LOG = LogManager.getLogger(SmokeTest.class);
    
    /**
     * Here we test some resources which are needed for patterntesting-tools
     * as Maven plugin.
     */
    @Test
    public void testArchetypeResources() {
        URL archetypeURL = ClasspathMonitor.getResource("/META-INF/maven/archetype-metadata.xml");
        assertNotNull(archetypeURL);
        LOG.info("archetypeURL={}", archetypeURL);
    }

}

