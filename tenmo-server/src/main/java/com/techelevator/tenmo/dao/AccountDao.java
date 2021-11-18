package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

Account getAccount(long user_id);

void addToAccountBalance(BigDecimal amount, long user_id);

void subtractFromAccountBalance(BigDecimal amount,long user_id);

}
