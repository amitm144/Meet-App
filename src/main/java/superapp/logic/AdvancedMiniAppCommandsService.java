package superapp.logic;

import superapp.boundaries.command.MiniAppCommandBoundary;

import java.util.List;

public interface AdvancedMiniAppCommandsService extends MiniAppCommandsService {

    public List<MiniAppCommandBoundary> getALlCommands(String userSuperapp, String email,int size,int page);
    public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName,String userSuperapp, String email,int size,int page);
    public void deleteALlCommands(String userSupperapp, String email);

}
