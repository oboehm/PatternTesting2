/*
 * $Id: ZombieAspect.aj,v 1.4 2016/12/18 21:59:31 oboehm Exp $
 *
 * Copyright (c) 2015 by Oliver Boehm
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
 * (c)reated 11.01.2015 by oliver (ob@oasd.de)
 */

package patterntesting.check.runtime;

import org.aspectj.lang.JoinPoint;
import org.slf4j.*;
import org.slf4j.LoggerFactory;

import patterntesting.annotation.check.runtime.Zombie;

/**
 * This is the aspect for the {@link Zombie} annotation. If you have a class
 * (or method) you want to delete but you are not sure if it is used you can
 * mark it as {@link Zombie} class. If this class will be used you will get a
 * warning in the log or an {@link AssertionError} will be thrown if assertions
 * are enabled.
 * <p>
 * Because this aspect is very similar to the {@link DeprecatedAspect} it uses
 * the same super aspect.
 * </p>
 * 
 * @author oliver
 * @version $Revision: 1.4 $
 * @since 1.6 (11.01.2015)
 */
public aspect ZombieAspect extends AbstractDeprecatedAspect {

    private static final Logger LOG = LoggerFactory.getLogger(ZombieAspect.class);
    
    /**
     * To return the aspect specific logger to the super aspect.
     * 
     * @return the aspect specific logger
     */
    @Override
    public final Logger getLog() {
        return LOG;
    }
    
    /**
     * Gets the marker for logging.
     *
     * @return "@Zombie"
     */
    @Override
    public final String getMarker() {
        return "@Zombie";
    }

    /**
     * Here we will throw an {@link ZombieException}.
     *
     * @param jp the joinpoint
     */
    @Override
    protected void throwCauseFor(final JoinPoint jp) {
        throw new ZombieException(jp);
    }

    /**
     * These are the methods which are marked as "@Zombie"
     * (unfortunately the methods marked as "deprecated" in a Javadoc
     * comment cannot be recognized).
     */
    pointcut deprecatedMethods() :
        execution((@Zombie *..*).new(..))
        || execution(* @Zombie *..*.*(..))
        || @withincode(java.lang.Deprecated)
        ;

    /**
     * "@Zombie" attributes should eiter be set nor read.a
     */
    pointcut deprecatedAttributes() :
        (set(@Zombie * *..*.*)
                && withincode(* *..*.*(..)))
        || get(@Zombie * *..*.*)
        ;

    /**
     * Deprecated code.
     */
    public pointcut deprecatedCode() :
        deprecatedMethods()
        || deprecatedAttributes()
        || within(@Zombie *)
        ;

}

