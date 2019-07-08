/*
 * Copyright (c) 2008-2019 by Oliver Boehm
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
 * (c)reated 25.01.2009 by oliver (ob@oasd.de)
 */
package patterntesting.sample;

import org.junit.jupiter.api.Test;
import patterntesting.annotation.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The Class IdGeneratorTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 */
public class IdGeneratorTest {

    private IdGenerator idGenerator = IdGenerator.getInstance();

    /**
     * Test method for {@link IdGenerator#getNextId()}.
     */
    @Test
    public final void testGetNextId() {
        int startId = idGenerator.getNextId();
        getId();
        int endId = idGenerator.getNextId();
        assertEquals(startId+11, endId);
    }

    @RunParallel(10)
    @TestThread
    private void getId() {
        idGenerator.getNextId();
    }

}
