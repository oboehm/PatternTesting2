package patterntesting.exception.sql;

import static org.junit.Assert.*;

import java.sql.*;

import org.junit.Test;


/**
 * Tests for SqlExceptionAspect class.
 *
 * @author Christian Heise
 *
 */
public class SqlSyntaxExceptionTest{

    /**
	 * The simplest case: Append sql command to message.
	 *
	 * @throws ClassNotFoundException thrown when hsqldb is not in classpath
	 */
	@Test
	public void testSingleParameterCall() throws ClassNotFoundException {
		String query = "SELECT * FROM test";
		try {
			Statement statement = new DummyStatement();
			statement.execute(query);
			statement.close();
			fail("should fail: " + query);
		} catch (SQLException e) {
            assertTrue("'" + query + "' missing in " + e, e.getMessage().contains(query));
		}
	}

}
