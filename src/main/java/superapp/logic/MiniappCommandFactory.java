package superapp.logic;

import superapp.util.wrappers.SuperAppObjectIdWrapper;
import superapp.util.wrappers.UserIdWrapper;

import java.util.Map;

public interface MiniappCommandFactory {
	void runCommand(String miniapp , SuperAppObjectIdWrapper targetObject , UserIdWrapper  user , Map<String,Object> attributes , String commandCase);
}
