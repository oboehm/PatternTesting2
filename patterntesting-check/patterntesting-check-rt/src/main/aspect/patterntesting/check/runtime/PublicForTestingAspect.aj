/*
 * Copyright (c) 2009-2019 by Oliver Boehm
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
 * (c)reated 18.03.2009 by oliver (ob@aosd.de)
 */
package patterntesting.check.runtime;

import junit.framework.TestCase;

import org.aspectj.lang.*;
import org.aspectj.lang.annotation.SuppressAjWarnings;

import patterntesting.annotation.check.ct.*;
import patterntesting.annotation.check.runtime.*;
import patterntesting.runtime.util.*;

/**
 * Methods and constructors marked as "@PublicForTesting" should be only called
 * by test methods or by the class itself. Test methods are recognized by
 * the @Test annotation (from JUnit) or @OnlyForTesting annotation.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.4 $
 * @since 18.03.2009
 */
public aspect PublicForTestingAspect {

    /**
     * JUnit5 test or setup/teardown methods.
     */
    private pointcut belowJUnit5Methods() :
            cflowbelow(execution(@org.junit.jupiter.api.Test * *..*()))
                    || cflowbelow(execution(@org.junit.jupiter.api.BeforeEach * *..*()))
                    || cflowbelow(execution(@org.junit.jupiter.api.BeforeAll * *..*()))
                    || cflowbelow(execution(@org.junit.jupiter.api.AfterEach * *..*()))
                    || cflowbelow(execution(@org.junit.jupiter.api.AfterAll * *..*()))
            ;

    /**
     * JUnit test or setup/teardown methods and methods marked with 
     * <code>@OnlyForTesting</code>.
     */
    private pointcut belowTestMethods() :
        belowJUnit5Methods()
        || cflowbelow(execution(@OnlyForTesting * *..*()))
        || cflowbelow(execution(@OnlyForTesting *..new()))
        || cflowbelow(@within(OnlyForTesting))
        ;

    /**
     * All methods and constructors annotated with "@PublicForTesting" should
     * be called only from test methods or from the class itself.
     */
    public pointcut applicationCode() :
        (call(@PublicForTesting * *..*(..))
            || call(@PublicForTesting *..new(..)))
        && !belowTestMethods()
        ;

    /**
     * We check the caller hierarchy after the pointcut "applicationCode()"
     * because otherwise the stacktrace from the failed assert points to
     * the line before the call (and not the call itself).
     * As a result the protected method is executed but you see the
     * AssertionError afterwards.
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    after() : applicationCode()  {
        if (Assertions.ENABLED) {
            String caller = getCallerClassName();
            Signature sig = thisJoinPointStaticPart.getSignature();
            String self = sig.getDeclaringTypeName();
            assert self.equals(caller) :
                "only test methods or object itself may call "
                + JoinPointHelper.getAsShortString(thisJoinPoint);
        }
    }

    /**
     * The 4th stack element was experimentally guessed and my be wrong
     * in other circumstances. The 3rd stack element is this aspect
     * (PublicForTestingAspect) itself.
     *
     * @return the caller of the protected method (actually the caller of
     *         this aspect here)
     */
    private static String getCallerClassName() {
        Thread t = Thread.currentThread();
        StackTraceElement[] stacktrace = t.getStackTrace();
        return stacktrace[4].getClassName();
    }

}
