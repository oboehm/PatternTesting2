package patterntesting.runtime.experimental.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import patterntesting.runtime.annotation.*;
import patterntesting.runtime.junit.ProxyRunner;

/**
 * Tests apparently the {@link Fibonacci} implementation. In reality the
 * {@link ProxyRunner} for the {@link SpringJUnit4ClassRunner} is tested.
 *
 * @author reik, 11/15/11
 */
@RunWith(ProxyRunner.class)
@DelegateTo(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= "testApplicationContext.xml")
@SkipTestOn(javaVersion="1.6.*")
public class FibonacciTest {

    private static final Logger log = LogManager.getLogger(FibonacciTest.class);

    @Autowired
    private Fibonacci fibonacci;

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
     * Test number 0.
     */
    @Test
    public void testNumber0() {
        assertEquals(0, this.fibonacci.calculate(0));
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
     * Test number 25. Uncomment the {@link Ignore} annotation if you want to
     * see if this test is really ignored.
     */
    @Test
    //@Ignore
    public void testNumber25() {
        assertEquals(75025, this.fibonacci.calculate(25));
    }

    /**
     * This is a broken method which should not be called for testing.
     */
    @Test
    @Broken(why = "for testing", hide = true)
    public void brokenMethod() {
        fail("should be never called because marked as @Broken!");
    }

    /**
     * Here we test the {@link RunTestOn} annotation. This test should be never
     * run because the given OS architecture does not exist.
     */
    @Test
    @RunTestOn(osName = "Mac", osArch = "fake", hide = true)
    public void testRunTestOnAnnotation() {
        fail("should be never called because class is annotated by @RunTestOn");
    }

    /**
     * Here we test the {@link RunTestOn} annotation. This test should be
     * run on Java 8.
     */
    @Test
    @RunTestOn(javaVendor = "1.8.*", hide = true)
    public void testRunTestOnJava8() {
        log.info("testRunTestOnJava8() executed.");
    }

}
