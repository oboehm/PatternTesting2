/*
 * $Id: AbstractSequenceDiagramAspect.aj,v 1.26 2016/12/18 20:19:38 oboehm Exp $
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

import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.SuppressAjWarnings;

import patterntesting.runtime.annotation.DrawSequenceDiagram;
import patterntesting.runtime.util.JoinPointHelper;

/**
 * This aspect provides the generation of sequence diagrams. To avaoid to
 * much details in the generated diagrams only the call of public methods
 * are logged.
 *
 * @author oliver
 * @since 1.3.1 (06.09.2013)
 */
public abstract aspect AbstractSequenceDiagramAspect {

    private static Logger log = LogManager.getLogger(AbstractSequenceDiagramAspect.class);
    private static SequenceGrapher grapher = new SequenceGrapher();
    private static List<SequenceGrapher> grapherList = new CopyOnWriteArrayList<>();

    static {
        grapherList.add(grapher);
    }

    /**
     * Start the painting of the diagram to file given by the annotation.
     *
     * @param diagramAnnotation the diagramAnnotation annotation with the file
     *        name
     */
    protected synchronized void startDiagram(final DrawSequenceDiagram diagramAnnotation) {
        File file = new File(diagramAnnotation.value());
        startDiagram(file);
        grapher.setExcludeFilter(diagramAnnotation.excluded());
    }

    /**
     * Start the painting of the diagram to the given file.
     *
     * @param file the file
     */
    private void startDiagram(File file) {
        grapher = new SequenceGrapher(file);
        grapherList.add(grapher);
        log.trace("Generation of sequence diagram to {} started.", file);
    }

    /**
     * If the executed joinpoint is not part of the logged diagram it will
     * be added to the diagramm.
     *
     * @param jp the execution joinpoint
     */
    protected void addToDiagram(final JoinPoint jp) {
        grapher.execute(jp);
    }

    /**
     * This method is the closing part of {@link #addToDiagram(JoinPoint)} for
     * execution of void methods.
     *
     * @param jp the execution joinpoint
     * @since 1.6 (03.06.2015)
     */
    protected void returnFromDiagram(final JoinPoint jp) {
        grapher.returnFromExecute(jp);
    }

    /**
     * This method is the closing part of {@link #addToDiagram(JoinPoint)} for
     * execution of methods with return value.
     *
     * @param jp the execution joinpoint
     * @param returnValue the return value
     * @since 1.6 (03.06.2015)
     */
    protected void returnFromDiagram(final JoinPoint jp, final Object returnValue) {
        grapher.returnFromExecute(jp, returnValue);
    }

    /**
     * Close diagram.
     */
    protected synchronized void closeDiagram() {
        grapher.close();
        log.trace("Sequence diagram closed.");
        int n = grapherList.size();
        grapherList.remove(n - 1);
        grapher = grapherList.get(n - 2);
    }

    /**
     * Application code.
     */
    public abstract pointcut applicationCode();

    /**
     * We exclude here all classes of PatternTesting. Except those (test)
     * classes which are needed to test this aspect here.
     */
    @SuppressAjWarnings({"adviceDidNotMatch", "unmatchedSuperTypeInCall"})
    private pointcut intersection() :
        (applicationCode() && !within(patterntesting.runtime..*)) ||
        (applicationCode() && within(SequenceDiagramTest))
        ;

    private pointcut callAllMethods() :
        call(public * *.*(..)) && !(call(public * org.junit.*.*(..)));
        //call(public !static * *.*(..)) && !(call(public * java.lang.*.valueOf(*)));

    private pointcut callVoidMethods() :
        call(public void *.*(..)) && !(call(public void org.junit.*.*(..)));
        //call(public !static void *.*(..));

    private pointcut callReturningMethods() :
        callAllMethods() && !callVoidMethods();

    private pointcut callConstructor() :
        call(*..*.new(..));

    /**
     * Logs the creation of an object in the sequence diagram.
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    Object around() : intersection() && callConstructor() {
        Object created = proceed();
        log.trace("--- <<create>> ---> {}", created);
        grapher.createMessage(thisJoinPoint, created);
        return created;
    }

    /**
     * Logs a call in the sequence diagram.
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    before() : intersection() && callAllMethods() {
        Object caller = thisJoinPoint.getThis();
        if (caller == null) {
            caller = JoinPointHelper.getCallerClass();
        }
        log.trace("{}: --- {} -->", caller, thisJoinPoint);
        grapher.message(caller, thisJoinPoint);
    }

    /**
     * Here we are back from the method call.
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    after() : intersection() && callVoidMethods() {
        log.trace("<-----: {}", thisJoinPoint);
        grapher.returnMessage(thisJoinPoint);
    }

    /**
     * Here we are back from the method call and return a value.
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    after() returning (Object ret): intersection() && callReturningMethods() {
        log.trace("<-- {} ---: {}", ret, thisJoinPoint);
        grapher.returnMessage(thisJoinPoint, ret);
    }

}

