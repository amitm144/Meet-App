package com.superapp.controllers;

import com.superapp.boundaries.command.MiniAppCommandBoundary;
import com.superapp.boundaries.command.MiniAppCommandIdBoundary;
import com.superapp.logic.UsersService;

import com.superapp.logic.concreteServices.MiniAppCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController {

    private UsersService usersService;
    private MiniAppCommandService miniappService;
    @Autowired
    public void setMessageService(UsersService usersService) {
        this.usersService = usersService;
    }
    @Autowired
    public void setMiniAppCommandService(MiniAppCommandService MiniAppCommandService) {
        this.miniappService = MiniAppCommandService;
    }
    @RequestMapping(
            path= {"/superapp/admin/users"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Object getAllUsers () { return this.usersService.getAllUsers(); }

    @RequestMapping(
            path= {"/superapp/admin/miniapp"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Object exportMiniAppsCommands () { return this.miniappService.getALlCommands();}


    @RequestMapping(
            path= {"/superapp/admin/miniapp/{miniAppName}"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Object exportSpecificMiniAppsCommands(@PathVariable("miniAppName") String miniAppName) {
        return this.miniappService.getAllMiniAppCommands(miniAppName);
    }

    @RequestMapping(
                path= {"/superapp/admin/users"},
                method = {RequestMethod.DELETE})
    public void deleteUsers () { this.usersService.deleteAllUsers(); }

    @RequestMapping(
            path= {"/superapp/admin/objects"},
            method = {RequestMethod.DELETE})
    public void deleteObjects () {}

    @RequestMapping(
            path= {"/superapp/admin/miniapp"},
            method = {RequestMethod.DELETE})
    public void deleteMiniApp () {this.miniappService.deleteALlCommands();}
}
