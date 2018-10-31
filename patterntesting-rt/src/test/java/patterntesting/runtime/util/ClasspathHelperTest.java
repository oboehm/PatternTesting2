/**
 * $Id: ClasspathHelperTest.java,v 1.5 2016/12/18 20:19:37 oboehm Exp $
 *
 * Copyright (c) 2009 by Oliver Boehm
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
 * (c)reated 29.12.2009 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;

/**
 * The Class ClasspathHelperTest.
 *
 * @author oliver
 */
public class ClasspathHelperTest {

    /**
     * Test method for {@link ClasspathHelper#getParent(URI, String)} .
     *
     * @throws URISyntaxException in case of a wrong URI
     */
	@Test
	public void testGetParentURIString() throws URISyntaxException {
		URI uri = new URI("jar:file:/a/b/c.jar!/d/e.class");
		URI parent = ClasspathHelper.getParent(uri, "/d/e.class");
		assertEquals("jar:file:/a/b/c.jar", parent.toString());
	}

    /**
     * Test method for {@link ClasspathHelper#getParent(URI, Class)} .
     *
     * @throws URISyntaxException in case of a wrong URI
     */
	@Test
	public void testGetParentURIClass() throws URISyntaxException {
		URI uri = new URI("jar:file:/a/b/c.jar!/java/lang/String.class");
		URI parent = ClasspathHelper.getParent(uri, String.class);
		assertEquals("jar:file:/a/b/c.jar", parent.toString());
	}

    /**
     * Test method for {@link ClasspathHelper#getParent(URI, String)} .
     *
     * @throws URISyntaxException in case of a wrong URI
     */
	@Test
	public void testGetParentWithBundelresource() throws URISyntaxException {
        URI uri = new URI("bundleresource://167.fwk-450682364/javax/ejb/EJBException.class");
        URI parent = ClasspathHelper.getParent(uri, "/javax/ejb/EJBException.class");
        assertEquals("bundleresource://167.fwk-450682364", parent.toString());
    }

}
