/*
 * $Id: DrawSequenceDiagram.java,v 1.9 2016/12/10 20:55:18 oboehm Exp $
 *
 * Copyright (c) 2013 by Oliver Boehm
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
 * (c)reated 06.09.2013 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.annotation;

import java.lang.annotation.*;

/**
 * With this annotation you can control the generation of sequence diagrams. You
 * can put this annotation before a constructor or method.
 *
 * @author oliver
 * @see IgnoreForSequenceDiagram
 * @since 1.4 (06.09.2013)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.CONSTRUCTOR, ElementType.METHOD })
public @interface DrawSequenceDiagram {

	/**
	 * If this value is given a new diagram with this filename will be
	 * generated. If the given filename ends with ".pic" the generated file can
	 * be used as input for <a href="http://umlgraph.org/">UMLGraph</a> and can
	 * be converted with the <i>pic2plot</i> of the GNU plutils into other
	 * graphic formats.
	 * <p>
	 * If the given filename ends with ".txt" the message sequence diagram will
	 * be genearted as simple text file which can be used as input for
	 * <a href="https://www.websequencediagrams.com/">websequence diagrams</a>.
	 * </p>
	 * <p>
	 * If no value is given the marked method or constructor will appended to
	 * the last generated diagram.
	 * </p>
	 *
	 * @return the string
	 */
	String value() default "";

	/**
	 * If you don't want the call to a class of the "com.pany" package you can
	 * add "com.pany.*" as pattern.
	 * <p>
	 * For the pattern the same syntax as in AspectJ is supported.
	 * </p>
	 * <p>
	 * NOTE: not yet realized
	 * </p>
	 *
	 * @return the string[]
	 */
	String[] excluded() default "";

	/**
	 * By default all classes are included. If you do not like that you can
	 * define a pattern wich packages and/or classes should be include.
	 * <p>
	 * For the pattern the same syntax as in AspectJ is supported.
	 * </p>
	 * <p>
	 * NOTE: not yet realized
	 * </p>
	 *
	 * @return the string[]
	 */
	String[] included() default "*";

}
