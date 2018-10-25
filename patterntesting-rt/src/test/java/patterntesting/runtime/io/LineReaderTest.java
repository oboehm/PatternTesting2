/*
 * $Id: LineReaderTest.java,v 1.2 2016/12/18 20:19:39 oboehm Exp $
 *
 * Copyright (c) 2016 by Oliver Boehm
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
 * (c)reated 15.03.2016 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link LineReader} class.
 *
 * @author oliver
 */
public final class LineReaderTest {

    private static final Logger LOG = LogManager.getLogger(LineReaderTest.class);

    /**
     * Test method for {@link LineReader#read()}.
     *
     * @throws IOException I/O problems
     */
    @Test
    public void testRead() throws IOException {
        Reader reader = new StringReader("\nhello world\n");
        LineReader lineReader = new LineReader(reader);
        try {
            assertEquals('\n', lineReader.read());
        } finally {
            lineReader.close();
            LOG.info("{} was closed.", lineReader);
        }
    }

}

