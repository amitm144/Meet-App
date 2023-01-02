package superapp.logic;

import superapp.boundaries.split.SplitDebtBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.data.SuperAppObjectEntity;

public interface SplitsService {
 public void settleGroupDebts(SuperAppObjectEntity group);
 public SplitDebtBoundary showDebt(SuperAppObjectEntity.SuperAppObjectId group, UserIdBoundary user);
 public Object showAllDebts(SuperAppObjectEntity group);
}