/**
 * $Id: ContractAspect.aj,v 1.3 2016/12/18 20:19:36 oboehm Exp $
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
 * (c)reated 30.01.2009 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.dbc;

import org.slf4j.LoggerFactory;
import org.slf4j.*;
import org.aspectj.lang.annotation.SuppressAjWarnings;

import patterntesting.runtime.util.Assertions;
import patterntesting.runtime.util.JoinPointHelper;

/**
 * This aspect handles the DbC (Design by Contract) stuff.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 0.9
 */
public aspect ContractAspect {

    private static final Logger log = LoggerFactory.getLogger(ContractAspect.class);
    private static final boolean assertEnabled = Assertions.areEnabled();

    pointcut dbcMethods() :
        execution(public static void DbC.require(..))
        || execution(public static void DbC.ensure(..))
        || execution(public boolean Contract+.invariant(..))
        ;

    pointcut businessMethods() :
        execution(* Contract+.*(..))
        && !dbcMethods()
        && !within(ContractAspect);

    /**
     * call invariant() before each method
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    before(Contract c) : businessMethods() && !cflow(dbcMethods())
                        && this(c) {
        if (assertEnabled) {
            if (c.invariant()) {
                if (log.isTraceEnabled()) {
                    log.trace("invariant ok before "
                            + JoinPointHelper.getAsShortString(thisJoinPoint));
                }
            } else {
                String msg = "invariant violated before "
                            + JoinPointHelper.getAsShortString(thisJoinPoint);
                log.warn(msg);
                throw new ContractViolation(msg);
            }
        }
    }

    /**
     * call invariant() after each method
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    after(Contract c) : (businessMethods() || execution(Contract+.new(..)))
                        && !cflow(dbcMethods())
                        && this(c) {
        if (assertEnabled) {
            if (c.invariant()) {
                if (log.isTraceEnabled()) {
                    log.trace("invariant ok after "
                            + JoinPointHelper.getAsShortString(thisJoinPoint));
                }
            } else {
                String msg = "invariant violated after "
                            + JoinPointHelper.getAsShortString(thisJoinPoint);
                log.warn(msg);
                throw new ContractViolation(msg);
            }
        }
    }

}
