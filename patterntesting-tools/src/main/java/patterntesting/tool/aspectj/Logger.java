/*
 *========================================================================
 *
 * Copyright 2005 Vincent Massol & Matt Smith.
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
 *========================================================================
 */
package patterntesting.tool.aspectj;

import org.apache.logging.log4j.LogManager;
import org.aspectj.lang.reflect.SourceLocation;
import patterntesting.runtime.io.ExtendedFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;


/**
 * The Class Logger.
 *
 * @author <a href="mailto:matt@ojojo.com">Matt Smith</a>
 * @version $Id: Logger.java,v 1.8 2016/12/30 19:07:44 oboehm Exp $
 */
public final class Logger {
    
    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(Logger.class);
    
	/** Lists of errors that were logged. Array of {@link AjcFileResult} objects. */
	private Hashtable<String, AjcFileResult> errors;
    private final Hashtable<String, AjcFileResult> results = new Hashtable<String, AjcFileResult>();
	private File resultFile;
	private static final Logger LOGGER = new Logger();

    private Logger() {
        this.reset();
        try (OutputStream ostream = new FileOutputStream(this.resultFile)) {
            this.write(ostream);
        } catch (IOException ex) {
            LOG.warn("Cannot write to '{}':", this.resultFile, ex);
        }
    }

    /**
     * Gets the single instance of Logger.
     *
     * @return the only instance
     */
    public static Logger getInstance() {
        return LOGGER;
    }

    /**
     * Reset error table.
     */
    public void reset() {
        errors = new Hashtable<String, AjcFileResult>();
        if (this.resultFile == null) {
            this.setResultFile();
        }
        this.createResultDir();
    }

   private void createResultDir() {
	   File parent = this.getResultFile().getParentFile();
	   if (!parent.exists()) {
		   if (parent.mkdir()) {
			   LOG.info("Directory {} created for result file.", parent);
		   } else {
			   LOG.warn("Cannot create directory '{}'.", parent);
		   }
	   }
   }

   /**
    * Sets the result file to a temporary directoy.
    * "/patterntesting/patterntesting.xml" as default result file
    * is no longer supported.
    */
   private void setResultFile() {
	   File dir = ExtendedFile.getTmpdir("patterntesting");
	   if (!dir.mkdirs()) {
	       LOG.error("Cannot create dir '{}'.", dir);
	   }
	   this.setResultFile(new File(dir, "patterntesting.xml"));
   }

   /**
    * Set result file.
    * @param file the result fi.e
    */
   public void setResultFile(final File file) {
      this.resultFile = file;
   }

   /**
    * Gets the result file.
    *
    * @return the result file
    */
   public File getResultFile() {
	   return this.resultFile;
   }

   /**
    * Gets the errors.
    *
    * @return the list of errors that were logged as
    *          a List of AjcResult objects
    */
    public Enumeration<AjcFileResult> getErrors() {
        return this.errors.elements();
    }

   /**
    * Count errors.
    *
    * @return the number of counted errors
    */
   public int countErrors() {
		int n = 0;
        for (Enumeration<AjcFileResult> e = getErrors(); e.hasMoreElements(); e.nextElement()) {
            n++;
        }
		return n;
	}

    /**
     * Gets the report results.
     *
     * @return report results
     */
    public Enumeration<AjcFileResult> getReportResults() {
        return this.results.elements();
    }

   /**
    * Log pt violation.
    *
    * @param location the SourceLocation of the error
    * @param message the message to log
    */
    public synchronized void logPTViolation(final SourceLocation location, final String message) {
        if (location != null) {
            String path = location.getFileName();
            AjcFileResult fileResult = errors.get(path);
            if (fileResult == null) {
                fileResult = new AjcFileResult(path);
                errors.put(path, fileResult);
            }
            fileResult.addError(new AjcResult(location.getLine(), message));
            try (FileOutputStream out = new FileOutputStream(resultFile)) {
                this.write(out);
                out.flush();
            } catch (IOException ex) {
                LOG.warn("Cannot log to '{}':", resultFile, ex);
            }
        }
    }

   /**
    * Write.
    *
    * @param resultStream where to write the result
    */
    public void write(final OutputStream resultStream) {
        XMLFormatter formatter = new XMLFormatter();
        try {
            resultStream.write(formatter.format(getReportResults(), getErrors()).getBytes());
        } catch (IOException ioe) {
            LOG.warn("Cannot write to {}:", resultStream, ioe);
        }
    }
}
