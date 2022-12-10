package com.superapp.boundaries.data.split;

import com.superapp.boundaries.data.UserEntity;

import java.util.Date;

public class SplitTransaction {

    private String groupId;
    private UserEntity user;
    private Date timestamp;
    private String description;
    private double balance;

    public SplitTransaction(UserEntity user, Date timestamp, String description, double balance) {
        this.user = user;
        this.timestamp = timestamp;
        this.description = description;
        this.balance = balance;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }


}
