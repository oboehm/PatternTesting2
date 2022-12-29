/*
 * Copyright (c) 2012-2020 by Oliver Boehm
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
 * (c)reated 18.12.2011 by oliver (ob@oasd.de)
 */

package patterntesting.concurrent.junit.internal;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import patterntesting.annotation.concurrent.RunBackground;
import patterntesting.concurrent.RunBackgroundAspect;
import patterntesting.concurrent.junit.internal.RecordingRunNotifier.FireEvent;
import patterntesting.concurrent.junit.internal.RecordingRunNotifier.FireRecord;
import patterntesting.runtime.util.ThreadUtil;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * JUnit test for {@link RecordingRunNotifier} class.
 *
 * @author oliver
 * @since 1.2 (18.12.2011)
 */
public final class RecordingRunNotifierTest {

    private static final Logger log = LoggerFactory.getLogger(RecordingRunNotifierTest.class);
    private static int counter;
    private final RecordingRunNotifier notifier = new RecordingRunNotifier();

    /**
     * Test method for {@link RecordingRunNotifier#fireTestRunStarted(Description)}.
     * Here we test if the TEST_RUN_STARTED event is the first event in the
     * replayed events.
     */
    @Test
    public void testFireTestRunStartedDescription() {
        Description description = createDescription("testFireTestRunStartedDescription");
        notifier.fireTestRunStarted(description);
        notifier.fireTestStarted(description);
        notifier.fireTestFinished(description);
        checkReplayRange(FireEvent.TEST_RUN_STARTED, FireEvent.TEST_STARTED, FireEvent.TEST_FINISHED);
    }

    private void checkReplayRange(final FireEvent...events) {
        CountRunNotifier countRunNotifier = new CountRunNotifier();
        notifier.replay(countRunNotifier);
        List<FireRecord> fired = countRunNotifier.getFired();
        for (int i = 0; i < events.length; i++) {
            FireRecord frec = fired.get(i);
            assertEquals("wrong " + (i+1) + ". event", events[i], frec.event);
        }
    }

    /**
     * Test method for {@link RecordingRunNotifier#failureRecorded()}.
     */
    @Test
    public void testFailureRecorded() {
        Description description = createDescription("testFailureRecorded");
        notifier.fireTestStarted(description);
        notifier.fireTestFailure(new Failure(description, new RuntimeException("bumm")));
        checkReplayRange(FireEvent.TEST_STARTED, FireEvent.TEST_FAILURE);
    }

    /**
     * Test method for {@link RecordingRunNotifier#getTimeInMillis()}.
     */
    @Test
    public void testGetTimeInMillis() {
        long time = System.currentTimeMillis();
        recordStartStop();
        time = System.currentTimeMillis() - time;
        long recordedTime = notifier.getTimeInMillis();
        assertTrue(recordedTime + " <= 0", recordedTime > 0);
        assertTrue(recordedTime + " > " + time, recordedTime <= time);
    }

    /**
     * Test for reproduction of the ConcurrentModificationException as
     * reported in issue 3541705. The trick here is that we record some
     * events in the background. Before these recording is finished (remember
     * it's started in the background!) we replay the events which are still
     * recorded. With this strategy we were able to reproduce the
     * ConcurrentModificationException.
     * <p>
     * The next step is to fix it.
     * </p>
     *
     * @see <a href="http://sourceforge.net/tracker/?func=detail&aid=3541705&group_id=48833&atid=454317">issue 3541705</a>
     */
    @Test
    public void test3541705() {
        for (int i = 0; i < 2; i++) {
            recordEventInBackground();
        }
        notifier.replay(new RunNotifier());
    }

    /**
     * Sometimes some recoreded events are lost because they are replayed in
     * parallel why they are recorded. This seems to be a problem with the
     * synchronisation or with the used list for the stored events.
     */
    @Test
    public void testRecordedEvents() {
        int n = 3;
        CountRunNotifier countRunNotifier = new CountRunNotifier();
        for (int i = 0; i < n; i++) {
            recordEventInBackground();
        }
        while (RunBackgroundAspect.getActiveJobs().size() > 0) {
            log.info("Waiting for the end of {} background jobs...",
                    RunBackgroundAspect.getActiveJobs().size());
            ThreadUtil.sleep();
        }
        log.info("Replay of {} started.", notifier);
        notifier.replay(countRunNotifier);
        assertEquals(2 * n, countRunNotifier.getNumberOfEvents());
    }

    @RunBackground
    private void recordEventInBackground() {
        this.recordStartStop();
    }

    private void recordStartStop() {
        Description desc = createDescription("test-" + getCounter());
        notifier.fireTestStarted(desc);
        ThreadUtil.sleep();
        notifier.fireTestFinished(desc);
    }

    synchronized static private int getCounter() {
        return ++counter;
    }

    /**
     * Test the correct range of the replayed events.
     * The events from different threads may be recorded in any order. But if
     * it is replayed after each start event the recorded stop event should be
     * replayed. Otherwise the original {@link RunNotifier} may get confused
     * (and e.g. a test in the Eclipse UI may not be marked as "ended").
     */
    @Test
    public void testRangeOfRecordedEvents() {
        Description[] descriptions = {
                createDescription("testRangeOfRecordedEvents-1"),
                createDescription("testRangeOfRecordedEvents-2"),
                createDescription("testRangeOfRecordedEvents-3")
        };
        recordStartStops(descriptions);
        checkRecordedStartStops(descriptions);
    }

    private void recordStartStops(final Description[] descriptions) {
        for (Description description : descriptions) {
            notifier.fireTestStarted(description);
        }
        Description ignoreDescription = createDescription("ignore");
        notifier.fireTestIgnored(ignoreDescription);
        for (int i = descriptions.length - 1; i >= 0; i--) {
            notifier.fireTestFinished(descriptions[i]);
        }
    }

    private void checkRecordedStartStops(final Description[] descriptions) {
        CountRunNotifier testNotifier = new CountRunNotifier();
        notifier.replay(testNotifier);
        List<FireRecord> fired = testNotifier.getFired();
        for (int i = 0; i < 2 * descriptions.length; i += 2) {
            assertEquals("element " + i + " and " + (i + 1) + " should be the same", fired.get(i).getDescription(),
                    fired.get(i + 1).getDescription());
       }
    }

    private static Description createDescription(final String name) {
        return Description.createTestDescription(RecordingRunNotifierTest.class, name);
    }

    /**
     * We don't want the default implementation as result for the
     * {@link RecordingRunNotifier#toString()} method.
     */
    @Test
    public void testToString() {
        String s = notifier.toString();
        assertFalse("looks like default implementation: " + s, s.contains("@"));
        log.info("s = \"{}\"", s);
    }

    //----- CountRunNotifier --------------------------------------------------

    /**
     * The Class CountRunNotifier counts the fired events.
     * It can be (and is) used for testing.
     */
    private class CountRunNotifier extends RunNotifier {

        List<FireRecord> fired = new ArrayList<>();

        @Override
        public void fireTestRunStarted(final Description descr) {
            fired.add(new FireRecord(FireEvent.TEST_RUN_STARTED, descr));
        }

        @Override
        public void fireTestRunFinished(final Result result) {
            throw new UnsupportedOperationException("fireTestRunFinished(" + result + ")");
        }

        @Override
        public void fireTestStarted(final Description descr) throws StoppedByUserException {
            fired.add(new FireRecord(FireEvent.TEST_STARTED, descr));
        }

        @Override
        public void fireTestFailure(final Failure failure) {
            fired.add(new FireRecord(FireEvent.TEST_FAILURE, failure));
        }

        @Override
        public void fireTestAssumptionFailed(final Failure failure) {
            fired.add(new FireRecord(FireEvent.TEST_ASSUMPTION_FAILED, failure));
        }

        @Override
        public void fireTestIgnored(final Description descr) {
            fired.add(new FireRecord(FireEvent.TEST_IGNORED, descr));
        }

        @Override
        public void fireTestFinished(final Description descr) {
            fired.add(new FireRecord(FireEvent.TEST_FINISHED, descr));
        }

        public int getNumberOfEvents() {
            return this.fired.size();
        }

        public List<FireRecord> getFired() {
            return this.fired;
        }

    }

}

