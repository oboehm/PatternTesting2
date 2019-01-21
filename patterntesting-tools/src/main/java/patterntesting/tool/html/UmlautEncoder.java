/*
 * $Id: UmlautEncoder.java,v 1.10 2016/12/30 19:07:44 oboehm Exp $
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
 * (c)reated 06.01.2011 by oliver (ob@oasd.de)
 */

package patterntesting.tool.html;

import java.io.*;
import java.nio.*;
import java.nio.charset.*;
import java.util.regex.*;

import org.apache.commons.io.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.*;

import patterntesting.runtime.util.Converter;

/**
 * Although the encoding is correct defined in an HTML file it happens
 * sometimes that Umlauts are not displayed correct. One reason for this
 * incorrect display may be the webserver itself who delivers the website
 * with a different encoding defined. To avoid such problems you can convert
 * the Umlauts into their HTML representation using this class here.
 *
 * @author oliver
 * @since 1.1 (06.01.2011)
 */
public final class UmlautEncoder {

    private static final Logger LOG = LogManager.getLogger(UmlautEncoder.class);

    /** Utility class - no need to instantiate it. */
    private UmlautEncoder() {}

    /**
     * Encode a String.
     *
     * @param input the input
     * @return the string
     */
    public static String encode(final String input) {
        char[] characters = input.toCharArray();
        StringBuilder encoded = new StringBuilder(characters.length);
        for (int i = 0; i < characters.length; i++) {
            encoded.append(encode(characters[i]));
        }
        return encoded.toString();
    }

    /**
     * Encode a single character.
     *
     * @param c a single character
     * @return the encoded character
     */
    private static String encode(final char c) {
        switch (c) {
        case '\u00e4':
            return "&auml;";
        case '\u00f6':
            return "&ouml;";
        case '\u00fc':
            return "&uuml;";
        case '\u00df':
            return "&szlig;";
        case '\u00c4':
            return "&Auml;";
        case '\u00d6':
            return "&Ouml;";
        case '\u00dc':
            return "&Uuml;";
        default:
            return Character.toString(c);
        }
    }

    /**
     * Encode a file.
     *
     * @param file the file
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void encode(final File file) throws IOException {
        encode(file, file);
    }

    /**
     * Encode.
     *
     * @param from the from
     * @param to the to
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void encode(final File from, final File to) throws IOException {
        if (from.isDirectory()) {
            encodeDir(from, to);
        } else {
            encodeFile(from, to);
        }
    }

    private static void encodeFile(final File from, final File to) throws IOException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("encoding umlauts in " + from + " to " + to + "...");
        }
        String input = readFile(from);
        String encoded = encode(input);
        FileUtils.writeStringToFile(to, encoded, StandardCharsets.UTF_8);
    }

    private static String readFile(final File file) throws IOException {
        Charset encoding = guessEncoding(file);
        byte[] bytes = FileUtils.readFileToByteArray(file);
        CharBuffer cb = encoding.decode(ByteBuffer.wrap(bytes));
        return cb.toString();
    }

    /**
     * Guess the encoding of an HTHML file. It looks for a meta tag as described
     * in <a href="http://mindprod.com/jgloss/encoding.html">encoding</a>. An
     * XML tag at the beginning will be still ignored. Also comments are ignored
     * in case a meta tag is commented out.
     * <p>
     * Note: The CharsetToolkit of
     * <a href="http://guessencoding.codehaus.org/">guessencoding</a> was used
     * before but it does not work as expected.
     * </p>
     *
     * @param file must be an HTML file
     * @return The encoding the file uses (or default if not apparent).
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static Charset guessEncoding(final File file) throws IOException {
        String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        content = deleteComments(content);
        Matcher matcher = getMatcherFor("<meta\\s+http-equiv=['\"]?content-type['\"]?[^>]+?\\bcharset=([^'\"]+)");
        matcher.reset(content);
        if (matcher.find()) {
            String charsetName = matcher.group(1);
            if (StringUtils.isNotEmpty(charsetName)) {
                return Charset.forName(charsetName);
            }
        }
        return Charset.defaultCharset();
    }

    private static String deleteComments(final String content) throws IOException {
        Matcher commentMatcher = getMatcherFor("<!--.*?-->");
        commentMatcher.reset(content);
        return commentMatcher.replaceAll("");
    }

    private static Matcher getMatcherFor(final String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher commentMatcher = pattern.matcher("");
        return commentMatcher;
    }

    private static void encodeDir(final File from, final File to) throws IOException {
        if (!to.exists()) {
            if (!to.mkdir()) {
                throw new IOException("can't create dir " + to);
            }
            LOG.debug("created: dir " + to);
        }
        File[] files = from.listFiles();
        if (files == null) {
            throw new IllegalArgumentException("not a directory: " + to);
        }
        for (int i = 0; i < files.length; i++) {
            File dest = new File(to, files[i].getName());
            if (files[i].isDirectory()) {
                encodeDir(files[i], dest);
            } else if (hasHtmlSuffix(files[i])) {
                encodeFile(files[i], dest);
            } else if (!files[i].equals(dest)) {
                FileUtils.copyFile(files[i], dest);
            }
        }
    }

    private static boolean hasHtmlSuffix(final File file) {
        String suffix = FilenameUtils.getExtension(file.getName());
        return ("html".equalsIgnoreCase(suffix)
                || "htm".equalsIgnoreCase(suffix)
                || "xhtml".equalsIgnoreCase(suffix));
    }

    /**
     * You can encode a single file (if the first argument is a file) or a
     * whole directory tree (if the first argument is a directory).
     * Optionally you can determine a destination file or directory.
     * If not a second argument is given the original file will be overwritten
     * with the encoded variant.
     *
     * @param args the src dir or file and (optionally) the destination
     */
    public static void main(final String[] args) {
        try {
            switch (args.length) {
            case 1:
                encode(new File(args[0]));
                break;
            case 2:
                encode(new File(args[0]), new File(args[1]));
                break;
            default:
                System.err.println("usage: " + UmlautEncoder.class.getName()
                        + " src-file|dir [dest-file|dir]");
                System.exit(1);
            }
        } catch (IOException ioe) {
            LOG.error("main(" + Converter.toString(args) + ") failed", ioe);
            System.err.println("command failed: " + ioe.getLocalizedMessage());
        }
    }

}

