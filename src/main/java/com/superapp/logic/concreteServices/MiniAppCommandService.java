package com.superapp.logic.concreteServices;

import com.superapp.boundaries.command.MiniAppCommandBoundary;
import com.superapp.logic.MiniAppCommandsService;

import java.util.List;

public class MiniAppCommandService implements MiniAppCommandsService {
    @Override
    public Object invokeCommand(MiniAppCommandBoundary command) {
        return null;
    }

    @Override
    public List<MiniAppCommandBoundary> getALlCommands() {
        return null;
    }

    @Override
    public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName) {
        return null;
    }

    @Override
    public void deleteALlCommands() {

    }
}
