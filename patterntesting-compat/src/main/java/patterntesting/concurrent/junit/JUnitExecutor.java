package patterntesting.concurrent.junit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patterntesting.annotation.check.runtime.MayReturnNull;
import patterntesting.runtime.util.Environment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * This class is responsible for starting a JUnit test method. This means
 * that it must also be able to find the setup method which is called before
 * the JUnit test.
 *
 * @author oliver
 * @since 18.12.2009
 */
public abstract class JUnitExecutor {

    private static final Logger LOG = LogManager.getLogger(JUnitExecutor.class);
    private static final Logger RUNLOG = LogManager.getLogger(ParallelRunner.class);
    private static final Set<Class<?>> SETUP_CLASSES = new HashSet<Class<?>>();
    private final Class<?> testClass;
    private boolean runParallel;

    /** The setup before class method. */
    protected Method setupBeforeClassMethod;

    /** The setup method. */
    protected Method setupMethod;

    /** The results. */
    protected final Map<String, Result> results = new HashMap<String, Result>();

    /** The teardown method. */
    protected Method teardownMethod;
    private Executor executor;

    /**
     * The Class Result.
     */
    protected static class Result {

        /**
         * Instantiates a new result.
         *
         * @param method the method
         */
        public Result(final Method method) {
            this.method = method;
        }

        /** the called method. */
        public Method method;

        /** the (future) result. */
        public FutureTask<Throwable> future;
    }

    /**
     * Instantiates a new j unit executor.
     *
     * @param clazz the class with the test methods
     */
    public JUnitExecutor(final Class<?> clazz) {
        this.testClass = clazz;
        this.init();
    }

    /**
     * This method clears the recorded results.
     *
     * @since 1.0
     */
    public final void reset() {
        this.init();
        this.recordResults();
    }

    /**
     * Record results.
     */
    protected abstract void recordResults();

    private void init() {
        this.runParallel = this.initRunParallel();
        if (this.runParallel) {
            executor = Executors.newCachedThreadPool();
        } else if (LOG.isTraceEnabled()) {
            LOG.trace("RunTestsParallel is disabled for " + this.testClass);
        }
    }

    /**
     * In Google's App Engine environment (and maybe in other JEE environments)
     * multithreading is not allowed. So we can't start parallel tests in these
     * environments.
     *
     * @return true if RunTestsParallel is enabled
     */
    private boolean initRunParallel() {
        if (!Environment.areThreadsAllowed()) {
            return false;
        }
        if (Environment.isPropertyEnabled(Environment.RUN_TESTS_PARALLEL)) {
            return true;
        }
        return false;
    }

    /**
     * The default is "true". But you can disable the parallel runs of the test
     * method via the system property "patterntesting.runTestsParallel" or via the attribute
     * "enabled" in the RunTestsParallel annotation.
     *
     * @return true (default)
     */
    public final boolean isRunParallelEnabled() {
        return this.runParallel;
    }

    /**
     * Gets the test class.
     *
     * @return the test class
     */
    protected final Class<?> getTestClass() {
        return this.testClass;
    }

    /**
     * Record test methods.
     */
    protected final void recordTestMethods() {
        if (this.isRunParallelEnabled()) {
            for(Result result : results.values()) {
                triggerTest(result);
            }
        }
    }

    /**
     * Here we trigger the test only and store the result (a Throwable if the
     * JUnit test fails) in a "Future" object.
     *
     * @param result this contains the JUnit method
     */
    @MayReturnNull
    private void triggerTest(final Result result) {
        Callable<Throwable> callable = new Callable<Throwable>() {
            public Throwable call() throws Exception {
                try {
                    invokeTest(result.method);
                    return null;
                } catch (Throwable t) {
                    return t;
                }
            }
        };
        result.future = new FutureTask<Throwable>(callable);
        executor.execute(result.future);
    }

    /**
     * Replays the given test i.e. it looks for the test result and throws
     * an exception if the found test has thrown one.
     *
     * @param methodName
     *            the method name
     * @param obj
     *            the object
     */
    @SuppressWarnings("squid:S2142")
    public final void playTest(final String methodName, final Object obj) {
        Result result = this.results.get(methodName);
        if ((result == null) || (result.future == null)) {
            LOG.trace("starting now " + methodName + "...");
            invokeTest(methodName);
            return;
        }
        try {
            Throwable t = result.future.get();
            if (t != null) {
                Thrower.provoke(t);
            }
        } catch (InterruptedException e) {
            LOG.debug("Getting result from " + result.future + " was interrupted:", e);
            Thrower.provoke(e);
        } catch (ExecutionException e) {
            LOG.debug("Cannot execute " + result.future + ":", e);
            Thrower.provoke(e);
        }
    }

    private void invokeTest(final String methodName) {
        try {
            Method method = this.testClass.getMethod(methodName);
            invokeTest(method);
        } catch (SecurityException e) {
            throw new IllegalArgumentException(this.testClass + "."
                    + methodName + " can't be invoked", e);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(methodName + " not found in "
                    + this.testClass, e);
        }
    }

    /**
     * Calls the given test method. But before the setup method of the JUnit
     * test class is called (and afterwards the teardown method).
     * <p>
     * For each test a new instance is created. Why? To be sure that the tests
     * can run in parallel.
     * </p>
     *
     * @param methodName the method name
     */
    private void invokeTest(final Method method) {
        Throwable thrown = null;
        long[] t = new long[5];
        t[0] = System.currentTimeMillis();
        try {
            Object obj = this.testClass.newInstance();
            t[1] = System.currentTimeMillis();
            try {
                this.callSetup(obj);
                t[2] = System.currentTimeMillis();
                this.call(method, obj);
            } finally {
                t[3] = System.currentTimeMillis();
                this.callTeardown(obj);
                t[4] = System.currentTimeMillis();
            }
        } catch (InstantiationException e) {
            t[1] = System.currentTimeMillis();
            thrown = e;
            throw new AssertionError("can't instantiate " + this.testClass + " ("
                    + e + ")");
        } catch (IllegalAccessException e) {
            t[1] = System.currentTimeMillis();
            thrown = e;
            throw new AssertionError("can't access ctor of " + this.testClass
                    + " (" + e + ")");
        } catch (AssertionError e) {
            t[4] = System.currentTimeMillis();
            thrown = e;
            throw e;
        } finally {
            if (t[2] == 0) {
                t[2] = t[1];
            }
            if (t[3] == 0) {
                t[3] = t[2];
            }
            logMethod(method, t, thrown);
        }
    }

    /**
     * If the JUnit test class have a setupBeforeClass method we will call it
     * here.
     */
    protected void callSetupBeforeClass() {
        if (this.setupBeforeClassMethod != null) {
            synchronized(SETUP_CLASSES) {
                if (SETUP_CLASSES.contains(this.testClass)) {
                    if (LOG.isTraceEnabled()) {
                        LOG.trace(this.setupBeforeClassMethod
                                + " skipped (already called");
                    }
                } else {
                    SETUP_CLASSES.add(this.testClass);
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("calling " + this.setupBeforeClassMethod + "...");
                    }
                    String result = "unknown";
                    long t0 = System.currentTimeMillis();
                    try {
                        call(this.setupBeforeClassMethod);
                        result = "SUCCESS";
                    } catch (AssertionError e) {
                        result = "FAILURE";
                        throw e;
                    } finally {
                        long t = System.currentTimeMillis() - t0;
                        logMethod(result, this.setupBeforeClassMethod, t);
                    }
                }
            }
        }
    }

    /**
     * If the JUnit test class have a setup method we will call it here.
     *
     * @param obj the object for the method invoke
     */
    private void callSetup(final Object obj) {
        if (this.setupMethod != null) {
            call(this.setupMethod, obj);
        }
    }

    /**
     * If the JUnit test class have a teardown method we will call it here.
     *
     * @param obj the object for the method invoke
     */
    private void callTeardown(final Object obj) {
        if (this.teardownMethod != null) {
            call(this.teardownMethod, obj);
        }
    }

    /**
     * Calls the (static) method but throws an AssertionError if an exception
     * happens.
     *
     * @param method method to be called
     * @param obj the object for the method
     */
    private void call(final Method method) {
        try {
            method.invoke(null);
        } catch (IllegalAccessException e) {
            LOG.debug("Cannot access " + method + ":", e);
            throwAssertionErrorFor(this.teardownMethod, e);
        } catch (InvocationTargetException e) {
            LOG.debug("Cannot invoke " + method + ":", e);
            throwAssertionErrorFor(this.teardownMethod, e);
        }
    }

    /**
     * Calls the method but throws an AssertionError if an exception happens.
     *
     * @param method method to be called
     * @param obj the object for the method
     */
    private void call(final Method method, final Object obj) {
        try {
            method.setAccessible(true);
            method.invoke(obj);
        } catch (IllegalAccessException e) {
            LOG.debug("Cannot access " + method + ":", e);
            throwAssertionErrorFor(method, e);
        } catch (InvocationTargetException e) {
            LOG.debug("Cannot invoke " + method + " with " + obj + ":", e);
            Throwable t = e.getTargetException();
            if (t != null) {
                Thrower.provoke(t);
            } else {
                throwAssertionErrorFor(method, e);
            }
        }
    }

    private void throwAssertionErrorFor(final Method method, final Throwable t) {
        String detailedMessage = "invoke of " + testClass.getSimpleName() + "."
                + method + "() failed\n" + t;
        throw new AssertionError(detailedMessage);
    }

    /**
     * To string.
     *
     * @return the simple class name and the name of the test class
     */
    @Override
    public final String toString() {
        return this.getClass().getSimpleName() + " for "
                + this.testClass.getSimpleName() + " " + this.results.size()
                + " test methods";
    }

    private static void logMethod(final String result, final Method method, final long time) {
        if (RUNLOG.isInfoEnabled()) {
            LOG.info(result + ": " + method.getDeclaringClass().getName()
                    + "." + method.getName() + " (" + time + " ms)");
        }
    }

    /**
     * Logs the test method with the different times for setup(), testXxx()
     * and tearDown() method. And if the constructor call needs to long this
     * time is also logged.
     *
     * @param method
     *            the called method
     * @param t
     *            t[0]: start time
     *            t[1]: time after test class was instantiated
     *            t[2]: time after setup method was called
     *            t[3]: time after test method was called
     *            t[4]: time after tear down method was called
     * @param thrown
     *            the thrown exception (can be null)
     */
    private void logMethod(final Method method, final long[] t, final Throwable thrown) {
        if (RUNLOG.isInfoEnabled()) {
            String classname = method.getDeclaringClass().getName();
            long tInstantiated = t[1] - t[0];
            long tTestTotal = t[4] - t[1];
            if (tInstantiated > tTestTotal) {
                RUNLOG.info(classname + ".<init> (" + tInstantiated + "ms)");
            }
            StringBuffer msg = new StringBuffer(classname + "." + method.getName() + " (");
            if (this.setupMethod == null) {
                msg.append("-/");
            } else {
                msg.append(t[2] - t[1]);
                msg.append("/");
            }
            msg.append(t[3] - t[2]);
            if (this.teardownMethod == null) {
                msg.append("/- ms)");
            } else {
                msg.append("/");
                msg.append(t[4] - t[3]);
                msg.append(" ms)");
            }
            if (thrown != null) {
                msg.append(" - ");
                msg.append(thrown.getClass().getSimpleName());
            }
            RUNLOG.info("{}", msg);
        }
    }

    /**
     * The trick here is to use the constructor to throw any desired exception.
     * So you can throw any exception without the need to have it as throws
     * clause.
     *
     * @author <a href="boehm@javatux.de">oliver</a>
     */
    private static class Thrower {
        private static Throwable throwable;

        private Thrower() throws Throwable {
            throw throwable;
        }

        /**
         * Provoke an exception.
         *
         * @param t the Throwable which should be used as provoked exception.
         */
        public static void provoke(final Throwable t) {
            throwable = t;
            try {
                Thrower.class.newInstance();
            } catch (InstantiationException unexpected) {
                LOG.info("Cannot instantiate Thrower class:", unexpected);
            } catch (IllegalAccessException unexpected) {
                LOG.info("Cannot access Thrower constructor:", unexpected);
            }
        }
    }

}
