/**
 * $Id: BlackSheep.java,v 1.1 2014/11/27 06:47:05 oboehm Exp $
 *
 * Copyright (c) 2014 by Oliver Boehm
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
 * (c)reated 21.11.2014 by oliver (ob@oasd.de)
 */

/*
 * $Id: BlackSheep.java,v 1.1 2014/11/27 06:47:05 oboehm Exp $
 *
 * Copyright (c) 2014 by Oliver Boehm
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
 * (c)reated 21.11.2014 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit.test;

/**
 * This is a example class with a wrong implementation of the clone method.
 *
 * @author oliver
 * @since 1.5 (21.11.2014)
 */
public class BlackSheep extends Sheep {

    /**
     * Instantiates a new black sheep.
     */
    public BlackSheep() {
        super();
    }

    /**
     * Instantiates a new black sheep.
     *
     * @param name the name
     */
    public BlackSheep(final String name) {
        super(name);
    }

    /**
     * This is an example of a wrong clone implementation. It returns the
     * wrong type - not of type BlackSheep but Sheep!
     *
     * @return the object
     * @see Sheep#clone()
     */
    @Override
    public Object clone() {
        return new Sheep(this.getName());
    }

}

