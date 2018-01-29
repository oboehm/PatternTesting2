/*
 * $Id: StasiResultSetTest.java,v 1.6 2016/12/18 20:19:38 oboehm Exp $
 *
 * Copyright (c) 2014 by Oliver Boehm
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
 * (c)reated 15.04.2014 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.monitor.db.internal;

import static org.junit.Assert.*;

import java.sql.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.After;
import org.junit.Test;

import patterntesting.runtime.monitor.db.AbstractDbTest;

/**
 * Unit tests for {@link StasiResultSet} class.
 *
 * @author oliver
 * @since 1.4.1 (15.04.2014)
 */
public final class StasiResultSetTest extends AbstractDbTest {

    private static Logger LOG = LogManager.getLogger(StasiResultSetTest.class);
    private ResultSet resultSet;

    /**
     * Returns an object for testing.
     *
     * @return the object
     */
    @Override
    protected ResultSet getObject() {
        try {
            return getResultSetFor("SELECT * FROM country");
        } catch (SQLException sex) {
            throw new UnsupportedOperationException("cannot provide object for testing", sex);
        }
    }

    /**
     * Test method for {@link StasiResultSet#getObject(int)} and other
     * getXxx(int) methods.
     *
     * @throws SQLException the SQL exception
     */
    @Test
    public void testGetValues() throws SQLException {
        resultSet = getResultSetFor("SELECT * FROM persons WHERE country = 'DE'");
        while (resultSet.next()) {
            int id = resultSet.getInt(1);
            assertTrue("not > 0: " + id, id > 0);
            String name = resultSet.getString(2);
            Object obj = resultSet.getObject(2);
            assertEquals(name, obj);
            LOG.info("id = {}, name = \"{}\"", id, name);
        }
    }

    /**
     * Test method for {@link StasiResultSet#isFirst()} and
     * {@link StasiResultSet#getWrappedResultSet()}.
     *
     * @throws SQLException the SQL exception
     */
    @Test
    public void testIsFirst() throws SQLException {
        resultSet = getResultSetFor("SELECT * FROM persons WHERE id = 1001");
        StasiResultSet srs = (StasiResultSet) resultSet;
        ResultSet wrapped = srs.getWrappedResultSet();
        assertTrue("should be before first", srs.isBeforeFirst());
        assertTrue("should be before first", wrapped.isBeforeFirst());
        assertTrue("result expected", srs.next());
        assertTrue("should be first", srs.isFirst());
        assertTrue("should be last", srs.isLast());
        assertFalse("no result expected", srs.next());
        assertTrue("should be after last", srs.isAfterLast());
    }

    /**
     * Test method for {@link java.lang.Object#toString()}.
     *
     * @throws SQLException the SQL exception
     */
    @Test
    public void testToStringWithSQL() throws SQLException {
        resultSet = getResultSetFor("SELECT * FROM country");
        String s = resultSet.toString();
        assertFalse("looks like default implementation: " + s, s.contains("@"));
        LOG.info("s = \"{}\"", s);
    }

    private ResultSet getResultSetFor(final String sql) throws SQLException {
        Statement statement = this.proxy.createStatement();
        statement.execute(sql);
        return statement.getResultSet();
    }

    /**
     * Close result set.
     *
     * @throws SQLException the SQL exception
     */
    @After
    public void closeResultSet() throws SQLException {
        if (this.resultSet != null) {
            this.resultSet.close();
        }
    }

}
