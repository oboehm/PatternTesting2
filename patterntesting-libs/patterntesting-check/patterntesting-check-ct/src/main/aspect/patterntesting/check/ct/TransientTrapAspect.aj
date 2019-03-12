/*
 * $Id: TransientTrapAspect.aj,v 1.1 2013/04/28 16:46:03 oboehm Exp $
 *
 * Copyright (c) 2013 by Oliver Boehm
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
 * (c)reated 28.04.2013 by oliver (ob@oasd.de)
 */

package patterntesting.check.ct;

/**
 * This aspect finds class attributes which are transient <em>and</em> final.
 * After a class is serialized and deserialized transient attributes are
 * 'null'. And if such a transient attribute is final you have no chance to
 * serialize it!
 *
 * @author oliver (ob@aosd.de)
 * @since 1.3.1 (28.04.2013)
 * @version $Revision: 1.1 $
 */
public aspect TransientTrapAspect {

    declare warning: set(transient final * *..*) :
        "transient trap - don't use transient and final together!";

}

