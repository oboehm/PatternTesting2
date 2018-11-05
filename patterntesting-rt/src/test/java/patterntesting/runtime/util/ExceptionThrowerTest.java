/*
 * $Id: ExceptionThrowerTest.java,v 1.6 2016/10/24 20:22:07 oboehm Exp $
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
 * (c)reated 30.01.2010 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The Class ExceptionThrowerTest.
 *
 * @author oliver
 * @since 1.0 (30.01.2010)
 */
public final class ExceptionThrowerTest {

    /**
     * Test method for {@link ExceptionThrower#provoke(Class)}.
     */
    @Test
    public void testProvoke() {
        assertThrows(RuntimeException.class, () -> ExceptionThrower.provoke(RuntimeException.class));
    }

    /**
     * Test create.
     *
     * @throws ReflectiveOperationException the reflective operation exception
     */
    @Test
    public void testCreate() throws ReflectiveOperationException {
        Throwable t = ExceptionThrower.create(InterruptedException.class);
        assertTrue(t instanceof InterruptedException, t + ": wrong instance");
    }

}

