/*
 * $Id: AjcXmlTaskTest.java,v 1.6 2016/12/30 19:07:44 oboehm Exp $
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
 * (c)reated 26.03.2010 by oliver (ob@oasd.de)
 */

package patterntesting.tool.aspectj;

import static org.junit.Assert.*;

import java.io.*;

import org.apache.logging.log4j.LogManager;
import org.aspectj.tools.ant.taskdefs.AjcTask;
import org.junit.Test;

/**
 * This is the JUnit test for the AjcXml class.
 *
 * @author oliver
 * @since 1.0 (26.03.2010)
 */
public final class AjcXmlTaskTest {

    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(AjcXmlTaskTest.class);
    private final AjcXmlTask compiler = new AjcXmlTask();

    /**
     * Test method for {@link patterntesting.tool.aspectj.AjcXmlTask#execute()}.
     * @throws IOException impossible
     */
    @Test
    public void testEmptyExecute() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        compiler.setOutputStream(baos);
        compiler.execute();
        String xmlString = baos.toString();
        baos.close();
        checkXML(xmlString);
    }

    /**
     * We call the compiler task but did not set the OutputStream or Writer
     * here. The AjcXmlTask should do that for us.
     */
    @Test
    public void testGetResult() {
        compiler.execute();
        String xmlString = compiler.getResult();
        checkXML(xmlString);
    }

    private static void checkXML(final String xmlString) {
        LOG.debug(xmlString);
        assertTrue(xmlString, xmlString.trim().startsWith("<"));
    }

    /**
     * Test method for hasAspectjPath(). To test this method we manipulate the
     * classpath a little bit.
     *
     * @since 1.0
     */
    @Test
    public void testHasAspectjPath() {
        String classpath = System.getProperty("java.class.path");
        LOG.info("classpath=" + classpath);
        try {
            System.setProperty("java.class.path", "/tmp/aspectjrt.jar");
            assertTrue("aspectjrt.jar not recognized", AjcXmlTask
                    .hasAspectjPath());
            System.setProperty("java.class.path", "/tmp/aspectjrt-1.6.8.jar");
            assertTrue("aspectjrt-1.6.8.jar not recognized", AjcXmlTask
                    .hasAspectjPath());
        } finally {
            System.setProperty("java.class.path", classpath);
        }
    }

    /**
     * We should find the AspectJ runtime lib in the local maven repository.
     * Otherwise getApectjPathFromRepository() does not work correctly.
     *
     * @since 1.0
     */
    @Test
    public void testFindAspectjPath() {
        String classpath = System.getProperty("java.class.path");
        try {
            System.setProperty("java.class.path", ".");
            File jarFile = AjcXmlTask.findAspectjPath();
            LOG.info("aspectjrt.jar found: " + jarFile);
            assertTrue(jarFile + " not found", jarFile.exists());
        } finally {
            System.setProperty("java.class.path", classpath);
        }

    }

    /**
     * The AjcTask offers findAspectjtoolsJar(). This is tested here.
     * @since 1.0
     */
    @Test
    public void testFindAspectjtoolsJar() {
        File aspectjtoolsJar = AjcTask.findAspectjtoolsJar();
        LOG.info("aspectjtoolsJar=" + aspectjtoolsJar);
        assertNotNull(aspectjtoolsJar);
        assertTrue(aspectjtoolsJar + " not found", aspectjtoolsJar.exists());
    }

}

