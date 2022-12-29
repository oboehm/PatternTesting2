/*
 * $Id: ResourceTest.java,v 1.4 2016/12/18 20:19:39 oboehm Exp $
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
 * (c)reated 05.01.2016 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.io;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.jupiter.api.Test;
import clazzfish.monitor.ClasspathMonitor;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link Resource} class.
 *
 * @author oliver
 * @version $Revision: 1.4 $
 * @since 1.6 (05.01.2016)
 */
public class ResourceTest {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceTest.class);

    /**
     * Test method for {@link Resource#getModificationDate()}.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testGetModificationDateOfFile() throws IOException {
        File file = new File("src/test/java/patterntesting/runtime/io/ResourceTest.java");
        assertTrue(file.exists(), "does not exist: " + file);
        Date modified = getModificationTimeOf(file.toURI());
        assertEquals(file.lastModified(), modified.getTime());
    }

    /**
     * Test method for {@link Resource#getModificationDate()}.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testGetModificationDateOfJarResource() throws IOException {
        URI uri = ClasspathMonitor.getInstance().whichClass(Logger.class);
        Date modified = getModificationTimeOf(uri);
        assertNotNull(modified, "modification time expected for " + uri);
    }

    private static Date getModificationTimeOf(final URI uri) throws IOException {
        Resource resource = new Resource(uri);
        Date modified = resource.getModificationDate();
        LOG.info("Modification time of {} is {}.", resource, modified);
        return modified;
    }

}
