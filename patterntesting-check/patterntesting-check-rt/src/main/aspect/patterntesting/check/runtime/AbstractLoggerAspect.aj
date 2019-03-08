/*
 * $Id: AbstractLoggerAspect.aj,v 1.8 2016/12/18 21:59:31 oboehm Exp $
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
 * (c)reated 05.02.2014 by oliver (ob@oasd.de)
 */

package patterntesting.check.runtime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.apache.logging.log4j.*;

import patterntesting.annotation.check.runtime.SuppressLoggerWarning;
import patterntesting.runtime.util.JoinPointHelper;

/**
 * If you use a static {@link Logger} it can happen that you use the logger
 * for the wrong class. This can happen if you copy the create statement from
 * another class and forget to change the classname. E.g. if you copy
 * <pre>
 * private static final Logger log = LogManager.getLogger(MyClass.class);
 * </pre>
 * at the beginning of AnotherClass, you should not forget to change the
 * argument of {@link LogManager#getLogger(Class)} to "AnotherClass.class".
 * <p>
 * This aspect reminds you and prints a warning if you forgot it.
 * </p>
 * 
 * @author oliver
 * @since 1.4.1 (05.02.2014)
 */
@SuppressLoggerWarning
public abstract aspect AbstractLoggerAspect {
    
    private static final Logger log = LogManager.getLogger(AbstractLoggerAspect.class);

    /**
     * Specify what the application code is that should be subject to the
     * pattern test.
     * <br/>
     * Ex: <code>pointcut applicationCode(): within(patterntesting.sample.*)</code>
     */
    public abstract pointcut applicationCode();
    
    /**
     * The usual (and recommended) call to get a {@link Logger} is the call
     * of {@link LogManager#getLogger(Class)}.
     */
    pointcut getClassLogger() :
        (call(public org.apache.logging.log4j.Logger org.slf4j.LogManager.getClassLogger(Class))
        || call(public org.apache.commons.logging.Log org.apache.commons.logging.LogFactory.getLog(Class))
        || call(public org.apache.log4j..Logger org.apache.log4j..Logger.getLogger(Class)))
        && applicationCode();

    /**
     * But sometimes (e.g. if you use the JDK logger) you can only use String
     * as argument.
     */
    pointcut getStringLogger() :
        (call(public org.apache.logging.log4j.Logger org.slf4j.LogManager.getClassLogger(String))
        || call(public org.apache.log4j..Logger org.apache.log4j..Logger.getLogger(String))
        || call(public org.apache.commons.logging.Log org.apache.commons.logging.LogFactory.getLog(String))
        || call(public java.util.logging.Logger java.util.logging.Logger.getLogger(String)))
        && applicationCode();

    /**
     * If {@link LogManager#getLogger(Class)} is called we try to get the
     * class of the caller to verify if it matches the clazz argument.
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    after(Class<?> clazz) : getClassLogger() && args(clazz) {
        check(thisJoinPoint, clazz);
    }
    
    /**
     * If we match a String logger we try to guarantee that the class of the
     * caller is identical to the given String classname (if it *is* a
     * classname).
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    after(String classname) : getStringLogger() && args(classname) {
        check(thisJoinPoint, classname);
    }
    
    private static void check(final JoinPoint jp, final String classname) {
        try {
            check(jp, Class.forName(classname));
        } catch (ClassNotFoundException ex) {
            if (log.isTraceEnabled()) {
                log.trace("Used logger \"{}\" is not a classname because ){}.", classname,
                        ex.getMessage());
            }
        }
    }
    
    private static void check(final JoinPoint jp, final Class<?> clazz) {
        Class<?> callerClass = getCallerOf(jp);
        if (!clazz.equals(callerClass)) {
            Logger classLogger = LogManager.getLogger(callerClass);
            classLogger.warn("For " + JoinPointHelper.getAsShortString(jp) + " " + callerClass
                    + " is expected as argument in {}.", jp.getSourceLocation());
        } else if (log.isTraceEnabled()) {
            log.trace("Argument for {} is {} (as expected).", JoinPointHelper.getAsShortString(jp),
                    clazz);
        }
    }
 
    private static Class<?> getCallerOf(final JoinPoint jp) {
        Object caller = jp.getThis();
        if (caller == null) {
            return JoinPointHelper.getCallerClass(AbstractLoggerAspect.class, LoggerAspect.class,
                    JoinPointHelper.class);
        } else {
            return caller.getClass();
        }
    }

}

