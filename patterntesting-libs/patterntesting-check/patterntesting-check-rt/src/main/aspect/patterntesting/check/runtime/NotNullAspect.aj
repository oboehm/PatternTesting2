/**
 * $Id: NotNullAspect.aj,v 1.7 2016/02/14 11:14:53 oboehm Exp $
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
 * (c)reated 06.12.2008 by oliver (ob@oasd.de)
 */
package patterntesting.check.runtime;

import java.lang.annotation.Annotation;

import org.aspectj.lang.*;
import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.aspectj.lang.reflect.*;

import javax.validation.constraints.NotNull;

import patterntesting.runtime.util.JoinPointHelper;

/**
 * In contradiction to the NullPointerTrap which forbids all null arguments
 * you can use a more relax strategy and mark only these parameter (and
 * attributes) as "@NotNull" which should never be null.
 * <p>
 * Note: The support for patterntesting.annotation.check.runtime.NotNull will
 * be removed in 1.6.
 * </p>
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 0.8
 */
public aspect NotNullAspect {

    pointcut setNonNullAttribute() :
        set(@NotNull * *..*.*)
        ;

    pointcut ctorWithNotNullArg() :
        execution(*..*.new(@NotNull (*)))
        ;

    pointcut ctorWithNotNullArgs() :
        execution(*..*.new(@NotNull (*), ..))
        || execution(*..*.new(.., @NotNull (*)))
        || execution(*..*.new(.., @NotNull (*), ..))
        ;

    pointcut methodsWithNotNullArg() :
        execution(* *..*.*(@NotNull (*)))
        ;

    pointcut methodsWithNotNullArgs() :
        execution(* *..*.*(@NotNull (*), ..))
        || execution(* *..*.*(.., @NotNull (*)))
        || execution(* *..*.*(.., @NotNull (*), ..))
        ;

    pointcut notNullReturnMethods() :
        execution(@NotNull * *..*.*(..))
        && !execution(void *..*.*(..))
        ;

    /**
     * Checks the setting of an attribute.
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    before(Object arg) : setNonNullAttribute() && args(arg) {
        if (arg == null) {
            throw new AssertionError("@NotNull constraint violated: "
                    + thisJoinPoint.getSignature().toShortString() + "=null");
        }
    }

    /**
     * Checks the arguments of a constructor or method if they are null.
     * @param arg
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    before(Object arg) : (ctorWithNotNullArg() || methodsWithNotNullArg())
            && args(arg) {
        if (arg == null) {
            throw new AssertionError("@NotNull constraint violated: "
                    + JoinPointHelper.getAsShortString(thisJoinPoint));
        }
    }

    /**
     * Checks the arguments of a constructor or method if they are null.
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    before() : ctorWithNotNullArgs() || methodsWithNotNullArgs() {
        Signature sig = thisJoinPoint.getSignature();
        if (!(sig instanceof CodeSignature)) {
            return;
        }
        Annotation[][] paramAnnotations = JoinPointHelper
                .getParameterAnnotations(thisJoinPoint);
        assertArgsNotNull(thisJoinPoint, paramAnnotations);
    }

    /**
     * Checks the return value of a non-void method.
     * 
     * @param x the return value
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    after() returning (Object x): notNullReturnMethods() {
        if (x == null) {
            throw new AssertionError("@NotNull constraint violated: "
                    + JoinPointHelper.getAsShortString(thisJoinPoint)
                    + " returns null");
        }
    }

    /////   some local helper   ///////////////////////////////////////////////

    private static void assertArgsNotNull(final JoinPoint joinPoint,
            Annotation[][] paramAnnotations) {
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            if ((args[i] == null) && hasNotNullAnnotation(paramAnnotations[i])) {
                throw new AssertionError("@NotNull constraint violated: "
                        + JoinPointHelper.getAsShortString(joinPoint));
            }
        }
    }

    private static boolean hasNotNullAnnotation(final Annotation[] annotations) {
        if (annotations == null) {
            return false;
        }
        for (Annotation annotation : annotations) {
            if (annotation instanceof NotNull) {
                return true;
            }
        }
        return false;
    }

}
