/*
 * Copyright (c) 2013-2023 by Oliver Boehm
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
 * (c)reated 07.09.2013 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.log;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import patterntesting.runtime.annotation.DrawSequenceDiagram;
import patterntesting.runtime.annotation.IgnoreForSequenceDiagram;
import patterntesting.runtime.junit.FileTester;
import patterntesting.runtime.log.test.Address;
import patterntesting.runtime.log.test.AddressBook;
import patterntesting.runtime.log.test.City;
import patterntesting.runtime.log.test.CityRepo;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This is class if for testing the generation of sequence diagrams by the
 * SequenceDiagramAspect. Also the SequenceGrapher class is tested by these
 * unit tests.
 *
 * @author oliver
 * @since 1.3.1 (07.09.2013)
 */
public class SequenceDiagramTest {

    private static final Logger log = LoggerFactory.getLogger(SequenceDiagramTest.class);

    /** The ignored lines. */
    public final static Pattern[] IGNORED_LINES = { Pattern.compile("#.*"), Pattern.compile("[ \\t]*"),
            Pattern.compile("boxwid = .*") };

    /**
     * This is more a manual test. You have do look at the gerenated sequence
     * diagram if it looks ok.
     */
    @Test
    @DrawSequenceDiagram("target/test-creation.pic")
    public void testCreation() {
        City esslingen = new City("Esslingen");
        log.info("Created: {}", esslingen);
    }

    /**
     * This is the test case for bug #32 (NPE with @DrawSequenceDiagram).
     *
     * @see <a href="https://sourceforge.net/p/patterntesting/bugs/32/">bug
     *      #32</a>
     * @since 1.6 (27.05.2015)
     */
    @Test
    @DrawSequenceDiagram
    public void testGenerationWithoutFilename() {
        log.info("testGenerationWithoutFilename() is finished.");
    }

    /**
     * Test ping pong.
     */
    @Test
    public void testPingPong() {
        playPingPong();
        FileTester.assertContentEquals(new File(
                "src/test/resources/patterntesting/runtime/log/ping-pong.pic"), new File("target",
                "test-ping-pong.pic"), IGNORED_LINES);
    }

    @DrawSequenceDiagram("target/test-ping-pong.pic")
    private static void playPingPong() {
        Player will = new Player();
        Player frank = new Player(will);
        frank.startPingPong(3);
    }

    /**
     * Here we do not call the player direct. Nevertheless we want also the
     * calls between the two players in the generated sequence diagram.
     * <p>
     * The problem seems to be a call via reflection which is typical for
     * JUnit methods. This is the reason why we call the playPingPong method
     * indirect.
     * </p>
     */
    @Test
    public void testPingPongIndirect() throws ReflectiveOperationException {
        invokeMethod(this,"playPingPong", 2);
        FileTester.assertContentEquals(new File(
                "src/test/resources/patterntesting/runtime/log/ping-pong-indirect.pic"), new File(
                "target", "ping-pong-indirect.pic"), IGNORED_LINES);
    }

    private static void invokeMethod(Object target, String name, int n) throws ReflectiveOperationException {
        Method method = target.getClass().getMethod(name, int.class);
        method.invoke(target, n);
    }

    @DrawSequenceDiagram("target/ping-pong-indirect.pic")
    public void playPingPong(final int rounds) {
        Player hugo = new Player();
        playPingPongWith(hugo, rounds);
    }

    @DrawSequenceDiagram
    private void playPingPongWith(final Player player, final int rounds) {
        Player opponent = new Player(player);
        startPingPongWith(opponent, rounds);
    }

    private static void startPingPongWith(final Player player, final int rounds) {
        player.startPingPong(rounds);
    }

    /**
     * We this method we test the {@link AddressBook} implementation in the test
     * package. But more we test the generation of the sequence diagram produced
     * by this test
     * <p>
     * Note 1: For the moment the check of the generated sequence diagram is
     * done manually.
     * </p>
     * <p>
     * Note 2: We cannot check the generated sequnce diagram in this test method
     * here because the diagram will be only closed after the method has
     * finished.
     * </p>
     */
    @Test
    @DrawSequenceDiagram("target/test-address-book.pic")
    public void testAddressBook() {
        AddressBook addressBook = new AddressBook();
        List<Address> addresses = new ArrayList<>();
        addDucktalesAddress(addresses, "Donald Duck");
        addDucktalesAddress(addresses, "Dagobert Duck");
        addresses.add(new Address("Asterix", new City("Small Village")));
        addressBook.addAddresses(addresses);
        SortedSet<Address> ducktalesAddresses = addressBook.getAddressesOf(new City("Ducktales"));
        assertEquals(2, ducktalesAddresses.size());
        assertAddress("Dagobert Duck", "Ducktales", ducktalesAddresses.first());
        assertAddress("Donald Duck", "Ducktales", ducktalesAddresses.last());
    }

    /**
     * Adds the ducktales address.
     * <p>
     * This method is public because we want to see it in the generated
     * sequence diagram. This is also the reason why it is not static.
     * </p>
     *
     * @param addresses the addresses
     * @param name the name
     */
    @DrawSequenceDiagram
    private static void addDucktalesAddress(final List<Address> addresses, final String name) {
        Address address = new Address(name);
        address.setCity("Ducktales");
        addresses.add(address);
    }

    /**
     * Assert address.
     * <p>
     * This method is public because we want to see it in the generated
     * sequence diagram.
     * </p>
     *
     * @param name the name
     * @param city the city
     * @param address the address
     */
    @IgnoreForSequenceDiagram
    public static void assertAddress(final String name, final String city, final Address address) {
        assertEquals(name, address.getName());
        assertEquals(city, address.getCity().getName());
    }

    /**
     * This is a test for a call of a method which is also marked with {@link DrawSequenceDiagram}
     * annotation. This is the test case for bug #33.
     *
     * @see <a href="http://sourceforge.net/p/patterntesting/bugs/33/">bug #33</a>
     */
    @Test
    public void testAddAddress() {
        addAddress();
        FileTester.assertContentEquals(new File(
                "src/test/resources/patterntesting/runtime/log/add-address.pic"), new File(
                "target", "test-add-address.pic"), IGNORED_LINES);
    }

    @DrawSequenceDiagram("target/test-add-address.pic")
    private void addAddress() {
        AddressBook home = new AddressBook();
        home.addAddress(new Address("Home"));
    }

    /**
     * We want to generate a sequence diagram with an object which has an
     * id to see if this id is used as "name" in the life line.
     */
    @Test
    public void testDiagramWithId() {
        generateDiagramWithId();
        FileTester.assertContentEquals(new File(
                "src/test/resources/patterntesting/runtime/log/city.pic"), new File(
                "target", "test-city.pic"), IGNORED_LINES);
    }

    @DrawSequenceDiagram("target/test-city.pic")
    private void generateDiagramWithId() {
        City gerlingen = new City(70839, "Gerlingen");
        assertEquals(70839, gerlingen.getId());
    }

    /**
     * If a call comes from a static class this class should be shown as actor.
     */
    @Test
    public void testDiagramWithActor() {
        generateDiagramWithActor();
        FileTester.assertContentEquals(new File(
                "src/test/resources/patterntesting/runtime/log/city-repo.pic"), new File(
                "target", "test-city-repo.pic"), IGNORED_LINES);
    }

    @DrawSequenceDiagram("target/test-city-repo.pic")
    private void generateDiagramWithActor() {
        City stuttgart = new City(70000, "Stuttgart");
        CityRepo.addCity(stuttgart);
        City city = CityRepo.getCity(stuttgart.getName());
        assertEquals(stuttgart, city);
    }

    // ------------------------------------------------------------------------

    /**
     * The Class Player is only a simple sample. It is needed to be able to
     * generate some sequence diagrams.
     */
    public static class Player {

        private Player opponent;

        /**
         * Instantiates a new player.
         */
        @DrawSequenceDiagram
        public Player() {
            this.opponent = this;
        }

        /**
         * Instantiates a new player.
         *
         * @param opponent the opponent
         */
        public Player(final Player opponent) {
            this.opponent = opponent;
        }

        /**
         * Sets the opponent.
         *
         * @param opponent the new opponent
         */
        public void setOpponent(final Player opponent) {
            this.opponent = opponent;
            this.opponent.setOpponent(this);
        }

        /**
         * Start ping pong.
         *
         * @param rounds the rounds
         */
        public void startPingPong(final int rounds) {
            String msg = "ping";
            for (int i = 0; i < rounds; i++) {
                msg = this.opponent.ping(msg);
            }
        }

        /**
         * Ping.
         *
         * @param msg the msg
         * @return the string
         */
        public String ping(final String msg) {
            if (msg.equalsIgnoreCase("ping")) {
                return "pong";
            }
            return "ping";
        }

        /**
         * To string.
         *
         * @return the string
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "Player";
        }

    }

}
