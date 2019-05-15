package patterntesting.exception.sql;

import org.junit.jupiter.api.Test;
import java.sql.*;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests for SqlExceptionAspect class.
 *
 * @author Christian Heise
 *
 */
public class SqlSyntaxExceptionTest{

    /**
	 * The simplest case: Append sql command to message.
	 */
	@Test
	public void testSingleParameterCall() {
		String query = "SELECT * FROM test";
		Statement statement = new DummyStatement();
		SQLException ex = assertThrows(SQLException.class, () -> {
			statement.execute(query);
			statement.close();
		});
		assertThat(ex.getMessage(), containsString(query));
	}

}
