package patterntesting.concurrent.experimental.math;

/**
 * Uses an recursive algo to calculate the {@link Fibonacci} numbers.
 *
 * @author reik, 11/15/11
 */
public class RecursiveFibonacci implements Fibonacci {

    /**
     * Calculate.
     *
     * @param n the n
     * @return the long
     * @see Fibonacci#calculate(int)
     */
    public long calculate(final int n) {
        if (n <= 1) {
            return n;
        } else {
            return calculate(n - 1) + calculate(n - 2);
        }
    }
}
