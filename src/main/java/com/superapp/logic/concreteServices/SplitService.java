package com.superapp.logic.concreteServices;

import com.superapp.boundaries.command.MiniAppCommandBoundary;
import com.superapp.boundaries.split.GroupBoundary;
import com.superapp.boundaries.user.UserBoundary;
import com.superapp.converters.GroupConverter;
import com.superapp.converters.SplitGroupConverter;
import com.superapp.converters.UserConverter;
import com.superapp.data.GroupEntity;
import com.superapp.data.UserEntity;
import com.superapp.data.split.SplitGroupEntity;
import com.superapp.data.split.SplitTransaction;
import com.superapp.logic.SplitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Service
public class SplitService implements SplitsService {

        private SplitGroupConverter splitGroupConverter;
        private GroupConverter groupConverter;
        private UserConverter userConverter;
        private ArrayList<SplitGroupEntity> groups;

    @Autowired
        public SplitService(SplitGroupConverter splitGroupConverter ,GroupConverter groupConverter,UserConverter userConverter) {
            this.splitGroupConverter = splitGroupConverter;
            this.groupConverter = groupConverter;
            this.userConverter= userConverter;
            this.groups = new ArrayList<SplitGroupEntity>();
        }

    public void invokeCommand(MiniAppCommandBoundary command) {
            String commandCase = command.getCommand();
            GroupEntity group = this.groupConverter.toEntity((GroupBoundary) command.getCommandAttributes().get("group"));
            //UserId From DB
        switch (commandCase) {
                case "openNewSplitGroup": {
                    String title = (String) command.getCommandAttributes().get("title");
                    this.openNewSplitGroup(group, title);
                    break;
                }
                case "openNewTrasnaction": {
                    String description = (String) command.getCommandAttributes().get("description");
                    Double splitBalance = (Double) command.getCommandAttributes().get("splitBalance");
                    UserBoundary leader = (UserBoundary) command.getCommandAttributes().get("leader");
                    this.openNewTransaction(group, userConverter.toEntity(leader), description, splitBalance);//TODO add GET USER
                    break;
                }
                case "removeTrasnaction":
                    //TODO ADD removeTrasnacion
                    UserBoundary leader = (UserBoundary) command.getCommandAttributes().get("leader");

                    break;
                case "updateTransacion":
                    //TODO ADD updateTransacion
                    break;
                case "showDebt": {//TODO Show All USERS{
                    UserBoundary user = (UserBoundary) command.getCommandAttributes().get("user");
                    this.showDebt(group, userConverter.toEntity(user));
                    break;
                }
                case "payDebt": {
                    UserBoundary user = (UserBoundary) command.getCommandAttributes().get("user");
                    this.payDebt(group, userConverter.toEntity(user));
                    break;
                }
                default:
                    throw new RuntimeException("Command Not Found");
            }
    }

    @Override
    public void openNewSplitGroup(GroupEntity group, String title) {
        SplitGroupEntity newGroup = new SplitGroupEntity(group, title);
        this.groups.add(newGroup);
    }

    @Override
    public void openNewTransaction(GroupEntity group, UserEntity payedUser, String description, double splitbalance){
        SplitGroupEntity split_group = getGroupSplit(group);
        if(split_group == null )throw new RuntimeException("Incorrect Group!!!!!");
        SplitTransaction trans = new SplitTransaction(group,payedUser,new Date(),description,splitbalance);
        split_group.addNewTransaction(trans);//  Example : Payed user : 200,Not payed :0,  Not payed :0,Not payed :0
        computeTransacionBalance(split_group);//Example : Payed user : 150,Not payed :-50,  Not payed :-50,Not payed :-50
    }

    @Override
    public double showDebt(GroupEntity group, UserEntity user) {
        SplitGroupEntity split_group = getGroupSplit(group);
        for (SplitTransaction trans:split_group.getExpenses()) {
            HashMap<UserEntity, Double> debt = trans.getGroupDebts();
            if(debt.get(user)!=null)
                return debt.get(user);
        }
        throw new RuntimeException("User Not Found");
    }

    private SplitGroupEntity getGroupSplit(GroupEntity group){
        for (SplitGroupEntity g:this.groups) {
            if(group.equals(g.getGroup())) return g;
        }
        return null;
    }
    @Override
    public void payDebt(GroupEntity group,UserEntity user) {//Example : Payed user : 150,Not payed :-50,  Not payed :-50,Not payed :-50
        SplitGroupEntity split_group = getGroupSplit(group);
        for (SplitTransaction trans:split_group.getExpenses()) {
            HashMap<UserEntity, Double> debt = trans.getGroupDebts();
            if(debt.get(user)!=null &&trans.isOpen()) {
                trans.ComputeBank(user); //Example : Payed user : 100,Not payed :0,  Not payed :-50,Not payed :-50
            }
        }
    }
    @Override
    public void computeTransacionBalance(SplitGroupEntity group) { // total_compute_per_group
        for (UserEntity user:group.getGroup().getAllUsers()){
            for (SplitTransaction trans:group.getExpenses()) {
                HashMap<UserEntity, Double> debt = trans.getGroupDebts();
                double balance = debt.get(user);
                double new_balance = balance -trans.getOriginalPayment() / group.getGroup().getAllUsers().size();
                debt.put(user,new_balance);
            }
        }
    }
}
