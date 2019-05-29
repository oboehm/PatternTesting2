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

import java.net.SocketException;

/**
 * The DetailedSocketException provides (in contradiction to
 * {@link SocketException}) an constructor with a {@link Throwable} as
 * argument.
 *
 * @author oboehm
 * @since 2.0 (29.05.2019)
 */
public class DetailedSocketException extends SocketException {

    /**
     * Constructs a new {@code SocketException} with the
     * specified detail message and the given cause.
     *
     * @param msg   the detail message.
     * @param cause the cause
     */
    public DetailedSocketException(String msg, Throwable cause) {
        super(msg);
        initCause(cause);
    }

}
