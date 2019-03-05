/**
 * Copyright (c) 2008-2019 by Oliver Boehm
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
 * (c)reated 02.12.2008 by oliver (ob@oasd.de)
 */
package patterntesting.check.ct;

import java.io.IOException;
import java.sql.Date;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import patterntesting.annotation.check.ct.*;
import patterntesting.runtime.annotation.IntegrationTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This is the JUnit testcase for (Abstract)DatabaseAspect.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.9 $
 * @since 02.12.2008
 */
@DamnJDBC
public class DatabaseTest extends AbstractAcjXmlTest {

    /**
     * If you remove "@AllowJDBC" from this method you should see a lot of
     * errors from the (Abstract)DatabaseAspect because the Date class from
     * java.sql is used here.
     */
    @AllowJDBC
    @Test
    public final void testSqlDate() {
        long currentTimeMillis = System.currentTimeMillis();
        Date now = new Date(currentTimeMillis);
        Date later = new Date(currentTimeMillis + 100);
        assertTrue(now.before(later), now + " is not before " + later);
    }

    /**
     * Here we use the AjcXml extension of patterntesting-tools which allows
     * us to call the AspectJ compiler (ajc) and get the compiler output as
     * XML result.
     *
     * @throws IOException if XML resource can't be read
     * @throws SAXException if XML can't be parsed
     * @since 1.0
     */
    @IntegrationTest("test calls AspectJ compiler")
    @Test
    public final void testErrors() throws IOException, SAXException {
        checkErrors("patterntesting/check/ct/DatabaseSample.java,"
                + "patterntesting/check/ct/*DatabaseAspect.aj", "DatabaseTest.xml");
    }

}
