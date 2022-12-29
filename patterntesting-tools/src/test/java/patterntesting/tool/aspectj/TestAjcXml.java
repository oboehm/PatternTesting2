package patterntesting.tool.aspectj;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.Test;
import patterntesting.runtime.io.ExtendedFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link AjcXml}.
 * <p>
 * When you start this test it runs inside Eclipse. But if you start it via
 * Maven2 the compile output is emtpy - no idea why.
 * I guess the classpath is not set up correct so I added some tests to
 * verify it. (23-May-07, ob@aosd.de)
 * </p>
 *
 * @author <a href="mailto:vmassol@apache.org">Vincent Massol</a>
 *
 * @version $Id: TestAjcXml.java,v 1.19 2016/12/30 19:07:44 oboehm Exp $
 */
public final class TestAjcXml extends AjcTestCase {
    
	private static org.slf4j.Logger log = LoggerFactory.getLogger(TestAjcXml.class);

    /**
     * Look, if we find the Joinpoint class.
     */
	@Test
    public void testJoinpoint() {
    	URL url = TestAjcXml.class.getResource("/org/aspectj/lang/JoinPoint.class");
    	log.debug("JoinPoint is located in " + url);
    	assertNotNull(url);
    }

    /**
     * Look, if aspectjrt.jar can be found in the classpath.
     * Maybe maven fails because of missing jar.
     */
	@Test
    public void testClasspath() {
    	AjcXml compiler = new AjcXml();
    	compiler.reset();
    	String classpath = System.getProperty("java.class.path");
		assertTrue(classpath.matches(".*aspectjrt[\\-\\.\\w]*.jar.*"), "aspectjrt.jar not in " + classpath);
    }

    /**
     * Verify that the XML output of AjcCompiler.compile() is ok.
     *
     * @throws Exception on test failure
     */
	@Test
    public void testCompile() throws Exception {
        AjcXml compiler = new AjcXml();
        ByteArrayOutputStream baos = compileTest(compiler);
        log.debug("{}", baos);
        String result = StringUtils.deleteWhitespace(baos.toString());
        String begin = "<?xmlversion=\"1.0\"encoding=\"UTF-8\"?>"
                + "<patterntesting>"
                + "<patterntesting-report>"
                + "<filename=\"";
        assertEquals(result.substring(0,  begin.length()), begin);
        String end = "\">"
                + "<errorline=\"34\">Testerror</error>"
                + "<errorline=\"39\">Testerror</error>"
                + "<warningline=\"44\">Testwarning</warning>"
                + "<warningline=\"49\">Testwarning</warning>"
                + "</file>"
                + "</patterntesting-report>"
                + "</patterntesting>";
        assertEquals(result.substring(result.length() - end.length()), end);
    }

    /**
     * Check if the method assertErrorEquals() and assertWarningEquals() works
     * as expected.
     * @throws IOException in case of I/O error
     */
	@Test
	public void testAssertXxxEquals() throws IOException {
        AjcXml compiler = this.getCompiler();
        compileTest(compiler);
        String testFileName = getTestFileName();
        assertErrorEquals(testFileName, 34, "Test error");
        assertErrorEquals(testFileName, 39, "Test error");
        assertWarningEquals(testFileName, 44, "Test warning");
        assertWarningEquals(testFileName, 49, "Test warning");
	}

	/**
	 * @param compiler
	 * @return
	 * @throws IOException if stream can't be closed
	 */
	private static ByteArrayOutputStream compileTest(final AjcXml compiler) throws IOException {
		//compiler.setLog(new File("/tmp/ajc.log"));
        compiler.setFormatter("xml");

        compiler.addSrcdir(getBaseDir() + "/src/test/sample");
        compiler.setIncludes("patterntesting/tool/aspectj/Test.java,"
            + "patterntesting/tool/aspectj/TestAspect.aj");

        compiler.setDestdir(new File(getOutputDir()));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        compiler.setOutputStream(baos);

        compiler.setFailonerror(false);

        compiler.execute();
        baos.close();
        log.debug(baos.toString());
		return baos;
	}

	/**
	 * @return
	 */
	private String getTestFileName() {
		String testFileName = getTestSrcDir().toString()
        	+ "/patterntesting/tool/aspectj/Test.java";
        testFileName = testFileName.replace('/', File.separatorChar);
        testFileName = testFileName.replace('\\', File.separatorChar);
		return testFileName;
	}

	private File getTestSrcDir() {
		return new File(getBaseDir(), "src/test/sample");
	}

	/**
	 * On the website you find the following sample:
	 * <pre>
	 * AjcXml compiler = new AjcXml();
	 *
	 * compiler.createSrc().setPath("src/aspect");
	 * compiler.setDestdir("target/classes");
	 * compiler.setResultFile("result.xml");
	 *
	 * compiler.execute();
	 * </pre>
	 * And you find an example how to use it from an Ant task.
	 * But when I tried the Ant task I got the error messag
	 * <pre>
	 *    [ajcxml] 26.09.2008 05:54:44 patterntesting.tool.aspectj.AjcXmlTask checkClasspath
	 *    [ajcxml] INFO: :/usr/java/aspectj/lib/aspectjrt.jar added to java.class.path
	 *    [ajcxml] 26.09.2008 05:54:46 patterntesting.tool.aspectj.AjcErrorHandler write
	 *    [ajcxml] WARNUNG: can't write result
	 *    [ajcxml] java.io.FileNotFoundException: /tmp/patterntesting/patterntesting.xml (No such file or directory)
	 *    [ajcxml] 	at java.io.FileOutputStream.open(Native Method)
	 *    [ajcxml] 	at java.io.FileOutputStream.&lt;init&gt;(FileOutputStream.java:179)
	 *    [ajcxml] 	at java.io.FileOutputStream.&lt;init&gt;(FileOutputStream.java:131)
	 *    [ajcxml] 	at patterntesting.tool.aspectj.AjcErrorHandler.write(AjcErrorHandler.java:74)
	 *    [ajcxml] 	at patterntesting.tool.aspectj.AjcErrorHandler.handleMessage(AjcErrorHandler.java:109)
	 *    [ajcxml] 	at org.aspectj.bridge.CountingMessageHandler.handleMessage(CountingMessageHandler.java:61)
	 *    ...
	 * </pre>
	 * So I want to test if the same problems occurs if started from a Java
	 * application.
	 * @throws IOException if log file can't be written
	 */
	@Test
	public void testSample() throws IOException {
		cleanTmpDir();
		File srcDir = getTestSrcDir();
		File destDir = ExtendedFile.getTmpdir("classes");
		File resultFile = File.createTempFile("result", ".xml");
		try {
    		startSample(srcDir, destDir, resultFile);
    		assertTrue(resultFile.exists(), resultFile + " missing");
		} finally {
		    log.info("deleting " + resultFile + "...");
		    if (!resultFile.delete()) {
		        log.warn(resultFile + " can't be deleted");
		    }
		}
	}

	private void startSample(final File srcDir, final File destDir, final File resultFile) {
		AjcXml compiler = new AjcXml();
		compiler.createSrc().setPath(srcDir.toString());
		compiler.setDestdir(destDir.toString());
		compiler.setResultFile(resultFile.toString());
		compiler.setIncludes("patterntesting/plugin/**");
		compiler.execute();
	}

	/**
	 * Remove garbage from other tests, e.g. the /tmp/patterntesting directory.
	 * 
	 * @throws IOException in case of I/O errpr
	 */
	private void cleanTmpDir() throws IOException {
		File dir = ExtendedFile.getTmpdir("patterntesting");
		FileUtils.deleteDirectory(dir);
	}

}
