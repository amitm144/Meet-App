package superapp.logic;

import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.data.SuperAppObjectEntity;
import superapp.data.UserEntity;
import superapp.data.split.SplitTransaction;

import java.util.Map;

public interface SplitsService {
 // public void openNewSplitGroup(GroupEntity group, String title);
 public void payDebt(SuperAppObjectEntity group, UserEntity user);

 public double showDebt(SuperAppObjectEntity group, UserEntity user);
 public SuperAppObjectBoundary computeTransactionBalance(SuperAppObjectBoundary trans);

 public double showAllDebts(SuperAppObjectEntity group, UserEntity user);

 Map<UserEntity,Double> showAllDebt(SuperAppObjectEntity group);
}