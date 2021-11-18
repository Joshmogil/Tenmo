package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private long transfer_id;
    private int transfer_type_id;
    private int transfer_status_id;
    private long account_from;
    private long account_to;
    private BigDecimal amount;
    private long user_id_From;
    private long user_id_To;
    private String transfer_type;
    private String transfer_status;
    private String username_from;
    private String username_to;

    public long getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(long transfer_id) {
        this.transfer_id = transfer_id;
    }

    public int getTransfer_type_id() {
        return transfer_type_id;
    }

    public void setTransfer_type_id(int transfer_type_id) {
        this.transfer_type_id = transfer_type_id;
    }

    public int getTransfer_status_id() {
        return transfer_status_id;
    }

    public void setTransfer_status_id(int transfer_status_id) {
        this.transfer_status_id = transfer_status_id;
    }

    public long getAccount_from() {
        return account_from;
    }

    public void setAccount_from(long account_from) {
        this.account_from = account_from;
    }

    public long getAccount_to() {
        return account_to;
    }

    public void setAccount_to(long account_to) {
        this.account_to = account_to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public long getUser_id_From() {
        return user_id_From;
    }

    public void setUser_id_From(long user_id_From) {
        this.user_id_From = user_id_From;
    }

    public long getUser_id_To() {
        return user_id_To;
    }

    public void setUser_id_To(long user_id_To) {
        this.user_id_To = user_id_To;
    }

    public String getTransfer_type() {
        return transfer_type;
    }

    public void setTransfer_type(String transfer_type) {
        this.transfer_type = transfer_type;
    }

    public String getTransfer_status() {
        return transfer_status;
    }

    public void setTransfer_status(String transfer_status) {
        this.transfer_status = transfer_status;
    }

    public String getUsername_from() {
        return username_from;
    }

    public void setUsername_from(String username_from) {
        this.username_from = username_from;
    }

    public String getUsername_to() {
        return username_to;
    }

    public void setUsername_to(String username_to) {
        this.username_to = username_to;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transfer_id=" + transfer_id +
                ", transfer_type_id=" + transfer_type_id +
                ", transfer_status_id=" + transfer_status_id +
                ", account_from=" + account_from +
                ", account_to=" + account_to +
                ", amount=" + amount +
                ", user_id_From=" + user_id_From +
                ", user_id_To=" + user_id_To +
                ", transfer_type='" + transfer_type + '\'' +
                ", transfer_status='" + transfer_status + '\'' +
                ", username_from='" + username_from + '\'' +
                ", username_to='" + username_to + '\'' +
                '}';
    }
}