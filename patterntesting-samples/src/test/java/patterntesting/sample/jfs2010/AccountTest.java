/*
 * Copyright (c) 2013-2020 by Oliver Boehm
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
 * (c)reated 17.07.2013 by oliver (ob@oasd.de)
 */

package patterntesting.sample.jfs2010;

import java.math.BigDecimal;

import org.apache.logging.log4j.*;
import org.apache.logging.log4j.LogManager;

import org.junit.jupiter.api.Test;
import patterntesting.runtime.annotation.DrawSequenceDiagram;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link Account} class.
 *
 * @author oboehm
 * @since 1.3.1 (17.07.2013)
 */
public class AccountTest {

    private static final Logger log = LogManager.getLogger(AccountTest.class);
    private final Account account = new Account(1, new User("oli"));

    /**
     * Test method for {@link Account#deposit(BigDecimal)}.
     */
    @Test
    public void testDeposit() {
        BigDecimal balance = account.getBalance();
        account.deposit(new BigDecimal(2));
        log.info("account = {}", account);
        assertEquals(balance.intValue() + 2, account.getBalance().intValue());
    }

    /**
     * Test method for {@link Account#debit(BigDecimal)}.
     */
    @Test
    public void testDebit() {
        BigDecimal balance = account.getBalance();
        BigDecimal amount = new BigDecimal("1.23");
        account.debit(amount);
        log.info("account = {}", account);
        assertEquals(balance.subtract(amount), account.getBalance());
    }

    /**
     * Test method for {@link Account#transfer(String, Account)}.
     */
    @Test
    @DrawSequenceDiagram("target/test-transfer.pic")
    public void testTransfer() {
        Account other = new Account(2, new User("r2d2"));
        account.transfer("47.11", other);
        assertEquals(new BigDecimal("47.11"), other.getBalance());
        assertEquals(new BigDecimal("-47.11"), account.getBalance());
    }

}
