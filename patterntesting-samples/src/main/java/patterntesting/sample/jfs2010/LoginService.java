/*
 * Copyright (c) 2010-2020 by Oliver Boehm
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
 * (c)reated 16.04.2010 by oliver (ob@oasd.de)
 */

package patterntesting.sample.jfs2010;

import java.io.IOException;
import java.util.Arrays;

import javax.security.auth.login.LoginException;

import org.slf4j.*;

import patterntesting.annotation.exception.TestException;

/**
 * The Class LoginService.
 *
 * @author oliver
 * @since 1.0 (16.04.2010)
 */
public final class LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginService.class);
    private static final String[] KNOWN_LOGINS = { "ninthbit", "oboehm" };

    /** There is no need to instantiate this utility class. */
    private LoginService() {}

    /**
     * This is an example of a (bad designed) login method.
     * If the login fails the returned user will have a null name!
     *
     * @param name the user name, e.g. "oboehm"
     * @param passwd will be ignored in this implementation
     * @return the user
     */
    @TestException
    public static User login1(final String name, final String passwd) {
        User user = null;
        if (!Arrays.asList(KNOWN_LOGINS).contains(name)) {
            user = new User(name);
        }
        return user;
    }

    /**
     * This is an example of a login method.
     * If the login fails a LoginException will now be thrown!
     * <p>
     * NOTE: For Chuck Norris the login will be ALWAYS successful!
     * </p>
     *
     * @param name the user name, e.g. "oboehm"
     * @param passwd will be ignored in this implementation
     * @return the user
     * @throws LoginException the login exception
     */
    @TestException
    public static User login(final String name, final String passwd) throws LoginException {
        if (!name.equals("Chuck Norris")) {
            if (!Arrays.asList(KNOWN_LOGINS).contains(name)) {
                throw new LoginException(name + ": login failed");
            }
        }
        return new User(name);
    }

}

