package superapp.logic;

import superapp.data.SuperAppObjectEntity;
import superapp.data.UserEntity;
import superapp.util.wrappers.UserIdWrapper;

public interface SplitsService {
 public void payAllDebts(SuperAppObjectEntity group, UserIdWrapper payingUser);
 public double showDebt(SuperAppObjectEntity group, UserIdWrapper user);
 public SuperAppObjectEntity computeTransactionBalance(SuperAppObjectEntity trans);
 public Object showAllDebt(SuperAppObjectEntity group);
 public void removeTransaction(UserIdWrapper user, SuperAppObjectEntity group, SuperAppObjectEntity transaction);
}