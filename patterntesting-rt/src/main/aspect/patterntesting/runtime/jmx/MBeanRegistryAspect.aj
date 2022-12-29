/*
 * $Id: MBeanRegistryAspect.aj,v 1.2 2016/12/18 20:19:39 oboehm Exp $
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

import org.slf4j.LoggerFactory;
import org.slf4j.*;

/**
 * The Class MBeanRegistryAspect.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 19.02.2009
 * @version $Revision: 1.2 $
 */
public aspect MBeanRegistryAspect {

    private static final Logger log = LoggerFactory.getLogger(MBeanRegistryAspect.class);

    /**
     * Adds an MBean name to the MBeanRegistry class.
     */
    public String MBeanRegistry.mbeanName;

    /**
     * Adds the registerAsMBean() method to the MBeanRegistry class.
     *
     * @throws JMException the JM exception
     */
    public void MBeanRegistry.registerAsMBean() throws JMException {
        MBeanHelper.registerMBean(this);
        mbeanName = MBeanHelper.getMBeanName(this);
        if (log.isTraceEnabled()) {
        	log.trace(mbeanName + " registered");
        }
    }

    /**
     * Adds the registerAsMBean() method to the MBeanRegistry class.
     *
     * @param name the name
     * @throws JMException the JM exception
     */
    public void MBeanRegistry.registerAsMBean(String name) throws JMException {
        mbeanName = name;
        MBeanHelper.registerMBean(name, this);
    }

    /**
     * Adds the registerAsMBean() method to the MBeanRegistry class.
     *
     * @param name the name
     * @throws JMException the JM exception
     */
    public void MBeanRegistry.registerAsMBean(ObjectName name) throws JMException {
        mbeanName = name.getCanonicalName();
        MBeanHelper.registerMBean(name, this);
    }

    /**
     * Adds the unregisterAsMBean() method to the MBeanRegistry class.
     */
    public void MBeanRegistry.unregisterAsMBean() {
        MBeanHelper.unregisterMBean(mbeanName);
    }

    /**
     * Is the MBeanRegistry registered at JMX?
     *
     * @return true, if successful
     */
    public boolean MBeanRegistry.isRegisteredAsMBean() {
        return MBeanHelper.isRegistered(mbeanName);
    }

    /**
     * Gets the MBean name.
     *
     * @return the string
     */
    public String MBeanRegistry.getMBeanName() {
        return mbeanName;
    }

    /**
     * Gets the object name
     *
     * @return the object name
     *
     * @throws MalformedObjectNameException the malformed object name exception
     */
    public ObjectName MBeanRegistry.getObjectName()
            throws MalformedObjectNameException {
        return new ObjectName(mbeanName);
    }

}

/**
 * $Log: MBeanRegistryAspect.aj,v $
 * Revision 1.2  2016/12/18 20:19:39  oboehm
 * dependency to SLF4J removed
 *
 * Revision 1.1  2011/12/22 16:32:26  oboehm
 * directory structure adapted to preferences of maven-aspectj-plugin
 *
 * Revision 1.2  2011/07/09 21:43:23  oboehm
 * switched from commons-logging to SLF4J
 *
 * Revision 1.1  2010/01/05 13:26:18  oboehm
 * begin with 1.0
 *
 * Revision 1.4  2009/12/20 17:30:13  oboehm
 * trailing spaces removed
 *
 * Revision 1.3  2009/09/25 14:49:44  oboehm
 * javadocs completed with the help of JAutodoc
 *
 * Revision 1.2  2009/02/20 21:45:39  oboehm
 * using MBeanRegistry for registration of ClasspathMonitorMBean
 *
 * Revision 1.1  2009/02/20 21:34:03  oboehm
 * MBeanRegistry with default implementation added
 *
 * $Source: /cvsroot/patterntesting/PatternTesting10/patterntesting-rt/src/main/aspect/patterntesting/runtime/jmx/MBeanRegistryAspect.aj,v $
 */
