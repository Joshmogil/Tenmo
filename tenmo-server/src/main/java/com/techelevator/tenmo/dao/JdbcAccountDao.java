package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

//    public JdbcAccountDao(DataSource dataSource) {
//        this.jdbcTemplate = new JdbcTemplate(dataSource);
//    }

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccount(long user_id){

        Account account = null;

        String sql = "SELECT account_id, user_id, balance " +
                     "FROM accounts " +
                     "WHERE user_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, user_id);
        if (results.next()) {
            account = mapRowToAccount(results);
        }
        return account;
    }

    //delete these later
    @Override
    public void addToAccountBalance(BigDecimal amount, long user_id){

        String sql = "UPDATE accounts " +
                "SET balance = balance + ? " +
                "WHERE user_id = ?;";
        jdbcTemplate.update(sql, amount, user_id);

    }

    @Override
    public void subtractFromAccountBalance(BigDecimal amount,long user_id){

        String sql = "UPDATE accounts " +
                "SET balance = balance - ? " +
                "WHERE user_id = ?;";
        jdbcTemplate.update(sql, amount, user_id);

    };

    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();

        account.setAccount_id(rowSet.getLong("account_id"));
        account.setUser_id(rowSet.getLong("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));

        return account;
    }

}
