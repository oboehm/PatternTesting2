/*
 * Copyright (c) 2013 by Oli B.
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
 * (c)reated 06.04.2014 by Oli B. (boehm@javatux.de)
 */

package patterntesting.runtime.jmx;

import java.net.URI;
import java.util.Date;

/**
 * This is the interface for the info bean anf for the use of this bean as
 * MBean. This allows you to access the infos e.g. with the 'jconsole' during
 * runtime.
 *
 * @author oliver (boehm@javatux.de)
 * @since 1.4.1 (06.04.2014)
 */
public interface InfoMBean {

	/**
	 * Gets the version of PatternTesting.
	 *
	 * @return the version (e.g. "1.4.1")
	 */
	@Description("returns the version of PatternTesting")
	public String getVersion();

	/**
	 * Gets the builds the time of PatternTesting.
	 *
	 * @return the builds the time
	 */
	@Description("returns the build time of PatternTesting")
	public Date getBuildTime();

	/**
	 * Gets the manifest URI.
	 *
	 * @return the manifest URI
	 */
	@Description("return the URI of the Manifest")
	public URI getManifestURI();

}
