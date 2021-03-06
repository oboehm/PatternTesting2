/*
 *========================================================================
 *
 * Copyright 2001-2004 Vincent Massol & Matt Smith.
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
 *========================================================================
 */
package patterntesting.tool.aspectj;

import java.util.Enumeration;

/**
 * The Interface ResultFormatter.
 *
 * @author mattsmith
 */
public interface ResultFormatter {

    /**
     * Format.
     *
     * @param ajcFileResults results to be formatted
     * @return formatted strings
     */
    String format(Enumeration<AjcFileResult> ajcFileResults);

}
