/*
 * $Id: TestEnvironment.java,v 1.4 2016/12/10 20:55:18 oboehm Exp $
 *
 * Copyright (c) 2015 by Oliver Boehm
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
 * (c)reated 07.02.2015 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.util;

import org.apache.commons.lang3.SystemUtils;

/**
 * This class is intended for testing to be able to provide a different
 * environment.
 *
 * @author oliver
 * @version $Revision: 1.4 $
 * @since 1.6 (07.02.2015)
 */
public final class TestEnvironment extends Environment {

	private String osName = SystemUtils.OS_NAME;
	private String osArch = SystemUtils.OS_ARCH;
	private String osVersion = SystemUtils.OS_VERSION;
	private String javaVersion = SystemUtils.JAVA_VERSION;
	private String javaVendor = SystemUtils.JAVA_VENDOR;
	private String userName = SystemUtils.USER_NAME;

	/**
	 * Gets the os name.
	 *
	 * @return the osName
	 */
	@Override
	public String getOsName() {
		return osName;
	}

	/**
	 * Sets the os name.
	 *
	 * @param osName
	 *            the osName to set
	 */
	public void setOsName(final String osName) {
		this.osName = osName;
	}

	/**
	 * Gets the os arch.
	 *
	 * @return the osArch
	 */
	@Override
	public String getOsArch() {
		return osArch;
	}

	/**
	 * Sets the os arch.
	 *
	 * @param osArch
	 *            the osArch to set
	 */
	public void setOsArch(final String osArch) {
		this.osArch = osArch;
	}

	/**
	 * Gets the os version.
	 *
	 * @return the osVersion
	 */
	@Override
	public String getOsVersion() {
		return osVersion;
	}

	/**
	 * Sets the os version.
	 *
	 * @param osVersion
	 *            the osVersion to set
	 */
	public void setOsVersion(final String osVersion) {
		this.osVersion = osVersion;
	}

	/**
	 * Gets the java version.
	 *
	 * @return the javaVersion
	 */
	@Override
	public String getJavaVersion() {
		return javaVersion;
	}

	/**
	 * Sets the java version.
	 *
	 * @param javaVersion
	 *            the javaVersion to set
	 */
	public void setJavaVersion(final String javaVersion) {
		this.javaVersion = javaVersion;
	}

	/**
	 * Gets the java vendor.
	 *
	 * @return the javaVendor
	 */
	@Override
	public String getJavaVendor() {
		return javaVendor;
	}

	/**
	 * Sets the java vendor.
	 *
	 * @param javaVendor
	 *            the javaVendor to set
	 */
	public void setJavaVendor(final String javaVendor) {
		this.javaVendor = javaVendor;
	}

	/**
	 * Gets the user name.
	 *
	 * @return the userName
	 */
	@Override
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the user name.
	 *
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(final String userName) {
		this.userName = userName;
	}

}
