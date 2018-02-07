/*
 * $Id: ClasspathDigger.java,v 1.37 2017/07/16 16:01:18 oboehm Exp $
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
 * (c)reated 15.05.2009 by oliver (ob@aosd.de)
 */
package patterntesting.runtime.monitor.internal;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patterntesting.runtime.exception.NotFoundException;
import patterntesting.runtime.io.ExtendedFile;
import patterntesting.runtime.monitor.ClassloaderType;
import patterntesting.runtime.util.ClasspathHelper;
import patterntesting.runtime.util.Converter;
import patterntesting.runtime.util.Environment;
import patterntesting.runtime.util.ReflectionHelper;

import javax.management.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * This helper class digs into the classloader for information like used
 * classpath and other things. It was extracted from ClasspathMonitor to
 * separate the classloader specific part of it into its own class.
 * <p>
 * Since 1.6.1 this class was moved into the internal package. It is not
 * intended for external use
 * </p>
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.37 $
 * @since 15.05.2009
 */
public class ClasspathDigger extends AbstractDigger {

	/**
	 * The ClasspathAgent as MBean.
	 */
	protected static final ObjectName AGENT_MBEAN;

	protected static final Logger LOG = LogManager.getLogger(ClasspathDigger.class);
	private static final MBeanServer MBEAN_SERVER = ManagementFactory.getPlatformMBeanServer();
	private final ClassLoader classLoader;
	private final String[] bootClassPath = getClasspath("sun.boot.class.path");

	static {
		try {
			AGENT_MBEAN = new ObjectName("patterntesting.agent:type=ClasspathAgent");
		} catch (MalformedObjectNameException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	/**
	 * Instantiates a new classpath digger.
	 */
	public ClasspathDigger() {
		this(Environment.getClassLoader());
	}

	/**
	 * Instantiates a new classpath digger.
	 *
	 * @param cloader
	 *            the cloader
	 * @since 1.2
	 */
	public ClasspathDigger(final ClassLoader cloader) {
		this.classLoader = cloader;
	}

	/**
	 * Gets the class loader.
	 *
	 * @return the classLoader
	 */
	public ClassLoader getClassLoader() {
		return classLoader;
	}

	/**
	 * Checks if is classloader supported.
	 *
	 * @return true, if is classloader supported
	 */
	public boolean isClassloaderSupported() {
		return ClassloaderType.isSupported(classLoader.getClass().getName());
	}

	/**
	 * Checks if the ClasspathAgent is available as MBean. The ClasspathAgent is
	 * needed for classloaders which are not directly supported (e.g. IBM's
	 * classloader of their JDK).
	 *
	 * @return true, if is agent available
	 */
	public static boolean isAgentAvailable() {
		try {
			return MBEAN_SERVER.getObjectInstance(AGENT_MBEAN) != null;
		} catch (InstanceNotFoundException e) {
			LOG.debug("MBean '{}' is not available.", AGENT_MBEAN);
			LOG.trace("ClasspathAgent is not registered at {}:", MBEAN_SERVER, e);
			return false;
		}
	}

	/**
	 * To get the boot classpath the sytem property "sun.boot.class.path" is
	 * used to get them. This will work of course only for the SunVM.
	 *
	 * @return the boot classpath as String array
	 */
	public String[] getBootClasspath() {
		return this.bootClassPath;
	}

	/**
	 * We can use the system property "java.class.path" to get the classpath.
	 * But this works not inside an application server or servlet engine (e.g.
	 * inside Tomcat) because they have their own classloader to load the
	 * classes.
	 * <p>
	 * In the past we tried to use the private (and undocoumented) attribute
	 * "domains" of the classloader. This works for a normal application but
	 * Tomcat's WebappClassLoader listed also classes in the "domains"-Set. Now
	 * we will try to detect the different classloader to access some private
	 * and secret attributes of this classloader.
	 * <p>
	 * At the moment only org.apache.catalina.loader.WebappClassLoader is
	 * supported. For all other classloaders the standard approach using the
	 * system property "java.class.path" is used.
	 *
	 * @return the classpath as String array
	 */
	public String[] getClasspath() {
		try {
			switch (ClassloaderType.toClassloaderType(this.classLoader)) {
			case NET:
				return getNetClasspath();
			case TOMCAT:
                return getTomcatClasspath();
			case TOMCAT8:
				return getTomcat8Classpath();
			case WEBLOGIC:
				return getWeblogicClasspath();
			case WEBSPHERE:
				return getWebsphereClasspath();
			default:
				LOG.trace("using 'java.class.path' to get classpath...");
			}
		} catch (IllegalArgumentException ex) {
			LOG.warn("Will fallback to 'java.class.path' because cannot get classpath from {}:", classLoader, ex);
		}
		return getClasspath("java.class.path");
	}

	private String[] getNetClasspath() {
		URLClassLoader netloader = (URLClassLoader) this.classLoader;
		return getAsClasspath(netloader.getURLs());
	}

	private String[] getTomcatClasspath() {
		URL[] repositoryURLs = (URL[]) ClassloaderType.TOMCAT.getClasspathFrom(this.classLoader);
		return getAsClasspath(repositoryURLs);
	}

    private String[] getTomcat8Classpath() {
        @SuppressWarnings("unchecked")
        List<URL> repositoryURLs = (List<URL>) ClassloaderType.TOMCAT8.getClasspathFrom(this.classLoader);
        return getAsClasspath(repositoryURLs);
    }

    private static String[] getAsClasspath(final List<URL> repositoryURLs) {
        return getAsClasspath(repositoryURLs.toArray(new URL[repositoryURLs.size()]));
    }

	private static String[] getAsClasspath(final URL[] repositoryURLs) {
		String[] cp = new String[repositoryURLs.length];
		for (int i = 0; i < cp.length; i++) {
			cp[i] = Converter.toAbsolutePath(Converter.toURI(repositoryURLs[i]));
		}
		return cp;
	}

	private String[] getWeblogicClasspath() {
		return getClasspathFromPackages();
	}

	/**
	 * The WebSphere classload has an attribute 'localClassPath' where the
	 * classpath is stored as String. Unfortunately the path of HTML and JSP
	 * pages is also part of this classpath, e.g. the classpath looks like
	 * <p>
	 * "...:/tmp/web/WEB-INF/classes:/tmp/web/:...".
	 * <p>
	 * So we must remove e.g. "/tmp/web" from the classpath to get the wanted
	 * Java classpath.
	 *
	 * @return the websphere classpath
	 */
	private String[] getWebsphereClasspath() {
		String localClassPath = (String) ClassloaderType.WEBSPHERE.getClasspathFrom(this.classLoader);
		String[] classpath = splitClasspath(localClassPath);
		List<File> files = new ArrayList<>(classpath.length);
		List<File> toBeRemoved = new ArrayList<>(classpath.length);
		File webinfClasses = new File("WEB-INF", "classes");
		for (int i = 0; i < classpath.length; i++) {
			ExtendedFile f = new ExtendedFile(classpath[i]);
			files.add(f);
			if (f.endsWith(webinfClasses)) {
				toBeRemoved.add(f.getBaseDir(webinfClasses));
			}
		}
		for (File file : toBeRemoved) {
			files.remove(file);
		}
		return ExtendedFile.toStringArray(files);
	}

	/**
	 * Gets the classpath.
	 *
	 * @param key
	 *            the key
	 * @return the classpath as String array
	 */
	protected static String[] getClasspath(final String key) {
		String classpath = System.getProperty(key);
		if (classpath == null) {
			LOG.info(key + " is not set (not a SunVM?)");
			return new String[0];
		}
		String cp[] = splitClasspath(classpath);
		return validatedClasspath(cp);
	}

	private static String[] splitClasspath(final String classpath) {
		String[] cp = StringUtils.split(classpath, File.pathSeparator);
		for (int i = 0; i < cp.length; i++) {
			if (cp[i].endsWith(File.separator)) {
				cp[i] = cp[i].substring(0, (cp[i].length() - 1));
			}
		}
		return cp;
	}

	private static String[] validatedClasspath(String[] classpathes) {
		List<String> validated = new ArrayList<>();
		for (String name : classpathes) {
			File path = new File(name);
			if (name.contains("!") || path.exists()) {
				validated.add(name);
			} else {
				LOG.debug("'{}' in classpath is ignored because it does not exists.", path);
			}
		}
		return validated.toArray(new String[0]);
	}

	/**
	 * Get all packages found in the classpath.
	 * <p>
	 * In contradiction to {@link #getLoadedPackageArray()} we don't ask the
	 * classloader (e.g. by calling
	 * <code>classLoader.getResourceAsStream("")</code>) for the packages
	 * because the classloader would return only these packages it has already
	 * loaded.
	 * </p>
	 *
	 * @return the packages
	 */
	public String[] getPackageArray() {
		Collection<String> packages = this.getPackages();
		return packages.toArray(new String[packages.size()]);
	}

	private Collection<String> getPackages() {
		Collection<String> packages = new TreeSet<>();
		String[] classpath = this.getClasspath();
		for (int i = 0; i < classpath.length; i++) {
			addPackages(packages, new File(classpath[i]));
		}
		return packages;
	}

	private static void addPackages(final Collection<String> packages, final File path) {
		LOG.trace("Adding packages from {}...", path);
		try {
			if (path.isDirectory()) {
				addPackagesFromDir(packages, path);
			} else {
				addElementsFromArchive(packages, path, "/");
			}
		} catch (IOException ioe) {
			LOG.warn("Cannot add packages from {}:", path.getAbsolutePath(), ioe);
		}
	}

	private static void addPackagesFromDir(final Collection<String> packages, final File dir) throws IOException {
		ResourceWalker walker = new ResourceWalker(dir, DirectoryFileFilter.DIRECTORY);
		packages.addAll(walker.getPackages());
	}

	private static void addElementsFromArchive(Collection<String> elements, File archive, String suffix)
			throws IOException {
        Collection<String> allElements = readElementsFromNestedArchive(archive);
        for(String resource : allElements) {
            if (resource.endsWith(suffix)) {
                elements.add(Converter.resourceToClass(resource));
            }
        }
	}

	/**
	 * Returns the packages which were loaded by the classloader.
	 *
	 * @return array with the loaded packages
	 */
	public Package[] getLoadedPackageArray() {
		return Package.getPackages();
	}

	/**
	 * Here we use the loaded packages to calculate the classpath. For each
	 * loaded package we will look from which jar file or directory this package
	 * is loaded.
	 *
	 * @return the found classpath as string array
	 * @since 27-Jul-2009
	 */
	protected String[] getClasspathFromPackages() {
		Set<URI> packageURIs = new LinkedHashSet<>();
		Package[] packages = this.getLoadedPackageArray();
		for (int i = 0; i < packages.length; i++) {
			String resource = Converter.toResource(packages[i]);
			URI uri = ResourcepathDigger.whichResource(resource, this.classLoader);
			if (uri != null) {
				URI path = ClasspathHelper.getParent(uri, resource);
				packageURIs.add(path);
			}
		}
		return getClasspathFromPackages(packageURIs);
	}

	private String[] getClasspathFromPackages(final Set<URI> packages) {
		String[] classpath = new String[packages.size()];
		Iterator<URI> iterator = packages.iterator();
		for (int i = 0; i < classpath.length; i++) {
			URI uri = iterator.next();
			classpath[i] = Converter.toAbsolutePath(uri);
		}
		return classpath;
	}

	/**
	 * Gets the resources of the given name. Normally that would only be one
	 * element but some resources (like the MANIFEST.MF file) can appear
	 * several times in the classpath.
	 *
	 * @param name the name of the resource
	 * @return all resources with the given name
	 */
	public Enumeration<URL> getResources(final String name) {
		try {
			Enumeration<URL> resources = this.classLoader.getResources(name);
			if (!resources.hasMoreElements()) {
				LOG.debug("Resource '{}' not found in classpath", name);
				if (name.startsWith("/")) {
					return getResources(name.substring(1));
				}
			}
			Set<URL> resourceSet = asSet(resources);
			return new Vector<URL>(resourceSet).elements();
		} catch (IOException ioe) {
			throw new NotFoundException("resource '" + name + "' not found in classpath", ioe);
		}
	}
	
	private static Set<URL> asSet(Enumeration<URL> enums) {
		Set<URL> set = new HashSet<>();
		while (enums.hasMoreElements()) {
			set.add(enums.nextElement());
		}
		return set;
	}

	/**
	 * Gets the resourcepathes which belongs to the given collection of
	 * resources.
	 *
	 * @param resources the resources
	 * @return the resourcepath set
	 * @since 1.6.3
	 */
	public SortedSet<URI> getResourcepathSet(Collection<String> resources) {
		SortedSet<URI> rscPath = new TreeSet<>();
		for (String rsc : resources) {
			Enumeration<URL> urls = this.getResources(rsc);
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				URI path = Converter.toURI(url);
				rscPath.add(ClasspathHelper.getParent(path, rsc));
			}
		}
		return rscPath;
	}

	/**
	 * Returns the URI of the given resource and the given classloader. If the
	 * resource is not found it will be tried again with/without a leading "/"
	 * and with the parent classloader.
	 *
	 * @param name
	 *            resource name (e.g. "log4j.properties")
	 * @return URI of the given resource (or null if resource was not found)
	 */
	public URI whichResource(final String name) {
		return ResourcepathDigger.whichResource(name, this.classLoader);
	}

	/**
	 * Checks if the given classname is loaded. Why does we use not Class as
	 * parameter here? If you would allow a parameter of type "Class" this class
	 * will be problably loaded before and this method will return always true!
	 *
	 * @param classname
	 *            name of the class
	 * @return true if class is loaded
	 */
	public boolean isLoaded(final String classname) {
		for (Iterator<Class<?>> iterator = getLoadedClasses().iterator(); iterator.hasNext();) {
			Class<?> loaded = iterator.next();
			if (classname.equals(loaded.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Puts also the classloader in the toString representation.
	 *
	 * @return string containing the class laoder
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ClasspathDigger for " + this.classLoader;
	}

	/**
	 * Returns a list of classes which were loaded by the given classloader.
	 * <p>
	 * Ok, we must do some hacks here: there is an undocumented attribute
	 * "classes" which contains the loaded classes.
	 * </p>
	 * <p>
	 * HANDLE WITH CARE (it's a hack and it depends on the used classloader)
	 * </p>
	 *
	 * @return list of classes
	 */
	@SuppressWarnings("unchecked")
	public List<Class<?>> getLoadedClasses() {
		try {
			Field field = ReflectionHelper.getField(classLoader.getClass(), "classes");
			List<Class<?>> classList = (List<Class<?>>) field.get(classLoader);
			return new ArrayList<>(classList);
		} catch (NoSuchFieldException ex) {
			LOG.debug("Classloader {} has no field 'classes':", classLoader, ex);
		} catch (IllegalAccessException ex) {
			LOG.debug("Cannot access field 'classes' of {}:", classLoader, ex);
		}
		LOG.debug("Will use agent to get loaded classed because classloader {} is not supported.", classLoader);
		return getLoadedClassListFromAgent();
	}

	/**
	 * Gets the loaded class list from patterntesting-agent. For this method you
	 * must start the Java VM with PatternTesting Agent as Java agent
	 * (<tt>java -javaagent:patterntesting-agent-1.x.x.jar ...</tt>) because
	 * this MBean is needed for the loaded classes.
	 * <p>
	 * This class is protected for test reason.
	 * </p>
	 *
	 * @return the loaded class list from agent
	 */
	protected List<Class<?>> getLoadedClassListFromAgent() {
		try {
			LOG.trace("Using \"{}\" as fallback for unsupported classloader {}.", AGENT_MBEAN, this.classLoader);
			Class<?>[] classes = (Class<?>[]) MBEAN_SERVER.invoke(AGENT_MBEAN, "getLoadedClasses",
					new Object[] { this.getClass().getClassLoader() }, new String[] { ClassLoader.class.getName() });
			return Arrays.asList(classes);
		} catch (InstanceNotFoundException e) {
			LOG.warn("MBean \"{}\" not found ({}) - be sure to call patterntesting as agent"
					+ " ('java -javaagent:patterntesting-agent-1.x.x.jar...')", AGENT_MBEAN, e);
			return new ArrayList<>();
		} catch (JMException e) {
			LOG.warn("Cannot call 'getLoadedClasses(..)' from MBean \"{}\"", AGENT_MBEAN, e);
			return new ArrayList<>();
		}
	}

	/**
	 * Asks the classloader for the resources which it already loaded - either
	 * directly or because the corresponding package was already loaded.
	 *
	 * @return the loaded resources
	 * @since 1.6.3 (15.05.2016)
	 */
	public List<String> getLoadedResources() {
		List<String> resources = getLoadedResourcesOf("/");
		for (Package pkg : this.getLoadedPackageArray()) {
			String packageResource = "/" + pkg.getName().replaceAll("\\.", "/") + "/";
			resources.addAll(this.getLoadedResourcesOf(packageResource));
		}
		return resources;
	}

	private List<String> getLoadedResourcesOf(String packageResource) {
		List<String> loadedResources = new ArrayList<>();
		String packageResourceWithoutSlash = packageResource.substring(1);
		InputStream istream = this.classLoader.getResourceAsStream(packageResourceWithoutSlash);
		if (istream == null) {
			LOG.trace("Cannot load '{}' with {}.", packageResource, this.classLoader);
			return loadedResources;
		}
		try {
			List<String> lines = IOUtils.readLines(istream, StandardCharsets.UTF_8);
			for (String line : lines) {
				String resource = (packageResource + line);
				if (isNormalResource(resource)) {
					loadedResources.add(resource);
				}
			}
		} catch (IOException ioe) {
			LOG.warn("Cannot get resources for package '{}':", packageResource, ioe);
		} finally {
			IOUtils.closeQuietly(istream);
		}
		return loadedResources;
	}

	private static boolean isNormalResource(String resource) {
		return resource.contains(".") && !resource.endsWith(".class");
	}
	
	/**
	 * Digs into the classpath and returns the found classes.
	 * <p>
	 * NOTE: This logic was formerly part of the createClasspathSet(..) method
	 * of the ClasspathMonitor class.
	 * </p>
	 * 
	 * @return classes of the classpath
	 * @since 1.7.2
	 */
    public Set<String> getClasses() {
        Set<String> classSet = new TreeSet<>();
        for (String path : getClasspath()) {
            addClasses(classSet, new File(path));
        }
        return classSet;
    }

    private static void addClasses(final Set<String> classSet, final File path) {
        LOG.trace("Adding classes from {}...", path);
        try {
            if (path.isDirectory()) {
                addClassesFromDir(classSet, path);
            } else {
                addElementsFromArchive(classSet, path, ".class");
            }
        } catch (IOException ioe) {
            LOG.warn("Cannot add classes from " + path.getAbsolutePath() + ":", ioe);
        }
    }

    private static void addClassesFromDir(final Set<String> classSet, final File dir) throws IOException {
        ClassWalker classWalker = new ClassWalker(dir);
        Collection<String> classes = classWalker.getClasses();
        classSet.addAll(classes);
    }

}
