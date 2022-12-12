package com.superapp.logic;

import com.superapp.boundaries.command.MiniAppCommandBoundary;

import java.util.List;

public interface MiniAppCommandsService {
    public Object invokeCommand(MiniAppCommandBoundary command);
    public List<MiniAppCommandBoundary> getALlCommands();
    public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName);
    public void deleteALlCommands();
}
