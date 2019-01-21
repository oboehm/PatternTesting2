/*
 * $Id: XAjcTestCompileMojo.java,v 1.3 2017/05/14 20:10:08 oboehm Exp $
 *
 * Copyright (c) 2011 by Oliver Boehm
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
 * (c)reated 11.01.2011 by oliver (ob@oasd.de)
 */

package patterntesting.tool.maven;

import org.apache.maven.plugins.annotations.*;
import org.codehaus.mojo.aspectj.AjcTestCompileMojo;

/**
 * Weaves all test classes and adds the PatternTesting aspect libraries.
 *
 * @author oliver
 * @since 1.1 (11.01.2011)
 */
@Mojo(name = "test-compile",
        defaultPhase = LifecyclePhase.PROCESS_TEST_SOURCES,
        requiresDependencyResolution = ResolutionScope.TEST,
        threadSafe = true)
public class XAjcTestCompileMojo extends AjcTestCompileMojo {

}
