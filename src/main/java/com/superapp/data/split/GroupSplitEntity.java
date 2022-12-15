package com.superapp.data.split;

import com.superapp.data.Group.GroupEntity;

import java.util.ArrayList;

public class GroupSplitEntity {

    private GroupEntity group ;
    private ArrayList<SplitTransaction> expenses;
    private String SplitTitle;

    public GroupSplitEntity() {
    }

    public GroupSplitEntity(GroupEntity group, String SplitTitle) {
        this.group = group;
        this.SplitTitle = SplitTitle;
        this.expenses = new ArrayList<SplitTransaction>();
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

    public String getSplitTitle() {
        return SplitTitle;
    }

    public void setSplitTitle(String splitTitle) {
        SplitTitle = splitTitle;
    }


    public void addNewTransaction(SplitTransaction newTran) {
        this.expenses.add(newTran);
    }
}
