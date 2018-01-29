/*
 * $Id: DelegateTo.java,v 1.4 2016/12/10 20:55:18 oboehm Exp $
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
 * (c)reated 13.12.2011 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.annotation;

import java.lang.annotation.*;

import org.junit.runners.ParentRunner;
import org.junit.runners.model.FrameworkMethod;

/**
 * If you use <code>&#064;RunWith(ProxyRunner.class)</code> use must tell the
 * {@link patterntesting.runtime.junit.ProxyRunner} class which class it should
 * use for delegation. ProxyRunner will use this class (which must be derived
 * from {@link ParentRunner}) to start the tests but will handle the same
 * additional annotations like the
 * {@link patterntesting.runtime.junit.SmokeRunner}.
 *
 * @author oboehm
 * @since 1.2 (13.12.2011)
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DelegateTo {

	/**
	 * Value.
	 *
	 * @return a Runner class (must have a constructor that takes a single Class
	 *         to run).
	 */
	Class<? extends ParentRunner<FrameworkMethod>> value();

}
