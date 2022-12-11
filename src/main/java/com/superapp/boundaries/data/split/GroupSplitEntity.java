package com.superapp.boundaries.data.split;

import com.superapp.boundaries.data.UserEntity;
import com.superapp.boundaries.data.split.Group.GroupEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupSplitEntity {

    private GroupEntity group ;
    private ArrayList<SplitTransaction> expenses;
    private HashMap<UserEntity,Double> debts ;
    private double total_expenses;
    private String SplitTitle;
    private int numOfMembers;

    public GroupSplitEntity() {
    }
// TODO add DB first
//    public GroupSplitEntity(GroupEntity group) {
//        this.group = group;
//        total_expenses = 0.0;
//        this.balances = new ArrayList<SplitTransaction>();
//
//    }

    public GroupSplitEntity(GroupEntity group ,  ArrayList<SplitTransaction> expenses, String SplitTitle) {
        this.group = group;
        this.numOfMembers = group.getMembers().size();
        this.expenses = expenses;
        total_expenses = expenses.stream().mapToDouble(SplitTransaction::getBalance).sum();
        this.SplitTitle = SplitTitle;
    }

    public GroupEntity getGroup() {
        return group;
    }

    public void setGroup(GroupEntity group) {
        this.group = group;
    }

    public ArrayList<SplitTransaction> getExpenses() {
        return expenses;
    }

    public void setExpenses(ArrayList<SplitTransaction> balances) {
        this.expenses = balances;
    }

    public double getTotal_expenses() {
        return total_expenses;
    }

    public void setTotal_expenses(double total_expenses) {
        this.total_expenses = total_expenses;
    }

    public String getSplitTitle() {
        return SplitTitle;
    }

    public void setSplitTitle(String splitTitle) {
        SplitTitle = splitTitle;
    }


    public HashMap<UserEntity, Double> getDebts() {
        return debts;
    }

    public void setDebts(HashMap<UserEntity, Double> debts) {
        this.debts = debts;
    }

    public int getNumOfMembers() {
        return numOfMembers;
    }

    public void setNumOfMembers(int numOfMembers) {
        this.numOfMembers = numOfMembers;
    }



    public void addNewTransaction(SplitTransaction newTran) {
        expenses.add(newTran);
        debts.forEach((userEntity, aDouble) -> {
            if (userEntity == newTran.getUserPaid()) {
                aDouble += newTran.getBalance() * newTran.getPercentageToBeReturned();
            } else
                aDouble += newTran.getBalance() * newTran.getPercentageEachPay();
        });

    }





}
