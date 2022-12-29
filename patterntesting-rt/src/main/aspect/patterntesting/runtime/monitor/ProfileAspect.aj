/**
 * Copyright (c) 2008-2018 by Oliver Boehm
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
 * (c)reated 19.12.2008 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.monitor;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.SuppressAjWarnings;

import patterntesting.runtime.annotation.*;

/**
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 19.12.2008
 */
public aspect ProfileAspect extends AbstractProfileAspect {

    private static final Logger log = LoggerFactory.getLogger(ProfileAspect.class);

    /**
     * To return the aspect specific logger to the super aspect.
     * @return the aspect specific logger
     */
    @Override
    public final Logger getLog() {
        return log;
    }

    /**
     * applicationCode() includes all methods and constructors marked with
     * "@ProfileMe" but exclude the methods and constructors marked with
     * "@DontProfileMe".
     */
    @Override
    public pointcut applicationCode() :
        profiledMethods() && !unprofiledMethods();

    /**
     * profiledMethods() includes also the constructors marked with
     * "@ProfileMe".
     */
    pointcut profiledMethods() :
        ((execution(* *..*.*(..)) || execution(*..*.new(..)))
                && within(@ProfileMe *))
        || execution(@ProfileMe *..*.new(..))
        || execution(@ProfileMe * *..*.*(..))
        ;

    /**
     * unprofiledMethods() includes both: methods and constructors marked with
     * "@DontProfileMe".
     */
    pointcut unprofiledMethods() :
        execution(@DontProfileMe *..*.new(..))
        || execution(@DontProfileMe * *..*.*(..));

    pointcut profiledClasses() :
        staticinitialization(@ProfileMe *..*);

    /**
     * Put the profiled class into the internal statistic table after the the
     * class is initialized.
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    after() : profiledClasses() {
        Signature sig = thisJoinPointStaticPart.getSignature();
        log.trace("Putting {} into the statistic table.", sig);
        ProfileStatistic.getInstance().init(sig.getDeclaringType());
    }

}
