/*
 * $Id: SequenceDiagramAspect.aj,v 1.14 2016/12/18 20:19:38 oboehm Exp $
 *
 * Copyright (c) 2013 by Oliver Boehm
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
 * (c)reated 06.09.2013 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.log;

import org.apache.commons.lang3.StringUtils;

import patterntesting.runtime.annotation.DrawSequenceDiagram;
import patterntesting.runtime.annotation.IgnoreForSequenceDiagram;

/**
 * This aspect controls which code will be logged as sequence diagram.
 *
 * @author oliver
 * @since 1.3.1 (06.09.2013)
 */
@IgnoreForSequenceDiagram
public aspect SequenceDiagramAspect extends AbstractSequenceDiagramAspect {

    /**
     * applicationCode() includes all methods, fields and constructors marked
     * with "@DrawSequenceDiagram".
     * <p>
     * Note: The AspejctJ classes and JDK classes are excluded. I.e.
     * classes from org.aspectj and java will be not part of the generated
     * sequence diagram for two reasons:
     * <ol>
     *  <li>to speed up the weaving process</li>
     *  <li>to keep the generated sequence diagram clear</li>
     * </ol>
     * </p>
     */
    @Override
    public pointcut applicationCode() :
        ((within(@DrawSequenceDiagram *..*) || markedMethods()))
        && !excludedMethods()
        && !within(@IgnoreForSequenceDiagram *..*)
        && !call(* org.aspectj..*.*(..))
        && !call(* java..*.*(..))
        && !call(org.aspectj..*.new(..))
        && !call(java..*.new(..))
        ;

    private pointcut drawCalls() :
        call(@DrawSequenceDiagram *..*.new(..))
        || call(@DrawSequenceDiagram * *..*.*(..))
        ;

    private pointcut drawCreation() :
        execution(@DrawSequenceDiagram *..*.new(..));

    private pointcut drawVoidExecution() :
        execution(@DrawSequenceDiagram void *..*.*(..));

    private pointcut drawNonVoidExecution() :
        execution(@DrawSequenceDiagram Object+ *..*.*(..));

    private pointcut drawExecution() :
        drawCreation() || drawVoidExecution() || drawNonVoidExecution();

    private pointcut markedMethods() :
        cflow(drawCalls())
        || withincode(@DrawSequenceDiagram *..*.new(..))
        || withincode(@DrawSequenceDiagram * *..*.*(..))
        ;

    private pointcut excludedMethods() :
        execution(@IgnoreForSequenceDiagram *..*.new(..))
        || execution(@IgnoreForSequenceDiagram * *..*.*(..));

    before(DrawSequenceDiagram a) : drawExecution() && @annotation(a) {
        if (StringUtils.isEmpty(a.value())) {
            addToDiagram(thisJoinPoint);
        } else {
            startDiagram(a);
        }
    }

    after(DrawSequenceDiagram a) : drawCreation() && @annotation(a) {
        if (StringUtils.isNotEmpty(a.value())) {
            closeDiagram();
        }
    }

    after(DrawSequenceDiagram a) : drawVoidExecution() && @annotation(a) {
        if (StringUtils.isEmpty(a.value())) {
            returnFromDiagram(thisJoinPoint);
        } else {
            closeDiagram();
        }
    }

    after(DrawSequenceDiagram a) returning(Object ret) : drawNonVoidExecution() && @annotation(a) {
        if (StringUtils.isEmpty(a.value())) {
            returnFromDiagram(thisJoinPoint, ret);
        } else {
            closeDiagram();
        }
    }

}

