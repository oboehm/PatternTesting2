/*
 * $Id: ProfileStatistic.java,v 1.40 2017/06/01 17:24:29 oboehm Exp $
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
 * (c)reated 22.12.2008 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.monitor;

import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import javax.management.ObjectName;
import javax.management.openmbean.*;

import org.apache.logging.log4j.*;
import org.aspectj.lang.Signature;

import patterntesting.annotation.check.runtime.MayReturnNull;
import patterntesting.runtime.annotation.DontProfileMe;
import patterntesting.runtime.jmx.MBeanHelper;
import patterntesting.runtime.util.*;

/**
 * This is constructed as a thin layer around com.jamonapi.MonitorFactory for
 * the needs of patterntesting. The reason for this layer is that sometimes you
 * want to minimize the use of other libraries. So this implementation provides
 * also an implementation if the JaMon library is missing.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.40 $
 * @see com.jamonapi.MonitorFactory
 * @since 22.12.2008
 */
public class ProfileStatistic extends Thread implements ProfileStatisticMBean {

	private static final ProfileStatistic INSTANCE;
	private static final Logger LOG = LogManager.getLogger(ProfileStatistic.class);

	private ObjectName mbeanName = MBeanHelper.getAsObjectName(this.getClass());
	private final ProfileMonitorFactory factory;

	/** Is JaMon library available?. */
	private static final boolean JAMON_AVAILABLE;

	/**
	 * ProfileStatistic *must* be initialized after isJamonAvailable attribute
	 * is set. Otherwise you'll get a NullPointerException after MBean
	 * registration.
	 */
	static {
		JAMON_AVAILABLE = Environment.isJamonAvailable();
		INSTANCE = new ProfileStatistic("root");
	}

	/**
	 * Gets the single instance of ProfileStatistic.
	 *
	 * @return single instance of ProfileStatistic
	 */
	public static ProfileStatistic getInstance() {
		return INSTANCE;
	}

	/**
	 * Instantiates a new profile statistic.
	 *
	 * @param rootLabel
	 *            the root label
	 */
	protected ProfileStatistic(final String rootLabel) {
		SimpleProfileMonitor rootMonitor = new SimpleProfileMonitor(rootLabel);
		MBeanHelper.registerMBean(this.mbeanName, this);
		factory = JAMON_AVAILABLE ? new JamonMonitorFactory(rootMonitor) : new SimpleProfileMonitorFactory(rootMonitor);
		factory.setMaxNumMonitors(100);
	}

	/**
	 * Normally the {@link ProfileMonitor} is already registered as MBean. So
	 * you don't need to register it manually. But if you want to register it
	 * under your own name you can use this method here. This can be useful if
	 * your application runs on an application server with several other active
	 * applications.
	 *
	 * @param name
	 *            e.g "my.company.ProfileMonitor"
	 * @since 1.6
	 */
	public static void registerAsMBean(final String name) {
		INSTANCE.registerMeAsMBean(name);
	}

	/**
	 * Register me as MBean.
	 *
	 * @param name
	 *            the name
	 */
	protected void registerMeAsMBean(final String name) {
		this.registerMeAsMBean(MBeanHelper.getAsObjectName(name));
	}

	private void registerMeAsMBean(final ObjectName name) {
		MBeanHelper.unregisterMBean(this.mbeanName);
		MBeanHelper.registerMBean(name, this);
		LOG.info("{} no longer registered as MBean '{}' but as MBean '{}'.", this.getClass().getSimpleName(),
				this.mbeanName, name);
		this.mbeanName = name;
	}

	/**
	 * You can register the instance as shutdown hook. If the VM is terminated
	 * the profile values are logged and dumped to a CSV file in the tmp
	 * directory.
	 *
	 * @see #logStatistic()
	 * @see #dumpStatistic()
	 */
	public static void addAsShutdownHook() {
		addAsShutdownHook(INSTANCE);
	}

	/**
	 * Adds the given instance (hook) as shutdown hook.
	 *
	 * @param hook
	 *            the hook
	 */
	protected static void addAsShutdownHook(final ProfileStatistic hook) {
		Runtime.getRuntime().addShutdownHook(hook);
		if (LOG.isDebugEnabled()) {
			LOG.debug(hook + " registered as shutdown hook");
		}
	}

	/**
	 * We can't reset all ProfileMonitors - we must keep the empty monitors with
	 * 0 hits to see which methods or constructors are never called.
	 */
	@Override
	public void reset() {
		synchronized (ProfileStatistic.class) {
			List<String> labels = new ArrayList<>();
			ProfileMonitor[] monitors = getMonitors();
			for (int i = 0; i < monitors.length; i++) {
				if (monitors[i].getHits() == 0) {
					labels.add(monitors[i].getLabel());
				}
			}
			this.factory.addMonitors(labels);
		}
	}

	/**
	 * Resets the root monitor.
	 */
	protected void resetRootMonitor() {
		this.factory.reset();
	}

	/**
	 * For each constructor and for each method of the given class a
	 * ProfileMonitor is initialized. This is done to be able to find
	 * constructors and methods which are are never used (i.e. their hit count
	 * is zero).
	 *
	 * @param cl
	 *            the given class
	 */
	public void init(final Class<?> cl) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("initializing monitors for " + cl + "...");
		}
		INSTANCE.init(cl, cl.getMethods());
		INSTANCE.init(cl.getConstructors());
	}

	/**
	 * Only methods of the given class but not the methods of the superclass are
	 * initialized for profiling.
	 *
	 * @param cl
	 * @param methods
	 */
	private void init(final Class<?> cl, final Method[] methods) {
		for (int i = 0; i < methods.length; i++) {
			Class<?> declaring = methods[i].getDeclaringClass();
			if (cl.equals(declaring)) {
				init(methods[i]);
			} else if (LOG.isTraceEnabled()) {
				LOG.trace(methods[i] + " not defined in " + cl + " -> no monitor initialized");
			}
		}
	}

	private void init(final Method method) {
		if (method.getAnnotation(DontProfileMe.class) != null) {
			if (LOG.isTraceEnabled()) {
				LOG.trace("@DontProfileMe " + method + " is ignored");
			}
			return;
		}
		Signature sig = SignatureHelper.getAsSignature(method);
		ProfileMonitor mon = getMonitor(sig);
		if (LOG.isTraceEnabled()) {
			LOG.trace(mon + " initialized");
		}
	}

	private void init(final Constructor<?>[] ctors) {
		for (int i = 0; i < ctors.length; i++) {
			init(ctors[i]);
		}
	}

	private void init(final Constructor<?> ctor) {
		if (ctor.getAnnotation(DontProfileMe.class) != null) {
			LOG.trace("@DontProfileMe {} is ignored", ctor);
			return;
		}
		Signature sig = SignatureHelper.getAsSignature(ctor);
		ProfileMonitor mon = getMonitor(sig);
		LOG.trace("{} initialized", mon);
	}

	/**
	 * Gets the MBean name of the registered {@link ProfileStatistic} bean.
	 *
	 * @return the MBean name
	 */
	public ObjectName getMBeanName() {
		return this.mbeanName;
	}

	/**
	 * Here you can set the maximal size of the statistic entries.
	 *
	 * @param size
	 *            the new max size
	 * @since 1.6
	 */
	@Override
	public void setMaxSize(final int size) {
		factory.setMaxNumMonitors(size);
	}

	/**
	 * Gets the max size.
	 *
	 * @return the max size
	 * @since 1.6
	 */
	@Override
	public int getMaxSize() {
		return factory.getMaxNumMonitors();
	}

	///// business logic (measurement, statistics and more) ///////////////

	/**
	 * This method is called when the PerformanceMonitor is registered as
	 * shutdown hook.
	 *
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			dumpStatistic();
		} catch (IOException ioe) {
			LOG.warn("Cannot dump statistic to temporary file:", ioe);
		}
	}

	/**
	 * Start.
	 *
	 * @param sig
	 *            the sig
	 *
	 * @return the profile monitor
	 */
	public static ProfileMonitor start(final Signature sig) {
		return INSTANCE.startProfileMonitorFor(sig);
	}

	/**
	 * Start profile monitor for the given signature.
	 *
	 * @param sig
	 *            the signature
	 * @return the profile monitor
	 */
	public ProfileMonitor startProfileMonitorFor(final Signature sig) {
		return this.startProfileMonitorFor(SignatureHelper.getAsString(sig));
	}

	/**
	 * Start profile monitor for the given signature.
	 *
	 * @param sig
	 *            the signature
	 * @return the profile monitor
	 */
	public ProfileMonitor startProfileMonitorFor(final String sig) {
		ProfileMonitor mon = factory.getMonitor(sig);
		mon.start();
		return mon;
	}

	private synchronized ProfileMonitor getMonitor(final Signature sig) {
		return factory.getMonitor(SignatureHelper.getAsString(sig));
	}

	/**
	 * Gets the monitors (unsorted).
	 *
	 * @return the monitors
	 */
	public ProfileMonitor[] getMonitors() {
		return factory.getMonitors();
	}

	/**
	 * Gets the sorted monitors.
	 *
	 * @return monitors sorted after total time (descending order)
	 */
	protected final ProfileMonitor[] getSortedMonitors() {
		ProfileMonitor[] monitors = getMonitors();
		Arrays.sort(monitors);
		return monitors;
	}

	private ProfileMonitor getMaxHitsMonitor() {
		ProfileMonitor[] monitors = getMonitors();
		ProfileMonitor max = new SimpleProfileMonitor();
		for (int i = 0; i < monitors.length; i++) {
			if (monitors[i].getHits() >= max.getHits()) {
				max = monitors[i];
			}
		}
		return max;
	}

	/**
	 * Gets the max hits.
	 *
	 * @return the max hits
	 * @see patterntesting.runtime.monitor.ProfileStatisticMBean#getMaxHits()
	 */
	@Override
	public int getMaxHits() {
		return getMaxHitsMonitor().getHits();
	}

	/**
	 * Gets the max hits label.
	 *
	 * @return the max hits label
	 * @see patterntesting.runtime.monitor.ProfileStatisticMBean#getMaxHitsLabel()
	 */
	@Override
	public String getMaxHitsLabel() {
		return getMaxHitsMonitor().getLabel();
	}

	/**
	 * Gets the max hits statistic.
	 *
	 * @return the max hits statistic
	 * @see patterntesting.runtime.monitor.ProfileStatisticMBean#getMaxHitsStatistic()
	 */
	@Override
	public String getMaxHitsStatistic() {
		return getMaxHitsMonitor().toShortString();
	}

	private ProfileMonitor getMaxTotalMonitor() {
		ProfileMonitor[] monitors = getMonitors();
		ProfileMonitor max = new SimpleProfileMonitor();
		for (int i = 0; i < monitors.length; i++) {
			if (monitors[i].getTotal() >= max.getTotal()) {
				max = monitors[i];
			}
		}
		return max;
	}

	/**
	 * Gets the max total.
	 *
	 * @return the max total
	 * @see patterntesting.runtime.monitor.ProfileStatisticMBean#getMaxTotal()
	 */
	@Override
	public double getMaxTotal() {
		return getMaxTotalMonitor().getTotal();
	}

	/**
	 * Gets the max total label.
	 *
	 * @return the max total label
	 * @see patterntesting.runtime.monitor.ProfileStatisticMBean#getMaxTotalLabel()
	 */
	@Override
	public String getMaxTotalLabel() {
		return getMaxTotalMonitor().getLabel();
	}

	/**
	 * Gets the max total statistic.
	 *
	 * @return the max total statistic
	 * @see patterntesting.runtime.monitor.ProfileStatisticMBean#getMaxTotalStatistic()
	 */
	@Override
	public String getMaxTotalStatistic() {
		return getMaxTotalMonitor().toShortString();
	}

	private ProfileMonitor getMaxAvgMonitor() {
		ProfileMonitor[] monitors = getMonitors();
		ProfileMonitor max = monitors[0];
		double maxValue = 0.0;
		for (int i = 0; i < monitors.length; i++) {
			double value = monitors[i].getAvg();
			if (!Double.isNaN(value) && (value > maxValue)) {
				maxValue = value;
				max = monitors[i];
			}
		}
		return max;
	}

	/**
	 * Gets the root monitor.
	 *
	 * @return the root monitor
	 */
	protected ProfileMonitor getRootMonitor() {
		return this.factory.getRootMonitor();
	}

	/**
	 * Gets the max avg.
	 *
	 * @return the max avg
	 * @see patterntesting.runtime.monitor.ProfileStatisticMBean#getMaxAvg()
	 */
	@Override
	public double getMaxAvg() {
		return getMaxAvgMonitor().getAvg();
	}

	/**
	 * Gets the max avg label.
	 *
	 * @return the max avg label
	 * @see patterntesting.runtime.monitor.ProfileStatisticMBean#getMaxAvgLabel()
	 */
	@Override
	public String getMaxAvgLabel() {
		return getMaxAvgMonitor().getLabel();
	}

	/**
	 * Gets the max avg statistic.
	 *
	 * @return the max avg statistic
	 * @see patterntesting.runtime.monitor.ProfileStatisticMBean#getMaxAvgStatistic()
	 */
	@Override
	public String getMaxAvgStatistic() {
		return getMaxAvgMonitor().toShortString();
	}

	private ProfileMonitor getMaxMaxMonitor() {
		ProfileMonitor[] monitors = getMonitors();
		ProfileMonitor max = new SimpleProfileMonitor();
		for (int i = 0; i < monitors.length; i++) {
			if (monitors[i].getMax() >= max.getMax()) {
				max = monitors[i];
			}
		}
		return max;
	}

	/**
	 * Gets the max max.
	 *
	 * @return the max max
	 * @see patterntesting.runtime.monitor.ProfileStatisticMBean#getMaxMax()
	 */
	@Override
	public double getMaxMax() {
		return getMaxMaxMonitor().getMax();
	}

	/**
	 * Gets the max max label.
	 *
	 * @return the max max label
	 * @see patterntesting.runtime.monitor.ProfileStatisticMBean#getMaxMaxLabel()
	 */
	@Override
	public String getMaxMaxLabel() {
		return getMaxMaxMonitor().getLabel();
	}

	/**
	 * Gets the max max statistic.
	 *
	 * @return the max max statistic
	 * @see patterntesting.runtime.monitor.ProfileStatisticMBean#getMaxMaxStatistic()
	 */
	@Override
	public String getMaxMaxStatistic() {
		return getMaxMaxMonitor().toShortString();
	}

	/**
	 * Gets the statistics.
	 *
	 * @return the statistics
	 * @see patterntesting.runtime.monitor.ProfileStatisticMBean#getStatistics()
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public TabularData getStatistics() {
		try {
			String[] itemNames = { "Label", "Units", "Hits", "Avg", "Total", "Min", "Max" };
			String[] itemDescriptions = { "method name", "time unit (e.g. ms)", "number of hits", "average time",
					"total time", "minimal time", "maximal time" };
			OpenType[] itemTypes = { SimpleType.STRING, SimpleType.STRING, SimpleType.INTEGER, SimpleType.DOUBLE,
					SimpleType.DOUBLE, SimpleType.DOUBLE, SimpleType.DOUBLE };
			CompositeType rowType = new CompositeType("propertyType", "property entry", itemNames, itemDescriptions,
					itemTypes);
			TabularDataSupport data = MBeanHelper.createTabularDataSupport(rowType, itemNames);
			ProfileMonitor[] monitors = getSortedMonitors();
			for (int i = 0; i < monitors.length; i++) {
				Map<String, Object> map = new HashMap<>();
				map.put("Label", monitors[i].getLabel());
				map.put("Units", monitors[i].getUnits());
				map.put("Hits", monitors[i].getHits());
				map.put("Avg", monitors[i].getAvg());
				map.put("Total", monitors[i].getTotal());
				map.put("Min", monitors[i].getMin());
				map.put("Max", monitors[i].getMax());
				CompositeDataSupport compData = new CompositeDataSupport(rowType, map);
				data.put(compData);
			}
			return data;
		} catch (OpenDataException e) {
			LOG.error("can't create TabularData for log settings", e);
			return null;
		}
	}

	/**
	 * Log statistic.
	 *
	 * @see patterntesting.runtime.monitor.ProfileStatisticMBean#logStatistic()
	 */
	@Override
	public void logStatistic() {
		LOG.info("----- Profile Statistic -----");
		ProfileMonitor[] monitors = getSortedMonitors();
		for (ProfileMonitor profMon : monitors) {
			LOG.info("{}", profMon);
		}
	}

	/**
	 * Dump statistic to a file in the temporary directory. Since 1.4.2 the
	 * filename is no longer "profile###.csv", but begins with the classname.
	 * The reason is located in the subclass SqlStatistic - now you can see
	 * which CSV file belongs to which statistic.
	 *
	 * @return the name of the dump file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @see ProfileStatisticMBean#dumpStatistic()
	 */
	@Override
	public File dumpStatistic() throws IOException {
		File dumpFile = File.createTempFile(this.getClass().getSimpleName(), ".csv");
		dumpStatisticTo(dumpFile);
		return dumpFile;
	}

	/**
	 * This operation dumps the statistic to the given file.
	 *
	 * @param filename
	 *            the file name
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @see ProfileStatisticMBean#dumpStatistic(String)
	 * @since 1.5
	 */
	@Override
	public void dumpStatistic(final String filename) throws IOException {
		this.dumpStatisticTo(new File(filename));
	}

	/**
	 * Dump statistic to the given file.
	 *
	 * @param dumpFile
	 *            the dump file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void dumpStatisticTo(final File dumpFile) throws IOException {
		ProfileMonitor[] monitors = getSortedMonitors();
		if (monitors.length == 0) {
			LOG.debug("No profiling data available.");
			return;
		}
		try (BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(dumpFile), StandardCharsets.UTF_8))) {
    		writer.write(monitors[0].toCsvHeadline());
    		writer.newLine();
    		for (ProfileMonitor profMon : monitors) {
    			writer.write(profMon.toCsvString());
    			writer.newLine();
    		}
		}
		LOG.info("Profiling data dumped to '{}'.", dumpFile);
	}

	/**
	 * Do you want to look for the monitor of a given method? Use this method
	 * here.
	 *
	 * @param clazz
	 *            the clazz
	 * @param method
	 *            the method name, including parameter e.g.
	 *            "getProfileMonitor(Class,String)"
	 *
	 * @return monitor of the given class or null
	 */
	@MayReturnNull
	public ProfileMonitor getProfileMonitor(final Class<?> clazz, final String method) {
		return this.getProfileMonitor(clazz.getName() + "." + method);
	}

	/**
	 * Do you want to look for the monitor of a given method? Use this method
	 * here.
	 *
	 * @param signature
	 *            e.g. "hello.World(String[])"
	 *
	 * @return monitor for the given signature or null
	 * @since 1.4.2
	 */
	@MayReturnNull
	public ProfileMonitor getProfileMonitor(final Signature signature) {
		return this.getProfileMonitor(SignatureHelper.getAsString(signature));
	}

	/**
	 * Do you want to look for the monitor of a given method? Use this method
	 * here.
	 *
	 * @param signature
	 *            e.g. "hello.World(String[])"
	 *
	 * @return monitor for the given signature or null
	 */
	@MayReturnNull
	public ProfileMonitor getProfileMonitor(final String signature) {
		for (ProfileMonitor profMon : this.getMonitors()) {
			if (signature.equals(profMon.getLabel())) {
				return profMon;
			}
		}
		LOG.trace("no ProfileMonitor for " + signature + " found");
		return null;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Thread#toString()
	 */
	@Override
	public String toString() {
		return this.mbeanName.toString();
	}

}
