/*
 * $Id: XAjcCompileMojo.java,v 1.10 2017/05/14 20:10:08 oboehm Exp $
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
 * (c)reated 10.01.2011 by oliver (ob@oasd.de)
 */

package patterntesting.tool.maven;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.codehaus.mojo.aspectj.AjcCompileMojo;
import org.codehaus.mojo.aspectj.Module;
import patterntesting.runtime.NullConstants;

import java.util.Iterator;
import java.util.List;

/**
 * Weaves all main classes and adds the PatternTesting aspect libraries.
 * It extends the AspectJ Compiler Plugin.
 *
 * @author oliver
 * @since 1.1 (10.01.2011)
 */
@Mojo(name = "compile",
        defaultPhase = LifecyclePhase.COMPILE,
        requiresDependencyResolution = ResolutionScope.COMPILE,
        threadSafe = true)
public class XAjcCompileMojo extends AjcCompileMojo {

    /**
     * Sets the compliance level.
     *
     * @param level e.g. "1.5" for Java 5
     */
    @Override
    public void setComplianceLevel(final String level) {
        super.setComplianceLevel(level);
        this.complianceLevel = level;
    }

    /**
     * Gets the complianceLevel.
     *
     * @return the complianceLevel level, e.g. "1.5" (for Java 5)
     */
    public String getComplianceLevel() {
        if (StringUtils.isEmpty(this.complianceLevel)) {
            for (Object obj : this.getAjcOptions()) {
                String option = (String) obj;
                if (option.startsWith("-1.")) {
                    return option.substring(1);
                }
            }
        }
        return this.complianceLevel;
    }

    /**
     * Sets the target level.
     *
     * @param level e.g. "1.5" for Java 5
     */
    @Override
    public void setTarget(final String level) {
        super.setTarget(level);
        this.target = level;
    }

    /**
     * Gets the target level.
     *
     * @return the target level, e.g. "1.5" (for Java 5)
     */
    public String getTarget() {
        if (StringUtils.isEmpty(this.target)) {
            return this.getAjcOption("-target");
        }
        return this.target;
    }

    /**
     * Sets the source level.
     *
     * @param level e.g. "1.5" for Java 5
     */
    @Override
    public void setSource(final String level) {
        super.setSource(level);
        this.source = level;
    }

    /**
     * Gets the source level.
     *
     * @return the source level, e.g. "1.5" (for Java 5)
     */
    public String getSource() {
        if (StringUtils.isEmpty(this.source)) {
            return this.getAjcOption("-source");
        }
        return this.source;
    }

    /**
     * Gets the ajc compiler options.
     *
     * @return the ajc options
     */
    public List<?> getAjcOptions() {
        return this.ajcOptions;
    }

    /**
     * Looks in the ajcOptions for the given name.
     *
     * @param name the name e.g. "source"
     * @return the ajc option e.g. "1.5"
     */
    public String getAjcOption(final String name) {
        for (Iterator<?> iterator = this.ajcOptions.iterator(); iterator.hasNext();) {
            Object option = iterator.next();
            if (name.equals(option)) {
                return (String) iterator.next();
            }
        }
        return NullConstants.NULL_STRING;
    }

    /**
     * Gets the aspect path.
     *
     * @return the aspect path as file list
     */
    public Module[] getAspectLibraries() {
        return this.aspectLibraries;
    }

    private void initXlint() {
        if (StringUtils.isEmpty(this.getXlint())) {
            this.setXlint("ignore");
        }
    }

    /**
     * Sets the value for the Xlint option.
     *
     * @param value allowed: "ignore", "warning" or "error"
     * @see org.codehaus.mojo.aspectj.AbstractAjcCompiler#setXlint(java.lang.String)
     */
    public void setXlint(final String value) {
        if (!"ignore".equals(value) && !"warning".equals(value) && !"error".equals(value)) {
            throw new IllegalArgumentException('"' + value
                    + "\" is not one of \"ignore\", \"warning\" or \"error\"");
        }
        this.Xlint = value;
        super.setXlint(value);
    }

    /**
     * Gets the value of the Xlint flag.
     *
     * @return "ignore", "warning" or "error"
     */
    public String getXlint() {
        for (Iterator<?> iterator = this.ajcOptions.iterator(); iterator.hasNext();) {
            String option = (String) iterator.next();
            if (option.startsWith("-Xlint:")) {
                return option.substring(7);
            }
        }
        return NullConstants.NULL_STRING;
    }

    /**
     * Before we can call the execute method of the super class we must prepare
     * some parameters.
     *
     * @throws MojoExecutionException the mojo execution exception
     * @see org.codehaus.mojo.aspectj.AbstractAjcCompiler#execute()
     */
    @Override
    public void execute() throws MojoExecutionException {
        if (this.project == null) {
            throw new MojoExecutionException("no project");
        }
        initCompileLevels();
        initXlint();
        setUpAspectLibraries();
        super.execute();
    }

    private void initCompileLevels() {
        if (StringUtils.isEmpty(this.getComplianceLevel())) {
            this.setComplianceLevel("1.5");
            getLog().debug("complianceLevel set to 1.5");
        }
        if (StringUtils.isEmpty(this.getSource())) {
            this.setSource("1.5");
            getLog().debug("source set to 1.5");
        }
        if (StringUtils.isEmpty(this.getTarget())) {
            this.setTarget("1.5");
            getLog().debug("target set to 1.5");
        }
    }

    /**
     * Add the PatternTesting libs as aspect library.
     */
    private void setUpAspectLibraries() {
        if (this.aspectLibraries == null) {
            this.aspectLibraries = new Module[0];
        }
        addAspectLibrary("patterntesting-rt");
        addAspectLibrary("patterntesting-check-ct");
        addAspectLibrary("patterntesting-check-rt");
        addAspectLibrary("patterntesting-concurrent");
        addAspectLibrary("patterntesting-exception");
    }

    private void addAspectLibrary(final String artifactId) {
        if (!containsAspectLibrary(artifactId)) {
            Module lib = new Module();
            lib.setGroupId("org.patterntesting");
            lib.setArtifactId(artifactId);
            this.aspectLibraries = ArrayUtils.add(this.aspectLibraries, lib);
            this.getLog().debug(lib + " added as aspect library");
        }
    }

    private boolean containsAspectLibrary(final String artifactId) {
        for (int i = 0; i < this.aspectLibraries.length; i++) {
            if (artifactId.equals(this.aspectLibraries[i].getArtifactId())) {
                return true;
            }
        }
        return false;
    }

}

