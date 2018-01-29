/*
 * $Id: StasiPreparedStatement.java,v 1.11 2016/12/18 20:19:39 oboehm Exp $
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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;

import patterntesting.runtime.log.LogWatch;
import patterntesting.runtime.monitor.ProfileMonitor;
import patterntesting.runtime.monitor.db.SqlStatistic;

/**
 * A simple wrapper for {@link PreparedStatement} to be able to find resource
 * problems while reading and writing to the database. It allows us also to
 * measure times of SQL statements.
 * <p>
 * Why the name "Stasi..."? The Stasi was the official state security service of
 * Eastern Germany which controls the people (like NSA in the U.S. or KGB in
 * Russia, see also <a href="http://en.wikipedia.org/wiki/Stasi">Wikipedia</a>).
 * The StasiPreparedStatement controls the embedded {@link PreparedStatement} -
 * therefore the name.
 * </p>
 *
 * @author oliver
 * @version $Revision: 1.11 $
 * @since 1.4.1 (05.04.2014)
 */
public final class StasiPreparedStatement extends StasiStatement implements PreparedStatement {

	private static final Logger LOG = LogManager.getLogger(StasiPreparedStatement.class);
	private final LogWatch logWatch = new LogWatch();
	private final PreparedStatement preparedStatement;

	private final String sqlTemplate;
	private final Map<Integer, Object> parameters = new HashMap<>();

	/**
	 * Instantiates a new stasi prepared statement.
	 *
	 * @param statement
	 *            the statement
	 * @param args
	 *            the arguments for the creation of the
	 *            {@link PreparedStatement}
	 */
	public StasiPreparedStatement(final PreparedStatement statement, final Object... args) {
		super(statement);
		this.preparedStatement = statement;
		this.sqlTemplate = (String) args[0];
	}

	/**
	 * In some cicrumstances you may want to acces the original
	 * {@link PreparedStatement} directly. Use this method to get it.
	 *
	 * @return the wrapped {@link PreparedStatement}
	 * @since 1.6.2
	 */
	public PreparedStatement getWrappedPreparedStatement() {
		return this.preparedStatement;
	}

	/**
	 * Adds the batch.
	 *
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#addBatch()
	 */
	@Override
	public void addBatch() throws SQLException {
		this.preparedStatement.addBatch();
		LOG.trace("Batch added.");
	}

	/**
	 * Clear parameters.
	 *
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#clearParameters()
	 */
	@Override
	public void clearParameters() throws SQLException {
		this.preparedStatement.clearParameters();
		this.parameters.clear();
		LOG.trace("Parameters cleared.");
	}

	/**
	 * Close the statement. Because we want to log the SQL we do not call the
	 * superclass here but do the close ourself.
	 *
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.Statement#close()
	 */
	@Override
	public void close() throws SQLException {
		this.getStatement().close();
		LOG.debug("Statement for \"{}\" was closed after {}.", this.sqlTemplate, logWatch);
	}

	/**
	 * Execute.
	 *
	 * @return true, if successful
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#execute()
	 */
	@Override
	public boolean execute() throws SQLException {
		ProfileMonitor mon = SqlStatistic.start(this.sqlTemplate);
		try {
			boolean ok = this.preparedStatement.execute();
			SqlStatistic.stop(mon, this.getSQL4Logging(), ok);
			return ok;
		} catch (SQLException ex) {
			throw enrichedSQLException(mon, ex);
		}
	}

	/**
	 * Execute query.
	 *
	 * @return the result set
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#executeQuery()
	 */
	@Override
	public ResultSet executeQuery() throws SQLException {
		ProfileMonitor mon = SqlStatistic.start(this.sqlTemplate);
		try {
			ResultSet rs = new StasiResultSet(this.preparedStatement.executeQuery());
			SqlStatistic.stop(mon, this.getSQL4Logging(), rs);
			return rs;
		} catch (SQLException ex) {
			throw enrichedSQLException(mon, ex);
		}
	}

	/**
	 * Execute update.
	 *
	 * @return the int
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#executeUpdate()
	 */
	@Override
	public int executeUpdate() throws SQLException {
		ProfileMonitor mon = SqlStatistic.start(this.sqlTemplate);
		try {
			int ret = this.preparedStatement.executeUpdate();
			saveUpdateStatistic(mon, this.getSQL4Logging(), ret);
			return ret;
		} catch (SQLException ex) {
			throw enrichedSQLException(mon, ex);
		}
	}

	private SQLException enrichedSQLException(final ProfileMonitor mon, final SQLException original) {
		return enrichedSQLException(mon, this.resolveSQL(), original);
	}

	/**
	 * Gets the meta data.
	 *
	 * @return the meta data
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#getMetaData()
	 */
	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return this.preparedStatement.getMetaData();
	}

	/**
	 * Gets the parameter meta data.
	 *
	 * @return the parameter meta data
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#getParameterMetaData()
	 */
	@Override
	public ParameterMetaData getParameterMetaData() throws SQLException {
		return this.preparedStatement.getParameterMetaData();
	}

	/**
	 * Sets the array.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setArray(int, java.sql.Array)
	 */
	@Override
	public void setArray(final int arg0, final Array arg1) throws SQLException {
		this.preparedStatement.setArray(arg0, arg1);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the ascii stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream,
	 *      int)
	 */
	@Override
	public void setAsciiStream(final int arg0, final InputStream arg1, final int arg2) throws SQLException {
		this.preparedStatement.setAsciiStream(arg0, arg1, arg2);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the ascii stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream,
	 *      long)
	 */
	@Override
	public void setAsciiStream(final int arg0, final InputStream arg1, final long arg2) throws SQLException {
		this.preparedStatement.setAsciiStream(arg0, arg1, arg2);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the ascii stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream)
	 */
	@Override
	public void setAsciiStream(final int arg0, final InputStream arg1) throws SQLException {
		this.preparedStatement.setAsciiStream(arg0, arg1);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the big decimal.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setBigDecimal(int, java.math.BigDecimal)
	 */
	@Override
	public void setBigDecimal(final int arg0, final BigDecimal arg1) throws SQLException {
		this.preparedStatement.setBigDecimal(arg0, arg1);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the binary stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream,
	 *      int)
	 */
	@Override
	public void setBinaryStream(final int arg0, final InputStream arg1, final int arg2) throws SQLException {
		this.preparedStatement.setBinaryStream(arg0, arg1, arg2);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the binary stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream,
	 *      long)
	 */
	@Override
	public void setBinaryStream(final int arg0, final InputStream arg1, final long arg2) throws SQLException {
		this.preparedStatement.setBinaryStream(arg0, arg1, arg2);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the binary stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream)
	 */
	@Override
	public void setBinaryStream(final int arg0, final InputStream arg1) throws SQLException {
		this.preparedStatement.setBinaryStream(arg0, arg1);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the blob.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setBlob(int, java.sql.Blob)
	 */
	@Override
	public void setBlob(final int arg0, final Blob arg1) throws SQLException {
		this.preparedStatement.setBlob(arg0, arg1);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the blob.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setBlob(int, java.io.InputStream, long)
	 */
	@Override
	public void setBlob(final int arg0, final InputStream arg1, final long arg2) throws SQLException {
		this.preparedStatement.setBlob(arg0, arg1, arg2);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the blob.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setBlob(int, java.io.InputStream)
	 */
	@Override
	public void setBlob(final int arg0, final InputStream arg1) throws SQLException {
		this.preparedStatement.setBlob(arg0, arg1);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the boolean.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setBoolean(int, boolean)
	 */
	@Override
	public void setBoolean(final int arg0, final boolean arg1) throws SQLException {
		this.preparedStatement.setBoolean(arg0, arg1);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the byte.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setByte(int, byte)
	 */
	@Override
	public void setByte(final int arg0, final byte arg1) throws SQLException {
		this.preparedStatement.setByte(arg0, arg1);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the bytes.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setBytes(int, byte[])
	 */
	@Override
	public void setBytes(final int arg0, final byte[] arg1) throws SQLException {
		this.preparedStatement.setBytes(arg0, arg1);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the character stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader,
	 *      int)
	 */
	@Override
	public void setCharacterStream(final int arg0, final Reader arg1, final int arg2) throws SQLException {
		this.preparedStatement.setCharacterStream(arg0, arg1, arg2);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the character stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader,
	 *      long)
	 */
	@Override
	public void setCharacterStream(final int arg0, final Reader arg1, final long arg2) throws SQLException {
		this.preparedStatement.setCharacterStream(arg0, arg1, arg2);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the character stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader)
	 */
	@Override
	public void setCharacterStream(final int arg0, final Reader arg1) throws SQLException {
		this.preparedStatement.setCharacterStream(arg0, arg1);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the clob.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setClob(int, java.sql.Clob)
	 */
	@Override
	public void setClob(final int arg0, final Clob arg1) throws SQLException {
		this.preparedStatement.setClob(arg0, arg1);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the clob.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setClob(int, java.io.Reader, long)
	 */
	@Override
	public void setClob(final int arg0, final Reader arg1, final long arg2) throws SQLException {
		this.preparedStatement.setClob(arg0, arg1, arg2);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the clob.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setClob(int, java.io.Reader)
	 */
	@Override
	public void setClob(final int arg0, final Reader arg1) throws SQLException {
		this.preparedStatement.setClob(arg0, arg1);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the date.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setDate(int, java.sql.Date,
	 *      java.util.Calendar)
	 */
	@Override
	public void setDate(final int arg0, final Date arg1, final Calendar arg2) throws SQLException {
		this.preparedStatement.setDate(arg0, arg1, arg2);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the date.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setDate(int, java.sql.Date)
	 */
	@Override
	public void setDate(final int arg0, final Date arg1) throws SQLException {
		this.preparedStatement.setDate(arg0, arg1);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the double.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setDouble(int, double)
	 */
	@Override
	public void setDouble(final int arg0, final double arg1) throws SQLException {
		this.preparedStatement.setDouble(arg0, arg1);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the float.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setFloat(int, float)
	 */
	@Override
	public void setFloat(final int arg0, final float arg1) throws SQLException {
		this.preparedStatement.setFloat(arg0, arg1);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the int.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setInt(int, int)
	 */
	@Override
	public void setInt(final int arg0, final int arg1) throws SQLException {
		this.preparedStatement.setInt(arg0, arg1);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the long.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setLong(int, long)
	 */
	@Override
	public void setLong(final int arg0, final long arg1) throws SQLException {
		this.preparedStatement.setLong(arg0, arg1);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the n character stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader,
	 *      long)
	 */
	@Override
	public void setNCharacterStream(final int arg0, final Reader arg1, final long arg2) throws SQLException {
		this.preparedStatement.setNCharacterStream(arg0, arg1, arg2);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the n character stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader)
	 */
	@Override
	public void setNCharacterStream(final int arg0, final Reader arg1) throws SQLException {
		this.preparedStatement.setNCharacterStream(arg0, arg1);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the n clob.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setNClob(int, java.sql.NClob)
	 */
	@Override
	public void setNClob(final int arg0, final NClob arg1) throws SQLException {
		this.preparedStatement.setNClob(arg0, arg1);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the n clob.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setNClob(int, java.io.Reader, long)
	 */
	@Override
	public void setNClob(final int arg0, final Reader arg1, final long arg2) throws SQLException {
		this.preparedStatement.setNClob(arg0, arg1, arg2);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the n clob.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setNClob(int, java.io.Reader)
	 */
	@Override
	public void setNClob(final int arg0, final Reader arg1) throws SQLException {
		this.preparedStatement.setNClob(arg0, arg1);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the n string.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setNString(int, java.lang.String)
	 */
	@Override
	public void setNString(final int arg0, final String arg1) throws SQLException {
		this.preparedStatement.setNString(arg0, arg1);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the null.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setNull(int, int, java.lang.String)
	 */
	@Override
	public void setNull(final int arg0, final int arg1, final String arg2) throws SQLException {
		this.preparedStatement.setNull(arg0, arg1, arg2);
		this.parameters.put(arg0, "'null'");
	}

	/**
	 * Sets the null.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setNull(int, int)
	 */
	@Override
	public void setNull(final int arg0, final int arg1) throws SQLException {
		this.preparedStatement.setNull(arg0, arg1);
		this.parameters.put(arg0, "'null'");
	}

	/**
	 * Sets the object.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @param arg3
	 *            the arg3
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int,
	 *      int)
	 */
	@Override
	public void setObject(final int arg0, final Object arg1, final int arg2, final int arg3) throws SQLException {
		this.preparedStatement.setObject(arg0, arg1, arg2, arg3);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the object.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int)
	 */
	@Override
	public void setObject(final int arg0, final Object arg1, final int arg2) throws SQLException {
		this.preparedStatement.setObject(arg0, arg1, arg2);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the object.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object)
	 */
	@Override
	public void setObject(final int arg0, final Object arg1) throws SQLException {
		this.preparedStatement.setObject(arg0, arg1);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the ref.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setRef(int, java.sql.Ref)
	 */
	@Override
	public void setRef(final int arg0, final Ref arg1) throws SQLException {
		this.preparedStatement.setRef(arg0, arg1);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the row id.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setRowId(int, java.sql.RowId)
	 */
	@Override
	public void setRowId(final int arg0, final RowId arg1) throws SQLException {
		this.preparedStatement.setRowId(arg0, arg1);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the sqlxml.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setSQLXML(int, java.sql.SQLXML)
	 */
	@Override
	public void setSQLXML(final int arg0, final SQLXML arg1) throws SQLException {
		this.preparedStatement.setSQLXML(arg0, arg1);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the short.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setShort(int, short)
	 */
	@Override
	public void setShort(final int arg0, final short arg1) throws SQLException {
		this.preparedStatement.setShort(arg0, arg1);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the string.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setString(int, java.lang.String)
	 */
	@Override
	public void setString(final int arg0, final String arg1) throws SQLException {
		this.preparedStatement.setString(arg0, arg1);
		this.parameters.put(arg0, "'" + arg1 + "'");
	}

	/**
	 * Sets the time.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setTime(int, java.sql.Time,
	 *      java.util.Calendar)
	 */
	@Override
	public void setTime(final int arg0, final Time arg1, final Calendar arg2) throws SQLException {
		this.preparedStatement.setTime(arg0, arg1, arg2);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the time.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setTime(int, java.sql.Time)
	 */
	@Override
	public void setTime(final int arg0, final Time arg1) throws SQLException {
		this.preparedStatement.setTime(arg0, arg1);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the timestamp.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp,
	 *      java.util.Calendar)
	 */
	@Override
	public void setTimestamp(final int arg0, final Timestamp arg1, final Calendar arg2) throws SQLException {
		this.preparedStatement.setTimestamp(arg0, arg1, arg2);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the timestamp.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp)
	 */
	@Override
	public void setTimestamp(final int arg0, final Timestamp arg1) throws SQLException {
		this.preparedStatement.setTimestamp(arg0, arg1);
		this.parameters.put(arg0, arg1);
	}

	/**
	 * Sets the url.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the sQL exception
	 * @see java.sql.PreparedStatement#setURL(int, java.net.URL)
	 */
	@Override
	public void setURL(final int arg0, final URL arg1) throws SQLException {
		this.preparedStatement.setURL(arg0, arg1);
	}

	/**
	 * Sets the unicode stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	@SuppressWarnings("deprecation")
	public void setUnicodeStream(final int arg0, final InputStream arg1, final int arg2) throws SQLException {
		this.preparedStatement.setUnicodeStream(arg0, arg1, arg2);
	}

	/**
	 * We want to return the real SQL as string represenation.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.resolveSQL();
	}

	private String resolveSQL() {
		String[] elements = StringUtils.split(this.sqlTemplate + " ", '?');
		StringBuilder buf = new StringBuilder(elements[0]);
		for (int i = 1; i < elements.length; i++) {
			buf.append(this.getSqlParameter(i));
			buf.append(elements[i]);
		}
		return buf.toString().trim();
	}

	private Object getSqlParameter(final int n) {
		Object param = this.parameters.get(n);
		if (param == null) {
			return '?';
		}
		return param;
	}

	/**
	 * Because the resolving of the sent SQL needs time it is only done if log
	 * level is set to DEBUG for the {@link SqlStatistic}.
	 *
	 * @return the SQL 4 logging
	 */
	private String getSQL4Logging() {
		if (LogManager.getLogger(SqlStatistic.class).isDebugEnabled()) {
			return this.resolveSQL();
		} else {
			return this.sqlTemplate;
		}
	}

}
