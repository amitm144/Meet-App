package superapp.logic;

import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.data.SuperAppObjectEntity;
import superapp.util.wrappers.SuperAppObjectIdWrapper;
import superapp.util.wrappers.UserIdWrapper;

import java.util.Map;

public interface ServicesFactory {
   public void setObjectDetails(SuperAppObjectBoundary object);
   public void updateObjectDetails(SuperAppObjectEntity object);
   public void runCommand(String miniapp , SuperAppObjectIdWrapper targetObject , UserIdWrapper user , Map<String,Object> attributes , String commandCase);

}
