/*
 * Copyright (c) 2011-2020 by Oliver Boehm
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
 * (c)reated 21.03.2011 by oliver (ob@oasd.de)
 */

package patterntesting.sample.intro;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import patterntesting.runtime.junit.CloneableTester;
import patterntesting.runtime.junit.ComparableTester;
import patterntesting.runtime.junit.ObjectTester;
import patterntesting.runtime.junit.SerializableTester;
import patterntesting.runtime.junit.extension.IntegrationTestExtension;

import java.io.NotSerializableException;


/**
 * This is an example how you can use ObjectTester and other Tester classes
 * for some general tests.
 * 
 * @author oliver
 * @since 1.1 (21.03.2011)
 */
@ExtendWith(IntegrationTestExtension.class)
public final class BaseTest {

    /**
     * Here we test the equals implementation of a whole package.
     */
    @Test
    public void testEqualsOfPackage() {
        ObjectTester.assertEquals(this.getClass().getPackage());
    }
    
    /**
     * Here we test the Comparable implementation of the {@link Rot13} class.
     */
    @Test
    public void testComparable() {
        ComparableTester.assertCompareTo(Rot13.class);
    }
    
    /**
     * Tests if all serializable classes in this package are really
     * serializable.
     *
     * @throws NotSerializableException the not serializable exception
     */
    @Test
    public final void testSerializable() throws NotSerializableException {
        SerializableTester.assertSerialization(this.getClass().getPackage());
    }
    
    /**
     * Clones all Cloneables in this package and checks the clones.
     */
    @Test
    public void testCloning() {
        CloneableTester.assertCloning(this.getClass().getPackage());
    }
    
    /**
     * And this is a test of all the previous checks together.
     * Remember, it is only a demo!
     */
    @Test
    public void testAll() {
        ObjectTester.assertAll(this.getClass().getPackage());
    }

}

