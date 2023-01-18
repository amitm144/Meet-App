package superapp.logic;

import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.object.SuperAppObjectBoundary;

import java.util.List;

public interface AdvancedMiniAppCommandsService extends MiniAppCommandsService {

    public List<MiniAppCommandBoundary> getAllCommands(String userSuperapp, String email,int size,int page);
    public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName,String userSuperapp, String email,int size,int page);
    public void deleteAllCommands(String userSupperapp, String email);
    public SuperAppObjectBoundary updateObjectCreationTimestamp(MiniAppCommandBoundary objectTimeTravel);
    public MiniAppCommandBoundary storeMiniAppCommand(MiniAppCommandBoundary miniappCommandBoundary);



}
