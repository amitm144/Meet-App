package com.superapp.data.split;

import com.superapp.data.UserEntity;
import com.superapp.data.Group.GroupEntity;

import java.util.Date;
import java.util.HashMap;

public class SplitTransaction {

    private HashMap<UserEntity,Double> groupDebts;
    private UserEntity userPaid;
    private Date timestamp;
    private String description;
    private double originalPayment;
    private double bank;
    private boolean isOpen;

    public SplitTransaction(GroupEntity group, UserEntity user, Date timestamp, String description, double originalPayment) { //balance must be greater than 0
        this.userPaid = user;
        this.timestamp = timestamp;
        this.description = description;
        this.originalPayment = originalPayment;
        this.isOpen= true;
        initGroupDebts(group);
        //TODO ID - auto incersment
    }

    private void initGroupDebts(GroupEntity group) {
        this.groupDebts = new HashMap<UserEntity,Double>();
        for (UserEntity user:group.getMembers())
            groupDebts.put(user,0.0);
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

    public double getOriginalPayment() {
        return originalPayment;
    }

    public void setOriginalPayment(double originalPayment) {
        this.originalPayment = originalPayment;
    }

    public HashMap<UserEntity, Double> getGroupDebts() {
        return groupDebts;
    }

    public void setGroupDebts(HashMap<UserEntity, Double> groupDebts) {
        this.groupDebts = groupDebts;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public double getBank() {
        return bank;
    }

    public void setBank(double bank) {
        this.bank = bank;
    }

    @Override
    public String toString() {
        return "$"+ originalPayment +"/"+this.groupDebts.keySet().size()+" per member || "+ getDescription() + "|| paid by "+ getUserPaid() + "|| At " + getTimestamp();
    }
}
