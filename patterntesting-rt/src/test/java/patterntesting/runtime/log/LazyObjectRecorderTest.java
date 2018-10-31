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
 * (c)reated 01.09.2013 by Oli B. (boehm@javatux.de)
 */

package patterntesting.runtime.log;

import java.io.File;

import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.Test;

import patterntesting.runtime.junit.FileTester;
import patterntesting.runtime.mock.JoinPointMock;

/**
 * JUnit tests for {@link LazyObjectRecorder} class.
 *
 * @author oliver (boehm@javatux.de)
 * @since 1.3.1 (01.09.2013)
 */
public class LazyObjectRecorderTest extends ObjectRecorderTest {

    /**
     * Creates the recorder.
     *
     * @param file the file
     * @return the object recorder
     * @see ObjectRecorderTest#createRecorder(File)
     */
    @Override
    protected ObjectRecorder createRecorder(final File file) {
        return new LazyObjectRecorder(file);
    }

    /**
     * Tests the "laziness" of the implementation. I.e. it tests if two equals
     * return values are only stored once.
     */
    @Test
    public void testLaziness() {
        File f1 = createRecord("test-1.xml", "a");
        File f2 = createRecord("test-2.xml", "a", "a", "a");
        FileTester.assertContentEquals(f1, f2);
    }

    /**
     * Only for the last equals return values of joinpoint it is allowed to
     * store them once (otherwise the ObjectPlayer does not work correct). This
     * is tested here.
     */
    @Test
    public void testLazinessNotPossible() {
        File f3 = createFile("test-3.xml");
        ObjectRecorder recorder = new ObjectRecorder(f3);
        recordWith(recorder, "a", "a", "a", "b", "b", "c");
        File f4 = createRecord("test-4.xml", "a", "a", "a", "b", "b", "c");
        FileTester.assertContentEquals(f3, f4);
    }

    private static File createRecord(final String filename, final Object... returnValues) {
        File record = createFile(filename);
        LazyObjectRecorder recorder = new LazyObjectRecorder(record);
        recordWith(recorder, returnValues);
        return record;
    }

    /**
     * Record the return values with given recorder. This method is public
     * because otherwise the JoinPointMock has problem to construct a valid
     * {@link JoinPoint}.
     * <p>
     * NOTE: This method has a return value because otherwise the
     * {@link LazyObjectRecorder} would not record it (that's a correct
     * behaviour).
     * </p>
     *
     * @param recorder the recorder
     * @param returnValues the return values
     * @return the join point
     */
    public static JoinPoint recordWith(final ObjectRecorder recorder, final Object... returnValues) {
        JoinPoint jp = new JoinPointMock("lazy");
        for (Object value : returnValues) {
            recorder.log(jp, value);
        }
        recorder.close();
        return jp;
    }

}
