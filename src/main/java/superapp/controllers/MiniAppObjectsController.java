package superapp.controllers;

import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.command.MiniAppCommandIdBoundary;
import superapp.logic.concreteServices.MiniAppCommandService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class MiniAppObjectsController {

    private MiniAppCommandService miniAppCommandService;

    @Autowired
    public void MiniAppCommandService(MiniAppCommandService miniappservice) {
        this.miniAppCommandService = miniappservice;
    }

    @RequestMapping(
            path= {"/superapp/miniapp/{miniAppName}"},
            method = {RequestMethod.POST},
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Object invokeMiniAppCommand (@RequestBody MiniAppCommandBoundary command,
                                        @PathVariable("miniAppName") String miniAppName)
    {
        MiniAppCommandIdBoundary cId = new MiniAppCommandIdBoundary();
        cId.setMiniapp(miniAppName);
        command.setCommandId(cId); // user command is being sent without commandId
        return this.miniAppCommandService.invokeCommand(command);
    }

    @RequestMapping(
            path={"/superapp/miniapp/TEST"},
            method ={RequestMethod.POST},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Object testMiniAppCommandBoundary(
            @RequestBody MiniAppCommandBoundary miniAppCommandBoundary) {
        String commandId = miniAppCommandBoundary.getCommand();
        return switch (commandId) {
            case "objectTimeTravel" ->
                    this.miniAppCommandService.updateObjectCreationTimestamp(miniAppCommandBoundary);
            case "echo" ->
                    this.miniAppCommandService.storeMiniAppCommand(miniAppCommandBoundary);
            default -> null;
        };
    }
}
