/*
 * $Id: EnvironmentTest.java,v 1.19 2016/12/30 22:13:15 oboehm Exp $
 *
 * Copyright (c) 2010 by Oliver Boehm
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
 * (c)reated 01.02.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.util;

import static org.junit.Assert.*;

import java.io.*;
import java.util.Properties;

import org.apache.logging.log4j.*;
import org.junit.Test;

/**
 * Unit tests for {@link Environment} class.
 *
 * @author oliver
 * @since 1.0 (01.02.2010)
 */
public final class EnvironmentTest {

    private static final Logger LOG = LogManager.getLogger(EnvironmentTest.class);

    /**
     * Test method for {@link Environment#getName()}.
     */
    @Test
    public void testGetName() {
        String name = Environment.getName();
        LOG.info("environment name: " + name);
        String cloader = Environment.getClassLoader().getClass().getName();
        assertTrue(name + " does not match " + cloader, cloader.startsWith(name));
        assertFalse(name + " has a '$' inside", name.indexOf('$') >= 0);
    }

    /**
     * Test method for {@link Environment#matchesOneOf(java.lang.String[])}.
     */
    @Test
    public void testMatchesOneOf() {
        String[] props = {"java.home"};
        assertTrue("'java.home' should exist", Environment.matchesOneOf(props));
    }

    /**
     * Test method for {@link Environment#matchesOneOf(java.lang.String[])}.
     */
    @Test
    public void testDoesNotMatches() {
        String[] props = {"nir.wana"};
        if (System.getProperty(props[0]) == null) {
            assertFalse("'nir.wana' should not exist", Environment
                    .matchesOneOf(props));
        }
    }

    /**
     * Test method for {@link Environment#isPropertyEnabled(String)}.
     */
    @Test
    public void testIsPropertyEnabled() {
        String testProp = "testStupidProperty";
        unsetSystemProperty(testProp);
        System.setProperty(testProp, "");
        assertTrue("empty property should be considered as 'true'",
                Environment.isPropertyEnabled(testProp));
        System.setProperty(testProp, "true");
        assertTrue(testProp + "=true", Environment.isPropertyEnabled(testProp));
        unsetSystemProperty(testProp);
    }

    private static void unsetSystemProperty(final String name) {
        Properties props = System.getProperties();
        props.remove(name);
        assertFalse(name + " is not set", Environment.isPropertyEnabled(name));
    }

    /**
     * Test method for {@link Environment#loadProperties(String)}.
     * @throws IOException if poperties can't be loaded
     */
    @Test
    public synchronized void testLoadProperties() throws IOException {
        Environment.loadProperties("test.properties");
        assertTrue("see test.properties", Environment
                .isPropertyEnabled("my.little.test.property"));
        unsetSystemProperty("my.little.test.property");
    }

    /**
     * Test method for {@link Environment#loadProperties(String)}.
     * @throws IOException if poperties can't be loaded
     */
    @Test
    public synchronized void testLoadPropertiesViaClassloader() throws IOException {
        Environment.loadProperties("/patterntesting/runtime/util/test.properties");
        assertTrue("see test.properties", Environment
                .isPropertyEnabled("my.little.test.property"));
        unsetSystemProperty("my.little.test.property");
    }

    /**
     * Test method for {@link Environment#isGoogleAppEngine()}.
     * We use the classloader to guess if we are in a Google App Engine
     * environment or not.
     */
    @Test
    public void testIsGoogleAppEngine() {
        String classLoaderName = this.getClass().getClassLoader().getClass().getName();
        LOG.info("classloader for testing is " + classLoaderName);
        boolean expected = classLoaderName.startsWith("com.google");
        assertEquals(expected, Environment.isGoogleAppEngine());
    }

    /**
     * Test method for {@link Environment#getLocalMavenRepositoryDir()}.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testGetLocalMavenRepositoryDir() throws IOException {
        File repoDir = Environment.getLocalMavenRepositoryDir();
        assertTrue("not a directory: " + repoDir, repoDir.isDirectory());
        LOG.debug("Local maven repository is at {}.", repoDir);
    }

    /**
     * Both things can't be true: the environment is a WebLogic server
     * <i>and</i> a Google AppEngine.
     */
    @Test
    public void testIsSpecialServer() {
    	boolean isWeblogic = Environment.isWeblogicServer();
    	boolean isAppEngine = Environment.isGoogleAppEngine();
    	if (isWeblogic || isAppEngine) {
    		assertEquals(!isWeblogic, Environment.isGoogleAppEngine());
    	}
    }
    
    /**
     * This test has only informative character.
     */
    @Test
    public void testAreThreadsAllowed() {
        boolean threadsAllowed = Environment.areThreadsAllowed();
        LOG.info("Threads are {}.",  threadsAllowed ? "allowed" : "not allowed");
    }

}

