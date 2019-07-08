/*
 * $Id: Config.java,v 1.6 2016/12/30 21:52:26 oboehm Exp $
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
 * (c)reated 22.10.2008 by oliver (ob@oasd.de)
 */
package patterntesting.sample;

import java.io.*;
import java.util.Properties;

import javax.validation.constraints.NotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import patterntesting.annotation.check.runtime.*;
import patterntesting.runtime.NullConstants;

/**
 * This class is an example how NullPointerExceptions may happen.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.6 $
 * @since 22.10.2008
 */
public class Config {

    private static final Log log = LogFactoryImpl.getLog(Config.class);
    private static String propertyResource = "default.properties";
    private static Properties properties = null;

    /** Utility class - no need to instantiate it. */
    private Config() {}

    /**
     * Sets the resource.
     *
     * @param name the new resource
     */
    public static void setResource(final String name) {
        propertyResource = name;
        properties = null;
    }

    /**
     * Gets the resource.
     *
     * @return the resource
     */
    public static String getResource() {
        return propertyResource;
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    public static String getVersion() {
        return getProperty("patterntesting.sample.version");
    }

    /**
     * This is an example how to use NULL_OBJECT as argument. If you don't want to
     * set a prefix or suffix use NullConstants.NULL_OBJECT as argument.
     * <p>
     * BTW: The code would also work if you would use NullConstants.NULL_STRING as
     * arguemnt (because it is an empty string).
     * </p>
     *
     * @author oliver
     * @param prefix e.g. "v" or NullConstants.NULL_OBJECT if you don't want to set a
     * prefix
     * @param version e.g. "0.9.7"
     * @param suffix e.g. "-SNAPSHOT" or NullConstants.NULL_OBJECT if you don't want to set
     * a suffix
     * @since 13-Jun-2009
     */
    public static void setVersion(final Object prefix, String version,
            final Object suffix) {
        if (prefix != NullConstants.NULL_OBJECT) {
            version = prefix.toString() + version;
        }
        if (suffix != NullConstants.NULL_OBJECT) {
            version = version + suffix.toString();
        }
        setProperty("patterntesting.sample.version", version);
    }

    /**
     * This is an example for a method which may return null.
     *
     * @param name of the key
     *
     * @return value of the property
     */
    @MayReturnNull
    public static String getProperty(final String name) {
        Properties props = getProperties();
        return props.getProperty(name);
    }

    /**
     * This is a (bad) example how you can use @NullArgsAllowed and combine it
     * with @NotNull.
     * <p>
     * Don't use such kind of code in your project. A better approach would be
     * to divide this methods into:
     * </p>
     * <ul>
     * <li>setProperty(String, String)</li>
     * <li>resetProperty(String)
     * </ul>
     *
     * @param key the key
     * @param value the value
     */
    @NullArgsAllowed
    public static void setProperty(@NotNull final String key, final String value) {
        Properties properties = getProperties();
        if (value == null) {
            properties.remove(key);
        } else {
            properties.setProperty(key, value);
        }
    }

    /**
     * Gets the properties.
     *
     * @return the properties
     */
    public static synchronized Properties getProperties() {
        if (properties != null) {
            return properties;
        }
        properties = new Properties();
        try {
            properties.load(getPropertyStream());
            log.info(propertyResource + " loaded");
        } catch (IOException ioe) {
            log.warn("can't load properties", ioe);
        }
        return properties;
    }

    /**
     * Gets the property stream.
     *
     * @return the property stream
     */
    public static InputStream getPropertyStream() {
        return Config.class.getResourceAsStream(propertyResource);
    }

    /**
     * Gets the OS.
     *
     * @return the OS
     */
    public static String getOS() {
        return System.getProperty("os.name");
    }

    /**
     * Gets the JDK home.
     *
     * @return the JDK home
     */
    public static String getJdkHome() {
        return System.getenv("JDK_HOME");
    }

    /**
     * Gets the JDK home dir.
     *
     * @return the JDK home dir
     */
    public static File getJdkHomeDir() {
        String jdkHome = getJdkHome();
        assert jdkHome != null;
        return new File(jdkHome);
    }

}
