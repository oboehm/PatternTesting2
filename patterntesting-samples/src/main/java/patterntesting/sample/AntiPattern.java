/*
 * Copyright (c) 2012-2019 by Oliver Boehm
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
 * (c)reated 07.08.2012 by oliver (ob@oasd.de)
 */

package patterntesting.sample;

import org.apache.commons.lang3.math.Fraction;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import patterntesting.runtime.annotation.ProfileMe;
import patterntesting.runtime.monitor.ProfileStatistic;
import patterntesting.runtime.util.Converter;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

/**
 * This class contains some common anti pattern. This class can be used to
 * see which of the anti patterns can be found by FindBugs, PMD/CPD or
 * PatternTesting itself.
 *
 * @author oliver (ob@aosd.de)
 * @since 1.2.20 (07.08.2012)
 */
public final class AntiPattern implements Serializable {

    private static final long serialVersionUID = 20120811L;
    private static final Logger log = LoggerFactory.getLogger(AntiPattern.class);
    private int counter;
    /** Transitent trap - attribute can be null after deserialisation. */
    private transient final Logger log2 = LoggerFactory.getLogger(getClass());
    /** This is not a Serializable class and will cause problems with serialization. */
    private Runtime runtime;

    /**
     * We want to find methods which are never called. So we ask the
     * {@link ProfileStatistic} to do the job for us. Watch the log to see
     * the file where the result will be stored.
     */
    static {
        ProfileStatistic.addAsShutdownHook();
    }

    /**
     * Sets the runtime. We need this method to set the runtime variable to
     * show that this class here has problems with serialization.
     *
     * @param runtime the new runtime
     */
    public void setRuntime(final Runtime runtime) {
        this.runtime = runtime;
    }

    /**
     * This is an example of a method which is never called. It should be
     * detected by the {@link ProfileStatistic} as "cold" (dead) code.
     * To find unused classes you can use {@link clazzfish.monitor.ClasspathMonitor}.
     * <p>
     * Anti pattern: lava flow (also known as "dead code")
     * </p>
     */
    @ProfileMe
    public void neverCalled() {
        log.info("Hello, somebody called me (neverCalled) with runtime {}.", runtime);
        this.counter++;
    }

    /**
     * This is an example for an auto-generated try-catch block using
     * <code>e.printStackTrace()</code> for logging. You should see a warning
     * like <i>"No logging should be done using Throwable.printStackTrace()"</i>
     * if you use PatternTesting Check.CT.
     */
    public static void printStackTrace() {
        try {
            URI uri = new URI("http://patterntesting.org");
            log.info("printStackTrace: uri = {}", uri);
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Do not use <code>System.out</code> or <code>System.err</code> for
     * logging. Use Log4J, SLF4J, commons-logging or JDK logging fort that
     * task.
     * <p>
     * If you use PatternTesting CheckCT you will see <i>"No logging should
     * be done using the default output and error streams."</i> as compiler
     * warning
     */
    public static void systemOutLogging() {
        System.out.println("SystemOutLogging was called.");
    }

    /**
     * Do not throw a general excption. The problem with catching Exception is
     * that if the method you are calling later adds a new checked exception to
     * its method signature, the developer's intent is that you should handle the
     * specific new exception.
     *
     * @throws Exception the exception
     */
    public static void throwingException() throws Exception {
        log.info("ThrowingException() was called.");
    }

    /**
     * This is one of the most annoying error-handling antipatterns. Either log
     * the exception, or throw it, but never do both. Logging and throwing
     * results in multiple log messages for a single problem in the code.
     *
     * @param uri the uri
     * @return the uri
     */
    public static URI logAndThrow(final String uri) {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            log.warn("illegal argument \"{}\"", uri);
            throw new IllegalArgumentException(uri);
        }
    }

    /**
     * The thrown RuntimeException in this example destroys the stacktrace
     * because the cause is not wrapped in the thrown exception. This is
     * <b>always</b> wrong. You should put all information you have in the
     * thrown exception <em>including</em> the cause.
     */
    public static void destructiveWrapping() {
        try {
            logAndThrow("hello");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("internal error");
        }
    }

    /**
     * Null flags are variable where the value 'null' stands for an
     * exceptional behaviour. That's no good idea because normally 'null'
     * as value means, that the variable was not initialized. And that is
     * normally an error which are difficult to find if 'null' can mean
     * something else.
     *
     * @param flag the flag
     * @return the string
     */
    public static String nullFlag(final Boolean flag) {
        if (flag == null) {
            return "perhaps";
        }
        return flag.toString();
    }

    /**
     * This is an example for the use of a control flag which is normally
     * an indication for bad design. Another indication is the name of the
     * flag: although it's named "onlyJPG" it handles also GIF files.
     * This is probably a relict of an older requirement where only JPEG
     * file were allowed.
     *
     * @param filename the filename
     * @param onlyJPG the control flag
     * @return true, if successful
     */
    public static boolean controlFlag(final String filename, final boolean onlyJPG) {
        if (onlyJPG && !filename.toLowerCase().endsWith(".jpg")
                && !filename.toLowerCase().endsWith(".jpeg")
                && !filename.toLowerCase().endsWith(".gif")) {
            log.info("Not a JPEG file: {}", filename);
            return false;
        } else {
            return true;
        }
    }

    /**
     * This is the same example as {@link #controlFlag(String, boolean)}, but
     * now with a list of allowed suffixes. This is not only more readable
     * (you must not guess if 'true' or 'false' is the right parameter) it is
     * also more general. If you want to add also ".png" files to be allowed
     * just add it as paramenter.
     *
     * @param file the file
     * @param allowedSuffixes the allowed suffixes
     * @return true, if is allowed
     */
    public static boolean isAllowed(final File file, final String...allowedSuffixes) {
        for (int i = 0; i < allowedSuffixes.length; i++) {
            if (file.getName().toLowerCase().endsWith(allowedSuffixes[i])) {
                return true;
            }
        }
        log.info("Not a image file: {} (does not end with: {})", file,
                Converter.toString(allowedSuffixes));
        return false;
    }

    /**
     * Instead of return 'null' as value it is better to throw an exception
     * and let the caller decide what to do.
     *
     * @param uri the uri
     * @return the uri
     */
    public static URI logAndReturnNull(final String uri) {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            log.warn("Cannot convert \"{}\" into a URI", uri, e);
            return null;
        }
    }

    /**
     * This is similar like the "log &amp; return null" anti pattern in the
     * method before. It is better to let the caller decide what to do.
     *
     * @param uri the uri
     * @return the uri
     */
    public static URI catchAndIgnore(final String uri) {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * This is a generated method by Eclipse. E.g. if you implement the
     * Comparable interface and let Eclipse generate the interface methods
     * you'll get a default implementation for a compareTo(..) method which
     * looks like this. The problem here is that you can use the method and
     * perhaps does not notice it that always the same (sometimes correct)
     * value is returned.
     *
     * @param frac the frac
     * @return the int
     */
    public static int faultyDefaultImplementation(final Fraction frac) {
        // Auto-generated method stub
        return 0;
    }

    /**
     * This code was copied from {@link #definedEncoding(File, InputStream, OutputStream)}
     * below. Normally CPD finds such pieces of code even if the variable names
     * were changed.
     *
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    public static void copyAndPaste() throws UnsupportedEncodingException {
        byte[] ba = "world".getBytes(StandardCharsets.US_ASCII);
        String s = new String(ba, StandardCharsets.US_ASCII);
        log.info("Hello {}", s);
    }

    /**
     * There are several problems with this equals implementation here.
     * First this method should return 'false' if you call it with 'null'
     * as argument (you'll get a NullPointerException instead).
     * Second the hashCode() implementation was not overwritten!
     *
     * @param other the other
     * @return true, if successful
     */
    @Override
    public boolean equals(final Object other) {
        try {
            return this.counter == ((AntiPattern) other).counter;
        } catch (ClassCastException cce) {
            return false;
        }
    }

    /**
     * The code below relies on the default encoding of the underlying
     * platform. So the result depends on the system you are running this
     * code.
     *
     * @param file the file
     * @param istream the istream
     * @param ostream the ostream
     * @throws IOException Signals that an I/O exception has occurred.
     * @see <a href="http://www.odi.ch/prog/design/newbies.php#9">Undefined encoding</a>
     */
    public static void undefinedEncoding(final File file, final InputStream istream, final OutputStream ostream)
            throws IOException {
        Reader fr = new FileReader(file);
        Writer fw = new FileWriter(file);
        Reader ir = new InputStreamReader(istream);
        Writer ow = new OutputStreamWriter(ostream);
        byte[] ba = "world".getBytes();
        String s = new String(ba);
        log.info("Hello {}", s);
        ow.close();
        ir.close();
        fw.close();
        fr.close();
    }

    /**
     * This is an example how to define the encoding for different streams and
     * string methods.s
     *
     * @param file the file
     * @param istream the istream
     * @param ostream the ostream
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void definedEncoding(final File file, final InputStream istream, final OutputStream ostream)
            throws IOException {
        Reader fr = new InputStreamReader(new FileInputStream(file), StandardCharsets.ISO_8859_1);
        Writer fw = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.ISO_8859_1);
        Reader ir = new InputStreamReader(istream, StandardCharsets.UTF_8);
        Writer ow = new OutputStreamWriter(ostream, StandardCharsets.UTF_8);
        byte[] ba = "world".getBytes(StandardCharsets.US_ASCII);
        String s = new String(ba, StandardCharsets.US_ASCII);
        log.info("Hello {}", s);
        ow.close();
        ir.close();
        fw.close();
        fr.close();
    }

    /**
     * Umlauts can cause problems and normally they do. E.g. your build server
     * is a Linux system with other encoding you may get a compiler error
     * saying something about "undefined encoding".Or more annoying: you open
     * a source file outside your favorite Java IDE with the wrong encoding, do
     * change and save it back. All your umlauts will look strange. And perhaps
     * you do not notice it - just sometime later. But then it is probably too
     * late and your change is already in the wild and you can't track it bad
     * when it happened.
     * <p>
     * To convert the umlauts in your source as unicode you can use
     * <i>native2ascii</i> which is part of the JDK tools.s
     * </p>
     *
     * @since 1.3.1 (Oliver Boehm)
     */
    public static void umlauts() {
        System.out.println("Gr\u00fc\u00df Gott!");
    }

    /**
     * If you look at <code>log2</code> attribute at the beginning of this
     * class you see that is transient and final. So after this class will be
     * serialized and deserialized <code>log2</code> will be 'null' (because it
     * is transient). And you have no chance to serialize it (because it is
     * final).
     */
    public void transientTrap() {
        log2.info("Hello World!");
    }

    /**
     * As result of the bill below we expect "0". But that's not what we get
     * if we use float or doubles for the caluculation.
     * <p>
     * The values for this example are taken from
     * <a href="http://haupz.blogspot.de/2009/01/ich-bin-reich.html">Haupz blog</a>.
     * </p>
     * See also http://c2.com/cgi/wiki?FloatingPointCurrency.
     */
    public static void floatingPointCurrency() {
        float preTax = 32.18F;
        float tax = Math.round(preTax * 19) / 100F;
        float sum = preTax + tax;
        log.info("\tsum pre-tax   (Summe vor Steuern): {}", preTax);
        log.info("\tsales tax     (Umsatzsteuer 19%) :  {}", tax);
        log.info("\t----------------------------------------");
        log.info("\tinvoice total (Rechnungssumme)   : {}", sum);
        double paid = 38.29;
        double rest = sum - paid;
        log.info("\tpaid already  (bereits gezahlt)  : {}", paid);
        log.info("\t========================================");
        log.info("\tremaining sum (Restbetrag)       : {}", rest);
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(final String[] args) {
        System.out.println("Object.toString(): " + new Object());
    }

}

