/*
 * $Id: TestOnFixture.java,v 1.7 2016/12/18 20:19:41 oboehm Exp $
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
 * (c)reated 06.09.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.fit.fixture;

import org.apache.commons.lang3.StringUtils;

import fit.ColumnFixture;
import patterntesting.runtime.junit.internal.TestOn;
import patterntesting.runtime.util.TestEnvironment;

/**
 * The Class TestOnFixture.
 *
 * @author oliver
 * @since 1.0.2 (06.09.2010)
 */
public class TestOnFixture extends ColumnFixture {

    /** Needed by FIT. */
    public String Attribute;
    /** Needed by FIT. */
    public String Value;
    /** Needed by FIT. */
    public String Actual;
    /** Needed by FIT. */
    public String Remarks;

    /**
     * Reset.
     *
     * @throws Exception the exception
     * @see fit.ColumnFixture#reset()
     */
    @Override
    public void reset() throws Exception {
        this.Remarks = "";
    }

    /**
     * Called by FIT.
     *
     * @return "yes" or "no"
     */
    public String matches() {
        String[] values = toStringArray(Value);
        if ("osName".equals(Attribute)) {
            return matchOsName(values, Actual) ? "yes" : "no";
        } else if ("osArch".equals(Attribute)) {
            return matchOsArch(values, Actual) ? "yes" : "no";
        }
        return "maybe";
    }

    /**
     * A given '"string"' (whereas the double quotes are part of the string)
     * will be converted to an array with one String inside.
     * A '{ "s1", "s2" }' will be converted to a string array.
     *
     * @param content a single string or string array
     * @return the string[]
     */
    protected static String[] toStringArray(final String content) {
        if (content.startsWith("\"")) {
            String[] values = new String[1];
            values[0] = content.substring(1, content.length() - 1);
            return values;
        }
        if (content.startsWith("{")) {
            return StringUtils.substringsBetween(content, "\"", "\"");
        }
        throw new IllegalArgumentException(content + " is neither a String nor an Array");
    }

    private static boolean matchOsName(final String[] values, final String actualOS) {
        TestEnvironment env = new TestEnvironment();
        env.setOsName(actualOS);
        TestOn testOn = new TestOn(env);
        testOn.setOsNames(values);
        return testOn.matches();
    }

    private static boolean matchOsArch(final String[] values, final String actualArch) {
        TestEnvironment env = new TestEnvironment();
        env.setOsArch(actualArch);
        TestOn testOn = new TestOn(env);
        testOn.setOsArchs(values);
        return testOn.matches();
    }

}

