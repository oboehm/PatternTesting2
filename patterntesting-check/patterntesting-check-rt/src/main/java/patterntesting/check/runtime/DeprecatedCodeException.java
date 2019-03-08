/*
 * $Id: DeprecatedCodeException.java,v 1.3 2016/01/06 20:47:19 oboehm Exp $
 *
 * Copyright (c) 2008 by Oliver Boehm
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
 * (c)reated 17.02.2009 by oliver (ob@oasd.de)
 */
package patterntesting.check.runtime;

import org.aspectj.lang.JoinPoint;

/**
 * The Class DeprecatedCodeException.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.3 $
 * @since 17.02.2009
 */
public class DeprecatedCodeException extends RuntimeException {

    private static final long serialVersionUID = 20090217L;

    /**
     * Instantiates a new DeprectedCodeException.
     *
     * @param jp the Joinpoint from the context which is deprecated.
     */
    public DeprecatedCodeException(final JoinPoint jp) {
        this(jp, "is deprecated!");
    }

    /**
     * Instantiates a new DeprectedCodeException.
     *
     * @param jp the Joinpoint from the context which is deprecated.
     * @param additionalInfo the additional info
     */
    protected DeprecatedCodeException(final JoinPoint jp, final String additionalInfo) {
        super(jp.toShortString() + " " + additionalInfo);
    }

}
