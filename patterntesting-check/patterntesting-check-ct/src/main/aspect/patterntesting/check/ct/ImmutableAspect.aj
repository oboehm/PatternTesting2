/**
 * Copyright (c) 2008-2019 by Oliver Boehm
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
 * (c)reated 29.09.2008 by oliver (ob@oasd.de)
 */
package patterntesting.check.ct;

import javax.annotation.concurrent.Immutable;

/**
 * Classes annotated with "@Immutable" are considered as immutable.
 *
 * @see AbstractImmutableAspect
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 29.09.2008
 * @version $Revision: 1.1 $
 */
public aspect ImmutableAspect extends AbstractImmutableAspect {

    /**
     * Classes annotated with "@Immutable" are considered as immutable.
     */
    public pointcut applicationCode(): within(@Immutable *);

}
