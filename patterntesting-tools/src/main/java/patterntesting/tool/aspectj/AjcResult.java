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

/**
 * An error entry reported by Ajc (for a given file).
 *
 * @author <a href="mailto:vmassol@apache.org">Vincent Massol</a>
 *
 * @version $Id: AjcResult.java,v 1.5 2016/01/06 20:47:32 oboehm Exp $
 */
public final class AjcResult
{
    /**
     * Error message reported by Ajc.     */
    private final String errorMessage;

    /**
     * Line number.     */
    private final int line;

    /**
     * Instantiates a new ajc result.
     *
     * @param line the line where the error occurred
     * @param errorMessage the associated error message
     */
    public AjcResult(final int line, final String errorMessage)
    {
        this.line = line;
        this.errorMessage = errorMessage;
    }

    /**
     * Gets the line.
     *
     * @return the line number where the error occurred
     */
    public int getLine()
    {
        return this.line;
    }

    /**
     * Gets the error message.
     *
     * @return the error message
     */
    public String getErrorMessage()
    {
        return this.errorMessage;
    }

    /**
     * To string.
     *
     * @return the string
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        return "line = [" + getLine() + "], "
            + "message = [" + getErrorMessage() + "]";
    }

}
