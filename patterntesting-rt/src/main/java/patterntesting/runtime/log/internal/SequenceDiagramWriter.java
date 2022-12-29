/*
 * $Id: SequenceDiagramWriter.java,v 1.14 2016/12/27 07:40:45 oboehm Exp $
 *
 * Copyright (c) 2015 by Oliver Boehm
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
 * (c)reated 14.06.2015 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.log.internal;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

import org.apache.commons.io.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.*;
import org.aspectj.lang.JoinPoint.StaticPart;

import patterntesting.runtime.io.ExtendedFile;
import patterntesting.runtime.util.Converter;

/**
 * The Class SequenceDiagramWriter uses the format of websequence diagram to
 * generate the chart. You can find this format at <a href=
 * "https://www.websequencediagrams.com/">www.websequencediagrams.com</a>.
 *
 * @author oliver
 * @version $Revision: 1.14 $
 * @since 1.6 (14.06.2015)
 */
public class SequenceDiagramWriter {

	private static final Logger LOG = LoggerFactory.getLogger(SequenceDiagramWriter.class);

	private final Writer writer;
	private final List<DrawStatement> statements = new ArrayList<>();
	private final Map<String, String> placeholders = new HashMap<>();

	/**
	 * Instantiates a new sequence diagram writer.
	 *
	 * @param file
	 *            the file
	 */
	public SequenceDiagramWriter(final File file) {
		this(ExtendedFile.createOutputStreamFor(file));
		writeHeaderFor(file);
	}

	/**
	 * Instantiates a new sequence diagram writer.
	 *
	 * @param ostream
	 *            the ostream
	 */
	public SequenceDiagramWriter(final OutputStream ostream) {
		this(new BufferedWriter(new OutputStreamWriter(ostream, Charset.forName("UTF-8"))));
	}

	/**
	 * Instantiates a new sequence diagram writer.
	 *
	 * @param writer
	 *            the writer
	 */
	public SequenceDiagramWriter(final Writer writer) {
		this.writer = writer;
	}

	/**
	 * Instantiates a new sequence diagram writer.
	 *
	 * @param writer
	 *            the writer
	 * @param statements
	 *            the statements
	 */
	public SequenceDiagramWriter(final Writer writer, final List<DrawStatement> statements) {
		this(writer);
		this.statements.addAll(statements);
	}

	/**
	 * Gets the writer.
	 *
	 * @return the writer
	 */
	protected Writer getWriter() {
		return writer;
	}

	/**
	 * Gets the statements.
	 *
	 * @return the statements
	 */
	protected List<DrawStatement> getStatements() {
		return Collections.unmodifiableList(statements);
	}

	/**
	 * Adds the statements.
	 *
	 * @param drawStatements
	 *            the statements
	 */
	public void addStatements(final Collection<DrawStatement> drawStatements) {
		this.statements.addAll(drawStatements);
	}

	/**
	 * Adds a statement.
	 *
	 * @param stmt
	 *            the statement.
	 */
	public void addStatement(final DrawStatement stmt) {
		this.statements.add(stmt);
	}

	/**
	 * Write sequence diagram.
	 */
	public void writeSequenceDiagram() {
		for (DrawStatement stmt : this.statements) {
			switch (stmt.getType()) {
			case ACTOR:
				placeholders.put(stmt.getSender(), stmt.getTarget());
				break;
			case OBJECT:
				addToPlaceholders(stmt.getSender(), stmt.getTarget());
				break;
			case PLACEHOLDER_OBJECT:
				LOG.debug("Statement '{}' is ignored for generated diagram.", stmt);
				break;
			case CREATE_MESSAGE:
				writeCreateMessage(stmt);
				break;
			case MESSAGE:
				writeMessage(stmt);
				break;
			case RETURN_MESSAGE:
				writeReturnMessage(stmt);
				break;
			default:
				writeLine(stmt.toString());
				break;
			}
		}
	}

	private void addToPlaceholders(final String varname, final String label) {
		if (this.getNumberOfObjectLabels(label) == 1) {
			this.placeholders.put(varname, toName(label, varname));
		} else {
			this.placeholders.put(varname, toName(label, ""));
		}
	}

	private int getNumberOfObjectLabels(final String label) {
		int n = 0;
		for (DrawStatement stmt : this.statements) {
			if ((stmt.getType() == DrawType.OBJECT) && (label.equals(stmt.getSender()))) {
				n++;
			}
		}
		return n;
	}

	/**
	 * Write create message.
	 *
	 * @param stmt
	 *            the stmt
	 */
	protected void writeCreateMessage(final DrawStatement stmt) {
		String name = toName((String) stmt.getArgs()[0], stmt.getTarget());
		placeholders.put(stmt.getTarget(), name);
		writeLine("# " + this.placeholders.get(stmt.getSender()) + " ->  +" + name + ": <<create>>");
	}

	/**
	 * Write message.
	 *
	 * @param stmt
	 *            the stmt
	 */
	protected void writeMessage(final DrawStatement stmt) {
		StaticPart jpInfo = stmt.getJpInfo();
		String target = stmt.getTarget();
		writeLine(this.placeholders.get(stmt.getSender()) + " ->  +" + this.placeholders.get(target) + ": "
				+ jpInfo.getSignature().getName() + stmt.getArgsAsString());
	}

	/**
	 * Write return message.
	 *
	 * @param stmt
	 *            the stmt
	 */
	protected void writeReturnMessage(final DrawStatement stmt) {
		writeLine(this.placeholders.get(stmt.getTarget()) + " --> -" + this.placeholders.get(stmt.getSender()) + ": "
				+ Converter.toShortString(stmt.getArgs()[0]));
	}

	private static String toName(final String label, final String varname) {
		String[] parts = label.split(":");
		String name = parts[0];
		if (StringUtils.isEmpty(name)) {
			name = varname;
		}
		name = parts[1] + " " + name;
		return name.trim();
	}

	/**
	 * This method can be used to write the title of the diagram. The title will
	 * be derived from the file name.
	 *
	 * @param file
	 *            the file
	 */
	public final void writeHeaderFor(final File file) {
		this.writeLine("title " + FilenameUtils.getBaseName(file.getName()));
		this.writeLine("");
	}

	/**
	 * Write line.
	 *
	 * @param line
	 *            the line
	 */
	protected final void writeLine(final String line) {
		try {
			this.getWriter().write(line.trim());
			this.getWriter().write("\n");
		} catch (IOException ioe) {
			LOG.debug("Writing to {} failed:", this.getWriter(), ioe);
			LOG.info(line);
		}
	}

	/**
	 * Close quietly.
	 */
	public void close() {
		IOUtils.closeQuietly(this.writer);
	}

}
