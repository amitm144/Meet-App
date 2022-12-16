package com.superapp.logic;

import com.superapp.data.Group.SplitGroupEntity;
import com.superapp.data.GroupEntity;
import com.superapp.data.UserEntity;
import com.superapp.data.split.SplitTransaction;

public interface SplitsService {
 public void openNewSplitGroup(GroupEntity group, String title);
 public void openNewTransaction(GroupEntity group, UserEntity payedUser, String description, double splitbalance);
 public void payDebt(GroupEntity group,UserEntity user);
 public double showDebt(GroupEntity group,UserEntity user);
 public void computeTransactionBalance(SplitGroupEntity group);
 public  void removeTransaction(UserEntity user,GroupEntity group, SplitTransaction transaction);
 public void updateTransaction(UserEntity user,GroupEntity group, SplitTransaction transaction,double updated_payment);
}
