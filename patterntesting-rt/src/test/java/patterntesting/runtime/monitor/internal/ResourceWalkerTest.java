/*
 * $Id: ResourceWalkerTest.java,v 1.10 2016/12/18 20:19:38 oboehm Exp $
 *
 * Copyright (c) 2016 by Oliver Boehm
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
 * (c)reated 24.04.16 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.monitor.internal;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.Test;

/**
 * Unit-Tests fuer {@link ResourceWalker} class.
 *
 * @author <a href="ob@aosd.de">oliver</a>
 * @version $Revision: 1.10 $
 * @since 1.6.4 (24.04.16)
 */
public final class ResourceWalkerTest {

    private static final Logger LOG = LogManager.getLogger(ResourceWalkerTest.class);
    private static final File CLASSES_DIR = new File("target", "test-classes");

    /**
     * Test method for {@link ResourceWalker#getResources()}
     *
     * @throws IOException in case of trouble
     */
    @Test
    public void testGetResources() throws IOException {
        ResourceWalker walker = new ResourceWalker(CLASSES_DIR, ".properties");
        Collection<String> resources = getResourcesFrom(walker);
        assertThat(resources, hasItem("/patterntesting/runtime/util/test.properties"));
    }

    /**
     * Test get all resources.
     *
     * @throws IOException the io exception
     */
    @Test
    public void testGetAllResources() throws IOException {
        ResourceWalker walker = new ResourceWalker(CLASSES_DIR);
        Collection<String> resources = getResourcesFrom(walker);
        assertThat(resources, not(hasItem("/META-INF")));
        assertThat(resources, hasItem("/patterntesting/runtime/util/test.properties"));
    }

    private static Collection<String> getResourcesFrom(ResourceWalker walker) throws IOException {
        Collection<String> resources = walker.getResources();
        assertFalse("resources expected", resources.isEmpty());
        LOG.info("{} resources found: {}", resources.size(), resources);
        for (String rsc : resources) {
            assertThat(rsc, not(endsWith(".class")));
        }
        return resources;
    }

    /**
     * Here we test if we can get the directories only.
     *
     * @throws IOException the io exception
     */
    @Test
    public void testGetDirResources() throws IOException {
        ResourceWalker walker = new ResourceWalker(CLASSES_DIR, DirectoryFileFilter.DIRECTORY);
        Collection<String> packages = walker.getPackages();
        assertThat(packages, hasItem("patterntesting/"));
        assertFalse("contain null values: " + packages, packages.contains(null));
        LOG.info("{} packages found: {}", packages.size(), packages);
    }

}
