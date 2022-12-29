/*
 * $Id: ClassloaderTypeTest.java,v 1.12 2017/07/18 14:31:46 oboehm Exp $
 *
 * Copyright (c) 2014 by Oliver Boehm
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
 * (c)reated 26.08.2014 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.monitor;

import org.apache.catalina.loader.ParallelWebappClassLoader;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import patterntesting.runtime.monitor.loader.CompoundClassLoader;
import patterntesting.runtime.monitor.loader.WebappClassLoader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link ClassloaderType}.
 *
 * @author oliver
 * @since 1.5 (26.08.2014)
 */
public class ClassloaderTypeTest {

    private static final Logger log = LoggerFactory.getLogger(ClassloaderTypeTest.class);

    /**
     * Test method for {@link ClassloaderType#toClassloaderType(ClassLoader)}.
     */
    @Test
    public void testToClassloaderType() {
        checkToClassloaderType(getCurrentClassLoader(),
                anyOf(equalTo(ClassloaderType.SUN), equalTo(ClassloaderType.JDK), equalTo(ClassloaderType.NET), equalTo(ClassloaderType.SUREFIRE)));
    }

    /**
     * Test method for {@link ClassloaderType#toClassloaderType(ClassLoader)}.
     */
    @Test
    public void testWebsphereClassloaderType() {
        checkToClassloaderType(new CompoundClassLoader(), ClassloaderType.WEBSPHERE);
    }

    /**
     * Test method for {@link ClassloaderType#toClassloaderType(ClassLoader)}.
     */
    @Test
    public void testTomcat8ClassloaderType() {
        checkToClassloaderType(new ParallelWebappClassLoader(), ClassloaderType.TOMCAT8);
    }

    /**
     * Test method for {@link ClassloaderType#toClassloaderType(ClassLoader)}.
     */
    @Test
    public void testNetClassloaderType() {
        checkToClassloaderType(new URLClassLoader(new URL[0]), ClassloaderType.NET);
    }

    private static void checkToClassloaderType(final ClassLoader cloader,
            final ClassloaderType expected) {
        checkToClassloaderType(cloader, equalTo(expected));
    }

    private static void checkToClassloaderType(ClassLoader cloader,
            Matcher<ClassloaderType> matcher) {
        ClassloaderType type = ClassloaderType.toClassloaderType(cloader);
        log.info("Type of {} is {}.", cloader, type);
        assertThat(type, matcher);
    }

    /**
     * Test method for {@link ClassloaderType#isSupported(String)}.
     */
    @Test
    public void testIsSupportedDefaultClassloader() {
        checkIsSupported(getCurrentClassLoader().getClass().getName(), true);
    }

    /**
     * Test method for {@link ClassloaderType#isSupported(String)}.
     */
    @Test
    public void testIsSupportedWebsphere() {
        checkIsSupported("com.ibm.ws.classloader.CompoundClassLoader", true);
    }

    /**
     * Test method for {@link ClassloaderType#isSupported(String)}.
     */
    @Test
    public void testIsUnsupported() {
        checkIsSupported("non.existing.ClassLoader", false);
    }

    private static void checkIsSupported(final String classname, final boolean expected) {
        assertEquals(ClassloaderType.isSupported(classname), expected, classname);
    }

    /**
     * Test get classpath from tomcat.
     *
     * @throws MalformedURLException the malformed url exception
     */
    @Test
    public void testGetClasspathFromTomcat() throws MalformedURLException {
        URL[] expected = { new URL("file:/tmp/a") };
        ClassLoader tomcat = new WebappClassLoader(expected);
        URL[] classpath = (URL[]) ClassloaderType.TOMCAT.getClasspathFrom(tomcat);
        assertEquals(expected.length, classpath.length);
    }
    
    /**
     * Till Tomcat 7 there was a private field 'repositoryURLs' which was used
     * to get the classpath. But this field is missing in Tomcat 8.
     */
    @Test
    public void testGetClasspathFromTomcat8() {
        ClassLoader tomcat8 = new org.apache.catalina.loader.WebappClassLoader();
        ClassloaderType type = ClassloaderType.toClassloaderType(tomcat8);
        type.getClasspathFrom(tomcat8);
    }
    
    /**
     * Test method for {@link ClassloaderType#getCurrentClassloaderType()}.
     */
    @Test
    public void testGetCurrentClassloaderType() {
        ClassloaderType clType = ClassloaderType.getCurrentClassloaderType();
        assertThat(clType, anyOf(is(ClassloaderType.SUN), is(ClassloaderType.JDK), is(ClassloaderType.NET), is(ClassloaderType.SUREFIRE)));
    }

    private static ClassLoader getCurrentClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

}
