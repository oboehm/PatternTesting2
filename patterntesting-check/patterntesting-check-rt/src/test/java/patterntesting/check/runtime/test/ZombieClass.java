/*
 * $Id: ZombieClass.java,v 1.2 2016/01/06 20:47:19 oboehm Exp $
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
 * (c)reated 11.01.2015 by oliver (ob@oasd.de)
 */

package patterntesting.check.runtime.test;

import java.net.URI;
import java.net.URISyntaxException;

import patterntesting.annotation.check.runtime.Zombie;

/**
 * This is only a dummy class to be able to test the {@link Zombie} annotation.
 *
 * @author oliver
 * @version $Revision: 1.2 $
 * @since 1.6 (11.01.2015)
 */
@Zombie
public class ZombieClass {

    /**
     * Gets link to the Zombie recipe.
     *
     * @return the URI for the Zombie recipe
     */
    public static URI getRecipe() {
        try {
            return new URI("http://www.greatcocktails.co.uk/Zombie.html");
        } catch (URISyntaxException ex) {
            throw new IllegalStateException("ups - no internet?", ex);
        }
    }

}

