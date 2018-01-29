/*
 * $Id: FitRunner.java,v 1.6 2016/12/18 20:19:39 oboehm Exp $
 *
 * Copyright (c) 2006 agentes AG
 * Raepplenstr. 17, 70191 Stuttgart, Germany
 * All rights reserved.
 *
 * (c)reated 25.09.2006 by oliver.boehm@agentes.de
 */

package patterntesting.runtime.fit;

import java.io.*;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;

import fit.FileRunner;

/**
 * If you want to start a Fixture test you can use this Runner class to
 * start it.
 *
 * @author <a href="mailto:oliver.boehm@agentes.de">oliver</a>
 * @see "http://fit.c2.com"
 * @see fit.FileRunner
 * @since 25.09.2006
 */
public class FitRunner extends FileRunner {

	private static final Logger log = LogManager.getLogger(FitRunner.class);
	private final File inputFile;
	private final File outputFile;

	/**
	 * Instantiates a new fit runner.
	 *
	 * @param inputFile the input file
	 * @param outputFile the output file
	 */
	public FitRunner(final File inputFile, final File outputFile) {
	    super();
	    this.inputFile = inputFile;
	    this.outputFile = outputFile;
	    log.debug(this + " created");
	}

    /**
     * Read the test document and run the fixtures.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @SuppressWarnings("unchecked")
    public void run() throws IOException {
        fixture.summary.put("input", inputFile.getAbsoluteFile());
        fixture.summary.put("output", outputFile.getAbsoluteFile());
        this.input = FileUtils.readFileToString(inputFile, StandardCharsets.UTF_8);
        this.output = new PrintWriter(outputFile);
        this.process();
        this.exit();
    }

    /**
     * Exit.
     *
     * @see fit.FileRunner#exit()
     */
    @Override
    protected void exit() {
        output.close();
        log.info(this.outputFile + ": " + fixture.counts());
    }

}