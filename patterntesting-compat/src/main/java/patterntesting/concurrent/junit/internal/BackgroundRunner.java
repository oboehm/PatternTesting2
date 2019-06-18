/*
 * $Id: BackgroundRunner.java,v 1.7 2016/12/18 21:56:49 oboehm Exp $
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
 * (c)reated 30.12.2011 by oliver (ob@oasd.de)
 */

package patterntesting.concurrent.junit.internal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import patterntesting.runtime.junit.SmokeRunner;
import patterntesting.runtime.util.Environment;

/**
 * The Class BackgroundRunner will run the wrapped Runner in background,
 * record the results und plays them when they are needed.
 * But not if threads are not allowed, e.g. if the system property
 * {@link Environment#DISABLE_THREADS} is set. In this case the tests
 * are started normal.
 *
 * @author oliver
 * @since 1.2 (30.12.2011)
 */
public final class BackgroundRunner extends Runner {

    private static final Logger log = LogManager.getLogger(BackgroundRunner.class);
    private static int threadNumber = 1;
    private final Runner wrapped;
    private final RecordingRunNotifier recorder = new RecordingRunNotifier();
    private Thread thread;
    private long threadStart;

    /**
     * Instantiates a new background runner.
     *
     * @param runner the runner
     */
    public BackgroundRunner(final Runner runner) {
        this.wrapped = runner;
    }

    /**
     * Instantiates a new background runner.
     *
     * @param testClass the test class
     * @throws InitializationError the initialization error
     */
    public BackgroundRunner(final Class<?> testClass) throws InitializationError {
        this(new SmokeRunner(testClass));
    }

    /**
     * Gets the description.
     *
     * @return the description
     * @see Runner#getDescription()
     */
    @Override
    public Description getDescription() {
        return this.wrapped.getDescription();
    }

    /**
     * Replays the recorded events.
     *
     * @param notifier the notifier
     * @see Runner#run(RunNotifier)
     */
    @Override
    public void run(final RunNotifier notifier) {
        if (this.recorder.isEmpty()) {
            log.trace("Running {} direct.", this.wrapped);
            this.wrapped.run(notifier);
        } else {
            this.recorder.replay(notifier, this.thread);
            log.info("{} ({} ms)", this.getDescription(), System.currentTimeMillis() - threadStart);
        }
    }

    /**
     * Starts the wrapped Runner in Background and records the events.
     */
    public void startRunner() {
        if (Environment.areThreadsAllowed()) {
            Runnable work = new Runnable() {
                public void run() {
                    log.info("Starting {} in background...", wrapped.getDescription());
                    wrapped.run(recorder);
                }
            };
            this.threadStart = System.currentTimeMillis();
            this.thread = startThread(work);
        }
    }

    private static Thread startThread(final Runnable work) {
        Thread t = new Thread(work, "bg-" + Integer.toHexString(threadNumber++));
        t.start();
        log.trace("{} started.", t);
        return t;
    }

}

