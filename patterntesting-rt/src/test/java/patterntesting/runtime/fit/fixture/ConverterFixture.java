/*
 * $Id: ConverterFixture.java,v 1.7 2016/12/30 20:52:26 oboehm Exp $
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

import java.lang.reflect.*;
import java.net.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.*;

import fit.ColumnFixture;
import patterntesting.runtime.util.Converter;

/**
 * The Class ConverterFixture.
 *
 * @author oliver
 * @since 1.0.2 (25.08.2010)
 */
public class ConverterFixture extends ColumnFixture {

    private static final Logger LOG = LogManager.getLogger(ConverterFixture.class);
    /** Needed by FIT. */
    public String MethodName;
    /** Needed by FIT. */
    public String Value;
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
     * @return result of the method as String
     * @throws SecurityException the security exception
     * @throws NoSuchMethodException the no such method exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws IllegalAccessException the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     */
    public String result() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Value = Value.trim();
        Class<?> parameterTypes = guessType(Value);
        Method method = Converter.class.getMethod(MethodName, parameterTypes);
        Object arg = convert(Value, parameterTypes);
        return method.invoke(null, arg).toString();
    }

    private static Class<?> guessType(final String content) {
        if (content.startsWith("{")) {
            return Object[].class;
        }
        if (content.startsWith("\"")) {
            return String.class;
        }
        try {
            Long.parseLong(content);
            return long.class;
        } catch (NumberFormatException e) {
            LOG.debug("not a number: " + content);
        }
        try {
            return new URI(content).getClass();
        } catch (URISyntaxException e) {
            LOG.debug("not a URI: " + content);
        }
        return Object.class;
    }

    private static Object convert(final String content, final Class<?> type) {
        if (type.equals(Object[].class)) {
            return convertToObjectArray(content);
        }
        if (type.equals(String.class)) {
            return content.substring(1, content.length() - 1);
        }
        if (type.equals(long.class)) {
            return Long.parseLong(content);
        }
        try {
            return new URI(content);
        } catch (URISyntaxException e) {
            return content;
        }
    }

    private static Object[] convertToObjectArray(final String content) {
        String[] values = StringUtils.substringsBetween(content, "\"", "\"");
        return values;
    }

}
