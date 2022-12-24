package superapp.logic;

import superapp.data.Group.SplitGroupEntity;
import superapp.data.GroupEntity;
import superapp.data.UserEntity;
import superapp.data.split.SplitTransaction;

public interface SplitsService {
 public void openNewSplitGroup(GroupEntity group, String title);
 public void openNewTransaction(GroupEntity group, UserEntity payedUser, String description, double splitbalance);
 public void payDebt(GroupEntity group,UserEntity user);
 public double showDebt(GroupEntity group,UserEntity user);
 public void computeTransactionBalance(SplitGroupEntity group);
 public  void removeTransaction(UserEntity user,GroupEntity group, SplitTransaction transaction);
 public void updateTransaction(UserEntity user,GroupEntity group, SplitTransaction transaction,double updated_payment);
}
