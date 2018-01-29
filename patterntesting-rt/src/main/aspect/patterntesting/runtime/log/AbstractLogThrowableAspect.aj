/**
 * $Id: AbstractLogThrowableAspect.aj,v 1.3 2016/12/18 20:19:38 oboehm Exp $
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

import org.apache.logging.log4j.*;
import org.aspectj.lang.JoinPoint;

import patterntesting.runtime.util.JoinPointHelper;

/**
 * Abstract aspect to handle logging stuff.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 16.12.2008
 */
public abstract aspect AbstractLogThrowableAspect {

    /**
     * To get the Aspect specific logger.
     * @return the logger
     */
    public abstract Logger getLog();

    /**
     * Log throwing.
     *
     * @param level the level
     * @param t the t
     * @param jp the jp
     */
    public void logThrowing(int level, Throwable t, JoinPoint jp) {
        logThrowing(getLog(), level, t, jp);
    }

    /**
     * Log throwing.
     *
     * @param log the log
     * @param level the level
     * @param t the t
     * @param jp the jp
     */
    public static void logThrowing(Logger log, int level, Throwable t, JoinPoint jp) {
        String msg = t.getClass().getSimpleName() + " in " + toString(jp);
        switch(level) {
        case SimpleLog.LOG_LEVEL_TRACE :
            log.trace(msg, t);
            break;
        case SimpleLog.LOG_LEVEL_DEBUG :
            log.debug(msg, t);
            break;
        case SimpleLog.LOG_LEVEL_INFO :
            log.info(msg, t);
            break;
        case SimpleLog.LOG_LEVEL_ERROR :
            log.error(msg, t);
            break;
        case SimpleLog.LOG_LEVEL_FATAL :
            log.error(msg, t);
            break;
        default :
            log.warn(msg, t);
            break;
        }
    }

    private static String toString(JoinPoint jp) {
        return jp.getSignature().getName()
                + JoinPointHelper.getArgsAsString(jp.getArgs());
    }

}
