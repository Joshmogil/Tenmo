package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component

public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Transfer getSingleTransfer(long transfer_id){
        Transfer transfer = null;
        String sql = "SELECT *, " +

        "(SELECT accounts.user_id " +
        "FROM transfers " +
        "JOIN accounts on transfers.account_from = accounts.account_id " +
        "WHERE transfers.transfer_id = ?) AS from_user_id, " +

        "(SELECT accounts.user_id " +
        "FROM transfers " +
        "JOIN accounts on transfers.account_to = accounts.account_id " +
        "WHERE transfers.transfer_id = ?) AS to_user_id, " +

        "(SELECT transfer_types.transfer_type_desc " +
        "FROM transfers " +
        "JOIN transfer_types ON transfer_types.transfer_type_id = transfers.transfer_type_id " +
        "WHERE transfers.transfer_id = ?) AS transfer_type, " +

        "(SELECT transfer_statuses.transfer_status_desc " +
        "FROM transfers " +
        "JOIN transfer_statuses ON transfer_statuses.transfer_status_id = transfers.transfer_status_id " +
        "WHERE transfers.transfer_id = ?) AS transfer_status, " +

        "(SELECT users.username "+
        "FROM users " +
        "JOIN accounts ON users.user_id = accounts.user_id " +
        "JOIN transfers ON accounts.account_id = transfers.account_from "+
        "WHERE transfers.transfer_id = ?) AS username_from, "+

        "(SELECT users.username "+
        "FROM users " +
        "JOIN accounts ON users.user_id = accounts.user_id " +
        "JOIN transfers ON accounts.account_id = transfers.account_to "+
        "WHERE transfers.transfer_id = ?) AS username_to " +

        "FROM transfers " +

        "WHERE transfer_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transfer_id, transfer_id, transfer_id, transfer_id, transfer_id , transfer_id, transfer_id);
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        }
        return transfer;
    }

    @Override
    public List<Transfer> getAllTransfers(long user_id){

        List<Transfer> transfers = new ArrayList<>();

        String sql = "SELECT transfers.*, a.user_id AS from_user_id, b.user_id AS to_user_id, " +
         "transfer_statuses.transfer_status_desc AS transfer_status, " +
        "transfer_types.transfer_type_desc AS transfer_type, c.username AS username_from, " +
        "d.username AS username_to " +
        "FROM transfers " +
        "JOIN accounts a ON transfers.account_from = a.account_id " +
        "JOIN accounts b ON transfers.account_to = b.account_id " +
        "JOIN transfer_statuses ON transfers.transfer_status_id = transfer_statuses.transfer_status_id " +
        "JOIN transfer_types ON transfers.transfer_type_id = transfer_types.transfer_type_id " +
        "JOIN users c ON a.user_id = c.user_id " +
        "JOIN users d ON b.user_id = d.user_id " +
        "WHERE c.user_id = ? OR d.user_id = ? ";


        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, user_id, user_id);

        while(results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;

    }

    @Override
    public Transfer createTransfer( long from_user_id,long to_user_id, BigDecimal amount){



        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount)\n" +
                "VALUES (2, 2, (SELECT account_id from accounts WHERE user_id = ?) , (SELECT account_id from accounts WHERE user_id = ?), ?) RETURNING transfer_id ";

        long newTransferId = -1;

        try {
           newTransferId = jdbcTemplate.queryForObject(sql, Long.class, from_user_id, to_user_id, amount);

            System.out.println(newTransferId);
        }catch (DataAccessException e) {
            System.out.println("Transfer creation failed");
        }

        runTransaction(getSingleTransfer(newTransferId));

        return getSingleTransfer(newTransferId);

    }

    @Override
    public boolean runTransaction(Transfer transfer){

        boolean transactionSuccessful = false;

        long userFrom = transfer.getUser_id_From();
        long userTo = transfer.getUser_id_To();
        BigDecimal amount = transfer.getAmount();
        String transferStatus = transfer.getTransfer_status();
        String transferType = transfer.getTransfer_type();

        BigDecimal balance = getAccountBalance(transfer.getUser_id_From());

        if(balance.compareTo(amount) >=0){
            addToBalance(userTo, amount);
            subtractFromBalance(userFrom, amount);
            transactionSuccessful = true;
        } else{
            deleteTransfer(transfer.getTransfer_id());

        }

        return transactionSuccessful;

    }

    @Override
    public BigDecimal getAccountBalance(long user_id){

        Account account = null;

        String sql = "SELECT account_id, user_id, balance " +
                "FROM accounts " +
                "WHERE user_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, user_id);
        if (results.next()) {
            account = mapRowToAccount(results);
        }
        return account.getBalance();

    }

    @Override
    public void addToBalance(long user_id,BigDecimal amount){

        String sql = "UPDATE accounts " +
                "SET balance = balance + ? " +
                "WHERE user_id = ?;";
        jdbcTemplate.update(sql, amount, user_id);

    }

    @Override
    public void subtractFromBalance(long user_id,BigDecimal amount){

        String sql = "UPDATE accounts " +
                "SET balance = balance - ? " +
                "WHERE user_id = ?;";
        jdbcTemplate.update(sql, amount, user_id);

    }

    @Override
    public void deleteTransfer(Long transfer_id) {
        String sql = "DELETE FROM transfers WHERE transfer_id = ?";
        jdbcTemplate.update(sql, transfer_id);
    }


    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();

        account.setAccount_id(rowSet.getLong("account_id"));
        account.setUser_id(rowSet.getLong("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));

        return account;
    }


    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();

        transfer.setTransfer_id(rowSet.getLong("transfer_id"));
        transfer.setTransfer_type_id(rowSet.getInt("transfer_type_id"));
        transfer.setTransfer_status_id(rowSet.getInt("transfer_status_id"));
        transfer.setAccount_from(rowSet.getLong("account_from"));
        transfer.setAccount_to(rowSet.getLong("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        transfer.setUser_id_From(rowSet.getLong("from_user_id"));
        transfer.setUser_id_To(rowSet.getLong("to_user_id"));
        transfer.setTransfer_type(rowSet.getString("transfer_type"));
        transfer.setTransfer_status(rowSet.getString("transfer_status"));
        transfer.setUsername_from(rowSet.getString("username_from"));
        transfer.setUsername_to(rowSet.getString("username_to"));

        return transfer;
    }



}


