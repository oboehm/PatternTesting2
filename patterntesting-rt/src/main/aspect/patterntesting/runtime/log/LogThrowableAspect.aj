/*
 * $Id: LogThrowableAspect.aj,v 1.3 2016/12/18 20:19:38 oboehm Exp $
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
 * (c)reated 16.12.2008 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.aspectj.lang.annotation.SuppressAjWarnings;

import patterntesting.runtime.annotation.LogThrowable;

/**
 * It is so easy with JUnit 4 to test a method where you expect an exception.
 * All you have to do is to annotate the method with
 * <code>@Test(expected=ExpectedException.class)</code>.
 * But sometime you want to see this exception in the log.
 * Now you can mark it with <code>LogThrowable</code> and you will see it in
 * the log as warn message. If you want to see it as other log level append it
 * as value, e.g. <code>LogThrowable(SimpleLog.LOG_LEVEL_INFO)</code>.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 16.12.2008
 * @version $Revision: 1.3 $
 */
public aspect LogThrowableAspect extends AbstractLogThrowableAspect {

    declare precedence : *, LogRuntimeExceptionAspect, LogThrowableAspect;

    private Logger log = LogManager.getLogger(LogRuntimeExceptionAspect.class);

    /**
     * For testing you can set the logger.
     * @param newLog the new logger
     */
    protected final void setLog(final Logger newLog) {
        log = newLog;
    }

    /**
     * @return the logger (the abstract aspect wants it)
     */
    @Override
    public Logger getLog() {
        return log;
    }

    /**
     * These are the methods which has the @LogThrowable annotation.
     *
     * @param a the annotation
     */
    public pointcut annotatedMethods(LogThrowable a) :
        (execution(@LogThrowable *..*.new(..))
        || execution(* @LogThrowable *..*.*(..)))
        && @annotation(a);
        ;

    /**
     * These are all methods inside a class annotated with @LogThrowable.
     */
    public pointcut methodsInsideAnnotatedClass() :
        (execution(*..*.new(..)) || execution(* *..*.*(..)))
        && !annotatedMethods(LogThrowable)
        && @within(LogThrowable)
        ;

    /**
     * Logs a thrown exception.
     *
     * @param a the annotation with the message
     * @param e the thrown exception
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    after(LogThrowable a) throwing(Throwable t) :
            annotatedMethods(a) {
        logThrowing(a.value(), t, thisJoinPoint);
    }

    /**
     * Unfortunately we can't combine the pointcuts with "@this(a)" to get the
     * annotation. Why? Because for static methods inside an annotated class we
     * don't have a "this"!
     *
     * @param t the thrown exception
     */
    @SuppressWarnings("unchecked")
    @SuppressAjWarnings({"adviceDidNotMatch"})
    after() throwing(Throwable t) :
            methodsInsideAnnotatedClass() {
        LogThrowable a = (LogThrowable) thisJoinPointStaticPart.getSignature().getDeclaringType()
                .getAnnotation(LogThrowable.class);
        logThrowing(a.value(), t, thisJoinPoint);
    }

}
