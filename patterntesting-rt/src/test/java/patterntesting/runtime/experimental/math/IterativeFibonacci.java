package patterntesting.runtime.experimental.math;

/**
 * Uses an iterative algo to calculate the {@link Fibonacci} numbers.
 *
 * @author reik, 11/15/11
 */
public class IterativeFibonacci implements Fibonacci {

    /**
     * Calculate.
     *
     * @param n the n
     * @return the long
     * @see Fibonacci#calculate(int)
     */
    @Override
	public long calculate(final int n) {
        int x = 0;
        int y = 1;
        for (int i = 0; i < n; i++) {
            int z = x;
            x = y;
            y = z + y;
        }
        return x;
    }
}
