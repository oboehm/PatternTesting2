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

import java.util.Enumeration;

/**
 * Formats a list of {@link AjcFileResult} objects into an XML string.
 *
 * @author <a href="mailto:vmassol@apache.org">Vincent Massol</a>
 *
 * @version $Id: XMLFormatter.java,v 1.6 2011/01/08 13:06:23 oboehm Exp $
 */
public final class XMLFormatter implements ResultFormatter {

    /**
     * Root element for all errors.
     */
    private static final String ROOT = "patterntesting";

    /**
     * Patterntesting report.
     */
    private static final String PATTERNTESTING_REPORT = "patterntesting-report";

    /**
     * Patterntesting Runtime.
     */
    private static final String PATTERNTESTING_RUNTIME = "patterntesting-runtime";

    /**
     * All errors in a given file.
     */
    private static final String FILE = "file";

    /**
     * File attribute.
     */
    private static final String ATTR_NAME = "name";

    /**
     * A single warning in a given file.
     */
    private static final String WARNING = "warning";

    /**
     * A single error in a given file.
     */
    private static final String ERROR = "error";

    /**
     * Line attribute (where the error ocurred).
     */
    private static final String ATTR_LINE = "line";

    /**
	 * Format.
	 *
	 * @param ajcFileResults the ajc file results
	 * @return the string
	 * @see patterntesting.tool.aspectj.ResultFormatter#format(java.util.Enumeration)
	 */
	public String format(final Enumeration<AjcFileResult> ajcFileResults) {
		StringBuffer xml = new StringBuffer();

		generateHeader(xml);
		generatePatternTesting(xml, ajcFileResults);
		generateFooter(xml);

		return xml.toString();
	}

    /**
     * Format.
     *
     * @param ajcFileResults results to be formatted
     * @param runtimeResults results to be formatted
     * @return formatted result
     */
    public String format(final Enumeration<AjcFileResult> ajcFileResults,
			final Enumeration<AjcFileResult> runtimeResults) {
		StringBuffer xml = new StringBuffer();

		generateHeader(xml);
		generatePatternTesting(xml, ajcFileResults);
		generatePatternTestingRuntime(xml, runtimeResults);
		generateFooter(xml);

		return xml.toString();
	}

	private static void generateHeader(final StringBuffer buffer) {
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
		buffer.append("<" + ROOT + ">\n");
	}

	private static void generatePatternTesting(final StringBuffer buffer,
			final Enumeration<AjcFileResult> ajcFileResults) {
		buffer.append("\t<" + PATTERNTESTING_REPORT + ">\n");
		while (ajcFileResults.hasMoreElements()) {
			AjcFileResult fileResult = ajcFileResults.nextElement();
			generateFile(buffer, fileResult);
		}
		buffer.append("\t</" + PATTERNTESTING_REPORT + ">\n");
	}

	private static void generatePatternTestingRuntime(final StringBuffer buffer,
			final Enumeration<AjcFileResult> ajcFileResults) {
		buffer.append("\t<" + PATTERNTESTING_RUNTIME + ">\n");
		while (ajcFileResults.hasMoreElements()) {
			AjcFileResult fileResult = ajcFileResults
					.nextElement();
			generateFile(buffer, fileResult);
		}
		buffer.append("\t</" + PATTERNTESTING_RUNTIME + ">\n");
	}

	private static void generateFile(final StringBuffer buffer,
			final AjcFileResult fileResult) {
		buffer.append("\t\t<" + FILE + " " + ATTR_NAME + "=\""
				+ fileResult.getFileName() + "\">\n");
		Enumeration<AjcResult> results = fileResult.getErrors();
		while (results.hasMoreElements()) {
			AjcResult result = results.nextElement();
			generateError(buffer, result);
		}
		Enumeration<AjcResult> warnings = fileResult.getWarnings();
		while (warnings.hasMoreElements()) {
			AjcResult result = warnings.nextElement();
			generateWarning(buffer, result);
		}
		buffer.append("\t\t</" + FILE + ">\n");
	}

    private static void generateWarning(final StringBuffer buffer, final AjcResult result) {
		buffer.append("\t\t\t<" + WARNING + " " + ATTR_LINE + "=\""
				+ result.getLine() + "\">");
		buffer.append(xmlEncode(result.getErrorMessage()));
		buffer.append("</" + WARNING + ">\n");
	}

	private static void generateError(final StringBuffer buffer, final AjcResult result) {
		buffer.append("\t\t\t<" + ERROR + " " + ATTR_LINE + "=\"" + result.getLine()
				+ "\">");
		buffer.append(xmlEncode(result.getErrorMessage()));
		buffer.append("</" + ERROR + ">\n");
	}

	private static void generateFooter(final StringBuffer buffer) {
		buffer.append("</" + ROOT + ">\n");
	}

	/**
	 * Escapes reserved XML characters.
	 *
	 * @param theString
	 *            the string to escape
	 * @return the escaped string
	 */
	private static String xmlEncode(final String theString) {
		String newString;

		// It is important to replace the "&" first as the other replacements
		// also introduces "&" chars ...
		newString = replace(theString, '&', "&amp;");

		newString = replace(newString, '<', "&lt;");
		newString = replace(newString, '>', "&gt;");
		newString = replace(newString, '\"', "&quot;");

		return newString;
	}

	/**
	 * Replaces a character in a string by a substring.
	 *
	 * @param theBaseString
	 *            the base string in which to perform replacements
	 * @param theChar
	 *            the char to look for
	 * @param theNewString
	 *            the string with which to replace the char
	 * @return the string with replacements done or null if the input string was
	 *         null
	 */
	public static String replace(final String theBaseString, final char theChar,
			final String theNewString) {
		if (theBaseString == null) {
			return null;
		}
		String baseString = theBaseString;

		final int len = baseString.length() - 1;
		int pos = -1;

		while ((pos = baseString.indexOf(theChar, pos + 1)) > -1) {
			if (pos == 0) {
				final String after = baseString.substring(1);

				baseString = theNewString + after;
			} else if (pos == len) {
				final String before = baseString.substring(0, pos);

				baseString = before + theNewString;
			} else {
				final String before = baseString.substring(0, pos);
				final String after = baseString.substring(pos + 1);

				baseString = before + theNewString + after;
			}
		}

		return baseString;
	}

}
