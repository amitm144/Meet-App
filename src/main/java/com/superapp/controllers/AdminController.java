package com.superapp.controllers;

import com.superapp.boundaries.command.CommandBoundary;
import com.superapp.boundaries.command.CommandIdBoundary;
import com.superapp.boundaries.user.UserBoundary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController {
    @RequestMapping(
            path= {"/superapp/admin/users"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public UserBoundary[] createUsers () { return getNRandomUsers(5); }
    @RequestMapping(
            path= {"/superapp/admin/miniapp"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public CommandBoundary[] exportMiniAppsCommands () { return CommandBoundary.getNcommandBoundries(5); }
    @RequestMapping(
            path= {"/superapp/admin/miniapp/{miniAppName}"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public CommandBoundary[] exportSpecificMiniAppsCommands(@PathVariable("miniAppName") String miniAppName) {
        CommandBoundary[] c = CommandBoundary.getNcommandBoundries(1);
        CommandIdBoundary b = c[0].getCommandId();
        b.setMiniApp(miniAppName);
        c[0].setCommandId(b);
        return c;
    }
    @RequestMapping(
                path= {"/superapp/admin/users"},
                method = {RequestMethod.DELETE})
    public void deleteUsers () {}
    @RequestMapping(
            path= {"/superapp/admin/objects"},
            method = {RequestMethod.DELETE})
    public void deleteObjects () {}
    @RequestMapping(
            path= {"/superapp/admin/miniapp"},
            method = {RequestMethod.DELETE})
    public void deleteMiniApp () {}

    private UserBoundary[] getNRandomUsers(int n) {
        UserBoundary[] userBoundaries = new UserBoundary[n];
        for (int i = 0; i < n; i++) {
            userBoundaries[i] = new UserBoundary(
                    String.format("random%d@example.com", i),"example",
                    String.format("random%d", i), String.format("%d", i));
        }
        return userBoundaries;
    }
}
