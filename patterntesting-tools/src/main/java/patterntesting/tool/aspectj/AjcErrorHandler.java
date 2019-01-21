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

import java.util.*;

import org.aspectj.bridge.*;

/**
 * Error handler to pass to the Ajc compiler in order to get
 * structured error information.
 *
 * @author <a href="mailto:vmassol@apache.org">Vincent Massol</a>
 * @author <a href="mailto:matt@ojojo.com">Matt Smith</a>
 *
 * @version $Id: AjcErrorHandler.java,v 1.5 2016/01/06 20:47:32 oboehm Exp $
 */
public final class AjcErrorHandler extends MessageHandler {

    /**
     * Lists of errors received from the Ajc compiler. Array of
     * {@link AjcFileResult} objects.     */
    private final Hashtable<String, AjcFileResult> errors = new Hashtable<String, AjcFileResult>();

    /**
     * Gets the results.
     *
     * @return the list of errors received from the Ajc compiler as
     *          a List of AjcResult objects
     */
    public Enumeration<AjcFileResult> getResults() {
        return this.errors.elements();
    }

    /**
     * Gets the error results.
     *
     * @return the map of errors received from the Ajc compiler as
     *          a map of AjcResult objects
     */
    public Map<String, AjcFileResult> getErrorResults() {
        return errors;
    }

    /**
     * Handle message.
     *
     * @param message the message
     * @return true, if successful
     * @see org.aspectj.bridge.IMessageHandler#handleMessage(org.aspectj.bridge.IMessage)
     */
    @Override
    public boolean handleMessage(final IMessage message) {
        ISourceLocation location = message.getSourceLocation();
        if (location != null) {
            String path = location.getSourceFile().getPath();
            AjcFileResult fileResult = this.errors.get(path);
            if (fileResult == null) {
                fileResult = new AjcFileResult(path);
                this.errors.put(path, fileResult);
            }
            if (message.getKind() == IMessage.ERROR) {
                fileResult.addError(new AjcResult(location.getLine(), message
                        .getMessage()));
            } else {
                fileResult.addWarning(new AjcResult(location.getLine(), message
                        .getMessage()));
            }
            // this.write();
        }
        return true;
    }

	/**
	 * To string.
	 *
	 * @return the string
	 * @see Object#toString()
	 */
    @Override
    public String toString() {
    	if (errors.size() > 0) {
    		return "AjcErrorHandler: with error/warning(s)";
    	} else {
    		return super.toString();
    	}
    }

}
