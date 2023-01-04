package superapp.logic;

import superapp.boundaries.user.UserIdBoundary;
import superapp.data.SuperAppObjectEntity;

public interface SplitsService {
 public void settleGroupDebts(SuperAppObjectEntity group);
 public float showDebt(SuperAppObjectEntity group, UserIdBoundary user);
 public Object showAllDebts(SuperAppObjectEntity group);
}