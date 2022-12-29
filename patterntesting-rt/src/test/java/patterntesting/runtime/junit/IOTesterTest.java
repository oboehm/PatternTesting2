/*
 * $Id: IOTesterTest.java,v 1.4 2016/12/18 20:19:38 oboehm Exp $
 *
 * Copyright (c) 2011 by Oliver Boehm
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
 * (c)reated 30.03.2011 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.junit;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * JUnit test for IOTester.
 *
 * @author oliver
 * @since 1.1 (30.03.2011)
 */
public final class IOTesterTest {

    private static final Logger LOG = LoggerFactory.getLogger(IOTesterTest.class);

    /**
     * Test method for
     * {@link IOTester#assertContentEquals(InputStream, InputStream)} .
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testAssertContentEquals() throws IOException {
        InputStream in1 = this.getClass().getResourceAsStream("file1.txt");
        InputStream in2 = this.getClass().getResourceAsStream("file2.txt");
        try {
            IOTester.assertContentEquals(in1, in2);
        } finally {
            in1.close();
            in2.close();
        }
    }

    /**
     * Test method for
     * {@link IOTester#assertContentEquals(InputStream, InputStream)} .
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testAssertContentNotEquals() throws IOException {
        InputStream in1 = this.getClass().getResourceAsStream("file1.txt");
        InputStream in2 = this.getClass().getResourceAsStream("file3.txt");
        try {
            IOTester.assertContentEquals(in1, in2);
            fail("AssertionError expected");
        } catch (AssertionError expected) {
            String msg = expected.getMessage();
            LOG.info(msg);
            assertThat(msg, Matchers.stringContainsInOrder(
                    Arrays.asList("expected", "fourth", "line", "but was", "next", "line")));
       } finally {
            in1.close();
            in2.close();
        }
    }

}

