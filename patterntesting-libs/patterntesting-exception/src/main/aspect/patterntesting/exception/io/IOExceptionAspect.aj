/*
 * Copyright (c) 2008-2019 by Oliver Boehm
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
 * (c)reated 10.09.2008 by oliver (ob@oasd.de)
 */
package patterntesting.exception.io;

import java.io.*;

import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.*;
import org.aspectj.lang.annotation.SuppressAjWarnings;

/**
 * Often the thrown IOExceptions contains too less informations so you don't
 * know what happens. So this aspect try to catch all methods from the
 * java.io package which may throw an IOException. It tries to enrich the
 * exception with additional informations like the filename wich was not found
 * or other useful infos.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 0.6
 *
 * TODO provide an annotation 'NoBetterIOExceptions'
 */
public aspect IOExceptionAspect {

	private static final Logger log = LogManager.getLogger(IOExceptionAspect.class);

	/**
	 * If File.createTempFile(String prefix, String suffix, File directory)
	 * throws an IOException it may be that the directory does not exist in
	 * the temp directory. If it is so the exception should contain the missing
	 * directory name.
	 *
	 * @param dir
	 * @return
	 * @throws IOException
	 * @see {@link File#createTempFile(String, String, File)}
	 */
    @SuppressAjWarnings({"adviceDidNotMatch"})
	File around(final File dir) throws IOException :
			call(public static File File.createTempFile(String, String, File) throws IOException)
			&& args(*, *, dir) {
		try {
			return proceed(dir);
		} catch (IOException ioe) {
			File tmpdir = new File(SystemUtils.getJavaIoTmpDir(), dir.toString());
			if (tmpdir.exists()) {
				throw ioe;
			} else {
				throw IOExceptionHelper.getDirNotFoundException(ioe, tmpdir);
			}
		}
	}

	/**
	 * If File.createTempFile(String prefix, String suffix)
	 * throws an IOException it may be that the prefix may contain a directory
	 * part which does not exist. Or perhaps the suffix is wrong.
	 *
	 * @param dir
	 * @return
	 * @throws IOException
	 * @see {@link File#createTempFile(String, String)}
	 */
    @SuppressAjWarnings({"adviceDidNotMatch"})
	File around(final String prefix, final String suffix) throws IOException :
			call(public static File File.createTempFile(String, String) throws IOException)
			&& args(prefix, suffix) {
		try {
			return proceed(prefix, suffix);
		} catch (IOException ioe) {
			String dirname = new File(prefix).getParent();
			if (dirname != null) {
				File tmpdir = new File(SystemUtils.getJavaIoTmpDir(), dirname);
				if (!tmpdir.exists()) {
					throw IOExceptionHelper.getDirNotFoundException(ioe, tmpdir);
				}
			}
			dirname = new File(suffix).getParent();
			if (dirname != null) {
				String msg = "invalid suffix '" + suffix + "'";
				log.debug("converting: " + ioe.getMessage() + " -> " + msg);
				throw new IOException(msg);
			}
			throw ioe;
		}
	}

	/**
	 * We want to see the file as additional information if an IOException
	 * is thrown.
	 */
    @SuppressAjWarnings({"adviceDidNotMatch"})
	Object around(final File file) throws IOException :
			call(public !static * File.*(..) throws IOException)
			&& target(file) {
		try {
			return proceed(file);
		} catch (IOException ioe) {
			throw IOExceptionHelper.getBetterIOException(ioe, file);
		}
	}



	/////  FileNotFoundExceptions   ///////////////////////////////////////////

	/**
	 * The constructor of FileInputStream(..), FileOutputStream(..)
	 * or RandomAccessFile(..)
	 * can throw an FileNotFoundException. You see the file in this exception
	 * but if the file is not given as absolute path to the constructor you don't
	 * see the path. But this is an important information especially in a
	 * web environment where you don't know the current working directory.
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     * @see {@link FileInputStream}
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    Object around(final File file) throws FileNotFoundException :
            (call(public FileInputStream.new(File) throws FileNotFoundException)
            || call(public FileOutputStream.new(File) throws FileNotFoundException)
            || call(public RandomAccessFile.new(File, String) throws FileNotFoundException))
            && args(file, ..) {
        try {
            return proceed(file);
        } catch (FileNotFoundException ioe) {
            throw IOExceptionHelper.getBetterFileNotFoundException(ioe, file);
        }
    }

    /**
     * The constructors of FileInputStream, FileOutputStream and
     * RandomAcessFile can throw a FileNotFoundException. This aspect
     * enriches this exception by the filename which was not found.
     * 
     * @param filename the filename which was not found
     * @return the created object
     * @throws FileNotFoundException exception with the filename
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    Object around(final String filename) throws FileNotFoundException :
            (call(public FileInputStream.new(String) throws FileNotFoundException)
            || call(public FileOutputStream.new(String) throws FileNotFoundException)
            || call(public RandomAccessFile.new(String, String) throws FileNotFoundException))
            && args(filename, ..) {
        try {
            return proceed(filename);
        } catch (FileNotFoundException ioe) {
            File file = new File(filename);
            throw IOExceptionHelper.getBetterFileNotFoundException(ioe, file);
        }
    }

}
