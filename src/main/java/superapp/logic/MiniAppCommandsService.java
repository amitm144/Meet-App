package superapp.logic;

import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.object.SuperAppObjectBoundary;

import java.util.List;

public interface MiniAppCommandsService {
    public Object invokeCommand(MiniAppCommandBoundary command);
    @Deprecated
    public List<MiniAppCommandBoundary> getAllCommands();
    @Deprecated
    public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName);
    @Deprecated
    public void deleteAllCommands();
    public SuperAppObjectBoundary updateObjectCreationTimestamp(String userSuperapp, String userEmail,
                                                                MiniAppCommandBoundary objectTimeTravel);
}
