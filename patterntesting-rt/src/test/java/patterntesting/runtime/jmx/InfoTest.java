/*
 * Copyright (c) 2013 by Oli B.
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
 * (c)reated 06.04.2014 by Oli B. (boehm@javatux.de)
 */

package patterntesting.runtime.jmx;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.*;
import java.util.jar.Manifest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.Test;

/**
 * Unit tests for {@link Info} class.
 *
 * @author oliver (boehm@javatux.de)
 * @since 1.4 (06.04.2014)
 */
public final class InfoTest {

    private static final Logger LOG = LogManager.getLogger(InfoTest.class);
    private final Info info = new Info();

    /**
     * Test method for {@link Info#getVersion()}.
     */
    @Test
    public void testGetVersion() {
        String version = info.getVersion();
        assertTrue("version should be not empty", StringUtils.isNotBlank(version));
        assertFalse(version + " version", version.equalsIgnoreCase("unknown"));
        LOG.info("version = {}", version);
    }

    /**
     * Test method for {@link Info#getBuildTime()}.
     */
    @Test
    public void testGetBuildTime() {
        Date buildTime = info.getBuildTime();
        assertNotNull("invalid: " + buildTime, buildTime);
        LOG.info("buildTime = {}", buildTime);
    }

    /**
     * Test method for {@link Info#getManifest()}.
     */
    @Test
    public void testGetManifest() {
        checkManifest(Info.PATTERNTESTING);
    }

    /**
     * Test method for {@link Info#getManifest()}.
     */
    @Test
    public void testGetManifestOfJarClass() {
        Info buildInfo = new Info(StringUtils.class);
        checkManifest(buildInfo);
    }

    private static void checkManifest(final Info buildInfo) {
        Manifest manifest = buildInfo.getManifest();
        assertNotNull("manifest expected", manifest);
        assertFalse("empty " + manifest, manifest.getMainAttributes().isEmpty());
    }

    /**
     * Test method for {@link Info#getManifestURI()}.
     * @since 1.6
     */
    @Test
    public void testGetManifestURI() {
    	URI uri = info.getManifestURI();
    	assertNotNull("URI expected", uri);
    	LOG.info("uri = {}", uri);
    }

    /**
     * Test method for {@link Info#getProperties()}.
     */
    @Test
    public void testGetProperties() {
        Properties props = info.getProperties();
        assertFalse("properties should be not empty", props.isEmpty());
        LOG.info("Build properties = {}", props);
    }

    /**
     * Test method for {@link Info#getInfos()}.
     */
    @Test
    public void testGetInfos() {
        SortedMap<String, String> infos = info.getInfos();
        assertTrue("unsorted: " + infos, infos.firstKey().compareTo(infos.lastKey()) < 0);
    }

    /**
     * Here we test the fallback behaviour of the {@link Info} class. Even if
     * there are no info properties we should get a reasonable version.
     */
    @Test
    public void testErrorHandling() {
        Info info = new Info(String.class);
        LOG.info("info = '{}'", info);
        assertEquals(SystemUtils.JAVA_VERSION, info.getVersion());
    }

}

