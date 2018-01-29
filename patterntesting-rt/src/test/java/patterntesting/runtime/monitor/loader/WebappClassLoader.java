/*
 * $Id: WebappClassLoader.java,v 1.2 2015/12/12 17:42:05 oboehm Exp $
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

package patterntesting.runtime.monitor.loader;

import java.net.URL;

/**
 * This is a simple implementation of {@link ClassLoader} to be able to
 * simulate the Tomcat classloader for testing (till Tomcat 7).
 *
 * @author oliver
 * @since 1.5 (27.08.2014)
 */
public final class WebappClassLoader extends ClassLoader {

    private final URL[] repositoryURLs;

    /**
     * Instantiates a new webapp class loader.
     *
     * @param urls the classpath
     */
    public WebappClassLoader(final URL... urls) {
        this.repositoryURLs = urls;
    }

    /**
     * Gets the classpath.
     *
     * @return the classpath
     */
    public URL[] getClasspath() {
        return this.repositoryURLs;
    }

}

