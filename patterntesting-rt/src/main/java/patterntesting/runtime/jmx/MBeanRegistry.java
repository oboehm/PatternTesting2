/**
 * $Id: MBeanRegistry.java,v 1.6 2016/12/10 20:55:22 oboehm Exp $
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
 * (c)reated 19.02.2009 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.jmx;

import javax.management.*;

/**
 * To avoid confusion with the existing MBeanRegistration interface from
 * javax.management this interface is now called MBeanRegistry (although there
 * are too many negative associations with the word "registry").
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.6 $
 * @since 19.02.2009
 */
public interface MBeanRegistry {

	/**
	 * Registers the MBean as "a.b.c:type=Xxx" (where "a.b.c" is the package
	 * name and "Xxx" the classname).
	 *
	 * @throws JMException
	 *             the JMX exception
	 */
	public void registerAsMBean() throws JMException;

	/**
	 * Register the MBean with the given name.
	 *
	 * @param name
	 *            e.g. "a.b.c:type=Xxx"
	 * @throws JMException
	 *             the JMX exception
	 */
	public void registerAsMBean(String name) throws JMException;

	/**
	 * Register the MBean with the given object name.
	 *
	 * @param name
	 *            e.g. new ObjectName("a.b.c:type=Xxx");
	 * @throws JMException
	 *             the JMX exception
	 */
	public void registerAsMBean(ObjectName name) throws JMException;

	/**
	 * Unregisters the MBean from the MBean server.
	 */
	public void unregisterAsMBean();

	/**
	 * Was the object registered as MBean?.
	 *
	 * @return true if object was registered.
	 */
	public boolean isRegisteredAsMBean();

	/**
	 * Returns the MBean name under which this class was registered.
	 *
	 * @return e.g. "a.b.c:type=Xxx"
	 */
	public String getMBeanName();

	/**
	 * Returns the MBean name under which this class was registered.
	 *
	 * @return e.g. "a.b.c:type=Xxx" as ObjectName
	 * @throws MalformedObjectNameException
	 *             the malformed object name exception
	 */
	public ObjectName getObjectName() throws MalformedObjectNameException;

}
