/*
 * $Id: ClassWalker.java,v 1.4 2016/12/18 20:19:41 oboehm Exp $
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
 * (c)reated 14.04.2009 by oliver (ob@aosd.de)
 */
package patterntesting.runtime.monitor.internal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import patterntesting.runtime.util.Converter;

/**
 * The Class ClassWalker.
 * <p>
 * This class is for internal use only. This is the reason why it was moved in
 * v1.6.4 into the internal package.
 * </p>
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 14.04.2009
 */
public class ClassWalker extends ResourceWalker {

	/**
	 * Instantiates a new class walker.
	 *
	 * @param dir
	 *            the dir
	 */
	public ClassWalker(final File dir) {
		super(dir, ".class");
	}

	/**
	 * Walk thru the directories and return all class files as classname, e.g. a
	 * file java/lang/String.class should be returned as "java.lang.String".
	 * <p>
	 * See also <a href=
	 * "http://commons.apache.org/io/api-release/org/apache/commons/io/DirectoryWalker.html">
	 * DirectoryWalker</a>.
	 * </p>
	 *
	 * @return a collection of classnames
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public Collection<String> getClasses() throws IOException {
		Collection<String> resources = this.getResources();
		Collection<String> classes = new ArrayList<>(resources.size());
		for (String res : resources) {
			classes.add(Converter.resourceToClass(res));
		}
		return classes;
	}

}
