/*
 * $Id: XMLFormatterTest.java,v 1.6 2016/12/30 19:07:44 oboehm Exp $
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
 * (c)reated 04.03.2010 by oliver (ob@oasd.de)
 */

package patterntesting.tool.aspectj;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.junit.*;

import patterntesting.runtime.junit.FileTester;

/**
 * The Class XMLFormatterTest.
 *
 * @author oliver
 * @since 1.0 (04.03.2010)
 */
public final class XMLFormatterTest {

    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(XMLFormatterTest.class);
    private static Vector<AjcFileResult> ajcFileResults = new Vector<AjcFileResult>();
    private final XMLFormatter xmlFormatter = new XMLFormatter();

    /**
     * We put one result element in the list for testing.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        AjcFileResult result = new AjcFileResult("test-report");
        ajcFileResults.add(result);
    }

    /**
     * Test method for {@link XMLFormatter#format(java.util.Enumeration)}.
     * We want the XML file formatted. So look at the test-report.xml how it
     * should look like.
     *
     * @throws IOException if test resource can't be read
     */
    @Test
    public void testFormatEnumerationOfAjcFileResult() throws IOException {
        String formatted = xmlFormatter.format(ajcFileResults.elements());
        File testReport = new File("target", "test-report.xml");
		FileUtils.write(testReport, formatted, StandardCharsets.UTF_8);
        LOG.debug(formatted);
		FileTester.assertContentEquals(new File("src/test/resources/patterntesting/tool/aspectj/test-report.xml"),
				testReport);
    }

}

