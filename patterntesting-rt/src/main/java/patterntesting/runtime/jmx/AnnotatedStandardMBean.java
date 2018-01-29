package patterntesting.runtime.jmx;

import java.lang.reflect.Method;
import java.util.*;

import javax.management.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;

import patterntesting.runtime.util.Converter;

/**
 * The Class AnnotatedStandardMBean.
 *
 * @author ninthbit
 *
 *         StandardMBeans have meaningless default values with can be overcome
 *         subclassing and annotations. This classed is based on the article
 *         http://weblogs.java.net/blog/emcmanus/archive/2005/07/adding_informat.html
 */
public class AnnotatedStandardMBean extends StandardMBean {

	private static final Logger LOG = LogManager.getLogger(AnnotatedStandardMBean.class);

	/**
	 * Instance where the MBean interface is implemented by another object.
	 *
	 * @param <T>
	 *            not necessary but occasionally avoids compiler warnings about
	 *            generics
	 * @param impl
	 *            Class which implements an MBean interface
	 * @param mbeanInterface
	 *            MBean interface
	 * @throws NotCompliantMBeanException
	 *             thrown if MBean is not JMX compliant
	 */
	public <T> AnnotatedStandardMBean(final T impl, final Class<T> mbeanInterface) throws NotCompliantMBeanException {
		super(impl, mbeanInterface);
	}

	/**
	 * Instance where the MBean interface is implemented by this object.
	 *
	 * @param mbeanInterface
	 *            MBean interface
	 * @throws NotCompliantMBeanException
	 *             thrown if MBean is not JMX compliant
	 */
	protected AnnotatedStandardMBean(final Class<?> mbeanInterface) throws NotCompliantMBeanException {
		super(mbeanInterface);
	}

	/**
	 * Overrides the default description with the content of a @Description
	 * annotation.
	 *
	 * @param op
	 *            the op
	 * @return the description
	 * @see patterntesting.runtime.jmx.Description Description
	 */
	@Override
	protected String getDescription(final MBeanOperationInfo op) {
		String descr = op.getDescription();
		Method m = methodFor(getMBeanInterface(), op);
		return getDescriptionForMethod(descr, m);
	}

	/**
	 * Gets the description.
	 *
	 * @param info
	 *            the info
	 * @return the description
	 * @see javax.management.StandardMBean#getDescription(javax.management.MBeanAttributeInfo)
	 */
	@Override
	protected String getDescription(final MBeanAttributeInfo info) {
		String description = info.getDescription();
		Method m = getMethodForAttribute(info);
		return getDescriptionForMethod(description, m);
	}

	private String getDescriptionForMethod(String descr, final Method m) {
		if (m != null) {
			Description d = m.getAnnotation(Description.class);
			if (d != null) {
				return d.value();
			}
		}
		return descr;
	}

	private Method getMethodForAttribute(final MBeanAttributeInfo info) {
		Method m = null;
		Iterator<String> methodNames = constructPossibleMethodNames(info.getName()).iterator();
		boolean found = false;
		while (!found && methodNames.hasNext()) {
			try {
				m = getMBeanInterface().getMethod(methodNames.next(), new Class[0]);
				found = true;
			} catch (NoSuchMethodException e) {
				LOG.debug(info + " not found in " + methodNames + ":", e);
				found = false;
			}
		}
		return m;
	}

	private List<String> constructPossibleMethodNames(final String attributeName) {
		List<String> names = new ArrayList<>();
		String capitalizedVarName = attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
		names.add("get" + capitalizedVarName);
		names.add("is" + capitalizedVarName);
		names.add("has" + capitalizedVarName);
		names.add("are" + capitalizedVarName);
		return names;
	}

	private static Method methodFor(final Class<?> mbeanInterface, final MBeanOperationInfo op) {
		final MBeanParameterInfo[] params = op.getSignature();
		final String[] paramTypes = new String[params.length];
		for (int i = 0; i < params.length; i++) {
			paramTypes[i] = params[i].getType();
		}

		return findMethod(mbeanInterface, op.getName(), paramTypes);
	}

	private static Method findMethod(final Class<?> mbeanInterface, final String name, final String... paramTypes) {
		try {
			final ClassLoader loader = mbeanInterface.getClassLoader();
			final Class<?>[] paramClasses = new Class<?>[paramTypes.length];
			for (int i = 0; i < paramTypes.length; i++) {
				paramClasses[i] = classForName(paramTypes[i], loader);
			}
			return mbeanInterface.getMethod(name, paramClasses);
		} catch (ClassNotFoundException ex) {
			LOG.error("Cannot find all parameter classes " + Converter.toString(paramTypes) + ":", ex);
		} catch (NoSuchMethodException ex) {
			LOG.error("Cannot find method '" + name + "' for " + mbeanInterface + ":", ex);
		}
		LOG.info("Will return 'null' as fallback for {} and method '{}'.", mbeanInterface, name);
		return null;
	}

	/**
	 * Class for name.
	 *
	 * @param name
	 *            the name
	 * @param loader
	 *            the loader
	 * @return the class
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	private static Class<?> classForName(final String name, final ClassLoader loader) throws ClassNotFoundException {
		Class<?> c = primitiveClasses.get(name);
		if (c == null) {
			c = Class.forName(name, false, loader);
		}
		return c;
	}

	private static final Map<String, Class<?>> primitiveClasses = new HashMap<>();
	static {
		Class<?>[] prims = { byte.class, short.class, int.class, long.class, float.class, double.class, char.class,
				boolean.class, };
		for (Class<?> c : prims) {
			primitiveClasses.put(c.getName(), c);
		}
	}

}
