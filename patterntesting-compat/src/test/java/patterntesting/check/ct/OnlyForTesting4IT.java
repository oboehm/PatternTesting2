/*
 * $Id: OnlyForTestingTest.java,v 1.4 2010/12/29 14:51:26 oboehm Exp $
 *
 * Copyright (c) 2008 by Oliver Boehm
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
 * (c)reated 19.02.2009 by oliver (ob@oasd.de)
 */
package patterntesting.check.ct;

import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import patterntesting.runtime.annotation.IntegrationTest;
import patterntesting.tool.aspectj.AjcXml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This is the integration test for OnlyForTesting4Aspect.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 19.02.2009
 */
@IntegrationTest("test calls AspectJ compiler")
public final class OnlyForTesting4IT {

    private static final Logger LOG = LoggerFactory.getLogger(OnlyForTesting4IT.class);

    /**
     * We want to tell XMLUnit to ignore white spaces.
     */
    @BeforeAll
    public static void setUpXMLUnit() {
        XMLUnit.setIgnoreWhitespace(true);
    }

    /**
     * We set up the AjcXml compiler by adding the source path and other
     * parameters for PatternTesting Check.CT tests.
     *
     * @return the setup AcjXml compiler
     */
    public static AjcXml createAjcXml() {
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
        LOG.info(xmlString);
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
            assertEquals(0, errors, diff.toString());
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

    /**
     * Here we use the AjcXml extension of patterntesting-tools which allows
     * us to call the AspectJ compiler (ajc) and get the compiler output as
     * XML result.
     *
     * @throws IOException if XML resource can't be read
     * @throws SAXException if XML can't be parsed
     */
    @Test
    public void testJUnit4Errors() throws IOException, SAXException {
        checkErrors("patterntesting/check/ct/junit4/OnlyForTesting4Test.java,"
                        + "patterntesting/check/ct/*OnlyForTesting*Aspect.aj",
                "junit4/OnlyForTesting4Test.xml");
    }

}
