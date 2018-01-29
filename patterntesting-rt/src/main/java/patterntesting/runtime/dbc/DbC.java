/**
 *
 */
package patterntesting.runtime.dbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;

import patterntesting.runtime.util.Assertions;

/**
 * The Class DbC.
 *
 * @author oliver
 */
public final class DbC {

	private static final Logger LOG = LogManager.getLogger(DbC.class);

	static {
		if (Assertions.ENABLED) {
			LOG.debug("DbC is active");
		} else {
			LOG.debug("DbC deactivated - call 'java -ea' (SunVM) to activate it");
		}
	}

	/** No need to instantiate it (utility class). */
	private DbC() {
	}

	/**
	 * Use this method to describe the condition which must be true when your
	 * method is called.
	 *
	 * @param precondition
	 *            e.g. (arg0 != null)
	 */
	public static void require(final boolean precondition) {
		require(precondition, "precondition violated");
	}

	/**
	 * Use this method to describe the condition which must be true when your
	 * method is called.
	 *
	 * @param precondition
	 *            e.g. (arg0 != null)
	 * @param message
	 *            e.g. "null argument is not allowed"
	 */
	public static void require(final boolean precondition, final Object message) {
		if (Assertions.ENABLED && !precondition) {
			throw new ContractViolation(message);
		}
	}

	/**
	 * Use this method to describe the condition which must be true when the
	 * preconditions were true and your method is finished.
	 *
	 * @param postcondition
	 *            e.g. (value &gt; 0)
	 */
	public static void ensure(final boolean postcondition) {
		ensure(postcondition, "postcondition violated");
	}

	/**
	 * Use this method to describe the condition which must be true when the
	 * preconditions were true and your method is finished.
	 *
	 * @param postcondition
	 *            e.g. (value &gt; 0)
	 * @param message
	 *            e.g. "value must be positive"
	 */
	public static void ensure(final boolean postcondition, final Object message) {
		if (Assertions.ENABLED && !postcondition) {
			throw new ContractViolation(message);
		}
	}

}
