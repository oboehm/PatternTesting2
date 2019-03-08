/*
 * $Id: DeprecatedAspect.aj,v 1.5 2016/12/18 21:59:31 oboehm Exp $
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
 * (c)reated 17.02.2009 by oliver (ob@oasd.de)
 */
package patterntesting.check.runtime;

import org.aspectj.lang.JoinPoint;
import org.apache.logging.log4j.*;
import org.apache.logging.log4j.LogManager;

/**
 * Do you have developers who are resistent against deprecated methods?
 * Now you can see it in the log as warning which "@Deprecated" methods
 * were executed.
 * <br/>
 * And if assertions are enabled ('java -ea ...') a DeprecatedCodeException
 * will be thrown to force the use of *not* deprecated methods.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.5 $
 * @since 17.02.2009
 */
public aspect DeprecatedAspect extends AbstractDeprecatedAspect {
    
    private static final Logger LOG = LogManager.getLogger(DeprecatedAspect.class);
    
    /**
     * To return the aspect specific logger to the super aspect.
     * @return the aspect specific logger
     */
    @Override
    public final Logger getLog() {
        return LOG;
    }

    /**
     * Gets the marker for logging.
     *
     * @return "@Deprecated"
     */
    @Override
    public final String getMarker() {
        return "@Deprecated";
    }
    
    /**
     * Here we will throw an {@link DeprecatedCodeException}.
     *
     * @param jp the joinpoint
     */
    @Override
    protected void throwCauseFor(final JoinPoint jp) {
        throw new DeprecatedCodeException(jp);
    }

    /**
     * These are the methods which are marked as "@Deprecated"
     * (unfortunately the methods marked as "deprecated" in a Javadoc
     * comment cannot be recognized).
     */
    pointcut deprecatedMethods() :
        execution((@Deprecated *..*).new(..))
        || execution(* @Deprecated *..*.*(..))
        || @withincode(Deprecated)
        ;

    /**
     * "@Deprecated" attributes should eiter be set nor read.a
     */
    pointcut deprecatedAttributes() :
        (set(@Deprecated * *..*.*)
                && withincode(* *..*.*(..)))
        || get(@Deprecated * *..*.*)
        ;

    /**
     * Deprecated code.
     */
    public pointcut deprecatedCode() :
        deprecatedMethods()
        || deprecatedAttributes()
        ;

}
