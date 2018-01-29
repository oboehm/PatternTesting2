/*
 * $Id: MBeanRegistryTest.java,v 1.8 2016/12/18 20:19:41 oboehm Exp $
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

import static org.junit.Assert.*;

import javax.management.JMException;
import javax.management.ObjectName;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.Test;

import patterntesting.runtime.jmx.demo.Jim;

/**
 * The Class MBeanRegistryTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.8 $
 * @since 19.02.2009
 */
public final class MBeanRegistryTest {

    private static final Logger log = LogManager.getLogger(MBeanRegistryTest.class);
    private final Jim jim = new Jim();

    /**
     * Test manual registration.
     *
     * @throws JMException the JM exception
     */
    @Test
    public void testManualRegistration() throws JMException {
        Jim bean = new Jim("Jonny");
        MBeanHelper.registerMBean("test:type=Jonny", bean);
        assertTrue(MBeanHelper.isRegistered("test:type=Jonny"));
        MBeanHelper.unregisterMBean("test:type=Jonny");
    }

    /**
     * Test default registration.
     *
     * @throws JMException the JM exception
     */
    @Test
    public void testDefaultRegistration() throws JMException {
        MBeanHelper.registerMBean(jim);
        String name = MBeanHelper.getMBeanName(jim);
        log.info("MBean name for jim: " + name);
        assertTrue(MBeanHelper.isRegistered(name));
        MBeanHelper.unregisterMBean(name);
    }

    /**
     * Test register as m bean.
     *
     * @throws JMException the JM exception
     */
    @Test
    public void testRegisterAsMBean() throws JMException {
        jim.registerAsMBean();
        checkMBeanRegistration("patterntesting.runtime:type=jmx,jmx=demo,name=Jim");
    }

    /**
     * Test register as m bean named.
     *
     * @throws JMException the JM exception
     */
    @Test
    public void testRegisterAsMBeanNamed() throws JMException {
        String mbeanName = "test:type=Jim.Bean";
        jim.registerAsMBean(mbeanName);
        checkMBeanRegistration(mbeanName);
    }

    private void checkMBeanRegistration(final String mbeanName) throws JMException {
        assertEquals(jim.getObjectName(), new ObjectName(mbeanName));
        assertTrue(MBeanHelper.isRegistered(mbeanName));
        assertTrue(jim.isRegisteredAsMBean());
        unregister(jim);
    }

    private static void unregister(final Jim bean) {
        bean.unregisterAsMBean();
        assertFalse(bean.isRegisteredAsMBean());
    }

    /**
     * Test register two MBeans.
     *
     * @throws JMException the JM exception
     */
    @Test
    public void testRegisterSeveralMBeans() throws JMException {
        jim.registerAsMBean();
        Jim jonny = new Jim("Jonny");
        jonny.registerAsMBean("test:type=Jonny.Walker");
        Jim jerry = new Jim("Tom");
        jerry.registerAsMBean(MBeanHelper.getMBeanName(jerry, 3));
        checkRegistration(jim);
        checkRegistration(jonny);
        checkRegistration(jerry);
    }

    private static void checkRegistration(final Jim jones) {
        assertTrue(jones.isRegisteredAsMBean());
        unregister(jones);
    }

}
