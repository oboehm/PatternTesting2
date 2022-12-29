/*
 * $Id: Resource.java,v 1.7 2016/12/18 20:19:36 oboehm Exp $
 *
 * Copyright (c) 2016 by Oliver Boehm
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
 * (c)reated 05.01.2016 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.io;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.LoggerFactory;
import org.slf4j.*;

import patterntesting.runtime.util.Converter;

/**
 * A resource can be a file, an entry in a jar, tar or zip file or a resource in
 * the classpath.
 *
 * @author oliver
 * @version $Revision: 1.7 $
 * @since 1.6 (05.01.2016)
 */
public class Resource {

	private static final Logger LOG = LoggerFactory.getLogger(Resource.class);
	private final URI uri;

	/**
	 * Instantiates a new resource.
	 *
	 * @param uri
	 *            the uri
	 */
	public Resource(final URI uri) {
		this.uri = uri;
	}

	/**
	 * Gets the modification date of the resource.
	 *
	 * @return the modification date
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public Date getModificationDate() throws IOException {
		String scheme = this.uri.getScheme();
		if ("file".equalsIgnoreCase(scheme)) {
			File file = new File(this.uri);
			return new Date(file.lastModified());
		} else if ("jar".equalsIgnoreCase(scheme)) {
			return getModificationTimeOf(Converter.toFile(this.uri));
		} else {
			LOG.warn("Will return {} as Date for unsupported scheme '{}'.", new Date(0L), scheme);
			return new Date(0L);
		}
	}

	private static Date getModificationTimeOf(final File jarFile) throws IOException {
		String[] parts = jarFile.toString().split("!");
		JarFile jar = new JarFile(parts[0]);
		String entryName = parts[1];
		if (entryName.startsWith("/")) {
			entryName = entryName.substring(1);
		}
		try {
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
				JarEntry element = entries.nextElement();
				if (entryName.equals(element.getName())) {
					return new Date(element.getTime());
				}
			}
			LOG.info("Will return {} as Date for missing entry '{}' in {}.", new Date(0L), entryName, jar);
			return new Date(0L);
		} finally {
			jar.close();
		}
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.uri.toString();
	}

}
