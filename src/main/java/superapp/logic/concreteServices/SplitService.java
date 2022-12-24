package superapp.logic.concreteServices;

import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.split.GroupBoundary;
import superapp.boundaries.user.UserBoundary;
import superapp.converters.GroupConverter;
import superapp.converters.SplitGroupConverter;
import superapp.converters.UserConverter;
import superapp.data.Group.SplitGroupEntity;
import superapp.data.GroupEntity;
import superapp.data.UserEntity;
import superapp.data.split.SplitTransaction;
import superapp.logic.SplitsService;
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
            case "openNewTransaction": {
                String description = (String) command.getCommandAttributes().get("description");
                Double splitBalance = (Double) command.getCommandAttributes().get("splitBalance");
                UserBoundary user = (UserBoundary) command.getCommandAttributes().get("user");
                this.openNewTransaction(group, userConverter.toEntity(user), description, splitBalance);//TODO add GET USER
                break;
            }
            case "removeTransaction":{
                //TODO ADD removeTrasnacion{
                UserBoundary user = (UserBoundary) command.getCommandAttributes().get("user");
                String transId = (String) command.getCommandAttributes().get("transactionId");
                SplitTransaction trans = null;//DBfindByID
                this.removeTransaction(this.userConverter.toEntity(user), group, trans);
                    break;
            }
            case "updateTransaction": {
                //TODO ADD updateTransacion
                UserBoundary user = (UserBoundary) command.getCommandAttributes().get("user");
                String transId = (String) command.getCommandAttributes().get("transactionId");
                SplitTransaction trans = null;//DBfindByID
                double updated_payment = (double) command.getCommandAttributes().get("payment");
                this.updateTransaction(this.userConverter.toEntity(user), group, trans,updated_payment);
                break;
            }
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
        computeTransactionBalance(split_group);//Example : Payed user : 150,Not payed :-50,  Not payed :-50,Not payed :-50
    }

    @Override
    public double showDebt(GroupEntity group, UserEntity user) {
        SplitGroupEntity split_group = getGroupSplit(group);
        double allDebt = 0;
        for (SplitTransaction trans:split_group.getExpenses()) {
            if(trans.getGroupDebts().containsKey(user))
                allDebt+=trans.getGroupDebts().get(user);
        }
        return allDebt;
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
    public void computeTransactionBalance(SplitGroupEntity group) { // total_compute_per_group
        for (UserEntity user:group.getGroup().getAllUsers()){
            for (SplitTransaction trans:group.getExpenses()) {
                HashMap<UserEntity, Double> debt = trans.getGroupDebts();
                double balance = debt.get(user);
                double new_balance = balance -trans.getOriginalPayment() / group.getGroup().getAllUsers().size();
                debt.put(user,new_balance);
            }
        }
    }

    @Override
    public void removeTransaction(UserEntity user,GroupEntity group, SplitTransaction transaction) {

        if(!user.equals(transaction.getUserPaid()))
            throw new RuntimeException("Only the payer can remove the transaction");
        if(transaction.getGroupDebts().get(user) != transaction.getOriginalPayment())
            throw new RuntimeException("Cannot close payment , Atleast one user has been paid");
        SplitGroupEntity split_group = getGroupSplit(group);
        for (UserEntity trans_user : transaction.getGroupDebts().keySet())
            transaction.getGroupDebts().put(trans_user,0.0);

        transaction.setOpen(false);
        split_group.getExpenses().remove(transaction);
    }

    @Override
    public void updateTransaction(UserEntity user, GroupEntity group, SplitTransaction transaction,double updated_payment) {
        SplitGroupEntity split_group = getGroupSplit(group);
        if(!user.equals(transaction.getUserPaid()))
            throw new RuntimeException("Only the payer can remove the transaction");
        if(transaction.getGroupDebts().get(user) != transaction.getOriginalPayment())
            throw new RuntimeException("Cannot close payment , Atleast one user has been paid");

        for (UserEntity trans_user : transaction.getGroupDebts().keySet())
            transaction.getGroupDebts().put(trans_user,0.0);
        transaction.getGroupDebts().put(user,updated_payment);
        computeTransactionBalance(split_group);
    }
}
