package patterntesting.runtime.monitor.internal;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.util.*;

import javax.management.InstanceNotFoundException;
import javax.management.JMException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.util.CollectionUtils;

import patterntesting.runtime.annotation.SkipTestOn;
import patterntesting.runtime.jmx.MBeanHelper;
import patterntesting.runtime.junit.*;
import patterntesting.runtime.monitor.loader.CompoundClassLoader;
import patterntesting.runtime.monitor.loader.WebappClassLoader;

/**
 * Here we test some methods from ClasspathDigger.
 *
 * @author oliver
 * @version $Revision: 1.26 $
 * @since 27.07.2009
 */
@RunWith(SmokeRunner.class)
public final class ClasspathDiggerTest extends AbstractDiggerTest {

    private static final Logger LOG = LogManager.getLogger(ClasspathDiggerTest.class);
    private final ClasspathDigger digger = new ClasspathDigger();

    /**
     * Returns the {@link ClasspathDigger} for testing.
     *
     * @return digger
     */
    @Override
    protected AbstractDigger getDigger() {
        return digger;
    }

    /**
     * The tests in this JUnit class are only useful if this test here is ok.
     */
    @Test
    public void testIsClassloaderSupported() {
        assertTrue(digger.getClassLoader() + " is not supported", digger.isClassloaderSupported());
        LOG.info(digger + " supports the given classloader");
    }

    /**
     * Test get loaded class list.
     */
    @Test
    @SkipTestOn(javaVendor = "IBM Corporation")
    public void testGetLoadedClassList() {
        List<Class<?>> classes = digger.getLoadedClasses();
        assertFalse(classes.isEmpty());
        LOG.info("{} classes loaded.", classes.size());
    }

    /**
     * Test get loaded class list from patterntesting-agent. For this test you
     * must start the Java VM with PatternTesting Agent as Java agent:
     * <tt>java -javaagent:patterntesting-agent-1.x.x.jar ...</tt>
     *
     * @throws JMException the jM exception
     */
    @Test
    public void testGetLoadedClassListFromAgent() throws JMException {
        try {
            MBeanHelper.getObjectInstance(ClasspathDigger.AGENT_MBEAN);
            List<Class<?>> classes = digger.getLoadedClassListFromAgent();
            assertFalse(classes.isEmpty());
            LOG.info("{} classes loaded.", classes.size());
        } catch (InstanceNotFoundException e) {
            LOG.warn("You must use patterntesting-agent as Java agent for this test!");
        }
    }

    /**
     * Test for {@link ClasspathDigger#isLoaded(String)}.
     */
    @Test
    public void testIsLoaded() {
        if (ClasspathDigger.isAgentAvailable()) {
            checkIsLoaded(this.getClass());
            checkIsLoaded(ClasspathDigger.class);
            checkIsLoaded(Test.class);
        }
    }

    private void checkIsLoaded(final Class<?> testClass) {
        String classname = testClass.getName();
        assertTrue(classname, digger.isLoaded(classname));
    }

    /**
     * Here we test only if we get we get the loaded packages from the
     * ClassLoader.
     */
    @Test
    public void testGetLoadedPackageArray() {
        Package[] packages = digger.getLoadedPackageArray();
        LOG.info(packages.length + " packages loaded");
        assertTrue(packages.length > 0);
    }

    /**
     * Test method for {@link ClasspathDigger#getClasspath()}.
     */
    @Test
    public void testGetClasspath() {
        checkClasspath(digger.getClasspath());
    }

    private static void checkClasspath(final String[] classpath) {
        for (int i = 0; i < classpath.length; i++) {
            File path = new File(classpath[i]);
            assertTrue("path does not exist: " + path, path.exists());
            LOG.info("{}. path: {}", i+1, path);
        }
    }

    /**
     * Test method for {@link ClasspathDigger#getClasspath()}. But here we
     * want to see if the classpath contains only real path elements. I.e.
     * pathes which does not exist should not be part of the returned
     * classpath array.
     */
    @Test
    public void testGetRealClasspath() {
        String[] classpathes = {
                "target/classes",
                "src/test/resources/patterntesting/runtime/monitor/world.war!/WEB-INF/classes!",
                "src/test/resources/patterntesting/runtime/monitor/world.war!/WEB-INF/lib/patterntesting-agent-1.6.3.jar!"
        };
        String classpath = "gibts/net";
        for(int i = 0; i < classpathes.length; i++) {
            classpath += File.pathSeparator + classpathes[i];
        }
        System.setProperty("test-classpath", classpath);
        String[] realClasspathes = ClasspathDigger.getClasspath("test-classpath");
        ArrayTester.assertEquals(classpathes, realClasspathes);
    }

    /**
     * It is hard to get the classpath in an application server like WLS
     * (Weblogic Server) or others. One (hard) way is to use the loaded
     * packages and look from which jar file or directory each package is
     * loaded.
     *
     * But how can it be tested if this is the correct classpath? I don't know.
     * So it is not really tested here. It is only manually compared which
     * parts of the classpath are missing.
     */
    @Test
    public void testGetClasspathFromPackages() {
    	String[] classpath = digger.getClasspath();
    	String[] packageClasspath = digger.getClasspathFromPackages();
    	Collection<String> missing = findMissingElementsOf(packageClasspath, classpath);
    	for (Iterator<String> iterator = missing.iterator(); iterator.hasNext();) {
			LOG.info("unused: " + iterator.next());
		}
    	Collection<String> toomuch = findMissingElementsOf(classpath, packageClasspath);
    	for (Iterator<String> iterator = toomuch.iterator(); iterator.hasNext();) {
			LOG.info("not in java.class.path: " + iterator.next());
		}
    }

    private static Collection<String> findMissingElementsOf(
            final String[] unknown, final String[] reference) {
    	Collection<String> missing = new ArrayList<>();
    	for (int i = 0; i < unknown.length; i++) {
    	    if (!ArrayUtils.contains(reference, unknown[i])) {
				missing.add(unknown[i]);
			}
		}
    	return missing;
    }

    /**
     * Here we want to test the private getTomcatClasspath(..) method of
     * {@link ClasspathDigger}.
     *
     * @throws MalformedURLException the malformed url exception
     */
    @Test
    public void testGetTomcatClasspath() throws MalformedURLException {
        File file = new File("/tmp/one");
        ClassLoader tomcat = new WebappClassLoader(file.toURI().toURL());
        ClasspathDigger tomcatDigger = new ClasspathDigger(tomcat);
        String[] classpath = tomcatDigger.getClasspath();
        FileTester.assertEquals(file, new File(classpath[0]));
    }

    /**
     * Here we want to test the private getWebspherClasspath(..) method of
     * {@link ClasspathDigger}.
     */
    @Test
    public void testGetWebsphereClasspath() {
        File[] input = { new File("/tmp/bin"), new File("/tmp/web/WEB-INF/classes"),
                new File("/tmp/web"), new File("/tmp/lib/commons-lang.jar")};
        CompoundClassLoader wsLoader = new CompoundClassLoader(input);
        ClasspathDigger wsDigger = new ClasspathDigger(wsLoader);
        String[] classpath = wsDigger.getClasspath();
        FileTester.assertEquals(input[0], new File(classpath[0]));
        FileTester.assertEquals(input[1], new File(classpath[1]));
        FileTester.assertEquals(input[3], new File(classpath[2]));
        assertEquals(3, classpath.length);
    }

    /**
     * Here we use the real tomcat class loader to reproduce
     * <a href="https://sourceforge.net/p/patterntesting/bugs/34/">bug 34</a>.
     *
     * @throws MalformedURLException the malformed url exception
     * @since 1.6
     */
    @Test
    public void testGetTomcat8() throws MalformedURLException {
        org.apache.catalina.loader.WebappClassLoader tomcat = new org.apache.catalina.loader.WebappClassLoader(Thread.currentThread().getContextClassLoader());
        ClasspathDigger tomcatDigger = new ClasspathDigger(tomcat);
        String[] classpath = tomcatDigger.getClasspath();
        checkClasspath(classpath);
    }

    /**
     * Test method for {@link ClasspathDigger#getPackageArray()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetPackageArray() {
        String[] packages = digger.getPackageArray();
        assertThat(packages.length, not(0));
        List<String> pkgs = CollectionUtils.arrayToList(packages);
        assertThat(pkgs, hasItem("org/junit/"));
        assertFalse("contains null values: " + pkgs, pkgs.contains(null));
        Package[] clPackages = Package.getPackages();
        LOG.info("{} packages found, {} packages loaded.", packages.length, clPackages.length);
        assertTrue("elements missing in packages", packages.length >= clPackages.length);
    }

    /**
     * Test method for {@link ClasspathDigger#getLoadedResources()}.
     */
    @Test
    public void testGetLoadedResources() {
        List<String> loadedResources = digger.getLoadedResources();
        LOG.info("{} resources loaded.", loadedResources.size());
        assertNotNull(this.getClass().getResource("/log4j2.xml"));
        assertThat(loadedResources, hasItem("/log4j2.xml"));
        assertThat(loadedResources, not(hasItem("/patterntesting")));
        assertThat(loadedResources, hasItem("/patterntesting/runtime/junit/file1.txt"));
        assertThat(loadedResources, not(hasItem("/patterntesting/runtime/BrokenClass.class")));
    }

    /**
     * Test method for {@link ClasspathDigger#getClasses()}.
     */
    @Test
    public void testGetClasses() {
        Set<String> classes = digger.getClasses();
        assertThat(classes, hasItem(this.getClass().getName()));
    }

    /**
     * The {@link ClasspathDigger} has problems with executable war's generated
     * by spring-boot (and probably also generated by other tools). In this
     * situatation the following stacktrace was observed:
     * <pre>
     * WARN ClasspathMonitor - Cannot add classes from .../some-artifact.war!/WEB-INF/classes!:
     * java.io.FileNotFoundException: .../some-artifact.war!/WEB-INF/classes! (No such file or directory)
     *     at java.util.zip.ZipFile.open(Native Method) ~[?:1.8.0_45]
     *     at java.util.zip.ZipFile.&lt;init&gt;(ZipFile.java:220) ~[?:1.8.0_45]
     *     at java.util.zip.ZipFile.&lt;init&gt;(ZipFile.java:150) ~[?:1.8.0_45]
     *     at java.util.zip.ZipFile.&lt;init&gt;(ZipFile.java:164) ~[?:1.8.0_45]
     *     at patterntesting.runtime.monitor.internal.ClasspathDigger.addElementsFromArchive(ClasspathDigger.java:312) ~[patterntesting-rt-1.6.3.jar!/:1.6.3]
     *     at patterntesting.runtime.monitor.ClasspathMonitor.addClasses(ClasspathMonitor.java:938) [patterntesting-rt-1.6.3.jar!/:1.6.3]
     *     at patterntesting.runtime.monitor.ClasspathMonitor.createClasspathClassSet(ClasspathMonitor.java:916) [patterntesting-rt-1.6.3.jar!/:1.6.3]
     *     at patterntesting.runtime.monitor.ClasspathMonitor.getClasspathClassArray(ClasspathMonitor.java:922) [patterntesting-rt-1.6.3.jar!/:1.6.3]
     *     at patterntesting.runtime.monitor.ClasspathMonitor.access$0(ClasspathMonitor.java:921) [patterntesting-rt-1.6.3.jar!/:1.6.3]
     *     at patterntesting.runtime.monitor.ClasspathMonitor$1.call(ClasspathMonitor.java:134) [patterntesting-rt-1.6.3.jar!/:1.6.3]
     *     at patterntesting.runtime.monitor.ClasspathMonitor$1.call(ClasspathMonitor.java:1) [patterntesting-rt-1.6.3.jar!/:1.6.3]
     *     ...
     * </pre>
     * <p>
     * We cannot mock {@link ClasspathDigger#getClasspath()} to dig into a web
     * classpath. So we mock a {@link URLClassLoader} to influence the result of
     * {@link ClasspathDigger#getClasspath()}.
     * </p>
     *
     * @throws MalformedURLException as a result of using an
     *         {@link URLClassLoader}
     */
    @Test
    public void testWarClasses() throws MalformedURLException {
        ClasspathDigger warDigger = createClasspathDigger(WORLD_WAR, "!/WEB-INF/classes");
        checkGetClasses(warDigger, "patterntesting.sample.World");
    }

    /**
     * This is the same test as before but now for a JAR inside a WAR. E.g.
     * here we have now a nested JAR hierarchy to parse.
     *
     * @throws MalformedURLException as a result of using an
     *         {@link URLClassLoader}
     */
    @Test
    public void testWarJar() throws MalformedURLException {
        ClasspathDigger warDigger = createClasspathDigger(WORLD_WAR, "!/WEB-INF/lib/patterntesting-agent-1.6.3.jar");
        checkGetClasses(warDigger, "patterntesting.agent.ClasspathAgent");
    }

    /**
     * Here we test the next level of nesting: a JAR inside a WAR inside an EAR.
     *
     * @throws MalformedURLException as a result of using an
     *         {@link URLClassLoader}
     */
    @Test
    public void testEarWarJar() throws MalformedURLException {
        ClasspathDigger warDigger = createClasspathDigger(WORLD_EAR,
                "!/world.war!/WEB-INF/lib/patterntesting-agent-1.6.3.jar");
        checkGetClasses(warDigger, "patterntesting.agent.ClasspathAgent");
    }

    /**
     * Here we test the next level of nesting: a classes directory inside a WAR
     * inside an EAR.
     *
     * @throws IOException as a result of using an {@link URLClassLoader}
     */
    @Test
    public void testEarWarClasses() throws IOException {
        ClasspathDigger warDigger = createClasspathDigger(WORLD_EAR, "!/world.war!/WEB-INF/classes");
        checkGetClasses(warDigger, "patterntesting.sample.World");
    }

    private void checkGetClasses(ClasspathDigger warDigger, String classname) {
        Set<String> classes = warDigger.getClasses();
        assertThat(classes, hasItem(classname));
    }

    private static ClasspathDigger createClasspathDigger(File jar, String path) throws MalformedURLException {
        URLClassLoader mockedClassLoader = mockURLClassLoader(jar, path);
        return new ClasspathDigger(mockedClassLoader);
    }

}
