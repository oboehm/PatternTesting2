package patterntesting.exception.sql;

import java.sql.SQLException;

/**
 * The class SqlExceptionHelper extends the SQL exception message
 * with the error-causing SQL command.
 * 
 * @author Christian Heise
 *
 */
public class SqlExceptionHelper {
	
	/** Only utility class, no constructor necessary */
	private SqlExceptionHelper() { };
	
	/**
	 * If the default SQLException does not contains the SQL
	 * command it will be appended to the original exception
	 * message.
	 * @param originalException the original SQLException
	 * @param sqlCommand the SQL command with failed to execute
	 * @return a SQLException with the error-causing SQL command
	 */
	public static SQLException getBetterSqlException(
			final SQLException originalException,
			final String sqlCommand) {
		String message = originalException.getMessage();
		if (!message.toLowerCase().contains(sqlCommand.toLowerCase())) {
			StringBuffer buf = new StringBuffer(message);
			buf.append(" (SQL command: ");
			buf.append(sqlCommand);
			buf.append(" )");
			message = buf.toString();
		}
		return new SQLException(message);
	}
	
}
