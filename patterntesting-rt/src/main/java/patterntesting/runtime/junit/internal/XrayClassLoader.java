/*
 * $Id: XrayClassLoader.java,v 1.6 2016/12/18 20:19:41 oboehm Exp $
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
 * (c)reated 26.12.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit.internal;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patterntesting.runtime.monitor.ClasspathMonitor;
import patterntesting.runtime.util.Converter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * If we want to load a class and see what happens if another class (needed by
 * the original class) is missing we need a class loader where we can control
 * the classpath and other things.
 * <p>
 * Since 1.6 this class is now moved from the experimental to the internal
 * package.
 * </p>
 *
 * @author oliver
 * @since 1.1 (26.12.2010)
 */
public final class XrayClassLoader extends ClassLoader {

	private static final ClasspathMonitor classpathMonitor = ClasspathMonitor.getInstance();
	private static final Logger LOG = LogManager.getLogger(XrayClassLoader.class);
	private final Map<String, Class<?>> loadedClassMap = new HashMap<>();

	/**
	 * Load class.
	 *
	 * @param classname
	 *            the classname
	 * @return the class
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 * @see java.lang.ClassLoader#loadClass(java.lang.String)
	 */
	@Override
	public Class<?> loadClass(final String classname) throws ClassNotFoundException {
		Class<?> loaded = loadedClassMap.get(classname);
		if (loaded == null) {
			try {
				loaded = findClass(classname);
			} catch (SecurityException ce) {
				LOG.debug("Using parent to load " + classname + ":", ce);
				loaded = super.loadClass(classname);
			}
			loadedClassMap.put(classname, loaded);
		}
		return loaded;
	}

	/**
	 * Loads the class with the given binary name. If the given class is a Java
	 * class from the bootstrap class we ask the parent to do the job. Otherwise
	 * we would get a security exception or something like that.
	 *
	 * @param name
	 *            the binary name of the class
	 * @param resolve
	 *            inidicates if the class should be resolved (linked) or not
	 * @return the loaded class
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	@Override
	protected synchronized Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
		if (name.startsWith("java")) {
			return super.loadClass(name, resolve);
		}
		Class<?> c = findLoadedClass(name);
		if (c == null) {
			c = findClass(name);
		}
		if (resolve) {
			resolveClass(c);
		}
		LOG.trace("{} loaded with resolve={}.", c, resolve);
		return c;
	}

	/**
	 * Gets the loaded classed of this classloader here.
	 *
	 * @return the loaded classed
	 */
	public Set<Class<?>> getLoadedClasses() {
		return new HashSet<>(this.loadedClassMap.values());
	}

	/**
	 * Find class.
	 *
	 * @param classname
	 *            the classname
	 * @return the class
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 * @see java.lang.ClassLoader#findClass(java.lang.String)
	 */
	@Override
	protected Class<?> findClass(final String classname) throws ClassNotFoundException {
		URI classUri = classpathMonitor.whichClass(classname);
		try {
			byte[] data = read(classUri);
			return defineClass(classname, data, 0, data.length);
		} catch (IOException ioe) {
			throw new ClassNotFoundException("can't load class " + classname, ioe);
		}
	}

	private static byte[] read(final URI uri) throws IOException {
		LOG.trace("loading " + uri + "...");
		try {
			File file = new File(uri);
			return FileUtils.readFileToByteArray(file);
		} catch (IllegalArgumentException iae) {
			String scheme = uri.getScheme();
			if ("jar".equals(scheme)) {
				LOG.debug("Will read " + uri + " as JAR:", iae);
				return readJar(uri);
			} else {
				throw new IllegalArgumentException("don't know how to load " + uri, iae);
			}
		}
	}

	private static byte[] readJar(final URI uri) throws IOException {
		File file = Converter.toFile(StringUtils.substringBefore(uri.toString(), "!"));
		String classpath = StringUtils.substringAfterLast(uri.toString(), "!");
		JarFile jarFile = new JarFile(file);
		JarEntry entry = getEntry(classpath, jarFile);
		InputStream istream = jarFile.getInputStream(entry);
		try {
			return IOUtils.toByteArray(istream);
		} finally {
			istream.close();
		}
	}

	private static JarEntry getEntry(final String classpath, final JarFile jarFile) {
		JarEntry entry = jarFile.getJarEntry(classpath);
		if (entry == null) {
			entry = jarFile.getJarEntry(classpath.substring(1));
		}
		return entry;
	}

}
