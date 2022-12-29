/**
 * $Id: NullConstants.java,v 1.7 2016/12/18 20:19:41 oboehm Exp $
 *
 * Copyright (c) 2009 by Oliver Boehm
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
 * (c)reated 13.06.2009 by oliver (ob@aosd.de)
 */
package patterntesting.runtime;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import org.slf4j.LoggerFactory;
import org.slf4j.*;

/**
 * This class contains some constants (like a NULL_STRING) as constant. It can
 * be statically imported.
 * <p>
 * If you are using Java 8 you can use Optionals to indicate empty (or NULL)
 * values.
 * </p>
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.7 $
 * @since 13.06.2009
 */
public final class NullConstants {

	private static final Logger LOG = LoggerFactory.getLogger(NullConstants.class);

	/** to avoid that this class will be instantiated */
	private NullConstants() {
	}

	/**
	 * If you have a method with a list of arguments where some arguments are
	 * optional you can
	 * <ul>
	 * <li>provide the method with different signatures (good idea, but not
	 * always feasible)</li>
	 * <li>admit null arguments and handle it in the methode (not a good idea
	 * because it can happen accidentally)</li>
	 * <li>use this special NULL_OBJECT object to mark an argument as optional
	 * </li>
	 * </ul>
	 * Unfortunately this works only for object as argument. For String as
	 * argument you can use NULL_STRING.
	 *
	 * @see #NULL_STRING
	 */
	public static final Object NULL_OBJECT = new Object();

	/**
	 * You need a null string and don't want to use <i>null</i>? Use
	 * <i>NULL_STRING</i> and you can write code like
	 *
	 * <pre>
	 * if (name == NULL_STRING) ...
	 * </pre>
	 */
	public static final String NULL_STRING = "";

	/**
	 * You need a null file and don't want to use <i>null</i>? Use
	 * <i>NULL_FILE</i> and you can write code like
	 *
	 * <pre>
	 * if (name == NULL_STRING) ...
	 * </pre>
	 */
	public static final File NULL_FILE = new File(NULL_STRING);

	/**
	 * You need a null throwable and don't want to use <i>null</i>? Use
	 * <i>NULL_THROWABLE</i> and you can write code like
	 *
	 * <pre>
	 * if (name == NULL_THROWABLE) ...
	 * </pre>
	 */
	public static final Throwable NULL_THROWABLE = new Throwable();

	/**
	 * You need a null exception and don't want to use <i>null</i>? Use
	 * <i>NULL_EXCEPTION</i> and you can write code like
	 *
	 * <pre>
	 * if (name == NULL_EXCEPTION) ...
	 * </pre>
	 */
	public static final Throwable NULL_EXCEPTION = new Exception();

	/**
	 * You need a null date and don't want to use <i>null</i>? Use
	 * <i>NULL_DATE</i> and you can write code like
	 *
	 * <pre>
	 * if (name == NULL_DATE) ...
	 * </pre>
	 *
	 * The NULL_DATE is defined here as 1.1.1970 (the epoch).
	 */
	public static final Date NULL_DATE = new Date(0L);

	/**
	 * You need a null URI and don't want to use <i>null</i>? Uses
	 * <i>NULL_URI</i> and you can write code like
	 *
	 * <pre>
	 * if (name == NULL_URI) ...
	 * </pre>
	 *
	 * The NULL_URI is defined as URI for "http://null".
	 */
	public static final URI NULL_URI = getNullURI();

	private static URI getNullURI() {
		try {
			return new URI("http://null");
		} catch (URISyntaxException cannothappen) {
			LOG.warn("NULL_URI defined as 'null':", cannothappen);
			return null;
		}
	}

}
