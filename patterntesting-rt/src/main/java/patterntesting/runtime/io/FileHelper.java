/**
 * $Id: FileHelper.java,v 1.24 2016/12/29 07:42:35 oboehm Exp $
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
 * (c)reated 11.09.2008 by oliver (ob@oasd.de)
 */
package patterntesting.runtime.io;

import java.io.*;
import java.util.List;

import org.apache.commons.io.*;

/**
 * The Class FileHelper.
 * <p>
 * TODO: Will be removed in 2.0.
 * </p>
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.24 $
 * @since 11.09.2008
 * @deprecated use {@link ExtendedFile} or FileUtils from Apache Commons
 */
@Deprecated
public final class FileHelper {

	/** No need to instantiate it (utility class). */
	private FileHelper() {
	}

	/**
	 * Gets the tmpdir.
	 *
	 * @return the tmpdir
	 * @deprecated use {@link FileUtils#getTempDirectory()}
	 */
	@Deprecated
	public static File getTmpdir() {
		return new File(System.getProperty("java.io.tmpdir"));
	}

	/**
	 * Gets the tmpdir.
	 *
	 * @param file
	 *            the file
	 *
	 * @return the tmpdir
	 */
	public static File getTmpdir(final File file) {
		return getTmpdir(file.toString());
	}

	/**
	 * Gets the tmpdir.
	 *
	 * @param filename
	 *            the filename
	 *
	 * @return the tmpdir
	 */
	public static File getTmpdir(final String filename) {
		return new File(getTmpdir(), filename);
	}

	/**
	 * Creates a directory in the temp directory.
	 *
	 * @return the directory
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static File createTmpdir() throws IOException {
		return createTmpdir("dir", "");
	}

    /**
     * Creates a directory in the temp directory.
     *
     * @param prefix the prefix
     * @param suffix the suffix
     * @return the directory
     * @throws IOException Signals that an I/O exception has occurred.
     * @deprecated use {@link ExtendedFile#createTmpdir(String, String)}
     */
	@Deprecated
	public static File createTmpdir(final String prefix, final String suffix) throws IOException {
	    return ExtendedFile.createTmpdir(prefix, suffix);
	}

	/**
	 * Gets the home dir.
	 *
	 * @return the home dir
	 */
	public static File getHomeDir() {
		return new File(System.getProperty("user.home"));
	}

	/**
	 * Normalizes a file, analogous to {@link FilenameUtils#normalize(String)}.
	 * But before we normalize it we try to ask to OS for the canonical path for
	 * it.
	 *
	 * @param file the file
	 * @return the file
	 * @deprecated use {@link ExtendedFile#normalize()}
	 */
	@Deprecated
	public static File normalize(final File file) {
		return new ExtendedFile(file).normalize();
	}

	/**
     * Comparing two files is not so easy as it seems to be. On unix systems or
     * Mac OS-X you can see the same file via two different mount points or as
     * two different hard links. So the files are "normalized" and then
     * compared.
     * <p>
     * Note: If you are on *nix you can use the inode to compare but it is hard
     * to get it with Java. Also it is not portable. So another approach is used
     * here (see above).
     * </p>
     *
     * @param f1 file 1
     * @param f2 file 2
     * @return true, if successful
     * @since 1.1
     * @deprecated use {@link ExtendedFile#equals(Object)}
     */
	@Deprecated
	public static boolean isEquals(final File f1, final File f2) {
		File one = new ExtendedFile(f1);
		File two = new ExtendedFile(f2);
		return one.equals(two);
	}

	/**
	 * Converts a list with files into a string array.
	 *
	 * @param files the files
	 * @return the string[]
	 * @since 1.5 (27.08.2014)
	 * @deprecated use {@link ExtendedFile#toStringArray(List)}
	 */
	@Deprecated
	public static String[] toStringArray(final List<File> files) {
	    return ExtendedFile.toStringArray(files);
	}

	/**
	 * Gets the stream for the given log file. As fallback <em>stdout</em> will
	 * be used.
	 *
	 * @param logFile the log file
	 * @return the stream for
	 * @deprecated use {@link ExtendedFile#createOutputStreamFor(File)}
	 */
	@Deprecated
	public static OutputStream getStreamFor(final File logFile) {
		return ExtendedFile.createOutputStreamFor(logFile);
	}

}
