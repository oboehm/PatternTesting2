/*
 * $Id: SignatureHelper.java,v 1.13 2016/12/18 20:19:36 oboehm Exp $
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
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express orimplied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * (c)reated 03.04.2009 by oliver (ob@aosd.de)
 */
package patterntesting.runtime.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.*;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.*;

import patterntesting.runtime.util.reflect.ConstructorSignatureImpl;
import patterntesting.runtime.util.reflect.MethodSignatureImpl;

/**
 * The Class SignatureHelper.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.13 $
 * @since 03.04.2009
 */
public class SignatureHelper {

	private static final Logger LOG = LoggerFactory.getLogger(SignatureHelper.class);

	/** Utility class - no need to instantiate it */
	private SignatureHelper() {
	}

	/**
	 * The difference to Signature.toString() is that in case of a methode or
	 * constructor the return type is not part of result.
	 *
	 * @param sig
	 *            the sig
	 *
	 * @return the given signature as string
	 */
	public static String getAsString(final Signature sig) {
		if (sig instanceof CodeSignature) {
			try {
				CodeSignature codeSig = (CodeSignature) sig;
				String name = getAsString(sig.getDeclaringTypeName(), sig);
				Class<?>[] params = codeSig.getParameterTypes();
				return name + "(" + Converter.toShortString(params) + ")";
			} catch (RuntimeException e) {
				LOG.debug("Cannot get '" + sig + "' as short string:", e);
			}
		}
		return sig.toString();
	}

	/**
	 * Gets the as string.
	 *
	 * @param type
	 *            the type
	 * @param sig
	 *            the sig
	 *
	 * @return the as string
	 */
	public static String getAsString(final String type, final Signature sig) {
		String name = sig.getName();
		if ("<init>".equals(name)) {
			return "new " + type;
		} else {
			return type + "." + name;
		}
	}

	/**
	 * Gets the as signature.
	 *
	 * @param method
	 *            the method
	 *
	 * @return the as signature
	 */
	public static MethodSignature getAsSignature(final Method method) {
		return new MethodSignatureImpl(method);
	}

	/**
	 * Gets the as signature.
	 *
	 * @param ctor
	 *            the ctor
	 *
	 * @return the as signature
	 */
	public static ConstructorSignature getAsSignature(final Constructor<?> ctor) {
		return new ConstructorSignatureImpl(ctor);
	}

	/**
	 * Gets the as signature.
	 *
	 * @param label
	 *            e.g. "java.lang.String.substring(int)" or "new
	 *            java.lang.String()
	 * @return the given label as Signature
	 * @throws ReflectiveOperationException
	 *             the reflective operation exception
	 */
	public static Signature getAsSignature(final String label) throws ReflectiveOperationException {
		if (label.startsWith("new ")) {
			return getAsConstructorSignature(label);
		}
		String classDotMethod = StringUtils.substringBefore(label, "(");
		String className = StringUtils.substringBeforeLast(classDotMethod, ".");
		String methodName = StringUtils.substringAfterLast(classDotMethod, ".");
		Class<?>[] params = getParameterTypes(label);
		return getAsSignature(Class.forName(className), methodName, params);
	}

	private static Signature getAsSignature(final Class<?> clazz, final String methodName,
			final Class<?>[] parameterTypes) throws ReflectiveOperationException {
		Method method = clazz.getMethod(methodName, parameterTypes);
		return getAsSignature(method);
	}

	private static ConstructorSignature getAsConstructorSignature(final String label)
			throws ReflectiveOperationException {
		String ctor = StringUtils.substringAfterLast(label, " ").trim();
		String className = StringUtils.substringBefore(ctor, "(");
		Class<?>[] params = getParameterTypes(label);
		return getAsConstructorSignature(Class.forName(className), params);
	}

	private static ConstructorSignature getAsConstructorSignature(final Class<?> clazz, final Class<?>[] parameterTypes)
			throws ReflectiveOperationException {
		Constructor<?> ctor = clazz.getConstructor(parameterTypes);
		return getAsSignature(ctor);
	}

	private static Class<?>[] getParameterTypes(final String label) throws ClassNotFoundException {
		String parameters = StringUtils.substringAfter(label, "(");
		parameters = StringUtils.substringBefore(parameters, ")");
		String[] params = StringUtils.split(parameters, ',');
		Class<?>[] parameterTypes = new Class<?>[params.length];
		for (int i = 0; i < params.length; i++) {
			parameterTypes[i] = Class.forName(params[i].trim());
		}
		return parameterTypes;
	}

	/**
	 * Returns true if the given signature is a {@link MethodSignature} and the
	 * return value is not of type 'void'.
	 *
	 * @param signature
	 *            the signature
	 * @return true, if successful
	 * @since 1.3.1
	 */
	public static boolean hasReturnType(final Signature signature) {
		if (signature instanceof MethodSignature) {
			MethodSignature methodSignature = (MethodSignature) signature;
			if (methodSignature.getReturnType() != void.class) {
				return true;
			}
		}
		return false;
	}

}
