package superapp.logic;

import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.data.SuperAppObjectEntity;
import superapp.data.UserPK;
import superapp.util.wrappers.SuperAppObjectIdWrapper;

import java.util.Map;

public interface MiniAppServices {
   public void handleObjectByType(SuperAppObjectBoundary object);
   public Object runCommand(MiniAppCommandBoundary command);
   public void checkValidBinding(SuperAppObjectEntity parent, SuperAppObjectEntity child, UserPK userId);

}
