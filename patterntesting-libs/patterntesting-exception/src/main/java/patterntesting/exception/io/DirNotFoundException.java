/**
 * $Id: DirNotFoundException.java,v 1.4 2016/01/06 20:46:12 oboehm Exp $
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
 * (c)reated 10.06.2009 by oliver (ob@aosd.de)
 */
package patterntesting.exception.io;

import java.io.FileNotFoundException;

/**
 * Not every FileNotFoundException is a FileNotFoundException. Sometimes a
 * FileNotFoundException is thrown if the parent directory does not exist.
 * For this case you can now use this exception.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.4 $
 * @see FileNotFoundException
 * @since 10.06.2009
 */
public class DirNotFoundException extends FileNotFoundException {

    private static final long serialVersionUID = 20090610L;

    /**
     * The default constructor. You should should better use
     * {@link DirNotFoundException#DirNotFoundException(String)}
     */
    public DirNotFoundException() {
        super();
    }

    /**
     * Instantiates a new dir not found exception.
     *
     * @param msg            the message given more information why the directory can't be
     *            found.
     */
    public DirNotFoundException(final String msg) {
        super(msg);
    }

}
