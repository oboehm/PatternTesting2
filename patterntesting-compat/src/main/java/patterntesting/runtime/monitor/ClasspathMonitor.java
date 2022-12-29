/**
 * $Id: ClasspathMonitor.java,v 1.114 2017/11/09 20:34:50 oboehm Exp $
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

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import patterntesting.runtime.jmx.MBeanHelper;

import javax.management.ObjectName;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;

/**
 * To avoid classpath problems like double entries of the same class or resource
 * in the classpath there are several methods available.
 * <p>
 * To get the boot classpath the system property "sun.boot.class.path" is used
 * to get them. This will work of course only for the SunVM.
 * </p>
 * <p>
 * After an idea from
 * <a href="http://www.javaworld.com/javaworld/javatips/jw-javatip105.html">Java
 * Tip 105</a>.
 * </p>
 * <p>
 * With v1.5 the startup time and performance was increased. To speed up time a
 * {@link FutureTask} is used to set up internal structurs. For a better
 * performance doublets are now detected with parallel threads. Also some
 * expensive operations are cached now.
 * </p>
 * <p>
 * Because some stuff are done in parallel we no longer use a WeakHashMap but
 * {@link ConcurrentHashMap} to avoid synchronization problems. It is expected
 * that the size of cached objects is small enough and we will not run in any
 * OutOfMemory problems.
 * </p>
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 10.02.2009
 * @deprecated replaced by clazzfish.monitor.{@link clazzfish.monitor.ClasspathMonitor}
 */
@Deprecated
public class ClasspathMonitor extends clazzfish.monitor.ClasspathMonitor {

	private static final Logger LOG = LoggerFactory.getLogger(ClasspathMonitor.class);
	private static final ClasspathMonitor INSTANCE;

	static {
		INSTANCE = new ClasspathMonitor();
	}

	/**
	 * Instantiates a new classpath monitor.
	 * <p>
	 * Note: The constructor is protected because it is needed by the benchmark
	 * subproject which compares different implementations.
	 * </p>
	 */
	protected ClasspathMonitor() {
		super();
	}

	/**
	 * Yes, it is a Singleton because it offers only some services. So we don't
	 * need the object twice.
	 *
	 * @return the only instance
	 */
	public static ClasspathMonitor getInstance() {
		return INSTANCE;
	}

	/**
	 * With this method you can register the {@link ClasspathMonitor} with the
	 * default name.
	 * <p>
	 * You can only register the {@link ClasspathMonitor} once only. If you want
	 * to register it with another name you have to first unregister it.
	 * </p>
	 *
	 * @see ClasspathMonitor#registerAsMBean(String)
	 * @see ClasspathMonitor#unregisterAsMBean()
	 */
	public static void registerAsMBean() {
	    getInstance().registerMeAsMBean();
	}

	/**
	 * With this method you can register the {@link ClasspathMonitor} with your
	 * own name. This is e.g. useful if you have an application server with
	 * several applications.
	 * <p>
	 * You can register the {@link ClasspathMonitor} once only. If you want
	 * to register it with another name you have to first unregister it.
	 * </p>
	 *
	 * @param name e.g "my.company.ClasspathMonitor"
	 * @see ClasspathMonitor#unregisterAsMBean()
	 * @since 1.6
	 */
	public static void registerAsMBean(final String name) {
		registerAsMBean(MBeanHelper.getAsObjectName(name));
	}

	/**
	 * With this method you can register the {@link ClasspathMonitor} with your
	 * own name. This is e.g. useful if you have an application server with
	 * several applications.
	 * <p>
	 * You can only register the {@link ClasspathMonitor} once only. If you want
	 * to register it with another name you have to first unregister it.
	 * </p>
	 *
	 * @param name
	 *            the name
	 * @see ClasspathMonitor#unregisterAsMBean()
	 * @since 1.6
	 */
	public static synchronized void registerAsMBean(final ObjectName name) {
		if (isRegisteredAsMBean()) {
			LOG.debug("MBean already registered - registerAsMBean(\"{}\") ignored.", name);
		} else {
		    getInstance().registerMeAsMBean(name);
		}
	}

	/**
	 * Unregister ClasspathMonitor as MBean.
	 */
	public static void unregisterAsMBean() {
	    getInstance().unregisterMeAsMBean();
	}

	/**
	 * If you want to ask JMX if bean is already registered you can ask the
	 * MBeanHelper (if you know the object name) or you can ask this method.
	 *
	 * @return true if MBean is already registered.
	 * @since 1.0
	 */
	public static boolean isRegisteredAsMBean() {
	    return getInstance().isMBean();
	}

}
