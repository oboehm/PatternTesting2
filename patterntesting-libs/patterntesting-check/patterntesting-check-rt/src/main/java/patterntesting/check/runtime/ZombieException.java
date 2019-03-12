/*
 * $Id: ZombieException.java,v 1.2 2016/01/06 20:47:19 oboehm Exp $
 *
 * Copyright (c) 2015 by Oliver Boehm
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
 * (c)reated 13.01.2015 by oliver (ob@oasd.de)
 */

package patterntesting.check.runtime;

import org.aspectj.lang.JoinPoint;

/**
 * This exception may be thrown if you try to use a class or call a method
 * marked as "@Zombie".
 *
 * @author oliver
 * @version $Revision: 1.2 $
 * @since 1.6 (13.01.2015)
 */
public class ZombieException extends DeprecatedCodeException {

    private static final long serialVersionUID = 20150113L;

    /**
     * Instantiates a new ZombieException.
     *
     * @param jp the Joinpoint from the Zombie context
     */
    public ZombieException(final JoinPoint jp) {
        super(jp, "is a @Zombie");
    }

}

