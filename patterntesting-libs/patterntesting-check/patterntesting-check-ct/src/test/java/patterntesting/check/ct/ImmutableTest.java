/*
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
 * (c)reated 29.09.2008 by oliver (ob@oasd.de)
 */
package patterntesting.check.ct;

import java.io.IOException;

import javax.annotation.concurrent.Immutable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import patterntesting.runtime.annotation.IntegrationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * With this test class you can check if the ImmutableAspect is working
 * correct.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.11 $
 * @since 29.09.2008
 */
@Immutable
public final class ImmutableTest extends AbstractAcjXmlTest {

    private static final Logger LOG = LogManager.getLogger(ImmutableTest.class);

    /** The x. */
    //private int w = 1;    // should give a warning
	protected int x;        // SUPPRESS CHECKSTYLE
	private transient int y = 1;
	private final int z = 2;

	/**
	 * This test changes the internal attribute x if you remove the comment.
	 */
	@Test
	public void testMutable() {
		//x = 2;	// should give a warning
		y = z;		// this is allowed because y is transient
		//y = x;	// you should see a warning here because x should be final
		//z = 3;	// not allowed -> final
	}

	/**
	 * This test does not change the internal state.
	 */
	@Test
	public void testImmutable() {
		y = 0;
		LOG.debug("y = " + y);
		assertEquals(0, y);
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
    public void testErrors() throws IOException, SAXException {
        checkErrors("patterntesting/check/ct/ImmutableSample.java,"
                + "patterntesting/check/ct/*ImmutableAspect.aj", "ImmutableTest.xml");
    }

}
