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
    private SplitService splitService;
    private Map<String, ArrayList<MiniAppCommandEntity>> miniAppsCommands; // { miniapp: miniAppCommand }
    @Autowired
    public MiniAppCommandService(MiniappCommandConverter miniAppConverter) {
        this.miniAppConverter = miniAppConverter;
    }
    @Autowired
    public void setMiniAppCommandService(SplitService splitService) {
        this.splitService = splitService;
    }
    @PostConstruct
    public void setup() {
        this.miniAppsCommands = Collections.synchronizedMap(new HashMap<>());
    }

    @Override
    public Object invokeCommand(MiniAppCommandBoundary command) {
        if (miniAppsCommands.get(command.getCommandId().getMiniapp()) == null) {// TODO Check if miniapp is one of the miniapps
            ArrayList<MiniAppCommandEntity> commandList = new ArrayList<MiniAppCommandEntity>();
            invokeCommandAtMiniapp(command, command.getCommandId().getMiniapp());
            commandList.add(this.miniAppConverter.toEntity(command));
            miniAppsCommands.put(command.getCommandId().getMiniapp(),commandList);
        } else {
            invokeCommandAtMiniapp(command, command.getCommandId().getMiniapp());
            miniAppsCommands.get(command.getCommandId().getMiniapp()).add(this.miniAppConverter.toEntity(command));
        }
        return command;
    }
    private void invokeCommandAtMiniapp(MiniAppCommandBoundary command, String miniapp){
        if (miniapp.equals("Split"))
            splitService.invokeCommand(command);
        else
        throw new RuntimeException("Unknown miniApp");
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
