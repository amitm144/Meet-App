package com.superapp.boundaries.data.split;

import com.superapp.boundaries.data.UserEntity;
import com.superapp.boundaries.data.Group.GroupEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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

    public GroupSplitEntity(GroupEntity group, String SplitTitle) {
        this.group = group;
        this.numOfMembers = group.getMembers().size();
        total_expenses = expenses.stream().mapToDouble(SplitTransaction::getBalance).sum();
        this.SplitTitle = SplitTitle;
        initSplitTransactions();
    }
    public GroupEntity getGroup() {
        return group;
    }

    public void initSplitTransactions(){
        this.expenses = new ArrayList<SplitTransaction>();
        for (UserEntity user:this.group.getMembers()) {
            SplitTransaction trans = new SplitTransaction(this.group,user,new Date(),this.SplitTitle,0);
            this.expenses.add(trans);
        }
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
        this.expenses.add(newTran);
        this.debts.forEach((userEntity, aDouble) -> {
            if (userEntity == newTran.getUserPaid()) {
                aDouble += newTran.getBalance() * newTran.getPercentageToBeReturned();
            } else
                aDouble += newTran.getBalance() * newTran.getPercentageEachPay();
        });
    }

    public SplitTransaction getUserTransaction(UserEntity checkUser) {
        for (SplitTransaction trans:this.expenses)
            if(trans.getUserPaid().equals(checkUser)) return trans;
        return null;
    }
}
