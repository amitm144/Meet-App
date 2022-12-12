package com.superapp.boundaries.data.split;

import com.superapp.boundaries.data.UserEntity;
import com.superapp.boundaries.data.Group.GroupEntity;

import java.util.Date;

public class SplitTransaction {

    private GroupEntity group;
    private UserEntity userPaid;
    private Date timestamp;
    private String description;
    private double balance;
    private double percentageToBeReturned;
    private double percentageEachPay;

    public SplitTransaction(GroupEntity group, UserEntity user, Date timestamp, String description, double balance) { //balance must be greater than 0
        this.group = group;
        this.userPaid = user;
        this.timestamp = timestamp;
        this.description = description;
        this.balance = balance;
        this.percentageToBeReturned = group.getMembers().size()-1/group.getMembers().size();
        this.percentageEachPay = -1/group.getMembers().size();
        //TODO ID - auto incersment
    }

    public UserEntity getUserPaid() {
        return userPaid;
    }

    public void setUserPaid(UserEntity user) {
        this.userPaid = userPaid;
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

    public GroupEntity getGroup() {
        return group;
    }

    public void setGroup(GroupEntity group) {
        this.group = group;
    }

    public double getPercentageToBeReturned() {
        return percentageToBeReturned;
    }

    public void setPercentageToBeReturned(double percentageToBeReturned) {
        this.percentageToBeReturned = percentageToBeReturned;
    }

    public double getPercentageEachPay() {
        return percentageEachPay;
    }

    public void setPercentageEachPay(double percentageEachPay) {
        this.percentageEachPay = percentageEachPay;
    }

    @Override
    public String toString() {
        return "$"+balance+"/"+group.getMembers()+" per member || "+ getDescription() + "|| paid by "+ getUserPaid() + "|| At " + getTimestamp();
    }
}
