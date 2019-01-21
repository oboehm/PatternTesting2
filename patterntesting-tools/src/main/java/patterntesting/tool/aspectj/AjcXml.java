/*
 *========================================================================
 *
 * Copyright 2001-2004 Vincent Massol & Matt Smith.
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
 *========================================================================
 */
package patterntesting.tool.aspectj;

import java.io.File;

import org.apache.tools.ant.*;

/**
 * Allow to use the {@link AjcXmlTask} Ant task from a java application.
 *
 * @author <a href="mailto:vmassol@apache.org">Vincent Massol</a>
 *
 * @version $Id: AjcXml.java,v 1.6 2016/01/06 20:47:32 oboehm Exp $
 */
public final class AjcXml extends AjcXmlTask
{
    /**
     * Create default Ant objects to be able to run the task outside of the
     * Ant engine.     */
	public AjcXml() {
        super();
        Project proj = new Project();
        proj.init();
        super.setProject(proj);
        super.setTaskType("ajcxml");
        super.setTaskName("ajcxml");
    }

	/**
	 * With this method you can add different source dirs for compiling.
	 *
	 * @param dirname the name of the directory
	 * @since 1.0
	 */
	public void addSrcdir(final String dirname) {
	    super.createSrc().setPath(dirname);
	}

    /**
     * The example on the patterntesting page uses
     * <pre>compiler.setDestdir("target/classes");</pre>
     * so we should provide it as additional method.
     *
     * @param dirname as String
     */
    public void setDestdir(final String dirname) {
    	super.setDestdir(new File(dirname));
    }

    /**
     * The example on the patterntesting page uses
     * <pre>compiler.setResultFile("result.xml");</pre>
     * so we should provide it as additional method.
     *
     * @param filename as String
     */
    public void setResultFile(final String filename) {
    	super.setResultFile(new File(filename));
    }

}
