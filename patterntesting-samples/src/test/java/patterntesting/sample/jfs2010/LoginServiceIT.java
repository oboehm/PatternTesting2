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
 * (c)reated 11.05.2010 by oliver (ob@oasd.de)
 */

package patterntesting.sample.jfs2010;

import java.io.IOException;

import javax.security.auth.login.LoginException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import patterntesting.runtime.annotation.IntegrationTest;
import patterntesting.runtime.junit.extension.IntegrationTestExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * JUnit tests for {@link LoginService} class. It is also an example for the
 * use of the {@link IntegrationTestExtension} which can be used to mark a
 * test as integration test for your favourite IDE.
 * <p>
 * To enable the integration tests call this class with the option
 * <tt>-Dpatterntesting.integrationTest</tt>.
 * </p>
 *
 * @author oliver
 * @since 1.0 (11.05.2010)
 */
@ExtendWith(IntegrationTestExtension.class)
@IntegrationTest("needs internet access")
public class LoginServiceIT {

    /**
     * Test method for {@link LoginService#login(String, String)}.
     */
    @Test
    public void testLoginFailed() {
        assertThrows(LoginException.class, () -> LoginService.login("guiseppe", "segretissimo"));
    }

    /**
     * Test method for {@link LoginService#login(String, String)}.
     *
     * @throws LoginException the login exception
     */
    @Test
    public void testLoginOk() throws LoginException {
        User max = LoginService.login("oboehm", "topsecret");
        assertNotNull(max);
    }

    /**
     * Test method for {@link LoginService#login(String, String)}.
     */
    @Test
    public void testLoginNobody() {
        assertThrows(LoginException.class, () -> LoginService.login("n-o-b-o-d-y", "nopassword"));
    }

}
