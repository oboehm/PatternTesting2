/*
 * $Id: IORuntimeException.java,v 1.1 2016/12/30 20:52:26 oboehm Exp $
 *
 * Copyright (c) 2016 by Oliver Boehm
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
 * (c)reated 30.12.2016 by oboehm (ob@oasd.de)
 */

package patterntesting.runtime.io;

/**
 * If you want to throw an IOException as unchecked exception you can use this
 * exception here.
 * 
 * @author oboehm
 * @version $Revision: 1.1 $
 * @since 1.7 (30.12.2016)
 */
public class IORuntimeException extends RuntimeException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 20161230L;

    /**
     * Instantiates a new IO runtime exception.
     *
     * @param message the message
     */
    public IORuntimeException(String message) {
        super(message);
    }

    /**
     * Instantiates a new IO runtime exception.
     *
     * @param cause the cause
     */
    public IORuntimeException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

    /**
     * Instantiates a new IO runtime exception.
     *
     * @param message the message
     * @param cause the cause
     */
    public IORuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new IO runtime exception.
     *
     * @param message the message
     * @param cause the cause
     * @param enableSuppression the enable suppression
     * @param writableStackTrace the writable stack trace
     */
    public IORuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
