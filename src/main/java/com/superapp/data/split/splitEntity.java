package com.superapp.data.split;
import com.superapp.data.UserEntity;
import com.superapp.data.Group.GroupEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class splitEntity implements SplitCommand{

    private ArrayList<GroupSplitEntity> groups;

    public splitEntity() {
        this.groups = new ArrayList<GroupSplitEntity>();
    }// TODO Load From DB

    public void openNewSplitGroup(GroupEntity group, String title) {
        GroupSplitEntity newGroup = new GroupSplitEntity(group, title);
        this.groups.add(newGroup);
    }
    public void openNewTrasnaction(GroupEntity group, UserEntity payedUser, String description, double splitbalance){
        GroupSplitEntity split_group = getGroupSplit(group);
        if(split_group == null )throw new RuntimeException("Incorrect Group!!!!!");
        SplitTransaction trans = new SplitTransaction(group,payedUser,new Date(),description,splitbalance);
        split_group.addNewTransaction(trans);
    }

    @Override
    public double showDebt(GroupEntity group, UserEntity user) {
        GroupSplitEntity split_group = getGroupSplit(group);
        for (SplitTransaction trans:split_group.getExpenses()) {
            HashMap<UserEntity, Double> debt = trans.getGroupDebts();
            if(debt.get(user)!=null)
                    return debt.get(user);
        }
        throw new RuntimeException("User Not Found");
    }

    private GroupSplitEntity getGroupSplit(GroupEntity group){
        for (GroupSplitEntity g:this.groups) {
            if(group.equals(g.getGroup())) return g;
        }
        return null;
    }
    public void payDebt(GroupEntity group,UserEntity user) {
        GroupSplitEntity split_group = getGroupSplit(group);
        for (SplitTransaction trans:split_group.getExpenses()) {
            HashMap<UserEntity, Double> debt = trans.getGroupDebts();
            if(debt.get(user)!=null) {
                debt.put(user,0.0); // Payed
                return;
            }
        }
        throw new RuntimeException("User Not Found");
    }

    public void computeBalancesPerGroup(GroupSplitEntity group) { // total_compute_per_group

        for (UserEntity user:group.getGroup().getMembers()){
            for (SplitTransaction trans:group.getExpenses()) {
                HashMap<UserEntity, Double> debt = trans.getGroupDebts();
                double balance = debt.get(user);
                double new_balance = balance -trans.getOriginalPayment() / group.getGroup().getMembers().size();
                debt.put(user,new_balance);
            }
        }
    }
        public void computeTotalBalances() { // total_compute
        for (GroupSplitEntity group : this.groups) {
            computeBalancesPerGroup(group);
        }
    }
}
