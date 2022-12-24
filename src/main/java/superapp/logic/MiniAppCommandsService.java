package superapp.logic;

import superapp.boundaries.command.MiniAppCommandBoundary;

import java.util.List;

public interface MiniAppCommandsService {
    Object invokeCommand(MiniAppCommandBoundary command);
    List<MiniAppCommandBoundary> getALlCommands();
    List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName);
    void deleteALlCommands();
}
