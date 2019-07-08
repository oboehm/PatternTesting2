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
 * (c)reated 15.03.2009 by oliver (ob@aosd.de)
 */
package patterntesting.annotation.check.ct;

import java.lang.annotation.*;

/**
 * If PatternTesting reports a warning about the improber use of JUnit you can
 * suppress the warning with this annotation. You can put this annotation in
 * front of a method. If you annotate a class all warnings will be suppressed.
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 15.03.2009
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface SuppressJUnitWarning {

}
