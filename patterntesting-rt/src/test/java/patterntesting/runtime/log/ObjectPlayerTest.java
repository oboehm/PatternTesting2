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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.Test;

import patterntesting.runtime.junit.ObjectTester;
import patterntesting.runtime.mock.JoinPointMock;

/**
 * JUnit tests for {@link ObjectPlayer} class.
 *
 * @author oliver (boehm@javatux.de)
 * @since 1.3.1 (31.08.2013)
 */
public final class ObjectPlayerTest {

    private static final Logger LOG = LogManager.getLogger(ObjectPlayerTest.class);
    private static JoinPoint testJoinPoint = new JoinPointMock("test");

    /**
     * Here we store some joinpoints with return value and load them
     * afterwards.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testReplay() throws IOException {
        File file = File.createTempFile("test", ".rec");
        ObjectRecorder recorder = new ObjectRecorder(file);
        JoinPoint jp = save(recorder, "a", "b", "c");
        recorder.close();
        checkReplay(file, jp);
    }

    /**
     * The file from the previous test {@link #testReplay()} was compressed to
     * test if it can be reloaded again.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testReplayCompressed() throws IOException {
        File file = new File("src/test/resources/patterntesting/runtime/log/testreplay.rec.gz");
        checkReplay(file, testJoinPoint);
    }

    private static void checkReplay(final File file, final JoinPoint jp) throws IOException {
        ObjectPlayer player = new ObjectPlayer(file);
        assertEquals("a", player.getReturnValue(jp));
        assertEquals("b", player.getReturnValue(jp));
        assertEquals("c", player.getReturnValue(jp));
        LOG.info("{} was succesful replayed with {}.", file, player);
    }

    /**
     * Creates an joinpoint and save the given return values. This method is
     * public because otherwise the JoinPointMock has problem to construct a
     * valid {@link JoinPoint}.
     *
     * @param recorder the recorder
     * @param retValues the ret values
     * @return the join point
     */
    public static JoinPoint save(final ObjectRecorder recorder, final Object...retValues) {
        JoinPoint jp = new JoinPointMock("test");
        for (Object value : retValues) {
            recorder.log(jp, value);
        }
        return jp;
    }

    /**
     * Test method for {@link ObjectPlayer#equals(Object)}.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testEquals() throws IOException {
        File file = new File("src/test/resources/patterntesting/runtime/log/testreplay.rec.gz");
        ObjectPlayer one = new ObjectPlayer(file);
        ObjectPlayer anotherOne = new ObjectPlayer(file);
        ObjectTester.assertEquals(one, anotherOne);
    }

}

