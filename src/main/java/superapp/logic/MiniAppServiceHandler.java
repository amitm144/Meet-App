package superapp.logic;

import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.util.wrappers.SuperAppObjectIdWrapper;

public interface MiniAppServiceHandler {
   public void handleObjectByType(SuperAppObjectBoundary object);
   public Object runCommand(MiniAppCommandBoundary command);
}
