/*
 * Copyright (c) 2015 by Oli B.
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
 * (c)reated 19.12.2015 by Oli B. (ob@aosd.de)
 */

package patterntesting.runtime.monitor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.*;
import org.junit.Test;

/**
 * Unit tests for {@link ProfileMonitorFactory}.
 *
 * @author oboehm (ob@aosd.de)
 * @since 1.6 (19.12.2015)
 */
public abstract class ProfileMonitorFactoryTest {

	private static final Logger LOG = LogManager.getLogger(ProfileMonitorFactoryTest.class);
	private final ProfileMonitorFactory factory = this.createProfileMonitorFactory();

	/**
	 * Gets the profile monitor factory.
	 *
	 * @return the profile monitor factory
	 */
	protected abstract ProfileMonitorFactory createProfileMonitorFactory();

	/**
	 * Gets the factory.
	 *
	 * @return the factory
	 */
	protected ProfileMonitorFactory getFactory() {
		return this.factory;
	}

	/**
	 * Test method for {@link ProfileMonitorFactory#setMaxNumMonitors(int)}.
	 */
	@Test
	public void testSetMaxNumMonitors() {
		factory.setMaxNumMonitors(10);
		assertEquals(10, factory.getMaxNumMonitors());
	}

	/**
	 * Test method for {@link ProfileMonitorFactory#getMonitors()}.
	 */
	@Test
	public void testGetMonitors() {
        ProfileMonitor[] monitors = factory.getMonitors();
        for (int i = 0; i < monitors.length; i++) {
			LOG.info("monitors[{}] = {}", i, monitors[i]);
		}
	}

	/**
	 * Test method for {@link ProfileMonitorFactory#addMonitors(List)}.
	 */
	@Test
	public void testAddMonitors() {
		this.addMethodsOf(ProfileMonitorFactory.class);
		List<String> labels = new ArrayList<>();
		labels.add(Dummy.class.getName() + ".hallo()");
		int newSize = factory.getMonitors().length;
		factory.setMaxNumMonitors(newSize);
		factory.addMonitors(labels);
		assertEquals(newSize, factory.getMonitors().length);
	}

	/**
	 * Adds the methods of the given class. But only methods without no
	 * arguments.
	 *
	 * @param clazz the clazz
	 */
	protected void addMethodsOf(final Class<?> clazz) {
		Method[] methods = clazz.getMethods();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getParameterTypes().length > 0) {
				continue;
			}
			String label = clazz.getName() + "." + methods[i].getName() + "()";
			factory.addMonitor(label);
			ProfileMonitor monitor = factory.getMonitor(label);
			monitor.start();
			assertEquals(label, monitor.getLabel());
			monitor.stop();
			LOG.info("Monitor {}: {}", i, monitor);
		}
	}

	/**
	 * We do not want to see the default implementation here.
	 */
	@Test
	public void testToString() {
		String s = factory.toString();
		assertFalse("looks like default implementation: " + s, s.contains("@"));
		LOG.info("s = \"{}\"", s);
	}

}
