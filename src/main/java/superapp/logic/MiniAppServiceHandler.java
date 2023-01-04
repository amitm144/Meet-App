package superapp.logic;

import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.util.wrappers.SuperAppObjectIdWrapper;

public interface MiniAppServiceHandler {
   public void handleObjectByType(SuperAppObjectBoundary object);
   public Object runCommand(String miniapp,
                          SuperAppObjectIdWrapper targetObject,
                          UserIdBoundary invokedBy,
                          String commandCase);
}
