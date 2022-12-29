/*
 * $Id: ZombieAspectTest.java,v 1.3 2016/12/18 21:59:31 oboehm Exp $
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

package patterntesting.check.runtime;

import java.net.URI;

import org.slf4j.*;
import org.slf4j.LoggerFactory;

import org.junit.jupiter.api.Test;
import patterntesting.check.runtime.test.ZombieClass;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the ZombieAspect.
 *
 * @author oliver
 * @version $Revision: 1.3 $
 * @since 1.6 (11.01.2015)
 */
public final class ZombieAspectTest extends AbstractRuntimeTest {

    private static final Logger LOG = LoggerFactory.getLogger(ZombieAspectTest.class);

    /**
     * If a zombie class is loaded we expect an {@link AssertionError} if this
     * class will be loaded. So we provocate it by calling a method of the
     * {@link ZombieClass}.
     * <p>
     * We expect the {@link ExceptionInInitializerError} because in the
     * ZombieAspect will cause a {@link RuntimeException} during the static
     * initialization of the {@link ZombieClass}.
     * </p>
     */
    @Test
    public void testZombieClass() {
        assertThrows(ExceptionInInitializerError.class, () -> {
            URI uri = ZombieClass.getRecipe();
            LOG.info("uri = {}", uri);
        });
    }

}

