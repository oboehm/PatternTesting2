/*
 * $Id: RecordingRunNotifier.java,v 1.19 2016/12/18 21:56:49 oboehm Exp $
 *
 * Copyright (c) 2012 by Oliver Boehm
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
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import patterntesting.runtime.util.Converter;
import patterntesting.runtime.util.ThreadUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeoutException;

/**
 * This is a RunNotifier which records the single fire events. These events
 * can be replayed later.
 *
 * @author oliver
 * @since 1.2 (18.12.2011)
 */
public final class RecordingRunNotifier extends RunNotifier {

    private static final Logger log = LoggerFactory.getLogger(RecordingRunNotifier.class);
    private static final Object NO_ARG = createNoArgDescription();

    private static Object createNoArgDescription() {
        try {
            return Description.createTestDescription(RecordingRunNotifier.class, "NO_ARG");
        } catch (RuntimeException ex) {
            log.error("Cannot create test description", ex);
            return "NO_ARG";
        }
    }

    /**
     * The Enum FireEvent. It is protected because it is used for testing.
     */
    protected enum FireEvent {
        /** Testrun started. */
        TEST_RUN_STARTED,
        /** Testrun finished. */
        TEST_RUN_FINISHED,
        /** Test started. */
        TEST_STARTED,
        /** Test failure. */
        TEST_FAILURE,
        /** Test assumption failed. */
        TEST_ASSUMPTION_FAILED,
        /** Test ignored. */
        TEST_IGNORED,
        /** Test finished. */
        TEST_FINISHED,
        /** Please stop. */
        PLEASE_STOP;
    }

    /**
     * Class for the recorded events and arguments.
     * The class is protected because it is used for testing.
     */
    protected static class FireRecord {
        /**
         * Instantiates a new fire record.
         *
         * @param event the event
         * @param arg the arg
         */
        FireRecord(final FireEvent event, final Object arg) {
            this.event = event;
            this.arg = arg;
            this.time = System.currentTimeMillis();
        }
        /** The stored event. */
        final FireEvent event;
        /** The stored argument. */
        private final Object arg;
        /** The recorded start time. */
        final long time;
        /**
         * Gets the description.
         *
         * @return the description
         */
        Description getDescription() {
            if (this.arg instanceof Failure) {
                return ((Failure) this.arg).getDescription();
            } else if (this.arg instanceof Description) {
                return (Description) this.arg;
            } else {
                throw new IllegalStateException("arg is a " + arg.getClass().getName()
                    + "; expected a Failure or Description. Arg: " + arg);
            }
        }
        /**
         * Gets the result.
         *
         * @return the result
         */
        Result getResult() {
            return (Result) this.arg;
        }
        /**
         * Gets the failure.
         *
         * @return the failure
         */
        Failure getFailure() {
            return (Failure) this.arg;
        }

        /**
         * To string.
         *
         * @return the string
         * @see Object#toString()
         */
        @Override
        public String toString() {
            return this.arg + " " + this.event;
        }
    }

    /** The recorded events and arguments. */
    private final Queue<FireRecord> startRecords = new ConcurrentLinkedQueue<FireRecord>();
    private final Map<Description, List<FireRecord>> endRecords = new ConcurrentHashMap<Description, List<FireRecord>>();
    private FireRecord firstRecord;
    private FireRecord lastRecord;

    /**
     * Fire test run started.
     *
     * @param description the description
     * @see RunNotifier#fireTestRunStarted(Description)
     */
    @Override
    public void fireTestRunStarted(final Description description) {
        this.recordStart(FireEvent.TEST_RUN_STARTED, description);
    }

    /**
     * Fire test run finished.
     *
     * @param result the result
     * @see RunNotifier#fireTestRunFinished(Result)
     */
    @Override
    public void fireTestRunFinished(final Result result) {
        this.recordEnd(FireEvent.TEST_RUN_FINISHED, result);
    }

    /**
     * Fire test started.
     *
     * @param description the description
     * @throws StoppedByUserException the stopped by user exception
     * @see RunNotifier#fireTestStarted(Description)
     */
    @Override
    public void fireTestStarted(final Description description) throws StoppedByUserException {
        this.recordStart(FireEvent.TEST_STARTED, description);
    }

    /**
     * Fire test failure.
     *
     * @param failure the failure
     * @see RunNotifier#fireTestFailure(Failure)
     */
    @Override
    public void fireTestFailure(final Failure failure) {
        this.recordEnd(FireEvent.TEST_FAILURE, failure);
    }

    /**
     * Fire test assumption failed.
     *
     * @param failure the failure
     * @see RunNotifier#fireTestAssumptionFailed(Failure)
     */
    @Override
    public void fireTestAssumptionFailed(final Failure failure) {
        this.recordEnd(FireEvent.TEST_ASSUMPTION_FAILED, failure);
    }

    /**
     * Fire test ignored.
     *
     * @param description the description
     * @see RunNotifier#fireTestIgnored(Description)
     */
    @Override
    public void fireTestIgnored(final Description description) {
        this.recordStart(FireEvent.TEST_IGNORED, description);
    }

    /**
     * Fire test finished.
     *
     * @param description the description
     * @see RunNotifier#fireTestFinished(Description)
     */
    @Override
    public void fireTestFinished(final Description description) {
        this.recordEnd(FireEvent.TEST_FINISHED, description);
    }

    /**
     * Please stop.
     *
     * @see RunNotifier#pleaseStop()
     */
    @Override
    public void pleaseStop() {
        this.recordEnd(FireEvent.PLEASE_STOP, NO_ARG);
    }

    /**
     * Record a start event.
     * <p>
     * NOTE: Also an ignore event is considered as start event.
     * </p>
     *
     * @param event the event
     * @param arg the argument to be stored for the given event.
     */
    private void recordStart(final FireEvent event, final Object arg) {
        FireRecord frec = new FireRecord(event, arg);
        log.trace("RECORDING: {}", frec);
        this.startRecords.add(frec);
        if (this.firstRecord == null) {
            this.firstRecord = frec;
        }
        this.endRecords.put(frec.getDescription(), new ArrayList<FireRecord>());
        this.lastRecord = frec;
    }

    private void recordEnd(final FireEvent event, final Object arg) {
        FireRecord frec = new FireRecord(event, arg);
        log.trace("RECORDING: {}", frec);

        List<FireRecord> endRecordList
            = this.endRecords.get(frec.getDescription());
        endRecordList.add(frec);
        this.lastRecord = frec;
    }

    /**
     * Checks if something was recorded.
     *
     * @return true, if is empty
     */
    public boolean isEmpty() {
        return this.startRecords.isEmpty();
    }

    /**
     * Replays the recorded events for the given notifier.
     * But not the TEST_STARTED event - this must be sent by
     * the calling method.
     * <p>
     * We have one problem with the used queue here: if the queue is empty
     * we do not know if there are some tests are missing - perhaps some tests
     * are not yet finised.
     * </p>
     * <p>
     * We keep the start and end events separate. The end events are stored
     * in a hash map. Normally we would remove the replayed end event but
     * this does not work - probably the synchronisation does not work perfect.
     * </p>
     *
     * @param notifier the notifier
     */
    public void replay(final RunNotifier notifier) {
        while (!this.startRecords.isEmpty()) {
            FireRecord frec = this.startRecords.poll();
            replay(notifier, Arrays.asList(frec));
            if (frec.event == FireEvent.TEST_STARTED) {
                Description descr = frec.getDescription();
                try {
                    List<FireRecord> endRecordList = waitForEndRecordList(descr, 20000);
                    replay(notifier, endRecordList);
                    //this.endRecords.remove(descr);
                } catch (TimeoutException ex) {
                    log.warn("Giving up to wait longer - will continue with next event!", ex);
                }
            }
        }
    }

    private List<FireRecord> waitForEndRecordList(final Description descr, final long waitTimeInMillis) throws TimeoutException {
        long endTime = System.currentTimeMillis() + waitTimeInMillis;
        while (System.currentTimeMillis() < endTime) {
            List<FireRecord> endRecordList = this.endRecords.get(descr);
            if ((endRecordList != null) && !endRecordList.isEmpty()) {
                return endRecordList;
            }
            ThreadUtil.sleep();
        }
        throw new TimeoutException("waited " + Converter.getTimeAsString(waitTimeInMillis)
                        + " in vain for end of " + descr.getDisplayName());
    }

    /**
     * Replays the record events till the given thread has ended.
     *
     * @param notifier the notifier
     * @param waitFor the thread to wait for
     */
    @SuppressWarnings("squid:S2142")
    public void replay(final RunNotifier notifier, final Thread waitFor) {
        this.replay(notifier);
        try {
            while (waitFor.getState() != Thread.State.TERMINATED) {
                waitFor.join(1);
                log.trace("Waited for {} to replay {}.", waitFor, notifier);
                replay(notifier);
            }
        } catch (InterruptedException ie) {
            log.info("{} was interrupted", waitFor, ie);
            this.replay(notifier, Arrays.asList(new FireRecord(FireEvent.PLEASE_STOP, NO_ARG)));
        }
    }

    private void replay(final RunNotifier notifier, final List<FireRecord> frecList) {
        for (FireRecord frec : frecList)
            replayRecord(notifier, frec);
    }

    private void replayRecord(final RunNotifier notifier, final FireRecord frec) {
        log.trace("REPLAYING: {}", frec);
        switch (frec.event) {
        case TEST_RUN_STARTED:
            notifier.fireTestRunStarted(frec.getDescription());
            break;
        case TEST_RUN_FINISHED:
            notifier.fireTestRunFinished(frec.getResult());
            break;
        case TEST_STARTED:
            notifier.fireTestStarted(frec.getDescription());
            break;
        case TEST_FAILURE:
            notifier.fireTestFailure(frec.getFailure());
            break;
        case TEST_ASSUMPTION_FAILED:
            notifier.fireTestAssumptionFailed(frec.getFailure());
            break;
        case TEST_IGNORED:
            notifier.fireTestIgnored(frec.getDescription());
            break;
        case TEST_FINISHED:
            notifier.fireTestFinished(frec.getDescription());
            break;
        case PLEASE_STOP:
            notifier.pleaseStop();
            break;
        default:
            log.warn("Unknown event \"{}\" recorded.", frec.event);
            break;
        }
    }

    /**
     * Looks if a failure event was recorded.
     *
     * @return true, if TEST_FAILURE or TEST_ASSUMPTION_FAILED event was recorded
     */
    @SuppressWarnings("incomplete-switch")
    public boolean failureRecorded() {
        for (List<FireRecord> frecList : this.endRecords.values()) {
            for (FireRecord frec : frecList) {
                switch (frec.event) {
                case TEST_FAILURE:
                case TEST_ASSUMPTION_FAILED:
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets the time difference between first and last recorded event.
     *
     * @return the time in milliseconds
     */
    public long getTimeInMillis() {
        if ((this.firstRecord == null) || (this.lastRecord == null)) {
            throw new IllegalStateException("nothing recorded");
        }
        return this.lastRecord.time - this.firstRecord.time;
    }

    /**
     * A toString() implementation for better logging and easier debugging.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " with " + this.startRecords.size() + "/"
                + this.endRecords.size() + " start/end record(s)";
    }

}

