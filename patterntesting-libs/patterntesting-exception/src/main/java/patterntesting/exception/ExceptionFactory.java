/*
 * $Id: ExceptionFactory.java,v 1.21 2016/12/18 21:57:35 oboehm Exp $
 *
 * Copyright (c) 2009 by Oliver Boehm
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * (c)reated 05.03.2009 by oliver (ob@aosd.de)
 */
package patterntesting.exception;

import javax.management.JMException;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.apache.logging.log4j.*;

import patterntesting.runtime.jmx.MBeanHelper;
import patterntesting.runtime.util.*;

/**
 * This class can create for testing purpose any desired checked exception.
 * Implemented with the help of Thomas.Darimont@web.de (via mailing list of
 * aspectj-users@eclipse.org).
 * <p>
 * It is realized as Singleton to be able to extract an MBean interface for the
 * use via JMX.
 * </p>
 * <p>
 * The exceptions are controlled by AbstractTestExceptionFactory.
 * You will see the ExceptionFactory not before the first
 * method marked as @TestException has finished (because it is realized as
 * after advice). If you want to see it before
 * call <i>ExceptionFactory.getInstance()</i> (with the creation of
 * the instance it is also registered as MBean).
 * </p>
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.21 $
 * @since 05.03.2009
 */
public final class ExceptionFactory implements ExceptionFactoryMBean {

    private static final Logger LOG = LogManager.getLogger(ExceptionFactory.class);
    private static final ExceptionFactory INSTANCE;
    /** number of provoked exceptions. */
    private long numberOfProvoked = 0L;
    /** maximal number of provoked exceptions. */
    private long maxNumberOfProvoked = 0L;
	/** the last provoked exception which was thrown. */
    private Throwable lastProvoked;
    /** only classes and subclasses of 'limitedTo' can be provoked. */
    private Class<? extends Throwable> limitedTo = Throwable.class;
    /** to exception to be fired */
    private Class<? extends Throwable> fire = null;
    /** the scope can be limited to a single class. */
    private Class<?> scope;

    static {
    	INSTANCE = new ExceptionFactory();
    	try {
    	    if (Assertions.ENABLED) {
    	        INSTANCE.registerMeAsMBean();
    	    } else {
    	        LOG.debug("ExceptionFactory is disabled (no assertions)");
    	    }
		} catch (JMException e) {
			LOG.info("Cannot register " + INSTANCE + " as MBean:", e);
		}
    }

    private ExceptionFactory() {
    	if (LOG.isTraceEnabled()) {
    		LOG.trace(this + " created");
    	}
    }

    /**
     * We implement registerAsMBean ourself and not via MBeanRegistry
     * interface because we don't need the other methods declared in this
     * interface. And because the Ajdoc generation via maven failed
     * (it does not recognize patterntesting-rt as AspectJ lib).
     *
     * @throws JMException if it can't be registered to JMX
     */
    private void registerMeAsMBean() throws JMException {
        MBeanHelper.registerMBean(this);
        LOG.debug("ExceptionFactory registered as MBean");
    }

    /**
     * Normally the ExceptionFactory register itself at JMX. But only if the
     * class is loaded. To force the classloader to load the ExceptionFactory
     * you can use the getInstance() method or this method here.
     *
     * @throws JMException if it can't be registerd at JMX
     * @since 1.0
     */
    public static void registerAsMBean() throws JMException {
        ExceptionFactory factory = ExceptionFactory.getInstance();
        synchronized (factory) {
            if (!MBeanHelper.isRegistered(factory)) {
                factory.registerMeAsMBean();
            }
        }
    }

    /**
     * It is realized as singleton because of the MBean interface.
     *
     * @return the only instance
     */
    public static ExceptionFactory getInstance() {
    	return INSTANCE;
    }

    /**
     * Gets the maximal number of provoked exceptions
     * (default is Long.MAX_VALUE).
     *
     * @return the number of provoked exceptions
     * @see ExceptionFactoryMBean#getMaxNumberOfProvoked()
     */
    public synchronized long getMaxNumberOfProvoked() {
        return this.maxNumberOfProvoked;
    }

    /**
     * Gets the number of provoked exceptions.
     *
     * @return the number of provoked exceptions
     * @see ExceptionFactoryMBean#getNumberOfProvoked()
     */
    public synchronized long getNumberOfProvoked() {
        return this.numberOfProvoked;
    }

    /**
     * Limit the number of provoked maximal total number of provoked exceptions
     * to n (default is Long.MAX_VALUE).
     *
     * @param n the maximal number of provoked exceptions
     * @see ExceptionFactoryMBean#setMaxNumberOfProvoked(long)
     */
    public synchronized void setMaxNumberOfProvoked(final long n) {
        this.maxNumberOfProvoked = n;
    }

    /**
     * Checks if is active.
     *
     * @return true if active
     * @see ExceptionFactoryMBean#isActive()
     */
    public synchronized boolean isActive() {
		return this.maxNumberOfProvoked > this.numberOfProvoked;
	}

    /**
     * You can only provoke Exceptions if the active flag is set.
     *
     * @param active true or false
	 * @see ExceptionFactoryMBean#setActive(boolean)
	 */
    public synchronized void setActive(final boolean active) {
		this.maxNumberOfProvoked = active ? Long.MAX_VALUE
                : this.numberOfProvoked;
	}

    /**
     * Activate.
     *
     * @see ExceptionFactoryMBean#activate()
     */
    public synchronized void activate() {
		this.setActive(true);
	}

    /**
     * Activate once.
     *
     * @see ExceptionFactoryMBean#activateOnce()
     */
    public synchronized void activateOnce() {
        this.maxNumberOfProvoked = this.numberOfProvoked + 1;
    }

    /**
     * Deactivate.
     *
     * @see ExceptionFactoryMBean#deactivate()
     */
    public synchronized void deactivate() {
		this.setActive(false);
	}

	/**
     * To see if the (Sun) Java-VM was called with the option "-ea"
     * ("enable asserts") this getter shows it.
     *
     * @return true if asserts are enabled
	 * @see ExceptionFactoryMBean#isAssertsEnabled()
	 */
	public boolean isAssertsEnabled() {
		return Assertions.ENABLED;
	}

	/**
     * Gets the last provoked exception.
     *
     * @return the last exception which was thrown by one of the provoke methods
	 * @see ExceptionFactoryMBean#getLastProvoked()
	 */
	public synchronized Throwable getLastProvoked() {
		return lastProvoked;
	}

	/**
	 * "Not limited" means, limitedTo is a super class of the given type.
	 * @param type the class type
	 * @return true or false
	 */
	private synchronized boolean isNotLimited(final Class<?> type) {
		if (type.equals(limitedTo)) {
			return true;
		}
		if (type.equals(Throwable.class)) {
			return false;
		}
		return isNotLimited(type.getSuperclass());
	}

	/**
	 * "can be fired" means, that the given type is a subclass of "fire".
	 * @param type the exception type
	 * @return true or false
	 */
	private boolean canBeFired(final Class<? extends Throwable> type) {
//	    return this.fire.isAssignableFrom(type);
	    if (this.fire == null) {
	        return true;
	    }
        return type.isAssignableFrom(this.fire);
	}

    /**
     * Be careful - you can provoke any Exception with the method without the
     * need to declare it with a throws statement. For example
     * <pre>provoke(IOException.class)</pre>
     * would throw an IOException.
     *
     * @param type e.g. IOException.class
     * @see ExceptionThrower#provoke(Class)
     */
    public synchronized void provoke(final Class<? extends Throwable> type) {
        if (Assertions.ENABLED && this.isActive() && this.isNotLimited(type)
                && this.canBeFired(type)) {
            fire(type);
    	} else {
    		if (LOG.isTraceEnabled()) {
                LOG.trace("active flag not set or " + this.getFire()
                        + " cannot be fired here");
    		}
    	}
    }

    private void fire(final Class<? extends Throwable> type) {
        this.numberOfProvoked++;
        if (this.fire == null) {
            ExceptionThrower.provoke(type);
        } else {
            ExceptionThrower.provoke(this.fire);
        }
    }

    /**
     * This methods throws one of the exception which is possible for the given
     * joinpoint. But only if the joinpoint matches one of the registered
     * objects, classes or threads.
     *
     * @param jp the joinpoint for which an exception should be provoked
     */
    @SuppressWarnings("unchecked")
    public synchronized void provokeFor(final JoinPoint jp) {
        if ((this.scope == null) || this.matchScope(jp.getThis())) {
            CodeSignature sig = (CodeSignature) jp.getSignature();
            this.provokeOneOf(sig.getExceptionTypes());
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("no excecption provoked for "
                        + JoinPointHelper.getAsShortString(jp)
                        + " because scope = " + this.getScope());
            }
        }
    }

    private boolean matchScope(final Object target) {
        return matchScope(target.getClass());
    }

    /**
     * Checks if a scope is limited to the class itself. Subclasses and
     * interfaces are also supported as parameter.
     *
     * @param target the target class
     * @return true if target and scope are the same class
     */
    private boolean matchScope(final Class<?> target) {
        return this.scope.isAssignableFrom(target);
    }

    /**
     * This method throws the first given Throwable type. If this fails the
     * next element array is tried to be created as Throwable.
     * <p>
     * You can only provoke an Exception if the active flag is set.
     * </p>
     *
     * @param types a class array with exception types
     */
    protected synchronized void provokeOneOf(final Class<? extends Throwable>[] types) {
    	if (this.isActive()) {
	        for (int i = 0; i < types.length; i++) {
	        	if (isNotLimited(types[i]) && this.canBeFired(types[i])) {
	        	    fire(types[i]);
	        	    break;
	        	}
	        }
    	} else {
    		if (LOG.isTraceEnabled()) {
                LOG.trace("active flag not set or not a subclass of "
                        + this.limitedTo + " -> no "
                        + Converter.toString(types) + " thrown");
    		}
    	}
    }

    /**
     * To limit the exception to be thrown for a given class you can use
     * this method here.
     * <p>
     * This setter is not part of the ExceptionFactoryMBean interface because
     * JMX allows only one setter for an attribute. Otherwise you'll get an
     * <i>javax.management.NotCompliantMBeanException:
     * Attribute Scope has more than one setter</i>.
     * </p>
     *
     * @param target the target
     * @see patterntesting.exception.ExceptionFactoryMBean#setScope(java.lang.String)
     */
    public synchronized void setScope(final Class<?> target) {
        this.scope = target;
    }

    /**
     * To limit the exception to be thrown for a given class you can use
     * this method here.
     * <p>
     * A string is excepted instead of a Class object because of the jconsole.
     * With the jconsole only basic types or strings are available as input
     * fields.
     * </p>
     *
     * @param classname for which an exception should be thrown
     * @see patterntesting.exception.ExceptionFactoryMBean#setScope(java.lang.String)
     * @since 1.1
     */
    public synchronized void setScope(final String classname) {
        try {
            Class<?> clazz = Class.forName(classname);
            this.setScope(clazz);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(classname + " not found", e);
        }
    }

    /**
     * To set the scope back to "all classes" use this method here.
     *
     * @since 1.1
     */
    public synchronized void resetScope() {
        this.scope = null;

    }

    /**
     * Returns the scope for which the exceptions will be thrown.
     * The default value is "all classes" if the scope is not set.
     *
     * @return class for which an exception will be thrown.
     * @see patterntesting.exception.ExceptionFactoryMBean#getScope()
     * @since 1.1
     */
    public synchronized String getScope() {
        if (this.scope == null) {
            return ALL_CLASSES;
        }
        return this.scope.getName();
    }

    /**
     * Only exceptions of this returned type will be fired.
     * The return value is a String because of the use as MBean (works better
     * in the JConsole).
     *
     * @return the exeption to be fired (e.g. "java.lang.Throwable")
     * @see patterntesting.exception.ExceptionFactoryMBean#getFire()
     * @since 1.1
     */
    public synchronized String getFire() {
        if (this.fire == null) {
            return "all exceptions";
        }
        return this.fire.getName();
    }

    /**
     * You want to provoked an SocketException whenever it is possible?
     * Then give it as parameter to this setter method.
     * <br>
     * For better use with the JConsole the class could given as String.
     * But don't forget to give the complete classname (with package) as
     * String.
     *
     * @param classname e.g. "java.net.SocketException"
     * @throws ClassNotFoundException if parameter is not a class name
     * @see patterntesting.exception.ExceptionFactoryMBean#setFire(java.lang.String)
     * @since 1.1
     */
    @SuppressWarnings("unchecked")
    public synchronized void setFire(final String classname) throws ClassNotFoundException {
        this.fire = (Class<? extends Throwable>) Class.forName(classname);
    }

    /**
     * You want to provoked an SocketException whenever it is possible?
     * Then give it as parameter to this setter method.
     *
     * @param fire e.g. SocketException.class
     * @since 1.1
     */
    public synchronized void setFire(final Class<? extends Throwable> fire) {
        this.fire = fire;
    }

    /**
     * Allows again that all exceptions would be fired.
     * @see patterntesting.exception.ExceptionFactoryMBean#resetFire()
     * @since 1.1
     */
    public synchronized void resetFire() {
        this.fire = null;
    }

    /**
     * Resets all preferences and deactivates ExceptionFactory.
     * @see patterntesting.exception.ExceptionFactoryMBean#reset()
     * @since 1.1
     */
    public synchronized void reset() {
        this.resetScope();
        this.resetFire();
        this.deactivate();
        this.limitedTo = Throwable.class;
    }

}
