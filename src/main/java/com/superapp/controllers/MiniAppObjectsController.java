package com.superapp.controllers;

import com.superapp.boundaries.command.MiniAppCommandBoundary;
import com.superapp.logic.concreteServices.MiniAppCommandService;
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
    command.getCommandId().setMiniApp(miniAppName);
        return this.miniAppCommandService.invokeCommand(command);
    }
}
