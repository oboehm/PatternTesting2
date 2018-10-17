/*
 * $Id: ConnectionMonitorTest.java,v 1.12 2016/12/18 20:19:39 oboehm Exp $
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
 * (c)reated 07.10.2012 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.monitor.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.sql.*;

import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.TabularData;

import org.junit.Test;

import patterntesting.runtime.monitor.AbstractMonitorTest;

/**
 * JUnit tests for {@link ConnectionMonitor} class.
 * <p>
 * Note: The database and open connection for testing is set up in the
 * superclass.
 * </p>
 *
 * @author oliver (ob@aosd.de)
 * @version $Revision: 1.12 $
 * @since 1.3 (07.10.2012)
 */
public class ConnectionMonitorTest extends AbstractDbTest {

    private static final ConnectionMonitor monitor = ConnectionMonitor.getInstance();

    /**
     * Returns an object for testing.
     *
     * @return the object
     */
    @Override
    protected ConnectionMonitor getObject() {
        return monitor;
    }

    /**
     * Test method for {@link ConnectionMonitor#getOpenConnections()}.
     *
     * @throws SQLException the sQL exception
     */
    @Test
    public void testGetOpenConnections() throws SQLException {
        assertEquals(1, monitor.getOpenConnections());
    }

    /**
     * Test method for {@link ConnectionMonitor#getLastCallerStacktrace()}.
     */
    @Test
    public void testGetLastCallerStacktrace() {
        StackTraceElement[] stacktrace = monitor.getLastCallerStacktrace();
        assertTrue("expected: valid stacktrace", stacktrace.length > 0);
    }

    /**
     * Test method for {@link ConnectionMonitor#getCallerStacktraces()}.
     *
     * @throws OpenDataException the open data exception
     */
    @Test
    public void testGetCallerStacktraces() throws OpenDataException {
        TabularData stacktraces = monitor.getCallerStacktraces();
        assertEquals(1, stacktraces.size());
    }

    /**
     * Test method for {@link ConnectionMonitor#getClosedConnections()} and for
     * {@link ConnectionMonitor#getSumOfConnections()}.
     *
     * @throws SQLException the sQL exception
     */
    @Test
    public void testGetClosedConnections() throws SQLException {
        int expected = monitor.getSumOfConnections() - monitor.getOpenConnections();
        int closedConnections = monitor.getClosedConnections();
        assertEquals(expected, closedConnections);
        assertTrue("should be >= 0: " + closedConnections, closedConnections >= 0);
    }

    /**
     * Test method for {@link ConnectionMonitor#assertConnectionsClosed()}.
     */
    @Test(expected = AssertionError.class)
    public void testAssertConnectionsClosed() {
        ConnectionMonitor.assertConnectionsClosed();
    }

    /**
     * Test method for {@link ConnectionMonitor#getCallerOf(Connection)}.
     */
    @Test
    public void testGetCallerOf() {
        StackTraceElement caller = ConnectionMonitor.getCallerOf(connection);
        assertEquals("wrong caller: " + caller, "setUpConnection", caller.getMethodName());
    }

    /**
     * Test method for {@link ConnectionMonitor#getCallerOf(Connection)}. We
     * do not want to see the {@link ConnectionMonitor} as caller!
     *
     * @throws SQLException the SQL exception
     */
    @Test
    public void testGetCallerOfMonitoredConnection() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:hsqldb:mem:testdb");
        Connection proxyCon = ConnectionMonitor.getMonitoredConnection(con);
        try {
            StackTraceElement callerOf = ConnectionMonitor.getCallerOf(con);
            assertEquals(this.getClass().getName(), callerOf.getClassName());
        } finally {
            proxyCon.close();
        }
    }

    /**
     * Test method for {@link ConnectionMonitor#getCallers()}.
     */
    @Test
    public void testGetCallers() {
        StackTraceElement[] callers = monitor.getCallers();
        assertEquals(1, callers.length);
    }

    /**
     * Test method for {@link ConnectionMonitor#logCallerStacktraces()}. Watch
     * the log to see if it works.
     */
    @Test
    public void testLogStacktraces() {
    	monitor.logCallerStacktraces();
    }

    /**
     * Test method for {@link ConnectionMonitor#addMeAsShutdownHook()}.
     */
    @Test
    public void testAddMeAsShutdownhook() {
        AbstractMonitorTest.testAddAsShutdownHook(monitor);
    }

    /**
     * Watch the log to see if the {@link ConnectionMonitor#run()} produces
     * the expected output.
     */
    @Test
    public void testRun() {
        monitor.run();
    }

    /**
     * Test method for {@link AbstractMonitor#dumpMe(File)}.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testDumpMe() throws IOException {
        AbstractMonitorTest.checkDumpMe(monitor, 4);
    }

}

