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
import java.util.Vector;

/**
 * All errors reported by Ajc for a given file.
 *
 * @author <a href="mailto:vmassol@apache.org">Vincent Massol</a>
 *
 * @version $Id: AjcFileResult.java,v 1.6 2016/01/06 20:47:32 oboehm Exp $
 */
public final class AjcFileResult {

    /**
     * Source file name in error.     */
    private final String fileName;

    /**
     * List of errors for this file.     */
    private final Vector<AjcResult> errors = new Vector<AjcResult>();

    /**
     * List of warnings for this file.
     */
    private final Vector<AjcResult> warnings = new Vector<AjcResult>();

    /**
     * Instantiates a new ajc file result.
     *
     * @param fileName the file name in error
     */
    public AjcFileResult(final String fileName)
    {
        this.fileName = fileName;
    }

    /**
     * Adds the warning.
     *
     * @param warning a warning that happened on this file
     */
    public void addWarning(final AjcResult warning)
    {
       this.warnings.add(warning);
    }

    /**
     * Adds the error.
     *
     * @param error an error that happened on this file
     */
    public void addError(final AjcResult error)
    {
        this.errors.add(error);
    }

    /**
     * Gets the file name.
     *
     * @return the source file name in error
     */
    public String getFileName()
    {
        return this.fileName;
    }

    /**
     * Gets the warnings.
     *
     * @return the list of warnings on this file
     */
    public Enumeration<AjcResult> getWarnings()
    {
       return this.warnings.elements();
    }

    /**
     * Gets the errors.
     *
     * @return the list of errors on this file
     */
    public Enumeration<AjcResult> getErrors()
    {
        return this.errors.elements();
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
        StringBuffer buffer = new StringBuffer();
        buffer.append("{fileName = [" + getFileName() + "], "
            + "errors = [");
        Enumeration<AjcResult> enumeration = this.errors.elements();
        while (enumeration.hasMoreElements())
        {
            AjcResult result = enumeration.nextElement();
            buffer.append("[");
            buffer.append(result.toString());
            buffer.append("]");
        }
        buffer.append("], warnings = [");
        Enumeration<AjcResult> e = this.getWarnings();
        while(e.hasMoreElements())
        {
           AjcResult result = e.nextElement();
           buffer.append(result.toString());
           buffer.append("]");
        }
        buffer.append("]}");
        return buffer.toString();
    }

}
