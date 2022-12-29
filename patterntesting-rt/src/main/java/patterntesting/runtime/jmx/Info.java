/*
 * Copyright (c) 2013 by Oli B.
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
 * (c)reated 06.04.2014 by Oli B. (boehm@javatux.de)
 */

package patterntesting.runtime.jmx;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;
import java.util.jar.*;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.*;

import patterntesting.runtime.NullConstants;
import patterntesting.runtime.io.Resource;
import clazzfish.monitor.ClasspathMonitor;

/**
 * This Info bean is registered as MBean and provides some information about
 * PatternTesting. Since 1.6 it can be now subclassed and provides information
 * from the Manifest file.
 *
 * @author oliver (boehm@javatux.de)
 * @since 1.4 (06.04.2014)
 */
public class Info implements InfoMBean {

	private static final Logger LOG = LoggerFactory.getLogger(Info.class);
	private static final String MANIFEST_URI = "Manifest-URI";
	private final Properties properties;
	private final Manifest manifest;

	/** Info about PatternTesting itself. */
	public static final Info PATTERNTESTING = new Info();

	static {
		MBeanHelper.registerMBean("patterntesting.runtime:name=Info", Info.PATTERNTESTING);
	}

	/**
	 * Instantiates a new info.
	 */
	public Info() {
		this(Info.class);
	}

	/**
	 * Instantiates a new builds the info.
	 *
	 * @param clazz
	 *            die Klasse, von der ich die Build-Infos will.
	 */
	public Info(final Class<?> clazz) {
		this.properties = loadProperties(clazz);
		this.manifest = getManifest(clazz);
	}

	private static Properties loadProperties(final Class<?> clazz) {
		Properties properties = new Properties();
		InputStream istream = clazz.getResourceAsStream("info.properties");
		try {
			if (istream == null) {
				throw new IOException("no info.properties found for " + clazz);
			}
			LOG.debug("Read info.properties for {}.", clazz);
			properties.load(istream);
		} catch (IOException ioe) {
			LOG.warn("Cannot read info.properties for {}:", clazz, ioe);
			properties.put("project.version", getImplementationVersion(clazz));
		} finally {
			IOUtils.closeQuietly(istream);
		}
		return properties;
	}

	private static String getImplementationVersion(Class<?> clazz) {
		Package pkg = clazz.getPackage();
		String version = pkg.getImplementationVersion();
		if (version == null) {
			String nameAndVersion = clazz.getModule().getDescriptor().toNameAndVersion();
			version = StringUtils.substringAfter(nameAndVersion, "@");
		}
		return version;
	}

	private static Manifest getManifest(final Class<?> clazz) {
		ClasspathMonitor cpmon = ClasspathMonitor.getInstance();
		String clazzURI = cpmon.whichClassPath(clazz).toString();
		ClassLoader classLoader = clazz.getClassLoader();
		if (classLoader == null) {
			LOG.warn("Will return emtpy MANIFEST because got no classloader for {}.", clazz);
			return new Manifest();
		}
		try {
			Enumeration<URL> resources = classLoader.getResources("META-INF/MANIFEST.MF");
			while (resources.hasMoreElements()) {
				URI uri = resources.nextElement().toURI();
				String path = uri.toString();
				if (path.startsWith(clazzURI)) {
					return getManifest(uri);
				}
				LOG.trace("{} does not match {} for {}.", uri, clazzURI, clazz);
			}
		} catch (IOException| URISyntaxException ex) {
			LOG.warn("Cannot read MANIFEST for {}:", clazz, ex);
		}
		LOG.error("No MANIFEST for {} found, will use first one as fallback.", clazz);
		return getFirstManifest(clazz);
	}

	private static Manifest getFirstManifest(final Class<?> clazz) {
		try {
			URI uri = clazz.getClassLoader().getResource("META-INF/MANIFEST.MF").toURI();
			LOG.trace("Found {} as first MANIFEST of {}.", uri, clazz);
			return getManifest(uri);
		} catch (IOException | URISyntaxException ex) {
			LOG.warn("Will return empty MANIFEST because cannot read first one for {}:", clazz, ex);
		}
		return new Manifest();
	}

	private static Manifest getManifest(final URI uri) throws IOException {
		String content = IOUtils.toString(uri, StandardCharsets.UTF_8);
		InputStream istream = new ByteArrayInputStream(content.getBytes("UTF8"));
		try {
			Manifest manifest = new Manifest(istream);
			manifest.read(istream);
			manifest.getMainAttributes().putValue(MANIFEST_URI, uri.toString());
			return manifest;
		} finally {
			istream.close();
		}
	}

	/**
	 * Gets the manifest.
	 *
	 * @return the manifest
	 * @since 1.6
	 */
	public Manifest getManifest() {
		return this.manifest;
	}

	/**
	 * Gets the manifest uri.
	 *
	 * @return the manifest uri
	 * @since 1.6
	 */
	@Override
	public URI getManifestURI() {
		return URI.create(this.manifest.getMainAttributes().getValue(MANIFEST_URI));
	}

	/**
	 * Gets the properties.
	 *
	 * @return the properties
	 * @since 1.6
	 */
	public Properties getProperties() {
		return this.properties;
	}

	/**
	 * Gets the properties, but sorted and as map.
	 *
	 * @return the infos
	 * @see Info#getProperties()
	 */
	@SuppressWarnings("unchecked")
	public SortedMap<String, String> getInfos() {
		SortedMap<?, ?> sortedProps = new TreeMap<>(this.properties);
		SortedMap<String, String> infos = (SortedMap<String, String>) sortedProps;
		addAttributesTo(infos, this.manifest.getMainAttributes());
		return infos;
	}

	private static void addAttributesTo(final SortedMap<String, String> infos, final Attributes attributes) {
		for (Entry<Object, Object> entry : attributes.entrySet()) {
			infos.put(entry.getKey().toString(), entry.getValue().toString());
		}
	}

	/**
	 * Gets the version.
	 *
	 * @return the version
	 * @see InfoMBean#getVersion()
	 */
	@Override
	public String getVersion() {
		return this.properties.getProperty("project.version", "unknown");
	}

	/**
	 * Gets the builds the time of PatternTesting.
	 *
	 * @return the builds the time
	 */
	@Override
	public Date getBuildTime() {
		Resource resource = new Resource(this.getManifestURI());
		try {
			return resource.getModificationDate();
		} catch (IOException ioe) {
			LOG.info("Will return {} because cannot get build time:", NullConstants.NULL_DATE, ioe);
			return NullConstants.NULL_DATE;
		}
	}

}
