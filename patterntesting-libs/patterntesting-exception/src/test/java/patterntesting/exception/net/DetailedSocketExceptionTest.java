/*
 * Copyright (c) 2019 by Oliver Boehm
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * (c)reated 29.05.2019 by oboehm (ob@oasd.de)
 */
package patterntesting.exception.net;

import org.junit.jupiter.api.Test;

import java.net.SocketException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link DetailedSocketException}.
 */
class DetailedSocketExceptionTest {

    @Test
    void getCause() {
        Throwable cause = new IllegalStateException("bumm");
        SocketException ex = new DetailedSocketException("broken", cause);
        assertEquals(cause, ex.getCause());
    }

    @Test
    void of() {
        SocketException orig = new SocketException("not connected");
        String host = "testhost";
        SocketException better = DetailedSocketException.of(orig, host, 4711);
        assertThat(better.getMessage(), containsString(host));
    }

}
