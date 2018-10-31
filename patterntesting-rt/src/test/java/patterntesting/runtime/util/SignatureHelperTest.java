/**
 * $Id: SignatureHelperTest.java,v 1.4 2016/10/04 21:00:42 oboehm Exp $
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
 * (c)reated 09.04.2009 by oliver (ob@aosd.de)
 */
package patterntesting.runtime.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.aspectj.lang.Signature;
import org.junit.jupiter.api.Test;

/**
 * The Class SignatureHelperTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.4 $
 * @since 09.04.2009
 */
public final class SignatureHelperTest {

    /**
     * Test method for {@link SignatureHelper#getAsSignature(String)}.
     *
     * @throws ReflectiveOperationException if class or method was not found
     */
    @Test
    public final void testGetAsSignatureString() throws ReflectiveOperationException {
        Signature sig = SignatureHelper
                .getAsSignature("new java.lang.String()");
        assertEquals(String.class, sig.getDeclaringType());
    }

}
