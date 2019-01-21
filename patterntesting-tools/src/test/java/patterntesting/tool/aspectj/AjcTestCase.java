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

import org.apache.logging.log4j.LogManager;

import java.util.Enumeration;
import java.util.Map;

import static org.junit.Assert.fail;

/**
 * Helper test cases to write JUnit test case to assert result of Ajc
 * compilation.
 *
 * @author <a href="mailto:vmassol@apache.org">Vincent Massol</a>
 * @author <a href="mailto:matt@ojojo.com">Matt Smith</a>
 *
 * @version $Id: AjcTestCase.java,v 1.8 2016/12/30 19:07:44 oboehm Exp $
 */
public class AjcTestCase
{
	/** For normal logging. */
	private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(AjcTestCase.class);

   /**
    * The AjcXml compiler.
    */
   private final AjcXml compiler = new AjcXml();
    /**
     * Property to specify the project base directory (from where
     * all source paths are computed).
     */
    private static final String BASEDIR_PROPERTY = "basedir";

    /**
     * Property to specify the output dir where compiled classes will
     * be compiled.
     */
    private static final String OUTPUTDIR_PROPERTY = "patterntesting.outputdir";

    /**
     * If property BASEDIR_PROPERTY is not set the actual working directroy
     * is now used as default. (20-May-07, ob@aosd.de)
     *
     * @return the project base directory as set by the
     *          BASEDIR_PROPERTY system property
     */
    protected static String getBaseDir() {
		String basedir = System.getProperty(BASEDIR_PROPERTY, System.getProperty("user.dir"));
		if (basedir == null) {
            throw new IllegalStateException("The \"" + BASEDIR_PROPERTY + "\" property has not been set");
		}
		LOG.info("Using {}={} as base directory.", BASEDIR_PROPERTY, basedir);
		return basedir;
	}

    /**
     * If property OUTPUTDIR_PROPERTY is not set the temp directroy
     * is now used as default. (20-May-07, ob@aosd.de)
     *
	 * @return the output directory as set by the OUTPUTDIR_PROPERTY system
	 *         property
	 */
    protected static String getOutputDir() {
		String outputdir = System.getProperty(OUTPUTDIR_PROPERTY);
		if (outputdir == null) {
			outputdir = System.getProperty("java.io.tmpdir");
			if (outputdir == null) {
				throw new RuntimeException("The \"" + OUTPUTDIR_PROPERTY
						+ "\" property has not been set");
			} else {
				LOG.info("The \"" + OUTPUTDIR_PROPERTY
						+ "\" property has not been set - using " + outputdir
						+ " as default");
				System.setProperty(OUTPUTDIR_PROPERTY, outputdir);
			}
		}
		return outputdir;
	}

    /**
     * Gets the compiler.
     *
     * @return the compiler
     */
    protected final AjcXml getCompiler()
    {
       return this.compiler;
    }

    /**
     * Assert error equals.
     *
     * @param testFileName the test file name
     * @param lineNumber the line number
     * @param errorMessage the error message
     * @return true if error messages are equal
     */
    protected final boolean assertErrorEquals(final String testFileName,
            final int lineNumber, final String errorMessage) {
		AjcFileResult fileResult = getFileResult(testFileName);
		for (Enumeration<AjcResult> results = fileResult.getErrors(); results
				.hasMoreElements();) {
			AjcResult result = results.nextElement();
			if (result.getLine() == lineNumber
					&& result.getErrorMessage().equals(errorMessage)) {
				return true;
			}
		}
		fail("no match for `line " + lineNumber + ": " + errorMessage);
		return false;
	}

    /**
     * Assert warning equals.
     *
     * @param testFileName the test file name
     * @param lineNumber the line number
     * @param warnMessage the warn message
     * @return true if error messages are equal
     */
    protected final boolean assertWarningEquals(final String testFileName,
            final int lineNumber, final String warnMessage) {
		AjcFileResult fileResult = getFileResult(testFileName);
		for (Enumeration<AjcResult> results = fileResult.getWarnings(); results
				.hasMoreElements();) {
			AjcResult result = results.nextElement();
			if (result.getLine() == lineNumber
					&& result.getErrorMessage().equals(warnMessage)) {
				return true;
			}
		}
		fail("no match for `line " + lineNumber + ": " + warnMessage);
		return false;
	}

	/**
	 * @param testFileName
	 * @return
	 */
	private AjcFileResult getFileResult(final String testFileName) {
		Map<String, AjcFileResult> errors = this.compiler.getErrors();
		if (errors == null) {
			fail(this.compiler + " found no errors/warnings");
		}
		if (errors.get(testFileName) == null) {
			fail("no compiler errors/warnings found for " + testFileName);
		}
		AjcFileResult fileResult = errors.get(testFileName);
		return fileResult;
	}

}
