/**
 * $Id: NotYetImplemented.java,v 1.6 2016/12/10 20:55:18 oboehm Exp $
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
 * Eclipse and other tools will generate default implementation for you if you
 * want to implement an interface. The problem with this implementation is that
 * you must always overwrite this default implementation.
 * <p>
 * To be able to mark these methods as "not yet finished" you can put this
 * annotation in front of it. If you add a text this text is used for thrown
 * UnsupportedOperationException. You can add "{0}" if you want to see the
 * method name in the exception e.g.
 * <code>@NotYetImplemented("don''t call {0} today")</code>.
 * </p>
 * <p>
 * NOTE: a single quote must be written as "''" (see example above) because
 * java.text.MessageFormat is used here for the error message.
 * </p>
 * <p>
 * See patterntesting.sample.Fraction in PatternTesting Samples as an example
 * how to use it.
 * </p>
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.6 $
 * @see UnsupportedOperation
 * @see java.text.MessageFormat
 * @since 19.10.2008
 */
@Documented
@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotYetImplemented {

	/**
	 * You can define the text here which is used for the thrown
	 * UnsupportedOperationException. You can add "{0}" if you want to see the
	 * method name in the exception e.g.
	 * <code>@NotYetImplemented("don''t call {0} today")</code>.
	 *
	 * @return the string
	 */
	String value() default "{0} is not yet implemented";
}
