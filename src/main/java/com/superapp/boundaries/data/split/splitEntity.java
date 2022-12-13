package com.superapp.boundaries.data.split;

import com.superapp.boundaries.data.MiniAppCommandEntity;
import com.superapp.boundaries.data.UserEntity;
import com.superapp.boundaries.data.split.Group.GroupEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class splitEntity {

    private HashMap<GroupSplitEntity,Double> groups;


    public splitEntity(){
        this.groups = new HashMap<GroupSplitEntity,Double>();
    }

//    GroupEntity group ;
//    ArrayList<SplitTransaction> expenses;
//    HashMap<UserEntity,Double> debts ;
//    double total_expenses;
//    String SplitTitle;
//    int numOfMembers;

    public void openNewGroup(List<UserEntity> users ,String SplitTitle ){

        GroupSplitEntity newGroup = new GroupSplitEntity()

        this.groups.put(new GroupEntity( new HashMap<UserEntity,Double>group.getMembers(),));
    }


//    private void computeBalances(){
//        for (GroupSplitEntity splitGroup:groups.keySet()) { // total
//            double total_expenses = splitGroup.getTotal_expenses();
//            for (UserEntity user: splitGroup.getBalances()) {
//               double balance = splitGroup.getBalances().get(user);
//               double new_balance = balance - total_expenses / splitGroup.getGroup().getMembers().size();
//                splitGroup.getBalances().put(user,new_balance);
//            }
//        }
//    }
//

//
//
//    private void computeBalances(){
//        for (GroupSplitEntity splitGroup:groups.keySet()) { // total
//            double total_expenses = splitGroup.getTotal_expenses();
//            for (UserEntity user: splitGroup.getBalances()) {
//                double balance = splitGroup.getBalances().get(user);
//                double new_balance = balance - total_expenses / splitGroup.getGroup().getMembers().size();
//                splitGroup.getBalances().put(user,new_balance);
//            }
//        }
//    }
//    export function computeBalances(allExpensesObject, allMembers, allDonePaymentsObject) {
//        let allExpenses = Object.entries(allExpensesObject);
//        let allDonePayments = Object.entries(allDonePaymentsObject);
//        if (!allExpenses || allMembers.length === 0)
//            return [];
//
//        let total = allExpenses.map((x) => x[1].amount).reduce((a, b) => a + b, 0);
//        let numMembers = allMembers.length;
//        let eachUserBalance = {};
//        let payments = {}; // payments["charles"] = [["cryptoboid", 10]]
//
//        for (const member of allMembers) { // if (member[1] === NaN) return {};
//            eachUserBalance[member[0]] = 0;
//            payments[member[0]] = [];
//        }
//        // console.debug("after members! ===============", eachUserBalance);
//
//        for (const expense of allExpenses) {
//        const payer = expense[1].paidBy;
//            if (eachUserBalance[payer] === undefined)
//                return [];
//
//            eachUserBalance[payer] += expense[1].amount;
//        }
//        for (let [usr, balance] of Object.entries(eachUserBalance)) {
//        eachUserBalance[usr] = balance - total / numMembers;
//    }
//
//    // console.debug(eachUserBalance);
//    return Object.entries(eachUserBalance).sort((a, b) => b[1] - a[1]);
}
