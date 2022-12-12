package com.superapp.boundaries.data.split;

import com.superapp.boundaries.command.MiniAppCommandBoundary;
import com.superapp.boundaries.data.MiniAppCommandEntity;
import com.superapp.boundaries.data.UserEntity;
import com.superapp.boundaries.data.Group.GroupEntity;

import java.util.ArrayList;
import java.util.HashMap;

public class splitEntity {

    private ArrayList<GroupSplitEntity> groups;

    public splitEntity() {
        this.groups = new ArrayList<GroupSplitEntity>();
    }// TODO Load From DB

    public void openNewGroup(GroupEntity group, String title) {
        GroupSplitEntity newGroup = new GroupSplitEntity(group, title);
        this.groups.add(newGroup);
    }

    public void payDebt(UserEntity user, double transID, double amount) {
        for (GroupSplitEntity group : groups) {
            for (SplitTransaction transaction : group.getExpenses()) {
                UserEntity payed_user = transaction.getUserPaid();
                if (payed_user.equals(user)) {
                    transaction.setBalance(transaction.getBalance() - amount);
                    computeBalancesPerGroup(group);
                }
            }
        }
    }
    public void computeBalancesPerGroup(GroupSplitEntity group) { // total_compute_per_group
        for (UserEntity user : group.getGroup().getMembers()) {
            SplitTransaction users_trans = group.getUserTransaction(user);
            double balance = users_trans.getBalance();
            double new_balance = balance - group.getTotal_expenses() / group.getNumOfMembers();
            users_trans.setBalance(new_balance);
        }
    }

    public void computeBalances() { // total_compute
        for (GroupSplitEntity group : this.groups) {
            computeBalancesPerGroup(group);
        }
    }
}
