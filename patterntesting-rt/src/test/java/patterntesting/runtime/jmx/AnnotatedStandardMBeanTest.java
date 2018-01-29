/*
 * $Id: AnnotatedStandardMBeanTest.java,v 1.4 2016/12/18 20:19:41 oboehm Exp $
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
 * (c)reated 30.03.2016 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.jmx;

import static org.junit.Assert.assertEquals;

import javax.management.*;

import org.junit.Before;
import org.junit.Test;

import patterntesting.runtime.monitor.ClasspathMonitor;
import patterntesting.runtime.monitor.ClasspathMonitorMBean;

/**
 * Unit tests for {@link AnnotatedStandardMBean} class.
 *
 * @author oliver
 * @since 1.6 (30.03.2016)
 */
public final class AnnotatedStandardMBeanTest {

    private AnnotatedStandardMBean mbean;

    /**
     * Sets the up m bean.
     *
     * @throws NotCompliantMBeanException the not compliant m bean exception
     */
    @Before
    public void setUpMBean() throws NotCompliantMBeanException {
        mbean = new AnnotatedStandardMBean(ClasspathMonitor.getInstance(),
                ClasspathMonitorMBean.class);
    }

    /**
     * Test method for
     * {@link AnnotatedStandardMBean#getDescription(MBeanOperationInfo)}.
     *
     * @throws NoSuchMethodException the no such method exception
     * @throws SecurityException the security exception
     */
    @Test
    public void testGetDescriptionOperation() throws NoSuchMethodException, SecurityException {
        MBeanOperationInfo info = new MBeanOperationInfo("logMe",
                ClasspathMonitor.class.getMethod("logMe"));
        String description = mbean.getDescription(info);
        assertEquals("logs all attributes to the log output", description);
    }

    /**
     * Test method for
     * {@link AnnotatedStandardMBean#getDescription(MBeanAttributeInfo)}.
     *
     * @throws NoSuchMethodException the no such method exception
     * @throws SecurityException the security exception
     * @throws IntrospectionException the introspection exception
     */
    @Test
    public void testGetDescriptionAttribute() throws NoSuchMethodException, SecurityException, IntrospectionException {
        MBeanAttributeInfo info = new MBeanAttributeInfo("multiThreadingEnabled", "experimental",
                ClasspathMonitor.class.getMethod("isMultiThreadingEnabled"),
                ClasspathMonitor.class.getMethod("setMultiThreadingEnabled", boolean.class));
        String description = mbean.getDescription(info);
        assertEquals("experimental", description);
    }

}

