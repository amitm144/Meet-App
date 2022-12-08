package com.superapp.logic.concreteServices;

import com.superapp.boundaries.command.MiniAppCommandBoundary;
import com.superapp.converters.MiniappCommandConverter;
import com.superapp.data.MiniAppCommandEntity;
import com.superapp.logic.MiniAppCommandsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class MiniAppCommandService implements MiniAppCommandsService {
    private MiniappCommandConverter miniAppConverter;
    private Map<String, ArrayList<MiniAppCommandEntity>> miniAppsCommands; // { miniapp: miniAppCommand }
    @Autowired
    public MiniAppCommandService(MiniappCommandConverter miniAppConverter) {
        this.miniAppConverter = miniAppConverter;
    }
    @PostConstruct
    public void setup() {
        this.miniAppsCommands = Collections.synchronizedMap(new HashMap<>());
    }

    @Override
    public Object invokeCommand(MiniAppCommandBoundary command) {

        if(miniAppsCommands.get(command.getCommandId().getMiniapp()) == null){
            ArrayList<MiniAppCommandEntity> commandList = new ArrayList<MiniAppCommandEntity>();
            commandList.add(this.miniAppConverter.toEntity(command));
            miniAppsCommands.put(command.getCommandId().getMiniapp(),commandList);
        }
        else
            miniAppsCommands.get(command.getCommandId().getMiniapp()).add(this.miniAppConverter.toEntity(command));
        return command;
    }

    @Override
    public List<MiniAppCommandBoundary> getALlCommands() {
        ArrayList<MiniAppCommandBoundary> rv = new ArrayList<>();
        for (ArrayList<MiniAppCommandEntity> mini_app_command_list: this.miniAppsCommands.values())
            for (MiniAppCommandEntity command: mini_app_command_list)
                rv.add(this.miniAppConverter.toBoundary(command));
        return rv;
    }

    @Override
    public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName) {
        ArrayList<MiniAppCommandEntity> mini_app_command_list = this.miniAppsCommands.get(miniAppName);
        if(mini_app_command_list == null)
            throw new RuntimeException("Unknown miniApp");
        ArrayList<MiniAppCommandBoundary> rv = new ArrayList<>();
        for (MiniAppCommandEntity command:mini_app_command_list) {
            rv.add(this.miniAppConverter.toBoundary(command));
        }
        return rv;
    }

    @Override
    public void deleteALlCommands() {
        this.miniAppsCommands.clear();
    }
}
