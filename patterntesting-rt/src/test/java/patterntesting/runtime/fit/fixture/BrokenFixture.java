/*
 * $Id: BrokenFixture.java,v 1.11 2016/12/18 20:19:41 oboehm Exp $
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
 * (c)reated 25.08.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.fit.fixture;

import java.lang.annotation.Annotation;

import patterntesting.runtime.annotation.Broken;
import patterntesting.runtime.junit.internal.SmokeFilter;
import patterntesting.runtime.util.Converter;
import patterntesting.runtime.util.TestEnvironment;

/**
 * Because we can't test the Broken annotation direct we do it indirect by
 * accessing the SmokeFilter. The SmokeFilter is used by the SmokeRunner to
 * decide if a test is broken or not.
 *
 * @author oliver
 * @since 1.0.2 (25.08.2010)
 */
public class BrokenFixture extends TestOnFixture {

    /**
     * Called by FIT.
     *
     * @return "yes" or "no"
     */
    public String isExecuted() {
        String stringValue = Value.substring(1, Value.length() - 1);
        if ("till".equals(Attribute)) {
            return tillDateReached(stringValue, Actual);
        }
        String[] values = toStringArray(Value);
        if ("osName".equals(Attribute)) {
            return matchOsName(values, Actual);
        }
        if ("user".equals(Attribute)) {
            return matchUser(values, Actual);
        }
        return "don't know";
    }

    private static String tillDateReached(final String tillDate, final String actDate) {
        SmokeFilter filter = new SmokeFilter();
        filter.setToday(Converter.toDate(actDate));
        Broken broken = createBroken(tillDate, new String[] { "" }, new String[] { "" });
        return filter.isBroken("testMethod", broken) ? "no" : "yes";
    }

    private static String matchOsName(final String[] values, final String actualOS) {
        SmokeFilter filter = new SmokeFilter();
        TestEnvironment env = new TestEnvironment();
        env.setOsName(actualOS);
        filter.setEnvironment(env);
        Broken broken = createBroken("", values, new String[] { "" });
        return filter.isBroken("testMethod", broken) ? "no" : "yes";
    }

    private static String matchUser(final String[] values, final String actualUser) {
        SmokeFilter filter = new SmokeFilter();
        TestEnvironment env = new TestEnvironment();
        env.setUserName(actualUser);
        filter.setEnvironment(env);
        Broken broken = createBroken("", new String[] { "" }, values);
        return filter.isBroken("testMethod", broken) ? "no" : "yes";
    }

    private static Broken createBroken(final String tillDate, final String[] osNames,
            final String[] userNames) {
        Broken broken = new Broken() {
            @Override
			public Class<? extends Annotation> annotationType() {
                return Broken.class;
            }
            @Override
			public String why() {
                return value();
            }
            @Override
			public String value() {
                return "marked as broken for testing";
            }
            @Override
			public String[] user() {
                return userNames;
            }
            @Override
			public String till() {
                return tillDate;
            }
            @Override
			public String since() {
                return "";
            }
            @Override
			public String[] property() {
                return new String[] { "" };
            }
            @Override
			public String[] osVersion() {
                return new String[] { "" };
            }
            @Override
			public String[] osName() {
                return osNames;
            }
            @Override
			public String[] osArch() {
                return new String[] { "" };
            }
            @Override
			public String[] javaVersion() {
                return new String[] { "" };
            }
            @Override
			public String[] javaVendor() {
                return new String[] { "" };
            }
            @Override
			public String[] host() {
                return new String[] { "" };
            }
            @Override
			public boolean hide() {
                return true;
            }
        };
        return broken;
    }

}

