/*
 * Copyright (c) 2015 by Oli B.
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
 * (c)reated 14.06.2015 by Oli B. (boehm@javatux.de)
 */

package patterntesting.runtime.log.internal;

import java.io.File;

import org.aspectj.lang.Signature;
import org.junit.Test;

import patterntesting.runtime.junit.FileTester;
import patterntesting.runtime.log.SequenceDiagramTest;
import patterntesting.runtime.log.test.*;
import patterntesting.runtime.mock.JoinPointStaticPartMock;
import patterntesting.runtime.util.Converter;
import patterntesting.runtime.util.reflect.ConstructorSignatureImpl;

/**
 * Unit tests for {@link SequenceDiagramWriter} class.
 *
 * @author oliver (boehm@javatux.de)
 * @since 1.6 (14.06.2015)
 */
public class SequenceDiagramWriterTest {

    /**
     * Test method for {@link SequenceDiagramWriter#writeSequenceDiagram()}.
     *
     * @throws SecurityException the security exception
     * @throws NoSuchMethodException the no such method exception
     */
    @Test
    public void testWriteSequenceDiagram() throws SecurityException, NoSuchMethodException {
        File file = new File("target", "ping-pong.txt");
        SequenceDiagramWriter diagramWriter = new SequenceDiagramWriter(file);
        writePingPongDiagram(diagramWriter);
        FileTester.assertContentEquals(new File("src/test/resources/patterntesting/runtime/log/ping-pong.txt"), file);
    }

    /**
     * We use here the ping-pong example to generate (and write) the diagram.
     * For this example the following statements were recorded:
     *
     * <pre>
     * actor(S0, &quot;SequenceDiagramTest&quot;);
     * placeholder_object(P1);
     * placeholder_object(P2);
     * step();
     * active(S0);
     * create_message(S0, P1, &quot;:Player&quot;);
     * create_message(S0, P2, &quot;:Player&quot;);
     * message(S0, P2, &quot;startPingPong(3)&quot;);
     * message(P2, P1, &quot;ping(\&quot;ping\&quot;)&quot;);
     * return_message(P1, P2, &quot;pong&quot;);
     * message(P2, P1, &quot;ping(\&quot;pong\&quot;)&quot;);
     * return_message(P1, P2, &quot;ping&quot;);
     * message(P2, P1, &quot;ping(\&quot;ping\&quot;)&quot;);
     * return_message(P1, P2, &quot;pong&quot;);
     * return_message(P2, S0, &quot;&quot;);
     * </pre>
     *
     * @param diagramWriter the diagram writer
     * @throws SecurityException the security exception
     * @throws NoSuchMethodException the no such method exception
     */
    protected static void writePingPongDiagram(final SequenceDiagramWriter diagramWriter) throws SecurityException, NoSuchMethodException {
        diagramWriter.addStatement(new DrawStatement(DrawType.ACTOR, "S0", "SequenceDiagramTest"));
        Object p1 = new SequenceDiagramTest.Player();
        Signature sig = new ConstructorSignatureImpl(p1.getClass().getConstructor());
        diagramWriter.addStatement(new DrawStatement(DrawType.PLACEHOLDER_OBJECT, "P1", new JoinPointStaticPartMock(sig)));
        diagramWriter.addStatement(new DrawStatement(DrawType.PLACEHOLDER_OBJECT, "P2", new JoinPointStaticPartMock(sig)));
        diagramWriter.addStatement(new DrawStatement("S0", p1, "P1", new JoinPointStaticPartMock(sig)));
        Object p2 = new SequenceDiagramTest.Player();
        diagramWriter.addStatement(new DrawStatement("S0", p2, "P2", new JoinPointStaticPartMock(sig)));
        JoinPointStaticPartMock jpInfoStartPingPong = new JoinPointStaticPartMock("startPingPong");
        diagramWriter.addStatement(new DrawStatement("S0", "P2", jpInfoStartPingPong, Converter.toArray(3)));
        JoinPointStaticPartMock jpInfoPing = new JoinPointStaticPartMock("ping");
        diagramWriter.addStatement(new DrawStatement("P2", "P1", jpInfoPing, Converter.toArray("ping")));
        diagramWriter.addStatement(new DrawStatement("P2", "P1", (Object) "pong", jpInfoPing));
        diagramWriter.addStatement(new DrawStatement("P2", "P1", jpInfoPing, Converter.toArray("pong")));
        diagramWriter.addStatement(new DrawStatement("P2", "P1", (Object) "ping", jpInfoPing));
        diagramWriter.addStatement(new DrawStatement("P2", "P1", jpInfoPing, Converter.toArray("ping")));
        diagramWriter.addStatement(new DrawStatement("P2", "P1", (Object) "pong", jpInfoPing));
        diagramWriter.addStatement(new DrawStatement("S0", "P2", (Object) "", jpInfoStartPingPong));
        diagramWriter.writeSequenceDiagram();
        diagramWriter.close();
    }

    /**
     * Test method for {@link SequenceDiagramWriter#writeSequenceDiagram()}.
     *
     * @throws SecurityException the security exception
     * @throws NoSuchMethodException the no such method exception
     */
    @Test
    public void testWriteSequenceDiagramAddAdress() throws SecurityException, NoSuchMethodException {
        File file = new File("target", "add-address.txt");
        SequenceDiagramWriter diagramWriter = new SequenceDiagramWriter(file);
        writeAddAddressDiagram(diagramWriter);
        FileTester.assertContentEquals(new File("src/test/resources/patterntesting/runtime/log/add-address.txt"), file);
    }

    /**
     * Here we use the add-address example to genenerate the diagram. We
     * use the following statementens to reconstruct this example:
     * <pre>
     * ACTOR(S1,"SequenceDiagramTest");
     * PLACEHOLDER_OBJECT(A0,"null");
     * S1 --- &lt;&lt;create&gt;&gt; --&gt; A0
     * S1 --- call(void patterntesting.runtime.log.test.AddressBook.addAddress(Address)) --&gt; A0
     * S1 &lt;-- execution(void patterntesting.runtime.log.test.AddressBook.addAddress(Address)) --- A0
     * </pre>
     *
     * @param diagramWriter the diagram writer
     * @throws SecurityException the security exception
     * @throws NoSuchMethodException the no such method exception
     */
    protected static void writeAddAddressDiagram(final SequenceDiagramWriter diagramWriter) throws SecurityException, NoSuchMethodException {
        diagramWriter.addStatement(new DrawStatement(DrawType.ACTOR, "S1", "SequenceDiagramTest"));
        Object a0 = new AddressBook();
        Signature sig = new ConstructorSignatureImpl(a0.getClass().getConstructor());
        diagramWriter.addStatement(new DrawStatement(DrawType.PLACEHOLDER_OBJECT, "A0", new JoinPointStaticPartMock(sig)));
        diagramWriter.addStatement(new DrawStatement("S1", a0, "A0", new JoinPointStaticPartMock(sig)));
        JoinPointStaticPartMock jpInfoPing = new JoinPointStaticPartMock("addAddress");
        diagramWriter.addStatement(new DrawStatement("S1", "A0", jpInfoPing, Converter.toArray(new Address("Home"))));
        diagramWriter.addStatement(new DrawStatement("S1", "A0", (Object) "", jpInfoPing));
        diagramWriter.writeSequenceDiagram();
        diagramWriter.close();
    }

    /**
     * Test method for {@link SequenceDiagramWriter#writeSequenceDiagram()}.
     *
     * @throws SecurityException the security exception
     * @throws NoSuchMethodException the no such method exception
     */
    @Test
    public void testWriteSequenceDiagramWithId() throws SecurityException, NoSuchMethodException {
        File file = new File("target", "city.txt");
        SequenceDiagramWriter diagramWriter = new SequenceDiagramWriter(file);
        writeCityDiagram(diagramWriter);
        FileTester.assertContentEquals(new File("src/test/resources/patterntesting/runtime/log/city.txt"), file);
    }

    /**
     * Here we use the city example to genenerate the diagram.
     *
     * @param diagramWriter the diagram writer
     * @throws SecurityException the security exception
     * @throws NoSuchMethodException the no such method exception
     */
    protected static void writeCityDiagram(final SequenceDiagramWriter diagramWriter) throws SecurityException, NoSuchMethodException {
        diagramWriter.addStatement(new DrawStatement(DrawType.ACTOR, "S1", "SequenceDiagramTest"));
        Object c0 = new City(70839, "Gerlingen");
        Signature sig = new ConstructorSignatureImpl(c0.getClass().getConstructor(int.class, String.class));
        diagramWriter.addStatement(new DrawStatement(DrawType.PLACEHOLDER_OBJECT, "C0", new JoinPointStaticPartMock(sig)));
        diagramWriter.addStatement(new DrawStatement("S1", c0, "C0", new JoinPointStaticPartMock(sig)));
        JoinPointStaticPartMock jpInfoPing = new JoinPointStaticPartMock("getId");
        diagramWriter.addStatement(new DrawStatement("S1", "C0", jpInfoPing, new Object[0]));
        diagramWriter.addStatement(new DrawStatement("S1", "C0", 70839, jpInfoPing));
        diagramWriter.writeSequenceDiagram();
        diagramWriter.close();
    }

    /**
     * Test method for {@link SequenceDiagramWriter#writeSequenceDiagram()}. This
     * is an example were {@link DrawType#OBJECT} was used.
     */
    @Test
    public void testWriteSequenceDiagramWithObject() {
        File file = new File("target", "object.txt");
        SequenceDiagramWriter diagramWriter = new SequenceDiagramWriter(file);
        diagramWriter.addStatement(new DrawStatement(DrawType.OBJECT, "Oli", "L:Boehm"));
        diagramWriter.writeSequenceDiagram();
        diagramWriter.close();
    }


}

