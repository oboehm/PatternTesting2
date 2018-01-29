/*
 * $Id: SequenceGrapher.java,v 1.71 2017/06/01 17:24:29 oboehm Exp $
 *
 * Copyright (c) 2013 by Oliver Boehm
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
 * (c)reated 06.09.2013 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.log;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.JoinPoint.StaticPart;
import org.aspectj.lang.reflect.ConstructorSignature;

import patterntesting.annotation.check.runtime.NullArgsAllowed;
import patterntesting.runtime.exception.NotFoundException;
import patterntesting.runtime.io.ExtendedFile;
import patterntesting.runtime.log.internal.*;
import patterntesting.runtime.util.*;
import patterntesting.runtime.util.regex.TypePattern;

/**
 * This class supports the creation of simple sequence diagrams as described in
 * the user manual of <a href="http://umlgraph.org/">UML Graph</a>. This format
 * will be used if the result will be stored in a file with the extension
 * ".pic".
 * <p>
 * Since 1.6 the sequence diagram can be also generated in a simple text format
 * which can be used as input for
 * <a href="https://www.websequencediagrams.com/">websequence diagrams</a>. This
 * format will be used as default.
 * </p>
 *
 * @author oliver
 * @since 1.3.1 (06.09.2013)
 */
public class SequenceGrapher extends AbstractLogger {

	private static final Logger LOG = LogManager.getLogger(SequenceGrapher.class);
	private final SequenceDiagramWriter diagramWriter;
	private final List<DrawStatement> statements = new ArrayList<>();
	private final Map<Object, String> objnames = new HashMap<>();
	private final Map<Object, DrawStatement> placeHolders = new HashMap<>();
	private final Map<Object, String> varnames = new HashMap<>();
	private final Deque<String> callerNames = new ArrayDeque<>();
	private TypePattern[] excludeFilter = new TypePattern[0];
	private int objectNumber = 0;

	/**
	 * Instantiates a new SequenceGrapher. The generated sequence diagram is
	 * stored in a temporary file.
	 */
	public SequenceGrapher() {
		this(createTempLogFile("seq-diagram", ".pic"));
	}

	/**
	 * Instantiates a new sequence grapher.
	 *
	 * @param logFile
	 *            the log file
	 */
	public SequenceGrapher(final File logFile) {
		this(ExtendedFile.createOutputStreamFor(logFile), logFile);
	}

	private SequenceGrapher(final OutputStream ostream, final File logFile) {
		super(ostream);
		LOG.info("Sequence diagram will be written to \"{}\".", logFile);
		String extension = FilenameUtils.getExtension(logFile.getName());
		if ("pic".equalsIgnoreCase(extension)) {
			diagramWriter = new UmlGraphWriter(ostream);
		} else {
			diagramWriter = new SequenceDiagramWriter(ostream);
			diagramWriter.writeHeaderFor(logFile);
		}
	}

	/**
	 * Sets the exclude filter. Classes which matches the filter will not appear
	 * in the generated sequence diagram.
	 *
	 * @param pattern
	 *            the new exclude filter
	 * @since 1.4.1
	 */
	public void setExcludeFilter(final String[] pattern) {
		this.excludeFilter = new TypePattern[pattern.length];
		for (int i = 0; i < pattern.length; i++) {
			this.excludeFilter[i] = new TypePattern(pattern[i]);
		}
	}

	/**
	 * Closes the stream with the logged objects.
	 */
	@Override
	public void close() {
		this.closeQuietly();
		super.close();
	}

	private void closeQuietly() {
		this.sortOutEmptyCreateMessages();
		writeSequenceDiagram();
		diagramWriter.close();
	}

	private void writeSequenceDiagram() {
		if (!objnames.isEmpty()) {
			this.addObjects();
		}
		diagramWriter.addStatements(statements);
		diagramWriter.writeSequenceDiagram();
	}

	/**
	 * Here we prepare the cached statements to the file. If a create message is
	 * found with no other activities this creation will be sorted out to keep
	 * the generated sequence diagram simple.
	 */
	private void sortOutEmptyCreateMessages() {
		List<DrawStatement> emptyCreateMessages = new ArrayList<>();
		for (DrawStatement stmt : this.statements) {
			if ((stmt.getType() == DrawType.CREATE_MESSAGE) && !hasActivities(stmt)) {
				LOG.debug("{} will be ignored because it is a single statement.", stmt);
				emptyCreateMessages.add(stmt);
				Object toBeRemoved = this.getPlaceHolderKey(stmt.getTarget());
				this.placeHolders.remove(toBeRemoved);
				removeValue(this.varnames, stmt.getTarget());
			}
		}
		this.statements.removeAll(emptyCreateMessages);
	}

	private Object getPlaceHolderKey(final String name) {
		for (Entry<Object, DrawStatement> entry : this.placeHolders.entrySet()) {
			DrawStatement stmt = entry.getValue();
			if (name.equals(stmt.getSender())) {
				return entry.getKey();
			}
		}
		return "?";
	}

	private static void removeValue(final Map<Object, String> map, final String name) {
		for (Map.Entry<Object, String> entry : map.entrySet()) {
			if (name.equals(entry.getValue())) {
				map.remove(entry.getKey());
				break;
			}
		}
	}

	private void addObjects() {
		List<DrawStatement> objects = new ArrayList<>();
		SortedMap<String, Object> sortedObjectNames = getSortedObjectNames();
		String firstName = sortedObjectNames.firstKey();
		objects.add(this.getActorStatement(firstName, sortedObjectNames.get(firstName)));
		sortedObjectNames.remove(firstName);
		List<String> actorNames = this.getActorNames();
		for (Entry<String, Object> entry : sortedObjectNames.entrySet()) {
			objects.add(this.getObjectStatement(entry));
		}
		Collection<DrawStatement> placeHolderStatements = getSortedPlaceHolders();
		for (DrawStatement stmt : placeHolderStatements) {
			objects.add(stmt);
		}
		for (int i = 0; i < actorNames.size(); i++) {
			objects.add(this.getActorStatement(actorNames.get(i)));
		}
		statements.addAll(0, objects);
	}

	private SortedMap<String, Object> getSortedObjectNames() {
		SortedMap<String, Object> sortedObjectNames = new TreeMap<>(new VarnameComparator());
		for (Entry<Object, String> entry : objnames.entrySet()) {
			Object name = entry.getKey();
			if (!isActor(name)) {
				sortedObjectNames.put(entry.getValue(), name);
			}
		}
		return sortedObjectNames;
	}

	private Collection<DrawStatement> getSortedPlaceHolders() {
		Collection<DrawStatement> stmts = new TreeSet<>(new PlaceholderComparator());
		stmts.addAll(this.placeHolders.values());
		return stmts;
	}

	/**
	 * Static classes with only static elements are considered as actor to
	 * distinguish them from "normal" objects.
	 *
	 * @return the actors
	 */
	private List<String> getActorNames() {
		List<String> actors = new ArrayList<>();
		for (Entry<Object, String> entry : objnames.entrySet()) {
			Object name = entry.getKey();
			if (isActor(name)) {
				actors.add(entry.getValue());
			}
		}
		return actors;
	}

	/**
	 * Classes with only static methods are considered as actor to distinguish
	 * them from "normal" classed.
	 *
	 * @param name
	 *            the name
	 * @return true, if is actor
	 */
	private static boolean isActor(final Object name) {
		if (!(name instanceof Class<?>)) {
			return false;
		}
		Class<?> clazz = (Class<?>) name;
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (!Modifier.isStatic(method.getModifiers()) && !method.getDeclaringClass().equals(Object.class)) {
				return false;
			}
		}
		Field[] fields = clazz.getFields();
		for (Field field : fields) {
			if (!Modifier.isStatic(field.getModifiers())) {
				return false;
			}
		}
		return true;
	}

	private DrawStatement getActorStatement(final String name) {
		Entry<Object, String> entry = getEntryOfValue(name, objnames);
		return getActorStatement(name, entry.getKey());
	}

	private DrawStatement getActorStatement(final String name, final Object value) {
		Class<?> clazz = value.getClass();
		if (value instanceof Class<?>) {
			clazz = (Class<?>) value;
		}
		String targetName = clazz.getSimpleName();
		return new DrawStatement(DrawType.ACTOR, name, targetName);
	}

	private DrawStatement getObjectStatement(final Entry<String, Object> entry) {
		String targetName = DrawStatement.createTargetName(entry.getValue());
		return new DrawStatement(DrawType.OBJECT, entry.getKey(), targetName);
	}

	private static Entry<Object, String> getEntryOfValue(final String value, final Map<Object, String> map) {
		for (Entry<Object, String> entry : map.entrySet()) {
			if (value.equals(entry.getValue())) {
				return entry;
			}
		}
		throw new NotFoundException("value \"" + value + "\" not found in " + map);
	}

	/**
	 * If the target of the given statement is part of any other statement
	 * 'true' will be returned.
	 *
	 * @param drawStatement
	 *            the draw statement
	 * @return true, if successful
	 */
	private boolean hasActivities(final DrawStatement drawStatement) {
		String target = drawStatement.getTarget();
		for (DrawStatement stmt : this.statements) {
			if (drawStatement.equals(stmt)) {
				continue;
			}
			if (stmt.hasActor(target)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Logs the creation of an object in the created sequence diagram.
	 *
	 * @param call
	 *            the call
	 * @param result
	 *            the created object
	 */
	public void createMessage(final JoinPoint call, final Object result) {
		Object creator = call.getThis();
		if (creator == null) {
			String classname = JoinPointHelper.getCallerOf(call).getClassName();
			try {
				creator = Class.forName(classname);
			} catch (ClassNotFoundException ex) {
				throw new NotFoundException(classname, ex);
			}
		}
		this.createMessage(creator, result, call.getStaticPart());
	}

	/**
	 * Logs the creation of an object in the created sequence diagram.
	 *
	 * @param creator
	 *            the creator
	 * @param createdObject
	 *            the created object
	 * @param jpInfo
	 *            the jp info
	 */
	public void createMessage(final Object creator, final Object createdObject, final StaticPart jpInfo) {
		if (this.matches(creator) || this.matches(createdObject)) {
			LOG.debug("{} --creates--> {} is not logged because of exclude filter.", creator, createdObject);
			return;
		}
		String name = this.varnames.get(createdObject);
		String typeName = this.addPlaceHolder(createdObject, jpInfo);
		if (name != null) {
			LOG.trace("Creation of {} is already logged.", createdObject);
			this.objnames.remove(createdObject);
			return;
		}
		name = this.getVarnameFor(creator);
		this.addCreateMessage(name, createdObject, typeName, jpInfo);
	}

	private boolean matches(final Object creator) {
		for (int i = 0; i < this.excludeFilter.length; i++) {
			if (this.excludeFilter[i].matches(creator)) {
				return true;
			}
		}
		return false;
	}

	private String addPlaceHolder(final Object obj, final StaticPart jpInfo) {
		DrawStatement stmt = this.placeHolders.get(obj);
		if (stmt == null) {
			String name = this.objnames.get(obj);
			if (name == null) {
				name = this.addVarnameFor(obj);
			}
			stmt = new DrawStatement(DrawType.PLACEHOLDER_OBJECT, name, jpInfo);
			this.placeHolders.put(obj, stmt);
		}
		return stmt.getSender();
	}

	private String addObject(final Object obj) {
		String name = this.addVarnameFor(obj);
		objnames.put(obj, name);
		return name;
	}

	@NullArgsAllowed
	private String getVarnameFor(final Object target, final StaticPart jpInfo) {
		if (target == null) {
			Class<?> targetClass = jpInfo.getSignature().getDeclaringType();
			return this.getVarnameFor(targetClass);
		} else {
			return this.getVarnameFor(target);
		}
	}

	@NullArgsAllowed
	private String getVarnameFor(final Object obj) {
		if (obj == null) {
			return getActorName();
		}
		String name = this.varnames.get(obj);
		if (name == null) {
			if (obj instanceof Class<?>) {
				name = this.getVarnameFor((Class<?>) obj);
			} else {
				name = this.varnames.get(obj.getClass());
			}
		}
		if (name == null) {
			name = addObject(obj);
		}
		return name;
	}

	private String getVarnameFor(final Class<?> clazz) {
		String name = this.varnames.get(clazz);
		if (name == null) {
			for (Entry<Object, String> entry : this.varnames.entrySet()) {
				if (clazz.equals(entry.getKey().getClass())) {
					return entry.getValue();
				}
			}
			name = addObject(clazz);
		}
		return name;
	}

	private String getActorName() {
		String name = this.varnames.get("Actor");
		if (name == null) {
			name = addVarnameFor("Actor");
		}
		return name;
	}

	private String addVarnameFor(final Object obj) {
		if (obj instanceof Class<?>) {
			return addVarnameFor((Class<?>) obj);
		}
		String name = toName(obj.getClass());
		return addVarname(name, obj);
	}

	private String addVarnameFor(final Class<?> clazz) {
		String name = toName(clazz);
		return addVarname(name, clazz);
	}

	private String addVarname(final String name, final Object obj) {
		if (this.varnames.containsKey(obj)) {
			LOG.trace("{} already in map of var names.", obj);
		} else {
			this.varnames.put(obj, name);
			this.objectNumber++;
		}
		return this.varnames.get(obj);
	}

	private String toName(final Class<?> clazz) {
		return clazz.getSimpleName().substring(0, 1).toUpperCase()
				+ Integer.toString(this.objectNumber, Character.MAX_RADIX);
	}

	/**
	 * Trys to log the call of the given excecution joinpoint. For this reason
	 * we must find the caller which is a little bit tricky. We use the
	 * classname of the mapped variable names to guess which could be the
	 * caller.
	 *
	 * @param execution
	 *            the execution joinpoint
	 */
	public void execute(final JoinPoint execution) {
		DrawStatement stmt = this.getLastMessage();
		if (stmt.isFromCallJoinpoint() && stmt.hasSameSignatureAs(execution.getStaticPart())) {
			LOG.debug("Joinpoint '{}' is logged already as call.", execution);
			return;
		}
		String senderName = getCallerNameOf(execution);
		String targetName = getTargetName(execution);
		StaticPart jpInfo = execution.getStaticPart();
		if (execution.getSignature() instanceof ConstructorSignature) {
			this.addCreateMessage(senderName, execution.getThis(), targetName, jpInfo);
		} else {
			this.message(senderName, targetName, jpInfo, execution.getArgs());
		}
	}

	/**
	 * This is the closing method of {@link #execute(JoinPoint)}. After an
	 * execution joinpoint has ended we must complete the diagram with the
	 * return message.
	 *
	 * @param execution
	 *            the execution joinpoint
	 * @since 1.6 (03.06.2015)
	 */
	public void returnFromExecute(final JoinPoint execution) {
		this.returnFromExecute(execution, "");
	}

	/**
	 * This is the closing method of {@link #execute(JoinPoint)}. After an
	 * execution joinpoint has ended we must complete the diagram with the
	 * return message.
	 *
	 * @param execution
	 *            the execution joinpoint
	 * @param returnValue
	 *            the return value
	 * @since 1.6 (03.06.2015)
	 */
	public void returnFromExecute(final JoinPoint execution, final Object returnValue) {
		String senderName = getCallerNameOf(execution);
		String targetName = getTargetName(execution);
		String caller = this.callerNames.pop();
		LOG.trace("Caller '{}' was taken from stack.", caller);
		assert caller.equals(senderName) : "'" + senderName + "' was not on top of stack " + this.callerNames;
		this.addReturnMessage(senderName, targetName, returnValue, execution.getStaticPart());
	}

	private String getTargetName(final JoinPoint execution) {
		Object thisObject = execution.getThis();
		if (thisObject == null) {
			StackTraceElement element = StackTraceScanner.find(execution.getSignature());
			try {
				return this.getVarnameFor(Class.forName(element.getClassName()));
			} catch (ClassNotFoundException ex) {
				LOG.debug("Classname of " + element + " not found.", ex);
				return "unknown";
			}
		} else {
			return this.getVarnameFor(thisObject);
		}
	}

	private String getCallerNameOf(final JoinPoint execution) {
		StackTraceElement caller = JoinPointHelper.getCallerOf(execution);
		String classname = caller.getClassName();
		for (Entry<Object, String> entry : varnames.entrySet()) {
			if (classname.equals(entry.getKey().getClass().getName())) {
				LOG.trace("Caller of {} is {}.", execution, entry);
				return entry.getValue();
			}
		}
		LOG.trace("Caller of {} not found in {}.", execution, varnames);
		try {
			return this.addObject(Class.forName(caller.getClassName()));
		} catch (ClassNotFoundException ex) {
			LOG.info("cannot get class for {}:", caller, ex);
			return getActorName();
		}
	}

	/**
	 * Logs the call of a method to the generated sequence diagram.
	 *
	 * @param call
	 *            the call
	 */
	public void message(final JoinPoint call) {
		this.message(call.getThis(), call);
	}

	/**
	 * Logs the call of a method to the generated sequence diagram.
	 *
	 * @param caller
	 *            the caller
	 * @param call
	 *            the call
	 */
	public void message(final Object caller, final JoinPoint call) {
		this.message(caller, call.getTarget(), call.getStaticPart(), call.getArgs());
	}

	/**
	 * Logs the call of a method to the generated sequence diagram.
	 *
	 * @param sender
	 *            the sender
	 * @param target
	 *            the target
	 * @param jpInfo
	 *            the static joinpoint info with the method name
	 * @param args
	 *            the args
	 */
	@NullArgsAllowed
	public void message(final Object sender, final Object target, final StaticPart jpInfo, final Object[] args) {
		if (this.matches(sender) || this.matches(target)) {
			LOG.debug("{} -----------> {} is not logged because of exclude filter.", sender, target);
			return;
		}
		String senderName = this.getVarnameFor(sender);
		String targetName = this.getVarnameFor(target, jpInfo);
		this.message(senderName, targetName, jpInfo, args);
	}

	private void message(final String senderName, final String targetName, final StaticPart jpInfo,
			final Object[] args) {
		this.addMessage(senderName, targetName, jpInfo, args);
	}

	/**
	 * Logs the return arrow from the last call to the generated sequence
	 * diagram.
	 *
	 * @param call
	 *            the call
	 */
	public void returnMessage(final JoinPoint call) {
		this.returnMessage(call, "");
	}

	/**
	 * Logs the return arrow from the last call to the generated sequence
	 * diagram. Because it could be that the return message is already logged by
	 * an execution joinpoint we check that first.
	 *
	 * @param call
	 *            the call
	 * @param returnValue
	 *            the return value
	 */
	public void returnMessage(final JoinPoint call, final Object returnValue) {
		DrawStatement stmt = this.getLastMessage();
		if (stmt.isFromExecutionJoinpoint() && stmt.hasSameSignatureAs(call.getStaticPart())) {
			LOG.debug("Joinpoint '{}' is logged already as call.", call);
			return;
		}
		this.returnMessage(call.getTarget(), returnValue, call.getStaticPart());
	}

	/**
	 * Return message.
	 *
	 * @param returnee
	 *            the returnee
	 * @param returnValue
	 *            the return value
	 * @param jpInfo
	 *            the jp info
	 */
	public void returnMessage(final Object returnee, final Object returnValue, final StaticPart jpInfo) {
		if (this.matches(returnee)) {
			LOG.debug("{} <--{}-- is not logged because of exclude filter.", returnee, returnValue);
			return;
		}
		this.addReturnMessage(returnee, returnValue, jpInfo);
	}

	private DrawStatement getLastMessage() {
		for (int i = this.statements.size() - 1; i >= 0; i--) {
			DrawStatement stmt = this.statements.get(i);
			switch (stmt.getType()) {
			case MESSAGE:
			case CREATE_MESSAGE:
			case RETURN_MESSAGE:
				return stmt;
			default:
				break;
			}
		}
		return DrawStatement.NULL;
	}

	private void addCreateMessage(final String senderName, final Object created, final String typeName,
			final StaticPart jpInfo) {
		DrawStatement stmt = new DrawStatement(senderName, created, typeName, jpInfo);
		this.statements.add(stmt);
	}

	private void addMessage(final String senderName, final String targetName, final StaticPart jpInfo,
			final Object[] args) {
		this.callerNames.push(senderName);
		DrawStatement stmt = new DrawStatement(senderName, targetName, jpInfo, args);
		this.statements.add(stmt);
	}

	private void addReturnMessage(final Object returnee, final Object returnValue, final StaticPart jpInfo) {
		String receiverName = this.callerNames.pop();
		String returneeName = this.getVarnameFor(returnee, jpInfo);
		addReturnMessage(receiverName, returneeName, returnValue, jpInfo);
	}

	private void addReturnMessage(final String receiverName, final String returneeName, final Object returnValue,
			final StaticPart jpInfo) {
		DrawStatement stmt = new DrawStatement(receiverName, returneeName, returnValue, jpInfo);
		this.statements.add(stmt);
	}

	/**
	 * The Class VarnameComparator compares two variable names. The name is of
	 * the form "A999...", e.g. first character is a (uppercase) letter and the
	 * next characters are a number (to base {@link Character#MAX_RADIX}). Only
	 * the number are used for comparison to get the creation order.
	 */
	private static final class VarnameComparator implements Comparator<String>, Serializable {

		private static final long serialVersionUID = 20140104L;

		/**
		 * Compares two variable names.
		 *
		 * @param x1
		 *            the x1
		 * @param x2
		 *            the x2
		 * @return a negativ valule if x1 < x2, "0" for x1 == x2, otherwise
		 *         positive value
		 * @see java.util.Comparator#compare(Object, Object)
		 */
		@Override
		public int compare(final String x1, final String x2) {
			return toNumber(x1) - toNumber(x2);
		}

		private static int toNumber(final String varname) {
			String numberPart = varname.substring(1);
			return Integer.parseInt(numberPart, Character.MAX_RADIX);
		}

	}

	/**
	 * The Class PlaceholderComparator compares two placeholder variables.
	 */
	private static final class PlaceholderComparator implements Comparator<DrawStatement>, Serializable {

		private static final long serialVersionUID = 20150614L;

		/**
		 * Compares two variable names.
		 *
		 * @param x1
		 *            the x1
		 * @param x2
		 *            the x2
		 * @return a negativ valule if x1 < x2, "0" for x1 == x2, otherwise
		 *         positive value
		 * @see java.util.Comparator#compare(Object, Object)
		 */
		@Override
		public int compare(final DrawStatement x1, final DrawStatement x2) {
			VarnameComparator comparator = new VarnameComparator();
			return comparator.compare(x1.getSender(), x2.getSender());
		}

	}

}
