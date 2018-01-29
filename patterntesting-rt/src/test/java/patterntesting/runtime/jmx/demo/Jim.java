/*
 * $Id: Jim.java,v 1.5 2016/12/10 20:55:17 oboehm Exp $
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
 * (c)reated 19.02.2009 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.jmx.demo;

import patterntesting.runtime.jmx.MBeanRegistry;

/**
 * The Class Jim.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.5 $
 * @since 19.02.2009
 */
public final class Jim implements JimMBean, MBeanRegistry {

    private static final long serialVersionUID = 20090219L;
    private String name;

    /**
     * Instantiates a new jim.
     */
    public Jim() {
        this.name = "Jim";
    }

    /**
     * Instantiates a new jim.
     *
     * @param name the name
     */
    public Jim(final String name) {
        this.name = name;
    }

    /**
     * Gets the name.
     *
     * @return the name
     * @see JimMBean#getName()
     */
    @Override
	public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     * @see JimMBean#setName(java.lang.String)
     */
    @Override
	public void setName(final String name) {
        this.name = name;
    }

}
