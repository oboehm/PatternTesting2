/*
 * Copyright (c) 2012-2020 by Oliver Boehm
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
 * (c)reated 28.12.2011 by oliver (ob@oasd.de)
 */

package patterntesting.sample.intro;

/**
 * This interface is only an example. It was orginally introduced as an example
 * for a Spring JUnit test.
 * 
 * @author oliver
 * @since 1.2 (28.12.2011)
 */
public interface Cryptable {

    /**
     * Sets the message which should be crypted.
     *
     * @param s the new message
     */
    void setMessage(final String s);

    /**
     * We return a crypted message.
     *
     * @return the message encryped
     */
    String crypt();

}
