/**
 * $Id: UnsupportedOperation.java,v 1.5 2016/12/10 20:55:18 oboehm Exp $
 *
 * Copyright (c) 2008 by Oliver Boehm
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
 * (c)reated 19.10.2008 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.annotation;

import java.lang.annotation.*;

/**
 * This is similar to the @NotYetImplemented annotation. The difference is only
 * the semantic: "@UnsupportedOperation" means, it is not supported yet and the
 * annotated method will probably also not supported in the future.
 * "@NotYetImplemented" means, that the method is not yet implemented but will
 * be available in the (near) future.
 * <p>
 * In PatternTesting Samples (patterntesting.sample.Fraction) you'll find an
 * example you to use it.
 * </p>
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.5 $
 * @see NotYetImplemented
 * @see java.text.MessageFormat
 * @since 19.10.2008
 */
@Documented
@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR })
@Retention(RetentionPolicy.RUNTIME)
public @interface UnsupportedOperation {

	/**
	 * You can define the text here which is used for the thrown
	 * UnsupportedOperationException. You can add "{0}" if you want to see the
	 * method name in the exception e.g.
	 * <code>@NotYetImplemented("don''t call {0} today")</code>.
	 *
	 * @return the string
	 */
	String value() default "{0} is not supported";
}
