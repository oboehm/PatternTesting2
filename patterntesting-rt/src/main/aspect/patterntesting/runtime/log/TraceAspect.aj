/*
 * $Id: TraceAspect.aj,v 1.4 2016/12/18 20:19:38 oboehm Exp $
 *
 * Copyright (c) 2010 by Oliver Boehm
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
 * (c)reated 30.09.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.log;

import patterntesting.runtime.annotation.DontTraceMe;
import patterntesting.runtime.annotation.TraceMe;

/**
 * This aspect is responsible that classes with the {@link TraceMe} annotation
 * are traced to the log file.
 *
 * @author oliver
 * @since 1.0.3 (30.09.2010)
 */
public aspect TraceAspect extends AbstractTraceAspect {

    /**
     * applicationCode() includes all methods, fields and constructors marked with
     * "@TraceMe" but exclude the stuff marked with "@DontTraceMe".
     */
    @Override
    public pointcut applicationCode() :
        (within(@TraceMe *..*) || markedMethods() || markedFields())
        && !excludedMethods() && !excludedMethods() && !excludedFields()
        ;

    private pointcut markedMethods() :
        execution(@TraceMe *..*.new(..))
        || execution(@TraceMe * *..*.*(..))
        ;

    private pointcut excludedMethods() :
        execution(@DontTraceMe *..*.new(..))
        || execution(@DontTraceMe * *..*.*(..));

    private pointcut markedFields() :
        set(@TraceMe * *..*)
        || get(@TraceMe * *..*)
        ;

    private pointcut excludedFields() :
        set(@DontTraceMe * *..*) || get(@DontTraceMe * *..*);

}

