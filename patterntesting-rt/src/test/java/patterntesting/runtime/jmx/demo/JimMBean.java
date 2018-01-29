package patterntesting.runtime.jmx.demo;

import java.io.Serializable;

/**
 * The Interface JimMBean.
 */
public interface JimMBean extends Serializable {

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName();

    /**
     * Sets the name.
     *
     * @param name the name to set
     */
    public void setName(String name);

}
/**
 * $Log: JimMBean.java,v $
 * Revision 1.1  2010/01/05 13:26:17  oboehm
 * begin with 1.0
 *
 * Revision 1.3  2009/12/19 22:34:09  oboehm
 * trailing spaces removed
 *
 * Revision 1.2  2009/09/25 14:49:43  oboehm
 * javadocs completed with the help of JAutodoc
 *
 * Revision 1.1  2009/02/20 21:34:03  oboehm
 * MBeanRegistry with default implementation added
 *
 * $Source: /cvsroot/patterntesting/PatternTesting10/patterntesting-rt/src/test/java/patterntesting/runtime/jmx/demo/JimMBean.java,v $
 */
