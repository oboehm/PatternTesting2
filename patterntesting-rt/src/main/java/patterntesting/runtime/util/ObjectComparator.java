/**
 * $Id: ObjectComparator.java,v 1.6 2016/12/10 20:55:18 oboehm Exp $
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
 * (c)reated 11.02.2009 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.util;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Compares two objects using their string representation.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.6 $
 * @since 11.02.2009
 */
public class ObjectComparator implements Comparator<Object>, Serializable {

	private static final long serialVersionUID = 20100106L;

	/**
	 * Compare.
	 *
	 * @param o1
	 *            the object 1
	 * @param o2
	 *            the object 2
	 * @return 0 if both are equals
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final Object o1, final Object o2) {
		String s1 = o1.toString();
		String s2 = o2.toString();
		return s1.compareTo(s2);
	}

}
