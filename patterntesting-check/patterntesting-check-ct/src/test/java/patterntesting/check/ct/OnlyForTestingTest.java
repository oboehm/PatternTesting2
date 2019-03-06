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

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import patterntesting.runtime.annotation.IntegrationTest;

/**
 * This is the test class for (Abstract)OnlyForTestingAspect.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 19.02.2009
 */
@IntegrationTest("test calls AspectJ compiler")
public final class OnlyForTestingTest extends AbstractAcjXmlTest {

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
                        + "patterntesting/check/ct/*OnlyForTestingAspect.aj",
                "junit4/OnlyForTesting4Test.xml");
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
    public void testJUnit5Errors() throws IOException, SAXException {
        checkErrors("patterntesting/check/ct/junit5/OnlyForTesting5Test.java,"
                        + "patterntesting/check/ct/*OnlyForTestingAspect.aj",
                "junit5/OnlyForTesting5Test.xml");
    }

}
