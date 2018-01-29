/*
 * $Id: ProxyConnectionTest.java,v 1.8 2016/12/18 20:19:39 oboehm Exp $
 *
 * Copyright (c) 2012 by Oliver Boehm
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
 * (c)reated 29.09.2012 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.monitor.db;

import static org.junit.Assert.*;

import java.sql.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.Test;

/**
 * JUnit tests for {@link ProxyConnection} class.
 * To test DB access we use here an in-memory db (HSQL-DB).
 *
 * @author oliver (ob@aosd.de)
 * @version $Revision: 1.8 $
 * @since 1.3 (29.09.2012)
 */
public class ProxyConnectionTest extends AbstractDbTest {

    private static final Logger LOG = LogManager.getLogger(ProxyConnectionTest.class);

    /**
     * Creates an object for testing.
     *
     * @return the object
     */
    @Override
    protected Connection getObject() {
        return this.proxy;
    }

    /**
     * Test method for {@link ProxyConnection#invoke(Object, java.lang.reflect.Method, Object[])}.
     *
     * @throws SQLException the sQL exception
     */
    @Test
    public void testInvoke() throws SQLException {
        DatabaseMetaData metaData = proxy.getMetaData();
        assertNotNull(metaData);
        LOG.info("metaData = {}", metaData);
        Statement stmt = proxy.createStatement();
        stmt.execute("create table TEST_TABLE(ID decimal(5))");
        stmt.execute("drop table TEST_TABLE");
    }


    /**
     * Test method for
     * {@link ProxyConnection#invoke(Object, java.lang.reflect.Method, Object[])}.
     * Here we check the {@link SQLException} which are thrwon in case of an
     * error.
     *
     * @throws SQLException the sQL exception
     */
    @Test(expected = SQLSyntaxErrorException.class)
    public void testInvokePreparedStatementWithWrongSyntax() throws SQLException {
        proxy.prepareStatement("create table with wrong syntax");
    }

    /**
     * Test method for {@link ProxyConnection#getCaller()}.
     *
     * @throws SQLException the sQL exception
     */
    @Test
    public void testGetCaller() throws SQLException {
        Connection conn = ProxyConnection.newInstance(proxy);
        try {
            StackTraceElement lastCaller = ConnectionMonitor.getInstance().getLastCaller();
            assertEquals("testGetCaller", lastCaller.getMethodName());
        } finally {
            conn.close();
        }
    }

    /**
     * Here we test the closing of a connection.
     *
     * @throws SQLException the sQL exception
s     */
    @Test
    public void testClose() throws SQLException {
        Connection conn = ProxyConnection.newInstance(proxy);
        try {
        assertFalse("should be not closed: " + conn, conn.isClosed());
        } finally {
            conn.close();
            assertTrue("should be closed:", conn.isClosed());
        }
    }

}
