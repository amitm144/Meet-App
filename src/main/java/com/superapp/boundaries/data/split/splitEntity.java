package com.superapp.boundaries.data.split;
import com.superapp.boundaries.data.UserEntity;
import com.superapp.boundaries.data.Group.GroupEntity;
import com.superapp.boundaries.user.UserBoundary;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

public class splitEntity {

    private ArrayList<GroupSplitEntity> groups;

    public splitEntity() {
        this.groups = new ArrayList<GroupSplitEntity>();
    }// TODO Load From DB

    public void openNewGroup(GroupEntity group, String title) {
        GroupSplitEntity newGroup = new GroupSplitEntity(group, title);
        this.groups.add(newGroup);
    }
    public void openNewSplit(GroupEntity group,UserEntity payedUser,String description,double splitbalance){
        GroupSplitEntity split_group = getGroupSplit(group);
        if(split_group == null )throw new RuntimeException("Incorrect Group!!!!!");
        SplitTransaction trans = new SplitTransaction(group,payedUser,new Date(),description,splitbalance);
        split_group.addNewTransaction(trans);
    }
    private GroupSplitEntity getGroupSplit(GroupEntity group){
        for (GroupSplitEntity g:this.groups) {
            if(group.equals(g.getGroup())) return g;
        }
        return null;
    }
    public void payDebt(GroupEntity group,UserEntity user, double amount) {
        GroupSplitEntity split_group = getGroupSplit(group);
        if(split_group == null )throw new RuntimeException("Incorrect Group!!!!!");
        for (UserEntity findUser:split_group.getDebts().keySet()) {
            if( user.equals(findUser)) {
                double oldDebt = split_group.getDebts().get(user);
                split_group.getDebts().put(findUser,oldDebt-amount);
            }
        }
    }

    public void computeBalancesPerGroup(GroupSplitEntity group) { // total_compute_per_group
        Set<UserEntity> allUsers = group.getDebts().keySet();
        for (UserEntity user : allUsers) {
            double balance = group.getDebts().get(user);
            double new_balance = balance - group.getTotal_expenses() / group.getNumOfMembers();
            group.getDebts().put(user,new_balance);
        }
    }

    public void computeTotalBalances() { // total_compute
        for (GroupSplitEntity group : this.groups) {
            computeBalancesPerGroup(group);
        }
    }
}
