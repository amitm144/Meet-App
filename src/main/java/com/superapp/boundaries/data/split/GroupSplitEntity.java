package com.superapp.boundaries.data.split;

import com.superapp.boundaries.data.UserEntity;
import com.superapp.boundaries.data.split.Group.GroupEntity;

import java.util.ArrayList;

public class GroupSplitEntity {

    private GroupEntity group ;
    private ArrayList<SplitTransaction> balances;
    private double total_expenses;
    private String SplitTitle;

    public GroupSplitEntity() {
    }
// TODO add DB first
//    public GroupSplitEntity(GroupEntity group) {
//        this.group = group;
//        total_expenses = 0.0;
//        this.balances = new ArrayList<SplitTransaction>();
//
//    }

    public GroupSplitEntity(GroupEntity group ,  ArrayList<SplitTransaction> balances,String SplitTitle) {
        this.group = group;
        this.balances = balances;
        total_expenses = balances.stream().reduce((double) 0, Double::sum);
        this.SplitTitle= SplitTitle;
    }

    public double getTotal_expenses() {
        return total_expenses;
    }

    public void setTotal_expenses(double total_expenses) {
        this.total_expenses = total_expenses;
    }

    public GroupEntity getGroup() {
        return group;
    }

    public void setGroup(GroupEntity group) {
        this.group = group;
    }

    public ArrayList<SplitTransaction> getBalances() {
        return balances;
    }

    public void setBalances(ArrayList<SplitTransaction> balances) {
        this.balances = balances;
    }

    public String getSplitTitle() {
        return SplitTitle;
    }

    public void setSplitTitle(String splitTitle) {
        SplitTitle = splitTitle;
    }
}
