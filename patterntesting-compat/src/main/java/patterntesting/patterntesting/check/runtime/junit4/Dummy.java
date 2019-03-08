/*
 * Copyright (c) 2009-2019 by Oliver Boehm
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
 * (c)reated 18.03.2009 by oliver (ob@aosd.de)
 */
package patterntesting.patterntesting.check.runtime.junit4;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patterntesting.annotation.check.runtime.PublicForTesting;

/**
 * This is onyl a dummy class for testing.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.6 $
 * @since 18.03.2009
 */
public final class Dummy {

    private static final Logger log = LogManager.getLogger(Dummy.class);

    private Dummy() {}

    /**
     * This method should fail if it is not (directly or indirectly) called
     * by a test method.
     */
    @PublicForTesting
    public static void hello() {
        log.info("hello");
    }

}
