/*
 * $Id: StasiResultSet.java,v 1.14 2016/12/27 07:40:46 oboehm Exp $
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

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import org.apache.logging.log4j.*;

import patterntesting.runtime.junit.ObjectTester;
import patterntesting.runtime.log.LogWatch;

/**
 * A simple wrapper for {@link ResultSet} to be able to find resource problems
 * while iterating through a {@link ResultSet}.
 * <p>
 * Why the name "Stasi..."? The Stasi was the official state security service of
 * Eastern Germany which controls the people (like NSA in the U.S. or KGB in
 * Russia, see also <a href="http://en.wikipedia.org/wiki/Stasi">Wikipedia</a>).
 * The StasiResultSet controls the embedded {@link ResultSet} - therefore the
 * name.
 * </p>
 *
 * @author oliver
 * @since 1.4.1 (15.04.2014)
 */
public final class StasiResultSet implements ResultSet {

	private static final Logger LOG = LogManager.getLogger(StasiResultSet.class);
	private final ResultSet resultSet;
	private final LogWatch logWatch = new LogWatch();

	/**
	 * Instantiates a new stasi result set.
	 *
	 * @param rs
	 *            the rs
	 */
	public StasiResultSet(final ResultSet rs) {
		this.resultSet = rs;
	}

	/**
	 * In some cicrumstances you may want to acces the original
	 * {@link ResultSet} directly. Use this method to get it.
	 *
	 * @return the wrapped {@link ResultSet}
	 * @since 1.6.2
	 */
	public ResultSet getWrappedResultSet() {
		return this.resultSet;
	}

	/**
	 * Absolute.
	 *
	 * @param arg0
	 *            the arg0
	 * @return true, if successful
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#absolute(int)
	 */
	@Override
	public boolean absolute(final int arg0) throws SQLException {
		return this.resultSet.absolute(arg0);
	}

	/**
	 * After last.
	 *
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#afterLast()
	 */
	@Override
	public void afterLast() throws SQLException {
		this.resultSet.afterLast();
	}

	/**
	 * Before first.
	 *
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#beforeFirst()
	 */
	@Override
	public void beforeFirst() throws SQLException {
		this.resultSet.beforeFirst();
	}

	/**
	 * Cancel row updates.
	 *
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#cancelRowUpdates()
	 */
	@Override
	public void cancelRowUpdates() throws SQLException {
		this.resultSet.cancelRowUpdates();
	}

	/**
	 * Clear warnings.
	 *
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#clearWarnings()
	 */
	@Override
	public void clearWarnings() throws SQLException {
		this.resultSet.clearWarnings();
	}

	/**
	 * Close.
	 *
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#close()
	 */
	@Override
	public void close() throws SQLException {
		this.resultSet.close();
		LOG.debug("{} was closed after {}.", this, logWatch);
	}

	/**
	 * Delete row.
	 *
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#deleteRow()
	 */
	@Override
	public void deleteRow() throws SQLException {
		this.resultSet.deleteRow();
	}

	/**
	 * Find column.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the int
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#findColumn(java.lang.String)
	 */
	@Override
	public int findColumn(final String arg0) throws SQLException {
		return this.resultSet.findColumn(arg0);
	}

	/**
	 * First.
	 *
	 * @return true, if successful
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#first()
	 */
	@Override
	public boolean first() throws SQLException {
		return this.resultSet.first();
	}

	/**
	 * Gets the array.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the array
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getArray(int)
	 */
	@Override
	public Array getArray(final int arg0) throws SQLException {
		return this.resultSet.getArray(arg0);
	}

	/**
	 * Gets the array.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the array
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getArray(java.lang.String)
	 */
	@Override
	public Array getArray(final String arg0) throws SQLException {
		return this.resultSet.getArray(arg0);
	}

	/**
	 * Gets the ascii stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the ascii stream
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getAsciiStream(int)
	 */
	@Override
	public InputStream getAsciiStream(final int arg0) throws SQLException {
		return this.resultSet.getAsciiStream(arg0);
	}

	/**
	 * Gets the ascii stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the ascii stream
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getAsciiStream(java.lang.String)
	 */
	@Override
	public InputStream getAsciiStream(final String arg0) throws SQLException {
		return this.resultSet.getAsciiStream(arg0);
	}

	/**
	 * Gets the big decimal.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @return the big decimal
	 * @throws SQLException
	 *             the SQL exception
	 * @deprecated use {@link #getBigDecimal(int)}
	 */
	@Override
	@Deprecated
	public BigDecimal getBigDecimal(final int arg0, final int arg1) throws SQLException {
		return this.resultSet.getBigDecimal(arg0, arg1);
	}

	/**
	 * Gets the big decimal.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the big decimal
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getBigDecimal(int)
	 */
	@Override
	public BigDecimal getBigDecimal(final int arg0) throws SQLException {
		return this.resultSet.getBigDecimal(arg0);
	}

	/**
	 * Gets the big decimal.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @return the big decimal
	 * @throws SQLException
	 *             the SQL exception
	 * @deprecated use {@link #getBigDecimal(int)}
	 */
	@Override
	@Deprecated
	public BigDecimal getBigDecimal(final String arg0, final int arg1) throws SQLException {
		return this.resultSet.getBigDecimal(arg0, arg1);
	}

	/**
	 * Gets the big decimal.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the big decimal
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getBigDecimal(java.lang.String)
	 */
	@Override
	public BigDecimal getBigDecimal(final String arg0) throws SQLException {
		return this.resultSet.getBigDecimal(arg0);
	}

	/**
	 * Gets the binary stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the binary stream
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getBinaryStream(int)
	 */
	@Override
	public InputStream getBinaryStream(final int arg0) throws SQLException {
		return this.resultSet.getBinaryStream(arg0);
	}

	/**
	 * Gets the binary stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the binary stream
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getBinaryStream(java.lang.String)
	 */
	@Override
	public InputStream getBinaryStream(final String arg0) throws SQLException {
		return this.resultSet.getBinaryStream(arg0);
	}

	/**
	 * Gets the blob.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the blob
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getBlob(int)
	 */
	@Override
	public Blob getBlob(final int arg0) throws SQLException {
		return this.resultSet.getBlob(arg0);
	}

	/**
	 * Gets the blob.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the blob
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getBlob(java.lang.String)
	 */
	@Override
	public Blob getBlob(final String arg0) throws SQLException {
		return this.resultSet.getBlob(arg0);
	}

	/**
	 * Gets the boolean.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the boolean
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getBoolean(int)
	 */
	@Override
	public boolean getBoolean(final int arg0) throws SQLException {
		return this.resultSet.getBoolean(arg0);
	}

	/**
	 * Gets the boolean.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the boolean
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getBoolean(java.lang.String)
	 */
	@Override
	public boolean getBoolean(final String arg0) throws SQLException {
		return this.resultSet.getBoolean(arg0);
	}

	/**
	 * Gets the byte.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the byte
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getByte(int)
	 */
	@Override
	public byte getByte(final int arg0) throws SQLException {
		return this.resultSet.getByte(arg0);
	}

	/**
	 * Gets the byte.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the byte
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getByte(java.lang.String)
	 */
	@Override
	public byte getByte(final String arg0) throws SQLException {
		return this.resultSet.getByte(arg0);
	}

	/**
	 * Gets the bytes.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the bytes
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getBytes(int)
	 */
	@Override
	public byte[] getBytes(final int arg0) throws SQLException {
		return this.resultSet.getBytes(arg0);
	}

	/**
	 * Gets the bytes.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the bytes
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getBytes(java.lang.String)
	 */
	@Override
	public byte[] getBytes(final String arg0) throws SQLException {
		return this.resultSet.getBytes(arg0);
	}

	/**
	 * Gets the character stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the character stream
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getCharacterStream(int)
	 */
	@Override
	public Reader getCharacterStream(final int arg0) throws SQLException {
		return this.resultSet.getCharacterStream(arg0);
	}

	/**
	 * Gets the character stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the character stream
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getCharacterStream(java.lang.String)
	 */
	@Override
	public Reader getCharacterStream(final String arg0) throws SQLException {
		return this.resultSet.getCharacterStream(arg0);
	}

	/**
	 * Gets the clob.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the clob
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getClob(int)
	 */
	@Override
	public Clob getClob(final int arg0) throws SQLException {
		return this.resultSet.getClob(arg0);
	}

	/**
	 * Gets the clob.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the clob
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getClob(java.lang.String)
	 */
	@Override
	public Clob getClob(final String arg0) throws SQLException {
		return this.resultSet.getClob(arg0);
	}

	/**
	 * Gets the concurrency.
	 *
	 * @return the concurrency
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getConcurrency()
	 */
	@Override
	public int getConcurrency() throws SQLException {
		return this.resultSet.getConcurrency();
	}

	/**
	 * Gets the cursor name.
	 *
	 * @return the cursor name
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getCursorName()
	 */
	@Override
	public String getCursorName() throws SQLException {
		return this.resultSet.getCursorName();
	}

	/**
	 * Gets the date.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @return the date
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getDate(int, java.util.Calendar)
	 */
	@Override
	public Date getDate(final int arg0, final Calendar arg1) throws SQLException {
		return this.resultSet.getDate(arg0, arg1);
	}

	/**
	 * Gets the date.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the date
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getDate(int)
	 */
	@Override
	public Date getDate(final int arg0) throws SQLException {
		return this.resultSet.getDate(arg0);
	}

	/**
	 * Gets the date.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @return the date
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getDate(java.lang.String, java.util.Calendar)
	 */
	@Override
	public Date getDate(final String arg0, final Calendar arg1) throws SQLException {
		return this.resultSet.getDate(arg0, arg1);
	}

	/**
	 * Gets the date.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the date
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getDate(java.lang.String)
	 */
	@Override
	public Date getDate(final String arg0) throws SQLException {
		return this.resultSet.getDate(arg0);
	}

	/**
	 * Gets the double.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the double
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getDouble(int)
	 */
	@Override
	public double getDouble(final int arg0) throws SQLException {
		return this.resultSet.getDouble(arg0);
	}

	/**
	 * Gets the double.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the double
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getDouble(java.lang.String)
	 */
	@Override
	public double getDouble(final String arg0) throws SQLException {
		return this.resultSet.getDouble(arg0);
	}

	/**
	 * Gets the fetch direction.
	 *
	 * @return the fetch direction
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getFetchDirection()
	 */
	@Override
	public int getFetchDirection() throws SQLException {
		return this.resultSet.getFetchDirection();
	}

	/**
	 * Gets the fetch size.
	 *
	 * @return the fetch size
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getFetchSize()
	 */
	@Override
	public int getFetchSize() throws SQLException {
		return this.resultSet.getFetchSize();
	}

	/**
	 * Gets the float.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the float
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getFloat(int)
	 */
	@Override
	public float getFloat(final int arg0) throws SQLException {
		return this.resultSet.getFloat(arg0);
	}

	/**
	 * Gets the float.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the float
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getFloat(java.lang.String)
	 */
	@Override
	public float getFloat(final String arg0) throws SQLException {
		return this.resultSet.getFloat(arg0);
	}

	/**
	 * Gets the holdability.
	 *
	 * @return the holdability
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getHoldability()
	 */
	@Override
	public int getHoldability() throws SQLException {
		return this.resultSet.getHoldability();
	}

	/**
	 * Gets the int.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the int
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getInt(int)
	 */
	@Override
	public int getInt(final int arg0) throws SQLException {
		return this.resultSet.getInt(arg0);
	}

	/**
	 * Gets the int.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the int
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getInt(java.lang.String)
	 */
	@Override
	public int getInt(final String arg0) throws SQLException {
		return this.resultSet.getInt(arg0);
	}

	/**
	 * Gets the long.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the long
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getLong(int)
	 */
	@Override
	public long getLong(final int arg0) throws SQLException {
		return this.resultSet.getLong(arg0);
	}

	/**
	 * Gets the long.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the long
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getLong(java.lang.String)
	 */
	@Override
	public long getLong(final String arg0) throws SQLException {
		return this.resultSet.getLong(arg0);
	}

	/**
	 * Gets the meta data.
	 *
	 * @return the meta data
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getMetaData()
	 */
	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return this.resultSet.getMetaData();
	}

	/**
	 * Gets the n character stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the n character stream
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getNCharacterStream(int)
	 */
	@Override
	public Reader getNCharacterStream(final int arg0) throws SQLException {
		return this.resultSet.getNCharacterStream(arg0);
	}

	/**
	 * Gets the n character stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the n character stream
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getNCharacterStream(java.lang.String)
	 */
	@Override
	public Reader getNCharacterStream(final String arg0) throws SQLException {
		return this.resultSet.getNCharacterStream(arg0);
	}

	/**
	 * Gets the n clob.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the n clob
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getNClob(int)
	 */
	@Override
	public NClob getNClob(final int arg0) throws SQLException {
		return this.resultSet.getNClob(arg0);
	}

	/**
	 * Gets the n clob.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the n clob
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getNClob(java.lang.String)
	 */
	@Override
	public NClob getNClob(final String arg0) throws SQLException {
		return this.resultSet.getNClob(arg0);
	}

	/**
	 * Gets the n string.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the n string
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getNString(int)
	 */
	@Override
	public String getNString(final int arg0) throws SQLException {
		return this.resultSet.getNString(arg0);
	}

	/**
	 * Gets the n string.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the n string
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getNString(java.lang.String)
	 */
	@Override
	public String getNString(final String arg0) throws SQLException {
		return this.resultSet.getNString(arg0);
	}

	/**
	 * Gets the object.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @return the object
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getObject(int, java.util.Map)
	 */
	@Override
	public Object getObject(final int arg0, final Map<String, Class<?>> arg1) throws SQLException {
		return this.resultSet.getObject(arg0, arg1);
	}

	/**
	 * Gets the object.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the object
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getObject(int)
	 */
	@Override
	public Object getObject(final int arg0) throws SQLException {
		return this.resultSet.getObject(arg0);
	}

	/**
	 * Gets the object.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @return the object
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getObject(java.lang.String, java.util.Map)
	 */
	@Override
	public Object getObject(final String arg0, final Map<String, Class<?>> arg1) throws SQLException {
		return this.resultSet.getObject(arg0, arg1);
	}

	/**
	 * Gets the object.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the object
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getObject(java.lang.String)
	 */
	@Override
	public Object getObject(final String arg0) throws SQLException {
		return this.resultSet.getObject(arg0);
	}

	/**
	 * Gets the ref.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the ref
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getRef(int)
	 */
	@Override
	public Ref getRef(final int arg0) throws SQLException {
		return this.resultSet.getRef(arg0);
	}

	/**
	 * Gets the ref.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the ref
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getRef(java.lang.String)
	 */
	@Override
	public Ref getRef(final String arg0) throws SQLException {
		return this.resultSet.getRef(arg0);
	}

	/**
	 * Gets the row.
	 *
	 * @return the row
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getRow()
	 */
	@Override
	public int getRow() throws SQLException {
		return this.resultSet.getRow();
	}

	/**
	 * Gets the row id.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the row id
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getRowId(int)
	 */
	@Override
	public RowId getRowId(final int arg0) throws SQLException {
		return this.resultSet.getRowId(arg0);
	}

	/**
	 * Gets the row id.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the row id
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getRowId(java.lang.String)
	 */
	@Override
	public RowId getRowId(final String arg0) throws SQLException {
		return this.resultSet.getRowId(arg0);
	}

	/**
	 * Gets the sqlxml.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the sqlxml
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getSQLXML(int)
	 */
	@Override
	public SQLXML getSQLXML(final int arg0) throws SQLException {
		return this.resultSet.getSQLXML(arg0);
	}

	/**
	 * Gets the sqlxml.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the sqlxml
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getSQLXML(java.lang.String)
	 */
	@Override
	public SQLXML getSQLXML(final String arg0) throws SQLException {
		return this.resultSet.getSQLXML(arg0);
	}

	/**
	 * Gets the short.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the short
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getShort(int)
	 */
	@Override
	public short getShort(final int arg0) throws SQLException {
		return this.resultSet.getShort(arg0);
	}

	/**
	 * Gets the short.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the short
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getShort(java.lang.String)
	 */
	@Override
	public short getShort(final String arg0) throws SQLException {
		return this.resultSet.getShort(arg0);
	}

	/**
	 * Gets the statement.
	 *
	 * @return the statement
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getStatement()
	 */
	@Override
	public Statement getStatement() throws SQLException {
		return this.resultSet.getStatement();
	}

	/**
	 * Gets the string.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the string
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getString(int)
	 */
	@Override
	public String getString(final int arg0) throws SQLException {
		return this.resultSet.getString(arg0);
	}

	/**
	 * Gets the string.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the string
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getString(java.lang.String)
	 */
	@Override
	public String getString(final String arg0) throws SQLException {
		return this.resultSet.getString(arg0);
	}

	/**
	 * Gets the time.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @return the time
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getTime(int, java.util.Calendar)
	 */
	@Override
	public Time getTime(final int arg0, final Calendar arg1) throws SQLException {
		return this.resultSet.getTime(arg0, arg1);
	}

	/**
	 * Gets the time.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the time
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getTime(int)
	 */
	@Override
	public Time getTime(final int arg0) throws SQLException {
		return this.resultSet.getTime(arg0);
	}

	/**
	 * Gets the time.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @return the time
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getTime(java.lang.String, java.util.Calendar)
	 */
	@Override
	public Time getTime(final String arg0, final Calendar arg1) throws SQLException {
		return this.resultSet.getTime(arg0, arg1);
	}

	/**
	 * Gets the time.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the time
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getTime(java.lang.String)
	 */
	@Override
	public Time getTime(final String arg0) throws SQLException {
		return this.resultSet.getTime(arg0);
	}

	/**
	 * Gets the timestamp.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @return the timestamp
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getTimestamp(int, java.util.Calendar)
	 */
	@Override
	public Timestamp getTimestamp(final int arg0, final Calendar arg1) throws SQLException {
		return this.resultSet.getTimestamp(arg0, arg1);
	}

	/**
	 * Gets the timestamp.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the timestamp
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getTimestamp(int)
	 */
	@Override
	public Timestamp getTimestamp(final int arg0) throws SQLException {
		return this.resultSet.getTimestamp(arg0);
	}

	/**
	 * Gets the timestamp.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @return the timestamp
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getTimestamp(java.lang.String,
	 *      java.util.Calendar)
	 */
	@Override
	public Timestamp getTimestamp(final String arg0, final Calendar arg1) throws SQLException {
		return this.resultSet.getTimestamp(arg0, arg1);
	}

	/**
	 * Gets the timestamp.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the timestamp
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getTimestamp(java.lang.String)
	 */
	@Override
	public Timestamp getTimestamp(final String arg0) throws SQLException {
		return this.resultSet.getTimestamp(arg0);
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getType()
	 */
	@Override
	public int getType() throws SQLException {
		return this.resultSet.getType();
	}

	/**
	 * Gets the url.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the url
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getURL(int)
	 */
	@Override
	public URL getURL(final int arg0) throws SQLException {
		return this.resultSet.getURL(arg0);
	}

	/**
	 * Gets the url.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the url
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getURL(java.lang.String)
	 */
	@Override
	public URL getURL(final String arg0) throws SQLException {
		return this.resultSet.getURL(arg0);
	}

	/**
	 * Gets the unicode stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the unicode stream
	 * @throws SQLException
	 *             the SQL exception
	 * @deprecated use normal encoding
	 */
	@Override
	@Deprecated
	public InputStream getUnicodeStream(final int arg0) throws SQLException {
		return this.resultSet.getUnicodeStream(arg0);
	}

	/**
	 * Gets the unicode stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @return the unicode stream
	 * @throws SQLException
	 *             the SQL exception
	 * @deprecated use normal encoding
	 */
	@Override
	@Deprecated
	public InputStream getUnicodeStream(final String arg0) throws SQLException {
		return this.resultSet.getUnicodeStream(arg0);
	}

	/**
	 * Gets the warnings.
	 *
	 * @return the warnings
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#getWarnings()
	 */
	@Override
	public SQLWarning getWarnings() throws SQLException {
		return this.resultSet.getWarnings();
	}

	/**
	 * Insert row.
	 *
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#insertRow()
	 */
	@Override
	public void insertRow() throws SQLException {
		this.resultSet.insertRow();
	}

	/**
	 * Checks if is after last.
	 *
	 * @return true, if is after last
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#isAfterLast()
	 */
	@Override
	public boolean isAfterLast() throws SQLException {
		return this.resultSet.isAfterLast();
	}

	/**
	 * Checks if is before first.
	 *
	 * @return true, if is before first
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#isBeforeFirst()
	 */
	@Override
	public boolean isBeforeFirst() throws SQLException {
		return this.resultSet.isBeforeFirst();
	}

	/**
	 * Checks if is closed.
	 *
	 * @return true, if is closed
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#isClosed()
	 */
	@Override
	public boolean isClosed() throws SQLException {
		return this.resultSet.isClosed();
	}

	/**
	 * Checks if is first.
	 *
	 * @return true, if is first
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#isFirst()
	 */
	@Override
	public boolean isFirst() throws SQLException {
		return this.resultSet.isFirst();
	}

	/**
	 * Checks if is last.
	 *
	 * @return true, if is last
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#isLast()
	 */
	@Override
	public boolean isLast() throws SQLException {
		return this.resultSet.isLast();
	}

	/**
	 * Checks if is wrapper for.
	 *
	 * @param arg0
	 *            the arg0
	 * @return true, if is wrapper for
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	@Override
	public boolean isWrapperFor(final Class<?> arg0) throws SQLException {
		return this.resultSet.isWrapperFor(arg0);
	}

	/**
	 * Last.
	 *
	 * @return true, if successful
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#last()
	 */
	@Override
	public boolean last() throws SQLException {
		return this.resultSet.last();
	}

	/**
	 * Move to current row.
	 *
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#moveToCurrentRow()
	 */
	@Override
	public void moveToCurrentRow() throws SQLException {
		this.resultSet.moveToCurrentRow();
	}

	/**
	 * Move to insert row.
	 *
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#moveToInsertRow()
	 */
	@Override
	public void moveToInsertRow() throws SQLException {
		this.resultSet.moveToInsertRow();
	}

	/**
	 * Next.
	 *
	 * @return true, if successful
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#next()
	 */
	@Override
	public boolean next() throws SQLException {
		return this.resultSet.next();
	}

	/**
	 * Previous.
	 *
	 * @return true, if successful
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#previous()
	 */
	@Override
	public boolean previous() throws SQLException {
		return this.resultSet.previous();
	}

	/**
	 * Refresh row.
	 *
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#refreshRow()
	 */
	@Override
	public void refreshRow() throws SQLException {
		this.resultSet.refreshRow();
	}

	/**
	 * Relative.
	 *
	 * @param arg0
	 *            the arg0
	 * @return true, if successful
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#relative(int)
	 */
	@Override
	public boolean relative(final int arg0) throws SQLException {
		return this.resultSet.relative(arg0);
	}

	/**
	 * Row deleted.
	 *
	 * @return true, if successful
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#rowDeleted()
	 */
	@Override
	public boolean rowDeleted() throws SQLException {
		return this.resultSet.rowDeleted();
	}

	/**
	 * Row inserted.
	 *
	 * @return true, if successful
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#rowInserted()
	 */
	@Override
	public boolean rowInserted() throws SQLException {
		return this.resultSet.rowInserted();
	}

	/**
	 * Row updated.
	 *
	 * @return true, if successful
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#rowUpdated()
	 */
	@Override
	public boolean rowUpdated() throws SQLException {
		return this.resultSet.rowUpdated();
	}

	/**
	 * Sets the fetch direction.
	 *
	 * @param arg0
	 *            the new fetch direction
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#setFetchDirection(int)
	 */
	@Override
	public void setFetchDirection(final int arg0) throws SQLException {
		this.resultSet.setFetchDirection(arg0);
	}

	/**
	 * Sets the fetch size.
	 *
	 * @param arg0
	 *            the new fetch size
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#setFetchSize(int)
	 */
	@Override
	public void setFetchSize(final int arg0) throws SQLException {
		this.resultSet.setFetchSize(arg0);
	}

	/**
	 * Unwrap.
	 *
	 * @param <T>
	 *            the generic type
	 * @param arg0
	 *            the arg0
	 * @return the t
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	@Override
	public <T> T unwrap(final Class<T> arg0) throws SQLException {
		return this.resultSet.unwrap(arg0);
	}

	/**
	 * Update array.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateArray(int, java.sql.Array)
	 */
	@Override
	public void updateArray(final int arg0, final Array arg1) throws SQLException {
		this.resultSet.updateArray(arg0, arg1);
	}

	/**
	 * Update array.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateArray(java.lang.String, java.sql.Array)
	 */
	@Override
	public void updateArray(final String arg0, final Array arg1) throws SQLException {
		this.resultSet.updateArray(arg0, arg1);
	}

	/**
	 * Update ascii stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateAsciiStream(int, java.io.InputStream, int)
	 */
	@Override
	public void updateAsciiStream(final int arg0, final InputStream arg1, final int arg2) throws SQLException {
		this.resultSet.updateAsciiStream(arg0, arg1, arg2);
	}

	/**
	 * Update ascii stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateAsciiStream(int, java.io.InputStream, long)
	 */
	@Override
	public void updateAsciiStream(final int arg0, final InputStream arg1, final long arg2) throws SQLException {
		this.resultSet.updateAsciiStream(arg0, arg1, arg2);
	}

	/**
	 * Update ascii stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateAsciiStream(int, java.io.InputStream)
	 */
	@Override
	public void updateAsciiStream(final int arg0, final InputStream arg1) throws SQLException {
		this.resultSet.updateAsciiStream(arg0, arg1);
	}

	/**
	 * Update ascii stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateAsciiStream(java.lang.String,
	 *      java.io.InputStream, int)
	 */
	@Override
	public void updateAsciiStream(final String arg0, final InputStream arg1, final int arg2) throws SQLException {
		this.resultSet.updateAsciiStream(arg0, arg1, arg2);
	}

	/**
	 * Update ascii stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateAsciiStream(java.lang.String,
	 *      java.io.InputStream, long)
	 */
	@Override
	public void updateAsciiStream(final String arg0, final InputStream arg1, final long arg2) throws SQLException {
		this.resultSet.updateAsciiStream(arg0, arg1, arg2);
	}

	/**
	 * Update ascii stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateAsciiStream(java.lang.String,
	 *      java.io.InputStream)
	 */
	@Override
	public void updateAsciiStream(final String arg0, final InputStream arg1) throws SQLException {
		this.resultSet.updateAsciiStream(arg0, arg1);
	}

	/**
	 * Update big decimal.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateBigDecimal(int, java.math.BigDecimal)
	 */
	@Override
	public void updateBigDecimal(final int arg0, final BigDecimal arg1) throws SQLException {
		this.resultSet.updateBigDecimal(arg0, arg1);
	}

	/**
	 * Update big decimal.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateBigDecimal(java.lang.String,
	 *      java.math.BigDecimal)
	 */
	@Override
	public void updateBigDecimal(final String arg0, final BigDecimal arg1) throws SQLException {
		this.resultSet.updateBigDecimal(arg0, arg1);
	}

	/**
	 * Update binary stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateBinaryStream(int, java.io.InputStream, int)
	 */
	@Override
	public void updateBinaryStream(final int arg0, final InputStream arg1, final int arg2) throws SQLException {
		this.resultSet.updateBinaryStream(arg0, arg1, arg2);
	}

	/**
	 * Update binary stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateBinaryStream(int, java.io.InputStream,
	 *      long)
	 */
	@Override
	public void updateBinaryStream(final int arg0, final InputStream arg1, final long arg2) throws SQLException {
		this.resultSet.updateBinaryStream(arg0, arg1, arg2);
	}

	/**
	 * Update binary stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateBinaryStream(int, java.io.InputStream)
	 */
	@Override
	public void updateBinaryStream(final int arg0, final InputStream arg1) throws SQLException {
		this.resultSet.updateBinaryStream(arg0, arg1);
	}

	/**
	 * Update binary stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateBinaryStream(java.lang.String,
	 *      java.io.InputStream, int)
	 */
	@Override
	public void updateBinaryStream(final String arg0, final InputStream arg1, final int arg2) throws SQLException {
		this.resultSet.updateBinaryStream(arg0, arg1, arg2);
	}

	/**
	 * Update binary stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateBinaryStream(java.lang.String,
	 *      java.io.InputStream, long)
	 */
	@Override
	public void updateBinaryStream(final String arg0, final InputStream arg1, final long arg2) throws SQLException {
		this.resultSet.updateBinaryStream(arg0, arg1, arg2);
	}

	/**
	 * Update binary stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateBinaryStream(java.lang.String,
	 *      java.io.InputStream)
	 */
	@Override
	public void updateBinaryStream(final String arg0, final InputStream arg1) throws SQLException {
		this.resultSet.updateBinaryStream(arg0, arg1);
	}

	/**
	 * Update blob.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateBlob(int, java.sql.Blob)
	 */
	@Override
	public void updateBlob(final int arg0, final Blob arg1) throws SQLException {
		this.resultSet.updateBlob(arg0, arg1);
	}

	/**
	 * Update blob.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateBlob(int, java.io.InputStream, long)
	 */
	@Override
	public void updateBlob(final int arg0, final InputStream arg1, final long arg2) throws SQLException {
		this.resultSet.updateBlob(arg0, arg1, arg2);
	}

	/**
	 * Update blob.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateBlob(int, java.io.InputStream)
	 */
	@Override
	public void updateBlob(final int arg0, final InputStream arg1) throws SQLException {
		this.resultSet.updateBlob(arg0, arg1);
	}

	/**
	 * Update blob.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateBlob(java.lang.String, java.sql.Blob)
	 */
	@Override
	public void updateBlob(final String arg0, final Blob arg1) throws SQLException {
		this.resultSet.updateBlob(arg0, arg1);
	}

	/**
	 * Update blob.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateBlob(java.lang.String, java.io.InputStream,
	 *      long)
	 */
	@Override
	public void updateBlob(final String arg0, final InputStream arg1, final long arg2) throws SQLException {
		this.resultSet.updateBlob(arg0, arg1, arg2);
	}

	/**
	 * Update blob.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateBlob(java.lang.String, java.io.InputStream)
	 */
	@Override
	public void updateBlob(final String arg0, final InputStream arg1) throws SQLException {
		this.resultSet.updateBlob(arg0, arg1);
	}

	/**
	 * Update boolean.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateBoolean(int, boolean)
	 */
	@Override
	public void updateBoolean(final int arg0, final boolean arg1) throws SQLException {
		this.resultSet.updateBoolean(arg0, arg1);
	}

	/**
	 * Update boolean.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateBoolean(java.lang.String, boolean)
	 */
	@Override
	public void updateBoolean(final String arg0, final boolean arg1) throws SQLException {
		this.resultSet.updateBoolean(arg0, arg1);
	}

	/**
	 * Update byte.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateByte(int, byte)
	 */
	@Override
	public void updateByte(final int arg0, final byte arg1) throws SQLException {
		this.resultSet.updateByte(arg0, arg1);
	}

	/**
	 * Update byte.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateByte(java.lang.String, byte)
	 */
	@Override
	public void updateByte(final String arg0, final byte arg1) throws SQLException {
		this.resultSet.updateByte(arg0, arg1);
	}

	/**
	 * Update bytes.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateBytes(int, byte[])
	 */
	@Override
	public void updateBytes(final int arg0, final byte[] arg1) throws SQLException {
		this.resultSet.updateBytes(arg0, arg1);
	}

	/**
	 * Update bytes.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateBytes(java.lang.String, byte[])
	 */
	@Override
	public void updateBytes(final String arg0, final byte[] arg1) throws SQLException {
		this.resultSet.updateBytes(arg0, arg1);
	}

	/**
	 * Update character stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateCharacterStream(int, java.io.Reader, int)
	 */
	@Override
	public void updateCharacterStream(final int arg0, final Reader arg1, final int arg2) throws SQLException {
		this.resultSet.updateCharacterStream(arg0, arg1, arg2);
	}

	/**
	 * Update character stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateCharacterStream(int, java.io.Reader, long)
	 */
	@Override
	public void updateCharacterStream(final int arg0, final Reader arg1, final long arg2) throws SQLException {
		this.resultSet.updateCharacterStream(arg0, arg1, arg2);
	}

	/**
	 * Update character stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateCharacterStream(int, java.io.Reader)
	 */
	@Override
	public void updateCharacterStream(final int arg0, final Reader arg1) throws SQLException {
		this.resultSet.updateCharacterStream(arg0, arg1);
	}

	/**
	 * Update character stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateCharacterStream(java.lang.String,
	 *      java.io.Reader, int)
	 */
	@Override
	public void updateCharacterStream(final String arg0, final Reader arg1, final int arg2) throws SQLException {
		this.resultSet.updateCharacterStream(arg0, arg1, arg2);
	}

	/**
	 * Update character stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateCharacterStream(java.lang.String,
	 *      java.io.Reader, long)
	 */
	@Override
	public void updateCharacterStream(final String arg0, final Reader arg1, final long arg2) throws SQLException {
		this.resultSet.updateCharacterStream(arg0, arg1, arg2);
	}

	/**
	 * Update character stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateCharacterStream(java.lang.String,
	 *      java.io.Reader)
	 */
	@Override
	public void updateCharacterStream(final String arg0, final Reader arg1) throws SQLException {
		this.resultSet.updateCharacterStream(arg0, arg1);
	}

	/**
	 * Update clob.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateClob(int, java.sql.Clob)
	 */
	@Override
	public void updateClob(final int arg0, final Clob arg1) throws SQLException {
		this.resultSet.updateClob(arg0, arg1);
	}

	/**
	 * Update clob.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateClob(int, java.io.Reader, long)
	 */
	@Override
	public void updateClob(final int arg0, final Reader arg1, final long arg2) throws SQLException {
		this.resultSet.updateClob(arg0, arg1, arg2);
	}

	/**
	 * Update clob.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateClob(int, java.io.Reader)
	 */
	@Override
	public void updateClob(final int arg0, final Reader arg1) throws SQLException {
		this.resultSet.updateClob(arg0, arg1);
	}

	/**
	 * Update clob.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateClob(java.lang.String, java.sql.Clob)
	 */
	@Override
	public void updateClob(final String arg0, final Clob arg1) throws SQLException {
		this.resultSet.updateClob(arg0, arg1);
	}

	/**
	 * Update clob.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateClob(java.lang.String, java.io.Reader,
	 *      long)
	 */
	@Override
	public void updateClob(final String arg0, final Reader arg1, final long arg2) throws SQLException {
		this.resultSet.updateClob(arg0, arg1, arg2);
	}

	/**
	 * Update clob.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateClob(java.lang.String, java.io.Reader)
	 */
	@Override
	public void updateClob(final String arg0, final Reader arg1) throws SQLException {
		this.resultSet.updateClob(arg0, arg1);
	}

	/**
	 * Update date.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateDate(int, java.sql.Date)
	 */
	@Override
	public void updateDate(final int arg0, final Date arg1) throws SQLException {
		this.resultSet.updateDate(arg0, arg1);
	}

	/**
	 * Update date.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateDate(java.lang.String, java.sql.Date)
	 */
	@Override
	public void updateDate(final String arg0, final Date arg1) throws SQLException {
		this.resultSet.updateDate(arg0, arg1);
	}

	/**
	 * Update double.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateDouble(int, double)
	 */
	@Override
	public void updateDouble(final int arg0, final double arg1) throws SQLException {
		this.resultSet.updateDouble(arg0, arg1);
	}

	/**
	 * Update double.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateDouble(java.lang.String, double)
	 */
	@Override
	public void updateDouble(final String arg0, final double arg1) throws SQLException {
		this.resultSet.updateDouble(arg0, arg1);
	}

	/**
	 * Update float.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateFloat(int, float)
	 */
	@Override
	public void updateFloat(final int arg0, final float arg1) throws SQLException {
		this.resultSet.updateFloat(arg0, arg1);
	}

	/**
	 * Update float.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateFloat(java.lang.String, float)
	 */
	@Override
	public void updateFloat(final String arg0, final float arg1) throws SQLException {
		this.resultSet.updateFloat(arg0, arg1);
	}

	/**
	 * Update int.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateInt(int, int)
	 */
	@Override
	public void updateInt(final int arg0, final int arg1) throws SQLException {
		this.resultSet.updateInt(arg0, arg1);
	}

	/**
	 * Update int.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateInt(java.lang.String, int)
	 */
	@Override
	public void updateInt(final String arg0, final int arg1) throws SQLException {
		this.resultSet.updateInt(arg0, arg1);
	}

	/**
	 * Update long.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateLong(int, long)
	 */
	@Override
	public void updateLong(final int arg0, final long arg1) throws SQLException {
		this.resultSet.updateLong(arg0, arg1);
	}

	/**
	 * Update long.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateLong(java.lang.String, long)
	 */
	@Override
	public void updateLong(final String arg0, final long arg1) throws SQLException {
		this.resultSet.updateLong(arg0, arg1);
	}

	/**
	 * Update n character stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateNCharacterStream(int, java.io.Reader, long)
	 */
	@Override
	public void updateNCharacterStream(final int arg0, final Reader arg1, final long arg2) throws SQLException {
		this.resultSet.updateNCharacterStream(arg0, arg1, arg2);
	}

	/**
	 * Update n character stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateNCharacterStream(int, java.io.Reader)
	 */
	@Override
	public void updateNCharacterStream(final int arg0, final Reader arg1) throws SQLException {
		this.resultSet.updateNCharacterStream(arg0, arg1);
	}

	/**
	 * Update n character stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateNCharacterStream(java.lang.String,
	 *      java.io.Reader, long)
	 */
	@Override
	public void updateNCharacterStream(final String arg0, final Reader arg1, final long arg2) throws SQLException {
		this.resultSet.updateNCharacterStream(arg0, arg1, arg2);
	}

	/**
	 * Update n character stream.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateNCharacterStream(java.lang.String,
	 *      java.io.Reader)
	 */
	@Override
	public void updateNCharacterStream(final String arg0, final Reader arg1) throws SQLException {
		this.resultSet.updateNCharacterStream(arg0, arg1);
	}

	/**
	 * Update n clob.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateNClob(int, java.sql.NClob)
	 */
	@Override
	public void updateNClob(final int arg0, final NClob arg1) throws SQLException {
		this.resultSet.updateNClob(arg0, arg1);
	}

	/**
	 * Update n clob.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateNClob(int, java.io.Reader, long)
	 */
	@Override
	public void updateNClob(final int arg0, final Reader arg1, final long arg2) throws SQLException {
		this.resultSet.updateNClob(arg0, arg1, arg2);
	}

	/**
	 * Update n clob.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateNClob(int, java.io.Reader)
	 */
	@Override
	public void updateNClob(final int arg0, final Reader arg1) throws SQLException {
		this.resultSet.updateNClob(arg0, arg1);
	}

	/**
	 * Update n clob.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateNClob(java.lang.String, java.sql.NClob)
	 */
	@Override
	public void updateNClob(final String arg0, final NClob arg1) throws SQLException {
		this.resultSet.updateNClob(arg0, arg1);
	}

	/**
	 * Update n clob.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateNClob(java.lang.String, java.io.Reader,
	 *      long)
	 */
	@Override
	public void updateNClob(final String arg0, final Reader arg1, final long arg2) throws SQLException {
		this.resultSet.updateNClob(arg0, arg1, arg2);
	}

	/**
	 * Update n clob.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateNClob(java.lang.String, java.io.Reader)
	 */
	@Override
	public void updateNClob(final String arg0, final Reader arg1) throws SQLException {
		this.resultSet.updateNClob(arg0, arg1);
	}

	/**
	 * Update n string.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateNString(int, java.lang.String)
	 */
	@Override
	public void updateNString(final int arg0, final String arg1) throws SQLException {
		this.resultSet.updateNString(arg0, arg1);
	}

	/**
	 * Update n string.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateNString(java.lang.String, java.lang.String)
	 */
	@Override
	public void updateNString(final String arg0, final String arg1) throws SQLException {
		this.resultSet.updateNString(arg0, arg1);
	}

	/**
	 * Update null.
	 *
	 * @param arg0
	 *            the arg0
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateNull(int)
	 */
	@Override
	public void updateNull(final int arg0) throws SQLException {
		this.resultSet.updateNull(arg0);
	}

	/**
	 * Update null.
	 *
	 * @param arg0
	 *            the arg0
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateNull(java.lang.String)
	 */
	@Override
	public void updateNull(final String arg0) throws SQLException {
		this.resultSet.updateNull(arg0);
	}

	/**
	 * Update object.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateObject(int, java.lang.Object, int)
	 */
	@Override
	public void updateObject(final int arg0, final Object arg1, final int arg2) throws SQLException {
		this.resultSet.updateObject(arg0, arg1, arg2);
	}

	/**
	 * Update object.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateObject(int, java.lang.Object)
	 */
	@Override
	public void updateObject(final int arg0, final Object arg1) throws SQLException {
		this.resultSet.updateObject(arg0, arg1);
	}

	/**
	 * Update object.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateObject(java.lang.String, java.lang.Object,
	 *      int)
	 */
	@Override
	public void updateObject(final String arg0, final Object arg1, final int arg2) throws SQLException {
		this.resultSet.updateObject(arg0, arg1, arg2);
	}

	/**
	 * Update object.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateObject(java.lang.String, java.lang.Object)
	 */
	@Override
	public void updateObject(final String arg0, final Object arg1) throws SQLException {
		this.resultSet.updateObject(arg0, arg1);
	}

	/**
	 * Update ref.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateRef(int, java.sql.Ref)
	 */
	@Override
	public void updateRef(final int arg0, final Ref arg1) throws SQLException {
		this.resultSet.updateRef(arg0, arg1);
	}

	/**
	 * Update ref.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateRef(java.lang.String, java.sql.Ref)
	 */
	@Override
	public void updateRef(final String arg0, final Ref arg1) throws SQLException {
		this.resultSet.updateRef(arg0, arg1);
	}

	/**
	 * Update row.
	 *
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateRow()
	 */
	@Override
	public void updateRow() throws SQLException {
		this.resultSet.updateRow();
	}

	/**
	 * Update row id.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateRowId(int, java.sql.RowId)
	 */
	@Override
	public void updateRowId(final int arg0, final RowId arg1) throws SQLException {
		this.resultSet.updateRowId(arg0, arg1);
	}

	/**
	 * Update row id.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateRowId(java.lang.String, java.sql.RowId)
	 */
	@Override
	public void updateRowId(final String arg0, final RowId arg1) throws SQLException {
		this.resultSet.updateRowId(arg0, arg1);
	}

	/**
	 * Update sqlxml.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateSQLXML(int, java.sql.SQLXML)
	 */
	@Override
	public void updateSQLXML(final int arg0, final SQLXML arg1) throws SQLException {
		this.resultSet.updateSQLXML(arg0, arg1);
	}

	/**
	 * Update sqlxml.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateSQLXML(java.lang.String, java.sql.SQLXML)
	 */
	@Override
	public void updateSQLXML(final String arg0, final SQLXML arg1) throws SQLException {
		this.resultSet.updateSQLXML(arg0, arg1);
	}

	/**
	 * Update short.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateShort(int, short)
	 */
	@Override
	public void updateShort(final int arg0, final short arg1) throws SQLException {
		this.resultSet.updateShort(arg0, arg1);
	}

	/**
	 * Update short.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateShort(java.lang.String, short)
	 */
	@Override
	public void updateShort(final String arg0, final short arg1) throws SQLException {
		this.resultSet.updateShort(arg0, arg1);
	}

	/**
	 * Update string.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateString(int, java.lang.String)
	 */
	@Override
	public void updateString(final int arg0, final String arg1) throws SQLException {
		this.resultSet.updateString(arg0, arg1);
	}

	/**
	 * Update string.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateString(java.lang.String, java.lang.String)
	 */
	@Override
	public void updateString(final String arg0, final String arg1) throws SQLException {
		this.resultSet.updateString(arg0, arg1);
	}

	/**
	 * Update time.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateTime(int, java.sql.Time)
	 */
	@Override
	public void updateTime(final int arg0, final Time arg1) throws SQLException {
		this.resultSet.updateTime(arg0, arg1);
	}

	/**
	 * Update time.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateTime(java.lang.String, java.sql.Time)
	 */
	@Override
	public void updateTime(final String arg0, final Time arg1) throws SQLException {
		this.resultSet.updateTime(arg0, arg1);
	}

	/**
	 * Update timestamp.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateTimestamp(int, java.sql.Timestamp)
	 */
	@Override
	public void updateTimestamp(final int arg0, final Timestamp arg1) throws SQLException {
		this.resultSet.updateTimestamp(arg0, arg1);
	}

	/**
	 * Update timestamp.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#updateTimestamp(java.lang.String,
	 *      java.sql.Timestamp)
	 */
	@Override
	public void updateTimestamp(final String arg0, final Timestamp arg1) throws SQLException {
		this.resultSet.updateTimestamp(arg0, arg1);
	}

	/**
	 * Was null.
	 *
	 * @return true, if successful
	 * @throws SQLException
	 *             the SQL exception
	 * @see java.sql.ResultSet#wasNull()
	 */
	@Override
	public boolean wasNull() throws SQLException {
		return this.resultSet.wasNull();
	}

	/**
	 * A toString implementation which supports debuggings and logging.
	 * <p>
	 * NOTE: There are some implementations of the {@link ResultSet} which have
	 * a faulty toString implementation (e.g. the JTurbo driver of 2005). In
	 * this case we catch the {@link RuntimeException} and return a "strange
	 * ResultSet..." as result.
	 * </p>
	 * <p>
	 * The same driver causes also an java.lang.AbstractMethodError for a
	 * {@link ResultSet#isClosed()} call:
	 * </p>
	 *
	 * <pre>
	 * java.lang.AbstractMethodError: com.newatlanta.jturbo.driver.n.isClosed()Z
	 *        at patterntesting.runtime.monitor.db.internal.StasiResultSet.toString(StasiResultSet.java:2343)
	 *        at patterntesting.runtime.util.Converter.toString(Converter.java:310)
	 *        at patterntesting.runtime.util.Converter.toShortString(Converter.java:379)
	 *        at patterntesting.runtime.monitor.db.SqlStatistic.stop(SqlStatistic.java:110)
	 *        at patterntesting.runtime.monitor.db.internal.StasiPreparedStatement.logSQL(StasiPreparedStatement.java:862)
	 *        at patterntesting.runtime.monitor.db.internal.StasiPreparedStatement.executeQuery(StasiPreparedStatement.java:138)
	 *        ...
	 * </pre>
	 * <p>
	 * To avoid problems during logging {@link LinkageError}s are also catched
	 * since v1.5.1 and a "very strange ResultSet..." is given back as result.
	 * </p>
	 *
	 * @return a useful string representation
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		try {
			if (!ObjectTester.hasToStringDefaultImpl(this.resultSet)) {
				return this.resultSet.toString();
			}
			StringBuilder buf = new StringBuilder(this.resultSet.isClosed() ? "" : "open ");
			buf.append(this.resultSet.getClass().getSimpleName());
			return buf.toString();
		} catch (SQLException ex) {
			LOG.trace("Cannot get status of {}:", this.resultSet, ex);
			return this.resultSet.toString();
		} catch (RuntimeException ex) {
			LOG.trace(this.resultSet.getClass().getName() + ".toString() failed.", ex);
			return "strange ResultSet (" + ex + ")";
		} catch (LinkageError ex) {
			LOG.trace("Cannot convert {} in a string representation:", this.resultSet.getClass(), ex);
			return "very strange ResultSet (" + ex + ")";
		}
	}

	/**
	 * Gets the object.
	 *
	 * The implementation is not supported because it is only needed for Java 7.
	 * But we want to support also Java 6 where this method is not availabe in
	 * the wrapped {@link ResultSet}.
	 *
	 * @param <T>
	 *            the generic type
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @return the object
	 * @throws SQLException
	 *             the SQL exception
	 * @since Java 7
	 */
	@Override
	public <T> T getObject(final int arg0, final Class<T> arg1) throws SQLException {
		throw new UnsupportedOperationException("not yet implemented");
	}

	/**
	 * Gets the object.
	 *
	 * The implementation is not supported because it is only needed for Java 7.
	 * But we want to support also Java 6 where this method is not availabe in
	 * the wrapped {@link ResultSet}.
	 *
	 * @param <T>
	 *            the generic type
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @return the object
	 * @throws SQLException
	 *             the SQL exception
	 * @since Java 7
	 */
	@Override
	public <T> T getObject(final String arg0, final Class<T> arg1) throws SQLException {
		throw new UnsupportedOperationException("not yet implemented");
	}

}
