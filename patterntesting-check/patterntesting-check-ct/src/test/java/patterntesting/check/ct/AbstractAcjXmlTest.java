/*
 * $Id: AbstractAcjXmlTest.java,v 1.11 2016/12/18 21:58:55 oboehm Exp $
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
 * (c)reated 08.03.2010 by oliver (ob@oasd.de)
 */

package patterntesting.check.ct;

import static org.junit.Assert.assertEquals;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.custommonkey.xmlunit.*;
import org.junit.BeforeClass;
import org.xml.sax.SAXException;

import patterntesting.tool.aspectj.AjcXml;

/**
 * This abstract class sets up the AjcXml compiler for testing inside
 * PatternTesting Check.CT.
 *
 * @author oliver
 * @since 1.0 (08.03.2010)
 */
public abstract class AbstractAcjXmlTest {

    private static final Logger LOG = LogManager.getLogger(AbstractAcjXmlTest.class);

    /**
     * We want to tell XMLUnit to ignore white spaces.
     */
    @BeforeClass
    public static void setUpXMLUnit() {
        XMLUnit.setIgnoreWhitespace(true);
    }

    /**
     * We set up the AjcXml compiler by adding the source path and other
     * parameters for PatternTesting Check.CT tests.
     *
     * @return the setup AcjXml compiler
     */
    public static final AjcXml createAjcXml() {
        AjcXml compiler = new AjcXml();
        compiler.addSrcdir("src/test/sample");
        compiler.addSrcdir("src/main/aspect");
        compiler.setDestdir("target/test-classes");
        compiler.setFailonerror(false);
        return compiler;
    }

    /**
     * Here we use the AjcXml extension of patterntesting-tools which allows
     * us to call the AspectJ compiler (ajc) and get the compiler output as
     * XML result.
     *
     * @param includes pattern of the included sources
     * @param xmlReference the name of an XML file used as reference
     * @throws IOException if XML resource can't be read
     * @throws SAXException if XML can't be parsed
     */
    protected final void checkErrors(final String includes, final String xmlReference)
            throws IOException, SAXException {
        AjcXml compiler = createAjcXml();
        compiler.setIncludes(includes);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        compiler.setOutputStream(baos);
        compiler.execute();
        String xmlString = baos.toString();
        LOG.debug(xmlString);
        baos.close();
        checkXml(xmlReference, xmlString);
    }

    /**
     * We know that there is a difference with the filename attribute in
     * "DatabaseTest.xml". So we ignore this node in
     * <code>countErrors(..)</code>.
     *
     * @param xmlResource name of the resource, e.g. "DatabaseTest.xml"
     * @param xmlString this string is compared with the resource content
     * @throws IOException if resource can't be read
     * @throws SAXException if there is a problem with the XML
     */
    @SuppressWarnings("unchecked")
    public final void checkXml(final String xmlResource, final String xmlString)
            throws IOException, SAXException {
        try (InputStream istream = this.getClass().getResourceAsStream(xmlResource)) {
	        String expected = IOUtils.toString(istream, StandardCharsets.UTF_8);
	        Diff diff = new Diff(expected, xmlString);
	        DetailedDiff details = new DetailedDiff(diff);
	        List<Difference> allDiffs = details.getAllDifferences();
	        int errors = countErrors(allDiffs);
	        assertEquals(diff.toString(), 0, errors);
        }
    }

    private static int countErrors(final List<Difference> allDiffs) {
        int errors = 0;
        for (Difference d : allDiffs) {
            String xpath = d.getControlNodeDetail().getXpathLocation();
            if (xpath.endsWith("/file[1]/@name")) {
                LOG.debug("ignored: " + xpath);
            } else {
                LOG.info("difference: " + d);
                errors++;
            }
        }
        return errors;
    }

}

