/*
 * $Id: AbstractSerializerTest.java,v 1.3 2016/12/18 20:19:39 oboehm Exp $
 *
 * Copyright (c) 2013 by Oliver Boehm
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
 * (c)reated 30.11.2013 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The Class AbstractSerializerTest.
 *
 * @author oliver
 * @since 1.4 (30.11.2013)
 */
public abstract class AbstractSerializerTest {

    private static final Logger log = LogManager.getLogger(AbstractSerializerTest.class);
    private final AbstractSerializer serializer = this.getSerializer();

    /**
     * Gets the serializer.
     *
     * @return the serializer
     */
    protected abstract AbstractSerializer getSerializer();

    /**
     * Tests the serialization and deserialization.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testSerialize() throws IOException {
        File testFile = File.createTempFile("test", ".ser");
        log.info("File \"{}\" is used to test (de)serialization.", testFile);
        Object hello = "Hello World!";
        try {
            this.serializer.save(hello, testFile);
            Object deserialized = this.serializer.load(testFile);
            assertEquals(hello, deserialized);
        } finally {
            boolean deleted = testFile.delete();
            log.info("File \"{}\" was {} deleted.", testFile, deleted ? "successful" : "not");
        }
    }

}

