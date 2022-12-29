/*
 * Copyright (c) 2007-2020 by Oliver Boehm
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
 * (c)reated 11.12.2007 by oliver (ob@oasd.de)
 */
package patterntesting.check.runtime;

import org.slf4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.aspectj.lang.reflect.ConstructorSignature;
import patterntesting.annotation.check.runtime.MayBeNull;
import patterntesting.runtime.util.Assertions;
import patterntesting.runtime.util.JoinPointHelper;

import java.lang.annotation.Annotation;

/**
 * In the AbstractNullTest aspect we log only if a null value is given to a
 * method. The NullPointerTrap here is much stricter: it <b>requires</b>
 * that
 * <ul>
 * 	<li>no null value is given</li>
 * 	<li>null as return value is forbidden</li>
 * 	<li>if one of this requirements are hurt an AssertionExecption is
 * 		thrown.
 * 	</li>
 * </ul>
 * As in AbstractNullTest you can define exceptions from these strict rules
 * by defining the abstract pointcuts <i>applicationCode</i>,
 * <i>nullArgsAllowed</li> and <i>mayReturnNull</i>.
 * 
 * This aspect is intended more for finding bugs in the test phase whereas
 * AbstractNullTest can be also used during production because it logs only
 * the problematic calls with a null parameter.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.5 $
 * @since 11.12.2007
 */
public abstract aspect AbstractNullPointerTrap {

    /** The Constant assertEnabled. */
    protected static final boolean assertEnabled = Assertions.areEnabled();

    /**
     * To get the aspect specific logger.
     * @return the logger
     */
    public abstract Logger getLog();

	/**
	 * Specify what the application code is that should be subject to the
	 * pattern test.
	 * <br/>
	 * Ex: <code>pointcut applicationCode(): within(patterntesting.sample.*)</code>
	 */
	public abstract pointcut applicationCode();

	/**
	 * Specify which code is allowed to have <code>null</code> parameters.
	 * <br/>
	 * Ex: <code>execution(@NullArgsAllowed * *..*(*, ..))
     *     || execution(@NullArgsAllowed *..new(*, ..))</code>
	 */
	public abstract pointcut nullArgsAllowed();

	/**
	 * Specify which methods may return <code>null</code>.
	 * Some methods (like find methods or getters) may return a null value.
	 * <br/>
	 * Ex: <code>execution(@MayReturnNull Object+ *..*(..))</code>
	 */
	public abstract pointcut mayReturnNull();
	
	/**
	 * Addresses all methods (or constructors) with at least one argument which
	 * should not be given as null parameter.
	 * For performance reason this is limited only to public methods
	 * (or constructors)
	 */
	pointcut invalidNullParameter() :
		(execution(public * *..*(*, ..)) || execution(public *..new(*, ..)))
		&& applicationCode()
		&& !nullArgsAllowed()
		&& !within(*NullPointerTrap);
	
	/**
	 * If a (null) parameter is detected something is wrong with the
	 * application code.
	 * <p>
	 * Inner classes will be skipped because they are
	 * handled different by the compiler (for private static inner classes
	 * it adds an additional argument for the default constructor. But this
	 * additional argument is always null).
	 * </p>
	 * <p>
	 * Also some methods from the Object class (e.g. the equals method) are
	 * also skipped because these methods are prepared for null arguments.
	 * </p>
	 */
	@SuppressAjWarnings({"adviceDidNotMatch"})
	before() : invalidNullParameter() {
	    if (!assertEnabled) {
	        return;
	    }
	    Signature sig = thisJoinPoint.getSignature();
	    if ((sig instanceof ConstructorSignature)
	            && (JoinPointHelper.isInnerClass(sig))) {
	        if (getLog().isTraceEnabled()) {
	            getLog().trace("check for " + sig + " is skipped (inner class)");
	        }
	        return;
	    }
        Annotation[][] paramAnnotations = JoinPointHelper
                .getParameterAnnotations(thisJoinPoint);
        assertArgsNotNull(thisJoinPoint, paramAnnotations);
	}
	
	
	
	/**
	 * To check all non void methods if returns a null value we first define
	 * the pointcut here.
	 * For performance reason this is limited to public methods.
	 */
	private pointcut nonVoidMethods() :
		execution(public java.lang.Object+ *..*(..))
		&& applicationCode()
		&& !mayReturnNull()
		&& !within(*NullPointerTrap);
	
	/**
	 * A 'null' as return value is often an indication that something is wrong
	 * (otherwise an exception should be thrown)
	 */
	@SuppressAjWarnings({"adviceDidNotMatch"})
    after() returning(Object returned) : nonVoidMethods() {
        assert returned != null : thisJoinPoint.getSignature()
            + " returns null!";
    }

    /////   some local helper   ///////////////////////////////////////////////

    private static void assertArgsNotNull(final JoinPoint joinPoint,
            Annotation[][] paramAnnotations) {
    	if (canHaveNullArgs(joinPoint)) {
    		return;
		}
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            if ((args[i] == null) && !hasMayBeNullAnnotation(paramAnnotations[i])) {
                assert args[i] != null : "arg" + i + " is null";
            }
        }
    }

    private static boolean canHaveNullArgs(JoinPoint joinPoint) {
    	return "equals".equals(joinPoint.getSignature().getName());
	}

    private static boolean hasMayBeNullAnnotation(final Annotation[] annotations) {
        if (annotations == null) {
            return false;
        }
        for (Annotation annotation : annotations) {
            if (annotation instanceof MayBeNull) {
                return true;
            }
        }
        return false;
    }

}
