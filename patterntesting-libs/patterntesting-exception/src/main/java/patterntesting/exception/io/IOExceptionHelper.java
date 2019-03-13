/**
 * $Id: IOExceptionHelper.java,v 1.5 2016/01/06 20:46:12 oboehm Exp $
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

import java.io.*;

/**
 * The Class IOExceptionHelper.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 10.06.2009
 */
public final class IOExceptionHelper {

    /** No need to instantiate it (utility class). */
    private IOExceptionHelper() {}

    /**
     * In contrast to the original IOException the file name will be
     * appear alway as absolute file name.
     * The cause of the given IOException is ignored for the moment bcause
     * the constructor IOException(String s, Throwable t) is not available
     * in Java 5.
     *
     * @param ioe the original IOException
     * @param file the file
     * @return a better IOException
     */
    public static IOException getBetterIOException(final IOException ioe, final File file) {
        File parent = file.getAbsoluteFile().getParentFile();
        if (parent.exists()) {
            if (file.isAbsolute()) {
                return ioe;
            } else {
                String msg = ioe.getMessage() + ": " + file.getAbsolutePath();
                return new IOException(msg);
            }
        } else {
            return getDirNotFoundException(ioe, parent);
        }
    }

    /**
     * Gets the better io exception.
     *
     * @param ioe the original IOException
     * @param filename which caused the IOExeption
     * @return a better IOException
     */
    public static IOException getBetterIOException(final IOException ioe,
            final String filename) {
        return getBetterIOException(ioe, new File(filename));
    }

    /**
     * If the cause of a FileNotFoundException is a missing directory you will
     * get a DirNotFoundException as return value.
     *
     * @param ioe the original FileNotFoundException
     * @param file the cause of the FileNotFoundException
     * @return a FileNotFoundException or a DirNotFoundException
     */
    public static FileNotFoundException getBetterFileNotFoundException(
            final FileNotFoundException ioe, final File file) {
        File parent = file.getAbsoluteFile().getParentFile();
        if (!parent.exists()) {
            return getDirNotFoundException(ioe, parent);
        }
        if (file.isAbsolute()) {
            return ioe;
        } else {
            String msg = ioe.getMessage().substring(file.toString().length());
            return new FileNotFoundException(file.getAbsolutePath() + msg);
        }
    }

    /**
     * At the moment this is only a simple constructor call. But when we
     * switch to Java6 we will evaluate the cause of the given IOException.
     *
     * @param ioe the IOException with the cause
     * @param dir the directory
     * @return a DirNotFoundException
     */
    public static DirNotFoundException getDirNotFoundException(final IOException ioe,
            final File dir) {
        return new DirNotFoundException(dir.getAbsolutePath()
                + " (No such directory)");
    }

}
