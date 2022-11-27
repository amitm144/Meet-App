package com.superapp.logic.concreteServices;

import com.superapp.boundaries.command.MiniAppCommandBoundary;
import com.superapp.converters.MiniappCommandConverter;
import com.superapp.data.MiniAppCommandEntity;
import com.superapp.logic.MiniAppCommandsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MiniAppCommandService implements MiniAppCommandsService {
    private MiniappCommandConverter miniAppConverter;
    private Map<String, MiniAppCommandEntity> miniAppsCommands; // { miniapp: miniAppCommand }

    public MiniAppCommandService(MiniappCommandConverter miniAppConverter) {
        this.miniAppConverter = miniAppConverter;
    }

    @Override
    public Object invokeCommand(MiniAppCommandBoundary command) {
        miniAppsCommands.put(command.getCommandId().getMiniApp(),this.miniAppConverter.toEntity(command));
        return command;
    }

    @Override
    public List<MiniAppCommandBoundary> getALlCommands() {
        ArrayList<MiniAppCommandBoundary> rv = new ArrayList<>();
        for (MiniAppCommandEntity mini_app_command: this.miniAppsCommands.values()) {
            rv.add(this.miniAppConverter.toBoundary(mini_app_command));
        }
        return rv;
    }

    @Override
    public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName) {
        MiniAppCommandEntity mini_app = this.miniAppsCommands.get(miniAppName);
        if(mini_app == null)
            throw new RuntimeException("Unknown miniApp");
        ArrayList<MiniAppCommandBoundary> rv = new ArrayList<>();
        rv.add(this.miniAppConverter.toBoundary(mini_app));
        return rv;
    }

    @Override
    public void deleteALlCommands() {
        this.miniAppsCommands.clear();
    }
}
