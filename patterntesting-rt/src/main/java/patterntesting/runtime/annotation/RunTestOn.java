/*
 * $Id: RunTestOn.java,v 1.21 2016/12/10 20:55:18 oboehm Exp $
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
 * (c)reated 27.01.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.annotation;

import static patterntesting.runtime.NullConstants.NULL_STRING;

import java.lang.annotation.*;

/**
 * You have a test which should be run only on Linux or another operationg
 * system? Then you can use this annotation to run a single test (put the
 * annotation in front of the test method) or all tests in this class (put the
 * annotation in front of the class) on the given platform.
 *
 * @author oliver
 * @see SkipTestOn
 * @since 1.0 (27.01.2010)
 */
@Documented
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface RunTestOn {

	/**
	 * Should a test be run only on a single platform e.g. on Linux? Use "Linux"
	 * and the test will run only on the Linux platform. Instead of "Linux" you
	 * can use any other operation system. The format must be the same as
	 * returned by the system property "os.name". Valid values are:
	 * <ul>
	 * <li>"Linux"</li>
	 * <li>"Mac OS X" (for Mac),</li>
	 * <li>"Windows XP"</li>
	 * <li>and others (see http://lopica.sourceforge.net/os.html).</li>
	 * </ul>
	 * <p>
	 * Multiple values like <code>{ "Linux", "Mac OS X" }</code> are allowed. If
	 * no operation system is given you will get an IllegalArgumentException.
	 * </p>
	 * <p>
	 * The format of this attribute must be the same as returned by system
	 * property "os.name".
	 * </p>
	 * <p>
	 * Since 1.2 wildcards (*, ?) are supported.
	 * </p>
	 *
	 * @return the string[]
	 */
	String[] osName() default NULL_STRING;

	/**
	 * You want the test only to be run on the Intel architecture? Then you can
	 * use this attribute to limit it on this platform. You can define a single
	 * platform like <code>"x86_64"</code> for an Intel-Mac with 64 bit or
	 * multiple platform like <code>{ "x86_32", "x86_64" }</code> for Intel Mac
	 * with 42 or 64 bit.
	 * <p>
	 * The format of this attribute must be the same as returned by the ystem
	 * property "os.arch".
	 * </p>
	 * <p>
	 * Since 1.2 wildcards (*, ?) are supported.
	 * </p>
	 *
	 * @return the string[]
	 */
	String[] osArch() default NULL_STRING;

	/**
	 * You want the test only to be run on a special version of the operating
	 * system? Use this attribute to limit it. In contradiction to the other
	 * attributes the real version must start with the given version. I.e. if
	 * you define "10.6" as version this would match only real version "10.6.1"
	 * or "10.6.2".
	 * <p>
	 * You can't define a range of versions. But several versions are allowed.
	 * So if you want to run the tests in version 10.6.1 till 10.6.3 define {
	 * "10.6.1", "10.6.2", "10.6.3" } as values.
	 * </p>
	 * <p>
	 * Since 1.2 wildcards (*, ?) are supported.
	 * </p>
	 *
	 * @return the string[]
	 */
	String[] osVersion() default NULL_STRING;

	/**
	 * You want the test to be run on a special version of the VM, e.g. on
	 * version "1.6.0_17" because you know that on this version there is a bug
	 * that caused your test not to work? Then use this attribute.
	 * <p>
	 * You can't define a range of skipped versions. But regex (regular
	 * expressions) are allowed. And you can define more than one version (or
	 * regex).
	 * </p>
	 * <p>
	 * The format of this attribute must be the same as returned by the system
	 * property "java.version".
	 * </p>
	 * <p>
	 * Since 1.2 wildcards (*, ?) are supported.
	 * </p>
	 *
	 * @return the string[]
	 */
	String[] javaVersion() default NULL_STRING;

	/**
	 * You want the test to be run on a special vendor VM? Use this attribute
	 * here. The format of this attribute must be the same as returned by the
	 * system property "java.vendor". Valid values are:
	 * <ul>
	 * <li>"Apple Inc."</li>
	 * <li>and others</li>
	 * </ul>
	 * <p>
	 * Since 1.2 wildcards (*, ?) are supported.
	 * </p>
	 *
	 * @return the string[]
	 */
	String[] javaVendor() default NULL_STRING;

	/**
	 * With this attribute you can express that the test should be run on the
	 * given host(s). You can define the host by its name or by its IP address.
	 * <p>
	 * Since 1.2 wildcards (*, ?) are supported.
	 * </p>
	 *
	 * @return the string[]
	 */
	String[] host() default NULL_STRING;

	/**
	 * Does the test should be run only for user "Bob" because he is the only
	 * one who has access to a secret webservice? Use <code>user="bob"</code> to
	 * run this test only for his account.
	 * <p>
	 * Yep, the new boss of the team, Bill, is also allow to call this
	 * webservice. Ok, use <code>user={"bob", "bill"}</code> skip the test for
	 * both accounts.
	 * </p>
	 * <p>
	 * Since 1.2 wildcards (*, ?) are supported.
	 * </p>
	 *
	 * @return the string[]
	 * @since 1.1
	 */
	String[] user() default NULL_STRING;

	/**
	 * You have some tests that runs only if you are online? Define a property
	 * (e.g. <code>property="online"</code>). If this property is set (and its
	 * value is not "false") the test will be executed if you start the JavaVM
	 * with <code>java -Donline ...</code> or
	 * <code>java -Donline=true ...</code>.
	 * <p>
	 * You can control more than property with this attribute. Then all
	 * properties must be "true" to run the test.
	 * </p>
	 * <p>
	 * Wildcards (*, ?) are not yet supported for system properties.
	 * </p>
	 *
	 * @return the string[]
	 */
	String[] property() default NULL_STRING;

	/**
	 * With the day attribute you can limit the run of your tests on special
	 * days. Some tests (especially long running integration tests) need not to
	 * run all days.
	 * <p>
	 * The days are given as number where 1 = Monday and 7 = Sunday.
	 * </p>
	 *
	 * @return e.g. { 6, 7 } for the weekend
	 * @since 1.6
	 */
	int[] day() default {};

	/**
	 * With the time attribute you can limit the run of your tests on special
	 * hours. E.g. you can define some "happy hours" for tests which needs a
	 * little bit longer.
	 * <p>
	 * The pattern for the definition of the time range is "HH:mm-HH:mm", e.g.
	 * "0:00-24:00" for the whole day. You can also define several time ranges
	 * e.g. { "6:00-8:00", "18:00-20:00" }
	 * </p>
	 *
	 * @return e.g. "18:00-6:00" for the whole night for 18 o'clock and 6
	 *         o'clock.
	 * @since 1.6
	 */
	String[] time() default NULL_STRING;

	/**
	 * Normally a test would be listed as "ignore" if it is not run. If you do
	 * not want this set this flag to true. This can be useful if you must pass
	 * some quality gates which do not allow ignored tests (e.g. if you use
	 * Sonar that can be a quality gate).
	 * <p>
	 * NOTE: Because of limitations of JUnit and the RemoteTestRunner of Eclipse
	 * this flag not supported at class level, only on method level. If you'll
	 * try to set in on class level you will get an
	 * {@link UnsupportedOperationException} at runtime.
	 * </p>
	 *
	 * @return true, if successful
	 * @since 1.5
	 */
	boolean hide() default false;

	/**
	 * Here you can define the name of the operating system or version of the
	 * JDK for which a test should be executed. But it is better to use the
	 * attributes "osName" or "javaVersion" here because otherwise
	 * PatternTesting tries to guess what you mean.
	 * <p>
	 * Since 1.2 wildcards (*, ?) are supported.
	 * </p>
	 *
	 * @return the string[]
	 * @see #osName()
	 * @see #javaVersion()
	 */
	String[] value() default NULL_STRING;

}
