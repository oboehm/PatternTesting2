/*
 * (c)reated 21.07.2010 by oliver
 */
package patterntesting.runtime.junit;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import org.apache.logging.log4j.*;

import patterntesting.runtime.monitor.ClasspathMonitor;
import patterntesting.runtime.util.Converter;

/**
 * This is a utility class to check the serializable nature of classes.
 * <p>
 * NOTE: In the future this class will be perhaps part of the ObjectTester
 * class.
 * </p>
 * <p>
 * Before v1.1 the methods are named "checkSerialization". Since 1.1 these
 * methods will have now an "assert" prefix ("assertSerialization").
 * </p>
 *
 * @author oliver (oliver.boehm@agentes.de)
 * @since 1.0.3 (21.07.2010)
 */
public final class SerializableTester {

	private static final Logger LOG = LogManager.getLogger(SerializableTester.class);
	private static final ClasspathMonitor CLASSPATH_MONITOR = ClasspathMonitor.getInstance();

	/** Utitlity class - no need to instantiate it. */
	private SerializableTester() {
	}

	/**
     * Check serialization and deserialization of an object. Because not all
     * classes has a (correct) overwritten equals method we do not compare the
     * deserialized object with the equals() method. But if it implements
     * Comparable we verify the equality of the two objects with the compareTo()
     * method.
     *
     * @param object the object
     * @throws NotSerializableException if object can't serialized
     * @since 1.1
     */
	public static void assertSerialization(final Serializable object) throws NotSerializableException {
        Object deserialized = deserialize(object);
		if (object instanceof Comparable<?>) {
			ObjectTester.assertCompareTo(object, deserialized);
		}
		assertNoFinalTransientAttribute(object.getClass());
	}

    /**
     * Serialiazes and deserializes the given object. This can be used e.g. for
     * testing if you want to test the deserialized object if transient
     * attributes are initialized correct (transient attributes are null after
     * deserialization).
     * <p>
     * This method is a result of <a href=
     * "https://sourceforge.net/p/patterntesting/feature-requests/50/">feature
     * request 50</a>.
     * </p>
     * 
     * @param object the object to serialize
     * @return the deserialized object
     * @throws NotSerializableException if serialization fails
     * @since 1.7.2
     */
    public static Serializable deserialize(final Serializable object) throws NotSerializableException {
        byte[] bytes = Converter.serialize(object);
        LOG.debug(object + " serialized in " + bytes.length + " bytes");
        try {
            return Converter.deserialize(bytes);
        } catch (ClassNotFoundException canthappen) {
            throw new IllegalArgumentException("cannot be deserialized: " + object, canthappen);
        }
    }

	/**
	 * This method will create an object of the given class using the default
	 * constructor. So three preconditions must be true:
	 * <ol>
	 * <li>the class must not be abstract</li>
	 * <li>there must be a (public) default constructor</li>
	 * <li>it must be Serializable</li>
	 * </ol>
	 *
	 * @param clazz
	 *            the clazz
	 * @throws NotSerializableException
	 *             if the check fails
	 * @since 1.1
	 */
	public static void assertSerialization(final Class<? extends Serializable> clazz) throws NotSerializableException {
		if (LOG.isTraceEnabled()) {
			LOG.trace("checking " + clazz.getName() + " if it can be serialized...");
		}
		Serializable obj = instantiate(clazz);
		assertSerialization(obj);
	}

	/**
	 * Check for each class in the given collection if it can be serialized and
	 * deserialized.
	 *
	 * @param classes
	 *            a collection of classes to be checked
	 * @throws NotSerializableException
	 *             if one of the classes can't be serialized
	 * @since 1.1
	 */
	public static void assertSerialization(final Collection<Class<Serializable>> classes)
			throws NotSerializableException {
		for (Class<?> clazz : classes) {
			assertSerialization(clazz);
		}
	}

	/**
	 * Check for each class in the given package if it can be serialized and
	 * deserialized.
	 * <p>
	 * To get a name of a package call {@link Package#getPackage(String)}. But
	 * be sure that you can't get <em>null</em> as result. In this case use
	 * {@link #assertSerializationOfPackage(String)}.
	 * </p>
	 *
	 * @param pkg
	 *            the package e.g. "patterntesting.runtime"
	 * @throws NotSerializableException
	 *             if one of the class can't be serialized
	 * @see #assertSerializationOfPackage(String)
	 * @since 1.1
	 */
	public static void assertSerialization(final Package pkg) throws NotSerializableException {
		assert pkg != null;
		assertSerializationOfPackage(pkg.getName());
	}

	/**
	 * Check for each class in the given package if it can be serialized and
	 * deserialized.
	 *
	 * @param packageName
	 *            the package name e.g. "patterntesting.runtime"
	 * @throws NotSerializableException
	 *             if one of the class can't be serialized
	 * @since 1.1
	 */
	public static void assertSerializationOfPackage(final String packageName) throws NotSerializableException {
		assert packageName != null;
		Collection<Class<Serializable>> classes = getSerializableClasses(packageName);
		assertSerialization(classes);
	}

	@SuppressWarnings("unchecked")
	private static Collection<Class<Serializable>> getSerializableClasses(final String packageName) {
		String[] classnames = CLASSPATH_MONITOR.getClasspathClasses();
		Collection<Class<Serializable>> classes = new ArrayList<>();
		for (int i = 0; i < classnames.length; i++) {
			if (classnames[i].startsWith(packageName)) {
				try {
					Class<?> clazz = Class.forName(classnames[i]);
					if (Serializable.class.isAssignableFrom(clazz)) {
						classes.add((Class<Serializable>) clazz);
					}
				} catch (ClassNotFoundException ignored) {
					LOG.debug("Class {} is ignored:", classnames[i], ignored);
				} catch (LinkageError ignored) {
					LOG.debug("Uninitializable class {} is ignored:", classnames[i], ignored);
				}
			}
		}
		return classes;
	}

	/**
	 * Serializable classes must not have final transient attributes. Why?
	 * Transient attributes are not stored when an object is serialized. And
	 * if they are final they cannot be initialized after deserialization.
	 * 
	 * @param clazz the serialiable class
	 * @since 1.7.2
	 */
    public static void assertNoFinalTransientAttribute(Class<? extends Serializable> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            int mod = field.getModifiers();
            if (Modifier.isFinal(mod) && Modifier.isTransient(mod)) {
                throw new AssertionError("transient trap - don't use transient and final together in: " + field);
            }
        }
    }

	/**
     * Gets the size of of a newly created object of the given clazz. This is
     * the is the number of bytes the created object needs for serialization.
     *
     * @param clazz the clazz
     * @return the size of
     * @since 1.5 (14.10.2014)
     */
	public static int getSizeOf(final Class<Serializable> clazz) {
		Serializable obj = instantiate(clazz);
		return getSizeOf(obj);
	}

	/**
	 * Gets the size of the given object - this is the number of bytes the given
	 * object needs for serialization.
	 *
	 * @param object
	 *            the object
	 * @return the size of
	 * @since 1.5 (14.10.2014)
	 */
	public static int getSizeOf(final Serializable object) {
		try {
			return Converter.serialize(object).length;
		} catch (NotSerializableException ex) {
			throw new IllegalArgumentException("not serializable: " + object, ex);
		}
	}

	private static Serializable instantiate(final Class<? extends Serializable> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException ex) {
			throw new IllegalArgumentException("can't instantiate " + clazz, ex);
		} catch (IllegalAccessException ex) {
			throw new IllegalArgumentException("can't access ctor of " + clazz, ex);
		}
	}

}
