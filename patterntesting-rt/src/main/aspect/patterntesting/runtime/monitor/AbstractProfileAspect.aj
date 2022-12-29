/**
 * $Id: AbstractProfileAspect.aj,v 1.3 2016/12/18 20:19:41 oboehm Exp $
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
 * (c)reated 22.12.2008 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.monitor;

import org.slf4j.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.SuppressAjWarnings;

import patterntesting.runtime.util.JoinPointHelper;

/**
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 22.12.2008
 * @version $Revision: 1.3 $
 */
public abstract aspect AbstractProfileAspect {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractProfileAspect.class);

    /**
     * To get the aspect specific logger.
     * @return the logger
     */
    public abstract Logger getLog();

    /**
     * Application code.
     */
    public abstract pointcut applicationCode();

    /**
     * Measure the time for a method.
     *
     * @return the original return value of the method
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    Object around() : applicationCode() {
        Signature sig = thisJoinPoint.getSignature();
        LOG.trace("Profiling {}...", sig);
        ProfileMonitor mon = ProfileStatistic.start(sig);
        try {
            return proceed();
        } finally {
            mon.stop();
            LOG.trace("Profiling {} ended with {}.", sig, mon);
            log(thisJoinPoint, mon.getLastValue());
        }
    }

    /**
     * Log the given joinpoint.
     *
     * @param jp the joinpoint
     * @param millis time in milliseconds
     */
    protected void log(JoinPoint jp, double millis) {
        log(jp, (long) (millis));
    }

    /**
     * Methods which needs more than 2000 ms are logged as INFO message,
     * methods between 200 and 2000 ms as DEBUG message
     * (and below only if tracing is enabled).
     * @param jp the joinpoint
     * @param millis time in milliseconds
     */
    protected void log(JoinPoint jp, long millis) {
        if (millis < 20) {
            return;
        } else if (millis < 200) {
            if (getLog().isTraceEnabled()) {
                getLog().trace("+++ " + millis + "ms for "
                        + JoinPointHelper.getAsString(jp));
            }
        } else if (millis < 2000) {
            if (getLog().isDebugEnabled()) {
                getLog().debug("++++ " + millis + "ms for "
                        + JoinPointHelper.getAsString(jp));
            }
        } else if (millis < 20000) {
            if (getLog().isInfoEnabled()) {
                getLog().info("+++++ " + millis + "ms for "
                        + JoinPointHelper.getAsString(jp));
            }
        } else {
            if (getLog().isWarnEnabled()) {
                getLog().warn("++++++ " + millis + "ms for "
                        + JoinPointHelper.getAsString(jp));
            }
        }
    }

}
