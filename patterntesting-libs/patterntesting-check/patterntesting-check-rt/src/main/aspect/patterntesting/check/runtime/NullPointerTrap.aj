/*
 * $Id: NullPointerTrap.aj,v 1.6 2016/12/18 21:59:31 oboehm Exp $
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
 * (c)reated 12.09.2008 by oliver (ob@oasd.de)
 */
package patterntesting.check.runtime;

import org.apache.logging.log4j.*;
import org.apache.logging.log4j.LogManager;
import patterntesting.annotation.check.runtime.*;

/**
 * Together with the AbstractNullPointerTrap forbids this aspect "null" as
 * valid argument or return value. If you want to flag a null value as
 * valid arguemnt or return value you can use the annotations
 * <code>@NullArgsAllowed</code> and <code>@MayReturnNull</code>.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.6 $
 * @see AbstractNullPointerTrap
 * @see patterntesting.annotation.check.runtime.NullArgsAllowed
 * @see patterntesting.annotation.check.runtime.MayReturnNull
 * @since 12.09.2008
 * @version $Revision: 1.6 $
 */
public aspect NullPointerTrap  extends AbstractNullPointerTrap {

    private static final Logger LOG = LogManager.getLogger(NullPointerTrap.class);

    static {
        if (assertEnabled) {
            LOG.debug("NullPointerTrap is active");
        } else {
            LOG.debug("NullPointer checks are skipped - call 'java -ea' (SunVM) to activate it");
        }
    }
    
    /**
     * To return the aspect specific logger to the super aspect.
     * @return the aspect specific logger
     */
    public final Logger getLog() {
        return LOG;
    }

    /**
     * Application code is the whole application.
     */
    public pointcut applicationCode() :
        within(*..*);

    /**
     * Null arguments are allowed for methods and constructors which have
     * the <code>NullArgsAllowed</code> annotation either before the
     * method name / constructor or before the whole class.
     * Also inner classes are considered.
     * <p>
     * Also for some common methods from {@link Object} like the equals
     * method are allowed to have null as argument.
     * </p>
     */
    public pointcut nullArgsAllowed() :
        execution(@NullArgsAllowed * *..*(*, ..))
        || execution(@NullArgsAllowed *..new(*, ..))
        || execution(*..*$*.new(..))
        || execution(boolean *..equals(java.lang.Object))
        || within(@NullArgsAllowed *);

    /**
     * Return values are only allowed for methods which have
     * the <code>NullArgsAllowed</code> annotation either before the
     * method name or before the whole class.
     */
    public pointcut mayReturnNull() :
        execution(@MayReturnNull java.lang.Object+ *..*(..))
        || within(@MayReturnNull *);

}
