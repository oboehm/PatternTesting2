/*
 * $Id: Identifiable.java,v 1.3 2010/12/31 14:40:13 oboehm Exp $
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
 * (c)reated 13.09.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit.test;

import java.io.Serializable;

/**
 * This is an example for an interface which marks classes as "identifiable".
 *
 * @author oliver
 * @since 1.0.3 (13.09.2010)
 */
public interface Identifiable extends Serializable {

    /**
     * Gets the id.
     *
     * @return the id
     */
    long getId();

}

