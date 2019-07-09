/*
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
 * (c)reated 31.01.2009 by oliver (ob@oasd.de)
 */
package patterntesting.sample.dbc;

import static patterntesting.runtime.dbc.DbC.require;

import org.apache.logging.log4j.*;

import patterntesting.runtime.dbc.Contract;

/**
 * This is a simple example application to show the use of DbC (Design by
 * Contract).
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 0.8 (31.01.2009)
 */
public final class CoffeeAccount implements Comparable<CoffeeAccount>, Contract {

    private final String name;
    private int balance = 0;
    private static final int PRICE_PER_CUP = 25;

    /**
     * Invariant.
     *
     * @return true, if invariant
     *
     * @see Contract#invariant()
     */
    public boolean invariant() {
        return this.balance >= 0;
    }

    /**
     * Instantiates a new coffee account.
     *
     * @param name the name
     */
    public CoffeeAccount(final String name) {
        this.name = name;
    }

    /**
     * Pay in.
     *
     * @param amount the amount
     */
    public void payIn(final int amount) {
        require(amount > 0, "amount must be positive");
        this.balance += amount;
    }

    /**
     * Gets a cup of coffee.
     */
    public void getCupOfCoffee() {
        require(this.balance >= PRICE_PER_CUP, "not enough money");
        this.balance -= PRICE_PER_CUP;
        debitServiceFee();
    }

    /**
     * Debit service fee.
     */
    public void debitServiceFee() {
        this.balance -= 1;
    }

    /**
     * Compare to.
     *
     * @param other the other
     *
     * @return the int
     *
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final CoffeeAccount other) {
        return this.name.compareTo(other.name);
    }

    /**
     * Equals.
     *
     * @param obj the other
     * @return true, if successful
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof CoffeeAccount)) {
            return false;
        }
        CoffeeAccount other = (CoffeeAccount) obj;
        return this.name.equals(other.name) && (this.balance == other.balance);
    }

    /**
     * Hash code.
     *
     * @return the int
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.balance;
    }

    /**
     * To string.
     *
     * @return the string
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "CoffeeAccount for " + this.name;
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(final String[] args) {
        CoffeeAccount account = new CoffeeAccount("Hugo");
        account.payIn(40);
        account.getCupOfCoffee();
        /* here you will get a ContractViolation */
        account.getCupOfCoffee();
     }

}
