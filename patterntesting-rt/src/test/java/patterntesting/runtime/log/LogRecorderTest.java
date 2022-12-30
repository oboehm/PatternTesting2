/*
 * Copyright (c) 2022 by Oli B.
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
 * (c)reated 29.12.22 by oboehm
 */
package patterntesting.runtime.log;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-Test fuer {@link LogRecorder} ...
 *
 * @author oboehm
 * @since 2.3 (29.12.22)
 */
class LogRecorderTest {

    private final LogRecorder recorder = new LogRecorder();

    @Test
    void info() {
        recorder.info("hello world", new RuntimeException("bumm"));
        assertEquals(1, recorder.getNumberOfRecords());
        assertEquals("hello world", recorder.getText());
    }

    @Test
    void debugArguments() {
        recorder.debug("hello {}{}","world", '!');
        assertEquals("hello world!", recorder.getText());
    }

}