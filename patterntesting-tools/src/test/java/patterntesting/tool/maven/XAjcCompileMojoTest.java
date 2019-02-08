/*
 * $Id: XAjcCompileMojoTest.java,v 1.12 2016/12/30 19:07:44 oboehm Exp $
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
 * (c)reated 14.01.2011 by oliver (ob@oasd.de)
 */

package patterntesting.tool.maven;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.OptionConverter;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.handler.DefaultArtifactHandler;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.model.Model;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.project.MavenProject;
import org.codehaus.mojo.aspectj.Module;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;


/**
 * The Class XAjcCompileMojoTest.
 *
 * @author oliver
 * @since 1.1 (14.01.2011)
 */
public class XAjcCompileMojoTest extends AbstractMojoTestCase {

    private static final Logger LOG = LogManager.getLogger(XAjcCompileMojoTest.class);

    /**
     * Sets up the environment for the Mojo.
     *
     * @throws Exception the exception
     * @see org.apache.maven.plugin.testing.AbstractMojoTestCase#setUp()
     */
    protected void setUp() throws Exception {
        // required for mojo lookups to work
        super.setUp();
        LOG.debug(this + " prepared for testing");
    }
    
    /**
     * Tests the proper discovery and configuration of the mojo.
     *
     * @throws Exception the exception
     */
    public void testCompilerTestEnvironment() throws Exception {
        XAjcCompileMojo mojo = getXAjcCompileMojo("src/it/intro/pom.xml");
        setVariableValueToObject(mojo, "source", "");
        setVariableValueToObject(mojo, "target", "");
        mojo.execute();
        checkCompileLevels(mojo);
    }

    private void checkCompileLevels(final XAjcCompileMojo mojo) {
        assertEquals("1.5", mojo.getComplianceLevel());
        assertEquals("1.5", mojo.getSource());
        assertEquals("1.5", mojo.getTarget());
        assertEquals("1.5", mojo.getAjcOption("-source"));
        assertEquals("1.5", mojo.getAjcOption("-target"));
    }

    /**
     * The aspect path should contain the PatternTesting libraries.
     *
     * @throws Exception the exception
     */
    public void testAspectPath() throws Exception {
        XAjcCompileMojo mojo = getXAjcCompileMojo("src/it/hello/pom.xml");
        mojo.execute();
        Module[] libs = mojo.getAspectLibraries();
        assertEquals(5, libs.length);
        for (int i = 0; i < libs.length; i++) {
            LOG.info("lib " + (i+1) + ": " + libs[i]);
            assertEquals("org.patterntesting", libs[i].getGroupId());
        }
    }

    /**
     * The Xlint option should be "ignore" to avoid warnings like
     * <tt>[WARNING] advice defined in patterntesting.concurrent.RunParallelAspect
     * has not been applied [Xlint:adviceDidNotMatch]</tt>.
     *
     * @throws Exception the exception
     */
    public void testXlint() throws Exception {
        XAjcCompileMojo mojo = new XAjcCompileMojo();
        setUpProjectFor(mojo, new File("src/it/hello/pom.xml"));
        mojo.execute();
        String xlint = mojo.getXlint();
        assertEquals("ignore", xlint);
    }

    private XAjcCompileMojo getXAjcCompileMojo(final String pom) throws Exception {
        File testPom = new File(pom);
        XAjcCompileMojo mojo = (XAjcCompileMojo) lookupMojo("compile", testPom);
        assertNotNull(mojo);
        configureMojo(mojo, "patterntesting-tools", testPom);
        setUpProjectFor(mojo, testPom);
        return mojo;
    }

    private void setUpProjectFor(final Mojo mojo, final File pomFile) throws IllegalAccessException {
        assert mojo != null : "mojo must be created before!";
        MavenProject project = new MavenProject(new Model());
        Artifact artifact = createArtifact();
        project.addAttachedArtifact(artifact);
        project.setArtifact(artifact);
        project.setGroupId("org.patterntesting");
        project.setArtifactId("patterntesting-tools");
        project.setFile(pomFile);
        setVariableValueToObject(mojo, "project", project);
        setVariableValueToObject(mojo, "basedir", new File(getBasedir()));
    }

    private Artifact createArtifact() {
        Artifact artifact = new DefaultArtifact("org.patterntesting", "patterntesting-tools",
                VersionRange.createFromVersion("2.0.0-SNAPSHOT"), "test", "type", "classifier",
                null);
        ArtifactHandler handler = new DefaultArtifactHandler();
        //setVariableValueToObject(handler, "language", "java");
        artifact.setArtifactHandler(handler);
        return artifact;
    }

    /**
     * This method checks the result of a 'mvn archetype:generate' command.
     * It does the same as the following command:
     * <tt>mvn archetype:generate
     * -DarchetypeGroupId=org.patterntesting
     * -DarchetypeArtifactId=patterntesting-tools
     * -DarchetypeVersion=2.0.0-SNAPSHOT</tt>
     * (see also src/main/resources/META-INF/maven/archetype-metadata.xml
     * for the default values).
     *
     * @throws Exception Signals that an I/O exception has occurred.
     */
    public void testGeneratedArchetype() throws Exception {
        File outputDir = new File(getBasedir(), "target/it");
        File projectDir = new File(outputDir, "hello-world");
        generateArchetype(projectDir);
        File testPom = new File(projectDir, "pom.xml");
        XAjcCompileMojo mojo = getXAjcCompileMojo(testPom.getPath());
        mojo.execute();
    }

    /**
     * Does the same as the 'mvn archetype:generate' command but more or less
     * without the help of Plexus/Maven. Why? I don't know how do use the
     * Maven API for such a task.
     *
     * @param destDir the project dir
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void generateArchetype(final File destDir) throws IOException {
        LOG.info("faking '"
                + "mvn archetype:generate -DarchetypeGroupId=org.patterntesting -DarchetypeArtifactId=patterntesting-tools -DarchetypeVersion=2.0.0-SNAPSHOT"
                + "'...");
        Properties variables = new Properties();
        variables.put("package", "patterntesting.samples");
        variables.put("groupId", "org.patterntesting.samples");
        variables.put("artifactId", "hello-world");
        variables.put("archetypeVersion", "2.0.0-SNAPSHOT");
        copyResources(new File("src/main/resources/archetype-resources"), destDir, variables);
    }

    private static void copyResources(final File resourceDir, final File destDir,
            final Properties variables) throws IOException {
        if (destDir.mkdirs()) {
            LOG.debug("created: " + destDir);
        }
        File[] files = resourceDir.listFiles();
        if (files == null) {
            throw new IllegalArgumentException("not a directory: " + resourceDir);
        }
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                copyResource(files[i], destDir, variables);
            } else if (files[i].isDirectory() && !files[i].getName().equals("CVS")) {
                copyResources(files[i], new File(destDir, files[i].getName()), variables);
            } else {
                LOG.info("ignored: " + files[i]);
            }
        }
    }

    private static void copyResource(final File resourceFile, final File destDir,
            final Properties variables) throws IOException {
        File destFile = destDir;
        if (destFile.isDirectory()) {
            destFile = new File(destDir, resourceFile.getName());
        }
        String content = FileUtils.readFileToString(resourceFile, StandardCharsets.UTF_8);
        String filtered = OptionConverter.substVars(content, variables);
        FileUtils.writeStringToFile(destFile, filtered, StandardCharsets.UTF_8);
        LOG.debug("copied: " + resourceFile + " -> " + destFile);
    }

}

