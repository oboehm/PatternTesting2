/*
 * $Id: Broken.java,v 1.22 2016/12/10 20:55:18 oboehm Exp $
 *
 * Copyright (c) 2009 by Oliver Boehm
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
 * (c)reated 23.11.2009 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.annotation;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;

import static patterntesting.runtime.NullConstants.NULL_STRING;

/**
 * If you want to mark JUnit tests which does not work for the moment as
 * "broken" you can use this annotation. The tests will be skipped if you run
 * JUnit. You can also add a date till when you want to fix this "broken" test.
 * If the date is reached then the test will fail if it is not fixed (and
 * the @Broken removed).
 * <p>
 * Before 1.0.0 this annotation was handled by BrokenAspect. But since 1.0.0 it
 * is handled now by the {@link patterntesting.runtime.junit.SmokeRunner} class.
 * You should use this annotation together with
 * {@code @RunWith(SmokeRunner.class)}, also for JUnit 3 tests.
 * </p>
 * <p>
 * You can also use this annotation to mark a method or constructor as "broken".
 * If assertions are enabled an AssertionError will be thrown if you call such a
 * broken method. If not only a error message will be logged.
 * </p>
 * <p>
 * What is the difference to <code>@SkipTestOn</code>? "Broken" means, the test
 * does not work for the moment and should be (temporarily) skipped.
 * "SkipTestOn" means, this test is not constructed for that platform and should
 * be therefore skipped on it.
 * </p>
 *
 * @author oliver
 * @see SkipTestOn
 * @since 23.11.2009
 */
@Documented
@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Test
@Tag("broken")
public @interface Broken {

	/**
	 * You can change the default string to give a reason why the test is marked
	 * as "broken". You can use 'value=...' or 'wyh=...' to change it.
	 *
	 * @return the string
	 */
	String value() default "marked as @Broken";

	/**
	 * You can change the default string to give a reason why the test is marked
	 * as "broken". You can use 'value=...' or 'why=...' to change it.
	 *
	 * @return the string
	 */
	String why() default NULL_STRING;

	/**
	 * Use this attribute to mark the begin of the broken area, when it was
	 * detected that your JUnit test or method does not work as expected.
	 * <p>
	 * The format of the date is "dd-MMM-yyyy".
	 * </p>
	 *
	 * @return the string
	 */
	String since() default NULL_STRING;

	/**
	 * Use this attribute till you want the broken JUnit test to be fixed.
	 * <p>
	 * The format of the date is "dd-MMM-yyyy" or "dd-MMM-yyyy H:mm".
	 * </p>
	 *
	 * @return the string
	 */
	String till() default NULL_STRING;

	/**
	 * Does the test break only for user "Bob" and you can't fix it for him at
	 * the moment? Use <code>user="bob"</code> to mark it as broken for user
	 * account "bob".
	 * <p>
	 * Damn, the new member of the team, Bill, has the same problem and nobody
	 * is able to fix it. Ok, use <code>user={"bob", "bill"}</code> to mark the
	 * code as broken for both user.
	 * </p>
	 *
	 * @return the string[]
	 * @since 1.0
	 */
	String[] user() default NULL_STRING;

	/**
	 * Should a test be skipped on a single platform e.g. for Linux? Use "Linux"
	 * to mark it as broken only for the Linux platform. Instead of "Linux" you
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
	 * <p>
	 * Note: this attribute replaces the old 'os' attribute which was removed in
	 * 1.6.
	 * </p>
	 *
	 * @return the string[]
	 * @since 1.1
	 */
	String[] osName() default NULL_STRING;

	/**
	 * The test is broken only for the Intel architecture? Then you can use this
	 * attribute to limit it on this platform. You can define a single platform
	 * like <code>"x86_64"</code> for an Intel-Mac with 64 bit or multiple
	 * platform like <code>{ "x86_32", "x86_64" }</code> for Intel Mac with 42
	 * or 64 bit.
	 * <p>
	 * The format of this attribute must be the same as returned by the system
	 * property "os.arch".
	 * </p>
	 *
	 * @return the string[]
	 * @since 1.1
	 */
	String[] osArch() default NULL_STRING;

	/**
	 * The test breaks only on a special version of the operating system? Use
	 * this attribute to limit it. In contradiction to the other attributes the
	 * real version must start with the given version. I.e. if you define "10.6"
	 * as version this would match only real version "10.6.1" or "10.6.2".
	 * <p>
	 * You can't define a range of skipped versions. But several versions are
	 * allowed. So if you want to skip the tests in version 10.6.1 till 10.6.3
	 * define { "10.6.1", "10.6.2", "10.6.3" } as values. The format of this
	 * attribute must be the same as returned by the system property
	 * "osVersion".
	 * </p>
	 *
	 * @return the string[]
	 * @since 1.1
	 */
	String[] osVersion() default NULL_STRING;

	/**
	 * With this attribute you can express that the test is broken for the given
	 * host(s). Perhaps these hosts have not enough memory. You can define the
	 * host by its name or by its IP address.
	 *
	 * @return the string[]
	 * @since 1.1
	 */
	String[] host() default NULL_STRING;

	/**
	 * You want to mark the test as broken on a special version of the VM, e.g.
	 * on version "1.6.0_17" because you know that on this version there is a
	 * bug that caused your test not to work? Then use this attribute.
	 * <p>
	 * You can't define a range of skipped versions. But you regex (regular
	 * expressions) are allowed. And you can define more than one version (or
	 * regex).
	 * </p>
	 * <p>
	 * The format of this attribute must be the same as returned by the system
	 * property "java.version".
	 * </p>
	 *
	 * @return the string[]
	 * @since 1.1
	 */
	String[] javaVersion() default NULL_STRING;

	/**
	 * The test is broken for a special vendor VM? Use this attribute here. The
	 * format of this attribute must be the same as returned by the system
	 * property "java.vendor". Valid values are:
	 * <ul>
	 * <li>"Apple Inc."</li>
	 * <li>and others</li>
	 * </ul>
	 *
	 * @return the string[]
	 * @since 1.1
	 */
	String[] javaVendor() default NULL_STRING;

	/**
	 * You have some tests that are broken if some proxy properties are set?
	 * Define the property here (e.g. <code>property="proxy.host"</code>). If
	 * this property is set (and its value is not "false") the test will be
	 * skipped if this property is set as system property.
	 * <p>
	 * You can control more than property with this attribute. Then all
	 * properties must be "true" to skip the test.
	 * </p>
	 *
	 * @return the string[]
	 * @since 1.1
	 */
	String[] property() default NULL_STRING;

	/**
	 * Normally a test would be listed as "ignore" if it is marked as broken. If
	 * you must pass some quality gates which do not allow ignored tests (e.g.
	 * if you use Sonar that can be a quality gate) fix the bug and remove the
	 * {@link Broken} annotation.
	 * <p>
	 * Nevertheless you can set this flag to true. This can be useful if you
	 * want to fix the bug in a later release but must pass now some quality
	 * gates.
	 * </p>
	 * <p>
	 * NOTE: Because of limitations of JUnit and the RemoteTestRunner of Eclipse
	 * this flag not supported at class level, only on method level. If you'll
	 * try to set in on class level you will get an
	 * {@link UnsupportedOperationException} at runtime.
	 * </p>
	 *
	 * @return true, if successful
	 * @since 1.7
	 */
	boolean hide() default false;

}
