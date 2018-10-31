/**
 * $Id: ClassWalkerTest.java,v 1.2 2016/12/18 20:19:38 oboehm Exp $
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
 * (c)reated 14.04.2009 by oliver (ob@aosd.de)
 */
package patterntesting.runtime.monitor.internal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The Class ClassWalkerTest.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @version $Revision: 1.2 $
 * @since 14.04.2009
 */
public class ClassWalkerTest {

    private static final Logger log = LogManager.getLogger(ClassWalkerTest.class);
    private final File startDir = new File("target/test-classes");
    private final ClassWalker classWalker = new ClassWalker(startDir);

    /**
     * Test method for {@link ClassWalker#getClasses()}.
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public final void testGetClasses() throws IOException {
        Collection<String> classes = classWalker.getClasses();
        assertTrue(classes.size() > 0, "no classes found");
        log.info(classes.size() + " classes found in "
                + startDir.getAbsolutePath());
        log.info("{}", classes);
        String firstClass = classes.iterator().next();
        assertFalse(firstClass.startsWith("."), firstClass + " is not a classname");
    }

}
