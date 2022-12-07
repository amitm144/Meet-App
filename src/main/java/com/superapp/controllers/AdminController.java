package com.superapp.controllers;

import com.superapp.boundaries.command.CommandBoundary;
import com.superapp.boundaries.command.CommandIdBoundary;
import com.superapp.boundaries.user.UserBoundary;
import com.superapp.logic.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController {

    private UsersService usersService;

    @Autowired
    public void setMessageService(UsersService usersService) {
        this.usersService = usersService;
    }

    @RequestMapping(
            path= {"/superapp/admin/users"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public UserBoundary[] createUsers () { return UserBoundary.getNRandomUsers(5); }

    @RequestMapping(
            path= {"/superapp/admin/miniapp"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public CommandBoundary[] exportMiniappsCommands () { return CommandBoundary.getNcommandBoundries(5); }

    @RequestMapping(
            path= {"/superapp/admin/miniapp/{miniAppName}"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public CommandBoundary[] exportSpecificMiniappsCommands(@PathVariable("miniAppName") String miniappName) {
        CommandBoundary[] c = CommandBoundary.getNcommandBoundries(1);
        CommandIdBoundary b = c[0].getCommandId();
        b.setMiniapp(miniappName);
        c[0].setCommandId(b);
        return c;
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
    public void deleteMiniapp () {}
}
