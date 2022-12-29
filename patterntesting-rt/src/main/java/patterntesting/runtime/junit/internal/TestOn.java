/*
 * $Id: TestOn.java,v 1.35 2016/12/18 20:19:41 oboehm Exp $
 *
 * Copyright (c) 2010 by Oliver Boehm
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
 * (c)reated 23.04.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit.internal;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import patterntesting.runtime.net.Localhost;
import patterntesting.runtime.util.Converter;
import patterntesting.runtime.util.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

/**
 * This is a local helper class for the RunTestOn and SkipTestOn annotation. So
 * most of the methods are package default because there is no need to access
 * from somewhere else.
 *
 * @author oliver
 * @since 1.0 (23.04.2010)
 */
public final class TestOn {

	private static final Logger LOG = LoggerFactory.getLogger(TestOn.class);
	private static final String[] EMPTY_STRINGS = { "" };

	private final Environment env;
	private String[] osNames = EMPTY_STRINGS;
	private String[] osArchs = EMPTY_STRINGS;
	private String[] osVersions = EMPTY_STRINGS;
	private String[] hosts = EMPTY_STRINGS;
	private String[] javaVersions = EMPTY_STRINGS;
	private String[] javaVendors = EMPTY_STRINGS;
	private String[] users = EMPTY_STRINGS;
	private String[] systemProps = EMPTY_STRINGS;
	private int[] days = {};
	private String[] times = EMPTY_STRINGS;
	/** Contains the reason of the last match. */
	private String reason;

	/**
	 * Instantiates a new object with the default environment.
	 */
	public TestOn() {
		this(Environment.INSTANCE);
	}

	/**
	 * Instantiates a new object. For test you instantiate it now with your
	 * "own" environment.
	 *
	 * @param env
	 *            the env
	 */
	public TestOn(final Environment env) {
		this.env = env;
	}

	/**
	 * Sets the os names.
	 *
	 * @param osNames
	 *            the osNames to set
	 */
	public void setOsNames(final String[] osNames) {
		this.osNames = osNames.clone();
	}

	/**
	 * Sets the os names.
	 *
	 * @param fallback
	 *            the values for fallback if parameter os is empty
	 * @param os
	 *            the osNames to set
	 */
	public void setOsNames(final String[] fallback, final String[] os) {
		this.osNames = StringUtils.isEmpty(os[0]) ? fallback.clone() : os.clone();
	}

	/**
	 * Sets the os archs.
	 *
	 * @param osArchs
	 *            the osArchs to set
	 */
	public void setOsArchs(final String[] osArchs) {
		this.osArchs = osArchs.clone();
	}

	/**
	 * Sets the os versions.
	 *
	 * @param osVersions
	 *            the osVersions to set
	 */
	public void setOsVersions(final String[] osVersions) {
		this.osVersions = osVersions.clone();
	}

	/**
	 * Sets the hosts.
	 *
	 * @param hosts
	 *            the hosts to set
	 */
	public void setHosts(final String[] hosts) {
		this.hosts = hosts.clone();
	}

	/**
	 * Sets the java versions.
	 *
	 * @param javaVersions
	 *            the javaVersions to set
	 */
	public void setJavaVersions(final String[] javaVersions) {
		this.javaVersions = javaVersions.clone();
	}

	/**
	 * Sets the java vendors.
	 *
	 * @param javaVendors
	 *            the javaVendors to set
	 */
	public void setJavaVendors(final String[] javaVendors) {
		this.javaVendors = javaVendors.clone();
	}

	/**
	 * Sets the users.
	 *
	 * @param users
	 *            the users to set
	 */
	public void setUsers(final String[] users) {
		this.users = users.clone();
	}

	/**
	 * Sets the system props.
	 *
	 * @param systemProps
	 *            the systemProps to set
	 */
	public void setSystemProps(final String[] systemProps) {
		this.systemProps = systemProps.clone();
	}

	/**
	 * Sets the days.
	 *
	 * @param days
	 *            the days to set
	 */
	public void setDays(final int[] days) {
		this.days = days.clone();
	}

	/**
	 * Sets the times.
	 *
	 * @param times
	 *            the times to set
	 */
	public void setTimes(final String[] times) {
		this.times = times.clone();
	}

	/**
	 * Sets the reason.
	 *
	 * @param reason
	 *            the reason to set
	 */
	public void setReason(final String reason) {
		this.reason = reason;
	}

	/**
	 * Gets the reason.
	 *
	 * @return the reason
	 */
	public String getReason() {
		return hasReason() ? this.reason + " detected" : "";
	}

	/**
	 * Checks for reason.
	 *
	 * @return true, if reason is set
	 */
	public boolean hasReason() {
		return StringUtils.isNotBlank(this.reason);
	}

	/**
	 * If one of the attributes matches true will returned.
	 *
	 * @return true, if successful
	 */
	public boolean matches() {
		if (!matchesOs() || !matchesJava() || !matchesTime()) {
			return false;
		}
		if (!matches(this.users, this.env.getUserName())) {
			return false;
		}
		if (!Environment.matchesOneOf(systemProps)) {
			return false;
		}
		// this call is expensive so we shift it to the end
		if (!runsOn(this.hosts)) {
			return false;
		}
		reason = buildReason(this.osNames[0], this.env.getOsName()) + buildReason(this.osArchs[0], this.env.getOsArch())
				+ buildReason(this.osVersions[0], this.env.getOsVersion())
				+ buildReason(this.javaVersions[0], "JDK " + this.env.getJavaVersion())
				+ buildReason(this.javaVendors[0], this.env.getJavaVendor() + " as vendor")
				+ buildReason(this.users[0], this.env.getUserName() + " as user")
				+ buildReason(this.systemProps[0], Converter.toString(systemProps))
				+ buildReason(this.hosts[0], "host " + Converter.toString(this.hosts))
				+ (this.days.length == 0 ? "" : " day " + Converter.toString(this.days))
				+ buildReason(this.times[0], "time " + Converter.toString(this.times));
		return true;
	}

	private boolean matchesTime() {
		if (!isInDays()) {
			return false;
		}
		if (!isTimeInRange()) {
			return false;
		}
		return true;
	}

	private boolean matchesOs() {
		return matches(this.osNames, this.env.getOsName()) && matches(this.osArchs, this.env.getOsArch())
				&& matches(this.osVersions, this.env.getOsVersion());
	}

	private boolean matchesJava() {
		return matches(this.javaVersions, this.env.getJavaVersion())
				&& matches(this.javaVendors, this.env.getJavaVendor());
	}

	private static String buildReason(String cause, String reason) {
		return StringUtils.isEmpty(cause) ? "" : reason;
	}

	/**
	 * Checks if is value given.
	 *
	 * @return true, if is value given
	 */
	public boolean isValueGiven() {
		return isValueGiven(this.osNames[0], this.osArchs[0], this.osVersions[0])
				|| isValueGiven(this.javaVersions[0], this.javaVendors[0])
				|| isValueGiven(this.hosts[0], this.users[0], this.systemProps[0], this.times[0])
				|| this.days.length > 0;
	}

	private static boolean isValueGiven(String... values) {
		for (String val : values) {
			if (StringUtils.isNotEmpty(val)) {
				return true;
			}
		}
		return false;
	}

	private static boolean matches(final String[] names, final String name) {
		if (StringUtils.isEmpty(names[0])) {
			LOG.trace("Empty names are ignored for matching of '{}'.", name);
			return true;
		}
		for (int i = 0; i < names.length; i++) {
			if (matches(names[i], name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * If the given pattern parameter contains an asterisk ("*") it is
	 * considered as pattern. Otherwise only the beginning with name must match.
	 *
	 * @param pattern
	 *            the pattern
	 * @param name
	 *            the name
	 * @return true, if successful
	 * @since 1.1
	 */
	private static boolean matches(final String pattern, final String name) {
		if (name.startsWith(pattern)) {
			return true;
		}
		if (pattern.contains("*") || (pattern.contains("?"))) {
			String regex = wildcardToRegex(pattern);
			return name.matches(regex);
		}
		return false;
	}

	private boolean isInDays() {
		if (this.days.length == 0) {
			LOG.trace("Empty days are ignored for matching.");
			return true;
		}
		int d = getDayOfWeek();
		for (int i = 0; i < this.days.length; i++) {
			if (d == days[i]) {
				return true;
			}
		}
		return false;
	}

	private static int getDayOfWeek() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int dow = cal.get(Calendar.DAY_OF_WEEK);
		return ((dow + 5) % 7 + 1);
	}

	private boolean isTimeInRange() {
		if (StringUtils.isEmpty(this.times[0])) {
			LOG.trace("Empty time ranges are ignored for matching.");
			return true;
		}
		for (int i = 0; i < this.times.length; i++) {
			if (isTimeInRage(this.times[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the given range matches the actual time. If midnight is int the
	 * given range like "22:00-4:00" it is splitted into two parts,
	 * "22:00-24:00" and "0:00"-"4:00" and then checked.
	 *
	 * @param fromTo
	 *            the from to
	 * @return true, if is time in rage
	 */
	private static boolean isTimeInRage(final String fromTo) {
		String[] range = fromTo.split("-");
		Time t1 = Converter.toTime(range[0]);
		Time t2 = Converter.toTime(range[1]);
		String from = t1.toString();
		String to = t2.toString();
		if (t2.before(t1)) {
			String beforeMidnight = t1 + "-24:00";
			String afterMidnight = "0:00-" + t2;
			return isTimeInRage(beforeMidnight) || isTimeInRage(afterMidnight);
		}
		if ("00:00:00".equals(to)) {
			to = "24:00:00";
		}
		Time tnow = new Time(System.currentTimeMillis());
		String now = tnow.toString();
		return (now.compareTo(from) >= 0) && (now.compareTo(to) <= 0);
	}

	private static boolean runsOn(final String[] hosts) {
		try {
			InetAddress localhost = InetAddress.getLocalHost();
			if (ArrayUtils.contains(hosts, localhost.getHostAddress()) || matches(hosts, localhost.getHostName())) {
				return true;
			}
			return Localhost.matches(hosts);
		} catch (UnknownHostException e) {
			LOG.debug("Cannot get local InetAddress - using localhost:", e);
			return runsOnLocalhost(hosts);
		}
	}

	private static boolean runsOnLocalhost(final String[] hosts) {
		return ArrayUtils.contains(hosts, "127.0.0.1") || containsIgnoreCase(hosts, "localhost");
	}

	private static boolean containsIgnoreCase(final String[] names, final String name) {
		for (int i = 0; i < names.length; i++) {
			if (name.equalsIgnoreCase(names[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Wildcard to regex.
	 *
	 * @param wildcard
	 *            the wildcard
	 * @return the string
	 * @see "http://www.rgagnon.com/javadetails/java-0515.html"
	 */
	private static String wildcardToRegex(final String wildcard) {
		StringBuilder s = new StringBuilder(wildcard.length());
		s.append('^');
		for (int i = 0, is = wildcard.length(); i < is; i++) {
			char c = wildcard.charAt(i);
			s.append(charToRegex(c));
		}
		s.append('$');
		return (s.toString());
	}

	private static String charToRegex(char c) {
		switch (c) {
		case '*':
			return ".*";
		case '?':
			return ".";
		default:
			return escapeSpecialRegexpCharacters(c);
		}
	}

	private static String escapeSpecialRegexpCharacters(char c) {
		if ("()[]$^.{}|\\".contains(Character.toString(c))) {
			return "\\" + c;
		} else {
			return Character.toString(c);
		}
	}

}
