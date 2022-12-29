/*
 *========================================================================
 *
 * Copyright 2001-2004 Vincent Massol & Matt Smith.
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
 *========================================================================
 */
package patterntesting.tool.aspectj;

import clazzfish.monitor.ResourcepathMonitor;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.apache.tools.ant.BuildException;
import org.aspectj.bridge.context.CompilationAndWeavingContext;
import org.aspectj.tools.ant.taskdefs.AjcTask;
import patterntesting.annotation.check.runtime.MayReturnNull;
import patterntesting.runtime.util.Environment;

import java.io.*;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;

/**
 * Ant task that is a wrapper around the
 * {@link org.aspectj.tools.ant.taskdefs.compilers.Ajc} task. It configures it
 * to use the {@link AjcErrorHandler} error handler in order to provide
 * structured results from Ajc.
 *
 * @author <a href="mailto:vmassol@apache.org">Vincent Massol</a>
 * @author <a href="mailto:matt@ojojo.com">Matt Smith</a>
 *
 * @version $Id: AjcXmlTask.java,v 1.24 2016/12/30 19:07:44 oboehm Exp $
 */
public class AjcXmlTask extends AjcTask {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(AjcXmlTask.class);

   /**
	 * Lists of errors received from the Ajc compiler. Array of
	 * {@link AjcFileResult} objects.
	 */
	private Map<String, AjcFileResult> errors = new Hashtable<String, AjcFileResult>();

	/** The writer where to write the result to. */
	private Writer resultWriter;

	/**
	 * Formatter.
	 */
	private ResultFormatter formatter = new XMLFormatter();

	/**
	 * As a precondition for a successful compilation the aspectjrt.jar must
	 * be in the java.class.path - otherwise the AjcTask doesn't seem to work
	 * (got by experimental programming, also known als trial &amp; error).
	 *
	 * @author oliver (ob@aosd.de)
	 * @since 24-May-2007
	 */
	public AjcXmlTask() {
	    super();
		checkClasspath();
		this.setDefaults();
	}

	private static void checkClasspath() {
	    if (!hasAspectjPath()) {
	        String classpath = System.getProperty("java.class.path");
			LOG.debug("aspectjrt.jar not found in {}.", classpath);
			File aspectjrt = findAspectjPath();
			String s = File.pathSeparatorChar + aspectjrt.toString();
			System.setProperty("java.class.path", classpath + s);
			LOG.info("{} added to java.class.path", s);
		}
	}

	/**
	 * Here we look of aspectjrt.jar or aspectjrt-x.x.x.jar is in the
	 * classpath. It is package private for testing.
	 *
	 * @return true if aspectjrt.jar was found in classpath
     * @since 1.0
	 */
	static boolean hasAspectjPath() {
	    ResourcepathMonitor cpmon = ResourcepathMonitor.getInstance();
	    if (cpmon.whichResource("/org/aspectj/lang/JoinPoint.class") != null) {
	        return true;
	    }
        String classpath = System.getProperty("java.class.path");
        return classpath.matches(".*/aspectjrt[\\-\\.\\w]*.jar.*");
	}

	/**
     * Here we overwrite some defaults of the super class.
     * <ul>
     * <li>source: we support 1.6 so this is the default now</li>
     * </ul>
     * <p>
     * NOTE: Also we set "CompilationAndWeavingContext.setMultiThreaded(true);"
     * this has no effect because the AjcTask will set it to false again. At the
     * moment it is only a reminder to wait till
     * <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=319315">bug
     * 319315</a> will be solved.
     * </p>
     *
     * @since 1.0
     */
	private void setDefaults() {
	    super.setSource("1.6");
        CompilationAndWeavingContext.setMultiThreaded(true);
	}

	/**
	 * It use the environment variable ASPECTJ_HOME to determine the path of
	 * aspectjrt.jar. If ASPECTJ_HOME is not set it uses
	 * "/usr/java/aspectj/lib/aspectjrt.jar" as default which is valid on the
	 * build machine (bbb.i). But only if this jar file exists.
	 * If not we take a look at the local maven repository. If we find a
	 * aspectjrt.jar there we will use that.
	 * <p>
	 * This method is package private for testing.
     * </p>
	 *
	 * @return path of aspectjrt.jar, e.g. /usr/java/aspectj/lib/aspectjrt.jar
	 */
	static File findAspectjPath() {
		String aspectjHome = System.getenv("ASPECTJ_HOME");
		if (StringUtils.isEmpty(aspectjHome)) {
			aspectjHome = "/usr/java/aspectj";
		}
		LOG.debug("using ASPECTJ_HOME=" + aspectjHome);
		File aspectjrt = new File(aspectjHome, "lib/aspectjrt.jar");
        if (!aspectjrt.exists()) {
            aspectjrt = getAspectjPathFromRepository();
        }
        if (aspectjrt == null) {
            throw new RuntimeException("no aspectjrt.jar found in "
                    + aspectjHome + "/lib or .m2/repository");
        }
		if (!aspectjrt.exists()) {
            throw new RuntimeException("no aspectjrt.jar in classpath, "
                    + aspectjrt + " not found");
		}
		LOG.info("aspectjrt.jar found in '{}'.", aspectjrt);
		return aspectjrt;
	}

	/**
	 * Here we will look if we find a local Maven repository at
	 * {@code $user.home/.m2/repository} with a aspectjrt-x.x.x.jar
	 * inside.
	 *
	 * @since 1.0
	 * @return null if nothing was found
	 */
	@MayReturnNull
	private static File getAspectjPathFromRepository() {
	    try {
            File mavenRepoDir = Environment.getLocalMavenRepositoryDir();
            File repo = new File(mavenRepoDir, "org/aspectj/aspectjrt");
            String[] subdirs = repo.list();
            if (subdirs == null) {
                LOG.trace("Will return null because {} is not a directory.", repo);
                return null;
            }
            Arrays.sort(subdirs);
            for (int i = subdirs.length-1; i >= 0; i--) {
                File aspectjPath = getAspectjPathFrom(new File(repo, subdirs[i]));
                if (aspectjPath != null) {
                    return aspectjPath;
                }
            }
        } catch (IOException ioe) {
            LOG.warn("Local maven repository not found.", ioe);
        }
	    return null;
	}

	/**
	 * Looks in the given directory of a file "aspectjrt-x.x.x.jar" can be
	 * found.
	 *
	 * @param dir
	 * @return the jar file or null
	 */
	@MayReturnNull
	private static File getAspectjPathFrom(final File dir) {
	    String[] jars = dir.list(new WildcardFileFilter("aspectjrt*.jar"));
	    if (jars == null) {
	        LOG.trace("Will return null because {} is not a directory.", dir);
	        return null;
	    }
	    for (int i = 0; i < jars.length; i++) {
	        File jarFile = new File(dir, jars[i]);
	        if (jarFile.isFile()) {
	            return jarFile;
	        }
	    }
	    return null;
	}

	/**
	 * Gets the errors.
	 *
	 * @return map with the errors
	 */
    public final Map<String, AjcFileResult> getErrors() {
        return this.errors;
    }

    /**
     * Sets the XML result file.
     *
     * @param resultFile the new result file
     */
    public final void setResultFile(final File resultFile) {
        try {
            setOutputStream(new FileOutputStream(resultFile));
        } catch (IOException e) {
            throw new BuildException("Failed to create output file [" + resultFile + "]", e);
        }
    }

    /**
     * Sets the output stream. Useful if you wish to call this Ant task
     * from a java application and not from an Ant script.
     *
     * @param outputStream the new output stream
     */
    public final void setOutputStream(final OutputStream outputStream) {
        this.setResultWriter(new OutputStreamWriter(outputStream));
    }

    /**
     * Sets the result writer. Useful if you wish to call this Ant task
     * from a java application and not from an Ant script.
     *
     * @param writer where to write the XML result
     * @since 1.0
     */
    public final synchronized void setResultWriter(final Writer writer) {
        this.resultWriter = writer;
    }

    /**
     * Sets the formatter to "plain" or "xml".
     *
     * @param type e.g. "plain"
     */
    public final synchronized void setFormatter(final String type) {
        if ("plain".equals(type)) {
            this.formatter = new PlainFormatter();
        } else {
            this.formatter = new XMLFormatter();
        }
    }

    /**
     * Execute.
     *
     * @see org.aspectj.tools.ant.taskdefs.compilers.Ajc#execute()
     */
	@Override
    public final synchronized void execute() {
        if (this.resultWriter == null) {
            LOG.info("no result file given - using internal buffer");
            this.resultWriter = new StringWriter();
        }
		try {
			AjcErrorHandler handler = new AjcErrorHandler();
			this.setMessageHolder(handler);
	        CompilationAndWeavingContext.setMultiThreaded(true);
			super.execute();
			LOG.debug("compiler executed - " + handler);
			this.resultWriter.write(formatter.format(handler.getResults()));
			this.resultWriter.flush();
			this.errors = handler.getErrorResults();
		} catch (IOException e) {
			throw new BuildException("Could not execute compiler!", e);
		}
	}

	/**
	 * This method can be called after {@link #execute()}. It should return you
	 * the result of the last compilation.
	 *
	 * @return the result of the last compilation
	 * @since 1.0
	 */
	public final synchronized String getResult() {
	    if (this.resultWriter == null) {
	        throw new BuildException("Must specify result file or writer");
	    }
	    try {
            this.resultWriter.flush();
        } catch (IOException ioe) {
            LOG.warn("flush problem ignored", ioe);
        }
	    return this.resultWriter.toString();
	}

}
