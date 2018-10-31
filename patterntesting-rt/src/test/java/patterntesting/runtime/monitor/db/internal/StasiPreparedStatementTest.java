/*
 * $Id: StasiPreparedStatementTest.java,v 1.7 2016/12/18 20:19:38 oboehm Exp $
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
 * (c)reated 05.04.2014 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.monitor.db.internal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import patterntesting.runtime.monitor.db.AbstractDbTest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link StasiPreparedStatement} class.
 *
 * @author oliver
 * @version $Revision: 1.7 $
 * @since 1.4.1 (05.04.2014)
 */
public class StasiPreparedStatementTest extends AbstractDbTest {

    private static final Logger LOG = LogManager.getLogger(StasiPreparedStatementTest.class);

    /**
     * Returns an object for testing.
     *
     * @return the object
     */
    @Override
    protected PreparedStatement getObject() {
        try {
            return this.proxy.prepareStatement("SELECT * FROM country");
        } catch (SQLException sex) {
            throw new UnsupportedOperationException("cannot provide object for testing", sex);
        }
    }

    /**
     * Test method for {@link StasiPreparedStatement#execute(String)}.
     *
     * @throws SQLException the sQL exception
     */
    @Test
    public void testExecuteUpdate() throws SQLException {
        PreparedStatement stmt = this.proxy
                .prepareStatement("INSERT INTO country (lang, name, callingcode) "
                        + "VALUES(?, ?, ?)");
        try {
            assertEquals(StasiPreparedStatement.class, stmt.getClass());
            stmt.setString(1, "de");
            stmt.setString(2, "Germany");
            stmt.setInt(3, 49);
            int ret = stmt.executeUpdate();
            assertEquals(1, ret);
        } finally {
            stmt.close();
        }
    }

    /**
     * Test method for {@link StasiPreparedStatement#execute(String)}.
     * If the exceution fails we expect the wrong SQL as part of the
     * {@link SQLException}.
     *
     * @throws SQLException the sQL exception
     */
    @Test
    public void testExecuteFailing() throws SQLException {
        PreparedStatement stmt = this.proxy
                .prepareStatement("INSERT INTO country (lang, name, callingcode) "
                        + "VALUES(?, ?, ?)");
        try {
            stmt.setInt(1, 44);
            stmt.executeUpdate();
            fail("SQLException expected");
        } catch (SQLException expected) {
            LOG.debug("SQLException expected", expected);
            String msg = expected.getMessage();
            assertThat(msg, containsString("INSERT INTO country"));
        } finally {
            stmt.close();
        }
    }

    /**
     * Test method for {@link StasiPreparedStatement#executeQuery()} and
     * {@link StasiPreparedStatement#getWrappedPreparedStatement()}.
     *
     * @throws SQLException the SQL exception
     */
    @Test
    public void testExcecuteQuery() throws SQLException {
        StasiPreparedStatement stmt = (StasiPreparedStatement) this.proxy
                .prepareStatement("SELECT * FROM persons WHERE id = ?");
        try {
            checkExcecuteQuery(stmt);
            checkExcecuteQuery(stmt.getWrappedPreparedStatement());
        } finally {
            stmt.close();
        }
    }

    private static void checkExcecuteQuery(PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, 1001);
        ResultSet query = stmt.executeQuery();
        try {
            assertTrue(query.next(), "result expected");
            assertEquals(1001, query.getInt("id"));
        } finally {
            query.close();
        }
    }

    /**
     * The toString implementation should contain the real SQL.
     *
     * @throws SQLException the sQL exception
     */
    @Test
    public void testToStringWithSQL() throws SQLException {
        PreparedStatement stmt = this.proxy
                .prepareStatement("SELECT * FROM country WHERE lang = ? OR callingcode = ?");
        try {
            stmt.setString(1, "de");
            stmt.setInt(2, 42);
            String sql = stmt.toString();
            assertEquals("SELECT * FROM country WHERE lang = 'de' OR callingcode = 42", sql);
            LOG.info("sql = \"{}\"", sql);
        } finally {
            stmt.close();
        }
    }

}

