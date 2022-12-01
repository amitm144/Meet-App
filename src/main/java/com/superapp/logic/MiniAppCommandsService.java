package com.superapp.logic;

import com.superapp.boundaries.command.MiniAppCommandBoundary;

import java.util.List;

public interface MiniAppCommandsService {
    Object invokeCommand(MiniAppCommandBoundary command);
    List<MiniAppCommandBoundary> getALlCommands();
    List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName);
    void deleteALlCommands();
}
