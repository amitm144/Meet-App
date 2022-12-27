package superapp.logic;

import superapp.data.SuperAppObjectEntity;
import superapp.data.UserEntity;
import superapp.data.split.SplitTransaction;

public interface SplitsService {
// public void openNewSplitGroup(GroupEntity group, String title);
 public void openNewTransaction(SuperAppObjectEntity group, UserEntity payedUser, String description, double splitbalance);
 public void payDebt(SuperAppObjectEntity group,UserEntity user);
 public double showDebt(SuperAppObjectEntity group,UserEntity user);
// public void computeTransactionBalance(SplitGroupEntity group);
 public  void removeTransaction(UserEntity user,SuperAppObjectEntity group, SplitTransaction transaction);
 public void updateTransaction(UserEntity user,SuperAppObjectEntity group, SplitTransaction transaction,double updated_payment);
}
