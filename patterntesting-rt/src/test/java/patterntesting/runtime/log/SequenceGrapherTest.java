/*
 * $Id: SequenceGrapherTest.java,v 1.32 2016/12/27 07:40:46 oboehm Exp $
 *
 * Copyright (c) 2013 by Oliver Boehm
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
 * (c)reated 08.09.2013 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.log;

import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.jupiter.api.Test;
import patterntesting.runtime.junit.FileTester;
import patterntesting.runtime.mock.JoinPointStaticPartMock;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * JUnit tests for {@link SequenceGrapher} class.
 *
 * @author oliver
 * @since 1.3.1 (08.09.2013)
 */
public final class SequenceGrapherTest {

    private static final Logger LOG = LoggerFactory.getLogger(SequenceGrapherTest.class);
    private static final Pattern[] IGNORED_LINES = { Pattern.compile("#.*"),
            Pattern.compile("[ \\t]*"), Pattern.compile("boxwid = .*"), Pattern.compile("step().*") };

    /**
     * Test method for {@link SequenceGrapher#SequenceGrapher(File)}.
     * For this test we create an empty diagram to see if the header and footer
     * in the generated file looks ok.
     */
    @Test
    public void testCreateEmptyPicFile() {
        File file = new File("target", "seq-empty.pic");
        SequenceGrapher grapher = new SequenceGrapher(file);
        grapher.close();
        FileTester.assertContentEquals(new File(
                "src/test/resources/patterntesting/runtime/log/seq-empty.pic"), file,
                IGNORED_LINES);
    }

    /**
     * Test method for {@link SequenceGrapher#SequenceGrapher(File)} with a text
     * file as parameter. For this test we create an empty diagram to see the
     * generated file looks ok.
     */
    @Test
    public void testCreateEmptyTxtFile() {
        File file = new File("target", "seq-empty.txt");
        SequenceGrapher grapher = new SequenceGrapher(file);
        grapher.close();
        FileTester.assertContentEquals(new File("src/test/resources/patterntesting/runtime/log/seq-empty.txt"), file);
    }

    /**
     * Test method for {@link SequenceGrapher#SequenceGrapher(File)}. For this
     * test we create an empty diagram to see if the header and footer in the
     * generated file looks ok.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testCreateSmallPicFile() throws IOException {
        File file = new File("target", "seq-empty-small.pic");
        SequenceGrapher grapher = new SequenceGrapher(file);
        grapher.close();
        removeHeadFrom(file);
        FileTester.assertContentEquals(new File(
                "src/test/resources/patterntesting/runtime/log/seq-empty-small.pic"), file,
                IGNORED_LINES);
    }

    /**
     * Test method for the createMessage method.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testCreate() throws IOException {
        checkCreation("seq-create.pic", new Date());
    }

    /**
     * Test method for the createMessage method. Here we test what happens if
     * this method is called 2 times.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testCreate2() throws IOException {
        checkCreation("seq-create2.pic", new Date(1), new Date(2));
    }

    private void checkCreation(final String filename, final Object... objects) throws IOException {
        File file = new File("target", filename);
        SequenceGrapher grapher = new SequenceGrapher(file);
        for (Object object : objects) {
            grapher.createMessage(this, object, new JoinPointStaticPartMock());
        }
        for (Object object : objects) {
            grapher.message(this, object, new JoinPointStaticPartMock("getTime"), new Object[0]);
        }
        grapher.close();
        removeHeadFrom(file);
        FileTester.assertContentEquals(new File(
                "src/test/resources/patterntesting/runtime/log", filename), file, IGNORED_LINES);
    }

    /**
     * Test method for the createMessage method with an object array.
     * This is a test for just one call with one return value.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testCreateMessage() throws IOException {
        File file = new File("target", "seq-create-message.pic");
        SequenceGrapher grapher = new SequenceGrapher(file);
        Object target = new Date(3L);
        grapher.createMessage(this, target, new JoinPointStaticPartMock());
        grapher.message(this, target, new JoinPointStaticPartMock("getTime"), new Object[0]);
        grapher.returnMessage(target, 4711, new JoinPointStaticPartMock());
        grapher.close();
        removeHeadFrom(file);
        FileTester.assertContentEquals(new File(
                "src/test/resources/patterntesting/runtime/log/seq-create-message.pic"), file,
                IGNORED_LINES);
    }

    private static void removeHeadFrom(final File file) throws IOException {
        List<String> lines = FileUtils.readLines(file, "ISO-8859-1");
        lines.subList(0, 421).clear();
        FileUtils.writeLines(file, "ISO-8859-1", lines);
    }

    /**
     * Test method for the createMessage method with an object array.
     * This is a test for just one call with one return value. The different to
     * {@link #testCreateMessage()} is that the object, where the message will
     * be sent, is not created before. This is e.g. a typically situation for
     * the call of static methods (et al) or if the creation of the object was
     * not part of the recorded sequence diagram.
     *
     * @throws AssertionError the assertion error
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testMessage() throws AssertionError, IOException {
        checkMessage("seq-message.pic", 4712);
    }

    /**
     * Test method for the createMessage method and for bug 25.
     *
     * @throws AssertionError the assertion error
     * @throws IOException Signals that an I/O exception has occurred.
     * @since 1.4.1 (08.01.2014)
     */
    @Test
    public void testReturnMessage() throws AssertionError, IOException {
        checkMessage("seq-return-message.pic", "12:34 \"MEZ\"");
    }

    private void checkMessage(final String filename, final Object returnValue)
            throws AssertionError, IOException {
        File file = new File("target", filename);
        SequenceGrapher grapher = new SequenceGrapher(file);
        startSequenceDiagram(grapher, returnValue);
        grapher.close();
        removeHeadFrom(file);
        FileTester.assertContentEquals(new File("src/test/resources/patterntesting/runtime/log",
                filename), file, IGNORED_LINES);
    }

    private void startSequenceDiagram(final SequenceGrapher grapher, final Object returnValue) {
        Object target = new Date(4L);
        grapher.message(this, target, new JoinPointStaticPartMock("getTime"), new Object[0]);
        grapher.returnMessage(target, returnValue, new JoinPointStaticPartMock());
    }

    /**
     * Test method for {@link SequenceGrapher#setExcludeFilter(String[])}.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testSetExcludeFilter() throws IOException {
        File file = new File("target", "seq-exclude-filter.pic");
        SequenceGrapher grapher = new SequenceGrapher(file);
        SequenceDiagramTest sdt = new SequenceDiagramTest();
        String[] excludePatterns = { sdt.getClass().getName() };
        grapher.setExcludeFilter(excludePatterns);
        startSequenceDiagram(grapher, 4712);
        grapher.message(this, sdt, new JoinPointStaticPartMock("testPingPong"), new Object[0]);
        grapher.returnMessage(sdt, "", new JoinPointStaticPartMock());
        grapher.message(sdt, sdt, new JoinPointStaticPartMock("playPingPong"), new Object[0]);
        grapher.returnMessage(sdt, "", new JoinPointStaticPartMock());
        grapher.close();
        removeHeadFrom(file);
        FileTester.assertContentEquals(new File(
                "src/test/resources/patterntesting/runtime/log/seq-message.pic"), file,
                IGNORED_LINES);
    }

    /**
     * Test method for {@link SequenceGrapher#toString()}.
     */
    @Test
    public void testToString() {
        try (SequenceGrapher grapher = new SequenceGrapher()) {
            String s = grapher.toString();
            assertFalse(s.contains("@"), "looks like default implementation: " + s);
            LOG.info("s = \"{}\"", s);
        }
    }

}
