/**
 * $Id: DatabaseAspect.aj,v 1.1 2011/12/22 17:15:08 oboehm Exp $
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
 * (c)reated 02.12.2008 by oliver (ob@oasd.de)
 */
package patterntesting.check.ct;

import patterntesting.annotation.check.ct.*;

/**
 * Classes annotated with "@DamnJDBC" should not use JDBC.
 *
 * @see AbstractDatabaseAspect
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 02.12.2008
 * @version $Revision: 1.1 $
 */
public aspect DatabaseAspect extends AbstractDatabaseAspect {

    /**
     * Classes annotated with "@DamnJDBC" should not use JDBC.
     */
    public pointcut applicationCode() : within(@DamnJDBC *);

    /**
     * Methods annotated with "@AllowJDBC" are allowed to use JDBC.
     */
    public pointcut allowedCode() :
        withincode(@AllowJDBC * *..*.*(..));

}
