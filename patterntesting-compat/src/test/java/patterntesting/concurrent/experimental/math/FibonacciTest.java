package patterntesting.concurrent.experimental.math;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import patterntesting.concurrent.junit.ParallelProxyRunner;
import patterntesting.runtime.annotation.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests apparently the {@link Fibonacci} implementation. In reality the
 * {@link ParallelProxyRunner} for the {@link SpringJUnit4ClassRunner} is tested.
 *
 * @author reik, 11/15/11, extended 11/12/09 by oboehm
 */
@RunWith(ParallelProxyRunner.class)
@DelegateTo(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= "testApplicationContext.xml")
public class FibonacciTest {

    private static final Logger log = LogManager.getLogger(FibonacciTest.class);

    @Autowired
    private Fibonacci fibonacci;

    /**
     * Test number 0.
     */
    @Test
    public void testNumber0() {
        long n = this.fibonacci.calculate(0);
        assertEquals(0, n);
    }

    /**
     * Test numbers.
     */
    @Test
    public void testNumbers() {
        final Fibonacci fib = this.fibonacci;
        assertEquals(0, fib.calculate(0));
        assertEquals(1, fib.calculate(1));
        assertEquals(1, fib.calculate(2));
        assertEquals(2, fib.calculate(3));
        assertEquals(3, fib.calculate(4));
        assertEquals(5, fib.calculate(5));
        assertEquals(8, fib.calculate(6));
    }

    /**
     * Test number 7.
     */
    @Test
    public void testNumber7() {
        assertEquals(13, this.fibonacci.calculate(7));
    }

    /**
     * Test number 10.
     */
    @Test
    public void testNumber10() {
        assertEquals(55, this.fibonacci.calculate(10));
    }

    /**
     * Test number 25.
     */
    @Test
    @Ignore
    public void testNumber25() {
        assertEquals(75025, this.fibonacci.calculate(25));
    }

    /**
     * Test number 26.
     */
    @Test
    @IntegrationTest
    public void testNumber26() {
        assertEquals(121393, this.fibonacci.calculate(26));
    }

    /**
     * This is a broken method which should not be called for testing.
     */
    @Test
    @Broken("for testing")
    public void brokenMethod() {
        fail("should be never called because marked as @Broken!");
    }

    /**
     * Here we test the {@link RunTestOn} annotation. This test should be never
     * run because the given OS architecture does not exist.
     */
    @Test
    @RunTestOn(osName = "Mac", osArch = "fake")
    public void testRunTestOnAnnotation() {
        fail("should be never called because class is annotated by @RunTestOn");
    }

    /**
     * Here we test the {@link SkipTestOn} annotation. This test should be never
     * skipped because the given OS architecture does not exist.
     */
    @Test
    @SkipTestOn(osName = "Mac", osArch = "fake")
    public void testSkipTestOnAnnotation() {
        log.info("testSkipTestOnAnnotation() executed.");
    }

}
