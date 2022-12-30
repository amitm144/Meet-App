package superapp.logic;

import superapp.data.SuperAppObjectEntity;
import superapp.data.UserEntity;
import superapp.util.wrappers.UserIdWrapper;

public interface SplitsService {
 // public void openNewSplitGroup(GroupEntity group, String title);
 public void payDebt(SuperAppObjectEntity group, UserIdWrapper user);

 public double showDebt(SuperAppObjectEntity group, UserIdWrapper user);
 public SuperAppObjectEntity computeTransactionBalance(SuperAppObjectEntity trans);
 public Object showAllDebt(SuperAppObjectEntity group);
}