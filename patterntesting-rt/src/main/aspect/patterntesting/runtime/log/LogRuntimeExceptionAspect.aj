/*
 * $Id: LogRuntimeExceptionAspect.aj,v 1.3 2016/12/18 20:19:38 oboehm Exp $
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
 * (c)reated 09.10.2008 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.aspectj.lang.annotation.SuppressAjWarnings;

import patterntesting.runtime.annotation.LogRuntimeException;

/**
 * Are you using Spring Webflow? And have you sometimes seen an exception like
 * this:
 * <pre>
 * Caused by: ognl.MethodFailedException: Method "setSomething" failed for object MyBean ...
 *	at ognl.OgnlRuntime.callAppropriateMethod(OgnlRuntime.java:837)
 *	at ognl.ObjectMethodAccessor.callMethod(ObjectMethodAccessor.java:61)
 *	at ognl.OgnlRuntime.callMethod(OgnlRuntime.java:860)
 *	at ognl.ASTMethod.getValueBody(ASTMethod.java:73)
 *	at ognl.SimpleNode.evaluateGetValueBody(SimpleNode.java:170)
 *	at ognl.SimpleNode.getValue(SimpleNode.java:210)
 *	at ognl.ASTChain.getValueBody(ASTChain.java:109)
 *	at ognl.SimpleNode.evaluateGetValueBody(SimpleNode.java:170)
 *	at ognl.SimpleNode.getValue(SimpleNode.java:210)
 *	at ognl.Ognl.getValue(Ognl.java:333)
 *	at org.springframework.binding.expression.ognl.OgnlExpression.getValue(OgnlExpression.java:81)
 *	... 57 more
 * </pre>
 * You don't see the cause for the failure so what you need is a logging of
 * each RuntimeException, or? Mark your class with @LogRuntimeException and
 * you will see it in the log.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 09.10.2008
 * @version $Revision: 1.3 $
 */
public aspect LogRuntimeExceptionAspect extends AbstractLogThrowableAspect {

    /** the log object. */
    protected Logger log = LogManager.getLogger(LogRuntimeExceptionAspect.class);

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
     * The application code
     */
	public pointcut applicationCode() :
		within(@LogRuntimeException *);

	pointcut allMethods() :
		execution(*..*.new(..)) || execution(* *..*.*(..));

	/**
	 * Logs a thrown RuntimeException.
	 *
	 * @param a the annotation with the message
	 * @param e the thrown RuntimeException
	 */
    @SuppressAjWarnings({"adviceDidNotMatch"})
	after(LogRuntimeException a) throwing(RuntimeException e) :
			allMethods() && @annotation(a) {
	    logThrowing(a.value(), e, thisJoinPoint);
	}

	/**
	 * Unfortunately we can't combine the pointcuts with "@this(a)" to get the
	 * annotation. Why? Because for static methods inside an annotated class we
	 * don't have a "this"!
	 *
	 * @param e the thrown RuntimeException
	 */
	@SuppressWarnings("unchecked")
    @SuppressAjWarnings({"adviceDidNotMatch"})
    after() throwing(RuntimeException e) :
			allMethods()
			&& applicationCode()
			&& !@annotation(LogRuntimeException) {
        LogRuntimeException a = (LogRuntimeException) thisJoinPointStaticPart.getSignature()
                .getDeclaringType().getAnnotation(LogRuntimeException.class);
		logThrowing(a.value(), e, thisJoinPoint);
	}

}
