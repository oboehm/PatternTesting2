package patterntesting.concurrent.junit;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import patterntesting.runtime.annotation.DelegateTo;

/**
 * The Class ParallelProxyRunnerTmpTest was received from Patrick Linskey as
 * PatternTestingConcurrentTest and as part of a critical patch. It is set to
 * "@Ignore" because two of the tests will fail. This test is only for
 * manual testing to see if the different events (success, failure, error)
 * will be recorded and replayed in the correct order.
 * 
 * @since 1.2.30 (03-Nov-2012)
 */
@Ignore // remove me if you want to test the different events.
@RunWith(ParallelProxyRunner.class)
@DelegateTo(BlockJUnit4ClassRunner.class)
public class ParallelProxyRunnerTmpTest {
    
    /**
     * Success.
     */
    @Test
    public void success() {
    }

    /**
     * Failure.
     */
    @Test
    public void failure() {
        Assert.fail();
    }

    /**
     * Error.
     */
    @Test
    public void error() {
        throw new RuntimeException();
    }

}
