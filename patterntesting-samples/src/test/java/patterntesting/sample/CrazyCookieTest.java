/*
 * Copyright (c) 2019 by Oliver Boehm
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
 */
package patterntesting.sample;

import org.junit.jupiter.api.Test;
import patterntesting.runtime.junit.ObjectTester;

import java.io.*;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * We don't test here what happens if a cookie is loaded after the restart of
 * the JVM. This would require an extra classloader and extra work.
 * We test here only if the CrazyCookie can be stored.
 *
 * @author oliver
 * @since 04.07.2009
 */
public final class CrazyCookieTest {

	private CrazyCookie cookie = new CrazyCookie();

	/**
	 * Test method for {@link CrazyCookie#store()}.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testStore() throws IOException {
		cookie.store();
		CrazyCookie stored = CrazyCookie.load(cookie.getFile());
		ObjectTester.assertEquals(cookie, stored);
	}

	@Test
	public void testNotEquals() {
		assertNotEquals(cookie, new Date());
		assertNotEquals(cookie, new CrazyCookie());
	}

	@Test
	public void testLoadWithException() throws IOException {
		File corruptFile = new File("target", "hello.world");
		try (OutputStream ostream = new FileOutputStream(corruptFile)) {
			ObjectOutput output = new ObjectOutputStream(ostream);
			output.writeObject("Hello World!");
		}
		assertThrows(IOException.class, () -> CrazyCookie.load(corruptFile));
	}

}
