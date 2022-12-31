package superapp.logic;

import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.data.SuperAppObjectEntity;
import superapp.util.wrappers.SuperAppObjectIdWrapper;

public interface ServicesFactory {
   public void handleObjectByType(SuperAppObjectBoundary object);
   public void updateObjectDetails(SuperAppObjectEntity object);
   public Object runCommand(String miniapp,
                          SuperAppObjectIdWrapper targetObject,
                          UserIdBoundary invokedBy,
                          String commandCase);
}
