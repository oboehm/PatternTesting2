/*
 * Copyright (c) 2013 by Oli B.
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
 * (c)reated 31.08.2013 by Oli B. (boehm@javatux.de)
 */

package patterntesting.runtime.log;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.Test;

import patterntesting.runtime.mock.JoinPointMock;

/**
 * JUnit tests for {@link ObjectRecorder} class.
 *
 * @author oliver (boehm@javatux.de)
 * @since 1.3.1 (31.08.2013)
 */
public class ObjectRecorderTest {

    private static final Logger log = LogManager.getLogger(ObjectRecorderTest.class);

    /**
     * We record here some joinpoints with its return value to see if the
     * record file is generated and looks ok.
     */
    @Test
    public void testRecorder() {
        File record = createFile("test-records.xml");
        ObjectRecorder recorder = createRecorder(record);
        recorder.log(new JoinPointMock("one"), "a");
        recorder.log(new JoinPointMock("one"), "b");
        recorder.log(new JoinPointMock("two"), "c");
        recorder.log(new JoinPointMock("two"), "d");
        recorder.log(new JoinPointMock("two"), "e");
        recorder.close();
        assertTrue(record + " does not exist", record.exists());
    }

    /**
     * Creates a file for testing in the target directory. If necessary
     * the file will be deleted first.
     *
     * @param filename the filename
     * @return the file
     */
    protected static File createFile(final String filename) {
        File file = new File("target", filename);
        if (file.delete()) {
            log.info("{} is deleted.", file);
        }
        return file;
    }

    /**
     * Creates the recorder. This method can (and should) be overwritten by
     * subclasses to return the tested recorder.
     *
     * @param file the file
     * @return the object recorder
     */
    protected ObjectRecorder createRecorder(final File file) {
        return new ObjectRecorder(file);
    }

    // ------------------------------------------------------------------------


}

