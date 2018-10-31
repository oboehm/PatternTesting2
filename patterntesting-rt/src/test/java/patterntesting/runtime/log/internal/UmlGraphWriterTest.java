/*
 * $Id: UmlGraphWriterTest.java,v 1.10 2017/05/11 20:08:56 oboehm Exp $
 *
 * Copyright (c) 2015 by Oliver Boehm
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
 * (c)reated 08.06.2015 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.log.internal;

import static patterntesting.runtime.log.SequenceDiagramTest.IGNORED_LINES;

import java.io.File;
import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;

import patterntesting.runtime.junit.FileTester;

/**
 * Unit tests for {@link UmlGraphWriter} class.
 *
 * @author oliver
 * @version $Revision: 1.10 $
 * @since 1.6 (08.06.2015)
 */
public final class UmlGraphWriterTest extends SequenceDiagramWriterTest {

    /**
     * Test method for {@link UmlGraphWriter#writeSequenceDiagram()}. We use
     * here the ping-pong example to check the generated pic file.
     *
     * @throws SecurityException the security exception
     * @throws NoSuchMethodException the no such method exception
     */
    @Override
    @Test
    public void testWriteSequenceDiagram() throws SecurityException, NoSuchMethodException {
        File file = new File("target", "ping-pong.pic");
        UmlGraphWriter umlGraphWriter = new UmlGraphWriter(file);
        writePingPongDiagram(umlGraphWriter);
        FileTester.assertContentEquals(new File("src/test/resources/patterntesting/runtime/log/ping-pong.pic"), file,
                IGNORED_LINES);
    }

    /**
     * Test method for {@link SequenceDiagramWriter#writeSequenceDiagram()}.
     *
     * @throws SecurityException the security exception
     * @throws NoSuchMethodException the no such method exception
     */
    @Override
    @Test
    public void testWriteSequenceDiagramAddAdress() throws SecurityException, NoSuchMethodException {
        File file = new File("target", "add-address.pic");
        UmlGraphWriter umlGraphWriter = new UmlGraphWriter(file);
        writeAddAddressDiagram(umlGraphWriter);
        FileTester.assertContentEquals(new File("src/test/resources/patterntesting/runtime/log/add-address.pic"), file,
                Charset.forName("ISO-8859-1"), IGNORED_LINES);
    }

    /**
     * Test method for {@link SequenceDiagramWriter#writeSequenceDiagram()}.
     *
     * @throws SecurityException the security exception
     * @throws NoSuchMethodException the no such method exception
     */
    @Override
    @Test
    public void testWriteSequenceDiagramWithId() throws SecurityException, NoSuchMethodException {
        File file = new File("target", "city.pic");
        UmlGraphWriter umlGraphWriter = new UmlGraphWriter(file);
        writeCityDiagram(umlGraphWriter);
        FileTester.assertContentEquals(new File("src/test/resources/patterntesting/runtime/log/city.pic"), file,
                IGNORED_LINES);
    }

}

