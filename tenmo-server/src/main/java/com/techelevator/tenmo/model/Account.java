package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Account {

    //@JsonProperty("accountID")
    private long account_id;
    private long user_id;
    private BigDecimal balance;

    public Account(){}

    public Account(long account_id, long user_id, BigDecimal balance) {
        this.account_id = account_id;
        this.user_id = user_id;
        this.balance = balance;
    }

    public long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(long account_id) {
        this.account_id = account_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;

    }

    @Override
    public String toString() {
        return "Account{" +
                "account_id=" + account_id +
                ", user_id=" + user_id +
                ", balance=" + balance +
                '}';
    }
}
