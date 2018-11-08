/*
 * $Id: ResourcepathMonitor.java,v 1.39 2017/08/19 14:55:29 oboehm Exp $
 *
 * Copyright (c) 2016 by Oliver Boehm
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
 * (c)reated 24.04.16 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.monitor;

import java.net.URI;

/**
 * Analogous to ClasspathMonitor this class allows you to ask for resources in
 * the classpath.
 *
 * @author <a href="ob@aosd.de">oliver</a>
 * @version $Revision: 1.39 $
 * @since 1.6.4
 */
public class ResourcepathMonitor extends clazzfish.monitor.ResourcepathMonitor {

    private static final ResourcepathMonitor INSTANCE;

	static {
		INSTANCE = new ResourcepathMonitor();
	}

	/**
	 * We offer only some services. So there is no need to instantiate it from
	 * outside.
	 */
	private ResourcepathMonitor() {
		super();
	}

	/**
	 * Yes, it is a Singleton because it offers only some services. So we don't
	 * need the object twice.
	 *
	 * @return the only instance
	 */
	public static ResourcepathMonitor getInstance() {
		return INSTANCE;
	}

    /**
     * With this method you can register the {@link ResourcepathMonitor} with the
     * default name.
     * <p>
     * You can only register the {@link ResourcepathMonitor} once only. If you want
     * to register it with another name you have to first unregister it.
     * </p>
     *
     * @since 1.7.2
     */
    public static void registerAsMBean() {
        getInstance().registerMeAsMBean();
    }

    /**
     * Unregister ResourcepathMonitor as MBean.
     */
    public static void unregisterAsMBean() {
        getInstance().unregisterMeAsMBean();
    }

    /**
     * If you want to ask JMX if bean is already registered you can use this
     * method.
     *
     * @return true if class is registered as MBean
     */
    public static boolean isRegisteredAsMBean() {
        return getInstance().isMBean();
    }

    /**
     * Gets the classpath which contains incompatible resources. Resources in
     * the META-INF folder are filtered out because resources in this folder
     * contains often only some meta info of the JAR.
     *
     * @return the resourcepath as array of URIs
     */
    @Override
	public URI[] getIncompatibleResourcepathURIs() {
		return super.getIncompatibleResourcepathURIs();
	}

}
