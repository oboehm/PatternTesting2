/*
 * $Id: ZipExceptionAspect.aj,v 1.1 2011/12/22 17:35:49 oboehm Exp $
 *
 * Copyright (c) 2010 by Oliver Boehm
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
 * (c)reated 11.11.2010 by oliver (ob@oasd.de)
 */

package patterntesting.exception.io;

import java.io.*;
import java.util.*;
import java.util.jar.*;
import java.util.zip.*;

import org.aspectj.lang.annotation.SuppressAjWarnings;

/**
 * This is a wrapper around ZipFile to enrich the thrown ZipException with
 * the name of the zip file.
 * 
 * @author oliver
 * @since 1.1 (11.11.2010)
 */
public aspect ZipExceptionAspect {
    
    /**
     * If the ZipFile constructor fails we want to have the filename in the
     * thrown exception.
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    ZipFile around(final Object file) throws ZipException, IOException: 
            call(public ZipFile.new(Object+, ..) throws ZipException, IOException)
            && args(file) {
        try {
            return proceed(file);
        } catch (ZipException ze) {
            if (file == null) {
                throw ze;
            }
            String msg = file + ": " + ze.getMessage();
            throw new ZipException(msg);
        }
    }
    
    /**
     * If the JarFile constructor fails we want to have the filename in the
     * thrown exception.
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    JarFile around(final Object file) throws IOException: 
            call(public JarFile.new(Object+, ..) throws IOException)
            && args(file) {
        try {
            return proceed(file);
        } catch (ZipException ze) {
            if (file == null) {
                throw ze;
            }
            String msg = file + ": " + ze.getMessage();
            throw new ZipException(msg);
        }
    }
    
    /**
     * If one of the ZipFile methods throws an exception the filename of the
     * zip file should be part of the exception.
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    Object around(final ZipFile zipFile) throws IOException:
            call(public * ZipFile+.*(..) throws IOException)
            && target(zipFile) {
        try {
            return proceed(zipFile);
        } catch (ZipException ze) {
            String msg = zipFile.getName() + ": " + ze.getMessage();
            throw new ZipException(msg);
        } catch (IllegalStateException ise) {
            String msg = zipFile.getName() + ": " + ise.getMessage();
            throw new IllegalStateException(msg);
        }
    }
    
    /**
     * Also the IllegalStateException is enriched by the filename of the
     * zip file.
     */
    @SuppressAjWarnings({"adviceDidNotMatch"})
    Object around(final ZipFile zipFile):
            (call(public ZipEntry ZipFile+.getEntry(String))
                || call(public Enumeration<? extends ZipEntry> ZipFile+.entries())
                || call(public int ZipFile+.size())
            )
            && target(zipFile) {
        try {
            return proceed(zipFile);
        } catch (IllegalStateException ise) {
            String msg = zipFile.getName() + ": " + ise.getMessage();
            throw new IllegalStateException(msg);
        }
    }

}

