/*
 * $Id: UmlGraphWriter.java,v 1.19 2016/12/27 07:40:45 oboehm Exp $
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
 * (c)reated 04.06.2015 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.log.internal;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.*;
import org.aspectj.lang.JoinPoint.StaticPart;

import patterntesting.runtime.exception.NotFoundException;
import patterntesting.runtime.io.ExtendedFile;
import patterntesting.runtime.util.Converter;

/**
 * This class is responsible for writing sequence diagrams in the UML Graph
 * format. This format is described on
 * <a href="http://umlgraph.org">umlgraph.org</a>.
 *
 * @author oliver
 * @version $Revision: 1.19 $
 * @since 1.6 (04.06.2015)
 */
public final class UmlGraphWriter extends SequenceDiagramWriter {

	private static final Logger LOG = LoggerFactory.getLogger(UmlGraphWriter.class);

	/**
	 * Instantiates a new uml graph writer.
	 *
	 * @param file
	 *            the file
	 */
	public UmlGraphWriter(final File file) {
		this(ExtendedFile.createOutputStreamFor(file));
	}

	/**
	 * Instantiates a new uml graph writer.
	 *
	 * @param ostream
	 *            the ostream
	 */
	public UmlGraphWriter(final OutputStream ostream) {
		this(new BufferedWriter(new OutputStreamWriter(ostream, Charset.forName("ISO-8859-1"))));
	}

	/**
	 * Instantiates a new uml graph writer.
	 *
	 * @param writer
	 *            the writer
	 */
	public UmlGraphWriter(final Writer writer) {
		this(writer, new ArrayList<DrawStatement>());
	}

	/**
	 * Instantiates a new uml graph writer.
	 *
	 * @param writer
	 *            the writer
	 * @param statements
	 *            the statements
	 */
	public UmlGraphWriter(final Writer writer, final List<DrawStatement> statements) {
		super(writer, statements);
		this.writeTemplate("seq-head.template");
	}

	/**
	 * Write sequence diagram.
	 */
	@Override
	public void writeSequenceDiagram() {
		this.writeStatements();
		this.completeObjects();
		this.writeTemplate("seq-tail.template");
	}

	private void writeStatements() {
		if (this.getStatements().isEmpty()) {
			LOG.debug("No draw statemtents are logged.");
			return;
		}
		this.writeObjects();
		this.writeLine("");
		this.writeLine("# Message sequences");
		this.writeMessages();
	}

	private void writeObjects() {
		List<DrawStatement> objects = this.getObjects();
		for (DrawStatement stmt : objects) {
			writeStatement(stmt);
		}
		this.writeLine("step();");
		this.writeLine("step();");
		this.writeLine("active(" + this.getFirstActor().getSender() + ");");
	}

	private void writeMessages() {
		List<DrawStatement> messages = this.getMessages();
		DrawStatement previous = DrawStatement.NULL;
		for (DrawStatement stmt : messages) {
			if (previous.hasMessageToLeft() && stmt.hasMessageToRight()) {
				this.writeLine("step();");
			}
			if (stmt.hasMessage()) {
				previous = stmt;
			}
			writeStatement(stmt);
		}
	}

	private DrawStatement getFirstActor() {
		for (DrawStatement stmt : this.getStatements()) {
			if (stmt.getType() == DrawType.ACTOR) {
				return stmt;
			}
		}
		throw new NotFoundException("no ACTORs in " + this.getStatements());
	}

	private List<DrawStatement> getObjects() {
		List<DrawStatement> objects = new ArrayList<>();
		for (DrawStatement stmt : this.getStatements()) {
			if (stmt.hasMessage()) {
				break;
			}
			objects.add(stmt);
		}
		return objects;
	}

	private List<DrawStatement> getMessages() {
		List<DrawStatement> messages = new ArrayList<>();
		for (DrawStatement stmt : this.getStatements()) {
			if (stmt.hasMessage()) {
				messages.add(stmt);
			}
		}
		return messages;
	}

	private void completeObjects() {
		this.writeLine("");
		this.writeLine("# Complete the lifelines");
		this.writeLine("step();");
		Set<String> names = this.getVarnames();
		for (String name : names) {
			this.writeLine("complete(" + name + ");");
		}
	}

	private Set<String> getVarnames() {
		Set<String> varnames = new TreeSet<>();
		for (DrawStatement stmt : this.getStatements()) {
			switch (stmt.getType()) {
			case CREATE_MESSAGE:
			case MESSAGE:
				varnames.add(stmt.getSender());
				varnames.add(stmt.getTarget());
				break;
			default:
				LOG.trace("{} is not used to get variable names.", stmt);
				break;
			}
		}
		return varnames;
	}

	private void writeTemplate(final String resource) {
		InputStream istream = this.getClass().getResourceAsStream(resource);
		if (istream == null) {
			LOG.warn("Resource \"{}\" not found - content will be missing in generated diagram.", resource);
		} else {
			try {
				String head = IOUtils.toString(istream, "ISO-8859-1");
				this.getWriter().write(head);
			} catch (IOException ioe) {
				LOG.warn("Content of \"" + resource + "\" will be missing in generated diagram.", ioe);
			} finally {
				IOUtils.closeQuietly(istream);
			}
		}
	}

	private void writeStatement(final DrawStatement stmt) {
		switch (stmt.getType()) {
		case ACTOR:
			writeObject(stmt, "actor");
			break;
		case OBJECT:
			writeObject(stmt, "object");
			break;
		case PLACEHOLDER_OBJECT:
			writeLine("placeholder_object(" + stmt.getSender() + ");");
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
			writeLine("# " + stmt);
			break;
		}
	}

	private void writeObject(final DrawStatement stmt, final String type) {
		writeLine(getBoxwidStatementFor(stmt.getTarget()));
		writeLine(type + "(" + stmt.getSender() + ",\"" + stmt.getTarget() + "\");");
	}

	private static String getBoxwidStatementFor(final String name) {
		return "boxwid = " + getBoxwidFor(name) + ";";
	}

	private static String getBoxwidFor(final String name) {
		int length = name.length() + 1;
		if (length > 16) {
			return "1.5";
		} else if (length > 10) {
			return "1.0";
		} else {
			return "0.75";
		}
	}

	@Override
	protected void writeCreateMessage(final DrawStatement stmt) {
		String classname = (String) stmt.getArgs()[0];
		writeLine(getBoxwidStatementFor(classname));
		writeLine("\n# --> " + stmt.getJpInfo());
		writeLine("create_message(" + stmt.getSender() + "," + stmt.getTarget() + ",\"" + classname + "\");");
	}

	@Override
	protected void writeMessage(final DrawStatement stmt) {
		if (stmt.hasMessageToLeft()) {
			writeLine("step();");
		}
		StaticPart jpInfo = stmt.getJpInfo();
		writeLine("\n# --> " + jpInfo);
		writeLine("message(" + stmt.getSender() + "," + stmt.getTarget() + ",\"" + jpInfo.getSignature().getName()
				+ StringEscapeUtils.escapeJava(stmt.getArgsAsString()) + "\");");
		writeLine("active(" + stmt.getTarget() + ");");
	}

	@Override
	protected void writeReturnMessage(final DrawStatement stmt) {
		if (stmt.hasMessageToLeft()) {
			writeLine("step();");
		}
		writeLine("\n# <-- " + stmt.getJpInfo());
		writeLine("return_message(" + stmt.getTarget() + "," + stmt.getSender() + ",\""
				+ toEscapedString(Converter.toShortString(stmt.getArgs()[0])) + "\");");
		writeLine("inactive(" + stmt.getTarget() + ");");
	}

	private static String toEscapedString(final Object returnValue) {
		if (returnValue == null) {
			return "null";
		}
		return StringEscapeUtils.escapeJava(returnValue.toString());
	}

}
