/*
 * $Id: DrawStatement.java,v 1.23 2016/12/18 20:19:41 oboehm Exp $
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
 * (c)reated 03.06.2015 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.log.internal;

import java.util.Arrays;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint.StaticPart;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.aspectj.lang.reflect.MethodSignature;

import patterntesting.runtime.util.*;

/**
 * Internal class for caching the different draw statements.
 *
 * @author oliver
 * @since 1.4.1 (17.01.2014)
 */
public final class DrawStatement {

	/** The Constant NULL. */
	public static final DrawStatement NULL = new DrawStatement(DrawType.UNKNOWN, "null", "null");

	private final DrawType type;
	private final String sender;
	private final String target;
	private final StaticPart jpInfo;
	private final Object[] args;

	/**
	 * Instantiates a new draw statement of type 'PLACE_HOLDER'.
	 *
	 * @param type
	 *            the type
	 * @param sender
	 *            the sender (or name) for the given type
	 * @param jpInfo
	 *            the jp info
	 */
	public DrawStatement(final DrawType type, final String sender, final StaticPart jpInfo) {
		this.type = type;
		this.sender = sender;
		this.target = null;
		this.jpInfo = jpInfo;
		this.args = null;
	}

	/**
	 * Instantiates a new draw statement of type 'ACTOR' or 'OBJECT'.
	 *
	 * @param type
	 *            the type
	 * @param name
	 *            the name
	 * @param objectName
	 *            the object name
	 */
	public DrawStatement(final DrawType type, final String name, final String objectName) {
		this.type = type;
		this.sender = name;
		this.target = objectName;
		this.jpInfo = null;
		this.args = null;
	}

	/**
	 * Instantiates a new draw statement.
	 *
	 * @param senderName
	 *            the sender name
	 * @param createdObject
	 *            the created object
	 * @param typeName
	 *            the type name
	 * @param jpInfo
	 *            the jp info
	 */
	public DrawStatement(final String senderName, final Object createdObject, final String typeName,
			final StaticPart jpInfo) {
		this.type = DrawType.CREATE_MESSAGE;
		this.sender = senderName;
		this.target = typeName;
		this.jpInfo = jpInfo;
		this.args = Converter.toArray(createTargetName(createdObject));
	}

	/**
	 * Creates the target name depending on the given object. If the given
	 * object has an id this id will be part of the generated name.
	 *
	 * @param createdObject
	 *            the created object
	 * @return the string
	 */
	public static String createTargetName(final Object createdObject) {
		if (createdObject instanceof Class<?>) {
			Class<?> clazz = (Class<?>) createdObject;
			return ":" + clazz.getSimpleName();
		}
		String name = "";
		if (ReflectionHelper.hasId(createdObject)) {
			name += ReflectionHelper.getId(createdObject);
		}
		return name + ":" + createdObject.getClass().getSimpleName();
	}

	/**
	 * Instantiates a new draw statement.
	 *
	 * @param sender
	 *            the sender
	 * @param target
	 *            the target
	 * @param jpInfo
	 *            the jp info
	 * @param args
	 *            the args
	 */
	public DrawStatement(final String sender, final String target, final StaticPart jpInfo, final Object[] args) {
		this.type = DrawType.MESSAGE;
		this.sender = sender;
		this.target = target;
		this.jpInfo = jpInfo;
		this.args = args.clone();
	}

	/**
	 * Instantiates a new draw statement.
	 *
	 * @param receiverName
	 *            the receiver name
	 * @param returnee
	 *            the returnee
	 * @param returnValue
	 *            the return value
	 * @param jpInfo
	 *            the jp info
	 */
	public DrawStatement(final String receiverName, final String returnee, final Object returnValue,
			final StaticPart jpInfo) {
		this.type = DrawType.RETURN_MESSAGE;
		this.sender = receiverName;
		this.target = returnee;
		this.jpInfo = jpInfo;
		this.args = Converter.toArray(returnValue);
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public DrawType getType() {
		return this.type;
	}

	/**
	 * Gets the sender.
	 *
	 * @return the sender
	 */
	public String getSender() {
		return this.sender;
	}

	/**
	 * Gets the target.
	 *
	 * @return the target
	 */
	public String getTarget() {
		return this.target;
	}

	/**
	 * Gets the jp info.
	 *
	 * @return the jp info
	 */
	public StaticPart getJpInfo() {
		return this.jpInfo;
	}

	/**
	 * Gets the args.
	 *
	 * @return the args
	 */
	public Object[] getArgs() {
		return this.args;
	}

	/**
	 * Gets the args as string.
	 *
	 * @return the args as string
	 */
	public String getArgsAsString() {
		return Converter.toShortString(JoinPointHelper.getArgsAsString(this.args));
	}

	/**
	 * Checks if the {@link DrawStatement} is generated from call joinpoint.
	 *
	 * @return true, if is generated from a call joinpoint
	 */
	public boolean isFromCallJoinpoint() {
		return isFromJoinpoint("-call");
	}

	/**
	 * Checks if the {@link DrawStatement} is generated from execution
	 * joinpoint.
	 *
	 * @return true, if is generated from a execution joinpoint
	 */
	public boolean isFromExecutionJoinpoint() {
		return isFromJoinpoint("-execution");
	}

	private boolean isFromJoinpoint(final String typename) {
		if (this.jpInfo == null) {
			return false;
		}
		return this.jpInfo.getKind().endsWith(typename);
	}

	/**
	 * Checks if the given parameter has the same origin and source location as
	 * the {@link DrawStatement}.
	 *
	 * @param otherJpInfo
	 *            the other Joinpoint info
	 * @return true, if successful
	 */
	public boolean hasSameSignatureAs(final StaticPart otherJpInfo) {
		if (this.jpInfo == null) {
			return false;
		}
		switch (type) {
		case CREATE_MESSAGE:
			if (!(otherJpInfo.getSignature() instanceof ConstructorSignature)) {
				return false;
			}
			break;
		case MESSAGE:
			if (!(otherJpInfo.getSignature() instanceof MethodSignature)) {
				return false;
			}
			break;
		default:
			break;
		}
		return this.jpInfo.getSignature().toLongString().equals(otherJpInfo.getSignature().toLongString());
	}

	/**
	 * If the given name is part of any stored actor in this statement 'true'
	 * will be returned.
	 *
	 * @param name
	 *            the name
	 * @return true, if is involved
	 */
	public boolean hasActor(final String name) {
		switch (this.type) {
		case CREATE_MESSAGE:
		case MESSAGE:
			return name.equals(this.target);
		case RETURN_MESSAGE:
			return name.equals(this.sender) || name.equals(this.target);
		default:
			return false;
		}
	}

	/**
	 * Checks for message.
	 *
	 * @return true, if successful
	 */
	public boolean hasMessage() {
		switch (this.type) {
		case CREATE_MESSAGE:
		case MESSAGE:
		case RETURN_MESSAGE:
			return true;
		default:
			return false;
		}
	}

	/**
	 * A message points to left, if the given senderName was created after the
	 * given targetName. This information can be derived from the name.
	 *
	 * @return true, if successful
	 */
	public boolean hasMessageToLeft() {
		switch (this.type) {
		case CREATE_MESSAGE:
		case MESSAGE:
			return toNumber(this.sender).compareTo(toNumber(this.target)) > 0;
		case RETURN_MESSAGE:
			return toNumber(this.target).compareTo(toNumber(this.sender)) > 0;
		default:
			return false;
		}
	}

	/**
	 * Checks for message to right.
	 *
	 * @return true, if successful
	 */
	public boolean hasMessageToRight() {
		switch (this.type) {
		case CREATE_MESSAGE:
		case MESSAGE:
		case RETURN_MESSAGE:
			return !this.hasMessageToLeft();
		default:
			return false;
		}
	}

	/**
	 * This is the opposite to the toName() method and returns the number part
	 * of a variable name.
	 *
	 * @param name
	 *            the name
	 * @return the string
	 */
	private static String toNumber(final String name) {
		return name.substring(1);
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.type.hashCode() + ((this.sender == null) ? 0 : this.sender.hashCode());
	}

	/**
	 * Equals.
	 *
	 * @param obj
	 *            the obj
	 * @return true, if successful
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof DrawStatement)) {
			return false;
		}
		DrawStatement other = (DrawStatement) obj;
		return (this.type == other.type) && StringUtils.equals(this.sender, other.sender)
				&& StringUtils.equals(this.target, other.target) && ObjectUtils.equals(this.jpInfo, other.jpInfo)
				&& Arrays.equals(this.args, other.args);
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		switch (type) {
		case ACTOR:
		case OBJECT:
		case PLACEHOLDER_OBJECT:
			return this.type + "(" + this.sender + ",\"" + this.target + "\");";
		case CREATE_MESSAGE:
			return this.sender + " --- <<create>> --> " + this.target;
		case MESSAGE:
			return this.sender + " --- " + this.jpInfo + " --> " + this.target;
		case RETURN_MESSAGE:
			return this.sender + " <-- " + this.jpInfo + " --- " + this.target;
		default:
			return "# " + this.type + " statement";
		}
	}

}
