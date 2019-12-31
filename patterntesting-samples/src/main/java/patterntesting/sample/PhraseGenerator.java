/*
 * $Id: PhraseGenerator.java,v 1.5 2016/01/06 20:47:37 oboehm Exp $
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
 * (c)reated 19.06.2009 by oliver (ob@aosd.de)
 */
package patterntesting.sample;

import patterntesting.annotation.check.ct.OnlyForTesting;

import java.io.*;

/**
 * This is a example class for the use the IO classes like Reader or Writer.
 * It shows also how to use the @SuppressStreamWarning annotation.
 * <p>
 * Normally you should use the BufferedWriter and BufferedReader class for
 * writing and reading. But because the phrase is not very long we haven't
 * done it.
 * </p>
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.5 $
 * @since 19.06.2009
 */
public final class PhraseGenerator {

    /** This is the "generated" default phrase. */
    private String phrase = "hello world\n";

    /**
     * Gets the phrase.
     *
     * @return the (generated default) phrase
     */
    public String getPhrase() {
        return phrase;
    }

    /**
     * Sets the phrase.
     *
     * @param s the new phrase
     */
    @OnlyForTesting
    protected void setPhrase(final String s) {
        this.phrase = s.trim();
    }

    /**
     * You should not use an OutputStream to write a String to a file.
     * Why? Because the String is converted into bytes and this depends on
     * the encoding of your environment. This encoding may be different from
     * the encoding where the file is stored. So use better a Writer class
     * instead (remember, this is only an example!)
     *
     * @param file the file
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void writeToStream(final File file) throws IOException {
        try (OutputStream ostream = new FileOutputStream(file)) {
            ostream.write(phrase.getBytes());
        }
    }

    /**
     * You should not use an InputStream to read a String from a file.
     * The reason is the same as for {@link #writeToStream(File)}.
     *
     * @param file the file
     *
     * @return the phrase read from the given file
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public String readFromStream(final File file) throws IOException {
        try (InputStream istream = new FileInputStream(file)) {
            byte[] buffer = new byte[(int) file.length()];
            if (istream.read(buffer) >= 0) {
                phrase = new String(buffer);
            }
        }
        return phrase;
    }

    /**
     * This method has the same functionality als {@link #writeToStream(File)}
     * except that it use the Writer class.
     *
     * @param file the file
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void writeTo(final File file) throws IOException {
        try (Writer writer = new FileWriter(file)) {
            writer.write(phrase);
        }
    }

    /**
     * Read from.
     *
     * @param file the file where to read from.
     *
     * @return the string
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public String readFrom(final File file) throws IOException {
        try (Reader reader = new FileReader(file)) {
            char[] buffer = new char[(int) file.length()];
            reader.read(buffer);
            phrase = new String(buffer).trim();
        }
        return phrase;
    }

    /**
     * The problem with the RandomAccessFile is the writeChars() method - it
     * will write each charactor using two bytes. So if you want to read the
     * String again from the file you have to use again the RandomAccessFile.
     *
     * @param file the file
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void writeToRandomAccessFile(final File file) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            raf.writeChars(phrase);
        }
    }

    /**
     * Read from a RandomAccessFile.
     *
     * @param file the file
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public String readFromRandomAccessFile(final File file) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            StringBuffer buffer = new StringBuffer();
            while (true) {
                try {
                    char c = raf.readChar();
                    buffer.append(c);
                    if (c == 0) {
                        break;
                    }
                } catch (EOFException eofe) {
                    break;
                }
            }
            phrase = buffer.toString();
        }
        return phrase;
    }

}
