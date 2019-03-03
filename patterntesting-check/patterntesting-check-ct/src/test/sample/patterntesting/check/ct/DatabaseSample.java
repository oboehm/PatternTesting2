/**
 * $Id: DatabaseSample.java,v 1.1 2010/03/10 21:33:00 oboehm Exp $
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

import java.sql.Date;

import org.junit.Test;

import patterntesting.annotation.check.ct.*;

import static org.junit.Assert.*;

/**
 * This is the JUnit testcase for (Abstract)DatabaseAspect.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 02.12.2008
 */
@DamnJDBC
public class DatabaseSample {

    /**
     * Because "@AllowJDBC" is removed from this method you see a lot of
     * errors from the (Abstract)DatabaseAspect because the Date class from
     * java.sql is used here.
     */
    @Test
    public final void testSqlDate() {
        long currentTimeMillis = System.currentTimeMillis();
        Date now = new Date(currentTimeMillis);
        Date later = new Date(currentTimeMillis + 100);
        assertTrue(now + " is not before " + later, now.before(later));
    }

}
