/*
 * $Id: ResourcepathDiggerTest.java,v 1.3 2017/08/19 15:02:49 oboehm Exp $
 *
 * Copyright (c) 2017 by Oliver Boehm
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
 * (c)reated 27.05.2017 by oboehm (ob@oasd.de)
 */

package patterntesting.runtime.monitor.internal;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.io.*;
import java.net.*;
import java.util.Set;

import org.junit.Test;

/**
 * Unit tests for ResourcepathDigger class.
 * 
 * @author oboehm
 * @version $Revision: 1.3 $
 */
public final class ResourcepathDiggerTest extends AbstractDiggerTest {

    /**
     * Here we test the digging of a classpath inside a WAR.
     * 
     * @throws MalformedURLException from using an {@link URLClassLoader}
     */
    @Test
    public void testWarClasses() throws MalformedURLException {
        ResourcepathDigger warDigger = createResourcepathDigger(WORLD_WAR, "!/WEB-INF/classes");
        checkGetResources(warDigger, "/patterntesting/sample/default.properties");
    }

    /**
     * Here we test the digging of a JAR a WAR. E.g. here we have now a nested
     * JAR hierarchy to parse.
     * 
     * @throws MalformedURLException from using an {@link URLClassLoader}
     */
    @Test
    public void testWarJar() throws MalformedURLException {
        ResourcepathDigger warDigger = createResourcepathDigger(WORLD_WAR,
                "!/WEB-INF/lib/patterntesting-agent-1.6.3.jar");
        checkGetResources(warDigger, "/patterntesting/agent/logging.properties");
    }

    /**
     * Here we test the next level of nesting: a JAR inside a WAR inside an EAR.
     *
     * @throws MalformedURLException from using an {@link URLClassLoader}
     */
    @Test
    public void testEarWarJar() throws MalformedURLException {
        ResourcepathDigger earDigger = createResourcepathDigger(WORLD_EAR,
                "!/world.war!/WEB-INF/lib/patterntesting-agent-1.6.3.jar");
        checkGetResources(earDigger, "/patterntesting/agent/logging.properties");
    }

    /**
     * Here we test the next level of nesting: a classes directory inside a WAR
     * inside an EAR.
     *
     * @throws IOException if ear file cannot be read
     */
    @Test
    public void testEarWarClasses() throws IOException {
        ResourcepathDigger earDigger = createResourcepathDigger(WORLD_EAR, "!/world.war!/WEB-INF/classes");
        checkGetResources(earDigger, "/patterntesting/sample/default.properties");
    }

    private static ResourcepathDigger createResourcepathDigger(File jar, String path) throws MalformedURLException {
        URLClassLoader mockedClassLoader = mockURLClassLoader(jar, path);
        return new ResourcepathDigger(mockedClassLoader);
    }

    private void checkGetResources(ResourcepathDigger warDigger, String resourcename) {
        Set<String> resources = warDigger.getResources();
        assertThat(resources, hasItem(resourcename));
    }
    
    /**
     * Directories are no files and therefore no resources.
     * 
     * @throws MalformedURLException from using an {@link URLClassLoader}
     */
    @Test
    public void testNoDirRessoures() throws MalformedURLException {
        ResourcepathDigger warDigger = createResourcepathDigger(WORLD_WAR,
                "!/WEB-INF/lib/patterntesting-agent-1.6.3.jar");
        Set<String> resources = warDigger.getResources();
        assertThat(resources, not(hasItem("META-INF")));
        assertThat(resources, not(hasItem("META-INF/")));
        assertThat(resources, not(hasItem("/META-INF")));
        assertThat(resources, not(hasItem("/META-INF/")));
    }

}
