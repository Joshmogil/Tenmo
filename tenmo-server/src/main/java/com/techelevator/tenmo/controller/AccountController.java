package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;
import java.util.zip.DataFormatException;

@RestController
@PreAuthorize("isAuthenticated()")

public class AccountController {

    private AccountDao accountDao;

    public AccountController(AccountDao accountDao) {
        this.accountDao = accountDao;

    }

//get an account object

@RequestMapping(path = "account/{id}", method = RequestMethod.GET)
public Account getAccount(@PathVariable long userid) {

    Account account = accountDao.getAccount(userid);
    return account;

}


// Return current balance



    @RequestMapping(path = "balance/{id}", method = RequestMethod.GET)
    public BigDecimal getAccountBalance(@PathVariable long id) {

        BigDecimal balance = accountDao.getAccount(id).getBalance();
        return balance;

    }




}
