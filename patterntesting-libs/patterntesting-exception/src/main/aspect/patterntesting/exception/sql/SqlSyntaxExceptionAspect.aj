package patterntesting.exception.sql;

import java.sql.SQLException;
import java.sql.Statement;


/**
 * An {@link java.sql.SQLException} itself does not contain any information about
 * the executed (and failed) SQL query. The necessary data is contained
 * in the {@link java.sql.Statement} that initiated the execution of the query. 
 * The SqlSyntaxExceptionAspect extends the SQLException message with the SQL command
 * of the exception-causing Statement class.
 * 
 * Because the {@link java.sql.Statement} is an interface, the incoming SQL queries
 * have to be bind to a unique field of the implementing class to assign it to the
 * exception later on. 
 * 
 * @author Christian Heise
 *
 */
public aspect SqlSyntaxExceptionAspect {
	pointcut singleParameterCommands(Statement statement, String query):
		target(statement)
		&& call (* java.sql.Statement+.execute*(String) throws SQLException)		
	    && args (query);

	Object around(Statement statement, String query) throws SQLException :
		singleParameterCommands(statement, query){
		try {
			return proceed(statement, query);
		} catch (SQLException e) {
			throw SqlExceptionHelper.getBetterSqlException(e, query);
		}
	}
}
