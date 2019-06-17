/**
 * $Id: AbstractTestThreadAspect.aj,v 1.2 2016/12/18 21:56:49 oboehm Exp $
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
 * (c)reated 01.12.2008 by oliver (ob@oasd.de)
 */
package patterntesting.concurrent;

import java.util.Random;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.apache.logging.log4j.*;

import patterntesting.annotation.check.runtime.NullArgsAllowed;
import patterntesting.runtime.util.*;

/**
 * This aspect provides some advices which put some sleeps between the given
 * pointcuts. This will interrupt the thread and gives other threads the chance
 * to get active.
 * To watch the different threads it traces the instrumented code if the
 * log level is on TRACE.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 0.8
 */
public abstract aspect AbstractTestThreadAspect {

    private static Random random = new Random();

    /**
     * To get the aspect specific logger.
     * @return the logger
     */
    public abstract Logger getLog();

    /**
     * Specify what the application code is that should be subject to the
     * pattern test.
     * <br/>
     * Ex: <code>pointcut applicationCode(): within(@TestThread *)</code>
     */
    public abstract pointcut applicationCode();

    /**
     * Only call is relevant because constructor is executed normally not
     * multi-threaded.
     */
    pointcut constructors() :
        call(*..*.new(..));

    pointcut voidMethods() :
        execution(void *..*.*(..)) || execution(static void *..*.*(..))
        || call(void *..*.*(..)) || call(static void *..*.*(..))
        ;

    pointcut allMethods() :
        execution(* *..*.*(..)) || execution(static * *..*.*(..))
        || call(* *..*.*(..)) || call(static * *..*.*(..));

    pointcut nonVoidMethods() :
        allMethods() && !voidMethods();

    pointcut setAttributes() :
         set(* *..*);

    pointcut getAttributes() :
        get(* *..*);

    pointcut accessAttributes() :
        getAttributes() || setAttributes();

    pointcut localCode() :
        within(AbstractTestThreadAspect)
        || cflow(execution(* AbstractTestThreadAspect.*(..)));

    /**
     * Let the thread sleep before each joinpoint to give other threads a chance
     * to start or continue.
     * <br/>
     * NOTE: normally a Thread.yield should do the same but it does not work
     *       on all VMs.
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    before() :
            (constructors() || allMethods() || accessAttributes())
            && applicationCode()
            && !localCode() {
        enter(thisJoinPoint);
        yield();
    }

    /**
     * Let the thread sleep after each joinpoint to give other threads a chance
     * to start or continue.
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    after() : (constructors() || voidMethods() || setAttributes())
            && applicationCode()
            && !localCode() {
        yield();
        leave(thisJoinPoint);
    }

    /**
     * Let the thread sleep after each joinpoint to give other threads a chance
     * to start or continue.
     * <br/>
     * If tracing is enabled you'll see also the return value in the log.
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    after() returning(Object ret) :
            (nonVoidMethods() || getAttributes()) && applicationCode()
            && !localCode() {
        yield();
        leave(thisJoinPoint, ret);
    }

    /**
     * Let the thread sleep after each joinpoint to give other threads a chance
     * to start or continue.
     * <br/>
     * If tracing is enabled you'll see also the thrown exception in the log.
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    after() throwing (Throwable t) :
            (constructors() || allMethods() || accessAttributes())
            && applicationCode()
            && !localCode() {
        yield();
        leave(thisJoinPoint, t);
    }



    /////   s o m e   p r i v a t e   h e l p e r s   /////////////////////////

    /**
     * The normal yield is only a hint for the scheduler to change to another
     * thread. But on Linux it does not work also after the priority of the
     * thread was removed to the mininmum.
     * So the default implemation for our "yield" is a simple (but random)
     * sleep.
     */
    private static void yield() {
        int t = random.nextInt(20);
        ThreadUtil.sleep(t);
    }

    private static ThreadLocal<StringBuffer> indent = new ThreadLocal<StringBuffer>() {
        @Override protected StringBuffer initialValue() {
            return new StringBuffer("");
        }
    };

    private void enter(JoinPoint joinpoint) {
        if (getLog().isTraceEnabled()) {
            getLog().trace(getIndent() + "> " + JoinPointHelper.getAsString(joinpoint) + "...");
            increaseIndent();
        }
    }

    private void leave(JoinPoint joinpoint) {
        if (getLog().isTraceEnabled()) {
            decreaseIndent();
            getLog().trace(getIndent() + "< " + JoinPointHelper.getAsString(joinpoint));
        }
    }

    private void leave(JoinPoint joinpoint, Throwable t) {
        if (getLog().isTraceEnabled()) {
            decreaseIndent();
            getLog().trace(getIndent() + "<*" + JoinPointHelper.getAsString(joinpoint) + " *** " + t);
        }
    }

    @NullArgsAllowed
    private void leave(JoinPoint joinpoint, Object returned) {
        if (getLog().isTraceEnabled()) {
            decreaseIndent();
            String msg = getIndent() + "< " + JoinPointHelper.getAsString(joinpoint);
            if (returned != null) {
                msg += " = " + returned;
            }
            getLog().trace(msg);
        }
    }

    private static String getIndent() {
        return indent.get().toString();
    }

    private static void increaseIndent() {
        indent.get().append("| ");
    }

    private static void decreaseIndent() {
        indent.get().delete(0, 2);
    }

}
