/*
 * $Id: StackTraceScanner.java,v 1.11 2016/12/18 20:19:36 oboehm Exp $
 *
 * Copyright (c) 2014 by Oliver Boehm
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
 * (c)reated 24.01.2014 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.util;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.*;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.aspectj.lang.reflect.MethodSignature;

import patterntesting.runtime.exception.NotFoundException;

/**
 * This class allows you to scan the stacktrace for different stuff.
 *
 * @author oliver
 * @since 1.4.1 (24.01.2014)
 */
public final class StackTraceScanner {

	private static final Logger LOG = LoggerFactory.getLogger(StackTraceScanner.class);

	/** Utility class - no need to instantiate it. */
	private StackTraceScanner() {
	}

	/**
	 * Find the constructor of the given class on the stack trace.
	 *
	 * @param clazz
	 *            the clazz
	 * @return the stack trace element
	 */
	public static StackTraceElement findConstructor(final Class<?> clazz) {
		return find(clazz.getName(), "<init>");
	}

	/**
	 * Find the given signature on the stack trace.
	 *
	 * @param signature
	 *            the signature
	 * @return the stack trace element
	 */
	public static StackTraceElement find(final Signature signature) {
		if (signature instanceof MethodSignature) {
			return find((MethodSignature) signature);
		} else if (signature instanceof ConstructorSignature) {
			return find((ConstructorSignature) signature);
		} else {
			throw new IllegalArgumentException(signature + " is not a method or ctor signature");
		}
	}

	private static StackTraceElement find(final ConstructorSignature signature) {
		return findConstructor(signature.getDeclaringType());
	}

	private static StackTraceElement find(final MethodSignature signature) {
		return find(signature.getDeclaringType(), signature.getMethod());
	}

	/**
	 * Find the given method on the stack trace.
	 *
	 * @param clazz
	 *            the clazz
	 * @param method
	 *            the method
	 * @return the stack trace element
	 */
	public static StackTraceElement find(final Class<?> clazz, final Method method) {
		return find(clazz.getName(), method.getName());
	}

	/**
	 * Find the given method on the stack trace.
	 *
	 * @param clazz
	 *            the clazz
	 * @param methodName
	 *            the method name
	 * @return the stack trace element
	 */
	public static StackTraceElement find(final Class<?> clazz, final String methodName) {
		return find(clazz.getName(), methodName);
	}

	/**
	 * Find the given method on the stack trace.
	 *
	 * @param classname
	 *            the classname
	 * @param methodName
	 *            the method name
	 * @return the stack trace element
	 */
	public static StackTraceElement find(final String classname, final String methodName) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		int i = getIndexOf(classname, methodName, stackTrace);
		return stackTrace[i];
	}

	/**
	 * Gets the caller of a constructor.
	 *
	 * @param clazz
	 *            the clazz
	 * @return the caller of constructor
	 */
	public static StackTraceElement getCallerOfConstructor(final Class<?> clazz) {
		return getCallerOfConstructor(clazz, new Pattern[0]);
	}

	/**
	 * Gets the caller of a constructor.
	 *
	 * @param clazz
	 *            the clazz
	 * @param excluded
	 *            the excluded
	 * @return the caller of constructor
	 */
	public static StackTraceElement getCallerOfConstructor(final Class<?> clazz, final Pattern... excluded) {
		try {
			return getCallerOf(clazz, "<init>", excluded);
		} catch (NotFoundException nfe) {
			LOG.trace("Caller of constructor of {} not found:", clazz, nfe);
			LOG.debug("Looking for adviced init method because of {}", nfe.getMessage());
			StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
			for (int i = 1; i < stackTrace.length; i++) {
				if (stackTrace[i].getMethodName().startsWith("init$")) {
					for (int j = i + 1; j < stackTrace.length; j++) {
						if (!matches(stackTrace[j].getMethodName(), excluded)) {
							return stackTrace[j];
						}
					}
					break;
				}
			}
			throw new NotFoundException(
					"new " + clazz.getSimpleName() + "(..) not part of " + Converter.toShortString(stackTrace));
		}
	}

	/**
	 * Gets the caller of the given signature.
	 *
	 * @param signature
	 *            the signature
	 * @return the caller of
	 */
	public static StackTraceElement getCallerOf(final Signature signature) {
		return getCallerOf(signature, new Pattern[0]);
	}

	/**
	 * Gets the caller of the given method.
	 *
	 * @param clazz
	 *            the clazz
	 * @param method
	 *            the method
	 * @return the caller of
	 */
	public static StackTraceElement getCallerOf(final Class<?> clazz, final Method method) {
		return getCallerOf(clazz, method, new Pattern[0]);
	}

	/**
	 * Gets the caller of the given method.
	 *
	 * @param clazz
	 *            the clazz
	 * @param methodName
	 *            the method name
	 * @return the caller of
	 */
	public static StackTraceElement getCallerOf(final Class<?> clazz, final String methodName) {
		return getCallerOf(clazz, methodName, new Pattern[0]);
	}

	/**
	 * Gets the caller of the given method.
	 *
	 * @param classname
	 *            the classname
	 * @param methodName
	 *            the method name
	 * @return the caller of
	 */
	public static StackTraceElement getCallerOf(final String classname, final String methodName) {
		return getCallerOf(classname, methodName, new Pattern[0]);
	}

	/**
	 * Gets the caller of the given signature.
	 *
	 * @param signature
	 *            the signature
	 * @param excluded
	 *            the excluded
	 * @return the caller of
	 */
	public static StackTraceElement getCallerOf(final Signature signature, final Pattern... excluded) {
		if (signature instanceof MethodSignature) {
			return getCallerOf((MethodSignature) signature, excluded);
		} else if (signature instanceof ConstructorSignature) {
			return getCallerOf((ConstructorSignature) signature, excluded);
		} else {
			throw new IllegalArgumentException(signature + " is not a method or ctor signature");
		}
	}

	private static StackTraceElement getCallerOf(final ConstructorSignature signature, final Pattern... excluded) {
		return getCallerOfConstructor(signature.getDeclaringType(), excluded);
	}

	private static StackTraceElement getCallerOf(final MethodSignature signature, final Pattern... excluded) {
		return getCallerOf(signature.getDeclaringType(), signature.getMethod(), excluded);
	}

	/**
	 * Gets the caller of the given method.
	 *
	 * @param clazz
	 *            the clazz
	 * @param method
	 *            the method
	 * @param excluded
	 *            a list of filters which should be not considered as caller
	 * @return the caller of
	 */
	public static StackTraceElement getCallerOf(final Class<?> clazz, final Method method, final Pattern... excluded) {
		return getCallerOf(clazz.getName(), method.getName(), excluded);
	}

	/**
	 * Gets the caller of the given method.
	 *
	 * @param clazz
	 *            the clazz
	 * @param methodName
	 *            the method name
	 * @param excluded
	 *            a list of filters which should be not considered as caller
	 * @return the caller of
	 */
	public static StackTraceElement getCallerOf(final Class<?> clazz, final String methodName,
			final Pattern... excluded) {
		return getCallerOf(clazz.getName(), methodName, excluded);
	}

	/**
	 * Gets the caller of the given method.
	 *
	 * @param classname
	 *            the classname
	 * @param methodName
	 *            the method name
	 * @param excluded
	 *            a list of filters which should be not considered as caller
	 * @return the caller of
	 */
	public static StackTraceElement getCallerOf(final String classname, final String methodName,
			final Pattern... excluded) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		for (int i = getIndexOf(classname, methodName, stackTrace) + 1; i < stackTrace.length; i++) {
			if (!matches(stackTrace[i].getMethodName(), excluded)) {
				return stackTrace[i];
			}
		}
		throw new NotFoundException(classname + "." + methodName + "()");
	}

	private static int getIndexOf(final String classname, final String methodName,
			final StackTraceElement[] stackTrace) {
		for (int i = 1; i < stackTrace.length; i++) {
			StackTraceElement element = stackTrace[i];
			if (methodName.equals(element.getMethodName()) && classname.equals(element.getClassName())) {
				return i;
			}
		}
		throw new NotFoundException(classname + "." + methodName + "()");
	}

	private static boolean matches(final String methodName, final Pattern... excluded) {
		for (int j = 0; j < excluded.length; j++) {
			if (excluded[j].matcher(methodName).matches()) {
				return true;
			}
		}
		return false;
	}

	private static boolean matches(final String className, final Class<?>... excluded) {
		for (int j = 0; j < excluded.length; j++) {
			if (className.equals(excluded[j].getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the caller class by examing the stacktrace.
	 *
	 * @return the caller class
	 */
	public static Class<?> getCallerClass() {
		return getCallerClass(new Pattern[0]);
	}

	/**
	 * Gets the caller class by examing the stacktrace.
	 *
	 * @param excluded
	 *            a list of filters which should be not considered as caller
	 * @return the caller of
	 */
	public static Class<?> getCallerClass(final Pattern... excluded) {
		return getCallerClass(excluded, new Class<?>[0]);
	}

	/**
	 * Gets the caller class by examing the stacktrace.
	 *
	 * @param excludedMethods
	 *            the excluded methods
	 * @param excludedClasses
	 *            the excluded classes
	 * @return the caller class
	 */
	public static Class<?> getCallerClass(final Pattern[] excludedMethods, final Class<?>... excludedClasses) {
		StackTraceElement[] stackTrace = getCallerStackTrace(excludedMethods, excludedClasses);
		String classname = stackTrace[0].getClassName();
		try {
			return Class.forName(classname);
		} catch (ClassNotFoundException ex) {
			throw new NotFoundException(classname, ex);
		}
	}

	/**
	 * Gets the caller stack trace of the method or constructor which calls it.
	 *
	 * @return the caller stack trace
	 * @since 1.4.2 (17.05.2014)
	 */
	public static StackTraceElement[] getCallerStackTrace() {
		return getCallerStackTrace(new Pattern[0]);
	}

	/**
	 * Gets the caller stack trace of the method or constructor which calls it.
	 *
	 * @param excluded
	 *            a list of filters which should be not considered as caller
	 * @return the caller stack trace
	 * @since 1.4.2 (17.05.2014)
	 */
	public static StackTraceElement[] getCallerStackTrace(final Pattern... excluded) {
		return getCallerStackTrace(excluded, new Class<?>[0]);
	}

	/**
	 * Gets the caller stack trace of the method or constructor which calls it.
	 *
	 * @param excludedMethods
	 *            the excluded methods
	 * @param excludedClasses
	 *            the excluded classes
	 * @return the caller stack trace
	 * @since 1.4.2 (17.05.2014)
	 */
	public static StackTraceElement[] getCallerStackTrace(final Pattern[] excludedMethods,
			final Class<?>... excludedClasses) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		int i = 2;
		String scannerClassName = StackTraceScanner.class.getName();
		for (; i < stackTrace.length - 1; i++) {
			if (!scannerClassName.equals(stackTrace[i].getClassName())) {
				break;
			}
		}
		for (; i < stackTrace.length - 1; i++) {
			if (!matches(stackTrace[i].getMethodName(), excludedMethods)
					&& !matches(stackTrace[i].getClassName(), excludedClasses)) {
				break;
			}
		}
		return ArrayUtils.subarray(stackTrace, i, stackTrace.length);
	}

}
