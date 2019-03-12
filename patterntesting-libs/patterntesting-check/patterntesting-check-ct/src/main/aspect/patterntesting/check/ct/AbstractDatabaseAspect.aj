/*
 *========================================================================
 *
 * Copyright 2001-2004 Vincent Massol & Matt Smith.
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
 *========================================================================
 */
package patterntesting.check.ct;

/**
 * Pattern Test that verifies that only some classes are allowed to perform
 * database accesss using JDBC.
 *
 * @author <a href="mailto:vmassol@apache.org">Vincent Massol</a>
 *
 * @version $Id: AbstractDatabaseAspect.aj,v 1.1 2011/12/22 17:15:08 oboehm Exp $
 */
public abstract aspect AbstractDatabaseAspect
{
    /**
     * Specify what is application code that should be subject to the
     * pattern test.
     *
     * Ex: <code>pointcut applicationCode(): within(patterntesting.*)</code>
     */
    public abstract pointcut applicationCode();

    /**
     * Specify which classes are allowed to call JDBC.
     */
    public abstract pointcut allowedCode();

    /**
     * Specify which JDBC calls are forbidden.     */
    pointcut forbiddenCalls() :
        call(public * java.sql..*(..)) ||
        call(public * javax.sql..*(..));

    /**
     * Specify which JDBC constructors are forbidden.
     */
    pointcut forbiddenConstructors() :
        call(public java.sql..new(..)) ||
        call(public javax.sql..new(..));

    /**
     * Raise error if violations found.
     */
    declare error: (applicationCode() && forbiddenCalls()) && !allowedCode() :
        "It is not allowed to use the JDBC API from here";

    /**
     * Raise error if a forbidden constructor call was found.
     */
    declare error:
            (applicationCode() && forbiddenConstructors()) && !allowedCode() :
        "It is not allowed to create any JDBC objects";

} 