/*
 * Copyright (c) 2008-2019 by Oliver Boehm
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
 * (c)reated 22.10.2008 by oliver (ob@oasd.de)
 */
package patterntesting.sample;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.jupiter.api.Test;
import patterntesting.annotation.check.ct.SuppressJUnitWarning;
import patterntesting.runtime.annotation.Broken;
import patterntesting.runtime.util.Assertions;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static patterntesting.runtime.NullConstants.NULL_OBJECT;
import static patterntesting.runtime.NullConstants.NULL_STRING;

/**
 * The Class ConfigTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.8 $
 * @since 22.10.2008
 */
public class ConfigTest {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigTest.class);

    /**
     * Test method for {@link Config#getVersion()}.
     */
    @Test
    @Broken("test must be called with -Dpatterntesting.sample.version=...")
    public final void testGetVersion() {
        String version = Config.getVersion();
        assertNotNull(version);
    }

    /**
     * This is an example how to use NullConstants.NULL_OBJECT and
     * NULL_STRING as arguments for Config.setVersion(..).
     *
     * @author oliver
     * @since 12-Jun-2009
     */
    @Test
    public final void testSetVersion() {
        Config.setVersion(NULL_OBJECT, "0.9.7", NULL_STRING);
        assertEquals("0.9.7", Config.getVersion());
    }

    /**
     * We want to see the AssertError in the log so we catch the
     * AssertionError in this method. Normally you shouldn't do that.
     */
    @Test
    @SuppressJUnitWarning
    public final void testGetNullVersion() {
        String resource = Config.getResource();
        try {
            Config.setResource("invalidresource");
            Config.getVersion();
        } catch (AssertionError expected) {
            LOG.info("expected AssertionError received", expected);
        } finally {
            Config.setResource(resource);
        }
    }

    /**
     * This test will fail. But you see the NullPointerException in
     * Config.setResource() and not Config.getVersion() when this method
     * tries to access the resource name.
     * <p>
     * Ok, you see not a NullPointerException but an AssertionError, but you
     * see the initiator of the error.
     * </p>
     */
    //@Test
    public final void testSetNullArg() {
        Config.setResource(null);
        Config.getVersion();
    }

    /**
     * We want to see the AssertError in the log so we catch the
     * AssertionError in this method. Normally you shouldn't do that.
     */
    @SuppressJUnitWarning
    @Test
    public final void testSetNullArg2() {
        Config.setProperty("test", "arg2-test");
        Config.setProperty("test", null);
        try {
            Config.setProperty(null, "test-value");
            throw new RuntimeException("AssertionError expected");
        } catch (AssertionError expected) {
            LOG.info("expected AssertionError received", expected);
        }
    }

    /**
     * Test get os.
     */
    @Test
    public final void testGetOS() {
        String os = Config.getOS();
        LOG.info("OS: " + os);
        assertNotNull(os);
    }

    /**
     * If the environment variable JDK_HOME is not set this test will fail
     * with an AssertionError in Config.getJdkHome().
     */
    //@Test
    public final void testJdkHomeDir() {
        File jdkDir = Config.getJdkHomeDir();
        LOG.info("JDK home: " + jdkDir);
        assertTrue(jdkDir.exists(), jdkDir + " doesn't exist");
    }

    /**
     * Config.getProperty() can return a null value. And this is tested here.
     */
    @Test
    @Broken("intentionally broken if run with '-ea'")
    public final void testGetProperty() {
        String property = Config.getProperty("notexist");
        assertNull(property);
    }

    /**
     * Test assert enabled.
     */
    @Test
    public final void testAssertEnabled() {
        assertTrue(Assertions.areEnabled(), "enable assertion (java -ea ...)");
    }

    /**
     * It is not allowed to set a null value. So we expect an AssertionError
     * here.
     */
    @Test
    public final void testSetProperty() {
        assertThrows(AssertionError.class, () -> {Config.setProperty(null, null);});
    }

}
