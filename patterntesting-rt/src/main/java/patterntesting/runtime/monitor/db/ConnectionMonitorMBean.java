package patterntesting.runtime.monitor.db;

/**
 * This is the interface of {@link ConnectionMonitor} for the use of this class
 * as MBean. You can monitor DB connections with it and ask the class for open
 * connections.
 * <p>
 * Note: Since 1.4.2 this class was moved from package
 * "patterntesting.runtime.db" to here.
 * </p>
 *
 * @author oliver (ob@aosd.de)
 * @version $Revision: 1.10 $
 * @since 1.3 (02-Jan-2013)
 * @deprecated since 2.0, use {@link clazzfish.jdbc.ConnectionMonitorMBean}
 */
@Deprecated
public interface ConnectionMonitorMBean extends clazzfish.jdbc.ConnectionMonitorMBean {
	
}
