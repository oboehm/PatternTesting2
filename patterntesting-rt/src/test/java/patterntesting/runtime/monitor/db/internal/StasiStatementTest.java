/*
 * $Id: StasiStatementTest.java,v 1.10 2016/12/18 20:19:38 oboehm Exp $
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
 * (c)reated 23.03.2014 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.monitor.db.internal;

import static org.junit.Assert.*;

import java.sql.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.*;

import patterntesting.runtime.monitor.db.AbstractDbTest;

/**
 * Unit tests for {@link StasiStatement} class.
 *
 * @author oliver
 * @version $Revision: 1.10 $
 * @since 1.4.1 (23.03.2014)
 */
public final class StasiStatementTest extends AbstractDbTest {

    private static final Logger log = LogManager.getLogger(StasiStatementTest.class);
    private StasiStatement statement;

    /**
     * Returns an object for testing.
     *
     * @return the object
     */
    @Override
    protected Statement getObject() {
        try {
            return this.proxy.createStatement();
        } catch (SQLException sex) {
            throw new UnsupportedOperationException("cannot provide object for testing", sex);
        }
    }

    /**
     * Sets up the statement for testing.
     *
     * @throws SQLException the SQL exception
     */
    @Before
    public void setUpStatement() throws SQLException {
        statement = (StasiStatement) this.proxy.createStatement();
    }

    /**
     * Test method for {@link StasiStatement#execute(String)}.
     *
     * @throws SQLException the sQL exception
     */
    @Test
    public void testExecute() throws SQLException {
        statement.execute("create table DUMMY(ID integer not null, NAME varchar(32))");
    }

    /**
     * If the execution of a SQL statement fails, we expected that SQL as part
     * of the exception message.
     */
    @Test
    public void testExecuteFailing() {
        String sql = "create something with wrong syntax";
        try {
            statement.execute(sql);
            fail("should fail: " + sql);
        } catch (SQLException expected) {
            log.debug("SQLException expected.", expected);
            String msg = expected.getMessage();
            assertTrue(msg, msg.contains(sql));
        }
    }

    /**
     * Test method for {@link StasiStatement#executeQuery(String)}.
     *
     * @throws SQLException the SQL exception
     */
    @Test
    public void testExcecuteQuery() throws SQLException {
        StasiStatement stasiStmt = statement;
        try {
            assertEquals(getIdWith(stasiStmt), getIdWith(stasiStmt.getStatement()));
        } finally {
            stasiStmt.close();
        }
    }

    private static int getIdWith(Statement stmt) throws SQLException {
        String sql = "SELECT * FROM persons WHERE id = 1001";
        ResultSet query = stmt.executeQuery(sql);
        try {
            assertTrue("result expected", query.next());
            return query.getInt("id");
        } finally {
            query.close();
        }
    }

    /**
     * Test method for {@link StasiStatement#addBatch(String)}. This method was
     * introduced to see the log for a void method. Watch the log!
     *
     * @throws SQLException the sQL exception
     */
    @Test
    public void testAddBatch() throws SQLException {
        statement.addBatch("create table BATCH(NAME varchar(12))");
    }

    /**
     * Test method for {@link StasiStatement#isWrapperFor(Class)}.
     *
     * @throws SQLException the SQL exception
     */
    @Test
    public void testIsWrapper() throws SQLException {
        Statement wrappedStatement = statement.getStatement();
        assertTrue("true expected for " + wrappedStatement, statement.isWrapperFor(wrappedStatement.getClass()));
    }

    /**
     * Test method for {@link StasiStatement#unwrap(Class)}.
     *
     * @throws SQLException the SQL exception
     */
    @Test
    public void testUnwrap() throws SQLException {
        Statement wrappedStatement = statement.getStatement();
        assertEquals(wrappedStatement, statement.unwrap(Statement.class));
    }

    /**
     * Close statement.
     *
     * @throws SQLException the sQL exception
     */
    @After
    public void closeStatement() throws SQLException {
        this.statement.close();
    }

}

