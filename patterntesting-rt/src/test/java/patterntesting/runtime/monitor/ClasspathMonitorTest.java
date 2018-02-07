/**
 * $Id: ClasspathMonitorTest.java,v 1.47 2017/09/01 20:33:17 oboehm Exp $
 *
 * Copyright (c) 2008 by Oliver Boehm
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
 * (c)reated 10.02.2009 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.monitor;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import patterntesting.runtime.annotation.SkipTestOn;
import patterntesting.runtime.io.ExtendedFile;
import patterntesting.runtime.junit.SmokeRunner;
import patterntesting.runtime.util.ArchivEntry;
import patterntesting.runtime.util.Converter;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.jar.JarFile;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

/**
 * The Class ClasspathMonitorTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.47 $
 * @since 10.02.2009
 */
@RunWith(SmokeRunner.class)
public final class ClasspathMonitorTest extends AbstractMonitorTest {

    private static final Logger LOG = LogManager.getLogger(ClasspathMonitorTest.class);
    private static ClasspathMonitor cpMon;
    private static long instanceTime;

    /**
     * Here we get the instance and measure the time for the
     * {@link #testGetInstancePerformance()} method.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        long t0 = System.currentTimeMillis();
        cpMon = ClasspathMonitor.getInstance();
        instanceTime = System.currentTimeMillis() - t0;
        LOG.info("ClasspathMonitorTest is set up with {}.", cpMon);
    }

    /**
     * We want to see the profiled data at the end!.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        ProfileStatistic.getInstance().dumpStatistic();
        LOG.info("ClasspathMonitorTest is teared down with dumped statistic.");
    }

    /**
     * Gets the monitor.
     *
     * @return the monitor
     * @see AbstractMonitorTest#getMonitor()
     */
    @Override
    protected ClasspathMonitor getMonitor() {
        return ClasspathMonitor.getInstance();
    }

    /**
     * The getInstance() method should be fast because it used also by the
     * other classes for testing. E.g. the ObjectTester and other Tester
     * classes uses it.
     * <p>
     * 19-Apr-2014: the accepted time is now increased from 100 to 500 ms.
     * </p>
     *
     * @since 1.1
     */
    @Test
    public void testGetInstancePerformance() {
        LOG.info(instanceTime + " ms needed to get instance");
        assertTrue("more than 500 ms needed (" + instanceTime + " ms)", instanceTime < 500);
    }

    /**
     * Test which class.
     */
    @Test
    public void testWhichClass() {
        LOG.info("testWhichClass() is started.");
        String className = "java.lang.String";
        URI classURI = cpMon.whichClass(className);
        assertTrue(className + " not found (" + classURI + ")",
                classURI.toString().endsWith("java/lang/String.class"));
    }

    /**
     * Test which classpath.
     */
    @Test
    public void testWhichClasspath() {
        LOG.info("testWhichClasspath() is started.");
        Package p = this.getClass().getPackage();
        URI uri = cpMon.whichClassPath(p);
        LOG.info(p + " was found in " + uri);
        URI expected = cpMon.whichClassPath(this.getClass());
        assertNotNull(expected);
        assertEquals(expected, uri);
    }

    /**
     * Test get resources.
     */
    @Test
    public void testGetResources() {
        LOG.info("testGetResources() is started.");
        checkClassloader("log4j2.xml");
        checkResource("log4j2.xml");
        checkResource("/log4j2.xml");
    }

    /**
     * Test get resource.
     *
     * @throws URISyntaxException
     *             the URI syntax exception
     */
    @Test
    public void testGetResource() throws URISyntaxException {
        LOG.info("testGetResource() is started.");
        URL url = ClasspathMonitor.getResource("/log4j2.xml");
        LOG.info("resource found: " + url);
        assertNotNull(url);
        url.toURI();
    }

    /**
     * Test get classes.
     */
    @Test
    public void testGetClasses() {
        LOG.info("testGetClasses() is started.");
        checkClassloader("java/lang/String.class");
        checkResource("java/lang/String.class");
        checkResource("/java/lang/String.class");
    }

    private void checkClassloader(final String name) {
        ClassLoader loader = ClasspathMonitorTest.class.getClassLoader();
        assertNotNull(loader.getResource(name));
    }

    private void checkResource(final String name) {
        assertTrue(cpMon.getResources(name).hasMoreElements());
    }

    /**
     * ClasspathMonitorTest seems to be 2 times in the classpath during build.
     * So we use now the String class for testing. But also with this class it
     * happened that it appeared 2 times in the classpath, e.g. if you call the
     * test inside your favorite IDE. In most cases this was the same classpath
     * where the doublet appears. Since 2.0 doublets in the same classpath are
     * not regarded as doublet.
     */
    @Test
    public void testGetNoClasses() {
        LOG.info("testGetNoClasses() is started.");
        Class<?> clazz = String.class;
        int n = cpMon.getNoClasses(clazz);
        assertThat(n, is(greaterThan(0)));
        for (int i = 0; i < n; i++) {
            LOG.info("The {} is found in {}.", clazz, cpMon.getDoublet(clazz, i));
        }
        if (n > 1) {
            assertThat(cpMon.getDoublet(clazz, 0), not(cpMon.getDoublet(clazz, 1)));
        }
    }

    /**
     * Normally you should find only one String.class in your classpath. But
     * sometimes you have several rt.jar in your classpath with different
     * String.classes. If this happens correct your classpath.
     */
    @Test
    public void testIsDoubletClass() {
        LOG.info("testIsDoubletClass() is started.");
        assertFalse(cpMon.isDoublet(String.class));
    }

    /**
     * Test get loaded class list.
     */
    @Test
    @SkipTestOn(javaVendor = "IBM")
    public void testGetLoadedClassList() {
        LOG.info("testGetLoadedClassList() is started.");
        List<Class<?>> classes = cpMon.getLoadedClassList();
        LOG.info(classes.size() + " classes loaded");
        assertFalse(classes.isEmpty());
        LOG.trace("loaded classes:\n" + cpMon.getLoadedClassesAsString());
    }

    /**
     * Test is loaded.
     */
    @Test
    @SkipTestOn(javaVendor = "IBM")
    public void testIsLoaded() {
        LOG.info("testIsLoaded() is started.");
        assertTrue(this.getClass() + " is loaded", cpMon.isLoaded(this.getClass().getName()));
        assertFalse("non existing classname", cpMon.isLoaded("nir.wa.na"));
    }

    /**
     * Test get unused classes.
     */
    @Test
    @SkipTestOn(javaVendor = "IBM")
    public void testGetUnusedClasses() {
        LOG.info("testGetUnusedClasses() is started.");
        String[] unusedClasses = cpMon.getUnusedClasses();
        LOG.info(unusedClasses.length + " unused classes found");
        String[] allClasses = cpMon.getClasspathClasses();
        assertTrue("unused Classes (" + unusedClasses.length + ") must be < " + allClasses.length,
                unusedClasses.length < allClasses.length);
    }

    /**
     * Test get classpath classes.
     */
    @Test
    public void testGetClasspathClasses() {
        LOG.info("testGetClasspathClasses() is started.");
        String[] allClasses = cpMon.getClasspathClasses();
        assertTrue("no classes found?", allClasses.length > 0);
    }

    /**
     * Test get classloader details.
     */
    //@Test
    public void testGetClassloaderDetails() {
        LOG.info("testGetClassloaderDetails() is started.");
        LOG.trace("classloader details: " + cpMon.getClassLoaderDetails());
    }

    /**
     * Test get classloader info.
     */
    @Test
    public void testGetClassloaderInfo() {
        LOG.info("testGetClassloaderInfo() is started.");
        String info = cpMon.getClassloaderInfo();
        LOG.info("classloader info: " + info);
        assertTrue("empty classloader info", StringUtils.isNotEmpty(info));
    }

    /**
     * Test get doublets.
     */
    @Test
    public void testGetDoublets() {
        LOG.info("testGetDoublets() is started.");
        String[] doublets = cpMon.getDoublets();
        LOG.info(doublets.length + " doublets found");
    }

    /**
     * Test get doublet classpath.
     */
    @Test
    public void testGetDoubletClasspath() {
        LOG.info("testGetDoubletClasspath() is started.");
        String[] cp = cpMon.getDoubletClasspath();
        LOG.info("doubletClasspath: " + Converter.toString(cp));
    }

    /**
     * Test get used classpath.
     */
    @Test
    public void testGetUsedClasspath() {
        LOG.info("testGetUsedClasspath() is started.");
        String[] classpath = cpMon.getUsedClasspath();
        checkClasspath(classpath);
        assertEquals(cpMon.getUsedClasspathSet().size(), classpath.length);
    }

    /**
     * During testing on a normal notebook the
     * {@link ClasspathMonitor#getUsedClasspathSet()} needed about 90 ms in
     * average and about 200 ms for the first call on a i5 CPU with
     * 2.60GHz. This is too much. We expect that we can do it in less than 50
     * ms. To avoid a false positive test we accept 80 ms for a call of this
     * method.
     */
    @Test
    public void testGetUsedClasspathSetPerformance() {
        LOG.info("testGetUsedClasspathSetPerformance() is started.");
        long t = 0;
        for (int i = 1; i <= 3; i++) {
            long measured = measureGetUsedClasspathSet();
            LOG.info("{}. call of getUsedClasspathSet() needed {} ms.", i, measured);
            t += measured;
            if (measured < 80) {
                return;
            }
        }
        fail("all calls of getUsedClasspathSet() needed too long (" + t + " ms)");
    }

    private long measureGetUsedClasspathSet() {
        long t0 = System.currentTimeMillis();
        cpMon.getUsedClasspathSet();
        return System.currentTimeMillis() - t0;
    }

    /**
     * Test get unused classpath.
     */
    @Test
    public void testGetUnusedClasspath() {
        LOG.info("testGetUnusedClasspath() is started.");
        String[] unused = cpMon.getUnusedClasspath();
        checkClasspath(unused);
    }

	/**
     * The intersection of used and unused classpath should be empty. This is
     * tested here.
     */
    @Test
    public void testIntersectionOfUsedAndUnusedClasspath() {
        LOG.info("testIntersectionOfUsedAndUnusedClasspath() is started.");
        Collection<File> used = getAsFiles(cpMon.getUsedClasspath());
        Collection<File> unused = getAsFiles(cpMon.getUnusedClasspath());
        for (File file : unused) {
			assertFalse(file + " is not in used classpath expected", used.contains(file));
		}
        LOG.info("No conflict between used ({} entries) and unused ({} entries) found.", used.size(), unused.size());
    }

    private static Collection<File> getAsFiles(final String[] filenames) {
    	Collection<File> files = new ArrayList<>(filenames.length);
    	for (String fname : filenames) {
			files.add(new File(fname).getAbsoluteFile());
		}
    	return files;
	}

    /**
     * This test can fail sometimes because of race conditions which we don't
     * have under control (classloader behaviour).
     */
    @Test
    public void testSizeOfUnusedClasspath() {
        LOG.info("testSizeOfUnusedClasspath() is started.");
        String[] used = cpMon.getUsedClasspath();
        String[] classpath = cpMon.getClasspath();
        String[] unused = cpMon.getUnusedClasspath();
        assertEquals(used.length + " pathes used, " + unused.length + " unused:", classpath.length, unused.length
                + used.length);
    }

    /**
     * Test get boot classpath.
     */
    @Test
    public void testGetBootClasspath() {
        LOG.info("testGetBootClasspath() is started.");
        String[] classpath = cpMon.getBootClasspath();
        checkClasspath(classpath);
        findInClasspath(String.class, classpath);
    }

    /**
     * Test get classpath.
     */
    @Test
    public void testGetClasspath() {
        LOG.info("testGetClasspath() is started.");
        String[] classpath = cpMon.getClasspath();
        checkClasspath(classpath);
        findInClasspath(this.getClass(), classpath);
    }

    private void checkClasspath(final String[] classpath) {
        for (int i = 0; i < classpath.length; i++) {
            LOG.debug(classpath[i]);
            assertTrue("element " + i + " is empty", StringUtils.isNotEmpty(classpath[i]));
            assertFalse(classpath[i].endsWith(File.separator));
        }
    }

    private void findInClasspath(final Class<?> clazz, final String[] classpath) {
        URI classpathURI = cpMon.whichClassPath(clazz);
        File path = Converter.toFile(classpathURI);
        if (isInClasspath(path, classpath)) {
            LOG.info("{} found in {}.", clazz, path);
            return;
        }
        fail(clazz + " not found in " + Converter.toShortString(classpath));
    }

    private static boolean isInClasspath(final File path, final String[] classpath) {
        for (String cp : classpath) {
            File clpath = new ExtendedFile(cp);
            if (clpath.equals(path)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Test which class jar.
     */
    @Test
    public void testWhichClassJar() {
        LOG.info("testWhichClassJar() is started.");
        JarFile jarfile = cpMon.whichClassJar(String.class);
        LOG.info("found: " + jarfile.getName());
        assertNotNull(jarfile);
    }

    /**
     * Test get serial version uid.
     *
     * @throws IllegalAccessException
     *             the illegal access exception
     */
    @Test
    public void testGetSerialVersionUID() throws IllegalAccessException {
        LOG.info("testGetSerialVersionUID() is started.");
        Long uid = cpMon.getSerialVersionUID(String.class.getName());
        LOG.info("String.serialVersionUID=" + uid);
        assertNotNull(uid);
    }

    /**
     * Here we test if we can find the manifest entries for the commons-lang
     * library. These entries should be found in the JAR file.
     */
    @Test
    public void testGetCommonsLangManifestEntries() {
        LOG.info("testGetCommonsLangManifestEntries() is started.");
        checkGetManifestEntries(StringUtils.class);
    }

    /**
     * Here we test if we can find the manifest entries for this test class
     * here. The entries should be found in compiled classes. If not check if
     * Eclipse (or Maven) puts it really in the output folder.
     */
    @Test
    public void testGetTestManifestEntries() {
        LOG.info("testGetTestManifestEntries() is started.");
        checkGetManifestEntries(this.getClass());
    }

    private void checkGetManifestEntries(final Class<?> clazz) {
        String[] entries = cpMon.getManifestEntries(clazz);
        LOG.info("Manifest entries for " + clazz);
        for (String entry : entries) {
            LOG.info(entry);
        }
        assertTrue("no Manifest entries found for " + clazz, entries.length > 0);
    }

    /**
     * Test get incompatible classes.
     *
     * @throws ClassNotFoundException should not happen
     * @throws IOException should not happen
     */
    @Test
    public void testGetIncompatibleClasses() throws ClassNotFoundException, IOException {
        LOG.info("testGetIncompatibleClasses() is started.");
        String[] incompatibleClasses = cpMon.getIncompatibleClasses();
        assertNotNull(incompatibleClasses);
        LOG.info("{} incompatible classes found: {}", incompatibleClasses.length, Converter.toString(incompatibleClasses));
        for (String classname : incompatibleClasses) {
            checkIncompatibleClass(StringUtils.substringAfter(classname, " "));
        }
    }

    private void checkIncompatibleClass(String classname) throws ClassNotFoundException, IOException {
        Class<?> cl = Class.forName(classname);
        assertThat(classname, cpMon.isDoublet(cl), is(true));
        if (cpMon.getNoClasses(cl) == 2) {
            byte[] first = getBytesFor(cl, 0);
            byte[] second = getBytesFor(cl, 1);
            assertThat("2 instances of " + cl + " are equals", Arrays.equals(first, second), is(false));
        }
    }

    private static byte[] getBytesFor(Class<?> cl, int number) throws IOException {
        URI classURI = cpMon.getDoublet(cl, number);
        ArchivEntry entry = new ArchivEntry(classURI);
        return entry.getBytes();
    }

    /**
     * Test get incompatible classpath.
     */
    @Test
    public void testGetIncompatibleClasspath() {
        LOG.info("testGetIncompatibleClasspath() is started.");
        String[] incompatibleClasspath = cpMon.getIncompatibleClasspath();
        assertNotNull(incompatibleClasspath);
        LOG.info(incompatibleClasspath.length + " incompatible classpath entries found");
    }

    /**
     * Test method for {@link ClasspathMonitor#dumpMe(File)}.
     * <p>
     * Note: In 1.5 the behaviour of this method has changed: the given file
     * parameter is no longer a file but a directory with different files which
     * will be created by the {@link ClasspathMonitor#dumpMe(File)} method.
     * </p>
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Override
    @Test
    public void testDumpMe() throws IOException {
        checkDumpMe(cpMon, 17);
    }

    /**
     * Test method for {@link ClasspathMonitor#registerAsMBean(String)}.
     */
    @Test
    public void testRegisterAsMBean() {
        LOG.info("testRegisterAsMBean() is started.");
        ClasspathMonitor.registerAsMBean();
        assertTrue(cpMon + " is not registered as MBean", ClasspathMonitor.isRegisteredAsMBean());
        ClasspathMonitor.unregisterAsMBean();
    }

    /**
     * Test method for {@link ClasspathMonitor#registerAsMBean(String)}.
     */
    @Test
    public void testRegisterAsMBeanString() {
        LOG.info("testRegisterAsMBeanString() is started.");
        ClasspathMonitor.registerAsMBean("test.ClasspathMonitor");
        assertTrue(cpMon + " is not registered as MBean", ClasspathMonitor.isRegisteredAsMBean());
        ClasspathMonitor.unregisterAsMBean();
    }

}
