/*
 * $Id: JoinPointHelper.java,v 1.24 2016/12/18 20:19:36 oboehm Exp $
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
 * (c)reated 23.10.2008 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.*;

import patterntesting.annotation.check.runtime.MayReturnNull;
import patterntesting.annotation.check.runtime.NullArgsAllowed;
import patterntesting.runtime.annotation.DontLogMe;

/**
 * The Class JoinPointHelper.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.24 $
 * @since 23.10.2008
 */
public final class JoinPointHelper {

	private static Pattern[] ADVICE_PATTERNS = { Pattern.compile("ajc\\$.*"), Pattern.compile(".*\\$_aroundBody.*") };

	/** Utility class - no need to instantiate it */
	private JoinPointHelper() {
	}

	/**
	 * Gets the given joinpoint as string.
	 *
	 * @param joinpoint
	 *            the joinpoint
	 *
	 * @return the as string
	 */
	public static String getAsString(final JoinPoint joinpoint) {
		Signature sig = joinpoint.getSignature();
		Object[] args = joinpoint.getArgs();
		if (sig instanceof FieldSignature) {
			if (args.length == 0) {
				return "get " + sig.toShortString();
			} else {
				return "set " + sig.toShortString() + " = " + getArgAsString(args[0]);
			}
		} else if (sig instanceof CodeSignature) {
			Annotation[][] paramAnnotations = getParameterAnnotations(joinpoint);
			return SignatureHelper.getAsString(sig.getDeclaringTypeName(), sig)
					+ getArgsAsString(args, paramAnnotations);
		} else {
			return SignatureHelper.getAsString(sig.getDeclaringTypeName(), sig) + getArgsAsString(args);
		}
	}

	/**
	 * Gets the as short string.
	 *
	 * @param joinpoint
	 *            the joinpoint
	 *
	 * @return the as short string
	 */
	public static String getAsShortString(final JoinPoint joinpoint) {
		Signature sig = joinpoint.getSignature();
		if (sig instanceof FieldSignature) {
			return getAsString(joinpoint);
		}
		Object[] args = joinpoint.getArgs();
		if (sig instanceof CatchClauseSignature) {
			return SignatureHelper.getAsString(sig.getDeclaringType().getSimpleName(), sig) + "("
					+ args[0].getClass().getName() + ")";
		}
		return SignatureHelper.getAsString(sig.getDeclaringType().getSimpleName(), sig) + getArgsAsShortString(args);
	}

	/**
	 * Gets the as long string.
	 *
	 * @param joinpoint
	 *            the joinpoint
	 *
	 * @return the as short string
	 */
	public static String getAsLongString(final JoinPoint joinpoint) {
		Signature sig = joinpoint.getSignature();
		if (sig instanceof FieldSignature) {
			return getAsString(joinpoint);
		}
		Object[] args = joinpoint.getArgs();
		return SignatureHelper.getAsString(sig.getDeclaringType().getName(), sig) + getArgsAsString(args);
	}

	/**
	 * Gets the parameter annotations.
	 *
	 * @param joinpoint
	 *            should be a Method- or Constructor-Signature
	 *
	 * @return the annotations of a method or constructor, otherwise null
	 */
	@MayReturnNull
	public static Annotation[][] getParameterAnnotations(final JoinPoint joinpoint) {
		Signature sig = joinpoint.getSignature();
		if (sig instanceof MethodSignature) {
			Method method = ((MethodSignature) sig).getMethod();
			if (method == null) {
				return new Annotation[0][0];
			}
			return method.getParameterAnnotations();
		} else if (sig instanceof ConstructorSignature) {
			ConstructorSignature ctorSig = (ConstructorSignature) sig;
			return getParameterAnnotations(ctorSig);
		} else {
			return new Annotation[0][0];
		}
	}

	/**
	 * Normally you get an annotation array with the size of the constructor
	 * parameters. But not for inner classes. Here the first parameter of the
	 * constructor is the embedded class. In this case we must correct the
	 * annotation array
	 *
	 * @param ctorSig
	 * @return
	 */
	@MayReturnNull
	private static Annotation[][] getParameterAnnotations(final ConstructorSignature ctorSig) {
		Constructor<?> ctor = ctorSig.getConstructor();
		Annotation[][] annotations = ctor.getParameterAnnotations();
		if (isInnerClass(ctorSig)) {
			Annotation[][] corrected = new Annotation[annotations.length + 1][0];
			corrected[0] = null;
			System.arraycopy(annotations, 0, corrected, 1, annotations.length);
			return corrected;
		}
		return annotations;
	}

	/**
	 * Returns the annotation for the given JoinPoint.
	 *
	 * @param jp
	 *            the JoinPoint
	 * @param annotationClass
	 *            the wanted annotation
	 * @return the annotation (if found) or null
	 * @since 1.0
	 */
	@MayReturnNull
	public static Annotation getClassAnnotation(final JoinPoint jp, final Class<? extends Annotation> annotationClass) {
		Class<?> thisClass = jp.getSignature().getDeclaringType();
		assert thisClass != null;
		return thisClass.getAnnotation(annotationClass);
	}

	/**
	 * Gets the args as string.
	 *
	 * @param args
	 *            the args
	 *
	 * @return the args as string
	 */
	@NullArgsAllowed
	public static String getArgsAsString(final Object[] args) {
		if ((args == null) || (args.length == 0)) {
			return "()";
		}
		StringBuilder sb = new StringBuilder("(");
		sb.append(getArgAsString(args[0]));
		for (int i = 1; i < args.length; i++) {
			sb.append(", ");
			sb.append(getArgAsString(args[i]));
		}
		sb.append(")");
		return sb.toString();
	}

	@NullArgsAllowed
	private static String getArgsAsString(final Object[] args, final Annotation[][] annotations) {
		if ((args == null) || (args.length == 0)) {
			return "()";
		}
		StringBuilder sb = new StringBuilder("(");
		sb.append(getArgAsString(args[0], annotations[0]));
		for (int i = 1; i < args.length; i++) {
			sb.append(", ");
			sb.append(getArgAsString(args[i], annotations[i]));
		}
		sb.append(")");
		return sb.toString();
	}

	/**
	 * Gets the args as short string. If the resulting argument string will be
	 * too long it will be abbreviated.
	 *
	 * @param args
	 *            the args
	 * @return the args as short string
	 * @since 1.4.1
	 */
	@NullArgsAllowed
	public static String getArgsAsShortString(final Object[] args) {
		String shortString = getArgsAsString(args);
		if (shortString.length() < 20) {
			return shortString;
		}
		StringBuilder sb = new StringBuilder("(");
		sb.append(getArgAsShortString(args[0]));
		int n = args.length;
		if (n > 3) {
			n = 3;
		}
		for (int i = 1; i < n; i++) {
			sb.append(", ");
			sb.append(getArgAsShortString(args[i]));
		}
		if (args.length > 3) {
			sb.append(", ..");
		}
		sb.append(")");
		return sb.toString();
	}

	/**
	 * Gets the arg as string.
	 *
	 * @param obj
	 *            the obj
	 *
	 * @return the arg as string
	 */
	@NullArgsAllowed
	public static String getArgAsString(final Object obj) {
		if (obj == null) {
			return "(null)";
		}
		if (obj instanceof String) {
			return "\"" + obj + "\"";
		}
		return obj.toString();
	}

	/**
	 * Gets the arg as short string.
	 *
	 * @param obj
	 *            the obj
	 * @return the arg as short string
	 * @since 1.4.1
	 */
	@NullArgsAllowed
	public static String getArgAsShortString(final Object obj) {
		if (obj == null) {
			return "(null)";
		}
		String shortString = Converter.toShortString(obj);
		if (obj instanceof String) {
			return "\"" + shortString + "\"";
		}
		return shortString;
	}

	@NullArgsAllowed
	private static String getArgAsString(final Object obj, final Annotation[] annotations) {
		if (hasDontLogMeAnnotation(annotations)) {
			return "*";
		} else {
			return getArgAsString(obj);
		}
	}

	private static boolean hasDontLogMeAnnotation(final Annotation[] annotations) {
		if (annotations == null) {
			return false;
		}
		for (Annotation annotation : annotations) {
			if (annotation instanceof DontLogMe) {
				return true;
			}
		}
		return false;
	}

	/**
	 * The Java compiler generates something like OuterClass$InnerClass as name
	 * for the inner class. So if a name contains a '$' it is probably an inner
	 * class.
	 *
	 * @param jp
	 *            the jp
	 *
	 * @return true if joinpoint belongs to an inner class
	 */
	public static boolean isInnerClass(final JoinPoint jp) {
		return isInnerClass(jp.getSignature());
	}

	/**
	 * The Java compiler generates something like OuterClass$InnerClass as name
	 * for the inner class. So if a name contains a '$' it is probably an inner
	 * class.
	 *
	 * @param sig
	 *            the sig
	 *
	 * @return true if signature belongs to an inner class
	 */
	public static boolean isInnerClass(final Signature sig) {
		String typeName = sig.getDeclaringTypeName();
		return typeName.contains("$");
	}

	/**
	 * Gets the caller class by examing the stacktrace.
	 * <p>
	 * Sometime the caller of the method is an aspect. Because normally you do
	 * not want the aspect it is filtered out. If you really want it call
	 * {@link #getCallerOf(JoinPoint, Pattern...)} direct with an empty pattern
	 * argument.
	 * </p>
	 *
	 * @return the caller of a joinpoint
	 * @see #getCallerOf(JoinPoint, Pattern...)
	 */
	public static Class<?> getCallerClass() {
		return StackTraceScanner.getCallerClass(ADVICE_PATTERNS, JoinPointHelper.class);
	}

	/**
	 * Gets the caller class by examing the stacktrace.
	 * <p>
	 * Sometime the caller of the method is an aspect. With this method you have
	 * the chance to list the aspects which should be ignored.
	 * </p>
	 *
	 * @param excluded
	 *            the excluded
	 * @return the caller of a joinpoint
	 */
	public static Class<?> getCallerClass(final Class<?>... excluded) {
		return StackTraceScanner.getCallerClass(ADVICE_PATTERNS, excluded);
	}

	/**
	 * Gets the caller of the given joinpoint. For a call joinpoint you have
	 * "this" as attribute which gives you the caller. But for an execution
	 * joinpoint "this" is not the caller but the object of execution. If you
	 * want the caller of the excution joinpoint call this method here.
	 * <p>
	 * Sometime the caller of the method is an aspect. Because normally you do
	 * not want the aspect it is filtered out. If you really want it call
	 * {@link #getCallerOf(JoinPoint, Pattern...)} direct with an empty pattern
	 * argument.
	 * </p>
	 *
	 * @param jp
	 *            the (execution) joinpoint
	 * @return the caller of the given joinpoint
	 * @see #getCallerOf(JoinPoint, Pattern...)
	 */
	public static StackTraceElement getCallerOf(final JoinPoint jp) {
		return getCallerOf(jp, ADVICE_PATTERNS);
	}

	/**
	 * Gets the caller of the given joinpoint. For a call joinpoint you have
	 * "this" as attribute which gives you the caller. But for an execution
	 * joinpoint "this" is not the caller but the object of execution. If you
	 * want the caller of the excution joinpoint call this method here.
	 *
	 * @param jp
	 *            the (execution) joinpoint
	 * @param excluded
	 *            a list of filters which should be not considered as caller
	 * @return the caller of
	 */
	public static StackTraceElement getCallerOf(final JoinPoint jp, final Pattern... excluded) {
		return StackTraceScanner.getCallerOf(jp.getSignature(), excluded);
	}

}
