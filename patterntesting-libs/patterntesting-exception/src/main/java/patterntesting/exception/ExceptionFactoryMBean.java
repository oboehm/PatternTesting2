/*
 * $Id: ExceptionFactoryMBean.java,v 1.10 2016/01/06 20:46:12 oboehm Exp $
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
 */
package patterntesting.exception;

import patterntesting.runtime.jmx.Description;

/**
 * The Interface ExceptionFactoryMBean.
 */
@Description("ExceptionFactory to control exceptions")
public interface ExceptionFactoryMBean {

    /** The default value for {@link #setScope(String)} if nothing was set. */
    String ALL_CLASSES = "all classes";

    /**
	 * Checks if is active.
	 *
	 * @return true, if is active
	 */
	boolean isActive();

	/**
	 * You can only provoke Exceptions if the active flag is set.
	 *
	 * @param activ true or false
	 */
	@Description("exceptions can only be provoked it is set to active")
	void setActive(boolean activ);

	/**
	 * You can only provoke Exceptions if the active flag is set.
	 * Call this method to set it.
	 */
	@Description("Exceptions will be provoked until the deactivate method is called")
	void activate();

	/**
	 * If you want to provoke an Exception only for the next time use this
	 * method.
	 */
	@Description("Exceptions will be only provoked once")
	void activateOnce();

	/**
	 * You don't want to provoke an exception any longer? Then call this
	 * method.
	 */
	@Description("Exceptions will be no  longer provoked")
	void deactivate();

	/**
	 * To see if the (Sun) Java-VM was called with the option "-ea"
	 * ("enable asserts") this getter shows it.
	 *
	 * @return true if asserts are enabled
	 */
	@Description("JavaVM started with '-ea'?")
	boolean isAssertsEnabled();

	/**
	 * Gets the last provoked exception.
	 *
	 * @return the last exception which was thrown by one of the provoke methods
	 */
	@Description("returns the last provoked exception")
	Throwable getLastProvoked();

    /**
     * Only exceptions of this returned type will be fired.
     * The return value is a String because of the use as MBean (works better
     * in the JConsole).
     *
     * @return the exeption to be fired (e.g. "java.lang.Throwable")
     * @since 1.1
     */
    @Description("only exception of this sub type are thrown")
    String getFire();

    /**
     * You want to provoked an SocketException whenever it is possible?
     * Then give it as parameter to this setter method.
     * <p>
     * If you want to reset it and want to provoke any exception set it to
     * Throwable or call the resetFire() method.
     * </p>
     * <p>
     * For better use with the JConsole the class could given as String.
     * But don't forget to give the complete classname (with package) as
     * String.
     * </p>
     *
     * @param classname e.g. "java.io.IOException"
     * @throws ClassNotFoundException if parameter is not a class name
     * @since 1.1
     */
    @Description("fire this exception whenver it is possible")
    void setFire(String classname) throws ClassNotFoundException;

    /**
     * Allows again that all exceptions would be fired.
     * @since 1.1
     */
    @Description("enables all exceptions again")
    void resetFire();

	/**
	 * Gets the number of provoked exceptions.
	 *
	 * @return the number of provoked exceptions
	 */
	@Description("total number of provoked exceptions")
	long getNumberOfProvoked();

	/**
     * Limit the number of provoked maximal total number of provoked exceptions
	 * to n.
	 *
	 * @param n the maximal number of provoked exceptions
	 * (default is Long.MAX_VALUE)
	 */
	@Description("how many exceptions should be provoked?")
	void setMaxNumberOfProvoked(long n);

	/**
	 * Gets the max number of provoked exceptions.
	 *
	 * @return the maximal number of provoked exceptions
	 * (default is Long.MAX_VALUE)
	 */
	@Description("gets the max number of provoked exceptions")
    long getMaxNumberOfProvoked();

    /**
     * To limit the exception to be thrown for a given class you can use
     * this method here.
     * At the moment a scope is only limited to the class itself.
     * Perhaps in the future this can change - than super classes or interfaces
     * may be also supported as parameter.
     * <p>
     * A string is excepted instead of a Class object because of the jconsole.
     * With the jconsole only basic types or strings are available as input
     * fields.
     * </p>
     *
     * @param classname for which an exception should be thrown
     * @since 1.1
     */
    @Description("the factory can be registered for a given classname")
    void setScope(String classname);

    /**
     * To set the scope back to "all classes" use this method here.
     *
     * @since 1.1
     */
    @Description("to set the scope back to all classes")
    void resetScope();

    /**
     * Returns the scope for which the exceptions will be thrown.
     * The default value is "all classes" if the scope is not set.
     *
     * @return class for which an exception will be thrown.
     * @since 1.1
     */
    @Description("returns the scope for which the exceptions will be thrown")
    String getScope();

    /**
     * Resets all preferences.
     * @since 1.1
     */
    @Description("resets all preferences")
    void reset();

}
